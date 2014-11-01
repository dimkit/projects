package database.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import shared.model.Cell;


public class CellDAO {
	private Database parentDatabase;
	public CellDAO(Database tempDatabase) {
		parentDatabase=tempDatabase;
	}

	/**Adds a given input cell to the database.
	 * 
	 * @return The ID of the newly added cell in the database.
	 * @throws DatabaseException 
	 */
	public void addCell(Cell inputCell) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into CELL (DATA, FIELD_ID, ROWNUM, BATCH_ID) values (?, ?, ?, ?)";	
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			stmt.setString(1, inputCell.getData());
			stmt.setInt(2, inputCell.getFieldID());
			stmt.setInt(3, inputCell.getRowNumber());
			stmt.setInt(4, inputCell.getBatchID());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = parentDatabase.getCurrentConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				inputCell.setID(id);
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
	public void addCells(ArrayList<Cell> inputList) throws DatabaseException{
		for(int i=0;i<inputList.size();i++){
			addCell(inputList.get(i));
		}
	}

	public ArrayList<Cell> getCellsByBatch(int inputBatch) throws DatabaseException{
		ArrayList<Cell> result = new ArrayList<Cell>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select ID, DATA, FIELD_ID, ROWNUM from CELL where BATCH_ID= "+inputBatch;
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String data = rs.getString(2);
				int field_ID = rs.getInt(3);
				int rowNum = rs.getInt(4);
				result.add(new Cell(data,id,rowNum, field_ID,inputBatch));
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
	private ArrayList<Cell> getCellsByField(int inputField) throws DatabaseException{
		ArrayList<Cell> result = new ArrayList<Cell>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select ID, DATA, BATCH_ID, ROWNUM from CELL where FIELD_ID= "+inputField;
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String data = rs.getString(2);
				int batch_id = rs.getInt(3);
				int rowNum = rs.getInt(4);
				result.add(new Cell(data,id,rowNum, inputField,batch_id));
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
	
	public ArrayList<Cell> getCellsbyFieldList(ArrayList<Integer> inputList) throws DatabaseException{
		ArrayList<Cell> results = new ArrayList<Cell>();
		for(int i=0;i<inputList.size();i++){
			results.addAll(getCellsByField(inputList.get(i)));
		}
		return results;
	}
}
