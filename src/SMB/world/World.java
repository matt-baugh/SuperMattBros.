package SMB.world;

import java.io.FileReader;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import SMB.main.Resources;
import SMB.main.Window;

public class World {
	
	public static Image[][] solids;
	public static Image[][] partialSolids;
	public static Image[][] background;
	public static int WIDTH;
	public static int HEIGHT;
	
	public static void render(float xRender, float yRender){
		
		int offset = 2;
		int xStart = (int)(xRender/Tile.SIZE) - offset;
		int yStart= (int)(yRender/Tile.SIZE) - offset;
		int xEnd = Window.WIDTH/Tile.SIZE + xStart + (offset*2);
		int yEnd = Window.HEIGHT/Tile.SIZE + yStart + (offset*2);
		
		for (int x=xStart;x<xEnd;x++){
			for (int y=yStart;y<yEnd;y++){
				if(solids[x][y]!=null){
					solids[x][y].draw(x*Tile.SIZE, y*Tile.SIZE, Tile.SIZE, Tile.SIZE);
				}else{
					background[x][y].draw(x*Tile.SIZE, y*Tile.SIZE, Tile.SIZE, Tile.SIZE);
				}
				if(partialSolids[x][y]!=null){
					partialSolids[x][y].draw(x*Tile.SIZE, y*Tile.SIZE, Tile.SIZE, Tile.SIZE);
				}
			}
		}
	}
	
	public static void load(String path) throws Exception{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(path));
		JSONObject jObj = (JSONObject) obj;
		
		JSONArray layers = (JSONArray) jObj.get("layers");
		int amount = layers.size();
		for( int i = 0; i < amount;i++){
			JSONObject layer = (JSONObject) layers.get(i);
			String type = (String) layer.get("name");
			System.out.println(type);
			WIDTH = ((Long) layer.get("width")).intValue();
			HEIGHT = ((Long) layer.get("height")).intValue();
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
				index = ((Long) arr.get((y*WIDTH)+x)).intValue();
				layer[x][y] = getSpriteImage(index);
				
			}
		}
		return layer;
	}
	
	private static Image getSpriteImage(int index){
		if(index == 0) return null;
		index -=1;
		
		SpriteSheet sheet = (SpriteSheet) Resources.getSprite("tiles");
		int vertical = sheet.getVerticalCount();
		int horizontal = sheet.getHorizontalCount();
		
		int y = (index/vertical);
		int x = (index%horizontal);
		
		return sheet.getSubImage(x,y);
		
		
	}
	public static boolean inBounds(int x, int y){
		return(x>=0&&y>=0&&x<WIDTH&&y<HEIGHT);
	}
	public static boolean solidTile(int x, int y){
		return (inBounds(x,y)&&solids[x][y]!=null);
	}
	public static boolean pSolidTile(int x, int y){
		return (inBounds(x,y)&&partialSolids[x][y]!=null);
	}
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
