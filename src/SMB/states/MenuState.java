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
	//declare variable
	public Image background, smallButton, largeButton;
	public int firstButtonX, firstButtonY, smallButtonWidth, largeButtonWidth, buttonHeight;
	
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		//initialise variables
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
		//draw background graphic
		background.draw(0, 0);
		
		//draws button to access manual server connecter
		smallButton.draw(firstButtonX, firstButtonY);
		Resources.normalFont.drawString(firstButtonX+13, firstButtonY+20, "Find server");
		Resources.normalFont.drawString(firstButtonX+25, firstButtonY+20+Resources.normalFont.getLineHeight(), "manually");
		
		//draws button to access game coordinator
		//(local game finder)
		smallButton.draw(firstButtonX+230, firstButtonY);
		Resources.normalFont.drawString(firstButtonX+250, firstButtonY+20, "Start local");
		Resources.normalFont.drawString(firstButtonX+235, firstButtonY+20+Resources.normalFont.getLineHeight(), "game finder");
		
		//draws start server button
		largeButton.draw(firstButtonX, firstButtonY+140);
		Resources.normalFont.drawString(firstButtonX+95, firstButtonY+180, "Start a server");
		
		//draws local play button
		largeButton.draw(firstButtonX, firstButtonY+280);
		Resources.normalFont.drawString(firstButtonX+45, firstButtonY+320, "Local play (2 player)");
		
		//draws exit game button
		largeButton.draw(firstButtonX, firstButtonY+420);
		Resources.normalFont.drawString(firstButtonX+130, firstButtonY+460, "Exit game");
		
		
	}

	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		//if the mouse is clicked, check to see if it clicked on a button
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);
		
	}
	
	private void handleButtons(GameContainer gc, StateBasedGame s) throws SlickException{
		
		//check if it clicked on the manual IP button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY 
				&& gc.getInput().getMouseX() < firstButtonX + smallButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight){
			if(s.getState(States.IPINPUT)==null){
				s.addState(new ManualInputState());
				s.getState(States.IPINPUT).init(gc,s);
			}else{
				((ManualInputState) s.getState(States.IPINPUT)).inputTextField.setText("Input IP here");
				((ManualInputState) s.getState(States.IPINPUT)).inputTextField.setAcceptingInput(true);

			}
			s.enterState(States.IPINPUT);
		}
		//check if it clicked on the game coordinator button
		if(gc.getInput().getMouseX() > firstButtonX +230 && gc.getInput().getMouseY() > firstButtonY 
				&& gc.getInput().getMouseX() < firstButtonX + 230 + smallButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight){
			
			s.addState(new GameCoordinator());
			s.getState(States.GAMECOORDINATOR).init(gc,s);
			
			s.enterState(States.GAMECOORDINATOR);
			
		}
		//check if it clicked on the start server button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY +140
				&& gc.getInput().getMouseX() < firstButtonX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight + 140){
			if(s.getState(States.SERVERTYPECHOOSER)==null){
				s.addState(new ServerTypeChooser());
				s.getState(States.SERVERTYPECHOOSER).init(gc,s);
			}else{
				((ServerTypeChooser) s.getState(States.SERVERTYPECHOOSER)).inputTextField.setText("Input name of your server");
				((ServerTypeChooser) s.getState(States.SERVERTYPECHOOSER)).inputTextField.setAcceptingInput(true);

			}
			s.enterState(States.SERVERTYPECHOOSER);
		}
		
		//check if it clicked on the local game button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY +280
				&& gc.getInput().getMouseX() < firstButtonX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight + 280){
			s.addState(new LocalGameState());
			s.getState(States.LOCALGAME).init(gc,s);
			s.enterState(States.LOCALGAME);
		}
		//check if it clicked on the exit game button
		if(gc.getInput().getMouseX() > firstButtonX && gc.getInput().getMouseY() > firstButtonY +420
				&& gc.getInput().getMouseX() < firstButtonX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstButtonY + buttonHeight + 420){
			System.exit(0);
		}
		
	}

	public int getID() {
		return States.MENU;
	}

}
