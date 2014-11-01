package database.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import shared.model.Batch;

public class BatchDAO {
	private Database parentDatabase;
	
	public BatchDAO(Database tempDatabase) {
		parentDatabase = tempDatabase;
	}
	/**Gets a batch object representing a specific batch in the database
	 * 
	 * @param batchID Batch to search for.
	 * @return Batch object representing this batch in the database, or null if it doesn't exist.
	 * @throws DatabaseException 
	 */
	public Batch getBatch(int batchID) throws DatabaseException{
		Batch output=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select IMAGEFILEPATH, PROJECTID, STATE from BATCH where ID="+batchID;
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				String filePath = rs.getString(1);
				int projID = rs.getInt(2);
				int state = rs.getInt(3);
				output = new Batch(batchID, filePath, projID, state);
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
	/**Updates the batch in the database based on the input batch being sent.
	 * 
	 * @param inputBatch Batch to update.
	 * @return true if the operation succeeds, false otherwise.
	 * @throws DatabaseException 
	 */
	public void updateBatch(Batch inputBatch) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "update BATCH set IMAGEFILEPATH='"+inputBatch.getImageFilePath()+"', PROJECTID="+inputBatch.getProjectID()+", STATE="+inputBatch.getState()+" where ID="+inputBatch.getID(); 
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			
			if (stmt.executeUpdate() != 1) {
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

	
	/**Adds a batch to the database
	 * 
	 * @param inputBatch Batch object to add to the database.
	 * @throws DatabaseException 
	 */
	public void addBatch(Batch inputBatch) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into BATCH (IMAGEFILEPATH, PROJECTID, STATE) values (?, ?, ?)";	
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			stmt.setString(1, inputBatch.getImageFilePath());
			stmt.setInt(2, inputBatch.getProjectID());
			stmt.setInt(3, inputBatch.getState());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = parentDatabase.getCurrentConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				inputBatch.setID(id);
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
	
	public ArrayList<Batch> getBatchesByProject(int projBatch) throws DatabaseException{
		ArrayList<Batch> result = new ArrayList<Batch>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select ID, IMAGEFILEPATH, STATE from BATCH where PROJECTID= "+projBatch;
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String filePath = rs.getString(2);
				int state = rs.getInt(3);

				result.add(new Batch(id,filePath, projBatch,state));
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
}
