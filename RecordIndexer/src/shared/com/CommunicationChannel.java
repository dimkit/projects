package shared.com;

/**Performs the communication operations required by both the client and server.
 * 
 * @author Kristopher Miles
 *
 */
public class CommunicationChannel {
//	private String destination;
	
	public CommunicationChannel(String dest){
	//	destination = dest;
	}
	
	/**Establishes a connection with the previously initalized location and sends the object to that location.
	 * 
	 * @param input The object to send through the connection.
	 * @return If this send operation results in an object being sent back, that object is returned. If nothing is returned, return null.
	 */
	public Object send(Object input){
		return null;
	}
	/**Prepares the subject of this connection to perform the command sent.
	 * 
	 * @param command The command the party at the other end of the connection must perform.
	 * @return true when the party at the other end is ready for the connection to send the object required to carry out the command.
	 */
	public boolean prepareForCommand(Command command){
		return false;
	}
}
