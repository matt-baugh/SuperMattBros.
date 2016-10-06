package SMB.entities;


import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import SMB.main.Resources;
import SMB.world.Tile;

public class Hero extends Entity{
	
	private float speed = 0.3f;
	private int jumpsRemaining = 1;
	private int AmountDamaged = 0;
	private Animation currentAnimation;

	@Override
	public void init() {
		x = 4234;
		y = 3138;
		width = 21*Tile.SCALE/1.5f;
		height = 47*Tile.SCALE/1.5f;
		image = Resources.getImage("p1Idle");
		System.out.println("setting image");
		
		Animation LGN = new Animation (new Image[]{Resources.getImage("p1LightGroundNeutral"), Resources.getImage("p1Idle")}, 100, false);
		Animation LGR = new Animation (new Image[]{Resources.getImage("p1LightGroundRight"), Resources.getImage("p1Idle")}, 100, false);
		Animation LGD = new Animation (new Image[]{Resources.getImage("p1LightGroundDown"), Resources.getImage("p1Idle")}, 100, false);
	}
	

	@Override
	public void indivUpdate(GameContainer gc, int delta) {
		
		if(currentAnimation!=null){
			image = currentAnimation.getCurrentFrame();
		}
		
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
			facingRight = false;
		}else if (input.isKeyDown(Input.KEY_RIGHT)){
			vPX = speed;
			facingRight = true;
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
		if (input.isKeyPressed(Input.KEY_K)){
			System.out.println("("+x+","+y+")");
		}
		
		if(testLeft()) x -= vTX *delta;
		if(testRight()) x -= vTX *delta;
		if(testUp()) {
			y += Math.abs(vTY) *delta;
			vTY = 0;
			vPY = 0;
		}
		
		
		if (input.isKeyDown(Input.KEY_Z)&&(isOnSolid()||isOnPSolid())){
			
		}
		
		
		if(isWithin()) y -= (getEndY() % Tile.SIZE);
	}
	public void getHit(int xF, int xY, int damage){
		AmountDamaged += damage;
		vKX+=xF*(AmountDamaged/50);
		vKY+=xY*(AmountDamaged/50);
	}
}
