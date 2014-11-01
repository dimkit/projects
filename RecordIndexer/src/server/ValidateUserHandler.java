package server;

import java.io.IOException;
import java.net.HttpURLConnection;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import database.helper.DatabaseException;
import shared.com.AuthenticationWrapper;
import shared.model.User;

public class ValidateUserHandler implements HttpHandler {
	
	private XStream xmlStream = new XStream(new DomDriver());
	private Logger logger = Logger.getLogger("indexer"); 

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		AuthenticationWrapper params = (AuthenticationWrapper)xmlStream.fromXML(exchange.getRequestBody());
		logger.log(Level.INFO, "Successfully recieved the following username and password: "+params.getUsername()+" "+params.getPassword());
		User output = null;
		try {
			output = ServerFacade.validateUser(params);
		} catch (DatabaseException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			System.out.println("Internal server errror validating user.");
			
			return;
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(output, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}
}
