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
		Resources.normalFont.drawString(firstButtonX+13, firstButtonY+20, "Find server");
		Resources.normalFont.drawString(firstButtonX+25, firstButtonY+20+Resources.normalFont.getLineHeight(), "manually");
		
		smallButton.draw(firstButtonX+230, firstButtonY);
		Resources.normalFont.drawString(firstButtonX+250, firstButtonY+20, "Start local");
		Resources.normalFont.drawString(firstButtonX+235, firstButtonY+20+Resources.normalFont.getLineHeight(), "game finder");
		
		largeButton.draw(firstButtonX, firstButtonY+140);
		Resources.normalFont.drawString(firstButtonX+95, firstButtonY+180, "Start a server");
		
		largeButton.draw(firstButtonX, firstButtonY+280);
		Resources.normalFont.drawString(firstButtonX+45, firstButtonY+320, "Local play (2 player)");
		
		largeButton.draw(firstButtonX, firstButtonY+420);
		Resources.normalFont.drawString(firstButtonX+130, firstButtonY+460, "Exit game");
		
		
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);
		
		if(gc.getInput().isKeyDown(Input.KEY_L)){
			//state tester
			s.addState(new ServerTypeChooser());
			s.getState(States.SERVERTYPECHOOSER).init(gc,s);
			s.enterState(States.SERVERTYPECHOOSER);
		}
		
	}
	
	private void handleButtons(GameContainer gc, StateBasedGame s) throws SlickException{
		
		// manual IP button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY 
				&& gc.getInput().getMouseX() < firstButtonX + smallButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight){
			System.out.println("manual IP");
			s.addState(new ManualInputState());
			s.getState(States.IPINPUT).init(gc,s);
			s.enterState(States.IPINPUT);
		}
		// game coordinator button
		if(gc.getInput().getMouseX() > firstButtonX +230 && gc.getInput().getMouseY() > firstButtonY 
				&& gc.getInput().getMouseX() < firstButtonX + 230 + smallButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight){
			
			s.addState(new GameCoordinator());
			s.getState(States.GAMECOORDINATOR).init(gc,s);
			s.enterState(States.GAMECOORDINATOR);
			
		}
		// 2 player server button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY +140
				&& gc.getInput().getMouseX() < firstButtonX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight + 140){
			System.out.println("2 player server");
			s.addState(new TwoPlayerServerState(2, "default"));
			s.getState(States.SERVERTWOPLAYER).init(gc,s);
			s.enterState(States.SERVERTWOPLAYER);
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
