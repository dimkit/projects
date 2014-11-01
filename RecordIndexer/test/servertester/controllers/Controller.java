package servertester.controllers;

import java.util.*;

import client.Communicator;
import client.PassoffCoordinator;
import servertester.views.*;

public class Controller implements IController {

	private IView _view;
	private Communicator communicator = null;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("1337");
		operationSelected();
		communicator= new Communicator();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {
		String usrName = _view.getParameterValues()[0];
		String pasWrd = _view.getParameterValues()[1];
		String output = PassoffCoordinator.validateUser(communicator.validateUser(usrName, pasWrd));
		_view.setResponse(output);
	}
	
	private void getProjects() {
		String usrName = _view.getParameterValues()[0];
		String pasWrd = _view.getParameterValues()[1];
		String output = PassoffCoordinator.getProjects((communicator.getProjects(usrName, pasWrd)));
		_view.setResponse(output);
	}
	
	private void getSampleImage() {
		String usrName = _view.getParameterValues()[0];
		String pasWrd = _view.getParameterValues()[1];
		int id = Integer.parseInt(_view.getParameterValues()[2]);
		String output = PassoffCoordinator.getSampleImage((communicator.getSampleImage(usrName, pasWrd,id)));
		_view.setResponse(output);
	}
	
	private void downloadBatch() {
		String usrName = _view.getParameterValues()[0];
		String pasWrd = _view.getParameterValues()[1];
		int id = Integer.parseInt(_view.getParameterValues()[2]);
		String output = PassoffCoordinator.downloadBatch((communicator.downloadBatch(usrName, pasWrd,id)));
		_view.setResponse(output);
	}
	
	private void getFields() {
		String usrName = _view.getParameterValues()[0];
		String pasWrd = _view.getParameterValues()[1];
		int id = Integer.parseInt(_view.getParameterValues()[2]);
		String output = PassoffCoordinator.getFields((communicator.getFields(usrName, pasWrd,id)));
		_view.setResponse(output);
	}
	
	private void submitBatch() {
		String usrName = _view.getParameterValues()[0];
		String pasWrd = _view.getParameterValues()[1];
		int batch = Integer.parseInt(_view.getParameterValues()[2]);
		String valueList = _view.getParameterValues()[3];
		String output = PassoffCoordinator.submitBatch((communicator.submitBatch(usrName, pasWrd, batch,valueList)));
		_view.setResponse(output);
	}
	

	private void search() {
		String usrName = _view.getParameterValues()[0];
		String pasWrd = _view.getParameterValues()[1];
		Scanner scan = new Scanner(_view.getParameterValues()[2]);
		scan.useDelimiter(",");
		ArrayList<Integer> fields = new ArrayList<Integer>();
		while(scan.hasNext()){
			fields.add(scan.nextInt());
		}
		scan.close();
		Scanner scan2 = new Scanner(_view.getParameterValues()[3]);
		scan.useDelimiter(",");
		ArrayList<String> terms = new ArrayList<String>();
		while(scan2.hasNext()){
			terms.add(scan2.next());
		}
		
		scan2.close();
		String output = PassoffCoordinator.search((communicator.search(usrName,pasWrd,fields,terms)));
		_view.setResponse(output);		
	}

}

