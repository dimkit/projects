package shared.com;

import java.util.ArrayList;

/**Stores the results of a search to return to the user.
 * 
 * @author Kristopher Miles
 *
 */
public class SearchResultsWrapper {
	private ArrayList<ResultWrapper> resultList;

	public SearchResultsWrapper(ArrayList<ResultWrapper> resultList) {
		super();
		this.resultList = resultList;
	}

	public ArrayList<ResultWrapper> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<ResultWrapper> resultList) {
		this.resultList = resultList;
	}
	
	
}
