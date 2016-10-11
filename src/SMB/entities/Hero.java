package SMB.entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import SMB.main.Resources;
import SMB.world.Tile;

public class Hero extends Entity {

	private float speed = 0.3f;
	private int jumpsRemaining = 1;

	private Animation currentAnimation, LGN, LGR, LGD, HGN, HGD, HGR,
			GGAttempt, GGSuccess, TGR, TGU, TGD;
	private Input input;

	@Override
	public void init() {
		label = "Player1";
		x = 4234;
		y = 3138;
		width = 21 * Tile.SCALE / 1.5f;
		height = 47 * Tile.SCALE / 1.5f;
		image = Resources.getImage("p1Idle");
		System.out.println("setting image");

		LGN = new Animation(new Image[] {
				Resources.getImage("p1LightGroundNeutral"),
				Resources.getImage("p1Idle") }, 50, false);
		LGR = new Animation(new Image[] {
				Resources.getImage("p1LightGroundRight"),
				Resources.getImage("p1Idle") }, 50, false);
		LGD = new Animation(new Image[] {
				Resources.getImage("p1LightGroundDown"),
				Resources.getImage("p1Idle") }, 50, false);

		HGN = new Animation(new Image[] {
				Resources.getImage("p1HeavyGroundNeutral1"),
				Resources.getImage("p1HeavyGroundNeutral2"),
				Resources.getImage("p1Idle") }, new int[] { 200, 200, 100 },
				false);
		HGD = new Animation(new Image[] {
				Resources.getImage("p1HeavyGroundDown1"),
				Resources.getImage("p1HeavyGroundDown2"),
				Resources.getImage("p1Idle") }, new int[] { 200, 200, 100 },
				false);
		HGR = new Animation(new Image[] {
				Resources.getImage("p1HeavyGroundRight1"),
				Resources.getImage("p1HeavyGroundRight2"),
				Resources.getImage("p1Idle") }, new int[] { 200, 200, 100 },
				false);

		TGR = new Animation(new Image[] {
				Resources.getImage("p1ThrowGroundRight"),
				Resources.getImage("p1Idle") }, 250, false);
		TGU = new Animation(new Image[] {
				Resources.getImage("p1ThrowGroundUp"),
				Resources.getImage("p1Idle") }, 250, false);
		TGD = new Animation(new Image[] {
				Resources.getImage("p1ThrowGroundDown"),
				Resources.getImage("p1Idle") }, 250, false);
		GGAttempt = new Animation(new Image[] {
				Resources.getImage("p1GrabGround"),
				Resources.getImage("p1Idle") }, 50, false);
		GGSuccess = new Animation(
				new Image[] { Resources.getImage("p1GrabGround") }, 50, false);
	}

	@Override
	public void indivUpdate(GameContainer gc, int delta) {

		if (currentAnimation != null) {
			image = currentAnimation.getCurrentFrame();
			currentAnimation.update(delta);

		}

			
			
		input = gc.getInput();
		if (!grabbing) {
			if (input.isKeyPressed(Input.KEY_UP)) {
				if (isOnSolid() || isOnPSolid()) {
					vPY = -2f;
				} else if (jumpsRemaining == 1) {
					vPY = -2f;
					jumpsRemaining = 0;
				}
				System.out.println(jumpsRemaining);
			}

			if (input.isKeyDown(Input.KEY_LEFT)) {
				vPX = -speed;
				facingRight = false;
			} else if (input.isKeyDown(Input.KEY_RIGHT)) {
				vPX = speed;
				facingRight = true;
			} else {
				vPX = 0;
			}

			if (!input.isKeyDown(Input.KEY_DOWN) && isOnPSolid()) {
				if (vPY >= 0) {
					vPY = 0;
					vTY = 0;
					jumpsRemaining = 1;
				}
			}
			if (isOnSolid()) {
				jumpsRemaining = 1;
			}
			if (input.isKeyPressed(Input.KEY_K)) {
				System.out.println("(" + x + "," + y + ")");
			}

			if (testLeft())
				x -= vTX * delta;
			if (testRight())
				x -= vTX * delta;
			if (testUp()) {
				y += Math.abs(vTY) * delta;
				vTY = 0;
				vPY = 0;
			}

			if (input.isKeyPressed(Input.KEY_Z)
					&& (isOnSolid() || isOnPSolid())) {
				if (input.isKeyDown(Input.KEY_RIGHT)
						|| input.isKeyDown(Input.KEY_LEFT)) {
					currentAnimation = LGR;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
					if (facingRight)
						vKX = 1.3f;
					else
						vKX = -1.3f;
				} else if (input.isKeyDown(Input.KEY_DOWN)) {
					currentAnimation = LGD;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				} else {
					currentAnimation = LGN;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}
			} else if (input.isKeyPressed(Input.KEY_X) && (isOnSolid() || isOnPSolid())) {
				if (input.isKeyDown(Input.KEY_RIGHT)
						|| input.isKeyDown(Input.KEY_LEFT)) {
					currentAnimation = HGR;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
					if (facingRight)
						vKX = 2f;
					else
						vKX = -2f;
				} else if (input.isKeyDown(Input.KEY_DOWN)) {
					currentAnimation = HGD;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				} else {
					currentAnimation = HGN;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}
			} else if (input.isKeyPressed(Input.KEY_C) && (isOnSolid() || isOnPSolid())) {
				currentAnimation = GGAttempt;
				currentAnimation.setLooping(false);
				currentAnimation.restart();

			}
		}else{
			currentAnimation = GGSuccess;
			currentAnimation.setLooping(false);
			currentAnimation.restart();
			if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_LEFT)) {
				currentAnimation = TGR;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
			}else if(input.isKeyDown(Input.KEY_UP)){	
				currentAnimation = TGU;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
				
			} else if (input.isKeyDown(Input.KEY_DOWN)) {
				currentAnimation = TGD;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
			}
		}

		if (isWithin())
			y -= (getEndY() % Tile.SIZE);
	}

}
