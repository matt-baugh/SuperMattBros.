package SMB.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import SMB.main.Resources;

public class MenuState extends BasicGameState {
	
	public Image background, smallButton, largeButton;
	public int firstButtonX, firstButtonY, smallButtonWidth, largeButtonWidth, buttonHeight;
	
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		background = Resources.getImage("menuBackground");
		smallButton  = Resources.getImage("smallButton");
		largeButton  = Resources.getImage("largeButton");
		firstButtonX = 1200;
		firstButtonY = 400;
		smallButtonWidth = 210;
		largeButtonWidth = 440;
		buttonHeight = 120;
		
	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		background.draw(0, 0);
		
		smallButton.draw(firstButtonX, firstButtonY);
		Resources.normalFont.drawString(firstButtonX+35, firstButtonY+20, "2 player");
		Resources.normalFont.drawString(firstButtonX+50, firstButtonY+20+Resources.normalFont.getLineHeight(), "server");
		
		smallButton.draw(firstButtonX+230, firstButtonY);
		Resources.normalFont.drawString(firstButtonX+265, firstButtonY+20, "2 player");
		Resources.normalFont.drawString(firstButtonX+285, firstButtonY+20+Resources.normalFont.getLineHeight(), "client");
		
		smallButton.draw(firstButtonX, firstButtonY+140);
		Resources.normalFont.drawString(firstButtonX+35, firstButtonY+160, "4 player");
		Resources.normalFont.drawString(firstButtonX+50, firstButtonY+160+Resources.normalFont.getLineHeight(), "server");
		
		smallButton.draw(firstButtonX+230, firstButtonY+140);
		Resources.normalFont.drawString(firstButtonX+265, firstButtonY+160, "4 player");
		Resources.normalFont.drawString(firstButtonX+285, firstButtonY+160+Resources.normalFont.getLineHeight(), "client");
		
		
		largeButton.draw(firstButtonX, firstButtonY+280);
		Resources.normalFont.drawString(firstButtonX+45, firstButtonY+320, "Local play (2 player)");
		
		largeButton.draw(firstButtonX, firstButtonY+420);
		Resources.normalFont.drawString(firstButtonX+130, firstButtonY+460, "Exit game");
		
		
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);
		
	}
	
	private void handleButtons(GameContainer gc, StateBasedGame s) throws SlickException{
		
		// 2 player server button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY 
				&& gc.getInput().getMouseX() < firstButtonX + smallButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight){
			System.out.println("2 player server");
			s.addState(new TwoPlayerServerState());
			s.getState(States.SERVERTWOPLAYER).init(gc,s);
			s.enterState(States.SERVERTWOPLAYER);
		}
		// 2 player client button
		if(gc.getInput().getMouseX() > firstButtonX +230 && gc.getInput().getMouseY() > firstButtonY 
				&& gc.getInput().getMouseX() < firstButtonX + 230 + smallButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight){
			System.out.println("2 player client");
		}
		// 4 player server button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY +140
				&& gc.getInput().getMouseX() < firstButtonX + smallButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight + 140){
			System.out.println("4 player server");
		}
		// 4 player client button
		if(gc.getInput().getMouseX() > firstButtonX +230 && gc.getInput().getMouseY() > firstButtonY +140
				&& gc.getInput().getMouseX() < firstButtonX + 230 + smallButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight + 140){
			System.out.println("4 player client");
		}
		//Local game button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY +280
				&& gc.getInput().getMouseX() < firstButtonX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight + 280){
			System.out.println("Local game");
			s.addState(new LocalGameState());
			s.getState(States.LOCALGAME).init(gc,s);
			s.enterState(States.LOCALGAME);
		}
		//Exit game button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY +420
				&& gc.getInput().getMouseX() < firstButtonX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight + 420){
			System.out.println("Exit");
			System.exit(0);
		}
		
	}

	public int getID() {
		// TODO Auto-generated method stub
		return States.MENU;
	}

}
