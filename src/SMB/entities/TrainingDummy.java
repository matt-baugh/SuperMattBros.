package SMB.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import SMB.main.Resources;
import SMB.tools.EntityInput;
import SMB.world.Tile;

public class TrainingDummy extends Entity{
	//this is just a dummy i used to test combat when creating the game,
	//doesnt actually get used in the game but left in as
	//if i had more time one thing i could do would be to make a practice mode
	//including this
	@Override
	public void init() {
		color = Color.white;
		lives = 20;
		spawn();
		width = 21*Tile.SCALE/1.5f;
		height = 47*Tile.SCALE/1.5f;
		image = Resources.getImage("trainingDummy");
		label = "Training";
	}

	@Override
	protected void indivUpdate(GameContainer gc, int delta, EntityInput newInput) {
		
		if(isOnPSolid()){
			if(vPY>=0){
			vPY = 0;
			vTY = 0;
			}
		}
		
		
		
	}

	@Override
	void spawn() {
		x = 2157;
		y = 2065;
	}

	@Override
	protected void indivRender(GameContainer gc, Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
