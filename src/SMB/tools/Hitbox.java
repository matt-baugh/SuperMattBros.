package SMB.tools;

public class Hitbox {
	public float x, y, width, height;
	
	public Hitbox(){
		
	}
	public Hitbox(float x, float y, float width, float height){
		this.x = x;
		this.y=y;
		this.width=width;
		this.height=height;
	} 
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
		if(this.x > other.x + other.width) return false;
		if(this.x + this.width < other.x ) return false;
		if(this.y > other.y + other.height) return false;
		if(this.y + this.height < other.y ) return false;
		return true;
	}
}
