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
	private float AmountDamaged = 0;
	public boolean facingRight = true, grabbed = false, grabbing = false, canJump = true;
	public String label;
	public boolean invulnerable;
	public int invulnerableTimer;
	public boolean busy;
	public int busyTimer;
	public boolean walkingB = false;
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
		if (testUp()) {
			y += (Tile.SIZE - (y % Tile.SIZE));
			vTY = 0;
			vPY = 0;
		}
		if (isWithin()) y -= (getEndY() % Tile.SIZE);
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
	public void getHit(float xF, float xY, int damage){
		AmountDamaged += damage;
		vKX+=(xF*(AmountDamaged/50));
		vKY+=(xY*(AmountDamaged/50));
		System.out.println("vKX: "+vKX);
		System.out.println("vKY: "+vKY);
		System.out.println("Damaged: "+ AmountDamaged);
	}
	public Hitbox getLGNHitBox(){
		if(facingRight) return new Hitbox(x+18*Tile.SCALE/1.5f, y+ 18*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
		else  return new Hitbox(x-3*Tile.SCALE/1.5f, y+ 18*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
	}
	public Hitbox getLGDHitBox(){
		if(facingRight) return new Hitbox(x+21*Tile.SCALE/1.5f, y+ 30*Tile.SCALE/1.5f, 11*Tile.SCALE/1.5f, 11*Tile.SCALE/1.5f);
		else  return new Hitbox(x-11*Tile.SCALE/1.5f, y+ 30*Tile.SCALE/1.5f, 11*Tile.SCALE/1.5f, 11*Tile.SCALE/1.5f);
	}
	public Hitbox getLGRHitBox(){
		if(facingRight) return new Hitbox(x+22*Tile.SCALE/1.5f, y+ 17*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
		else  return new Hitbox(x-7*Tile.SCALE/1.5f, y+ 17*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
	}
	public Hitbox getLANHitBox(){
		if(facingRight) return new Hitbox(x+18*Tile.SCALE/1.5f, y+ 4*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f);
		else  return new Hitbox(x-3*Tile.SCALE/1.5f, y+ 4*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f);
	}
	public Hitbox getLARHitBox(){
		if(facingRight) return new Hitbox(x+17*Tile.SCALE/1.5f, y+ 21*Tile.SCALE/1.5f, 10*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f);
		else  return new Hitbox(x-6*Tile.SCALE/1.5f, y+ 21*Tile.SCALE/1.5f, 10*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f);
	}
	
	public Hitbox getHGNHitBox(){
		if(facingRight) return new Hitbox(x+17*Tile.SCALE/1.5f, y+ 30*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
		else  return new Hitbox(x-1*Tile.SCALE/1.5f, y+ 30*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
	}
	public Hitbox getHGDHitBox(){
		if(facingRight) return new Hitbox(x+23*Tile.SCALE/1.5f, y+ 41*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
		else  return new Hitbox(x-8*Tile.SCALE/1.5f, y+ 41*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
	}
	public Hitbox getHGRHitBox(){
		if(facingRight) return new Hitbox(x+24*Tile.SCALE/1.5f, y+ 31*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
		else  return new Hitbox(x-8*Tile.SCALE/1.5f, y+ 31*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
	}
	public Hitbox getHADHitBox(){
		if(facingRight) return new Hitbox(x+2*Tile.SCALE/1.5f, y+ 29*Tile.SCALE/1.5f, 19*Tile.SCALE/1.5f, 9*Tile.SCALE/1.5f);
		else  return new Hitbox(x*Tile.SCALE/1.5f, y+ 29*Tile.SCALE/1.5f, 19*Tile.SCALE/1.5f, 9*Tile.SCALE/1.5f);
	}
	public Hitbox getHAUHitBox(){
		if(facingRight) return new Hitbox(x+17*Tile.SCALE/1.5f, y+ 1*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f, 17*Tile.SCALE/1.5f);
		else  return new Hitbox(x-8*Tile.SCALE/1.5f, y+ 1*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f, 17*Tile.SCALE/1.5f);
	}
	public Hitbox getHAN1HitBox(){
		return new Hitbox(x+18*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f, 16*Tile.SCALE/1.5f);
	}
	public Hitbox getHAN2HitBox(){
		return new Hitbox(x-14*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f, 16*Tile.SCALE/1.5f);
	}
	
	
	public Hitbox getGrabHitBox(){
		if(facingRight) return new Hitbox(x+16*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 7*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
		else  return new Hitbox(x-2*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 7*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
	}
	
	
}
