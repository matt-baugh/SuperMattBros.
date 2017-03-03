package SMB.states;


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
import SMB.tools.EntityInput;
import SMB.world.Tile;
import SMB.world.World;

public class LocalGameState extends BasicGameState {
	//declares variables
	public ArrayList<Entity> entities, toRemove;
	public ArrayList<EntityInput> inputs;
	public EntityInput p1Input, p2Input;
	private int xRender;
	private int yRender;

	public boolean gameOver;

	public String winner;
	
	public int swordSpawnTimer;

	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		//initialise variables
		xRender = 1366;
		yRender = 1791;
		 winner = null;
		swordSpawnTimer = 0;
		entities = new ArrayList<Entity>();
		p1Input = new EntityInput();
		p2Input = new EntityInput();
		inputs = new ArrayList<EntityInput>();
		startGame();

		toRemove = new ArrayList<Entity>();



	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {


		//moves everything that is drawn so that the map is central on the screan
		g.translate(-xRender, -yRender);
		//draws the map (all layers included)
		World.render(xRender, yRender);


		for (int i = 0; i <entities.size();i++){
			//renders all entities
			entities.get(i).render(gc, g);
		}
		if(winner!=null){
			//if there is a winner, displays some end of game text
			Resources.bigFont.drawString(2000, 2000, winner+" is the winner", Color.black);
			Resources.bigFont.drawString(1950, 2000+Resources.bigFont.getLineHeight(), "Press enter to play again", Color.black);
			Resources.bigFont.drawString(1800, 2000+(Resources.bigFont.getLineHeight()*2), "or press escape to return to the menu", Color.black);
		}
		//move everything back
		g.resetTransform();
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {



		if(!gameOver){
			//gets input for the players
			getPlayer1Input(gc);
			getPlayer2Input(gc);
			//for all the entities
			for (int i = 0; i <entities.size();i++){
				if(i<inputs.size()){
					//if there's an input for the entity, update it using that input
					//(all entities requiring input will always be at the begining
					//of entities so this works)
					entities.get(i).update(gc, delta, inputs.get(i));
				}else{
					//if not runs it without an input
					entities.get(i).update(gc, delta,null);
				}
				//continue if label is either of these, as all the following code
				//within the loop does not apply to them, only to players
				if(entities.get(i).label.equals("Training")) continue;
				if(entities.get(i).label.equals("Sword")) continue;
				//if the entity has gone outside the boundaries of the game
				if(entities.get(i).x > 80*Tile.SIZE||entities.get(i).x < 18*Tile.SIZE|| entities.get(i).y > 80*Tile.SIZE||entities.get(i).y < 30*Tile.SIZE){
					//run respawn method, which includes the decreasing of the players lives
					entities.get(i).respawn();
					//if the player has run out of lives, add them to the toRemove array
					if(entities.get(i).lives<0){
						toRemove.add(entities.get(i));
					}
				}
				//run the combat method for that entity
				combat(entities.get(i));
			}
			//remove all the elements in the toRemove arrayList from entities
			//if there is any, then clear toRemove
			if(toRemove.size()>0){
				entities.removeAll(toRemove);
				toRemove.clear();
			}
			//increments swordSpawnTimer if there isn't one in play
			if(!isSwordInPlay()){
				swordSpawnTimer++;
			}
			//once swordSpawnTimer reaches 2000 spawn a sword in
			if(swordSpawnTimer >= 2000){
				swordSpawnTimer = 0;
				entities.add(new Sword());
			}
			
			checkForWinner();
		}else{
			// if the game is over, restart the game if the enter key is pressed
			if(gc.getInput().isKeyPressed(Input.KEY_ENTER))startGame();
			//or return to the main menu if the escape key is pressed
			if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
				s.enterState(States.MENU);
			}
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
				//this attack involves two hitboxes, so each must be checked 
				//individually. Other than that is the same
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
		//if there's only one entity left, that entity must be the winner
		if(entities.size()==1){
			winner = entities.get(0).label;
			gameOver = true;
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
			//if there's one player left, thats the winner
			if(playersLeft == 1){
				winner = temp;
				gameOver = true;
			}
		}
	}

	public void startGame(){
		//resets the game
		entities.clear();
		entities.add(new Player(1));
		inputs.add(p1Input);
		entities.add(new Player(2));
		inputs.add(p2Input);
		winner = null;
		gameOver = false;
	}

	public void getPlayer1Input(GameContainer gc){
		//gets the 1st players input from their respective keys
		inputs.get(0).setUpKeyDown(gc.getInput().isKeyDown(Input.KEY_UP));
		inputs.get(0).setLeftKeyDown(gc.getInput().isKeyDown(Input.KEY_LEFT));
		inputs.get(0).setRightKeyDown(gc.getInput().isKeyDown(Input.KEY_RIGHT));
		inputs.get(0).setDownKeyDown(gc.getInput().isKeyDown(Input.KEY_DOWN));
		inputs.get(0).setLAKeyDown(gc.getInput().isKeyDown(Input.KEY_M));
		inputs.get(0).setHAKeyDown(gc.getInput().isKeyDown(Input.KEY_COMMA));
		inputs.get(0).setGrKeyDown(gc.getInput().isKeyDown(Input.KEY_PERIOD));
	}
	public void getPlayer2Input(GameContainer gc){
		//gets the 2nd players input from their respective keys
		inputs.get(1).setUpKeyDown(gc.getInput().isKeyDown(Input.KEY_W));
		inputs.get(1).setLeftKeyDown(gc.getInput().isKeyDown(Input.KEY_A));
		inputs.get(1).setRightKeyDown(gc.getInput().isKeyDown(Input.KEY_D));
		inputs.get(1).setDownKeyDown(gc.getInput().isKeyDown(Input.KEY_S));
		inputs.get(1).setLAKeyDown(gc.getInput().isKeyDown(Input.KEY_X));
		inputs.get(1).setHAKeyDown(gc.getInput().isKeyDown(Input.KEY_C));
		inputs.get(1).setGrKeyDown(gc.getInput().isKeyDown(Input.KEY_V));
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

	public int getID() {
		return States.LOCALGAME;
	}

}
