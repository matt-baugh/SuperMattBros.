package SMB.world;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import SMB.main.Engine;
import SMB.main.Resources;
import SMB.main.Window;

public class World {
	//Declare variables
	public static Image[][] solids;
	public static Image[][] partialSolids;
	public static Image[][] background;
	public static int WIDTH;
	public static int HEIGHT;
	
	
	public static void render(float xRender, float yRender){
		//offset is 2, meaning 2 tiles will be rendered just off the screen
		int offset = 2;
		//x coordinate of the first tile in terms of tile width
		int xStart = (int)(xRender/Tile.SIZE) - offset;
		//y coordinate of the first tile in terms of tile width
		int yStart= (int)(yRender/Tile.SIZE) - offset;
		//x coordinate of the last tile in terms of tile width
		int xEnd = Window.WIDTH/Tile.SIZE + xStart + (offset*2);
		//y coordinate of the last tile in terms of tile width
		int yEnd = Window.HEIGHT/Tile.SIZE + yStart + (offset*2);
		
		//x is the x coordinate of the tile being drawn in terms of tile width
		//same for y but for y coordinate
		for (int x=xStart;x<xEnd;x++){
			for (int y=yStart;y<yEnd;y++){
				
				//when being drawn each tile has its coordinate multiplied by the size
				//of one tile, so it's in terms of pixels instead of tiles
				
				if(background[x][y]!=null){
					//first draws the background if there is one
					background[x][y].draw(x*Tile.SIZE, y*Tile.SIZE, Tile.SIZE, Tile.SIZE);
				}	
				//then draws solid or partially solid tile, so it appears on top of the background
				//can only have solid or partially solid on a tile, not both
				if(solids[x][y]!=null){
					solids[x][y].draw(x*Tile.SIZE, y*Tile.SIZE, Tile.SIZE, Tile.SIZE);
				}else if(partialSolids[x][y]!=null){
					partialSolids[x][y].draw(x*Tile.SIZE, y*Tile.SIZE, Tile.SIZE, Tile.SIZE);
				}
			}
		}
	}
	
	public static void load(String path) throws Exception{
		JSONParser parser = new JSONParser();
		//loads object at a location
		InputStream mapStream = Engine.class.getResourceAsStream(path);
		Object obj = parser.parse(new InputStreamReader(mapStream));
		//makes that into a JSON object
		JSONObject jObj = (JSONObject) obj;
		
		JSONArray layers = (JSONArray) jObj.get("layers");
		int amount = layers.size();
		for( int i = 0; i < amount;i++){
			//makes an individual JSONObject for that layer
			JSONObject layer = (JSONObject) layers.get(i);

			//gets information about the layer
			WIDTH = ((Long) layer.get("width")).intValue();
			HEIGHT = ((Long) layer.get("height")).intValue();
			
			//gets data from the layer and sets the correct  array of 
			//images to be the parsed version of that layer
			String type = (String) layer.get("name");
			if(type.equals("solids")){
				solids =  parse((JSONArray)layer.get("data"));
			}else if(type.equals("background")){
				background = parse((JSONArray)layer.get("data"));	
			}else if(type.equals("partialSolids")){
				partialSolids  = parse((JSONArray)layer.get("data"));
			}
			
		}
	}
	
	private static Image[][] parse(JSONArray arr){
		Image[][] layer = new Image[WIDTH][HEIGHT];
		int index;
		
		for(int y= 0;y<HEIGHT;y++){
			for(int x= 0;x<WIDTH;x++){
				////gets the number at the correct place in the array
				index = ((Long) arr.get((y*WIDTH)+x)).intValue();
				//fills the correct loccation in the array of images
				//with the corresponding image for that number
				layer[x][y] = getSpriteImage(index);
				
			}
		}
		return layer;
	}
	
	private static Image getSpriteImage(int index){
		//this is done as the program that makes the map uses 0 to represent an empty cell
		//rather than the sprite associated with the number 0
		if(index == 0) return null;
		//converts it so its 0 indexed
		index -=1;
		
		//gets required information about the SpriteSheet being used
		SpriteSheet sheet = (SpriteSheet) Resources.getSprite("tiles");
		int horizontal = sheet.getHorizontalCount();
		
		//works out the coordinates of the required subimage
		int y = (index/horizontal);
		int x = (index%horizontal);
		
		return sheet.getSubImage(x,y);
	}
	public static boolean inBounds(int x, int y){
		return(x>=0&&y>=0&&x<WIDTH&&y<HEIGHT);
	}
	//checks if there is a solid tile at that location
	public static boolean solidTile(int x, int y){
		return (inBounds(x,y)&&solids[x][y]!=null);
	}
	//checks if there is a partially solid tile at that location
	public static boolean pSolidTile(int x, int y){
		return (inBounds(x,y)&&partialSolids[x][y]!=null);
	}
	//checks if there is a solid tile at that specific location
	public static boolean hitTest(float x, float y){
		int xPoint = (int) (x / Tile.SCALE) % Tile.SMALL_SIZE;
		int yPoint = (int) (y / Tile.SCALE) % Tile.SMALL_SIZE;
		int xTile = (int) (x / Tile.SIZE);
		int yTile = (int) (y / Tile.SIZE);
		
		if(solidTile(xTile, yTile)){
			return (solids[xTile][yTile].getColor(xPoint, yPoint).a > 0);
		}
		return false;
	}
	//checks if there is a partially solid tile at that specific location
	public static boolean hitTestPSolid(float x, float y){
		int xPoint = (int) (x / Tile.SCALE) % Tile.SMALL_SIZE;
		int yPoint = (int) (y / Tile.SCALE) % Tile.SMALL_SIZE;
		int xTile = (int) (x / Tile.SIZE);
		int yTile = (int) (y / Tile.SIZE);
		
		if(pSolidTile(xTile, yTile)){
			return (partialSolids[xTile][yTile].getColor(xPoint, yPoint).a > 0);
		}
		return false;
	}
}
