package SMB.entities;

import org.newdawn.slick.GameContainer;

import SMB.main.Resources;
import SMB.world.Tile;

public class TrainingDummy extends Entity{

	@Override
	public void init() {
		x = 4663;
		y = 3138;
		width = 21*Tile.SCALE/1.5f;
		height = 47*Tile.SCALE/1.5f;
		image = Resources.getImage("trainingDummy");
		label = "Training";
	}

	@Override
	protected void indivUpdate(GameContainer gc, int delta) {
		if(invulnerable){
			System.out.println(invulnerableTimer);
		}
		if(isOnPSolid()){
			if(vPY>=0){
			vPY = 0;
			vTY = 0;
			}
		}
		
		if(testLeft()) x -= vTX *delta;
		if(testRight()) x -= vTX *delta;
		if(testUp()) {
			y += Math.abs(vTY) *delta;
			vTY = 0;
			vPY = 0;
		}
		if(isWithin()) y -= (getEndY() % Tile.SIZE);
		
	}

}
