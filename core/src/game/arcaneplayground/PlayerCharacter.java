package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerCharacter extends Actor{
	TextureAtlas walkingAtlas, attackEffectAtlas, weaponAtlas;
	Animation<TextureRegion> walkingAnim, standingAnim, attackEffectAnim, weaponAnim;
	String direction;
	String weaponName = "fist";
	int weaponLV;
	int controlLeft, controlRight, controlUp, controlDown, controlAttack;
	Rectangle hitbox, attackHitbox;
	float attackWidth = 40, attackHeight = 40;
	float hp = 100, hpMax = 100;// change hpmax when there is more than 1 character
	float speed_x;
	float speed_y;
	float speedLeft, speedRight, speedUp, speedDown;
	float time;
	float attack = 1;
	float attackCooldown = 0.5f;
	float currentAttackCooldown;
	float attackChargeTime = 0.5f;
	float currentChargeTime;
	boolean faceLeft = false;
	boolean charging = false;
	boolean attacking = false;
	boolean hurt = false;
	UI hpBar;
	EffectRenderer attackEffect;
	PlayerWeapon weapon;
	
	static TextureAtlas ninja = new TextureAtlas(Gdx.files.internal("character1.atlas"));
	static TextureAtlas cyclop = new TextureAtlas(Gdx.files.internal("character4.atlas"));
	static TextureAtlas pirate = new TextureAtlas(Gdx.files.internal("character2.atlas"));
	static TextureAtlas cyborg = new TextureAtlas(Gdx.files.internal("character3.atlas"));
	static TextureAtlas test = new TextureAtlas(Gdx.files.internal("testcharacter.atlas"));
	
	// temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp temp
	Texture temp;
	Texture temp2;
	
	public PlayerCharacter()
	{
		attackHitbox = new Rectangle(-1000, -1000, attackWidth, attackHeight);
	}
	public PlayerCharacter(float x, float y, float width, float height, int up, int down, int left, int right, int attack, UI hpBar, TextureAtlas weaponAtlas, Animation<TextureRegion> weaponAnim)// have to add argument for setting player textureatlas animation weapon here later
	{
		attackEffectAtlas = EffectRenderer.punchAtlas;
		attackEffectAnim = EffectRenderer.punchAnimation;
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		setControl(up, down, left, right, attack);
		hitbox = new Rectangle(x, y , width, height);
		attackHitbox = new Rectangle(x, y, attackWidth, attackHeight);
		direction = "right";
		this.hpBar = hpBar;
		this.weaponAtlas = weaponAtlas;
		this.weaponAnim = weaponAnim;
		
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
		if (currentChargeTime > 0)
		{
			currentChargeTime -= Gdx.graphics.getDeltaTime();
			if (charging && currentChargeTime <= 0)
			{
				currentAttackCooldown = attackCooldown;
				charging = false;
				attacking = true;
			}
		}
		hpBar.setHPBarColor(hp, hpMax);
		hpBar.setWidth(hp/hpMax*100);
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
		if (hurt)
		{
			//currentAnim = hurtAnim;
			if (currentAnim.isAnimationFinished(time))
			{
				hurt = false;
			}
		}
		batch.draw(currentAnim.getKeyFrame(time, true), (faceLeft ? this.getX()+this.getWidth() : this.getX()), this.getY(), (faceLeft ? -this.getWidth() : this.getWidth()), this.getHeight());
		batch.draw(temp2, attackHitbox.getX(), attackHitbox.getY(), attackHitbox.getWidth(), attackHitbox.getHeight());
		if(currentChargeTime > 0)
		{
			batch.draw(temp, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		}
	}
	public void setNewRect()
	{
		hitbox = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void updateHitbox()
	{//temporary adjust hitbox to fit texture
		hitbox.setX(this.getX()+15);
		hitbox.setY(this.getY()+1);
		hitbox.setWidth(this.getWidth()-30);
		hitbox.setHeight(this.getHeight()-18);
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
		}
		else if (direction.equals("right"))
		{
			attackHitbox.setX(hitbox.getX()+hitbox.getWidth());
			attackHitbox.setY(hitbox.getY()-((Math.abs(hitbox.getHeight()-attackHitbox.getHeight()))/2)+5);
			attackHitbox.setWidth(attackWidth);
			attackHitbox.setHeight(attackHeight);
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
				attack = item.attack[1];
				weaponLV = 2;
			}
			else if (weaponLV == 2)
			{
				//set value for lv3
				weaponLV = 3;
			}
		}
		else
		{
			//set value for lv1
			attackWidth = item.attackWidth[0];
			attackHeight = item.attackHeight[0];
			attack = item.attack[0];
			updateAttackEffect(item.effectAtlas, item.effectAnimation);
			weaponName = item.weaponName;
			weaponLV = 1;
			weaponAtlas = item.weaponAtlas;
			weapon.updateNewWeapon();
		}
	}
	public void setPlayerWeaponRenderer(PlayerWeapon weapon)
	{
		this.weapon = weapon;
	}
	public void setControl(int up, int down, int left, int right, int attack)
	{
		controlLeft = left;
		controlRight = right;
		controlUp = up;
		controlDown = down;
		controlAttack = attack;
	}
	public void setTexture(TextureAtlas textureatlas)
	{
		walkingAtlas = textureatlas;
		walkingAnim = new Animation<TextureRegion>(0.2f, walkingAtlas.getRegions());
		walkingAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		standingAnim = new Animation<TextureRegion>(0.5f, walkingAtlas.findRegions("0001"));
	}
}
