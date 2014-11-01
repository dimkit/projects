package shared.com;

/**Used to send a user's project selection to the server. Contains the project ID selected and the user's autentication information.
 * 
 * @author Kristopher Miles
 *
 */
public class ProjectChoiceWrapper {
	private AuthenticationWrapper auth;
	private int project;
	
	public ProjectChoiceWrapper(AuthenticationWrapper auth, int project) {
		super();
		this.auth = auth;
		this.project = project;
	}
	
	public AuthenticationWrapper getAuth() {
		return auth;
	}
	public int getProject() {
		return project;
	}
	public void setAuth(AuthenticationWrapper auth) {
		this.auth = auth;
	}
	public void setProject(int project) {
		this.project = project;
	}
	
	

}
