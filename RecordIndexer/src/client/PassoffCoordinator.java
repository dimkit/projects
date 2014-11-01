package client;

import java.util.ArrayList;

import shared.com.BatchWrapper;
import shared.com.FieldListWrapper;
import shared.com.ImageWrapper;
import shared.com.ProjectListWrapper;
import shared.com.ResultWrapper;
import shared.com.SearchResultsWrapper;
import shared.model.Field;
import shared.model.ProjectTuple;
import shared.model.User;

public class PassoffCoordinator {
	public static String validateUser(User input){
		if(input==null){
			return "FALSE\n";
		}
		return "TRUE\n"+input.getFirstName()+"\n"+input.getLastName()+"\n"+input.getIndexedRecoreds()+"\n";
	}

	public static String getProjects (ProjectListWrapper input){
		if(input==null){
			return "FAILED\n";
		}
		ArrayList<ProjectTuple> projList = (ArrayList<ProjectTuple>) input.getProjectList();
		String output = "";
		for(int i=0;i<projList.size();i++){
			output+=""+projList.get(i).getID()+"\n";
			output+=""+projList.get(i).getName()+"\n";
		}
		return output;
	}

	public static String getSampleImage(ImageWrapper image){
		if(image==null){
			return"FAILED\n";
		}
		return image.getPicture();
	}

	public static String downloadBatch(BatchWrapper batch){
		if(batch==null){
			return "FAILED\n";
		}
		String output = ""+batch.getImage().getID()+"\n";
		output+=batch.getParentProject().getID()+"\n";
		output+=batch.getImage().getImageFilePath()+"\n";
		output+=batch.getParentProject().getFirstYCord()+"\n";
		output+=batch.getParentProject().getRecordHeight()+"\n";
		output+=batch.getParentProject().getRecordsPerImage()+"\n";
		output+=batch.getFieldList().size()+"\n";
		
		ArrayList<Field> fields = batch.getFieldList();
		for(int i=0;i<fields.size();i++){
			Field temp = fields.get(i);
			output+=temp.getID()+"\n";
			output+=i+"\n";
			output+=temp.getTitle()+"\n";
			output+=temp.getHelpHtml()+"\n";
			output+=temp.getxCord()+"\n";
			output+=temp.getWidth()+"\n";
			if(!temp.getKnownData().equals("")){
				output+=temp.getKnownData()+"\n";
			}
		}
		return output;
	}

	public static String submitBatch(boolean worked){
		if(worked){
			return"TRUE\n";
		}
		return"FAILED\n";
	}

	public static String getFields(FieldListWrapper fields){
		if(fields==null){
			return "FAILED\n";
		}
		ArrayList<Field> fieldList = fields.getFieldList();
		String output="";
		for(int i=0;i<fieldList.size();i++){
			output+=""+fieldList.get(i).getProjectID()+"\n";
			output+=""+fieldList.get(i).getID()+"\n";
			output+=""+fieldList.get(i).getTitle()+"\n";
		}
		return output;
	}

	public static String search (SearchResultsWrapper input){
		if(input==null){
			return "FAILED\n";
		}
		ArrayList<ResultWrapper> results = input.getResultList();
		String output = "";
		for(int i=0;i<results.size();i++){
			ResultWrapper temp = results.get(i);
			output+=temp.getBatchID()+"\n";
			output+=temp.getImageURL()+"\n";
			output+=temp.getRecordNumber()+"\n";
			output+=temp.getFieldID()+"\n";
		}	
		return output;
	}
}