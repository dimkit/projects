package importer;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import shared.model.Batch;
import shared.model.Cell;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;
import database.helper.BatchDAO;
import database.helper.CellDAO;
import database.helper.Database;
import database.helper.DatabaseBuilder;
import database.helper.DatabaseException;
import database.helper.FieldDAO;
import database.helper.ProjectDAO;
import database.helper.UserDAO;

public class DatabaseImporter {
	Document curDoc;
	Database myDatabase;
	public DatabaseImporter(){
		curDoc = null;
		myDatabase = new Database();
		myDatabase.initalize();
	}
	/**Builds the input XML into a tree for parsing.
	 * 
	 * @param fileName XML to parse
	 * @return true if the operation was successful, false if it fails for whatever reason (such as a bad XML file)
	 */
	private boolean buildDoc(String fileName){
		boolean worked = true;
		try { 
			File file = new File(fileName);
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
		                             .newDocumentBuilder();
			curDoc = dBuilder.parse(file);

		    } catch (Exception e) {
			worked = false;
		    }
		return worked;
	}
	/**Parses the XML file into the database.
	 * Function splits in two, parsing the users first and then the projects. Users are easy to parse, projects are split a little more.
	 * 
	 */
	private void parse(){
		Node root = curDoc.getDocumentElement();
		Node curNode = root.getFirstChild();
		try {
			myDatabase.startTransaction();
		} catch (DatabaseException e) {
			e.printStackTrace();
			System.out.println("FAILURE TO START TRANSACTION");
			curNode=null;
		}

		while(curNode!=null){
			if (curNode.getNodeType() == Node.ELEMENT_NODE) {
				//Handle the users.
				if(curNode.getNodeName().equalsIgnoreCase("users")){
					curNode = parseUsers(curNode);
				}
				//Now we handle the projects.
				if(curNode.getNodeName().equalsIgnoreCase("projects")){
					curNode = curNode.getFirstChild();
					while(curNode!=null){
						if(curNode.getNodeName().equalsIgnoreCase("project")){
							parseProject(curNode);
						}
						curNode = curNode.getNextSibling();
					}
				}
			}
			if(curNode!=null){
				curNode=curNode.getNextSibling();
			}
		}

		myDatabase.endTransaction(true);

	}
	
