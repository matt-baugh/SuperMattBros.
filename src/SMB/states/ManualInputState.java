package SMB.states;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import SMB.main.Resources;

public class ManualInputState extends BasicGameState{
	//Declares variables
	String errorMessage;
	public Image background, largeButton;
	public int largeButtonWidth, largeButtonHeight, firstComponentX, firstComponentY;
	public TextField inputTextField;

	@Override
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		//Initialise variables
		background  = Resources.getImage("menuBackground");
		largeButton  = Resources.getImage("largeButton");
		firstComponentX = 1200;
		firstComponentY =  400;
		largeButtonWidth = 440;
		largeButtonHeight = 120;
		inputTextField = new TextField(gc, Resources.normalFont, 1200,400, largeButtonWidth, largeButtonHeight);
		inputTextField.setText("Input IP here");
		inputTextField.setAcceptingInput(true);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		//draws background graphic
		background.draw(0,0);
		//draws text field for user input
		inputTextField.render(gc, g);
		
		//draws connect button
		largeButton.draw(firstComponentX, firstComponentY+140);
		Resources.normalFont.drawString(firstComponentX+70, firstComponentY+180, "Connect to server");

		//draws return to menu button
		largeButton.draw(firstComponentX, firstComponentY+280);
		Resources.normalFont.drawString(firstComponentX+80, firstComponentY+320, "Return to menu");


	}



	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		inputTextField.setFocus(true);
		//if the mouse clicks, check to see if it's clicked on a button
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);
		
	}

	private void handleButtons(GameContainer gc, StateBasedGame s){
		//if has clicked on connect to IP button
		if(gc.getInput().getMouseX() > firstComponentX && gc.getInput().getMouseY() > firstComponentY +140
				&& gc.getInput().getMouseX() < firstComponentX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 140){
			//first make sure the string input is an IP
			if(checkIfValidIP(inputTextField.getText())){
				//if so make a client
				s.addState(new ClientState());
				try {
					//try to initialise the client with it connecting to that IP
					((ClientState) s.getState(States.CLIENT)).init(gc,s, inputTextField.getText());
					s.enterState(States.CLIENT);
				} catch (SlickException e) {
					e.printStackTrace();
					//if it can't connect, tell the user that
					inputTextField.setText("Cannot connect, no server at that IP");
				}
				
			}else{
				//tell the user if they did not enter an IP address
				inputTextField.setText("That is not a valid IP address, try again");
			}
		}
		//if return to menu button is pressed, do so
		if(gc.getInput().getMouseX() > firstComponentX && gc.getInput().getMouseY() > firstComponentY +280
				&& gc.getInput().getMouseX() < firstComponentX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 280){
			s.enterState(States.MENU);
		}
	}
	public static boolean checkIfValidIP (String ip) {
		try {
			if ( ip == null || ip.isEmpty() ) {
				//if there isn't an input, that isn't an IP address so return null
				return false;
			}

			String[] parts = ip.split( "\\." );
			if ( parts.length != 4 ) {
				//if its not made up of 4 parts, separated by "."'s its not an IP address
				return false;
			}

			for ( String s : parts ) {
				int i = Integer.parseInt( s );
				if ( (i < 0) || (i > 255) ) {
					//if any of the parts aren't a digit 
					//between 0 and 255 inclusive its not an IP address
					return false;
				}
			}
			if ( ip.endsWith(".") ) {
				//if it ends with a "." its not an ip address
				return false;
			}

			return true;
		} catch (NumberFormatException nfe) {
			//if one of the parts cant be cast to an int its not an 
			//ip address
			return false;
		}
	}
	@Override
	public int getID() {

		return States.IPINPUT;
	}

}
