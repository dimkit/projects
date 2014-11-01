package shared.model;


/**Represents a project imported from the database.
 * 
 * @author Kristopher Miles
 *
 */
public class Project {
	private int ID;
	private String title;
	private int recordsPerImage;
	private int firstYCord;
	private int recordHeight;

	
	public Project(int iD, String title, int recordsPerImage, int firstYCord,
			int recordHeight) {
		super();
		ID = iD;
		this.title = title;
		this.recordsPerImage = recordsPerImage;
		this.firstYCord = firstYCord;
		this.recordHeight = recordHeight;
	}
	public int getID() {
		return ID;
	}
	public String getTitle() {
		return title;
	}
	public int getRecordsPerImage() {
		return recordsPerImage;
	}
	public int getFirstYCord() {
		return firstYCord;
	}
	public int getRecordHeight() {
		return recordHeight;
	}

	public void setID(int iD) {
		ID = iD;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setRecordsPerImage(int recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
	}
	public void setFirstYCord(int firstYCord) {
		this.firstYCord = firstYCord;
	}
	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}
	
	public static ProjectTuple makeTuple (Project input){
		return new ProjectTuple(input.getTitle(),input.getID());
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + firstYCord;
		result = prime * result + recordHeight;
		result = prime * result + recordsPerImage;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Project other = (Project) obj;
		if (firstYCord != other.firstYCord)
			return false;
		if (recordHeight != other.recordHeight)
			return false;
		if (recordsPerImage != other.recordsPerImage)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Project [ID=" + ID + ", title=" + title + ", recordsPerImage="
				+ recordsPerImage + ", firstYCord=" + firstYCord
				+ ", recordHeight=" + recordHeight + "]";
	}
	
	

}
