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
			images.put("soldier", loadImage("res/placeHolder.png") );
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
