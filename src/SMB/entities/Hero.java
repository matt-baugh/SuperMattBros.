package SMB.entities;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import SMB.main.Resources;
import SMB.world.Tile;

public class Hero extends Entity{
	
	private float speed = 0.3f;

	@Override
	public void init() {
		x = 1550;
		y = 932;
		width = 23*Tile.SCALE;
		height = 22*Tile.SCALE;
		image = Resources.getImage("soldier");
		System.out.println("setting image");
	}
	

	@Override
	public void indivUpdate(GameContainer gc, int delta) {
		
		
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_UP)&&isOnSolid()){
			vPY -= 1.4f;
		}else if (input.isKeyDown(Input.KEY_DOWN)){
			//y+= speed*delta;
		}
		
		if (input.isKeyDown(Input.KEY_LEFT)){
			vPX = -speed;
		}else if (input.isKeyDown(Input.KEY_RIGHT)){
			vPX = speed;
		}else{
			vPX = 0;
		}
		
		if(testLeft()) x += speed *delta;
		if(testRight()) x -= speed *delta;
		if(testUp()) y += speed *delta;
		if(isWithin()) y -= (getEndY() % Tile.SIZE);
	}
}
