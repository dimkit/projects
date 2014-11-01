package client;

import static org.junit.Assert.*;
import org.junit.*;

import shared.model.User;

public class CommunicatorTests {
	private static Communicator comunicate;
	
	@BeforeClass
	public static void setUp(){

		comunicate = new Communicator();
	}
	
	@Test
	public void testValidate(){
		User user = comunicate.validateUser("test1", "test1");
		assertNotNull(user);
	}
}
