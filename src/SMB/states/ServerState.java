package SMB.states;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
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
import SMB.world.Tile;
import SMB.world.World;


public class ServerState extends BasicGameState {
	//declare variables and initialise where appropriate
	public ArrayList<Entity> entities, toRemove;
	public ArrayList<EntityInput> inputs;
	public ArrayList<ObjectOutputStream> outputStreams;
	public ArrayList<Socket> sockets;
	public ArrayList<Thread> threads;
	
	public EntityInput p1Input, p2Input, p3Input, p4Input;
	
	private int xRender = 1366;
	private int yRender = 1791;
	private int desiredPlayers = 2;
	public String nameOfServer;
	private Thread serverInitialiser;

	public boolean preGame = true, gameOver, exitGame = false;;

	public String winner = null;
	
	public int swordSpawnTimer = 0;
	
	public ServerState(int playerNum, String serverName){
		desiredPlayers = playerNum;
		if(serverName!=null){
			nameOfServer = serverName;
		}
	}
	
	
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		//initialises more variables
		entities = new ArrayList<Entity>();
		inputs = new ArrayList<EntityInput>();
		p1Input = new EntityInput();
		p2Input = new EntityInput();
		entities.add(new Player(1));
		inputs.add(p1Input);
		entities.add(new Player(2));
		inputs.add(p2Input);
		//only initialise and add players 3 and 4 
		//if there are 4 desired players
		if(desiredPlayers==4){
			p3Input = new EntityInput();
			p4Input = new EntityInput();
			entities.add(new Player(3));
			inputs.add(p3Input);
			entities.add(new Player(4));
			inputs.add(p4Input);
		}
		outputStreams = new ArrayList<ObjectOutputStream>();

		toRemove = new ArrayList<Entity>();
		
		sockets = new ArrayList<Socket>();
		threads = new ArrayList<Thread>();
		
