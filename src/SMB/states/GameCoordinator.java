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

	public Image background, smallButton, largeButton;
	public ArrayList<AvailibleServer> availibleServers = new ArrayList<AvailibleServer>();
	public Thread searcher;
	public int firstServerButtonX, firstServerButtonY, 
	smallButtonWidth, largeButtonWidth, buttonHeight,
	refreshButtonX, refreshButtonY;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		background  = Resources.getImage("gameCoordinatorBackground");
		smallButton = Resources.getImage("smallButton");
		largeButton  = Resources.getImage("largeButton");
		
		firstServerButtonX = 400;
		firstServerButtonY = 400;
		smallButtonWidth = 210;
		largeButtonWidth = 440;
		buttonHeight = 120;
		
		refreshButtonX = 560;
		refreshButtonY = 775;
		
		searcher = new Thread(new searchForGames());
		searcher.start();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		background.draw(0,0);
		
		largeButton.draw(refreshButtonX, refreshButtonY);
		Resources.normalFont.drawString(refreshButtonX+90, refreshButtonY+40, "Refresh Search");
		
		largeButton.draw(refreshButtonX +largeButtonWidth+70, refreshButtonY);
		Resources.normalFont.drawString(refreshButtonX+largeButtonWidth+160, refreshButtonY+40, "Return to menu");
	
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
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);

	}
	
	private void handleButtons(GameContainer gc, StateBasedGame s) throws SlickException{
		
		for(int i = 0;i<availibleServers.size();i++){
			int xOffset = i % 5;
			int yOffset = i/5;
			if(gc.getInput().getMouseX() > firstServerButtonX+xOffset*(smallButtonWidth+20) && gc.getInput().getMouseY() > firstServerButtonY+yOffset*(buttonHeight+20) 
					&& gc.getInput().getMouseX() < firstServerButtonX+xOffset*(smallButtonWidth+20) + smallButtonWidth
					&& gc.getInput().getMouseY() < firstServerButtonY+yOffset*(buttonHeight+20) + buttonHeight){
				s.addState(new TwoPlayerClientState());
				((TwoPlayerClientState)s.getState(States.CLIENTTWOPLAYER)).init(gc,s, availibleServers.get(i).getIPAddress());
				s.enterState(States.CLIENTTWOPLAYER);
			}
		}
		
		//refresh search
		if(gc.getInput().getMouseX() > refreshButtonX 
				&& gc.getInput().getMouseY() > refreshButtonY
				&& gc.getInput().getMouseX() < refreshButtonX + largeButtonWidth
				&& gc.getInput().getMouseY() < refreshButtonY + buttonHeight ){
			System.out.println("refresh search");
			searcher.stop();
			searcher = new Thread(new searchForGames());
			searcher.start();
		}
		//return to menu button
		if(gc.getInput().getMouseX() > refreshButtonX+ largeButtonWidth+70 
				&& gc.getInput().getMouseY() > refreshButtonY 
				&& gc.getInput().getMouseX() < refreshButtonX +(2*largeButtonWidth)+70
				&& gc.getInput().getMouseY() < refreshButtonY + buttonHeight ){
			System.out.println("return to menu");
			searcher.stop();
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
				availibleServers.clear();
				datagramSocket = new DatagramSocket();
				datagramSocket.setSoTimeout(10000);
				try{
					outboundPacket = new DatagramPacket(message, message.length, InetAddress.getByName("255.255.255.255"), 10306);
					datagramSocket.send(outboundPacket);
					System.out.println("Message broadcast to 255.255.255.255");
				}catch(Exception e){
					e.printStackTrace();
				}
				while(Resources.networkInterfaces.hasMoreElements()){
					NetworkInterface networkInterface = (NetworkInterface) Resources.networkInterfaces.nextElement();

					if(networkInterface.isLoopback()||!networkInterface.isUp()) continue;

					for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()){
						InetAddress broadcastAddress = interfaceAddress.getBroadcast();

						if(broadcastAddress == null) continue;
						
						try{
							outboundPacket.setAddress(broadcastAddress);
							datagramSocket.send(outboundPacket);
							System.out.println("Message broadcast to "+broadcastAddress.getHostAddress());
						} catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				
				
				DatagramPacket inboundPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
				
				while(searching){
					try {
						datagramSocket.receive(inboundPacket);
						System.out.println("Reply from "+ inboundPacket.getAddress().getHostAddress());
						
						String message = new String(inboundPacket.getData()).trim();
						
						System.out.println("Message: "+ message);
						
						datagramSocket.receive(inboundPacket);
						String serverDetails = new String(inboundPacket.getData()).trim();
						System.out.println("ServerDetails: "+  serverDetails);
						String[] indivDetails = serverDetails.split("/");
						AvailibleServer newServer = new AvailibleServer(inboundPacket.getAddress().getHostAddress(), Integer.valueOf(indivDetails[1]), indivDetails[0]);
						availibleServers.add(newServer);
						
					} catch (IOException e) {
						searching = false;
						System.out.println("Server reply timeout");
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
