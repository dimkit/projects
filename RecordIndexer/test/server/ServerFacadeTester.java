package server;
import java.util.ArrayList;

import importer.DatabaseImporter;

import org.junit.* ;

import database.helper.CellDAO;
import database.helper.Database;
import database.helper.DatabaseException;
import server.ServerFacade;
import shared.com.AuthenticationWrapper;
import shared.com.BatchWrapper;
import shared.com.FieldListWrapper;
import shared.com.FinishedBatchWrapper;
import shared.com.ImageWrapper;
import shared.com.ProjectChoiceWrapper;
import shared.com.ResultWrapper;
import shared.com.SearchInputWrapper;
import shared.com.SearchResultsWrapper;
import shared.model.Cell;
import shared.model.Field;
import shared.model.Project;
import shared.model.ProjectTuple;
import shared.model.User;
import static org.junit.Assert.*;

public class ServerFacadeTester {
	ServerFacade server= new ServerFacade();
		
	@BeforeClass
	public static void reset(){
		DatabaseImporter in = new DatabaseImporter();
		in.run("Records.xml");
		in = null;
	}

	@Test
	public void testAuthenticate(){
		
		AuthenticationWrapper auth = gAuth();
		User output;
		try {
			output = ServerFacade.validateUser(new AuthenticationWrapper("test1","test1"));
			User tempUser = new User(1,"test1","test1","Test","One","test1@gmail.com",-1,0);		
			assertEquals(output,tempUser);
			 //We also need to make sure that an incorrect user cannot be found. This first one has a bad username, and the second has a bad password. They should both fail.
			auth = new AuthenticationWrapper("beeep","test1");
			output = ServerFacade.validateUser(auth);
			assertNull(output);
			auth = new AuthenticationWrapper("test1","TWILIGHT!");
			output = ServerFacade.validateUser(auth);
			assertNull(output);	
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetProjects(){
		try {
			ArrayList<ProjectTuple> projects = (ArrayList<ProjectTuple>) ServerFacade.getProjects(gAuth()).getProjectList();
			assertEquals(projects.size(),3);
			assertEquals(projects.get(0).getName(),"1890 Census");
			assertEquals(projects.get(0).getID(),1);
			assertEquals(projects.get(1).getName(),"1900 Census");
			assertEquals(projects.get(1).getID(),2);
			assertEquals(projects.get(2).getName(),"Draft Records");
			assertEquals(projects.get(2).getID(),3);
			assertNull(ServerFacade.getProjects(new AuthenticationWrapper("test1","wrongPassword")));
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Test
	public void testGetSampleImage(){
		ImageWrapper image;
		try {
			image = ServerFacade.getSampleImage(new ProjectChoiceWrapper(gAuth(),1));
			assertEquals(image.getPicture(),"database\\images\\1890_image0.png");
			image = ServerFacade.getSampleImage(new ProjectChoiceWrapper(gAuth(),-1));
			assertNull(image);
			image = ServerFacade.getSampleImage(new ProjectChoiceWrapper(gBadAuth(),1));
			assertNull(image);
			image = ServerFacade.getSampleImage(new ProjectChoiceWrapper(gAuth(),2));
			assertEquals(image.getPicture(),"database\\images\\1900_image0.png");
			image = ServerFacade.getSampleImage(new ProjectChoiceWrapper(gAuth(),3));
			assertEquals(image.getPicture(),"database\\images\\draft_image0.png");
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@Test
	public void testDownloadBatch(){
		BatchWrapper batch;
		try {
			batch = ServerFacade.downloadBatch(gProj());
			assertNotSame(batch,null);
			assertEquals(batch.getImage().getImageFilePath(), "database\\images\\1890_image0.png");
			assertEquals(batch.getParentProject().getID(), 1);
			assertEquals(batch.getParentProject().getRecordsPerImage(), 60);
			assertEquals(batch.getParentProject().getFirstYCord(), 199);
			assertEquals(batch.getParentProject().getRecordHeight(), 60);
			assertEquals(batch.getParentProject().getTitle(), "1890 Census");
			ArrayList<Field> fields = batch.getFieldList();
			assertNotNull(fields);
			assertEquals(fields.size(),4);
			assertEquals(fields.get(3).getTitle(),"Age");
			assertEquals(fields.get(1).getHelpHtml(),"database\\helps\\first_name.html");
			assertEquals(fields.get(1).getColNumber(),2);
			
			//Make sure it won't give us another batch if we already have one.
			batch = ServerFacade.downloadBatch(gProj());
			assertNull(batch);
			batch = ServerFacade.downloadBatch(new ProjectChoiceWrapper(gAuth(),10));
			assertNull(batch);
			batch = ServerFacade.downloadBatch(new ProjectChoiceWrapper(gBadAuth(),1));
			assertNull(batch);
			
			//Make sure it will allow other users to correctly download a batch after the first one does.
			batch = ServerFacade.downloadBatch(new ProjectChoiceWrapper(new AuthenticationWrapper("test2","test2"),1));
			assertNotNull(batch);
			assertNotSame(batch.getImage().getImageFilePath(), "database\\images\\1890_image0.png");
			assertEquals(batch.getParentProject().getID(), 1);
			User temp1 = ServerFacade.validateUser(gAuth());
			assertEquals(temp1.getCurrentBatch(),1);
			temp1 = ServerFacade.validateUser(new AuthenticationWrapper("test2","test2"));
			assertEquals(temp1.getCurrentBatch(),2);
			
			//We have to rebuild to ensure we don't screw up future tests.
			reset();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetFields(){
		ArrayList<Field> fields;
		try {
			fields = ServerFacade.getFields(gProj()).getFieldList();
			assertNotNull(fields);
			assertEquals(fields.size(),4);
			assertEquals(fields.get(3).getTitle(),"Age");
			assertEquals(fields.get(1).getHelpHtml(),"database\\helps\\first_name.html");
			assertEquals(fields.get(1).getColNumber(),2);
			FieldListWrapper plops = ServerFacade.getFields(new ProjectChoiceWrapper(gBadAuth(),1));
			assertNull(plops);
			plops = ServerFacade.getFields(new ProjectChoiceWrapper(gAuth(),100));
			assertNull(plops);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearch(){
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("FOX");
		ArrayList<Integer> fieldsToSearch = new ArrayList<Integer>();
		fieldsToSearch.add(new Integer(10));
		SearchInputWrapper temp = new SearchInputWrapper(terms, fieldsToSearch,gAuth());
		SearchResultsWrapper output;
		try {
			output = ServerFacade.search(temp);
			assertNotNull(output);
			ArrayList<ResultWrapper> results = output.getResultList();
			assertNotNull(results);
			assertEquals(results.size(),1);
			ResultWrapper result1 = results.get(0);
			assertNotNull(result1);
			assertEquals(result1.getBatchID(),41);
			assertEquals(result1.getImageURL(),"database\\images\\draft_image0.png");
			assertEquals(result1.getFieldID(),10);
			assertNotSame(result1.getRecordNumber(),-1);
			
			//Now we need to update the list to search more fields for more terms, to make sure the search function can handle them correctly.
			fieldsToSearch.add(new Integer(11));
			fieldsToSearch.add(new Integer(12));
			fieldsToSearch.add(new Integer(13));
			
			terms.add("EDWARDS");
			terms.add("RAMON");
			
			temp = new SearchInputWrapper(terms, fieldsToSearch,gAuth());
			output = ServerFacade.search(temp);
			results = output.getResultList();
			assertEquals(results.size(),3);
			result1 = results.get(0);
			assertNotNull(result1);
			assertEquals(result1.getBatchID(),41);
			assertEquals(result1.getImageURL(),"database\\images\\draft_image0.png");
			assertEquals(result1.getFieldID(),10);
			assertNotSame(result1.getRecordNumber(),-1);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testSubmitBatch(){
		BatchWrapper batch;
		try {
			batch = ServerFacade.downloadBatch(gProj());
			Project workingProject = batch.getParentProject();
			int batchID = batch.getImage().getID();
			ArrayList<Cell> dataValues = makeCells();
			
			FinishedBatchWrapper input = new FinishedBatchWrapper(workingProject, batchID,dataValues, gAuth()); 
			boolean result = ServerFacade.submitBatch(input);
			assertEquals(result,true);
			ArrayList<Cell> results =null;
			Database temp = new Database();
			try {
				temp.startTransaction();
				CellDAO tempCellDAO = new CellDAO(temp);
				results= tempCellDAO.getCellsByBatch(1);
				
			} catch (DatabaseException e) {
				System.out.println("Failed to open transaction in submit batch test");
				e.printStackTrace();
			}
			assertNotNull(results);
			assertEquals(results.size(),8);
			assertEquals(results.get(0),new Cell("Mooney",-1,-1,1,1));
			assertEquals(results.get(1),new Cell("Dick",-1,-1,2,1));
			assertEquals(results.get(2),new Cell("Male",-1,-1,3,1));
			assertEquals(results.get(3),new Cell("3",-1,-1,4,1));
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private ArrayList<Cell> makeCells(){
		ArrayList<Cell> dataValues = new ArrayList<Cell>();
		dataValues.add(new Cell("Mooney",-1,-1,1,1));
		dataValues.add(new Cell("Dick",-1,-1,2,1));
		dataValues.add(new Cell("Male",-1,-1,3,1));
		dataValues.add(new Cell("3",-1,-1,4,1));
		dataValues.add(new Cell("Carney",-1,-1,1,1));
		dataValues.add(new Cell("Maxwell",-1,-1,2,1));
		dataValues.add(new Cell("M",-1,-1,3,1));
		dataValues.add(new Cell("50",-1,-1,4,1));
		
		return dataValues;
	}
	private AuthenticationWrapper gAuth(){
		return new AuthenticationWrapper("test1","test1");
	}
	
	private AuthenticationWrapper gBadAuth(){
		return new AuthenticationWrapper("Fale","test1");
	}
	
	private ProjectChoiceWrapper gProj(){
		return new ProjectChoiceWrapper(gAuth(),1);
	}
}
