package SMB.tools;

import java.io.Serializable;

public class Hitbox  implements Serializable{
	//Declares variables
	public float x, y, width, height;

	public Hitbox(){
		
	}
	
	public Hitbox(float x, float y, float width, float height){
		this.x = x;
		this.y=y;
		this.width=width;
		this.height=height;
	} 
	//getters
	public float getEndX(){
		return(x+width);
	}
	public float getEndY(){
		return (y+height);
	}
	public float getHalfWidth(){
		return (width/2);
	}
	public float getHalfHeight(){
		return (height/2);
	}
	public float getCenterX(){
		return (x+getHalfWidth());
	}
	public float getCenterY(){
		return (y+getHalfHeight());
	}
	
	public boolean intersects(Hitbox other){
		//Compares other to this Hitbox, returning false if they don't intersect
		//and returning true if they do
		if(this.x > other.x + other.width) return false;
		if(this.x + this.width < other.x ) return false;
		if(this.y > other.y + other.height) return false;
		if(this.y + this.height < other.y ) return false;
		return true;
	}
}
