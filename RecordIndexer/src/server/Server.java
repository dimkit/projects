package server;

import java.io.*;
import java.net.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.sun.net.httpserver.*;


public class Server {
	private static Logger logger;
	private static int SERVER_PORT_NUMBER = 1337;
	private static int MAX_WAITING_CONNECTIONS = 15;
	ServerFacade facade = new ServerFacade();	
	private HttpServer server;
	
	public Server() {
		return;
	}
	
	private void run() {
		System.out.println("Attemping to initalize server.");
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {	
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		server.setExecutor(null);
		server.createContext("/ValidateUser", validateUser);
		
		server.start();
	}
	private static void initLog() throws IOException {
		
		Level logLevel = Level.FINE;
		logger = Logger.getLogger("indexer"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);
		FileHandler fileHandler = new FileHandler("indexerServerLog.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	private HttpHandler validateUser = new ValidateUserHandler();
	/*private HttpHandler addContactHandler = new AddContactHandler();
	private HttpHandler updateContactHandler = new UpdateContactHandler();
	private HttpHandler deleteContactHandler = new DeleteContactHandler();*/
	
	public static void main(String[] args) throws IOException {
		initLog();
		try{
			if(args[0]!=null){
				int port = Integer.parseInt(args[0]);
				new Server().run(port);
			}
		}catch(ArrayIndexOutOfBoundsException e){}
		new Server().run();
	}

	public void run(int port) {
		SERVER_PORT_NUMBER = port;
		run();	
	}

}