	private Node parseUsers(Node input){
		Node projects = input.getNextSibling();
		input=input.getFirstChild();
		boolean done = false;
		
		while(!done){
			if (input.getNodeType() == Node.ELEMENT_NODE){
				addUser(input.getTextContent());
			}
			input = input.getNextSibling();
			
			if(input==null){
				done=true;
			}

		}
		
		return projects;
	}
	private void addUser(String in){
		Scanner scan = new Scanner(in);
		String userName = null, password = null, firstName = null, lastName = null, email = null;
		int indexedRecords = 0;
		
		userName = scan.next();
		password = scan.next();
		firstName = scan.next();
		lastName = scan.next();
		email = scan.next();
		indexedRecords = scan.nextInt();
		
		try {
			UserDAO temp = new UserDAO(myDatabase);
			if(userName!=null&&password!=null&&firstName!=null&&lastName!=null&&email!=null){
				User tempUser = new User(-1,userName,password,firstName,lastName,email,-1,indexedRecords);
				temp.addUser(tempUser);
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scan.close();
		
	}

	/**
	 * Processes a project node and adds it and everything inside it to the database.
	 * @param input
	 */
	private void parseProject(Node input){
		String title;
		int recordsPerImage, firstYCord, recordHeight;
		Node tempNode = input.getFirstChild().getNextSibling();
		//first, we need to retrieve the initial values from the project, so it can be added and we can get its index.
		title = tempNode.getTextContent();
		tempNode = tempNode.getNextSibling().getNextSibling();
		recordsPerImage = Integer.parseInt(tempNode.getTextContent());
		tempNode = tempNode.getNextSibling().getNextSibling();
		firstYCord = recordsPerImage = Integer.parseInt(tempNode.getTextContent());
		tempNode = tempNode.getNextSibling().getNextSibling();
		recordHeight = recordsPerImage = Integer.parseInt(tempNode.getTextContent());
		
		//Now we know everything we need to know to build a temporary project to work from
		Project workingProject = new Project (-1,title,recordsPerImage,firstYCord,recordHeight);
		ProjectDAO tempDAO = new ProjectDAO(myDatabase);
		
		try {
			tempDAO.addProject(workingProject);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Now that we have added the project to the database we can process the images and fields.
		tempNode = tempNode.getNextSibling().getNextSibling();
		processFields(tempNode, workingProject.getID());
		tempNode = tempNode.getNextSibling().getNextSibling();
		processImages(tempNode, workingProject.getID());
				
	}
	/**Used to process through the inside of the fields and add them to the table.
	 * 
	 * @param tempNode
	 * @param inputProject
	 */
	private void processFields(Node input, int inputProject){
		Node tempNode = input.getFirstChild().getNextSibling();
		int index = 1;
		while (tempNode!=null){

			if(tempNode.getNodeName().equals("field")){
				addField(tempNode,inputProject,index);
				index++;
			}
			tempNode=tempNode.getNextSibling();
		}
	}
	/**Adds an individual field to the database.
	 * 
	 * @param input
	 * @param inputProject
	 */
	private void addField(Node input, int inputProject,int index){
		input =input.getFirstChild().getNextSibling();
		String title = input.getTextContent();
		input =input.getNextSibling().getNextSibling();
		int xCord = Integer.parseInt(input.getTextContent());
		input =input.getNextSibling().getNextSibling();
		int width = Integer.parseInt(input.getTextContent());
		input =input.getNextSibling().getNextSibling();
		String helpHTML = input.getTextContent();
		helpHTML = processHelp(helpHTML);
		input =input.getNextSibling().getNextSibling();
		String known = "";
		try{
			known = input.getTextContent();
			known = processKnown(known);
		
		}catch(NullPointerException e){
			known="";
		}
		
		FieldDAO tempDAO = new FieldDAO (myDatabase);
		Field tempField = new Field(-1,inputProject,title,xCord,width,helpHTML,known,index);

		try {
			tempDAO.addField(tempField);
		} catch (DatabaseException e) {
			System.out.println("Failed to add field: "+tempField);
			e.printStackTrace();
		}
		
	}
	/**Processes the file from the given location, copying it into the local data structure. 
	 * 
	 * @param inputDest
	 * @return location in server where file is stored.
	 */
	private String processHelp(String inputDest){
		int lastSlash =inputDest.lastIndexOf("/");
		String finalDest = "database\\helps\\"+inputDest.substring(lastSlash+1);
		Database.copy((new File (inputDest)), (new File (finalDest)));
		
		return finalDest;
	}
	private String processKnown(String inputDest){
		int lastSlash =inputDest.lastIndexOf("/");
		String finalDest = "database\\known\\"+inputDest.substring(lastSlash+1);
		Database.copy((new File (inputDest)), (new File (finalDest)));
		
		
		return finalDest;
	}
		
	/**Iterates through the images and 
	 * 
	 * @param tempNode
	 * @param inputProject
	 */
	private void processImages(Node tempNode, int inputProject){
		tempNode = tempNode.getFirstChild().getNextSibling();
		while(tempNode!=null){
			if(tempNode.getNodeName().equals("image")){
				addImage(tempNode,inputProject);
			}
			tempNode = tempNode.getNextSibling();
		}
		
	}
	/**Adds an individual image and every record in contains.
	 * 
	 * @param input
	 * @param projectIndex
	 */
	private void addImage(Node input, int projectIndex){
		//First we need to create a batch for this image to work from.
		input = input.getFirstChild().getNextSibling();
		String filePath = input.getTextContent();
		filePath = processImage(filePath);
		Batch workingImage = new Batch (-1,filePath,projectIndex,0);
		BatchDAO tempDAO = new BatchDAO(myDatabase);
		try {
			tempDAO.addBatch(workingImage);
		} catch (DatabaseException e) {
			System.out.println("Could not add Batch: "+workingImage);
			e.printStackTrace();
		}
		//Now we need to see if there are records, and add them to the table if they exist.
		input = input.getNextSibling().getNextSibling();
		if(input!=null&&input.getNodeName().equals("records")){

			workingImage.setState(2);
			try {
				tempDAO.updateBatch(workingImage);
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addRecords(input,projectIndex,workingImage.getID());
			
		}
	}
	
	private String processImage(String inputDest){
		int lastSlash =inputDest.lastIndexOf("/");
		String finalDest = "database\\images\\"+inputDest.substring(lastSlash+1);
		Database.copy((new File (inputDest)), (new File (finalDest)));
		
		
		return finalDest;
	}
	
	private void addRecords(Node input, int projectIndex, int batchIndex){
		input = input.getFirstChild().getNextSibling();
		while(input!=null){
			if(input.getNodeName().equals("record")){

				addRecord(input,projectIndex,batchIndex);
				
			}
			input=input.getNextSibling();
		}
	}
	
	private void addRecord(Node input, int projectIndex, int batchIndex){
		FieldDAO fieldDAO = new FieldDAO(myDatabase);
		CellDAO cellDAO = new CellDAO(myDatabase);
		ArrayList<Field> myFields=new ArrayList<Field>();
		//To add the records correctly, we need to know the field IDs for the project.
		try {
			 myFields= (ArrayList<Field>) fieldDAO.getFields(projectIndex);
		} catch (DatabaseException e) {
			System.out.println("Unable to get fields: "+input);
			e.printStackTrace();
		}
		int[] fieldIDs = new int[myFields.size()];
		for(int i=0;i<myFields.size();i++){
			fieldIDs[i]=myFields.get(i).getID();

		}
		Node value = input.getFirstChild().getNextSibling().getFirstChild().getNextSibling();

		
		for(int i=0;i<myFields.size();i++){
			String val = value.getTextContent();
			Cell temp = new Cell (val,-1,(i+1),fieldIDs[i],batchIndex);
			try {
				cellDAO.addCell(temp);
			} catch (DatabaseException e) {
				System.out.println("Could not add cell: "+temp);
				e.printStackTrace();
			}
			value=value.getNextSibling();
			if(value!=null){
				value =value.getNextSibling();
			}
		}
		
		
	}
	
	public void run(String path){		
		DatabaseBuilder tempDatabaseBuilder = new DatabaseBuilder();
		tempDatabaseBuilder.buildDatabaseFromScratch();
		String filename = path;
		buildDoc(filename);
		parse();
	}
	
	public static void main(String[] args) {
		DatabaseImporter in = new DatabaseImporter();
		String filename = null;
		boolean error = false;
		try{
			filename = args[0];
			in.buildDoc(filename);
		}catch(Exception e){
			System.out.println("USAGE: DatabaseImporter.java pathToXML");
			e.printStackTrace();
			error = true;
		}
		if(!error){
			in.parse();
		}
	}	
}