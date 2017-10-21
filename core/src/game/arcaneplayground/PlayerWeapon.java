package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerWeapon extends Actor{
	TextureAtlas textureAtlas;
	Animation<TextureRegion> animation, currentAnim, normalAnim, chargingAnim, attackingAnim;
	PlayerCharacter player;
	float time;
	float originX, originY, moveX, moveY;
	
	// all weapon here
//	static TextureAtlas fist = new TextureAtlas(Gdx.files.internal("fist.atlas"));
//	static Animation<TextureRegion> fistAnim = new Animation<TextureRegion>(0.5f, fist.getRegions());
	
	static TextureAtlas sword = new TextureAtlas(Gdx.files.internal("sword.atlas"));
	static Animation<TextureRegion> swordAnim = new Animation<TextureRegion>(0.1f, sword.getRegions());
	static TextureAtlas swordLV2 = new TextureAtlas(Gdx.files.internal("swordlv2.atlas"));
	static Animation<TextureRegion> swordLV2Anim = new Animation<TextureRegion>(0.1f, swordLV2.getRegions());
//	static TextureAtlas swordLV3 = new TextureAtlas(Gdx.files.internal("swordlv3.atlas"));
//	static Animation<TextureRegion> swordLV3Anim = new Animation<TextureRegion>(0.1f, swordLV3.getRegions());
	
	static TextureAtlas spear = new TextureAtlas(Gdx.files.internal("spear.atlas"));
	static Animation<TextureRegion> spearAnim = new Animation<TextureRegion>(0.1f, spear.getRegions());
//	static TextureAtlas spearLV2 = new TextureAtlas(Gdx.files.internal("spearlv2.atlas"));
//	static Animation<TextureRegion> spearLV2Anim = new Animation<TextureRegion>(0.1f, spearLV2.getRegions());
	static TextureAtlas spearLV3 = new TextureAtlas(Gdx.files.internal("spearlv3.atlas"));
	static Animation<TextureRegion> spearLV3Anim = new Animation<TextureRegion>(0.1f, spearLV3.getRegions());
	
	static TextureAtlas axe = new TextureAtlas(Gdx.files.internal("axe.atlas"));
	static Animation<TextureRegion> axeAnim = new Animation<TextureRegion>(0.1f, axe.getRegions());
//	static TextureAtlas axeLV2 = new TextureAtlas(Gdx.files.internal("axelv2.atlas"));
//	static Animation<TextureRegion> axeLV2Anim = new Animation<TextureRegion>(0.1f, axeLV2.getRegions());
//	static TextureAtlas axeLV3 = new TextureAtlas(Gdx.files.internal("axelv3.atlas"));
//	static Animation<TextureRegion> axeLV3Anim = new Animation<TextureRegion>(0.1f, axeLV3.getRegions());
	// all weapon here
	
	public PlayerWeapon()
	{
		
	}
	public PlayerWeapon(PlayerCharacter player)
	{
		this.player = player;
		this.textureAtlas = player.weaponAtlas;
		this.animation = player.weaponAnim;
		this.setX(player.getX());
		this.setY(player.getY());
		this.setWidth(47);
		this.setHeight(67);
		normalAnim = new Animation<TextureRegion>(0.5f, textureAtlas.findRegions("0001"));
		chargingAnim = new Animation<TextureRegion>(0.5f, textureAtlas.findRegion("attack", 1));
		attackingAnim = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("attack"));
		currentAnim = normalAnim;
	}
	public void draw(Batch batch, float alpha)
	{
		updateWeaponPosition();
		time += Gdx.graphics.getDeltaTime();
		if (player.charging)
		{
			currentAnim = chargingAnim;
			time = 0;
		}
		else
		{
			if (currentAnim != animation)
			{
				currentAnim = normalAnim;
			}
		}
		if(player.attacking)
		{
			currentAnim = animation;
			time = 0;
		}
		//batch.draw(currentAnim.getKeyFrame(time), this.getX(), this.getY(), 40, 20);// make weapon rotate according to direction and move weapon to where it should be here
		// set rotate origin in hereeeee
		if (player.weaponName.equals("sword"))
		{
			originX = 0;
			originY = 15;
			moveX = 30;
			moveY = 0;
			this.setWidth(47);
			this.setHeight(67);
		}
		else if (player.weaponName.equals("spear"))
		{
			originX = 0;
			originY = 9;
			moveX = 30;
			moveY = 7;
			this.setWidth(110);
			this.setHeight(20);
		}
		else if (player.weaponName.equals("axe"))
		{
			originX = 0;
			originY = 10;
			moveX = 30;
			moveY = 5;
			this.setWidth(30);
			this.setHeight(42);
		} 
		if (player.direction.equals("up"))
		{
			if (player.faceLeft && this.getHeight() > 0)
			{
				this.setHeight(-1*this.getHeight());
				if(player.weaponName.equals("axe"))this.setX(this.getX()-15);
				else if(player.weaponName.equals("spear"))this.setX(this.getX()-15);
				else if(player.weaponName.equals("sword"))this.setX(this.getX()-25);
			}
			batch.draw(currentAnim.getKeyFrame(time), this.getX()+moveX, this.getY()+moveY, originX, originY, this.getWidth(), this.getHeight(), 1, 1, 90);
			
		}
		else if (player.direction.equals("down"))
		{
			if (player.faceLeft && this.getHeight() > 0)
			{
				this.setHeight(-1*this.getHeight());
				if(player.weaponName.equals("axe"))this.setX(this.getX()-15);
				else if(player.weaponName.equals("spear"))this.setX(this.getX()-15);
				else if(player.weaponName.equals("sword"))this.setX(this.getX()-25);
			}
			batch.draw(currentAnim.getKeyFrame(time), this.getX()+moveX, this.getY()+moveY, originX, originY, -this.getWidth(), this.getHeight(), 1, 1, 90);
			
		}
		else if (player.direction.equals("left"))
		{
			batch.draw(currentAnim.getKeyFrame(time), this.getX()+moveX, this.getY()+moveY, originX, originY, -this.getWidth(), this.getHeight(), 1, 1, 0);
		}
		else if (player.direction.equals("right"))
		{
			batch.draw(currentAnim.getKeyFrame(time), this.getX()+moveX, this.getY()+moveY, originX, originY, this.getWidth(), this.getHeight(), 1, 1, 0);
		}
		if (currentAnim == animation)
		{
			if (currentAnim.isAnimationFinished(time))
			{
				currentAnim = normalAnim;
			}
		}
	}
	public void updateWeaponPosition()
	{
		this.setX(player.getX());// change weapon position later
		this.setY(player.getY());
	}
	public void updateWeaponAnimation()
	{
		// change texture of weapon
		this.textureAtlas = player.weaponAtlas;
		this.animation = player.weaponAnim;
		// this will be where memory leak happens
		normalAnim = new Animation<TextureRegion>(0.5f, textureAtlas.findRegions("0001"));
		chargingAnim = new Animation<TextureRegion>(0.5f, textureAtlas.findRegion("attack", 1));
		attackingAnim = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("attack"));
	}
}
