package shared.model;
/**Represents a user imported from the database.
 * 
 * @author Kristopher Miles
 *
 */
public class User {
	private int ID;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private int indexedRecoreds;
	private int currentBatch;
	
	public User(int ID, String username, String password, String firstName,
			String lastName, String email, int curBatch, int indexedRec) {
		super();
		this.ID = ID;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.indexedRecoreds = indexedRec;
		this.currentBatch = curBatch;
	}
	
	
	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getIndexedRecoreds() {
		return indexedRecoreds;
	}
	public void setIndexedRecoreds(int indexedRecoreds) {
		this.indexedRecoreds = indexedRecoreds;
	}
	public void addIndexedRecords(int toAdd){
		indexedRecoreds+=toAdd;
	}


	public int getCurrentBatch() {
		return currentBatch;
	}


	public void setCurrentBatch(int currentBatch) {
		this.currentBatch = currentBatch;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + currentBatch;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + indexedRecoreds;
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (ID != other.ID)
			return false;
		if (currentBatch != other.currentBatch)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (indexedRecoreds != other.indexedRecoreds)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "User [ID=" + ID + ", username=" + username + ", password="
				+ password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", indexedRecoreds="
				+ indexedRecoreds + ", currentBatch=" + currentBatch + "]";
	}
	

}
