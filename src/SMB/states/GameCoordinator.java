package SMB.states;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import SMB.main.Resources;
import SMB.tools.AvailibleServer;

public class GameCoordinator extends BasicGameState{
	//declare variables
	public Image background, smallButton, largeButton;
	public ArrayList<AvailibleServer> availibleServers;
	public Thread searcher;
	public int firstServerButtonX, firstServerButtonY, 
	smallButtonWidth, largeButtonWidth, buttonHeight,
	refreshButtonX, refreshButtonY;

	@Override
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		//initialise variables
		availibleServers = new ArrayList<AvailibleServer>();
		background  = Resources.getImage("gameCoordinatorBackground");
		smallButton = Resources.getImage("smallButton");
		largeButton  = Resources.getImage("largeButton");

		
		//sets coordinates and values for buttons
		firstServerButtonX = 400;
		firstServerButtonY = 400;
		smallButtonWidth = 210;
		largeButtonWidth = 440;
		buttonHeight = 120;

		refreshButtonX = 560;
		refreshButtonY = 775;
		
		//starts thread searching for availible games
		searcher = new Thread(new searchForGames());
		searcher.start();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		//draws background
		background.draw(0,0);

		//draws refresh search button
		largeButton.draw(refreshButtonX, refreshButtonY);
		Resources.normalFont.drawString(refreshButtonX+90, refreshButtonY+40, "Refresh Search");
		
		//draws return to menu button
		largeButton.draw(refreshButtonX +largeButtonWidth+70, refreshButtonY);
		Resources.normalFont.drawString(refreshButtonX+largeButtonWidth+160, refreshButtonY+40, "Return to menu");

