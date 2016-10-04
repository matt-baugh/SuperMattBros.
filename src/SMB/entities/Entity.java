package SMB.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import SMB.tools.Hitbox;
import SMB.world.World;


public abstract class Entity extends Hitbox{
	
	public Image image;
	public Color color;
	private final int TERMINAL_X = 5;
	private final int TERMINAL_Y = 5;
	
	public float vTX, vPX, vKX; //T is total, P is player controlled, K is knockback
	public float vTY, vPY, vKY;
	
	
	public Entity(){
		init();
	}
	
	public abstract void init();
	
	public void render(GameContainer gc, Graphics g){
		if (image != null){
			image.draw(x,y,width, height, color);
		}
	}
	protected abstract void indivUpdate(GameContainer gc, int delta);
	
	public void update(GameContainer gc, int delta){
		
		
		vTX = vPX + vKX;
		vTY = vPY + vKY;
		if(vTY<TERMINAL_Y){
			y += vTY*delta;
		}else{
			y+=TERMINAL_Y*delta;
		}
		if(vTX<TERMINAL_X){
			x += vTX*delta;
		}else{
			x+=TERMINAL_X*delta;
		}
		
		if(isOnSolid()){
			if(vTY>=0){
				vPY = 0;
				vTY = 0;
			}
		}else{
			vPY += 0.13f;
		}
		
		
		
		
		indivUpdate(gc, delta);
	}
	
	public boolean testLeft(){
		return (World.hitTest(x, getCenterY()));
	}
	public boolean testRight(){
		return (World.hitTest(getEndX(), getCenterY()));
	}
	public boolean testUp(){
		return (World.hitTest(getCenterX(), y));
	}
	public boolean testDown(){
		return (World.hitTest(getCenterX(), getEndY()));
	}
	public boolean isWithin(){
		return (World.hitTest(x,getEndY()) || World.hitTest(getEndX(),getEndY())) || World.hitTest(getCenterX(),getEndY()) ||World.hitTestPSolid(x, getEndY())||World.hitTestPSolid(getEndX(), getEndY())||World.hitTestPSolid(getCenterX(), getEndY());
	}
	public boolean isOnSolid(){
		return (World.hitTest(x, getEndY()+1)||World.hitTest(getEndX(), getEndY()+1))||World.hitTest(getCenterX(), getEndY()+1);
	}
	public boolean isOnPSolid(){
		return (World.hitTestPSolid(x, getEndY()+1)||World.hitTestPSolid(getEndX(), getEndY()+1))||World.hitTestPSolid(getCenterX(), getEndY()+1);
	}
	
}
