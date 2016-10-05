package SMB.main;




import org.newdawn.slick.*;

import SMB.world.Tile;

import java.util.*;


public class Resources {
	
	public static Map<String, Image> images;
	public static Map<String, SpriteSheet> sprites;
	public static Map<String, Sound> sounds;
	
	public Resources(){
		images = new HashMap<String, Image>();
		sprites = new HashMap<String, SpriteSheet>();
		sounds = new HashMap<String, Sound>();
		
		try {
			images.put("p1Idle", loadImage("res/playerImages/NewPlayerIdle.png") );
			images.put("p1HeavyGroundDown1", loadImage("res/playerImages/NewPlayerHAGD1.png") );
			images.put("p1HeavyGroundDown2", loadImage("res/playerImages/NewPlayerHAGD2.png") );
			images.put("p1HeavyGroundRight1", loadImage("res/playerImages/NewPlayerHAGLR1.png") );
			images.put("p1HeavyGroundRight2", loadImage("res/playerImages/NewPlayerHAGLR2.png") );
			images.put("p1HeavyGroundNeutral1", loadImage("res/playerImages/NewPlayerHAGN1.png") );
			images.put("p1HeavyGroundNeutral2", loadImage("res/playerImages/NewPlayerHAGN2.png") );
			images.put("p1LightGroundDown", loadImage("res/playerImages/NewPlayerLAGD.png") );
			images.put("p1LightGroundRight", loadImage("res/playerImages/NewPlayerLAGLR.png") );
			images.put("p1LightGroundNeutral", loadImage("res/playerImages/NewPlayerLAGN.png") );
			images.put("p1GrabGround", loadImage("res/playerImages/NewPlayerGrG.png") );
			images.put("p1ThrowGroundDown", loadImage("res/playerImages/NewPlayerTGD.png") );
			images.put("p1ThrowGroundRight", loadImage("res/playerImages/NewPlayerTGLR.png") );
			images.put("p1ThrowGroundUp", loadImage("res/playerImages/NewPlayerTGU.png") );
			images.put("trainingDummy", loadImage("res/training.png") );
			
			sprites.put("tiles", loadSprite("res/tiles.png", Tile.SMALL_SIZE, Tile.SMALL_SIZE));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public static Image loadImage(String path) throws SlickException {
		return new Image(path,false, Image.FILTER_NEAREST);
	}
	
	public static SpriteSheet loadSprite(String path, int tw, int th) throws SlickException{
		return new SpriteSheet(loadImage(path), tw, th);
	}
	
	public static Image getSpriteImage(String getter,int x, int y){
		
		return sprites.get(getter).getSubImage(x, y);
	}
	public static Image getSprite(String getter){
		
		return sprites.get(getter);
	}
	
	
	
	public static Image getImage(String getter){
		
		return images.get(getter);
	}
	public static Sound getSound(String getter){
		
		return sounds.get(getter);
	}

}
