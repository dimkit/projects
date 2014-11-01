package database.helper;

import java.sql.SQLException;

public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1382705688668774980L;

	public DatabaseException(String string) {
		super(string);
	}

	public DatabaseException(String string, SQLException e) {
		super (string,e);
	}

}
