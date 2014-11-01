package database.helper;

import java.util.ArrayList;

import org.junit.*;

import database.helper.Database;
import database.helper.DatabaseBuilder;
import database.helper.DatabaseException;
import database.helper.ProjectDAO;
import static org.junit.Assert.*;
import shared.model.Project;
import shared.model.ProjectTuple;

public class ProjectDAOTest {

	private ProjectDAO testDAO;
	private Database myDatabase;
	
	@Before
	public void setup(){
	    DatabaseBuilder testData= new DatabaseBuilder();
	    testData.buildDatabaseFromScratch();
	    myDatabase = new Database();
		testDAO = new ProjectDAO (myDatabase);
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
	
	public Project makeProject(){
		return new Project(-1,"2012 Taxes",25,30,50);
	}	
	@Test
	public void addProjectTest(){
		try{
			testDAO.addProject(makeProject());
			assertEquals(true,true);
		}catch(Exception e){
			e.printStackTrace();
			fail();		
		}	
	}
	@Test
	public void getProjectTest(){
		Project out=null;
		
		try{
			testDAO.addProject(makeProject());
			commit();
			start();
			out= testDAO.getProject(Project.makeTuple(makeProject()));
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		assertEquals(out,makeProject());

	}
	
	@Test
	public void updateProjectTest(){
		Project out= makeProject();
		
		try{
			testDAO.addProject(out);
			commit();
			start();
			out.setFirstYCord(-10);
			out.setTitle("Nemoy");
			out.setRecordsPerImage(-10);
			testDAO.updateProject(out);
			commit();
			start();
			Project output=testDAO.getProject(Project.makeTuple(out));
			assertEquals(output,out);
			
		}catch(Exception e){
			System.out.println("You failed the update test!");
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void getAllProjects(){
		ArrayList<ProjectTuple> output = null;
		try {
			testDAO.addProject(makeProject());
			testDAO.addProject(makeProject());
			testDAO.addProject(makeProject());
			testDAO.addProject(makeProject());
			commit();
			start();
			output = (ArrayList<ProjectTuple>) testDAO.getAllProjects();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		assertEquals(output.size(),4);
		assertEquals(output.get(1),Project.makeTuple(makeProject()));
	}
}
