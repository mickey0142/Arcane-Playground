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
	
	// all weapon here
	static TextureAtlas fist = new TextureAtlas(Gdx.files.internal("fistanim.atlas"));
	static Animation<TextureRegion> fistAnim = new Animation<TextureRegion>(0.5f, fist.getRegions());
//	static TextureAtlas sword = new TextureAtlas();
//	static Animation<TextureRegion> swordAnum = new Animation();
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
		normalAnim = new Animation<TextureRegion>(0.5f, textureAtlas.findRegions("001"));
		chargingAnim = new Animation<TextureRegion>(0.5f, textureAtlas.findRegions("002"));
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
		if (player.direction.equals("up"))
		{
			batch.draw(currentAnim.getKeyFrame(time), this.getX(), this.getY(), 40/2, 20/2, 40, 20, 1, 1, 90);
		}
		else if (player.direction.equals("down"))
		{
			batch.draw(currentAnim.getKeyFrame(time), this.getX(), this.getY()+40, 40/2, 20/2, -40, 20, 1, 1, 90);
		}
		else if (player.direction.equals("left"))
		{
			batch.draw(currentAnim.getKeyFrame(time), this.getX()+20, this.getY(), 40/2, 20/2, -40, 20, 1, 1, 0);
		}
		else if (player.direction.equals("right"))
		{
			batch.draw(currentAnim.getKeyFrame(time), this.getX(), this.getY(), 40/2, 20/2, 40, 20, 1, 1, 0);
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
	public void updateNewWeapon()
	{
		// change texture of weapon
		this.textureAtlas = player.weaponAtlas;
		this.animation = player.weaponAnim;
	}
}
