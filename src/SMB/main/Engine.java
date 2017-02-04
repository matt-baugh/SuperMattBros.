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
		
		//setNativesFolder
		File f = new File("natives");
		if (f.exists())System.setProperty("org.lwjgl.librarypath", f.getAbsolutePath());
		
		
		try {
			AppGameContainer game = new AppGameContainer(new Engine());
			game.setDisplayMode(Window.WIDTH, Window.HEIGHT, false);
			game.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void initStatesList(GameContainer gc) throws SlickException {
		gc.setTargetFrameRate(60);
		gc.setAlwaysRender(true);
		gc.setMaximumLogicUpdateInterval(60);
		gc.setVSync(true);
		gc.setShowFPS(false);
		
		new Resources();
		
		try {
			World.load("res/maps/RealMap.json");
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Map does not exist");
		}
		
		this.addState(new MenuState());
		
		
	}
	

}
