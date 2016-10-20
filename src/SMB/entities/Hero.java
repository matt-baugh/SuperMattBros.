package SMB.entities;



import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import SMB.main.Resources;
import SMB.world.Tile;

public class Hero extends Entity {

	private float speed = 0.5f;
	private int jumpsRemaining = 1;

	private Animation currentAnimation, LGN, LGR, LGD, LAN, LAR, HGN, HGD, HGR, HAN, HAU, HAD,
			GGAttempt, GGSuccess, GAAttempt, GASuccess, TGR, TGU, TGD, TAR, TAU, TAD, WalkingA;
	private Input input;

	public Hero(int playerNumber) {
		if(playerNumber == 1){
			color = Color.blue;
		}
	}

	@Override
	public void init() {
		label = "Player1";
		x = 4234;
		y = 3138;
		width = 21 * Tile.SCALE / 1.5f;
		height = 47 * Tile.SCALE / 1.5f;
		image = Resources.getImage("p1Idle");
		
		
		
		WalkingA = new Animation(new Image[]{Resources.getImage("p1Walking1"),Resources.getImage("p1Walking2")}, 100, false);

		LGN = new Animation(new Image[] {
				Resources.getImage("p1LightGroundNeutral"),
				Resources.getImage("p1Idle") }, 50, false);
		LGR = new Animation(new Image[] {
				Resources.getImage("p1LightGroundRight"),
				Resources.getImage("p1Idle") }, 50, false);
		LGD = new Animation(new Image[] {
				Resources.getImage("p1LightGroundDown"),
				Resources.getImage("p1Idle") }, 50, false);
		LAN = new Animation(new Image[] {
				Resources.getImage("p1LightAirNeutral"),
				Resources.getImage("p1IdleAir") }, 50, false);
		LAR = new Animation(new Image[] {
				Resources.getImage("p1LightAirRight"),
				Resources.getImage("p1IdleAir") }, 50, false);
		

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
		HAN = new Animation(new Image[] {
				Resources.getImage("p1HeavyAirNeutral1"),
				Resources.getImage("p1HeavyAirNeutral2"),
				Resources.getImage("p1IdleAir") }, new int[] { 200, 200, 100 },
				false);
		HAD = new Animation(new Image[] {
				Resources.getImage("p1HeavyAirDown")}, new int[] { 200 },
				false);
		HAU = new Animation(new Image[] {
				Resources.getImage("p1HeavyAirUp1"),
				Resources.getImage("p1HeavyAirUp2"),
				Resources.getImage("p1IdleAir") }, new int[] { 200, 200, 100 },
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
		TAR = new Animation(new Image[] {
				Resources.getImage("p1ThrowAirRight"),
				Resources.getImage("p1IdleAir") }, 250, false);
		TAU = new Animation(new Image[] {
				Resources.getImage("p1ThrowAirUp"),
				Resources.getImage("p1IdleAir") }, 250, false);
		TAD = new Animation(new Image[] {
				Resources.getImage("p1ThrowAirDown"),
				Resources.getImage("p1IdleAir") }, 250, false);
		GGAttempt = new Animation(new Image[] {
				Resources.getImage("p1GrabGround"),
				Resources.getImage("p1Idle") }, 300, false);
		GGSuccess = new Animation(
				new Image[] { Resources.getImage("p1GrabGround") },
				50, false);
		GAAttempt = new Animation(new Image[] {
				Resources.getImage("p1GrabAir"),
				Resources.getImage("p1IdleAir") }, 300, false);
		GASuccess = new Animation(
				new Image[] { Resources.getImage("p1GrabAir") },
				50, false);
	}

	@Override
	public void indivUpdate(GameContainer gc, int delta) {

		if (currentAnimation != null) {
			image = currentAnimation.getCurrentFrame();
			currentAnimation.update(delta);

		}
		if(image == Resources.getImage("p1HeavyAirNeutral2")){
			xImageOffset = 13*Tile.SCALE/1.5f;
		}else{
			xImageOffset = 0;
		}

			
			
		input = gc.getInput();
		
		if (!input.isKeyDown(Input.KEY_DOWN) && isOnPSolid()) {
			if (vPY >= 0) {
				vPY = 0;
				vTY = 0;
				jumpsRemaining = 1;
			}
		}
		if (isOnSolid()) {
			jumpsRemaining = 1;
			canJump = true;
		}
		if (input.isKeyPressed(Input.KEY_K)) {
			System.out.println("(" + x + "," + y + ")");
		}

		if (!grabbing&&!busy) {
			if (input.isKeyDown(Input.KEY_UP)) {
				if ((isOnSolid() || isOnPSolid())&&canJump) {
					vPY = -2f;
				} else if (jumpsRemaining == 1&&canJump) {
					vPY = -2f;
					jumpsRemaining = 0;
				}
				canJump = false;
				System.out.println(jumpsRemaining);
			}else{
				canJump = true;
			}

			if (input.isKeyDown(Input.KEY_LEFT)) {
				currentAnimation = WalkingA;
				currentAnimation.setLooping(true);
				vPX = -speed;
				facingRight = false;
			} else if (input.isKeyDown(Input.KEY_RIGHT)) {
				
				currentAnimation = WalkingA;
				currentAnimation.setLooping(true);
				
				vPX = speed;
				facingRight = true;
			} else {
				
				vPX = 0;
				if(currentAnimation == WalkingA){
					currentAnimation = null;
					image = Resources.getImage("p1Idle");
					
				}
			}

			if(image == Resources.getImage("p1Idle")&&!(isOnSolid()||isOnPSolid())){
				image = Resources.getImage("p1IdleAir");
			}
			if((image == Resources.getImage("p1IdleAir")||image == Resources.getImage("p1HeavyAirDown"))&&(isOnSolid()||isOnPSolid())){
				image = Resources.getImage("p1Idle");
				currentAnimation = null;
			}
			

			if (input.isKeyPressed(Input.KEY_Z) ) {
				busyTimer = 50;
				if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_LEFT)) {
					if(isOnSolid() || isOnPSolid()){
						currentAnimation = LGR;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
						if (facingRight)
							vKX = 1.3f;
						else
							vKX = -1.3f;
					}else{
						currentAnimation = LAR;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}
					
				} else if (input.isKeyDown(Input.KEY_DOWN)) {
					if(isOnSolid() || isOnPSolid()){
						currentAnimation = LGD;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}else{
						currentAnimation = LAR;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}
				} else {
					if(isOnSolid() || isOnPSolid()){
						currentAnimation = LGN;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}else{
						currentAnimation = LAN;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}
				}
			} else if (input.isKeyPressed(Input.KEY_X)) {
				busyTimer = 400;
				if (input.isKeyDown(Input.KEY_RIGHT)
						|| input.isKeyDown(Input.KEY_LEFT)) {
					if(isOnSolid() || isOnPSolid()){
						currentAnimation = HGR;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
						if (facingRight)
							vKX = 2f;
						else
							vKX = -2f;
					}
				} else if (input.isKeyDown(Input.KEY_DOWN)) {
					if(isOnSolid() || isOnPSolid()){
						currentAnimation = HGD;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}else{
						currentAnimation = HAD;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}
				}else if (input.isKeyDown(Input.KEY_UP)&&!(isOnSolid() || isOnPSolid())) {
					currentAnimation = HAU;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}else {
					if(isOnSolid() || isOnPSolid()){
						currentAnimation = HGN;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}else{
						currentAnimation = HAN;
						currentAnimation.setLooping(false);
						currentAnimation.restart();
					}
				}
			} else if (input.isKeyPressed(Input.KEY_C) ) {
				if(isOnSolid() || isOnPSolid()){
					currentAnimation = GGAttempt;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}else{
					currentAnimation = GAAttempt;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}
				busyTimer = 300;
				vPX = 0;

			}
		}else if (grabbing){
			currentAnimation = GGSuccess;
			currentAnimation.setLooping(false);
			currentAnimation.restart();
			if (input.isKeyDown(Input.KEY_RIGHT)) {
				busyTimer = 250;
				facingRight = true;
				if(isOnSolid()||isOnPSolid()) currentAnimation = TGR;
				else  currentAnimation = TAR;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
			}else if(input.isKeyDown(Input.KEY_LEFT)){
				busyTimer = 250;
				facingRight = false;
				if(isOnSolid()||isOnPSolid()) currentAnimation = TGR;
				else  currentAnimation = TAR;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
			}else if(input.isKeyDown(Input.KEY_UP)){	
				busyTimer = 250;
				if(isOnSolid()||isOnPSolid()) currentAnimation = TGU;
				else  currentAnimation = TAU;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
				
			} else if (input.isKeyDown(Input.KEY_DOWN)) {
				busyTimer = 250;
				if(isOnSolid()||isOnPSolid()) currentAnimation = TGD;
				else  currentAnimation = TAD;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
			}
		}
	}

}
