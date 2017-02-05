package SMB.entities;



import java.io.Serializable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import SMB.main.Resources;
import SMB.states.LocalGameState;
import SMB.tools.EntityInput;
import SMB.tools.Hitbox;
import SMB.world.Tile;

public class Player extends Entity{

	private float speed = 0.35f;
	private int jumpsRemaining = 1;

	private transient Animation currentAnimation, LGN, LGR, LGD, LAN, LAR, HGN, HGD, HGR, HAN, HAU, HAD,
			GGAttempt, GGSuccess, GAAttempt, GASuccess, TGR, TGU, TGD, TAR, TAU, TAD, Walking, WalkingSword, LGS, HGS, LAS, HAS, HASD;
	private transient EntityInput input;
	public int LATime, HATime, GTime;
	public int startingX, startingY;
	public boolean hasSword = false;
	public int swordTimer = 0;
	public float jumpHeight = -1.75f;
	public Color playerColor;
	
	
	
	public Player(int playerNumber) {
		lives = 4;
		if(playerNumber == 1){
			color = Color.red;
			playerColor = Color.red;
			label = "Player1";
			startingX = 1985;
			startingY = 2353;
			
		}
		if(playerNumber ==2){
			color = Color.blue;
			playerColor = Color.blue;
			label = "Player2";
			startingX = 2623;
			startingY = 2353;
			}
		if(playerNumber ==3){
			color = Color.green;
			playerColor = Color.green;
			label = "Player3";
			startingX = 2157;
			startingY = 2065;
			}
		if(playerNumber ==4){
			color = Color.yellow;
			playerColor = Color.yellow;
			label = "Player4";
			startingX = 2476;
			startingY = 2017;
			}
		spawn();
	}

