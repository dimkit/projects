package database.helper;

import java.util.ArrayList;

import org.junit.*;

import database.helper.BatchDAO;
import database.helper.Database;
import database.helper.DatabaseBuilder;
import database.helper.DatabaseException;
import static org.junit.Assert.*;
import shared.model.Batch;


public class BatchDAOTest{
	private BatchDAO testDAO;
	private Database myDatabase;
	
	@Before
	public void setup(){
	    DatabaseBuilder testData= new DatabaseBuilder();
	    testData.buildDatabaseFromScratch();
	    myDatabase = new Database();
		testDAO = new BatchDAO (myDatabase);
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
	
	public Batch makeBatch(){
		return new Batch(-1, "www.crap.com/4102",1,0);
	}	
	@Test
	public void addBatchTest(){
		try{
			testDAO.addBatch(makeBatch());
			assertEquals(true,true);
		}catch(Exception e){
			e.printStackTrace();
			fail();		
		}	
	}
	
	@Test
	public void getBatchTest(){
		Batch out=null;
		
		try{
			Batch test = makeBatch();
			testDAO.addBatch(test);
			commit();
			start();
			out= testDAO.getBatch(test.getID());
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		assertEquals(out,makeBatch());

	}
	
	@Test
	public void updateBatchTest(){
		Batch out= makeBatch();
		
		try{
			testDAO.addBatch(out);
			commit();
			start();
			out.checkOut();
			testDAO.updateBatch(out);
			commit();
			start();
			Batch output=testDAO.getBatch(out.getID());
			assertEquals(output,out);
			
		}catch(Exception e){
			System.out.println("You failed the update test!");
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void getBatchesByProjectTest(){
		ArrayList<Batch> output = null;
		try {
			testDAO.addBatch(makeBatch());
			testDAO.addBatch(makeBatch());
			testDAO.addBatch(makeBatch());
			testDAO.addBatch(makeBatch());
			commit();
			start();
			output = (ArrayList<Batch>) testDAO.getBatchesByProject(1);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		assertEquals(output.size(),4);
		assertEquals(output.get(1),makeBatch());
	}
}
