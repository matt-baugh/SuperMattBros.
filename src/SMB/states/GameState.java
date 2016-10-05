package SMB.states;

import java.util.ArrayList;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import SMB.entities.*;
import SMB.main.Window;
import SMB.world.World;

public class GameState extends BasicGameState {
	
	private ArrayList<Entity> entities;
	private int xRender = 2400;
	private int yRender = 2600;

	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		
		entities = new ArrayList<Entity>();
		entities.add(new Hero());
		entities.add(new TrainingDummy());
		 
		
	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		
		
		
		g.translate(-xRender, -yRender);
		World.render(xRender, yRender);
		
		int amount = entities.size();
		for (int i = 0; i <amount;i++){
			entities.get(i).render(gc, g);
		}
		g.resetTransform();
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) s.enterState(States.MENU);
		int amount = entities.size();
		for (int i = 0; i <amount;i++){
			entities.get(i).update(gc, delta);
			
			if(entities.get(i).x<xRender + 80)xRender -= 0.3f*delta;
			if(entities.get(i).y<yRender  + 60)yRender -= 0.3f*delta;
			
			if(entities.get(i).getEndX()>xRender + Window.WIDTH -80)xRender += 0.3f*delta;
			if(entities.get(i).getEndY()>yRender + Window.HEIGHT- 60)yRender += 0.3f*delta;
			
			
			
			Input input = gc.getInput();
			if (input.isKeyDown(Input.KEY_W)){
				yRender-= 0.3*delta;
			}else if (input.isKeyDown(Input.KEY_S)){
				yRender+= 0.3*delta;
			}
			
			if (input.isKeyDown(Input.KEY_A)){
				xRender-= 0.3*delta;
			}else if (input.isKeyDown(Input.KEY_D)){
				xRender+= 0.3*delta;
			}
		}
		
		
	}

	public int getID() {
		return States.GAME;
	}

}
