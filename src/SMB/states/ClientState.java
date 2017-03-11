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
	//declare and initialise where appropriate
	public ArrayList<Entity> entities, toRemove;
	public Input p1Input, p2Input;
	private int xRender = 1366;
	private int yRender = 1791;
	
	public int playerNumber;
	public String playerColour;

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
			Resources.bigFont.drawString(2326-Resources.bigFont.getWidth("You are player number "+playerNumber)/2, 2000+Resources.bigFont.getLineHeight(), "You are player number "+playerNumber, Color.black);
			Resources.bigFont.drawString(2326-Resources.bigFont.getWidth("And your player colour is "+playerColour)/2, 2000+Resources.bigFont.getLineHeight()*2, "And your player colour is "+playerColour, Color.black);
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




	public void startGame(){//starts game
		entities.clear();
		entities.add(new Player(1));
		entities.add(new Player(2));
		winner = null;
		gameOver = false;
		pregame = false;
	}

	public EntityInput getClientInput(GameContainer gc){
		//creates an EntityInput object and fills it with all the clients current input

		EntityInput newInput = new EntityInput();

		newInput.setUpKeyDown(gc.getInput().isKeyDown(Input.KEY_W));
		newInput.setLeftKeyDown(gc.getInput().isKeyDown(Input.KEY_A));
		newInput.setRightKeyDown(gc.getInput().isKeyDown(Input.KEY_D));
		newInput.setDownKeyDown(gc.getInput().isKeyDown(Input.KEY_S));
		newInput.setLAKeyDown(gc.getInput().isKeyDown(Input.KEY_J)||gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON));
		newInput.setHAKeyDown(gc.getInput().isKeyDown(Input.KEY_K)||gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON));
		newInput.setGrKeyDown(gc.getInput().isKeyDown(Input.KEY_L)||gc.getInput().isKeyDown(Input.KEY_LSHIFT));

		return newInput;
	}
	public void initialiseConnection(String IP){
		try {
			//makes a socket going to the server
			socket = new Socket(IP , 10305);
			//makes input and output streams for that socket 
			//are object streams so can send anything
			inputStream = new ObjectInputStream(socket.getInputStream());
			writeToServer = new ObjectOutputStream(socket.getOutputStream());
			
			//starts thread to handle messages being sent from the server
			serverHandler = new Thread(new ServerHandler());
			serverHandler.start();	
		} catch (IOException ex){
			ex.printStackTrace () ; 
		}
	}
	public void initialiseConnection(){
		try {
			//does same as above, but connects to this machine (hence "127.0.0.1")
			socket = new Socket("127.0.0.1" , 10305);
			inputStream = new ObjectInputStream(socket.getInputStream());
			writeToServer = new ObjectOutputStream(socket.getOutputStream());
			writeToServer.flush();
			playerNumber = inputStream.readInt()+1;
			serverHandler = new Thread(new ServerHandler());
			serverHandler.start();
		} catch (IOException ex){
			ex.printStackTrace () ; 
		}
	}

	public class ServerHandler implements Runnable{

		public ServerHandler(){

		}
		public void run(){
			String message;

			//so loops continuously
			while(true){
				try{
					//gets message from server
					message = (String) inputStream.readObject(); 
					//does different things depending on message
					switch(message){

					case "newEntities":
						//this message means the server is about to send the client 
						//creates temporary Arraylist so as to lessen the impact on the client
						//e.g. entities disappearing as new ones are sent
						ArrayList<Entity> temp = new ArrayList<Entity>();
						//this indicates the number of entities the client will receive
						int size = inputStream.readInt();
						for(int i = 0; i <size;i++){
							//server then sends client another string saying what type of
							//entity will be sent so it can be cast correctly
							switch((String) inputStream.readObject()){
								case "Player":
									temp.add((Player)inputStream.readObject());
								break;
								case "Sword":
									temp.add((Sword)inputStream.readObject());
								break;	
							}
							//gets the info about the entity and then sets it to those values
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
						//sets the real list of entities to the temporary one
						entities = new ArrayList<Entity>(temp);
						break;

					case "startGame":
						//self explanatory
						startGame();
						break;

					case "gameOver":
						//this means someone has won the game
						gameOver = true;
						//receives string from server, which is the name of the winner
						winner = (String) inputStream.readObject();
						break;	
					case "leaveGame":
						//causes player to leave the game
						leaveGame();
						break;	
					case "playerNumber":
						//gets what number this player is
						playerNumber = (int)(inputStream.readObject())+1;
						switch(playerNumber){
							case(2): 
								playerColour = "blue";
							break;
							case(3): 
								playerColour = "green";
							break;
							case(4): 
								playerColour = "yellow";
							break;
						
						}
					}

				}catch(EOFException ex){

					ex.printStackTrace();

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}

	}
	public void sendInputToServer(GameContainer gc){
		try {
			//sends the player the string "newInput" to indicate what it is 
			//about to send
			writeToServer.writeObject("newInput");
			//sends the server the clients input
			writeToServer.writeObject(getClientInput(gc));
			writeToServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void leaveGame() {
		try {
			//closes the socket
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		leaveGame = true;
		//stops the server handler so nothing is left 
		serverHandler.stop();
	}

	public int getID() {
		return States.CLIENT;
	}



}
