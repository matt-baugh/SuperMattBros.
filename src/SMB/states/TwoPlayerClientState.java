package SMB.states;


import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
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
	
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		
			entities = new ArrayList<Entity>();
			startGame();
		
			toRemove = new ArrayList<Entity>();
			
		
		
	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		
		
		
		g.translate(-xRender, -yRender);
		World.render(xRender, yRender);
		
		
		for (int i = 0; i <entities.size();i++){
			entities.get(i).render(gc, g);
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

	public int getID() {
		return States.LOCALGAME;
	}

}
