package shared.model;
/**Represents a field object taken from the database.
 * 
 * @author Kristopher Miles
 *
 */
public class Field {
	private String title;
	private int ID;
	private int xCord;
	private int width;
	private String helpHtml;
	private String knownData;
	private int projectID;
	private int colNumber;
	
	public Field(int ID, int projectID, String title, int xCord, int width, String helpHtml,
			String knownData, int colNumber) {
		super();
		this.ID=ID;
		this.title = title;
		this.xCord = xCord;
		this.width = width;
		this.helpHtml = helpHtml;
		this.knownData = knownData;
		this.projectID = projectID;
		this.colNumber = colNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getxCord() {
		return xCord;
	}
	public void setxCord(int xCord) {
		this.xCord = xCord;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getHelpHtml() {
		return helpHtml;
	}
	public void setHelpHtml(String helpHtml) {
		this.helpHtml = helpHtml;
	}
	public String getKnownData() {
		return knownData;
	}
	public void setKnownData(String knownData) {
		this.knownData = knownData;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getColNumber() {
		return colNumber;
	}
	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colNumber;
		result = prime * result
				+ ((helpHtml == null) ? 0 : helpHtml.hashCode());
		result = prime * result
				+ ((knownData == null) ? 0 : knownData.hashCode());
		result = prime * result + projectID;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + width;
		result = prime * result + xCord;
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
		Field other = (Field) obj;
		if (colNumber != other.colNumber)
			return false;
		if (helpHtml == null) {
			if (other.helpHtml != null)
				return false;
		} else if (!helpHtml.equals(other.helpHtml))
			return false;
		if (knownData == null) {
			if (other.knownData != null)
				return false;
		} else if (!knownData.equals(other.knownData))
			return false;
		if (projectID != other.projectID)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (width != other.width)
			return false;
		if (xCord != other.xCord)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Field [title=" + title + ", ID=" + ID + ", xCord=" + xCord
				+ ", width=" + width + ", helpHtml=" + helpHtml
				+ ", knownData=" + knownData + ", projectID=" + projectID
				+ ", colNumber=" + colNumber + "]";
	}
	
	

}
