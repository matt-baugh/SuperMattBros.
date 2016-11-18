package SMB.entities;

import org.newdawn.slick.GameContainer;

import SMB.main.Resources;
import SMB.world.Tile;

public class Sword extends Entity{

	@Override
	public void init() {
		image = Resources.getImage("sword");
		x = 4663;
		y = 2838;
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

}
