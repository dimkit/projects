package shared.com;


import java.util.List;


import shared.model.ProjectTuple;
/**Contains the list of all projects stored on the server.
 * 
 * @author Kristopher Miles
 *
 */
public class ProjectListWrapper {
	private List<ProjectTuple> projectList;

	public ProjectListWrapper(List<ProjectTuple> list) {
		super();
		this.projectList = list;
	}
	
	public List<ProjectTuple> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<ProjectTuple> projectList) {
		this.projectList = projectList;
	}
	
	

}
