package SMB.entities;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import SMB.main.Resources;
import SMB.tools.EntityInput;
import SMB.tools.Hitbox;
import SMB.world.Tile;
import SMB.world.World;


public abstract class Entity extends Hitbox implements Serializable{
	//declare variables and initialise where appropriate
	public transient Image image;
	public Color color;

	private transient final double TERMINAL_V = 2.25; //initialise variables
	public transient float AmountDamaged = 0;
	public transient boolean facingRight = true; 
	public transient boolean grabbed = false; 
	public transient boolean grabbing = false; 
	public transient boolean canJump = true;
	public String label, imageResourceLocation;
	public transient int lives;
	public transient boolean invulnerable;
	public transient int invulnerableTimer;
	public transient boolean busy;
	public transient int busyTimer;
	public transient float xImageOffset = 0;
	
	
	public float vTX, vPX, vKX; //T is total, P is player controlled, K is knockback
	public float vTY, vPY, vKY;
	
	
	public Entity(){
		init();
	}
	
	public abstract void init();
	
	public void render(GameContainer gc, Graphics g){
		//check the image exists
		if (image != null){					
			
			//draws the entity, facing the correct direction
			image.getFlippedCopy(!facingRight, false ).draw((facingRight) ? x-xImageOffset: x+xImageOffset-(image.getWidth()*Tile.SCALE/1.5f - width),y,image.getWidth()*Tile.SCALE/1.5f, image.getHeight()*Tile.SCALE/1.5f, color);
		}
		indivRender(gc, g);
	}
	
	public void clientRender(GameContainer gc, Graphics g){ //performs the same function as the render method except uses the resource location of the image, which is serializable, meaning can be done on the client side
		if (imageResourceLocation != null){
			try {
				Image temp = Resources.loadImage(imageResourceLocation);
				
				temp.getFlippedCopy(!facingRight, false ).draw((facingRight) ? x-xImageOffset: x+xImageOffset-(temp.getWidth()*Tile.SCALE/1.5f - width),y,temp.getWidth()*Tile.SCALE/1.5f, temp.getHeight()*Tile.SCALE/1.5f, color);
				
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		indivRender(gc, g);
	}
	
	//all these methods are specific to the type of entity, but instead of putting them normally in the classes I'm making them abstract in entity so i can call them when referring to the objects as entities
	
	protected abstract void indivRender(GameContainer gc, Graphics g);
	
	protected abstract void indivUpdate(GameContainer gc, int delta, EntityInput input);
	
	abstract void spawn();
	
	public void update(GameContainer gc, int delta, EntityInput input){
		
		//sets the height of the entitiy to the current height of the image representing the entity
		height = image.getHeight()*Tile.SCALE/1.5f;
		//updates the imageResourceLocation
		imageResourceLocation = image.getResourceReference();
		
		//determines whether the entity is busy or not
		if(busyTimer>0){
			busy = true;
			busyTimer-=delta;
		}else{
			busy=false;
		}
		//determines whether the entity is invulnerable or not
		if(invulnerableTimer>0){
			invulnerable = true;
			invulnerableTimer-=delta;
		}else{
			invulnerable = false;
		}
		//disables movement if the entity is grabbed
		if(!grabbed){
			vTX = vPX + vKX;
			vTY = vPY + vKY;
		}else{
			vTX = 0;
			vTY = 0;
		}
		
		//stops the entity's vertical velocity going over terminal velocity
		if(vTY*delta>TERMINAL_V*delta){
			y+=TERMINAL_V*delta;
		}else if(vTY*delta<-TERMINAL_V*delta){
			y -=TERMINAL_V*delta;
		}else{
			y+=vTY*delta;
		}
		
		//stops the entity's horizontal velocity going over terminal velocity
		if(vTX*delta>TERMINAL_V*delta){	
			x+=TERMINAL_V*delta;
		}else if(vTX*delta<-TERMINAL_V*delta){
			x -=TERMINAL_V*delta;
		}else{
			x+=vTX*delta;
		}
		
		//stops the entity falling through solid platforms (only solid not partially solid)
		if(isOnSolid()){
			if(vTY>=0){ //this means it only happens if the entity is falling
				vPY = 0;
				vKY = -vKY; //means if the entity was hit into the floor they will bound
			}
		}else{
			vPY += 0.13f;//gravity
		}
		
		if(-0.01<vKX&&vKX<0.01)vKX=0; //stops the knockback velocity continuously being a really small value
		if(-0.01<vKY&&vKY<0.01)vKY=0;
		
		vKX *= 0.7;//decreases the knockback velocity
		vKY *= 0.7;
		

		if (testLeft()) //stops entities passing through walls to the left, right or platforms above them
			x -= (vTX - 0.01f) * delta;
		if (testRight())
			x -= (vTX + 0.01f) * delta;
		if (testUp()){
			y += (Tile.SIZE - (y % Tile.SIZE));
			vTY = 0;
			vPY = 0;
		}
		while(isWithin()){ //moves the player upwards if the are somehow within a solid platform
			y --;
		}
		
		indivUpdate(gc, delta, input);
		
	}
	
	public void respawn(){ //reduces the number of lives by 1 then spawns the entity in if they have lives remaining
		lives--;
		if(lives>=0){
			spawn();
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
		AmountDamaged += damage;  //changes amount damage before knockback is calculated, so the damage from that specific hit is included in the knockback calculation
		vKX+=((xF/2)+(xF/2)*(AmountDamaged/50));
		vKY+=((yF/2)+(yF/2)*(AmountDamaged/50));
	}
	
	
	
}
