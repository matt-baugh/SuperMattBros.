package SMB.tools;

public class AvailibleServer{
	String IPAddress, serverName;
	int numberOfPlayers;
	public AvailibleServer(String ip, int playerNumber, String name){
		IPAddress = ip;
		numberOfPlayers = playerNumber;
		serverName = name;
	}
	
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
