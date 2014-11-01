package shared.com;

import java.util.ArrayList;

import shared.model.Cell;
import shared.model.Project;

/**Contains all the information about a finished batch needed to add it back into the database. This class always flows from the user to the server, so also contains authentication information.
 * 
 * @author Kristopher Miles
 *
 */
public class FinishedBatchWrapper {
	private Project project;
	private int batchID;
	private ArrayList<Cell> dataValues;
	private AuthenticationWrapper auth;
	
	public FinishedBatchWrapper(Project project, int batchID,
			ArrayList<Cell> dataValues, AuthenticationWrapper auth) {
		super();
		this.project = project;
		this.batchID = batchID;
		this.dataValues = dataValues;
		this.auth = auth;
	}
	
	public AuthenticationWrapper getAuth() {
		return auth;
	}


	public void setAuth(AuthenticationWrapper auth) {
		this.auth = auth;
	}

	public Project getProject() {
		return project;
	}
	public int getBatchID() {
		return batchID;
	}
	public ArrayList<Cell> getDataValues() {
		return dataValues;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
	public void setDataValues(ArrayList<Cell> dataValues) {
		this.dataValues = dataValues;
	}
	
	

}
