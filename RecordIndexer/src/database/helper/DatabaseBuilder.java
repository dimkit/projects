package database.helper;
import java.io.File;
import java.io.IOException;
import java.sql.*;
/**Used to build a fresh database if the database does not yet exists.
 * 
 * @author Kristopher Miles
 *
 */
public class DatabaseBuilder {
	
	public DatabaseBuilder() {
		// TODO Auto-generated constructor stub
	}
	/**Builds the database from scratch, deleting everything present there if there is any information there. Do not use unless you're willing to lose everything!
	 * 
	 */
	public void buildDatabaseFromScratch(){
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:database\\main.sqlite");
	      stmt = c.createStatement();
	      //Make the USER table.
	      String sql = "DROP TABLE IF EXISTS USER";
	      stmt.executeUpdate(sql);
	      sql = "DROP TABLE IF EXISTS FIELD";
	      stmt.executeUpdate(sql);
	      sql = "DROP TABLE IF EXISTS BATCH";
	      stmt.executeUpdate(sql);
	      sql = "DROP TABLE IF EXISTS CELL";
	      stmt.executeUpdate(sql);
	      sql = "DROP TABLE IF EXISTS PROJECT";
	      stmt.executeUpdate(sql);
	      sql = "CREATE TABLE USER " +
	                   "(ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL," +
	                   " USERNAME           TEXT     NOT NULL, " + 
	                   " PASSWORD           TEXT     NOT NULL, " + 
	                   " FIRSTNAME        TEXT, " +
	                   " LASTNAME         TEXT," +
	                   " RECORDCOUNT           INT, " + 
	                   " EMAIL           TEXT, " + 
	                   " CURRENT_BATCH        INT)" ; 
	      stmt.executeUpdate(sql);
	      //Make the FIELD table.
	      stmt = c.createStatement();
	      sql = "CREATE TABLE FIELD " +
                  "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                  " PROJECTID           INT, " + 
                  " HELPPATH           TEXT, " + 
                  " KNOWNDATAFILE        TEXT, " +
                  " XCORD         INT," +
                  " WIDTH           INT, " + 
                  " COLNUMBER           INT, " + 
                  " TITLE        TEXT)" ; 
	      stmt.executeUpdate(sql);
	      //Create the BATCH table.
	      sql = "CREATE TABLE BATCH " +
                  "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                  " IMAGEFILEPATH           TEXT, " + 
                  " PROJECTID           INT, " + 
                  " STATE        INT)"; 
	      stmt.executeUpdate(sql);
	      //Create the CELL table.
	      sql = "CREATE TABLE CELL " +
                  "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                  " DATA           TEXT, " + 
                  " FIELD_ID           INT, " + 
                  " ROWNUM        INT, " +
                  " BATCH_ID         INT)"; 
	      stmt.executeUpdate(sql);
	     //Create the PROJECT table.
	      sql = "CREATE TABLE PROJECT " +
                  "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                  " NAME           TEXT, " + 
                  " RECORDSPERBATCH           INT, " + 
                  " FIRSTY        INT, " +
                  " HEIGHT         INT)";
	      stmt.executeUpdate(sql);
	      
	      stmt.close();
	      c.close();} 
	    
	    catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    clearAndBuildDirectories();
	    
	  }

	private void clearAndBuildDirectories() {
		File images = new File("database\\images");
		File helps = new File("database\\helps");
		File known = new File("database\\known");
		try {
			Database.delete(images);
			Database.delete(helps);
			Database.delete(known);
		} catch (IOException e) {
			System.out.println("Cannot delete directory");
			e.printStackTrace();
		}
		images.mkdir();
		helps.mkdir();
		known.mkdir();
		
	}
	public static void main (String [] args){
		DatabaseBuilder tempData= new DatabaseBuilder();
		tempData.buildDatabaseFromScratch();
	}
}
