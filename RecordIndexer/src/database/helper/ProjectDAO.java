package database.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.helper.Database;
import database.helper.DatabaseException;
import shared.model.Project;
import shared.model.ProjectTuple;


public class ProjectDAO {
	private Database parentDatabase;
	
	
	public ProjectDAO(Database testData) {
		parentDatabase = testData;
	}
	/**Gets a list of tuples storing the project name and its ID number.
	 * 
	 * @return A tuple list of all the projects in the database.
	 */
	public List<ProjectTuple> getAllProjects()throws DatabaseException{
		ArrayList<ProjectTuple> result = new ArrayList<ProjectTuple>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select ID, NAME from PROJECT";
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);

			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);

				result.add(new ProjectTuple(name, id));
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
	/**Searches the database for the given project. If it can't be found, it is added to the database.
	 * 
	 * @param inputProject Project to add to the database.
	 * @throws DatabaseException 
	 */
	public void addProject(Project inputProject) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;	
		try {
			String query = "insert into PROJECT (NAME, RECORDSPERBATCH, FIRSTY, HEIGHT) values (?, ?, ?, ?)";
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			stmt.setString(1, inputProject.getTitle());
			stmt.setInt(2, inputProject.getRecordsPerImage());
			stmt.setInt(3, inputProject.getFirstYCord());
			stmt.setInt(4, inputProject.getRecordHeight());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = parentDatabase.getCurrentConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				inputProject.setID(id);
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

	/**Searches the database for a project and builds an object representing that project to load into memory.
	 * 
	 * @param inputProject Project to search for, by name and ID.
	 * @return The project object from the database, or null if it doesn't exist.
	 * @throws DatabaseException 
	 */
	public Project getProject(ProjectTuple inputProject) throws DatabaseException{
		Project output=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select NAME, RECORDSPERBATCH, FIRSTY, HEIGHT from PROJECT where ID="+inputProject.getID();
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				String name = rs.getString(1);
				int rperbatch = rs.getInt(2);
				int firstY = rs.getInt(3);
				int height = rs.getInt(4);
				output = new Project(inputProject.getID(), name, rperbatch, firstY, height);
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
	
	public Project getProject(int input) throws DatabaseException{
		ProjectTuple temp = new ProjectTuple("",input);
		return getProject(temp);
	}
		
	public void updateProject(Project inputProject) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "update PROJECT set NAME='"+inputProject.getTitle()+"', RECORDSPERBATCH="+inputProject.getRecordsPerImage()+", FIRSTY="+inputProject.getFirstYCord()+", HEIGHT="+inputProject.getRecordHeight()+
					" where ID="+inputProject.getID(); 
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			
			if (stmt.executeUpdate() == 1) {
				
			}
			else {
				throw new DatabaseException("Could not update");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
}