		//start server initialiser thread, which establishes
		//a connection with the players
		serverInitialiser = new Thread(new ServerInit());
		serverInitialiser.start();
	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {


		//moves everything so the map is on the screen and central
		g.translate(-xRender, -yRender);
		//renders the map
		World.render(xRender, yRender);

		if(preGame){
			//if it is before the game has started show the appropriate text
			Resources.bigFont.drawString(2050, 2000, "Waiting for players", Color.black);
			Resources.bigFont.drawString(1800, 2000+Resources.bigFont.getLineHeight(), "Your local IP address is "+ Resources.getLocalIP(), Color.black);
			Resources.bigFont.drawString(1750, 2000+(Resources.bigFont.getLineHeight()*2), "Your public IP address is "+ Resources.getPublicIP(), Color.black);
			Resources.bigFont.drawString(2300-(Resources.bigFont.getWidth("Server name is: "+ nameOfServer))/2, 2000+(Resources.bigFont.getLineHeight()*3), "Server name is: "+ nameOfServer, Color.black);
		}else{
			for (int i = 0; i <entities.size();i++){
				//renders each of the entities
				entities.get(i).render(gc, g);
			}
			if(winner!=null){
				//display winner text if there is a winner
				Resources.bigFont.drawString(2000, 2000, winner+" is the winner", Color.black);
				Resources.bigFont.drawString(1950, 2000+Resources.bigFont.getLineHeight(), "Press enter to play again", Color.black);
				Resources.bigFont.drawString(1800, 2000+(Resources.bigFont.getLineHeight()*2), "or press escape to return to the menu", Color.black);
			}
		}
		g.resetTransform();
	}


	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {

		if(preGame){
			//nothing needs to be updated, as is pregame
		} else if(!gameOver){
			//gets the input for player 1, as this is the player played by the user running
			//the server
			getPlayer1Input(gc);
			for (int i = 0; i <entities.size();i++){
				//if the entity is a player, update it using its corresponding input
				if(entities.get(i).label.contains("Player")){
					//get the players number
					int playerNum = Integer.parseInt(entities.get(i).label.replaceAll("[\\D]", ""));
					//make it zero indexed
					playerNum--;
					//use this number to ge the correct input from inputs
					entities.get(i).update(gc, delta, inputs.get(playerNum));
				}else{
					//otherwise update it normally
					entities.get(i).update(gc, delta,null);
				}
				//if the entity is a training dummy or a sword, skip the rest
				//as it is not relevant to them
				if(entities.get(i).label.equals("Training")) continue;
				if(entities.get(i).label.equals("Sword")) continue;
				
				//if the player (can only be a player as all other types of entities cannot
				//get to this point) has gone outside the boundaries of the map
				if(entities.get(i).x > 80*Tile.SIZE||entities.get(i).x < 18*Tile.SIZE|| entities.get(i).y > 80*Tile.SIZE||entities.get(i).y < 28*Tile.SIZE){
					//cause the player to respawn
					entities.get(i).respawn();
					//if the player has run out of lives
					if(entities.get(i).lives<0){
						//add the player to the arrayList of entities to be removed
						toRemove.add(entities.get(i));
					}
				}
				//run the combat method for that player
				combat(entities.get(i));
			}
			//if there is anything in toRemove
			if(toRemove.size()>0){
				//remove them from entities
				entities.removeAll(toRemove);
				toRemove.clear();
			}
			//increment the sword timer if it is not in play
			if(!isSwordInPlay()){
				swordSpawnTimer++;
			}
			//if the sword timer reaches 2000, reset the timer and spawn in a sword
			if(swordSpawnTimer >= 2000){
				swordSpawnTimer = 0;
				entities.add(new Sword());
			}
			
			updateClients();

			checkForWinner();
		}else{
			//this is what is checked for when someone has won the game
			//the functions of the buttons match what the text displayed
			//says they do
			if(gc.getInput().isKeyDown(Input.KEY_ENTER))startGame();
			if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)){
				exitGame = true;
			}
			
		}
		//if exitGame is true
		if(exitGame){
			//sends the string "leaveGame" to all the clients
			tellClients("leaveGame");
			for(int i = 0; i< sockets.size(); i ++){
				//stop all the threads which are recieving and processing
				//any messages sent to the server by the client
				threads.get(i).stop();
				try {
					//close the corresponding socket for that thread
					sockets.get(i).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}	
			//cause this program to return to the menu
			s.enterState(States.MENU);
		}
	}

	public void combat(Entity player){
		//runs comparison between the entity in question and all the ones 
		//in the game
		for(Entity opponent : entities){
			//stops the player hitting themselves
			if(player==opponent) continue;
			//stops the player interacting with invulnerable players
			if(opponent.invulnerable)continue;

			//i will only comment one of these as they are largely repetitions,
			//with a few exceptions which i will comment
			
			//if the players image is that which means he is performing a specific attack
			//in this case a light neutral attack on the ground
			//and the hitbox of the opponent intersects the hitbox of that attack
			if(player.image == Resources.getImage("p1LightGroundNeutral")&& ((Player) player).getLGNHitBox().intersects(opponent)){
				//cause the opponent to be hit, with the horizontal direction depending on
				//what direction the player is facing, as if the player is facing left
				//when they punch the opponent it will cause the opponent to be hit towards
				//the left. In this case there is no vertical component (hence '0') and the
				//attack does 5 damage
				opponent.getHit((player.facingRight) ? 2f : -2f, 0, 5);
				//cause the opponent to become temporarily invulnerable
				//this is to prevent the opponent being hit multiple times by the same
				//attack, notice how the duration of the attack depends on the duration
				//of the attack they are being hit by
				opponent.invulnerableTimer = ((Player) (player)).LATime;
				//skip checking all other attacks, as only 1 can occur at a time
				continue;
			}
			if(player.image == Resources.getImage("p1LightGroundDown")&&((Player) player).getLGDHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 1f : -1f, -2f, 2);
				opponent.invulnerableTimer = ((Player) (player)).LATime;
				continue;
			}
			if(player.image == Resources.getImage("p1LightGroundRight")&&((Player) player).getLGRHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 2.5f : -2.5f, 0, 3);
				opponent.invulnerableTimer = ((Player) (player)).LATime;
				continue;
			}
			if(player.image == Resources.getImage("p1LightAirNeutral")&&((Player) player).getLANHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 0.5f : -0.5f, -3, 5);
				opponent.invulnerableTimer = ((Player) (player)).LATime;
				continue;
			}
			if(player.image == Resources.getImage("p1LightAirRight")&&((Player) player).getLARHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 1.5f : -1.5f, 1.5f, 3);
				opponent.invulnerableTimer = ((Player) (player)).LATime;
				continue;
			}


			if(player.image == Resources.getImage("p1HeavyGroundNeutral2")&&((Player) player).getHGNHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 1.6f : -1.6f, 0, 30);
				opponent.invulnerableTimer = ((Player) (player)).HATime;
				continue;
			}
			if(player.image == Resources.getImage("p1HeavyGroundDown2")&&((Player) player).getHGDHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 1.2f : -1.2f, -2.5f, 39);
				opponent.invulnerableTimer = ((Player) (player)).HATime;
				continue;
			}
			if(player.image == Resources.getImage("p1HeavyGroundRight2")&&((Player) player).getHGRHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 2f : -2f, 0, 33);
				opponent.invulnerableTimer = ((Player) (player)).HATime;
				continue;
			}
			if(player.image == Resources.getImage("p1HeavyAirDown")&&((Player) player).getHADHitBox().intersects(opponent)){
				opponent.getHit(0, 3.5f, 24);
				opponent.invulnerableTimer = ((Player) (player)).HATime + 100;
				continue;
			}

			if(player.image == Resources.getImage("p1HeavyAirUp2")&&((Player) player).getHAUHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 1f : -1f, -3.5f, 33);
				opponent.invulnerableTimer = ((Player) (player)).HATime;
				continue;
			}
			if(player.image == Resources.getImage("p1HeavyAirNeutral2")){
				//this attack involves two hitboxes, so each must be checked 
				//individually. Other than that is the same
				if(((Player) player).getHAN1HitBox().intersects(opponent)){
					opponent.getHit( 2f, 0, 33);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
				}else if(((Player) player).getHAN2HitBox().intersects(opponent)){
					opponent.getHit( -2f, 0, 33);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
				}
				continue;
			}


			if((player.image == Resources.getImage("p1GrabGround")||player.image == Resources.getImage("p1GrabAir"))&&   
					((Player) player).getGrabHitBox().intersects(opponent)&&
					!player.grabbing){
				if(opponent.label.equals("Sword")){
					//causes player to pick up sword if they grab a sword
					((Player) player).pickUpSword();
					//removes the sword from the map as a seperate entity, as 
					//it has been picked up
					toRemove.add(opponent);
					continue;
				}
				player.grabbing = true;
				opponent.grabbed = true;
				//causes the opponent to move into such a position that 
				//the opponent appears to be being held up or grabbed by
				//the player
				if(player.facingRight){
					opponent.x = player.x + 14* Tile.SCALE / 1.5f;
				}else{
					opponent.x = player.x - 14* Tile.SCALE / 1.5f;
				}
				opponent.y = player.y - 20* Tile.SCALE / 1.5f;
			}
			//if the opponent is already grabbed
			if(opponent.grabbed){
				
				if(player.isOnSolid()||player.isOnPSolid()){
					opponent.grabberOnSolid = true;
				}else{
					opponent.grabberOnSolid = false;
				}		
				
				//throws the opponent in the correct direction
				//which can be found by which image the player is
				//(if none match they are not attempting to throw)
				//the opponent
				if(player.image == Resources.getImage("p1ThrowGroundUp")||player.image == Resources.getImage("p1ThrowAirUp")){
					player.grabbing = false;
					opponent.grabbed = false;
					opponent.getHit((player.facingRight) ? 2f : -2f, -10f, 1);
					if(player.isOnSolid()||player.isOnPSolid()) player.image = Resources.getImage("p1Idle");
					else player.image = Resources.getImage("p1IdleAir");				
					opponent.invulnerableTimer = ((Player) (player)).GTime;
					continue;

				}
				if(player.image == Resources.getImage("p1ThrowGroundRight")||player.image == Resources.getImage("p1ThrowAirRight")){
					player.grabbing = false;
					opponent.grabbed = false;
					opponent.getHit((player.facingRight) ? 7f : -7f, -0.1f, 1);
					if(player.isOnSolid()||player.isOnPSolid()) player.image = Resources.getImage("p1Idle");
					else player.image = player.image = Resources.getImage("p1IdleAir");
					opponent.invulnerableTimer = ((Player) (player)).GTime;
					continue;
				}
				if(player.image == Resources.getImage("p1ThrowGroundDown")||player.image == Resources.getImage("p1ThrowAirDown")){
					player.grabbing = false;
					opponent.grabbed = false;
					opponent.getHit((player.facingRight) ? 1f : -1f, 6f, 1);
					if(player.isOnSolid()||player.isOnPSolid()) player.image = Resources.getImage("p1Idle");
					else player.image = player.image = Resources.getImage("p1IdleAir");
					opponent.invulnerableTimer = ((Player) (player)).GTime;
				}
			}
			if((player.image == Resources.getImage("p1LightGroundSword")||player.image == Resources.getImage("p1LightAirSword"))&& ((Player) player).getLSwordHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 4f : -4f, 0, 10);
				opponent.invulnerableTimer = ((Player) (player)).LATime;
				continue;
			}
			if(player.image == Resources.getImage("p1HeavyGroundSword2")&& ((Player) player).getHGSwordHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 7f : -7f, 0, 60);
				opponent.invulnerableTimer = ((Player) (player)).HATime;
				continue;
			}
			if(player.image == Resources.getImage("p1HeavyAirSword2")&& ((Player) player).getHASwordHitBox().intersects(opponent)){
				opponent.getHit((player.facingRight) ? 3f : -3f, -6f, 60);
				opponent.invulnerableTimer = ((Player) (player)).HATime;
				continue;
			}
			if(player.image == Resources.getImage("p1HeavyAirSwordDown")&& ((Player) player).getHADownSwordHitBox().intersects(opponent)){
				opponent.getHit(0, 8f, 60);
				opponent.invulnerableTimer = ((Player) (player)).HATime;
				continue;
			}

		}
		//this loop checks if another player is grabbed
		//and stops the current player grabbing if no one else 
		//is grabbed, to prevent players getting stuck grabbing no-one
		if(player.grabbing){
			boolean isSomeoneGrabbed = false;
			for(Entity opponent : entities){
				if(player == opponent) continue;
				if(opponent.grabbed) isSomeoneGrabbed = true;
			}
			if(!isSomeoneGrabbed)player.grabbing=false;
		}
		


	}
	
	public void checkForWinner(){
		//if there's only one entity left, that entity must be the winner
		if(entities.size()==1){
			winner = entities.get(0).label;
			gameOver = true;
			//send the clients a messaged indicating that the game is over
			tellClients("gameOver");
			//send the clients the name of the player that has won
			tellClients(winner);
			//if there's 2 entities left, there's either 2 players or 1 player
			//and a sword
		}else if(entities.size()==2){
			int playersLeft = 0;
			String temp = null;
			//use looping through the labels of entities to count the number
			//of players left
			for (int i = 0; i <entities.size();i++){
				if(entities.get(i).label.contains("Player")){
					playersLeft++;
					temp = entities.get(i).label;
				}	
			}
			//if there's one player left, that player must be the winner
			if(playersLeft == 1){
				winner = temp;
				gameOver = true;
				//send the clients a messaged indicating that the game is over
				tellClients("gameOver");
				//send the clients the name of the player that has won
				tellClients(winner);
			}
		}
	}

	public void startGame(){
		//clear the current lists of entities and players
		entities.clear();
		inputs.clear();
		
		//add all the players and their inputs
		entities.add(new Player(1));
		inputs.add(p1Input);
		entities.add(new Player(2));
		inputs.add(p2Input);
		if(desiredPlayers==4){
			//add more players and inputs if appropriate
			entities.add(new Player(3));
			inputs.add(p3Input);
			entities.add(new Player(4));
			inputs.add(p4Input);
		}
		winner = null;
		gameOver = false;
		//notify all the clients that the game is starting
		tellClients("startGame");
	}
	
	public boolean isSwordInPlay(){
		boolean inPlay = false;
		for(int i = 0;i<entities.size();i++){
			if(entities.get(i).label.equals("Sword")){
				//checks if the entity is a sword
				inPlay = true;
				i = entities.size();
				//or checks if the entity is a sword wielding player
			}else if(entities.get(i).label.contains("Player")&&(((Player) entities.get(i)).hasSword)){
				inPlay = true;
				i = entities.size();
			}
		}
		return inPlay;
	}
	
	public void getPlayer1Input(GameContainer gc){
		//gets the 1st players input from their respective keys
		inputs.get(0).setUpKeyDown(gc.getInput().isKeyDown(Input.KEY_W));
		inputs.get(0).setLeftKeyDown(gc.getInput().isKeyDown(Input.KEY_A));
		inputs.get(0).setRightKeyDown(gc.getInput().isKeyDown(Input.KEY_D));
		inputs.get(0).setDownKeyDown(gc.getInput().isKeyDown(Input.KEY_S));
		inputs.get(0).setLAKeyDown(gc.getInput().isKeyDown(Input.KEY_J)||gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON));
		inputs.get(0).setHAKeyDown(gc.getInput().isKeyDown(Input.KEY_K)||gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON));
		inputs.get(0).setGrKeyDown(gc.getInput().isKeyDown(Input.KEY_L)||gc.getInput().isKeyDown(Input.KEY_LSHIFT));
	}
	public void updateClients(){
		//for each outputSteam, where each goes to a different client
		for(int i = 0; i < outputStreams.size();i++){
			try{
				outputStreams.get(i).flush();
				//notify the clients that a new set of entities is being set
				outputStreams.get(i).writeObject("newEntities");
				//tell the clients how many entities are going to be sent
				outputStreams.get(i).writeInt(entities.size());
				for(int n = 0; n < entities.size(); n++){
					//tell the client what type of entity they are about
					//to receive
					if(entities.get(n).label.equals("Sword")){
						outputStreams.get(i).writeObject("Sword");
					}else{
						outputStreams.get(i).writeObject("Player");
					}
					//send the entity itself to the object
					outputStreams.get(i).writeObject(entities.get(n));
					
					//create a CleintEntityInfo object and fill it this the entity's info
					ClientEntityInfo temp = new ClientEntityInfo();
					
					temp.setX(entities.get(n).x);
					temp.setY(entities.get(n).y);
					temp.setxOffset(entities.get(n).xImageOffset);
					temp.setImageResourceLocation(entities.get(n).imageResourceLocation);
					temp.setFacingRight(entities.get(n).facingRight);
					temp.setAmountDamaged(entities.get(n).AmountDamaged);
					temp.setLives(entities.get(n).lives);
					temp.setrColor(entities.get(n).color.getRed());
					temp.setgColor(entities.get(n).color.getGreen());
					temp.setbColor(entities.get(n).color.getBlue());
					temp.setaColor(entities.get(n).color.getAlpha());
					
					//send the CleintEntityInfo object to the client
					outputStreams.get(i).writeObject(temp);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public void tellClients(String message){
		//for each outputSteam, where each goes to a different client
		for(int i = 0; i < outputStreams.size();i++){
			try{
				outputStreams.get(i).flush();
				//send the message to that client
				outputStreams.get(i).writeObject(message);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public int getID() {
		return States.SERVER;
	}
	//implements Runnable, meaning it can be run as part of a thread
	public class ClientHandler implements Runnable{
		//declare variables
		ObjectInputStream inputStream;
		Socket socket;
		int playerNum;
		public ClientHandler(Socket clientSocket, int ID){
			try{
				//Initialise variables
				playerNum = ID;
				socket = clientSocket;
				inputStream = new ObjectInputStream(socket.getInputStream());
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		public void run(){
			EntityInput newInput;
			String identifier;
			try{
				//loops continuously
				while(true){
					switch(identifier = (String) inputStream.readObject()){
						//if the string first received is "newInput"
						case"newInput":
							//set the relevant input to the input received
							newInput = (EntityInput) inputStream.readObject();
							inputs.set(playerNum,newInput);//replace this with another method 
							//that searches for the player containing playerNum in its label
							//and sets the relevant input to that
						break;	
						//if the string first received is "newInput"
						case"clientLeft":
							exitGame = true;
						break;	
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	//implements Runnable, meaning it can be run as part of a thread
	public class ServerInit implements Runnable{
		ObjectInputStream inputStream;
		Socket socket;
		int playerNum;
		public ServerInit(){
		}
		public void run(){
			//make a thread that runs the server announcer class
			Thread serverAnnouncer = new Thread(new ServerAnnouncer());
			serverAnnouncer.start();
			try{
				//make a new server socket
				ServerSocket serverSocket = new ServerSocket(10305);
			
				for(int i = 1; i <desiredPlayers;i++){
					
					//make a socket for the connection recieved by the server socket
					Socket clientSocket = serverSocket.accept();
					//add it to sockets
					sockets.add(clientSocket);
					
					//make an ObjectOutputStream going to that socket
					ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					outputStream.flush();
					//add it to outputStreams
					outputStreams.add(outputStream);

					//make a thread which handles the input received by the client
					//that has just connected
					Thread clientHandler = new Thread(new ClientHandler(clientSocket, i));
					//add it to the list of active threads
					threads.add(clientHandler);
					//start the client handler
					clientHandler.start();
				}
				//as all the clients that the server wants have connected
				//close the server socket and stop the serverAnnouncer
				serverSocket.close();
				serverAnnouncer.stop();
			}catch(Exception ex){ex.printStackTrace();}
			
			startGame();
			//pregame is false, as the game is now starting
			preGame = false;


		}

	}
	
	public class ServerAnnouncer implements Runnable{
		//declare variables
		DatagramSocket datagramSocket;
		byte[] messageForClients, serverDetails, receiveBuffer;
		DatagramPacket inboundPacket; 
		@Override
		public void run() {
			try{
				//initialise variables
				datagramSocket = new DatagramSocket(10306, InetAddress.getByName("0.0.0.0"));
				datagramSocket.setBroadcast(true);
				
				messageForClients = "ImHere".getBytes();
				serverDetails = (nameOfServer+"/"+desiredPlayers).getBytes();
				receiveBuffer = new byte[15000];
				
				inboundPacket = new DatagramPacket(receiveBuffer , receiveBuffer.length);
				
				while(true){
					//when the datagramSocket receives a packet, make inboundPacket equal
					//to that packet
					datagramSocket.receive(inboundPacket);
					
					//make a string from the contents of the received packet
					String contents = new String(inboundPacket.getData()).trim();
					
					//if that string says "SMBServerAvailible?" then it was sent from one
					//of my programs, and is intended for any available servers
					if(contents.equals("SMBServerAvailible?")){
						
						//make and send a new DatagramPacket, containing a message saying that this is a SMB server
						DatagramPacket outboundPacket = new DatagramPacket(messageForClients, messageForClients.length, inboundPacket.getAddress(), inboundPacket.getPort());
						datagramSocket.send(outboundPacket);
						
						//make and send a second DatagramPacket containing the information about this server
						outboundPacket = new DatagramPacket(serverDetails, serverDetails.length, inboundPacket.getAddress(), inboundPacket.getPort());
						datagramSocket.send(outboundPacket);
					}
				}
				
			}catch (IOException e){
				e.printStackTrace();
			}
			
		}
		
	}

}
