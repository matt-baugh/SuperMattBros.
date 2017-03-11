package SMB.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import SMB.main.Resources;

public class ServerTypeChooser extends BasicGameState{

	//Declares variables
	public Image background, largeButton, smallButton;
	public int largeButtonWidth, smallButtonWidth, largeButtonHeight, firstComponentX, firstComponentY;
	public TextField inputTextField;

	@Override
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		//Initialise variables
		background  = Resources.getImage("menuBackground");
		largeButton  = Resources.getImage("largeButton");
		smallButton  = Resources.getImage("smallButton");
		firstComponentX = 1200;
		firstComponentY =  400;
		largeButtonWidth = 440;
		smallButtonWidth = 210;
		largeButtonHeight = 120;
		inputTextField = new TextField(gc, Resources.normalFont, 1200,400, largeButtonWidth, largeButtonHeight);
		inputTextField.setText("Input name of your server");
		inputTextField.setAcceptingInput(true);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		//draw the background graphic
		background.draw(0,0);
		
		//draw the text field
		inputTextField.render(gc, g);

		//draw a small button, with text on it saying "2 player server"
		smallButton.draw(firstComponentX, firstComponentY+140);
		Resources.normalFont.drawString(firstComponentX+13, firstComponentY+140+20, "2 player");
		Resources.normalFont.drawString(firstComponentX+25, firstComponentY+140+20+Resources.normalFont.getLineHeight(), "server");
		
		//draw a small button, with text on it saying "4 player server"
		smallButton.draw(firstComponentX + smallButtonWidth + 20, firstComponentY+140);
		Resources.normalFont.drawString(firstComponentX+230+13, firstComponentY+140+20,  "4 player");
		Resources.normalFont.drawString(firstComponentX+230+25, firstComponentY+140+20+Resources.normalFont.getLineHeight(), "server");
		
		//draw a large button, with text on it saying "Return to menu"
		largeButton.draw(firstComponentX, firstComponentY+280);
		Resources.normalFont.drawString(firstComponentX+80, firstComponentY+320, "Return to menu");
	}
	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		inputTextField.setFocus(true);
		//if the mouse is clicked, check to see if it clicked on a button
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);
	}

	private void handleButtons(GameContainer gc, StateBasedGame s) throws SlickException{
		//check if the 2 player server button was pressed
		if(gc.getInput().getMouseX() > firstComponentX && gc.getInput().getMouseY() > firstComponentY +140
				&& gc.getInput().getMouseX() < firstComponentX + smallButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 140){
			//make a 2 player server, initialises it and changes to that state
			s.addState(new ServerState(2, inputTextField.getText()));
			s.getState(States.SERVER).init(gc,s);
			s.enterState(States.SERVER);
		}
		//check if the 4 player server button was pressed
		if(gc.getInput().getMouseX() > firstComponentX + smallButtonWidth + 20 && gc.getInput().getMouseY() > firstComponentY +140
				&& gc.getInput().getMouseX() < firstComponentX + smallButtonWidth + 20 + smallButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 140){
			//make a 4 player server, initialises it and changes to that state
			s.addState(new ServerState(4, inputTextField.getText()));
			s.getState(States.SERVER).init(gc,s);
			s.enterState(States.SERVER);
		}
		//check is the Exit game button was pressed
		if(gc.getInput().getMouseX() > firstComponentX && gc.getInput().getMouseY() > firstComponentY +280
				&& gc.getInput().getMouseX() < firstComponentX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 280){
			//if so return to the menu state
			s.enterState(States.MENU);
		}
	}
	public static boolean checkIfValidIP (String ip) {
		try {
			//if ip if null or is empty, it's not an IP address so return false
			if ( ip == null || ip.isEmpty() ) {
				return false;
			}

			String[] parts = ip.split( "\\." );
			//if ip is not made up of 4 parts seperated by "."'s, 
			//it's not an IP address so return false

			if ( parts.length != 4 ) {
				return false;
			}
			
			//if any of the parts of ip are not an integer between 0 and 255,
			//it's not an IP address so return false
			for ( String s : parts ) {
				int i = Integer.parseInt( s );
				if ( (i < 0) || (i > 255) ) {
					return false;
				}
			}
			//if ip ends with a "." it is not an IP address so return null
			if ( ip.endsWith(".") ) {
				return false;
			}

			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	@Override
	public int getID() {
		return States.SERVERTYPECHOOSER;
	}

}
