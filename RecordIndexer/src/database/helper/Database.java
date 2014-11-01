package database.helper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.*;

/** The DatabaseAccess class is used to perform all interactions with the database. It returns pre-wrapped outputs, ready to send back to the client. 
@author Kristopher Miles
*/
public class Database {
	Connection currentConnection;
	private final String DATABASE_DIRECTORY = "database";
	private final String DATABASE_FILE = "main.sqlite";
	private final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_DIRECTORY +
												File.separator + DATABASE_FILE;
	public void initalize(){
		try{
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}catch(ClassNotFoundException e){
			System.out.println("CLASS NOT FOUND EXCEPTION");
		}
		
	}
	
	Connection getCurrentConnection(){
		return currentConnection;
	}
	void setCurrentConnection(Connection input){
		currentConnection = input;
	}
	public static void safeClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {}
		}
	}
	
	public static void safeClose(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {}
		}
	}
	public static void safeClose(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {}
		}
	}
	
	public static void safeClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {}
		}
	}
	public void startTransaction() throws DatabaseException {
		try {
			assert (currentConnection == null);			
			currentConnection = DriverManager.getConnection(DATABASE_URL);
			currentConnection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not connect to database. Make sure " + 
				DATABASE_FILE + " is available in ./" + DATABASE_DIRECTORY, e);
		}
	}
	
	public void endTransaction(boolean commit) {
		if (currentConnection != null) {		
			try {
				if (commit) {
					currentConnection.commit();
				}
				else {
					currentConnection.rollback();
				}
			}
			catch (SQLException e) {
				System.out.println("Could not end transaction");
				e.printStackTrace();
			}
			finally {
				safeClose(currentConnection);
				currentConnection = null;
			}
		}
	}
	public static void delete(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	    		if(file.list().length==0){
	    		   file.delete();	 
	    		}
	    		else{
	        	   String files[] = file.list();
	    	   for (String temp : files) {
	        	      File fileDelete = new File(file, temp);
	        	     delete(fileDelete);
	        	   }

	        	   if(file.list().length==0){
	           	     file.delete();
	        	   }
	    		}
	 
	    	}
	    	else{
	    		file.delete();
	    	}
	    }
	public static void copy(File source, File dest){
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			dest.delete();
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			inputChannel.close();
			outputChannel.close();
		}
		catch(IOException e){
			System.out.println("Source: "+source.getPath());
			System.out.println("Destination: "+dest.getPath());
			System.out.println("Failure to copy a file.");
			e.printStackTrace();
		}
		
	}
}