	@Override
	public void init() {
		
		width = 21 * Tile.SCALE / 1.5f;
		height = 47 * Tile.SCALE / 1.5f;
		image = Resources.getImage("p1Idle");
		
		LATime = 100;
		HATime = 400;
		GTime = 300;
		
		Walking = new Animation(new Image[]{Resources.getImage("p1Walking1"),Resources.getImage("p1Walking2")}, 100, false);
		WalkingSword = new Animation(new Image[]{Resources.getImage("p1WalkingSword1"),Resources.getImage("p1WalkingSword2")}, 100, false);
		
		LGN = new Animation(new Image[] {
				Resources.getImage("p1LightGroundNeutral"),
				Resources.getImage("p1Idle") }, LATime, false);
		LGR = new Animation(new Image[] {
				Resources.getImage("p1LightGroundRight"),
				Resources.getImage("p1Idle") }, LATime, false);
		LGD = new Animation(new Image[] {
				Resources.getImage("p1LightGroundDown"),
				Resources.getImage("p1Idle") }, LATime, false);
		LGS = new Animation(new Image[] {
				Resources.getImage("p1LightGroundSword"),
				Resources.getImage("p1IdleSword") }, LATime, false);
		LAN = new Animation(new Image[] {
				Resources.getImage("p1LightAirNeutral"),
				Resources.getImage("p1IdleAir") }, LATime, false);
		LAR = new Animation(new Image[] {
				Resources.getImage("p1LightAirRight"),
				Resources.getImage("p1IdleAir") }, LATime, false);
		LAS = new Animation(new Image[] {
				Resources.getImage("p1LightAirSword"),
				Resources.getImage("p1IdleSword") }, LATime, false);
		

		HGN = new Animation(new Image[] {
				Resources.getImage("p1HeavyGroundNeutral1"),
				Resources.getImage("p1HeavyGroundNeutral2"),
				Resources.getImage("p1Idle") }, new int[] { HATime, HATime, 100 },
				false);
		HGD = new Animation(new Image[] {
				Resources.getImage("p1HeavyGroundDown1"),
				Resources.getImage("p1HeavyGroundDown2"),
				Resources.getImage("p1Idle") }, new int[] { HATime, HATime, 100 },
				false);
		HGR = new Animation(new Image[] {
				Resources.getImage("p1HeavyGroundRight1"),
				Resources.getImage("p1HeavyGroundRight2"),
				Resources.getImage("p1Idle") }, new int[] { HATime, HATime, 100 },
				false);
		HGS = new Animation(new Image[] {
				Resources.getImage("p1HeavyGroundSword1"),
				Resources.getImage("p1HeavyGroundSword2"),
				Resources.getImage("p1IdleSword") }, new int[] { HATime, HATime, 100 },
				false);
		HAN = new Animation(new Image[] {
				Resources.getImage("p1HeavyAirNeutral1"),
				Resources.getImage("p1HeavyAirNeutral2"),
				Resources.getImage("p1IdleAir") }, new int[] { HATime, HATime, 100 },
				false);
		HAD = new Animation(new Image[] {
				Resources.getImage("p1HeavyAirDown")}, new int[] { HATime },
				false);
		HAU = new Animation(new Image[] {
				Resources.getImage("p1HeavyAirUp1"),
				Resources.getImage("p1HeavyAirUp2"),
				Resources.getImage("p1IdleAir") }, new int[] { HATime, HATime, 100 },
				false);
		HAS = new Animation(new Image[] {
				Resources.getImage("p1HeavyAirSword1"),
				Resources.getImage("p1HeavyAirSword2"),
				Resources.getImage("p1IdleAirSword") }, new int[] { HATime, HATime, 100 },
				false);
		HASD = new Animation(new Image[] {
				Resources.getImage("p1HeavyAirSwordDown")}, new int[] { HATime },
				false);
		
		

		TGR = new Animation(new Image[] {
				Resources.getImage("p1ThrowGroundRight"),
				Resources.getImage("p1Idle") }, GTime, false);
		TGU = new Animation(new Image[] {
				Resources.getImage("p1ThrowGroundUp"),
				Resources.getImage("p1Idle") }, GTime, false);
		TGD = new Animation(new Image[] {
				Resources.getImage("p1ThrowGroundDown"),
				Resources.getImage("p1Idle") }, GTime, false);
		TAR = new Animation(new Image[] {
				Resources.getImage("p1ThrowAirRight"),
				Resources.getImage("p1IdleAir") }, GTime, false);
		TAU = new Animation(new Image[] {
				Resources.getImage("p1ThrowAirUp"),
				Resources.getImage("p1IdleAir") }, GTime, false);
		TAD = new Animation(new Image[] {
				Resources.getImage("p1ThrowAirDown"),
				Resources.getImage("p1IdleAir") }, GTime, false);
		GGAttempt = new Animation(new Image[] {
				Resources.getImage("p1GrabGround"),
				Resources.getImage("p1Idle") }, GTime, false);
		GGSuccess = new Animation(
				new Image[] { Resources.getImage("p1GrabGround") },
				50, false);
		GAAttempt = new Animation(new Image[] {
				Resources.getImage("p1GrabAir"),
				Resources.getImage("p1IdleAir") }, GTime, false);
		GASuccess = new Animation(
				new Image[] { Resources.getImage("p1GrabAir") },
				50, false);
	}

