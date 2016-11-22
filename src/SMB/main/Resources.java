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
			images.put("p1Walking1", loadImage("res/playerImages/NewPlayerWalk1.png") );
			images.put("p1Walking2", loadImage("res/playerImages/NewPlayerWalk2.png") );
			
			images.put("p1IdleAir", loadImage("res/playerImages/NewPlayerIdleAir.png") );
			images.put("p1HeavyAirDown", loadImage("res/playerImages/NewPlayerHAAD.png") );
			images.put("p1HeavyAirUp1", loadImage("res/playerImages/NewPlayerHAAU1.png") );
			images.put("p1HeavyAirUp2", loadImage("res/playerImages/NewPlayerHAAU2.png") );
			images.put("p1HeavyAirNeutral1", loadImage("res/playerImages/NewPlayerHAAN1.png") );
			images.put("p1HeavyAirNeutral2", loadImage("res/playerImages/NewPlayerHAAN2.png") );
			images.put("p1LightAirRight", loadImage("res/playerImages/NewPlayerLAALR.png") );
			images.put("p1LightAirNeutral", loadImage("res/playerImages/NewPlayerLAAN.png") );
			images.put("p1GrabAir", loadImage("res/playerImages/NewPlayerGrA.png") );
			images.put("p1ThrowAirDown", loadImage("res/playerImages/NewPlayerTAD.png") );
			images.put("p1ThrowAirRight", loadImage("res/playerImages/NewPlayerTALR.png") );
			images.put("p1ThrowAirUp", loadImage("res/playerImages/NewPlayerTAU.png") );
			
			images.put("p1IdleSword", loadImage("res/playerImages/NewPlayerIdleSword.png") );
			images.put("p1IdleAirSword", loadImage("res/playerImages/NewPlayerIdleAirSword.png") );
			images.put("p1HeavyGroundSword1", loadImage("res/playerImages/NewPlayerHAG1Sword.png") );
			images.put("p1HeavyGroundSword2", loadImage("res/playerImages/NewPlayerHAG2Sword.png") );
			images.put("p1HeavyAirSword1", loadImage("res/playerImages/NewPlayerHAA1Sword.png") );
			images.put("p1HeavyAirSword2", loadImage("res/playerImages/NewPlayerHAA2Sword.png") );
			images.put("p1HeavyAirSwordDown", loadImage("res/playerImages/NewPlayerHAADSword.png") );
			images.put("p1LightGroundSword", loadImage("res/playerImages/NewPlayerLAGSword.png") );
			images.put("p1LightAirSword", loadImage("res/playerImages/NewPlayerLAASword.png") );
			images.put("p1WalkingSword1", loadImage("res/playerImages/NewPlayerWalk1Sword.png") );
			images.put("p1WalkingSword2", loadImage("res/playerImages/NewPlayerWalk2Sword.png") );
			
			
			images.put("trainingDummy", loadImage("res/training.png") );
			
			images.put("sword", loadImage("res/Sword.png") );
			
			sprites.put("tiles", loadSprite("res/newTiles.png", Tile.SMALL_SIZE, Tile.SMALL_SIZE));
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
