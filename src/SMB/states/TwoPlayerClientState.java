package SMB.states;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import SMB.entities.Entity;
import SMB.entities.Player;
import SMB.main.Resources;
import SMB.world.World;

public class TwoPlayerClientState extends BasicGameState {
	
	public ArrayList<Entity> entities, toRemove;
	public Input p1Input, p2Input;
	private int xRender = 1366;
	private int yRender = 1791;
	
	public boolean gameOver;
	
	public String winner = null;
	
	public Socket socket;
	public ObjectInputStream inputStream;
	public ObjectOutputStream writeToServer; 
	
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
			entities = new ArrayList<Entity>();
			initialiseConnection();
		
			toRemove = new ArrayList<Entity>();
			
		
		
	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		
		
		
		g.translate(-xRender, -yRender);
		World.render(xRender, yRender);
		
		
		for (int i = 0; i <entities.size();i++){
			entities.get(i).clientRender(gc, g);
		}
		if(winner!=null){
			Resources.bigFont.drawString(2000, 2000, winner+" is the winner", Color.black);
			Resources.bigFont.drawString(1950, 2000+Resources.bigFont.getLineHeight(), "Press enter to play again", Color.black);
			Resources.bigFont.drawString(1800, 2000+(Resources.bigFont.getLineHeight()*2), "or press escape to return to the menu", Color.black);
		}
		g.resetTransform();
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		System.out.println("update");
		while(!gameOver){
			sendInputToServer(gc);
		}
		
	}
	
	
	
	
	public void startGame(){
		entities.clear();
		entities.add(new Player(1));
		entities.add(new Player(2));
		//entities.add(new TrainingDummy());
		//entities.add(new Sword());
		winner = null;
		gameOver = false;
	}
	public void initialiseConnection(){
		try {
			System.out.println("Initialising connection");
			socket = new Socket("127.0.0.1 " , 10305);
			System.out.println("Made a socket");
			inputStream = new ObjectInputStream(socket.getInputStream());
			System.out.println("Made input stream");
			writeToServer = new ObjectOutputStream(socket.getOutputStream());
			writeToServer.flush();
			System.out.println("Made output stream");
			System.out.println ("Connection made") ;
			Thread serverHandler = new Thread(new ServerHandler());
			serverHandler.start();
			startGame();
		} catch (IOException ex){
			ex.printStackTrace () ; 
		}
	}
	
	public class ServerHandler implements Runnable{
		
		public ServerHandler(){
			
		}
		@SuppressWarnings("unchecked")
		public void run(){
			String message;
			try{
				while(true){
					message = (String) inputStream.readObject(); 
					switch(message){
					
						case "newEntities":
							
							entities = (ArrayList<Entity>) inputStream.readObject();
							
						break;
						
						case "startGame":
							System.out.println("game started(?)");
							startGame();

						break;
							
						case "gameOver":
							gameOver = true;
						break;	
					}
				}
			}catch(EOFException ex){
				
				ex.printStackTrace();

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void sendInputToServer(GameContainer gc){
		try {
			writeToServer.writeObject(gc.getInput());
			writeToServer.flush();
			System.out.println("Input sent");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getID() {
		return States.LOCALGAME;
	}

}
