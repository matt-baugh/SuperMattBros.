package SMB.entities;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import SMB.main.Resources;
import SMB.world.Tile;

public class Hero extends Entity{
	
	private float speed = 0.3f;
	private int jumpsRemaining = 1;

	@Override
	public void init() {
		x = 2500;
		y = 2700;
		width = 21*Tile.SCALE/1.5f;
		height = 47*Tile.SCALE/1.5f;
		image = Resources.getImage("p1Idle");
		System.out.println("setting image");
	}
	

	@Override
	public void indivUpdate(GameContainer gc, int delta) {
		
		
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_UP)){
			if(isOnSolid()||isOnPSolid()){
					vPY = -2f;
			}else if(jumpsRemaining==1){
				vPY = -2f;
				jumpsRemaining = 0;
			}
			System.out.println(jumpsRemaining);
		}
		
		if (input.isKeyDown(Input.KEY_LEFT)){
			vPX = -speed;
		}else if (input.isKeyDown(Input.KEY_RIGHT)){
			vPX = speed;
		}else{
			vPX = 0;
		}
		
		if(!input.isKeyDown(Input.KEY_DOWN)&&isOnPSolid()){
			if(vPY>=0){
			vPY = 0;
			vTY = 0;
			jumpsRemaining = 1;
			}
		}
		if(isOnSolid()){
			jumpsRemaining = 1;
		}

		
		if(testLeft()) x += speed *delta;
		if(testRight()) x -= speed *delta;
		if(testUp()) {
			y += Math.abs(vTY) *delta;
			vTY = 0;
			vPY = 0;
		}
		if(isWithin()) y -= (getEndY() % Tile.SIZE);
	}
}