		//draws buttons for any servers that have been found
		//and the servers info on those buttons
		for(int i = 0;i<availibleServers.size();i++){
			int xOffset = i % 5;
			int yOffset = i/5;
			smallButton.draw(firstServerButtonX+xOffset*(smallButtonWidth+20), firstServerButtonY+yOffset*(buttonHeight+20));
			Resources.smallFont.drawString(firstServerButtonX+xOffset*(smallButtonWidth+20)+105-(Resources.smallFont.getWidth("Name: "+availibleServers.get(i).getServerName())/2), firstServerButtonY+yOffset*(buttonHeight+20)+25, "Name: "+availibleServers.get(i).getServerName());
			Resources.smallFont.drawString(firstServerButtonX+xOffset*(smallButtonWidth+20)+13, firstServerButtonY+yOffset*(buttonHeight+20)+25+Resources.smallFont.getLineHeight(), "IP: "+availibleServers.get(i).getIPAddress());
			Resources.smallFont.drawString(firstServerButtonX+xOffset*(smallButtonWidth+20)+22, firstServerButtonY+yOffset*(buttonHeight+20)+25+2*Resources.smallFont.getLineHeight(), "Server size: "+availibleServers.get(i).getNumberOfPlayers());		
		}

	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		//if mouse is pressed, check to see if they clicked on a button
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);

	}

	private void handleButtons(GameContainer gc, StateBasedGame s) throws SlickException{
		//check to see if a server button was pressed
		for(int i = 0;i<availibleServers.size();i++){
			int xOffset = i % 5;
			int yOffset = i/5;
			if(gc.getInput().getMouseX() > firstServerButtonX+xOffset*(smallButtonWidth+20) && gc.getInput().getMouseY() > firstServerButtonY+yOffset*(buttonHeight+20) 
					&& gc.getInput().getMouseX() < firstServerButtonX+xOffset*(smallButtonWidth+20) + smallButtonWidth
					&& gc.getInput().getMouseY() < firstServerButtonY+yOffset*(buttonHeight+20) + buttonHeight){
				//if they did click on one, make a client
				s.addState(new ClientState());
				//cause the client to connect to the server
				((ClientState)s.getState(States.CLIENT)).init(gc,s, availibleServers.get(i).getIPAddress());
				//stop the thread searching for available servers
				//as is no longer required
				searcher.stop();
				//enter the client state
				s.enterState(States.CLIENT);
			}
		}

		//check if the refresh search button was pressed
		if(gc.getInput().getMouseX() > refreshButtonX 
				&& gc.getInput().getMouseY() > refreshButtonY
				&& gc.getInput().getMouseX() < refreshButtonX + largeButtonWidth
				&& gc.getInput().getMouseY() < refreshButtonY + buttonHeight ){
			//stop current search
			searcher.stop();
			//make new one
			searcher = new Thread(new searchForGames());
			//start new one
			searcher.start();
		}
		//check if return to menu button was pressed
		if(gc.getInput().getMouseX() > refreshButtonX+ largeButtonWidth+70 
				&& gc.getInput().getMouseY() > refreshButtonY 
				&& gc.getInput().getMouseX() < refreshButtonX +(2*largeButtonWidth)+70
				&& gc.getInput().getMouseY() < refreshButtonY + buttonHeight ){
			//stop seach as not needed
			searcher.stop();
			//switch back to the menu
			s.enterState(States.MENU);
		}
	}



	@Override
	public int getID() {
		return States.GAMECOORDINATOR;
	}

	public class searchForGames implements Runnable{
		DatagramSocket datagramSocket;
		byte[] message = "SMBServerAvailible?".getBytes();
		DatagramPacket outboundPacket;
		byte[] receiveBuffer = new byte[15000];
		boolean searching = true;
		@Override
		public void run() {

			try {
				//clears the current list of availible servers
				availibleServers.clear();
				datagramSocket = new DatagramSocket();
				//makes the socket close after 10 seconds of nothing being received
				datagramSocket.setSoTimeout(10000);
				try{
					//makes and broadcasts a packet to all devices on network
					//looking for a server
					outboundPacket = new DatagramPacket(message, message.length, InetAddress.getByName("255.255.255.255"), 10306);
					datagramSocket.send(outboundPacket);
				}catch(Exception e){
					e.printStackTrace();
				}
				//use a loop to makes sure the message is sent to all the 
				//broadcast addresses
				while(Resources.networkInterfaces.hasMoreElements()){
					NetworkInterface networkInterface = (NetworkInterface) Resources.networkInterfaces.nextElement();

					if(networkInterface.isLoopback()||!networkInterface.isUp()) continue;

					for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()){
						InetAddress broadcastAddress = interfaceAddress.getBroadcast();
						if(broadcastAddress == null) continue;
						try{
							outboundPacket.setAddress(broadcastAddress);
							datagramSocket.send(outboundPacket);
						} catch(Exception e){
							e.printStackTrace();
						}
					}
				}


				DatagramPacket inboundPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

				while(searching){
						try {
						//receives confirmation message here
						datagramSocket.receive(inboundPacket);
						String message = new String(inboundPacket.getData()).trim();
						//checks that the message was for them
						if(message.equals("ImHere")){
							
							//as it was for them, then receives 
							//details message here
							datagramSocket.receive(inboundPacket);

							String serverDetails = new String(inboundPacket.getData(), inboundPacket.getOffset(),
									inboundPacket.getLength()).trim();
							//splits details message into two parts
							//the first being the name of the server
							//the second being the amount of desired players of the server
							String[] indivDetails = serverDetails.split("/");
							
							//makes AvailibleServer object from the received information
							AvailibleServer newServer = new AvailibleServer(inboundPacket.getAddress().getHostAddress(),
									Integer.valueOf(indivDetails[indivDetails.length-1]), serverDetails.substring(0, serverDetails.length()-2));
							//adds the new object to the list of available servers
							availibleServers.add(newServer);
						}
					} catch (IOException e) {
						searching = false;
					}
				}
				datagramSocket.close();
			}catch(SocketException e){
				e.printStackTrace();
			}
		}
		public void closeSocket(){
			datagramSocket.close();
		}
	}

}
