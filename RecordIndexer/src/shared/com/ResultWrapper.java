package shared.com;
/**Stores a single search result from a search, along with all required information.
 * 
 * @author Kristopher Miles
 *
 */
public class ResultWrapper {
	private int batchID;
	private String imageURL;
	private int recordNumber;
	private int fieldID;
	public ResultWrapper(int batchID, String imageURL, int recordNumber,
			int fieldID) {
		super();
		this.batchID = batchID;
		this.imageURL = imageURL;
		this.recordNumber = recordNumber;
		this.fieldID = fieldID;
	}
	public int getBatchID() {
		return batchID;
	}
	public String getImageURL() {
		return imageURL;
	}
	public int getRecordNumber() {
		return recordNumber;
	}
	public int getFieldID() {
		return fieldID;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}
	
	
}
