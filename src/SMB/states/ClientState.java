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
import SMB.entities.Sword;
import SMB.main.Resources;
import SMB.tools.ClientEntityInfo;
import SMB.tools.EntityInput;
import SMB.world.World;

public class ClientState extends BasicGameState {
	//initialises variables
	public ArrayList<Entity> entities, toRemove;
	public Input p1Input, p2Input;
	private int xRender = 1366;
	private int yRender = 1791;

	public boolean gameOver, leaveGame = false, pregame = true;

	public String winner = null;

	public Socket socket;
	public ObjectInputStream inputStream;
	public ObjectOutputStream writeToServer;
	public Thread serverHandler;
	
	
	//2 optional init methods, depending on whether an IPAddress has been input
	public void init(GameContainer gc, StateBasedGame s, String IPAddress) throws SlickException {

		entities = new ArrayList<Entity>();
		initialiseConnection(IPAddress);
		toRemove = new ArrayList<Entity>();
	}
	@Override
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		entities = new ArrayList<Entity>();
		initialiseConnection();

		toRemove = new ArrayList<Entity>();

	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {

		//renders map
		g.translate(-xRender, -yRender);
		World.render(xRender, yRender);
		
		if(pregame){
			//displays this text if game hasnt started yet
			Resources.bigFont.drawString(1900, 2000, "Waiting for players to connect", Color.black);
		}
		//renders all entities
		for (int i = 0; i <entities.size();i++){
			entities.get(i).clientRender(gc, g);
		}
		if(winner!=null){
			//displays end of game text when there's a winner
			Resources.bigFont.drawString(2000, 2000, winner+" is the winner", Color.black);
			Resources.bigFont.drawString(1950, 2000+Resources.bigFont.getLineHeight(), "Wait for the server to restart the game", Color.black);
			Resources.bigFont.drawString(1800, 2000+(Resources.bigFont.getLineHeight()*2), "or press escape to return to the menu", Color.black);
		}
		g.resetTransform();
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		//no updating of entities as thats all server side
		if(!gameOver){      
			sendInputToServer(gc);
		}else if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			//client exits game, by telling the server it wants to leave
			//then the server shuts down for everyone
			try{
				writeToServer.writeObject("clientLeft");    
			}catch(Exception e){
				e.printStackTrace();
			}

		}
		if(leaveGame){
			//actually causes the player to leave the game
			s.enterState(States.MENU);
		}

	}




	public void startGame(){
		entities.clear();
		entities.add(new Player(1));
		entities.add(new Player(2));
		winner = null;
		gameOver = false;
		pregame = false;
	}

	public EntityInput getClientInput(GameContainer gc){
		EntityInput newInput = new EntityInput();

		newInput.setUpKeyDown(gc.getInput().isKeyDown(Input.KEY_W));
		newInput.setLeftKeyDown(gc.getInput().isKeyDown(Input.KEY_A));
		newInput.setRightKeyDown(gc.getInput().isKeyDown(Input.KEY_D));
		newInput.setDownKeyDown(gc.getInput().isKeyDown(Input.KEY_S));
		newInput.setLAKeyDown(gc.getInput().isKeyDown(Input.KEY_X));
		newInput.setHAKeyDown(gc.getInput().isKeyDown(Input.KEY_C));
		newInput.setGrKeyDown(gc.getInput().isKeyDown(Input.KEY_V));

		return newInput;
	}
	public void initialiseConnection(String IP){
		try {
			socket = new Socket(IP , 10305);
			inputStream = new ObjectInputStream(socket.getInputStream());
			writeToServer = new ObjectOutputStream(socket.getOutputStream());
			writeToServer.flush();
			serverHandler = new Thread(new ServerHandler());
			serverHandler.start();	
		} catch (IOException ex){
			ex.printStackTrace () ; 
		}
	}
	public void initialiseConnection(){
		try {
			socket = new Socket("127.0.0.1" , 10305);
			inputStream = new ObjectInputStream(socket.getInputStream());
			writeToServer = new ObjectOutputStream(socket.getOutputStream());
			writeToServer.flush();
			serverHandler = new Thread(new ServerHandler());
			serverHandler.start();
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

						ArrayList<Entity> temp = new ArrayList<Entity>();
						int size = inputStream.readInt();
						for(int i = 0; i <size;i++){
							switch((String) inputStream.readObject()){

							case "Player":
								temp.add((Player)inputStream.readObject());

								break;

							case "Sword":
								temp.add((Sword)inputStream.readObject());
								break;	
							}
							ClientEntityInfo info = (ClientEntityInfo) inputStream.readObject();
							temp.get(i).x = info.getX();
							temp.get(i).y = info.getY();
							temp.get(i).xImageOffset = info.getxOffset();

							temp.get(i).imageResourceLocation = info.getImageResourceLocation();
							temp.get(i).facingRight = info.isFacingRight();
							temp.get(i).AmountDamaged = info.getAmountDamaged();
							temp.get(i).lives = info.getLives();


							temp.get(i).color = new Color(info.getrColor(), info.getgColor(), info.getbColor(), info.getaColor());


						}

						entities = new ArrayList<Entity>(temp);
						break;

					case "startGame":

						startGame();

						break;

					case "gameOver":
						gameOver = true;
						winner = (String) inputStream.readObject();
						break;	
					case "leaveGame":
						leaveGame();
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
			writeToServer.writeObject("newInput");
			writeToServer.writeObject(getClientInput(gc));
			writeToServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void leaveGame() {


		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		leaveGame = true;
		serverHandler.stop();


	}

	public int getID() {
		return States.CLIENT;
	}



}
