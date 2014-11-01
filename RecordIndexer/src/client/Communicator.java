package client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.com.BatchWrapper;
import shared.com.FieldListWrapper;
import shared.com.FinishedBatchWrapper;
import shared.com.ImageWrapper;
import shared.com.ProjectListWrapper;
import shared.com.SearchResultsWrapper;
import shared.model.Cell;
import shared.model.Project;
import shared.model.User;
import shared.com.AuthenticationWrapper;
import shared.com.ProjectChoiceWrapper;

/**This class is used to process all client information to send to the server, and acomplish the actual HTTP communication.
 * 
 * @author Kristopher Miles
 *
 */
@SuppressWarnings("unused")
public class Communicator {
	private static final String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 1337;
	private static final String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	private static final String HTTP_POST = "POST";
	private XStream xmlStream;
	
	public Communicator() {
		xmlStream = new XStream(new DomDriver());
	}
	
	/**Authenticates with the server if the input username and password are both valid. Also creates the "auth" object to be used in future operations.
	 * 
	 * @param username The Username to validate.
	 * @param password The password to validate.
	 * @return A User model object if the user exists, null otherwise.
	 */
	public User validateUser(String username, String password){
		AuthenticationWrapper auth = new AuthenticationWrapper(username,password);
		return (User)transmit("/ValidateUser",auth);
	}
	/**Obtains a list of all projects from the server.
	 * 
	 * @return A wrapped list of projects to show the user.
	 */
	public ProjectListWrapper getProjects (String username, String password){
		return null;
	}
	/**Obtains an image from the selected project from the server.
	 * 
	 * @param project The project we need a sample image from.
	 * @return A wrapped image file from the selected project, or null if the operation fails.
	 */
	public ImageWrapper getSampleImage(String username, String password, int id){
		return null;
	}
	
	/**Downloads a batch for the user to work on.
	 * 
	 * @param project The project we are asking for a batch from.
	 * @return A wrapped batch containing everything the user needs to work on the batch, or null if the operation fails.
	 */
	public BatchWrapper downloadBatch(String username, String password, int id){
		return null;
	}
	
	/**Send the finished batch to the server to be added to the database
	 * 
	 * @param inputBatch The batch we are submitting
	 * @param data A list containing all the data we have created.
	 * @return true if the database is able to incorperate the data correctly, false otherwise.
	 */
	public boolean submitBatch(String username, String password, int batch, String inputList){
		return false;
	}
	/**Obtains a list of all the fields in a given project
	 * 
	 * @param project The project whose fields we wish to list.
	 * @return A wrapped list of all the fields in the selected project if successful, null if the operation fails.
	 */
	public FieldListWrapper getFields(String username, String password, int id){
		return null;
	}
	
	/**Searches the database in whatever fields we wish to examine in order to find the pattern we have selected. May search multiple fields and for multiple patterns.
	 * 
	 * @param search A list of strings we are searching for.
	 * @param fieldsToSearch A list of the IDs of the fields we are searching.
	 * @return A wrapped list of the search results if the search is conducted successfully, null if the search fails or credentials are invalid.
	 */
	public SearchResultsWrapper search(String username, String password,List<Integer> fieldsToSearch,List<String> search){
		return null;
	}

	private Object transmit(String urlPath, Object postData){
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.connect();
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("Failed to doPost");
				return null;
			}
			else{
				Object result = xmlStream.fromXML(connection.getInputStream());
				return result;
			}
		}
		catch (IOException e) {
			System.out.println("Failed to doPost");
			e.printStackTrace();
		}
		return null;
	}
	
}
