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

	private final double TERMINAL_V = 2.25;
	private float AmountDamaged = 0;
	public boolean facingRight = true; 
	public boolean grabbed = false; 
	public boolean grabbing = false; 
	public boolean canJump = true;
	public String label;
	public int lives;
	public boolean invulnerable;
	public int invulnerableTimer;
	public boolean busy;
	public int busyTimer;
	public float xImageOffset = 0;
	
	
	public float vTX, vPX, vKX; //T is total, P is player controlled, K is knockback
	public float vTY, vPY, vKY;
	
	
	public Entity(){
		
		init();
	}
	
	public abstract void init();
	
	public void render(GameContainer gc, Graphics g){
		if (image != null){
			image.getFlippedCopy(!facingRight, false ).draw((facingRight) ? x-xImageOffset: x+xImageOffset-(image.getWidth()*Tile.SCALE/1.5f - width),y,image.getWidth()*Tile.SCALE/1.5f, image.getHeight()*Tile.SCALE/1.5f, color);
		}
	}
	protected abstract void indivUpdate(GameContainer gc, int delta);
	
	abstract void spawn();
	
	public void update(GameContainer gc, int delta){
		
		height = image.getHeight()*Tile.SCALE/1.5f;
		
		if(busyTimer>0){
			busy = true;
			busyTimer-=delta;
		}else{
			busy=false;
		}
		if(invulnerableTimer>0){
			invulnerable = true;
			invulnerableTimer-=delta;
		}else{
			invulnerable = false;
		}
		if(!grabbed){
			vTX = vPX + vKX;
			vTY = vPY + vKY;
		}else{
			vTX = 0;
			vTY = 0;
		}
		
		if(vTY*delta>TERMINAL_V*delta){
			y+=TERMINAL_V*delta;
		}else if(vTY*delta<-TERMINAL_V*delta){
			y -=TERMINAL_V*delta;
		}else{
			y+=vTY*delta;
		}
		
		if(vTX*delta>TERMINAL_V*delta){	
			x+=TERMINAL_V*delta;
		}else if(vTX*delta<-TERMINAL_V*delta){
			x -=TERMINAL_V*delta;
		}else{
			x+=vTX*delta;
		}
		
		if(isOnSolid()){
			if(vTY>=0){
				vPY = 0;
				vKY = -vKY;
			}
		}else{
			vPY += 0.13f;
		}
		
		if(-0.5<vKX&&vKX<0.5)vKX=0;
		if(-0.5<vKY&&vKY<0.5)vKY=0;
		
		vKX *= 0.7;
		vKY *= 0.7;
		

		if (testLeft())
			x -= (vTX - 0.01f) * delta;
		if (testRight())
			x -= (vTX + 0.01f) * delta;
		if (testUp()){
			y += (Tile.SIZE - (y % Tile.SIZE));
			vTY = 0;
			vPY = 0;
		}
		while(isWithin()){ 
			y --;
		}
		indivUpdate(gc, delta);
	}
	
	public void respawn(){
		lives--;
		if(lives>=0){
			spawn();F
		}
	}
	
	
	
	public boolean testLeft(){
		return (World.hitTest(x, getCenterY()));
	}
	public boolean testRight(){
		return (World.hitTest(getEndX(), getCenterY()));
	}
	public boolean testUp(){
		return (World.hitTest(getCenterX(), y)||World.hitTest(x, y)||World.hitTest(getEndX(), y));
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
	public void getHit(float xF, float yF, int damage){
		AmountDamaged += damage;
		vKX+=((xF/2)+(xF/2)*(AmountDamaged/50));
		vKY+=((yF/2)+(yF/2)*(AmountDamaged/50));
		System.out.println("vKX: "+vKX);
		System.out.println("vKY: "+vKY);
		System.out.println("Damaged: "+ AmountDamaged);
	}
	
	
	
}
