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
	String errorMessage;
	public Image background, largeButton;
	public int largeButtonWidth, largeButtonHeight, firstComponentX, firstComponentY;
	public TextField inputTextField;

	@Override
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		background  = Resources.getImage("menuBackground");
		largeButton  = Resources.getImage("largeButton");
		firstComponentX = 1200;
		firstComponentY =  400;
		largeButtonWidth = 440;
		largeButtonHeight = 120;
		inputTextField = new TextField(gc, Resources.normalFont, 1200,400, largeButtonWidth, largeButtonHeight);
		inputTextField.setText("Input IP here");
		inputTextField.setFocus(true);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		background.draw(0,0);
		inputTextField.render(gc, g);

		largeButton.draw(firstComponentX, firstComponentY+140);
		Resources.normalFont.drawString(firstComponentX+70, firstComponentY+180, "Connect to server");

		largeButton.draw(firstComponentX, firstComponentY+280);
		Resources.normalFont.drawString(firstComponentX+80, firstComponentY+320, "Return to menu");


	}



	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);
		
	}

	private void handleButtons(GameContainer gc, StateBasedGame s){
		//Enter IP button
		if(gc.getInput().getMouseX() > firstComponentX && gc.getInput().getMouseY() > firstComponentY +140
				&& gc.getInput().getMouseX() < firstComponentX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 140){
			System.out.println("Enter IP");
			if(checkIfValidIP(inputTextField.getText())){
				System.out.println("valid");

				s.addState(new ClientState());
				System.out.println(s.getStateCount());
				System.out.println(s.getCurrentStateID());
				try {
					((ClientState) s.getState(States.CLIENT)).init(gc,s, inputTextField.getText());
				} catch (SlickException e) {
					e.printStackTrace();
				}
				s.enterState(States.CLIENT);
			}else{
				System.out.println("invalid");
			}
		}
		//Exit game button
		if(gc.getInput().getMouseX() > firstComponentX && gc.getInput().getMouseY() > firstComponentY +280
				&& gc.getInput().getMouseX() < firstComponentX + largeButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 280){
			System.out.println("Return to menu");
			s.enterState(States.MENU);
		}
	}
	public static boolean checkIfValidIP (String ip) {
		try {
			if ( ip == null || ip.isEmpty() ) {
				return false;
			}

			String[] parts = ip.split( "\\." );
			if ( parts.length != 4 ) {
				return false;
			}

			for ( String s : parts ) {
				int i = Integer.parseInt( s );
				if ( (i < 0) || (i > 255) ) {
					return false;
				}
			}
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

		return States.IPINPUT;
	}

}
