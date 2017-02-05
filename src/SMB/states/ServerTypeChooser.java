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


	public Image background, largeButton, smallButton;
	public int largeButtonWidth, smallButtonWidth, largeButtonHeight, firstComponentX, firstComponentY;
	public TextField inputTextField;

	@Override
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
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
		inputTextField.setFocus(true);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		background.draw(0,0);
		
		inputTextField.render(gc, g);

		smallButton.draw(firstComponentX, firstComponentY+140);
		Resources.normalFont.drawString(firstComponentX+13, firstComponentY+140+20, "2 player");
		Resources.normalFont.drawString(firstComponentX+25, firstComponentY+140+20+Resources.normalFont.getLineHeight(), "server");
		
		smallButton.draw(firstComponentX + smallButtonWidth + 20, firstComponentY+140);
		Resources.normalFont.drawString(firstComponentX+230+13, firstComponentY+140+20,  "4 player");
		Resources.normalFont.drawString(firstComponentX+230+25, firstComponentY+140+20+Resources.normalFont.getLineHeight(), "server");
		
		
		
		largeButton.draw(firstComponentX, firstComponentY+280);
		Resources.normalFont.drawString(firstComponentX+80, firstComponentY+320, "Return to menu");


	}



	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))handleButtons(gc, s);
		
	}

	private void handleButtons(GameContainer gc, StateBasedGame s) throws SlickException{
		//2 player server button
		if(gc.getInput().getMouseX() > firstComponentX && gc.getInput().getMouseY() > firstComponentY +140
				&& gc.getInput().getMouseX() < firstComponentX + smallButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 140){
			System.out.println("2 player server");
			s.addState(new ServerState(2, inputTextField.getText()));
			s.getState(States.SERVER).init(gc,s);
			s.enterState(States.SERVER);
		}
		if(gc.getInput().getMouseX() > firstComponentX + smallButtonWidth + 20 && gc.getInput().getMouseY() > firstComponentY +140
				&& gc.getInput().getMouseX() < firstComponentX + smallButtonWidth + 20 + smallButtonWidth
				&& gc.getInput().getMouseY() < firstComponentY + largeButtonHeight + 140){
			System.out.println("4 player server");
			s.addState(new ServerState(4, inputTextField.getText()));
			s.getState(States.SERVER).init(gc,s);
			s.enterState(States.SERVER);
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
		// TODO Auto-generated method stub
		return States.SERVERTYPECHOOSER;
	}

}
