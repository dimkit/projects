package shared.model;
/**Represents a batch imported from the database.
 * 
 * @author Kristopher Miles
 *
 */
public class Batch {
	
	private int ID;
	private String imageFilePath;
	private int projectID;
	/** 0 = incomplete
	 *  1 = checked out
	 *  2 = complete
	 */
	private int state;
	

	public Batch(int iD, String imageFilePath, int projectID, int state) {
		super();
		ID = iD;
		this.imageFilePath = imageFilePath;
		this.projectID = projectID;
		this.state = state;
	}
	
	public int getID() {
		return ID;
	}
	public String getImageFilePath() {
		return imageFilePath;
	}
	public int getProjectID() {
		return projectID;
	}
	public int getState() {
		return state;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public void checkOut(){
		if(state==0){
			state++;
		}
		else if(state==1){
			state++;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((imageFilePath == null) ? 0 : imageFilePath.hashCode());
		result = prime * result + projectID;
		result = prime * result + state;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Batch other = (Batch) obj;
		if (imageFilePath == null) {
			if (other.imageFilePath != null)
				return false;
		} else if (!imageFilePath.equals(other.imageFilePath))
			return false;
		if (projectID != other.projectID)
			return false;
		if (state != other.state)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Batch [ID=" + ID + ", imageFilePath=" + imageFilePath
				+ ", projectID=" + projectID + ", state=" + state + "]";
	}
	
	
	

}
