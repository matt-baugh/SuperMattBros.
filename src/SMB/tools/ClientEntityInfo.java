package SMB.tools;

import java.io.Serializable;

import org.newdawn.slick.Color;

public class ClientEntityInfo implements Serializable{
	//Declares variables
	public float x, y, xOffset, AmountDamaged;
	public int lives, rColor, gColor, bColor, aColor;
	public String imageResourceLocation;
	public boolean facingRight;
	
	
	//getters and setters
	public int getrColor() {
		return rColor;
	}
	public void setrColor(int rColor) {
		this.rColor = rColor;
	}
	public int getgColor() {
		return gColor;
	}
	public void setgColor(int gColor) {
		this.gColor = gColor;
	}
	public int getbColor() {
		return bColor;
	}
	public void setbColor(int bColor) {
		this.bColor = bColor;
	}
	public int getaColor() {
		return aColor;
	}
	public void setaColor(int aColor) {
		this.aColor = aColor;
	}
	public float getAmountDamaged() {
		return AmountDamaged;
	}
	public void setAmountDamaged(float amountDamaged) {
		AmountDamaged = amountDamaged;
	}
	public int getLives() {
		return lives;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public boolean isFacingRight() {
		return facingRight;
	}
	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getxOffset() {
		return xOffset;
	}
	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}
	public String getImageResourceLocation() {
		return imageResourceLocation;
	}
	public void setImageResourceLocation(String imageResourceLocation) {
		this.imageResourceLocation = imageResourceLocation;
	}
}
