package database.helper;

import org.junit.*;

import database.helper.Database;
import database.helper.DatabaseBuilder;
import database.helper.DatabaseException;
import database.helper.UserDAO;
import static org.junit.Assert.*;
import shared.model.User;

public class UserDAOTest {
	private UserDAO testDAO;
	private Database myDatabase;
	
	@Before
	public void setup(){
	    DatabaseBuilder testData= new DatabaseBuilder();
	    testData.buildDatabaseFromScratch();

	    myDatabase = new Database();
		testDAO = new UserDAO (myDatabase);
		try {
			myDatabase.startTransaction();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}
	@After
	public void tearDown(){
		myDatabase.endTransaction(true);
	}
	public User makeUser(){
		return new User(1,"myUserName","myPassword","myFirstName","myLastName","myEmail@email.com",-1,0);
	}
	private void startTransaction(){
		try {
			myDatabase.startTransaction();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void addUserTest(){

		try{
			testDAO.addUser(makeUser());
			assertEquals(true,true);
		}catch(Exception e){
			e.printStackTrace();
			fail();		
		}	
	}
	@Test
	public void findUserTest(){

		User out=null;
		
		try{
			testDAO.addUser(makeUser());
			myDatabase.endTransaction(true);
			startTransaction();
			out= testDAO.findUser("myUserName", "myPassword");
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		assertEquals(out,makeUser());
		System.out.println(out);
	}
	
	@Test
	public void updateUserTest(){
		User out=makeUser();
		
		try{
			testDAO.addUser(out);
			myDatabase.endTransaction(true);
			startTransaction();
			out.setFirstName("Lenord");
			out.setLastName("Nemoy");
			out.setEmail("thissucks@oiffd.com");
			testDAO.updateUser(out);
			myDatabase.endTransaction(true);
			startTransaction();
			User output=testDAO.findUser(out.getUsername(), out.getPassword());
			assertEquals(output,out);

			
		}catch(Exception e){
			System.out.println("You failed the update test!");
			e.printStackTrace();
			fail();
		}
	}
}
