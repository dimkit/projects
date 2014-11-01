package shared.com;

import java.util.ArrayList;

import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;

/**Contains all the information about a batch a user needs to work on it, wrapped for easy transfer. It does not contain authentication information, since this class only flows down from the server to the user.
 * 
 * @author Kristopher Miles
 *
 */
public class BatchWrapper {
	private Batch image;
	private Project parentProject;
	private ArrayList<Field> fieldList;
	
	public BatchWrapper(Batch image,Project inputProject,ArrayList<Field> arrayList) {
		super();
		this.image = image;
		this.parentProject = inputProject;
		this.fieldList = arrayList;
	}

	public Project getParentProject() {
		return parentProject;
	}

	public ArrayList<Field> getFieldList() {
		return fieldList;
	}

	public void setParentProject(Project parentProject) {
		this.parentProject = parentProject;
	}

	public void setFieldList(ArrayList<Field> fieldList) {
		this.fieldList = fieldList;
	}

	public Batch getImage() {
		return image;
	}

	public void setImage(Batch image) {
		this.image = image;
	}

}
