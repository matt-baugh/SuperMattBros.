package SMB.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import SMB.tools.Hitbox;
import SMB.world.Tile;
import SMB.world.World;


public abstract class Entity extends Hitbox{
	
	public Image image;
	public Color color;
	private final int TERMINAL_X = 5;
	private final int TERMINAL_Y = 5;
	private int AmountDamaged = 0;
	public boolean facingRight = true;
	public String label;
	
	public float vTX, vPX, vKX; //T is total, P is player controlled, K is knockback
	public float vTY, vPY, vKY;
	
	
	public Entity(){
		
		init();
	}
	
	public abstract void init();
	
	public void render(GameContainer gc, Graphics g){
		if (image != null){
			image.getFlippedCopy(!facingRight, false ).draw((facingRight) ? x: x-(image.getWidth()*Tile.SCALE/1.5f - width),y,image.getWidth()*Tile.SCALE/1.5f, image.getHeight()*Tile.SCALE/1.5f, color);
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
		
		if(-1<vKX&&vKX<1)vKX=0;
		if(-1<vKY&&vKY<1)vKY=0;
		
		vKX *= 0.7;
		vKY *= 0.7;
		
		
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
	public void getHit(int xF, int xY, int damage){
		AmountDamaged += damage;
		vKX+=xF*(AmountDamaged/50);
		vKY+=xY*(AmountDamaged/50);
		System.out.println("vKX: "+vKX);
		System.out.println("vKY: "+vKY);
	}
	public Hitbox getLGNHitBox(){
		if(facingRight) return new Hitbox(x+18*Tile.SCALE/1.5f, y+ 18*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
		else  return new Hitbox(x-3*Tile.SCALE/1.5f, y+ 18*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
	}
	
}
