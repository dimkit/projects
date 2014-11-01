package database.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.helper.Database;
import database.helper.DatabaseBuilder;
import database.helper.DatabaseException;
import database.helper.FieldDAO;
import shared.model.Field;

public class FieldDAOTest {
	private FieldDAO testDAO;
	private Database myDatabase;
	
	@Before
	public void setup(){
	    DatabaseBuilder testData= new DatabaseBuilder();
	    testData.buildDatabaseFromScratch();
	    myDatabase = new Database();
		testDAO = new FieldDAO (myDatabase);
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
	
	public Field makeField(){
		return new Field(-1,1,"Names",5,20,"myhelp.com/404","knowndata.com/webzone",5);
	}	
	@Test
	public void addFieldTest(){
		try{
			testDAO.addField(makeField());
			assertEquals(true,true);
		}catch(Exception e){
			e.printStackTrace();
			fail();		
		}	
	}
		
	@Test
	public void getFieldsByProjectTest(){
		ArrayList<Field> output = null;
		try {
			testDAO.addField(makeField());
			testDAO.addField(makeField());
			testDAO.addField(makeField());
			testDAO.addField(makeField());
			commit();
			start();
			output = (ArrayList<Field>) testDAO.getFields(1);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		assertEquals(output.size(),4);
		assertEquals(output.get(1),makeField());
	}
	
}