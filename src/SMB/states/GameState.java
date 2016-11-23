package SMB.states;

import java.util.ArrayList;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import SMB.entities.*;
import SMB.main.Resources;
import SMB.main.Window;
import SMB.world.Tile;
import SMB.world.World;

public class GameState extends BasicGameState {
	
	public ArrayList<Entity> entities, toRemove;
	private int xRender = 2400;
	private int yRender = 2600;

	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		
		entities = new ArrayList<Entity>();
		entities.add(new Player(1));
		entities.add(new Player(2));
		entities.add(new TrainingDummy());
		entities.add(new Sword());
		
		toRemove = new ArrayList<Entity>();
		 
		
	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		
		
		
		g.translate(-xRender, -yRender);
		World.render(xRender, yRender);
		
		
		for (int i = 0; i <entities.size();i++){
			entities.get(i).render(gc, g);
		}
		g.resetTransform();
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) s.enterState(States.MENU);
		
		for (int i = 0; i <entities.size();i++){
		
			entities.get(i).update(gc, delta);
			if(entities.get(i).label.equals("Training")) continue;
			if(entities.get(i).label.equals("Sword")) continue;
			
			
			if(entities.get(i).x<xRender + 80)xRender -= 0.3f*delta;
			if(entities.get(i).y<yRender  + 60)yRender -= 0.3f*delta;
			
			if(entities.get(i).getEndX()>xRender + Window.WIDTH -80)xRender += 0.3f*delta;
			if(entities.get(i).getEndY()>yRender + Window.HEIGHT- 60)yRender += 0.3f*delta;
			
			combat(entities.get(i));
			
			
			
		}
		if(toRemove.size()>0){
			entities.removeAll(toRemove);
			toRemove.clear();
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
					System.out.println("groundpound hit");
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
					opponent.getHit((player.facingRight) ? 2f : -2f, -5f, 1);
					if(player.isOnSolid()||player.isOnPSolid()) player.image = Resources.getImage("p1Idle");
					else player.image = Resources.getImage("p1IdleAir");				
					opponent.invulnerableTimer = ((Player) (player)).GTime;
					continue;

				}
				if(player.image == Resources.getImage("p1ThrowGroundRight")||player.image == Resources.getImage("p1ThrowAirRight")){
					player.grabbing = false;
					opponent.grabbed = false;
					opponent.getHit((player.facingRight) ? 3f : -3f, -0.1f, 1);
					if(player.isOnSolid()||player.isOnPSolid()) player.image = Resources.getImage("p1Idle");
					else player.image = player.image = Resources.getImage("p1IdleAir");
					opponent.invulnerableTimer = ((Player) (player)).GTime;
					continue;
				}
				if(player.image == Resources.getImage("p1ThrowGroundDown")||player.image == Resources.getImage("p1ThrowAirDown")){
					player.grabbing = false;
					opponent.grabbed = false;
					opponent.getHit((player.facingRight) ? 1f : -1f, 4f, 1);
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
				
				
			}
		
		
	}

	public int getID() {
		return States.GAME;
	}

}
