package database.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import shared.model.Field;



public class FieldDAO {
	private Database parentDatabase;
	
	
	public FieldDAO(Database tempDatabase) {
		parentDatabase = tempDatabase;
	}
	/**Adds a field to the database if it is not already present.
	 * 
	 * @param inputField Field object to add to the database.
	 * @throws DatabaseException 
	 */
	public void addField(Field inputField) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into FIELD (PROJECTID, HELPPATH, KNOWNDATAFILE, XCORD, WIDTH, COLNUMBER, TITLE) values (?, ?, ?, ?, ?, ?, ?)";
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			stmt.setInt(1, inputField.getProjectID());
			stmt.setString(2, inputField.getHelpHtml());
			stmt.setString(3, inputField.getKnownData());
			stmt.setInt(4, inputField.getxCord());
			stmt.setInt(5, inputField.getWidth());
			stmt.setInt(6, inputField.getColNumber());
			stmt.setString(7, inputField.getTitle());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = parentDatabase.getCurrentConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				inputField.setID(id);
			}
			else {
				throw new DatabaseException("Could not insert");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	/**Gets the list of all fields coresponding to a project iD
	 * 
	 * @param projectID The project whose ID we are looking for.
	 * @return A list containing object versions of all the fields corresponding to the projects in our database.
	 * @throws DatabaseException 
	 */
	public List<Field> getFields(int inputProject) throws DatabaseException{
		ArrayList<Field> result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select ID, HELPPATH, KNOWNDATAFILE, XCORD, WIDTH, COLNUMBER, TITLE from FIELD where PROJECTID= "+inputProject;
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);

			rs = stmt.executeQuery();
			while (rs.next()) {
				if(result == null){
					result = new ArrayList<Field>();
				}
				int id = rs.getInt(1);
				String helpPath = rs.getString(2);
				String knownDataFile = rs.getString(3);
				int xCord = rs.getInt(4);
				int width = rs.getInt(5);
				int colNum = rs.getInt(6);
				String title = rs.getString(7);
				result.add(new Field(id,inputProject,title,xCord,width,helpPath,knownDataFile,colNum));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

			
		return result;	
		}
	/**Searches the database for a specific field
	 * 
	 * @param fieldID Field to search for.
	 * @return the field indicated by the input as an object or null if it cannot be found.
	 * @throws DatabaseException 
	 */
	public Field getField(Field inputField) throws DatabaseException{
		Field output=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select PROJECTID, HELPPATH, KNOWNDATAFILE, XCORD, WIDTH, COLNUMBER, TITLE from FIELD where ID="+inputField.getID();
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = inputField.getID();
				int projectID = rs.getInt(1);
				String helpPath = rs.getString(2);
				String knownDataFile = rs.getString(3);
				int xCord = rs.getInt(4);
				int width = rs.getInt(5);
				int colNumber = rs.getInt(6);
				String title = rs.getString(7);
				output = new Field(id, projectID, title, xCord, width, helpPath, knownDataFile, colNumber);
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
			
		return output;	
	}
}
