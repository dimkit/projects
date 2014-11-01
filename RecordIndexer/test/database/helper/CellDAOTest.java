package database.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.helper.CellDAO;
import database.helper.Database;
import database.helper.DatabaseBuilder;
import database.helper.DatabaseException;
import shared.model.Cell;

public class CellDAOTest {
	private CellDAO testDAO;
	private Database myDatabase;
	
	@Before
	public void setup(){
	    DatabaseBuilder testData= new DatabaseBuilder();
	    testData.buildDatabaseFromScratch();
	    myDatabase = new Database();
		testDAO = new CellDAO (myDatabase);
		try {
			myDatabase.startTransaction();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}
	private void start(){
		try {
			myDatabase.startTransaction();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void commit(){
		myDatabase.endTransaction(true);
	}
	
	@After
	public void tearDown(){
		commit();
	}
	
	public Cell makeCell(){
		return new Cell("Fobar",-1,12,5,5);
	}	
	@Test
	public void addCellTest(){
		try{
			testDAO.addCell(makeCell());
			assertEquals(true,true);
		}catch(Exception e){
			e.printStackTrace();
			fail();		
		}	
	}
		
	@Test
	public void getCellsByProjectTest(){
		ArrayList<Cell> output = null;
		try {
			testDAO.addCell(makeCell());
			testDAO.addCell(makeCell());
			testDAO.addCell(makeCell());
			testDAO.addCell(makeCell());
			commit();
			start();
			output = (ArrayList<Cell>) testDAO.getCellsByBatch(5);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		assertEquals(output.size(),4);
		assertEquals(output.get(1),makeCell());
	}
}
