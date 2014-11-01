package shared.com;

import java.util.ArrayList;

import shared.model.Field;
/**Used to contain a list of fields for a given project, in order to relay them to the user.
 * 
 * @author Kristopher Miles
 *
 */
public class FieldListWrapper {
	private int projectID;
	private ArrayList<Field> fieldList;
	
	public FieldListWrapper(int projectID, ArrayList<Field> fieldList) {
		super();
		this.projectID = projectID;
		this.fieldList = fieldList;
	}

	public int getProjectID() {
		return projectID;
	}

	public ArrayList<Field> getFieldList() {
		return fieldList;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public void setFieldList(ArrayList<Field> fieldList) {
		this.fieldList = fieldList;
	}
	
	

}
