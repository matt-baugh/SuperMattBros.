package SMB.states;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import SMB.world.Tile;
import SMB.world.World;


public class TwoPlayerServerState extends BasicGameState {
	
	public ArrayList<Entity> entities, toRemove;
	public ArrayList<Input> inputs;
	public ArrayList<ObjectOutputStream> outputStreams;
	public Input p1Input, p2Input;
	private int xRender = 1366;
	private int yRender = 1791;
	private int desiredPlayers = 2;
	private Thread serverInitialiser;
	private String IPAddress ;
	
	public boolean preGame = true, gameOver;
	
	public String winner = null;
	
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		System.out.println("Waiting for players");
		
			entities = new ArrayList<Entity>();
			inputs = new ArrayList<Input>();
			outputStreams = new ArrayList<ObjectOutputStream>();
			try{
				IPAddress = InetAddress.getLocalHost().getHostAddress();
			}catch(Exception e){
				e.printStackTrace();
			}
		
		
			toRemove = new ArrayList<Entity>();
			serverInitialiser = new Thread(new ServerInit());
			serverInitialiser.start();
			
		
		
	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		
	 
		
		g.translate(-xRender, -yRender);
		World.render(xRender, yRender);
		
		if(preGame){
			Resources.bigFont.drawString(2000, 2000, "Waiting for players", Color.black);
			Resources.bigFont.drawString(1800, 2000+Resources.bigFont.getLineHeight(), "Your local IP address is "+ IPAddress, Color.black);
		}else{
			for (int i = 0; i <entities.size();i++){
				entities.get(i).render(gc, g);
			}
			if(winner!=null){
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
			
		} else if(!gameOver){
			
			updateClients();
			
		if(gc.getInput().isKeyPressed(Input.KEY_Y))entities.add(new Sword());
		inputs.set(0, gc.getInput());
			for (int i = 0; i <entities.size();i++){
				if(i<inputs.size()){
					entities.get(i).update(gc, delta, inputs.get(i));
				}else{
					entities.get(i).update(gc, delta,null);
				}
				if(entities.get(i).label.equals("Training")) continue;
				if(entities.get(i).label.equals("Sword")) continue;
			
				if(entities.get(i).x > 80*Tile.SIZE||entities.get(i).x < 18*Tile.SIZE|| entities.get(i).y > 80*Tile.SIZE||entities.get(i).y < 30*Tile.SIZE){
					entities.get(i).respawn();
					if(entities.get(i).lives<0){
						toRemove.add(entities.get(i));
					}
				}

				combat(entities.get(i));
			}
			if(toRemove.size()>0){
				entities.removeAll(toRemove);
				toRemove.clear();
			}
			checkForWinner();
		}else{
			if(gc.getInput().isKeyPressed(Input.KEY_ENTER))startGame();
			if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
				System.out.println(s.getStateCount());
				s.enterState(States.MENU);
			}
		}
	}
	
	public void combat(Entity player){
			
			for(Entity opponent : entities){
				if(player==opponent) continue;
				if(opponent.invulnerable)continue;
				
				
				if(player.image == Resources.getImage("p1LightGroundNeutral")&& ((Player) player).getLGNHitBox().intersects(opponent)){
					opponent.getHit((player.facingRight) ? 2f : -2f, 0, 5);
					opponent.invulnerableTimer = ((Player) (player)).LATime;
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
					opponent.getHit((player.facingRight) ? 1.6f : -1.6f, 0, 10);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
					continue;
				}
				if(player.image == Resources.getImage("p1HeavyGroundDown2")&&((Player) player).getHGDHitBox().intersects(opponent)){
					opponent.getHit((player.facingRight) ? 1.2f : -1.2f, -2.5f, 13);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
					continue;
				}
				if(player.image == Resources.getImage("p1HeavyGroundRight2")&&((Player) player).getHGRHitBox().intersects(opponent)){
					opponent.getHit((player.facingRight) ? 2f : -2f, 0, 11);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
					continue;
				}
				if(player.image == Resources.getImage("p1HeavyAirDown")&&((Player) player).getHADHitBox().intersects(opponent)){
					opponent.getHit(0, 3.5f, 8);
					opponent.invulnerableTimer = ((Player) (player)).HATime + 100;
					continue;
				}
				
				if(player.image == Resources.getImage("p1HeavyAirUp2")&&((Player) player).getHAUHitBox().intersects(opponent)){
					opponent.getHit((player.facingRight) ? 1f : -1f, -3.5f, 11);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
					continue;
				}
				if(player.image == Resources.getImage("p1HeavyAirNeutral2")){
					if(((Player) player).getHAN1HitBox().intersects(opponent)){
						opponent.getHit( 2f, 0, 11);
						opponent.invulnerableTimer = ((Player) (player)).HATime;
					}else if(((Player) player).getHAN2HitBox().intersects(opponent)){
						opponent.getHit( -2f, 0, 11);
						opponent.invulnerableTimer = ((Player) (player)).HATime;
					}
					continue;
				}
				
				
				if((player.image == Resources.getImage("p1GrabGround")||player.image == Resources.getImage("p1GrabAir"))&&((Player) player).getGrabHitBox().intersects(opponent)){
					if(opponent.label.equals("Sword")){
						((Player) player).pickUpSword();
						toRemove.add(opponent);
						continue;
					}
					player.grabbing = true;
					opponent.grabbed = true;
					if(player.facingRight){
						opponent.x = player.x + 14* Tile.SCALE / 1.5f;
					}else{
						opponent.x = player.x - 14* Tile.SCALE / 1.5f;
					}
					opponent.y = player.y - 20* Tile.SCALE / 1.5f;
				}
				
				if(opponent.grabbed){
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
				if((player.image == Resources.getImage("p1LightGroundSword")||player.image == Resources.getImage("p1LightGroundSword"))&& ((Player) player).getLSwordHitBox().intersects(opponent)){
					opponent.getHit((player.facingRight) ? 4f : -4f, 0, 10);
					opponent.invulnerableTimer = ((Player) (player)).LATime;
					continue;
				}
				if(player.image == Resources.getImage("p1HeavyGroundSword2")&& ((Player) player).getHGSwordHitBox().intersects(opponent)){
					opponent.getHit((player.facingRight) ? 7f : -7f, 0, 20);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
					continue;
				}
				if(player.image == Resources.getImage("p1HeavyAirSword2")&& ((Player) player).getHASwordHitBox().intersects(opponent)){
					opponent.getHit((player.facingRight) ? 3f : -3f, -6f, 20);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
					continue;
				}
				if(player.image == Resources.getImage("p1HeavyAirSwordDown")&& ((Player) player).getHADownSwordHitBox().intersects(opponent)){
					opponent.getHit(0, 8f, 20);
					opponent.invulnerableTimer = ((Player) (player)).HATime;
					continue;
				}
				
			}
		
		
	}
	public void checkForWinner(){
		if(entities.size()==1){
			winner = entities.get(0).label;
			gameOver = true;
		}else if(entities.size()==2){
			int playersLeft = 0;
			String temp = null;
			for (int i = 0; i <entities.size();i++){
				if(entities.get(i).label.contains("Player")){
					playersLeft++;
					temp = entities.get(i).label;
				}	
			}
			if(playersLeft == 1){
				winner = temp;
				gameOver = true;
				tellClients("gameOver");
				System.out.println("GameOver");
			}
		}
	}
	
	public void startGame(){
		entities.clear();
		inputs.clear();
		entities.add(new Player(1));
		inputs.add(p1Input);
		entities.add(new Player(2));
		inputs.add(p2Input);
		//entities.add(new TrainingDummy());
		//entities.add(new Sword());
		winner = null;
		gameOver = false;
		tellClients("startGame");
		System.out.println("clients told to start game");
	}
	public class ClientHandler implements Runnable{
		ObjectInputStream inputStream;
		Socket socket;
		int playerNum;
		public ClientHandler(Socket clientSocket, int ID){
			try{
				playerNum = ID;
				socket = clientSocket;
				inputStream = new ObjectInputStream(socket.getInputStream());
				System.out.println("input stream from client made");
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		public void run(){
			Input newInput;
			try{
				while((newInput = (Input) inputStream.readObject()) != null){
					
					
					inputs.set(playerNum,newInput);
					System.out.println("Input received");
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
	}
	public class ServerInit implements Runnable{
		ObjectInputStream inputStream;
		Socket socket;
		int playerNum;
		public ServerInit(){
			
		}
		public void run(){

			try{
				ServerSocket serverSocket = new ServerSocket(10305);
				for(int i = 1; i <desiredPlayers;i++){
					Socket clientSocket = serverSocket.accept();
					System.out.println("Connection attempted");
					ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					outputStream.flush();
					System.out.println("output to client made");
					outputStreams.add(outputStream);

					Thread clientHandler = new Thread(new ClientHandler(clientSocket, i));
					clientHandler.start();
					System.out.println("client handler started");
				}
				serverSocket.close();
			}catch(Exception ex){ex.printStackTrace();}

			startGame();
			System.out.println("Game starting");
			preGame = false;

			
		}
		
	}
	
	public void updateClients(){
		
		for(int i = 0; i < outputStreams.size();i++){
			try{
				
				outputStreams.get(i).flush();
				outputStreams.get(i).writeObject("newEntities");
				outputStreams.get(i).writeObject(entities);

			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public void tellClients(String message){
		
		for(int i = 0; i < outputStreams.size();i++){
			try{
				ObjectOutputStream outputToClient = (ObjectOutputStream) outputStreams.get(i);
				outputToClient.flush();
				outputToClient.writeObject(message);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public int getID() {
		return States.LOCALGAME;
	}

}
