package database.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import shared.model.User;

public class UserDAO {

	private Database parentDatabase;
	
	public UserDAO (Database c){
		parentDatabase =c;
	}
	
	/**Searches the database for a user matching the username and password sent to this method. If the user is
	 * inside, returns the object constructed from their information.
	 * @param username Username to validate
	 * @param password Password to validate
	 * @return The user object corresponding to the input username in the database, or null if they don't exist.
	 */
	public User findUser(String usrName, String pWord) throws DatabaseException {
		//logger.entering("server.database.Contacts", "getAll");
		User output=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select ID, PASSWORD, FIRSTNAME, LASTNAME, RECORDCOUNT, EMAIL, CURRENT_BATCH from USER where username='"+usrName+"'";
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			if (rs.next()&&pWord.equals(rs.getString(2))) {
				int id = rs.getInt(1);
				String username = usrName;
				String password = rs.getString(2);
				String firstName = rs.getString(3);
				String lastName = rs.getString(4);
				int recordCount = rs.getInt(5);
				String email = rs.getString(6);
				int current_Batch = rs.getInt(7);
				output = new User(id, username, password, firstName, lastName, email, current_Batch, recordCount);
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			//logger.throwing("server.database.Contacts", "getAll", serverEx);
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		//logger.exiting("server.database.Contacts", "getAll");
		
		return output;	
	}
	/**Searches the database for the user represented by the object given. If they can't be found, add them to the database.
	 * 
	 * @param inputUser User to search for.
	 */
	public void addUser(User inputUser) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into USER (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, RECORDCOUNT, EMAIL, CURRENT_BATCH) values (?, ?, ?, ?, ?, ?, ?)";
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			stmt.setString(1, inputUser.getUsername());
			stmt.setString(2, inputUser.getPassword());
			stmt.setString(3, inputUser.getFirstName());
			stmt.setString(4, inputUser.getLastName());
			stmt.setInt(5, inputUser.getIndexedRecoreds());
			stmt.setString(6, inputUser.getEmail());
			stmt.setInt(7, inputUser.getCurrentBatch());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = parentDatabase.getCurrentConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				inputUser.setID(id);
			}
			else {
				throw new DatabaseException("Could not insert");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	public void updateUser(User inputUser) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "update USER set PASSWORD='"+inputUser.getPassword()+"', FIRSTNAME='"+inputUser.getFirstName()+"', LASTNAME='"+inputUser.getLastName()+"', RECORDCOUNT="+
					inputUser.getIndexedRecoreds()+", EMAIL='"+inputUser.getEmail()+"', CURRENT_BATCH="+inputUser.getCurrentBatch()+" where USERNAME='"+inputUser.getUsername()+"'"; 
			stmt = parentDatabase.getCurrentConnection().prepareStatement(query);
			
			if (stmt.executeUpdate() == 1) {
				System.out.println("It worked!");
			}
			else {
				throw new DatabaseException("Could not update");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
}
