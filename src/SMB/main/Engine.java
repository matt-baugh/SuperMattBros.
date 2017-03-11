package SMB.main;

import java.io.File;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import SMB.states.*;
import SMB.world.World;


public class Engine extends StateBasedGame{

	public Engine() {
		super("SuperMattBros");
	}
	
	public static void main(String[] args){
		
		//sets up the window the game will be displayed in
		try {
			AppGameContainer game = new AppGameContainer(new Engine());
			game.setDisplayMode(Window.WIDTH, Window.HEIGHT, false);
			game.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
		
	public void initStatesList(GameContainer gc) throws SlickException {
		gc.setTargetFrameRate(60); //sets frame rate of game
		gc.setAlwaysRender(true); //means that the game is always rendered, even if window is not focused on
		gc.setMaximumLogicUpdateInterval(60);
		gc.setVSync(true);
		gc.setShowFPS(false);
		
		new Resources();
		
		try { //load the map
			World.load("RealMap.json");} catch (Exception e) 
			{
			e.printStackTrace();
			System.err.println("Map does not exist");
		}
		
		this.addState(new MenuState()); //so the game starts in the menu
		
		
	}
	

}
