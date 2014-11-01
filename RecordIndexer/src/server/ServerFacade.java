package server;

import java.util.ArrayList;

import database.helper.*;
import shared.com.*;
import shared.model.*;

public class ServerFacade {
	static{
		database = new Database();
	}
	
	private static Database database;
	/** Searches the database for the input user's username and password.
	 * 
	 * @param authInput The pre-wrapped username and password to test against the database
	 * @return The User model object coresponding to the input username and password if the credentials are valid, null otherwise.
	 * @throws DatabaseException 
	 */
	public static User validateUser(AuthenticationWrapper authInput) throws DatabaseException{
		if(authInput==null){
			return null;
		}
		
		String username = authInput.getUsername();
		String password = authInput.getPassword();
		
		UserDAO tempDAO = new UserDAO(database);
		User out = null;
		try {
			database.startTransaction();
			out= tempDAO.findUser(username, password);
		} catch (DatabaseException e) {
			System.out.println("Exception finding username");
			database.endTransaction(false);
			throw e;
		}
		database.endTransaction(false);
		return out;
	}
	
	/**Lists all the projects presently in the database, getting their name and Project ID number.
	 * 
	 * @param authInput User performing this operation. Without valid cradentials this list will not be given.
	 * @return A pre-wrapped ProjectListWrapper containing all projects if successful, null otherwise.
	 * @throws DatabaseException 
	 */
	public static ProjectListWrapper getProjects (AuthenticationWrapper authInput) throws DatabaseException{
		User user = validateUser(authInput);
		if(user!=null){
			ProjectDAO tempDAO = new ProjectDAO(database);
			
			ProjectListWrapper output=null;
			try {
				database.startTransaction();
				output = new ProjectListWrapper(tempDAO.getAllProjects());
				database.endTransaction(false);
			} catch (DatabaseException e) {
				System.out.println("Exception getting all projects");
				database.endTransaction(false);
			}
			
			return output;	
		}	
		return null;
	}
	/**Gives the user an image from the project they selected.
	 * 
	 * @param project pre-wrapped Project selection.
	 * @return a pre-wrapped ImageWrapper object containing the sample image if successful, null otherwise.
	 * @throws DatabaseException 
	 */
	public static ImageWrapper getSampleImage(ProjectChoiceWrapper inputProject) throws DatabaseException{
		if(inputProject==null){
			return null;
		}
		User user = validateUser(inputProject.getAuth());
		if(user!=null){
			BatchDAO tempDAO = new BatchDAO(database);
			int projectID = inputProject.getProject();
			try {
				database.startTransaction();
				ArrayList<Batch> batchList= new ArrayList<Batch>();
				batchList = tempDAO.getBatchesByProject(projectID);
				database.endTransaction(false);
				if(batchList.size()>0){
					ImageWrapper out = new ImageWrapper(batchList.get(0).getImageFilePath());
					
					return out;
				}
				
			} catch (DatabaseException e) {
				System.out.println("Failed to get sample image");
				database.endTransaction(false);
				e.printStackTrace();
			}
		
		}
		database.endTransaction(false);
		return null;
	}
	/**Chooses a batch from the server based on project selection that has not yet been worked on and packages it for transport.
	 * 
	 * @param project pre-wrapped Project selection
	 * @return pre-Wrapped batchWrapper object containg all required information about the batch if succeeds, null otherwise.
	 * @throws DatabaseException 
	 */
	public static BatchWrapper downloadBatch(ProjectChoiceWrapper inputProject) throws DatabaseException{
		if(inputProject==null){
			return null;
		}
		User user = validateUser(inputProject.getAuth());
		if(user!=null&&user.getCurrentBatch()==-1){
			int projectID = inputProject.getProject();
			ProjectDAO tempProjectDAO = new ProjectDAO(database);
			BatchDAO tempBatchDAO = new BatchDAO(database);
			FieldListWrapper fields = getFields(inputProject);
			BatchWrapper output = null;
			Project project =null;
			try {
				database.startTransaction();
				project = tempProjectDAO.getProject(projectID);
				ArrayList<Batch> canidateBatches = new ArrayList<Batch>();
				try{
					canidateBatches= tempBatchDAO.getBatchesByProject(projectID);
				}catch(DatabaseException e){}
			Batch chosenBatch = chooseBatch(canidateBatches);
			database.endTransaction(false);
			//Performs the actual update as best as possible.
			if(chosenBatch!=null&&project!=null&&fields!=null){
				UserDAO tempUserDAO = new UserDAO(database);
				database.startTransaction();
				
				tempBatchDAO.updateBatch(chosenBatch);
				user.setCurrentBatch(chosenBatch.getID());
				tempUserDAO.updateUser(user);
				output = new BatchWrapper(chosenBatch,project, fields.getFieldList());
				
				database.endTransaction(true);
				return output;
			}
			
			database.endTransaction(false);
			} catch (DatabaseException e) {
				System.out.println("Failed to download batch");
				database.endTransaction(false);
				e.printStackTrace();
			}
		}
		return null;
	}
	//Used in downloading a batch, finds a possible canidate to download.
	private static  Batch chooseBatch(ArrayList<Batch> batches){

		for(int i=0;i<batches.size();i++){
			if(batches.get(i).getState()==0){
				Batch output= batches.get(i);
				output.checkOut();
				return output;
			}
		}
		return null;
	}
	
