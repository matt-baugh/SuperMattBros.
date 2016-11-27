package SMB.entities;

import org.newdawn.slick.GameContainer;

import SMB.main.Resources;
import SMB.world.Tile;

public class Sword extends Entity{

	@Override
	public void init() {
		image = Resources.getImage("sword");
		lives = 0;
		spawn();
		label = "Sword";
		width = 9 * Tile.SCALE / 1.5f;
		height = 24 * Tile.SCALE / 1.5f;
	}

	@Override
	protected void indivUpdate(GameContainer gc, int delta) {
		if(isOnPSolid()){
			vPY = 0;
		}
		vKY = 0;
		vKX = 0;
		
	}

	@Override
	void spawn() {
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

}
