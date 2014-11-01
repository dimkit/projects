package shared.com;

import java.util.ArrayList;

/**Stores all the information nessisary to conduct the search operation on the server.
 * 
 * @author Kristopher Miles
 *
 */
public class SearchInputWrapper {
	private ArrayList<String> terms;
	private ArrayList<Integer> fieldIDs;
	private AuthenticationWrapper auth;
	
	public SearchInputWrapper(ArrayList<String> terms,
			ArrayList<Integer> fieldIDs, AuthenticationWrapper auth) {
		super();
		this.terms = terms;
		this.fieldIDs = fieldIDs;
		this.auth = auth;
	}
	public AuthenticationWrapper getAuth() {
		return auth;
	}
	
	public void setAuth(AuthenticationWrapper auth) {
		this.auth = auth;
	}
	public ArrayList<String> getTerms() {
		return terms;
	}
	public ArrayList<Integer> getFieldIDs() {
		return fieldIDs;
	}
	public void setTerms(ArrayList<String> terms) {
		this.terms = terms;
	}
	public void setFieldIDs(ArrayList<Integer> fieldIDs) {
		this.fieldIDs = fieldIDs;
	}
}