	/**Processes a submited batch and adds its results into the database.
	 * 
	 * @param doneBatch the pre-wrapped input batch and all required information about it.
	 * @return true if the database operation completes successfully, false otherwise.
	 * @throws DatabaseException 
	 */
	public static boolean submitBatch(FinishedBatchWrapper doneBatch) throws DatabaseException{
		if(doneBatch==null){
			return false;
		}
		User user = validateUser(doneBatch.getAuth());
		if(user!=null){
			if(user.getCurrentBatch()!=doneBatch.getBatchID()){
				return false;
			}
			
			try {
				database.startTransaction();
				BatchDAO tempBatchDAO = new BatchDAO(database);
				CellDAO tempCellDAO = new CellDAO(database);
				tempCellDAO.addCells(doneBatch.getDataValues());
				Batch tempBatch = tempBatchDAO.getBatch(doneBatch.getBatchID());
				tempBatch.checkOut();
				user.setCurrentBatch(-1);
				int numNewRecords = doneBatch.getDataValues().size();
				user.addIndexedRecords(numNewRecords);
				tempBatchDAO.updateBatch(tempBatch);
				UserDAO tempUserDAO = new UserDAO(database);
				tempUserDAO.updateUser(user);
				database.endTransaction(true);
				return true;
				
			} catch (DatabaseException e) {
				System.out.println("Failed to incorperate compete downloaded batch");
				database.endTransaction(false);
				e.printStackTrace();
			}
			
		}
		
		return false;
	}
	
	/**Obtains the list of fields for a given project
	 * 
	 * @param project The pre-wrapped project selection and authentication information.
	 * @return A pre-wrapped list of all the fields from the selected project if the operaton succeeds, null otherwise.
	 * @throws DatabaseException 
	 */
	public static FieldListWrapper getFields(ProjectChoiceWrapper inputProject) throws DatabaseException{
		if(inputProject==null){
			return null;
		}
		User user = validateUser(inputProject.getAuth());
		if(user!=null){
			FieldDAO temp = new FieldDAO(database);
			try {
				database.startTransaction();
				ArrayList<Field> fields = (ArrayList<Field>) temp.getFields(inputProject.getProject());
				if(fields==null){
					database.endTransaction(false);
					return null;
				}
				FieldListWrapper output = new FieldListWrapper(inputProject.getProject(),fields);
				database.endTransaction(false);
				return output;
			} catch (DatabaseException e) {
				System.out.println("Unable to get fields for a speicific project");
				database.endTransaction(false);
				e.printStackTrace();
			}
		}
		return null;
	}
	/**Searches the fields chosen in the database for the specific pattern indicated.
	 * 
	 * @param searchIn The pre-wrapped fields to search, authentication information, and pattern to search for.
	 * @return The results of the search wrapped for transport, or null if the search fails for any reason. Should no results be found, an empty wrapper is returned.
	 * @throws DatabaseException 
	 */
	public static SearchResultsWrapper search(SearchInputWrapper searchIn) throws DatabaseException{
		if(searchIn==null){
			return null;
		}
		User user = validateUser(searchIn.getAuth());
		if(user!=null){
			try {
				database.startTransaction();
				CellDAO tempCellDAO = new CellDAO(database);
				ArrayList<Cell> possibleResults = tempCellDAO.getCellsbyFieldList(searchIn.getFieldIDs());
				ArrayList<ResultWrapper> verifiedResults = new ArrayList<ResultWrapper>();
				ArrayList<String> searchTerms = searchIn.getTerms();
				BatchDAO tempBatchDAO = new BatchDAO(database);
				
				for(int i=0;i<possibleResults.size();i++){
					for(int j =0;j<searchTerms.size();j++){
						if(possibleResults.get(i).getData().equals(searchTerms.get(j))){
							String imageURL = tempBatchDAO.getBatch(possibleResults.get(i).getBatchID()).getImageFilePath();
							int fieldID = possibleResults.get(i).getFieldID();
							int batchID = possibleResults.get(i).getBatchID();
							verifiedResults.add(new ResultWrapper(batchID,imageURL,getRecordNumber(possibleResults.get(i)),fieldID));
							
						}
					}
				}
				SearchResultsWrapper output = new SearchResultsWrapper(verifiedResults);
				database.endTransaction(false);
				return output;
				
			} catch (DatabaseException e) {
				System.out.println("Failed during search operation");
				database.endTransaction(false);
				e.printStackTrace();
			}
			
		}
		return null;
	}
// Calculates the row a piece of data is located in a given batch.
	private static int getRecordNumber(Cell cell) throws DatabaseException {
		CellDAO tempCellDAO = new CellDAO (database);
		ArrayList<Cell> allCellsFromBatch = tempCellDAO.getCellsByBatch(cell.getBatchID());
		
		for(int i=0;i<allCellsFromBatch.size();i++){
				if(allCellsFromBatch.get(i).equals(cell)){
					return i+1;
				}
		}
		
		return -1;
	}
	
}