	@Override
	public void indivUpdate(GameContainer gc, int delta, EntityInput newInput) {
		
		if (currentAnimation != null) {
			image = currentAnimation.getCurrentFrame();
			currentAnimation.update(delta);

		}
		if(image == Resources.getImage("p1HeavyAirNeutral2")){
			xImageOffset = 13*Tile.SCALE/1.5f;
		}else{
			xImageOffset = 0;
		}
		
			
		input = newInput;
		
		if (!input.isDownKeyDown() && isOnPSolid()) {
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
		if(!busy){
			if (!grabbing&&!hasSword) {
				normalInput();
			}else if (grabbing){
				grabbingInput();
			}else if(hasSword){
				swordInput();
			}
		}

	}
	
	@Override
	void spawn() {
		if(lives == 4){
			x = startingX;
			y = startingY;
			lives--;
		}else{
			int i  = (int)(Math.random()*4);
			switch(i) {
				case 0:
					x = 1985;
					y = 2353;
				break;	
				case 1:
					x = 2623;
					y = 2353;
				break;	
				case 2:
					x = 2157;
					y = 2065;
				break;	
				case 3:
					x = 2476;
					y = 2017;
				break;	
				
			}
		}
		AmountDamaged = 0;
		vPY=0;
		vKY=0;
		
	}
	
	public void pickUpSword(){
		hasSword = true;
		swordTimer = 1000;
		image = Resources.getImage("p1IdleSword");
		currentAnimation = null;
		
	}
	
	public void normalInput(){

		if (input.isUpKeyDown()&&!input.isHAKeyDown()) {
			if ((isOnSolid() || isOnPSolid())&&canJump) {
				vPY = jumpHeight;
			} else if (jumpsRemaining == 1&&canJump) {
				vPY = jumpHeight;
				jumpsRemaining = 0;
			}
			canJump = false;
		}else{
			canJump = true;
		}

		if (input.isLeftKeyDown()) {
			currentAnimation = Walking;
			currentAnimation.setLooping(true);
			vPX = -speed;
			facingRight = false;
		} else if (input.isRightKeyDown()) {
			
			currentAnimation = Walking;
			currentAnimation.setLooping(true);
			
			vPX = speed;
			facingRight = true;
		} else {
			
			vPX = 0;
			if(currentAnimation == Walking){
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
		

		if (input.isLAKeyDown() ) {
			busyTimer = LATime;
			if (input.isRightKeyDown() || input.isLeftKeyDown()) {
				if(isOnSolid() || isOnPSolid()){
					currentAnimation = LGR;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
					if (facingRight)
						vKX = 0.5f;
					else
						vKX = -0.5f;
				}else{
					currentAnimation = LAR;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}
				
			} else if (input.isDownKeyDown()) {
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
		} else if (input.isHAKeyDown()) {
			busyTimer = HATime*2;
			if (input.isRightKeyDown()
					|| input.isLeftKeyDown()) {
				if(isOnSolid() || isOnPSolid()){
					currentAnimation = HGR;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
					
					if (facingRight){
						vKX = 0.5f;
						vPX = 0.1f;}
					else{
						vKX = -0.5f;
						vPX = -0.1f;}
				}
			} else if (input.isDownKeyDown()) {
				if(isOnSolid() || isOnPSolid()){
					currentAnimation = HGD;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}else{
					currentAnimation = HAD;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}
			}else if (input.isUpKeyDown()&&!(isOnSolid() || isOnPSolid())) {
				currentAnimation = HAU;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
				vPY = jumpHeight;
				
				
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
		} else if (input.isGrKeyDown() ) {
			if(isOnSolid() || isOnPSolid()){
				currentAnimation = GGAttempt;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
			}else{
				currentAnimation = GAAttempt;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
			}
			busyTimer = GTime;
			vPX = 0;

		}
	}
	
	public void grabbingInput(){

		currentAnimation = GGSuccess;
		currentAnimation.setLooping(false);
		currentAnimation.restart();
		if (input.isRightKeyDown()) {
			busyTimer = GTime;
			facingRight = true;
			if(isOnSolid()||isOnPSolid()) currentAnimation = TGR;
			else  currentAnimation = TAR;
			currentAnimation.setLooping(false);
			currentAnimation.restart();
		}else if(input.isLeftKeyDown()){
			busyTimer = GTime;
			facingRight = false;
			if(isOnSolid()||isOnPSolid()) currentAnimation = TGR;
			else  currentAnimation = TAR;
			currentAnimation.setLooping(false);
			currentAnimation.restart();
		}else if(input.isUpKeyDown()){	
			busyTimer = GTime;
			if(isOnSolid()||isOnPSolid()) currentAnimation = TGU;
			else  currentAnimation = TAU;
			currentAnimation.setLooping(false);
			currentAnimation.restart();
			
		} else if (input.isDownKeyDown()) {
			busyTimer = GTime;
			if(isOnSolid()||isOnPSolid()) currentAnimation = TGD;
			else  currentAnimation = TAD;
			currentAnimation.setLooping(false);
			currentAnimation.restart();
		}
	}
	
	public void swordInput(){
		color = color.lightGray;
		
		if (input.isUpKeyDown()&&!input.isHAKeyDown()) {
			if ((isOnSolid() || isOnPSolid())&&canJump) {
				vPY = jumpHeight;
			} else if (jumpsRemaining == 1&&canJump) {
				vPY = jumpHeight;
				jumpsRemaining = 0;
			}
			canJump = false;
		}else{
			canJump = true;
		}

		if (input.isLeftKeyDown()) {
			currentAnimation = WalkingSword;
			currentAnimation.setLooping(true);
			vPX = -speed;
			facingRight = false;
		} else if (input.isRightKeyDown()) {
			
			currentAnimation = WalkingSword;
			currentAnimation.setLooping(true);
			
			vPX = speed;
			facingRight = true;
		} else {
			
			vPX = 0;
			if(currentAnimation == WalkingSword){
				currentAnimation = null;
				image = Resources.getImage("p1IdleSword");
				
			}
		}
		if(image == Resources.getImage("p1IdleSword")&&!(isOnSolid()||isOnPSolid())){
			image = Resources.getImage("p1IdleAirSword");
			currentAnimation = null;
		}
		if((image == Resources.getImage("p1IdleAirSword")||image == Resources.getImage("p1HeavyAirDownSword"))&&(isOnSolid()||isOnPSolid())){
			image = Resources.getImage("p1IdleSword");
			currentAnimation = null;
		}
		if((image == Resources.getImage("p1HeavyAirSwordDown"))){
			if(isOnSolid()||(isOnPSolid()&&!input.isDownKeyDown())){
				image = Resources.getImage("p1IdleSword");
				currentAnimation = null;
			}else{
				image = Resources.getImage("p1IdleAirSword");
			}
		}
		
		
		if (input.isLAKeyDown() ) {
			busyTimer = LATime;
			
				if(isOnSolid() || isOnPSolid()){
					currentAnimation = LGS;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}else{
					currentAnimation = LAS;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}
			
		} else if (input.isHAKeyDown()) {
			busyTimer = HATime*2;
			if(isOnSolid()||isOnPSolid()){
				currentAnimation = HGS;
				currentAnimation.setLooping(false);
				currentAnimation.restart();
			}else{
				if(input.isDownKeyDown()){
					currentAnimation = HASD;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}else{
					currentAnimation = HAS;
					currentAnimation.setLooping(false);
					currentAnimation.restart();
				}
				
			}
			
			
		}
		
		swordTimer--;
		if(swordTimer<=0){
			hasSword = false;
			image = Resources.getImage("p1Idle");
			color = playerColor;
		}
		
	}
	
	public void indivRender(GameContainer gc, Graphics g){
		switch(Integer.parseInt(label.replaceAll("[\\D]", ""))){
			case 1:
				playerColor = Color.red;
				break;
			case 2:
				playerColor = Color.blue;
				break;
			case 3:
				playerColor = Color.green;
				break;
			case 4:
				playerColor = Color.yellow;
				break;
		}
		try {
			Resources.loadImage("res/playerImages/NewPlayerIcon.png").draw(3150, 1750+(Integer.parseInt(label.replaceAll("[\\D]", ""))*60), Resources.loadImage("res/playerImages/NewPlayerIcon.png").getWidth()*Tile.SCALE/1.5f , Resources.loadImage("res/playerImages/NewPlayerIcon.png").getHeight()*Tile.SCALE/1.5f, playerColor);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		Resources.normalFont.drawString(3050f,1760+(Integer.parseInt(label.replaceAll("[\\D]", ""))*60), String.valueOf(AmountDamaged) , playerColor);
		Resources.normalFont.drawString(3200f,1760+(Integer.parseInt(label.replaceAll("[\\D]", ""))*60), String.valueOf(lives) , playerColor);

	
		
	}
	
	public Hitbox getLGNHitBox(){
		if(facingRight) return new Hitbox(x+18*Tile.SCALE/1.5f, y+ 18*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
		else  return new Hitbox(x-3*Tile.SCALE/1.5f, y+ 18*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
	}
	public Hitbox getLGDHitBox(){
		if(facingRight) return new Hitbox(x+21*Tile.SCALE/1.5f, y+ 30*Tile.SCALE/1.5f, 11*Tile.SCALE/1.5f, 11*Tile.SCALE/1.5f);
		else  return new Hitbox(x-11*Tile.SCALE/1.5f, y+ 30*Tile.SCALE/1.5f, 11*Tile.SCALE/1.5f, 11*Tile.SCALE/1.5f);
	}
	public Hitbox getLGRHitBox(){
		if(facingRight) return new Hitbox(x+22*Tile.SCALE/1.5f, y+ 17*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
		else  return new Hitbox(x-7*Tile.SCALE/1.5f, y+ 17*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 4*Tile.SCALE/1.5f);
	}
	public Hitbox getLANHitBox(){
		if(facingRight) return new Hitbox(x+18*Tile.SCALE/1.5f, y+ 4*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f);
		else  return new Hitbox(x-3*Tile.SCALE/1.5f, y+ 4*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f);
	}
	public Hitbox getLARHitBox(){
		if(facingRight) return new Hitbox(x+17*Tile.SCALE/1.5f, y+ 21*Tile.SCALE/1.5f, 10*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f);
		else  return new Hitbox(x-6*Tile.SCALE/1.5f, y+ 21*Tile.SCALE/1.5f, 10*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f);
	}
	
	public Hitbox getHGNHitBox(){
		if(facingRight) return new Hitbox(x+17*Tile.SCALE/1.5f, y+ 30*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
		else  return new Hitbox(x-1*Tile.SCALE/1.5f, y+ 30*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
	}
	public Hitbox getHGDHitBox(){
		if(facingRight) return new Hitbox(x+23*Tile.SCALE/1.5f, y+ 41*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
		else  return new Hitbox(x-8*Tile.SCALE/1.5f, y+ 41*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
	}
	public Hitbox getHGRHitBox(){
		if(facingRight) return new Hitbox(x+24*Tile.SCALE/1.5f, y+ 31*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
		else  return new Hitbox(x-8*Tile.SCALE/1.5f, y+ 31*Tile.SCALE/1.5f, 5*Tile.SCALE/1.5f, 6*Tile.SCALE/1.5f);
	}
	public Hitbox getHADHitBox(){
		if(facingRight) return new Hitbox(x+2*Tile.SCALE/1.5f, y+ 14*Tile.SCALE/1.5f, 19*Tile.SCALE/1.5f, 24*Tile.SCALE/1.5f);
		else  return new Hitbox(x, y+ 14*Tile.SCALE/1.5f, 19*Tile.SCALE/1.5f, 24*Tile.SCALE/1.5f);
	}
	public Hitbox getHAUHitBox(){
		if(facingRight) return new Hitbox(x+17*Tile.SCALE/1.5f, y+ 1*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f, 17*Tile.SCALE/1.5f);
		else  return new Hitbox(x-8*Tile.SCALE/1.5f, y+ 1*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f, 17*Tile.SCALE/1.5f);
	}
	public Hitbox getHAN1HitBox(){
		return new Hitbox(x+18*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f, 16*Tile.SCALE/1.5f);
	}
	public Hitbox getHAN2HitBox(){
		return new Hitbox(x-14*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f, 16*Tile.SCALE/1.5f);
	}
	public Hitbox getGrabHitBox(){
		if(facingRight) return new Hitbox(x+16*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 7*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f);
		else  return new Hitbox(x-2*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 7*Tile.SCALE/1.5f, 12*Tile.SCALE/1.5f);
	}
	public Hitbox getLSwordHitBox(){
		if(facingRight) return new Hitbox(x+18*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 25*Tile.SCALE/1.5f, 19*Tile.SCALE/1.5f);
		else  return new Hitbox(x-22*Tile.SCALE/1.5f, y+ 12*Tile.SCALE/1.5f, 25*Tile.SCALE/1.5f, 19*Tile.SCALE/1.5f);
	}
	public Hitbox getHGSwordHitBox(){
		if(facingRight) return new Hitbox(x+25*Tile.SCALE/1.5f, y+ 18*Tile.SCALE/1.5f, 21*Tile.SCALE/1.5f, 9*Tile.SCALE/1.5f);
		else  return new Hitbox(x-25*Tile.SCALE/1.5f, y+ 18*Tile.SCALE/1.5f, 21*Tile.SCALE/1.5f, 9*Tile.SCALE/1.5f);
	}
	public Hitbox getHASwordHitBox(){
		if(facingRight) return new Hitbox(x+20*Tile.SCALE/1.5f, y, 17*Tile.SCALE/1.5f, 29*Tile.SCALE/1.5f);
		else  return new Hitbox(x-16*Tile.SCALE/1.5f, y, 17*Tile.SCALE/1.5f, 29*Tile.SCALE/1.5f);
	}
	public Hitbox getHADownSwordHitBox(){
		if(facingRight) return new Hitbox(x+16*Tile.SCALE/1.5f, y+ 24*Tile.SCALE/1.5f, 13*Tile.SCALE/1.5f, 23*Tile.SCALE/1.5f);
		else  return new Hitbox(x-8*Tile.SCALE/1.5f, y+ 24*Tile.SCALE/1.5f, 13*Tile.SCALE/1.5f, 23*Tile.SCALE/1.5f);
	}

	
	

}
