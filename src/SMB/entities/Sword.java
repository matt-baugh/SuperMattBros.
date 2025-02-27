package SMB.entities;



import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import SMB.main.Resources;
import SMB.tools.EntityInput;
import SMB.world.Tile;

public class Sword extends Entity{

	@Override
	public void init() {
		//initialise some variables
		image = Resources.getImage("sword");
		lives = 0;
		spawn();
		label = "Sword";
		width = 9 * Tile.SCALE / 1.5f;
		height = 24 * Tile.SCALE / 1.5f;
		color = Color.white; 
		//white, meaning no filter is on it, which is desired as the image itself has the correct amount of grey
	}

	@Override
	protected void indivUpdate(GameContainer gc, int delta, EntityInput newInput) {
		if(isOnPSolid()){
			vPY = 0;
		}
		vKY = 0;
		vKX = 0;
		//stops the sword being knocked back, which makes sense as its a sword
	}

	@Override
	void spawn() { //chooses 1 of 5 locations for the sword to spawn
		int i  = (int)(Math.random()*5);
		switch(i) {
			case 0:
				x = 2320;
				y = 2161;
			break;	
			case 1:
				x = 1727;
				y = 2209;
			break;	
			case 2:
				x = 2848;
				y = 2065;
			break;	
			case 3:
				x = 2374;
				y = 2449;
			break;	
			case 4:
				x = 2046;
				y = 1969;
			break;	
		}
		
	}

	@Override
	protected void indivRender(GameContainer gc, Graphics g) {
		//nothing to do here, as no extra elements specific to the sword to render
	}

}
