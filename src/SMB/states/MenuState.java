package SMB.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuState extends BasicGameState {

	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		g.drawString("DANK", 50, 50);
		
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) s.enterState(States.GAME);
		
	}

	public int getID() {
		// TODO Auto-generated method stub
		return States.MENU;
	}

}
