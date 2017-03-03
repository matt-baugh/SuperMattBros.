package SMB.tools;

public class AvailibleServer{
	//Declares variables
	String IPAddress, serverName;
	int numberOfPlayers;
	public AvailibleServer(String ip, int playerNumber, String name){
		//Initialises variables
		IPAddress = ip;
		numberOfPlayers = playerNumber;
		serverName = name;
	}
	//getters
	public int getNumberOfPlayers(){
		return numberOfPlayers;
	}
	
	public String getIPAddress(){
		return IPAddress;
	}
	public String getServerName(){
		return serverName;
	}
}
