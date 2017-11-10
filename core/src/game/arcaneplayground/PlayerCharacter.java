package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerCharacter extends Actor{
	TextureAtlas walkingAtlas, attackEffectAtlas, weaponAtlas, deadAtlas;
	Animation<TextureRegion> walkingAnim, standingAnim, attackEffectAnim, weaponAnim, deadAnim;
	String direction;
	String weaponName = "fist";
	int weaponLV;
	int controlLeft, controlRight, controlUp, controlDown, controlAttack, controlBack;
	float playerMoveSpeed = moveSpeed;
	Rectangle hitbox, attackHitbox;
	float attackWidth = 40, attackHeight = 40;
	int hp = 3;//, hpMax = 100;// use hpmax if character have different hp
	float armor = 100;
	float speed_x;
	float speed_y;
	float speedLeft, speedRight, speedUp, speedDown;
	float time;
	float attackCooldown = 0.5f;
	float currentAttackCooldown;
	float attackChargeTime = 0.5f;
	float currentChargeTime;
	float blinkTime = 2f, currentBlinkTime;
	float attack;
	float regenDelay = 2;
	float trapDelay = 0;
	int blinkFrameCount;
	boolean moving = false;
	boolean blink = false;
	boolean faceLeft = false;
	boolean charging = false;
	boolean attacking = false;
	boolean hurt = false;
	boolean dead = false;
	boolean chargeMax = false;
	boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false;
	float speedBoostTime = 0;
	float slowTime = 0;
	float fadeTime = 2;
	String controlType = "keyboard";
	int controllerCount = -1;
	PovDirection controllerUp = PovDirection.north;
	PovDirection controllerDown = PovDirection.south;
	PovDirection controllerLeft = PovDirection.west;
	PovDirection controllerRight = PovDirection.east;
	UI hpBar;
	UI chargeBar;
	UI armorBar;
	EffectRenderer attackEffect;
	PlayerWeapon weapon;
	GameObject checkBlock;
	Arrow arrow;
	Balloon balloon;
	Sound attackSound;
	
	static float moveSpeed = 5;
	static TextureAtlas heart = new TextureAtlas(Gdx.files.internal("heart.atlas"));
	static Animation<TextureRegion> heart3 = new Animation<TextureRegion>(1f, heart.findRegions("0003"));
	static Animation<TextureRegion> heart2 = new Animation<TextureRegion>(1f, heart.findRegions("0002"));
	static Animation<TextureRegion> heart1 = new Animation<TextureRegion>(1f, heart.findRegions("0001"));
	static Animation<TextureRegion> heart0 = new Animation<TextureRegion>(1f, heart.findRegions("0004"));
	static TextureAtlas character1 = new TextureAtlas(Gdx.files.internal("character1.atlas"));
	static TextureAtlas character2 = new TextureAtlas(Gdx.files.internal("character2.atlas"));
	static TextureAtlas character3 = new TextureAtlas(Gdx.files.internal("character3.atlas"));
	static TextureAtlas character4 = new TextureAtlas(Gdx.files.internal("character4.atlas"));
	static TextureAtlas character1Dead = new TextureAtlas(Gdx.files.internal("char1death.atlas"));
	static TextureAtlas character2Dead = new TextureAtlas(Gdx.files.internal("char2death.atlas"));
	static TextureAtlas character3Dead = new TextureAtlas(Gdx.files.internal("char3death.atlas"));
	static TextureAtlas character4Dead = new TextureAtlas(Gdx.files.internal("char4death.atlas"));
	static Texture character1Lose = new Texture(Gdx.files.internal("char1lose.png"));
	static Texture character2Lose = new Texture(Gdx.files.internal("char2lose.png"));
	static Texture character3Lose = new Texture(Gdx.files.internal("char3lose.png"));
	static Texture character4Lose = new Texture(Gdx.files.internal("char4lose.png"));
	static Texture character1Win = new Texture(Gdx.files.internal("char1win.png"));
	static Texture character2Win = new Texture(Gdx.files.internal("char2win.png"));
	static Texture character3Win = new Texture(Gdx.files.internal("char3win.png"));
	static Texture character4Win = new Texture(Gdx.files.internal("char4win.png"));
	// new textureatlas and animation for dead animation for each character here
	
	// temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp
	Texture temp;
	Texture temp2;
	
	public PlayerCharacter()
	{
		attackHitbox = new Rectangle(-1000, -1000, attackWidth, attackHeight);
	}
	
	public PlayerCharacter(float x, float y, float width, float height, int up, int down, int left, int right, int attack, int back, UI hpBar, UI armorBar, TextureAtlas weaponAtlas, Animation<TextureRegion> weaponAnim)// have to add argument for setting player textureatlas animation weapon here later
	{
		attackEffectAtlas = EffectRenderer.punchAtlas;
		attackEffectAnim = EffectRenderer.punchAnimation;
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		setControl(up, down, left, right, attack, back);
		hitbox = new Rectangle(x, y , width, height);
		attackHitbox = new Rectangle(x, y, attackWidth, attackHeight);
		direction = "right";
		this.hpBar = hpBar;
		this.hpBar.animation = true;
		this.armorBar = armorBar;
		updateHPBar();
		this.weaponAtlas = weaponAtlas;
		this.weaponAnim = weaponAnim;
		attackSound = PlayerWeapon.fistSound;
		
		// temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp
		temp = new Texture(Gdx.files.internal("box.png"));
		temp2 = new Texture(Gdx.files.internal("box2.png"));
	}
	
	public void draw(Batch batch, float alpha)
	{
		time += Gdx.graphics.getDeltaTime();
		Animation<TextureRegion> currentAnim;
		if (currentAttackCooldown > 0)
		{
			currentAttackCooldown -= Gdx.graphics.getDeltaTime();
		}
		if (charging && currentChargeTime <= attackChargeTime)
		{
			currentChargeTime += Gdx.graphics.getDeltaTime();
			if (currentChargeTime >= attackChargeTime)
			{
				chargeMax = true;
			}
		}
		else if (!charging && currentChargeTime > 0 && !attacking)
		{
			currentChargeTime -= Gdx.graphics.getDeltaTime();
		}
		if (armor < 100 && !dead)// change max armor here
		{
			if (regenDelay > 0)
			{
				regenDelay -= Gdx.graphics.getDeltaTime();
			}
			else
			{
				armor += 5*Gdx.graphics.getDeltaTime();
			}
		}
		if (armor > 100)armor = 100;
		if (trapDelay > 0)
		{
			trapDelay -= Gdx.graphics.getDeltaTime();
		}
		if (speedBoostTime > 0)
		{
			speedBoostTime -= Gdx.graphics.getDeltaTime();
		}
		if (slowTime > 0)
		{
			slowTime -= Gdx.graphics.getDeltaTime();
			playerMoveSpeed = moveSpeed-3;
			if (slowTime <= 0)
			{
				playerMoveSpeed = moveSpeed;
			}
		}
		updateHPBar();
		updateChargeBar();
		updateArmorBar();
		updateHitbox();
		updateAttackHitbox();
		if (speed_x > 0)
		{
			faceLeft = false;
		}
		else if (speed_x < 0)
		{
			faceLeft = true;
		}
		if (speed_x == 0 && speed_y == 0)//change this later improve if condition to change animation between stand walk attack block hurt
		{
			currentAnim = standingAnim;
		}
		else
		{
			currentAnim = walkingAnim;
		}
		if (dead)
		{
			currentAnim = deadAnim;
			weapon.setVisible(false);
			fadeTime -= Gdx.graphics.getDeltaTime();
			if (fadeTime <= 0)
			{
				this.setVisible(false);
			}
		}
		if (hurt)
		{
			currentBlinkTime += Gdx.graphics.getDeltaTime();
			blinkFrameCount += 1;
			if (currentBlinkTime < blinkTime)
			{
				if (blinkFrameCount % 10 <= 5)
				{
					blink = true;
				}
				else
				{
					blink = false;
				}
			}
			else
			{
				hurt = false;
				blink = false;
				currentBlinkTime = 0;
				blinkFrameCount = 0;
			}
		}
		if (!blink)
		{
			batch.draw(currentAnim.getKeyFrame(time, true), (faceLeft ? this.getX()+this.getWidth() : this.getX()), this.getY(), (faceLeft ? -this.getWidth() : this.getWidth()), this.getHeight());
		}
		//batch.draw(temp2, attackHitbox.getX(), attackHitbox.getY(), attackHitbox.getWidth(), attackHitbox.getHeight());
		//if(currentChargeTime > 0)
		{
			//batch.draw(temp, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		}
		
		//updateCheckBlockPosition(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}
	
	public void setNewRect()
	{
		hitbox = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	public void updateHitbox()
	{//temporary adjust hitbox to fit texture
		hitbox.setY(this.getY()+2);
		hitbox.setWidth(this.getWidth()-35);
		hitbox.setHeight(this.getHeight()-26);
		if (faceLeft)
		{
			hitbox.setX(this.getX()-10+hitbox.getWidth());
		}
		else
		{
			hitbox.setX(this.getX()+15);
		}
	}
	
	public void updateAttackHitbox()
	{
		if (direction.equals("up"))
		{
			attackHitbox.setX(hitbox.getX()-((Math.abs(hitbox.getWidth()-attackHitbox.getWidth()))/2));
			attackHitbox.setY(hitbox.getY()+hitbox.getHeight());
			attackHitbox.setWidth(attackHeight);
			attackHitbox.setHeight(attackWidth);
		}
		else if (direction.equals("down"))
		{
			attackHitbox.setX(hitbox.getX()-((Math.abs(hitbox.getWidth()-attackHitbox.getWidth()))/2));
			attackHitbox.setY(hitbox.getY()-attackHitbox.getHeight());
			attackHitbox.setWidth(attackHeight);
			attackHitbox.setHeight(attackWidth);
		}
		else if (direction.equals("left"))
		{
			attackHitbox.setX(hitbox.getX()-attackHitbox.getWidth());
			attackHitbox.setY(hitbox.getY()-((Math.abs(hitbox.getHeight()-attackHitbox.getHeight()))/2)+5);
			attackHitbox.setWidth(attackWidth);
			attackHitbox.setHeight(attackHeight);
			faceLeft = true;
		}
		else if (direction.equals("right"))
		{
			attackHitbox.setX(hitbox.getX()+hitbox.getWidth());
			attackHitbox.setY(hitbox.getY()-((Math.abs(hitbox.getHeight()-attackHitbox.getHeight()))/2)+5);
			attackHitbox.setWidth(attackWidth);
			attackHitbox.setHeight(attackHeight);
			faceLeft = false;
		}
	}
	
	public void updateAttackEffect(TextureAtlas textureatlas, Animation<TextureRegion> animation)
	{
		attackEffectAtlas = textureatlas;
		attackEffectAnim = animation;
	}
	
	public void setPlayerAttackEffectRenderer(EffectRenderer effect)
	{
		this.attackEffect = effect;
	}
	
	public void updateNewWeapon(ItemDrop item)
	{
		if (weaponName.equals(item.weaponName))
		{
			if (weaponLV == 1)
			{
				//set value for lv2
				attackWidth = item.attackWidth[1];
				attackHeight = item.attackHeight[1];
				attackChargeTime = item.attackChargeTime[1];
				attack = item.attack[1];
				updateAttackEffect(item.effectAtlas, item.effectAnimation);
				weaponAtlas = item.weaponAtlas[1];
				weaponAnim = item.weaponLV2Animation;
				weaponLV = 2;
				weapon.updateWeaponAnimation();
				attackSound = item.weaponSound;
			}
			else if (weaponLV == 2)
			{
				//set value for lv3
				attackWidth = item.attackWidth[2];
				attackHeight = item.attackHeight[2];
				attackChargeTime = item.attackChargeTime[2];
				attack = item.attack[2];
				updateAttackEffect(item.effectAtlas, item.effectAnimation);
				weaponAtlas = item.weaponAtlas[2];
				weaponAnim = item.weaponLV3Animation;
				weaponLV = 3;
				weapon.updateWeaponAnimation();
				attackSound = item.weaponSound;
			}
		}
		else
		{
			//set value for lv1
			attackWidth = item.attackWidth[0];
			attackHeight = item.attackHeight[0];
			attackChargeTime = item.attackChargeTime[0];
			attack = item.attack[0];
			updateAttackEffect(item.effectAtlas, item.effectAnimation);
			weaponName = item.weaponName;
			weaponLV = 1;
			weaponAtlas = item.weaponAtlas[0];
			weaponAnim = item.weaponAnimation;
			weapon.updateWeaponAnimation();
			attackSound = item.weaponSound;
		}
	}
	
	public void setPlayerWeaponRenderer(PlayerWeapon weapon)
	{
		this.weapon = weapon;
	}
	
	public void setControl(int up, int down, int left, int right, int attack, int back)
	{
		controlLeft = left;
		controlRight = right;
		controlUp = up;
		controlDown = down;
		controlAttack = attack;
		controlBack = back;
	}
	
	public void setTexture(TextureAtlas textureatlas, TextureAtlas deadatlas)
	{
		walkingAtlas = textureatlas;
		walkingAnim = new Animation<TextureRegion>(0.2f, walkingAtlas.getRegions());
		walkingAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		standingAnim = new Animation<TextureRegion>(0.5f, walkingAtlas.findRegions("0001"));
		deadAtlas = deadatlas;
		deadAnim = new Animation<TextureRegion>(0.5f, deadAtlas.getRegions());
	}
	
	public void updateHPBar()
	{
		if (hp == 3)
		{
			hpBar.currentAnim = heart3;
		}
		else if (hp == 2)
		{
			hpBar.currentAnim = heart2;
		}
		else if (hp == 1)
		{
			hpBar.currentAnim = heart1;
		}
		else if (hp <= 0)
		{
			hpBar.currentAnim = heart0;
		}
	}
	
	public void setChargeBar(UI chargeBar)
	{
		this.chargeBar = chargeBar;
	}
	
	public void updateChargeBar()
	{
		chargeBar.setX(this.getX());
		chargeBar.setY(this.getY()+this.getHeight()+20);
		if (currentChargeTime <= 0)
		{
			chargeBar.setVisible(false);
		}
		else
		{
			chargeBar.setVisible(true);
		}
		if (charging)
		{
			chargeBar.red = 1f;
			chargeBar.green = 1f;
			chargeBar.blue = 0f;
		}
		else
		{
			chargeBar.red = 1f;
			chargeBar.green = 0f;
			chargeBar.blue = 0f;
		}
		if (chargeMax)
		{
			chargeBar.red = 0f;
			chargeBar.green = 1f;
			chargeBar.blue = 0f;
		}
		chargeBar.setWidth(currentChargeTime/attackChargeTime*60);
	}
	
	public void setCheckBlockObject(GameObject check)
	{
		checkBlock = check;
	}
	
	public void updateCheckBlockPosition(float x, float y, float width, float height)
	{
		checkBlock.hitbox.setX(x);
		checkBlock.hitbox.setY(y);
		checkBlock.hitbox.setWidth(width);
		checkBlock.hitbox.setHeight(height);
		checkBlock.setX(checkBlock.hitbox.getX());
		checkBlock.setY(checkBlock.hitbox.getY());
		checkBlock.setWidth(checkBlock.hitbox.getWidth());
		checkBlock.setHeight(checkBlock.hitbox.getHeight());
	}
	
	public void updateArmorBar()
	{
		armorBar.setWidth(armor*1.5f);
	}
	
	public void setBalloon(Balloon balloon)
	{
		this.balloon = balloon;
	}
	
	public void setArrowRenderer(Arrow arrow)
	{
		this.arrow = arrow;
	}
	
	public void setIngame(boolean ingame)
	{
		hpBar.setVisible(ingame);
		chargeBar.setVisible(ingame);
		armorBar.setVisible(ingame);
		attackEffect.setVisible(ingame);
		weapon.setVisible(ingame);
		checkBlock.setVisible(ingame);
		//arrow.setVisible(ingame);
		this.setVisible(ingame);
		if (ingame)
		{
			this.updateHitbox();
		}
		else
		{
			hitbox.setX(-1000);
			hitbox.setY(-1000);
		}
	}
}
