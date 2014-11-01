package shared.model;
/**Represents a cell object imported from the database.
 * 
 * @author Kristopher Miles
 *
 */
public class Cell {
	private String data;
	private int ID;
	private int rowNumber;
	private int fieldID;
	private int batchID;
	public Cell(String data, int iD, int rowNumber, int fieldID,
			int batchID) {
		super();
		this.data = data;
		ID = iD;
		this.rowNumber = rowNumber;
		this.fieldID = fieldID;
		this.batchID = batchID;
	}
	public String getData() {
		return data;
	}
	public int getID() {
		return ID;
	}
	public int getRowNumber() {
		return rowNumber;
	}

	public void setData(String data) {
		this.data = data;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public int getFieldID() {
		return fieldID;
	}
	public int getBatchID() {
		return batchID;
	}
	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + batchID;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + fieldID;
		result = prime * result + rowNumber;
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
		Cell other = (Cell) obj;
		if (batchID != other.batchID)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (fieldID != other.fieldID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Cell [data=" + data + ", ID=" + ID + ", rowNumber=" + rowNumber
				+ ", fieldID=" + fieldID + ", batchID=" + batchID + "]";
	}
	
	

}
