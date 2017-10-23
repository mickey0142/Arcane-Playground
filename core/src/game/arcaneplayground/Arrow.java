package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Arrow extends GameObject{
	float speedX = 0, speedY = 0, maxSpeed = 0;
	float time = 0;
	static TextureAtlas arrowTextureAtlas = new TextureAtlas(Gdx.files.internal("arrowweapon.atlas"));
	static Animation<TextureRegion> arrowAnim = new Animation<TextureRegion>(0.2f, arrowTextureAtlas.getRegions());
	public Arrow()
	{
		this.setVisible(false);
	}
	public Arrow(float x, float y)
	{
		this.setX(x);
		this.setY(y);
		this.setWidth(30);
		this.setHeight(10);
		this.setVisible(false);
		this.hitbox = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void draw(Batch batch, float alpha)
	{
		time += Gdx.graphics.getDeltaTime();
		this.setX(this.getX()+speedX);
		this.setY(this.getY()+speedY);
		updateHitbox();
		//System.out.println(this.getX() + " " + this.getY());
		if (speedY == maxSpeed)//up
		{
			batch.draw(arrowAnim.getKeyFrame(time, true), this.getX()-15, this.getY(), this.getWidth()/2, this.getHeight()/2, this.getWidth(), this.getHeight(), 1, 1, 90);
		}
		else if (speedY == -maxSpeed)//down
		{
			batch.draw(arrowAnim.getKeyFrame(time, true), this.getX()-15, this.getY(), this.getWidth()/2, this.getHeight()/2, -this.getWidth(), this.getHeight(), 1, 1, 90);
		}
		else if (speedX == -maxSpeed)//left
		{
			batch.draw(arrowAnim.getKeyFrame(time, true), this.getX(), this.getY(), this.getWidth()/2, this.getHeight()/2, -this.getWidth(), this.getHeight(), 1, 1, 0);
		}
		else if (speedX == maxSpeed)//right
		{
			batch.draw(arrowAnim.getKeyFrame(time, true), this.getX(), this.getY(), this.getWidth()/2, this.getHeight()/2, this.getWidth(), this.getHeight(), 1, 1, 0);
		}
	}
	public void setArrow(float x, float y, String direction, int weaponLV)
	{
		this.setX(x);
		this.setY(y);
		if (weaponLV == 1)
		{
			maxSpeed = 3;
		}
		else if (weaponLV == 2)
		{
			maxSpeed = 5;
		}
		else if (weaponLV == 3)
		{
			maxSpeed = 7;
		}
		if (direction.equals("up"))
		{
			speedX = 0;
			speedY = maxSpeed;
			
		}
		else if (direction.equals("down"))
		{
			speedX = 0;
			speedY = -maxSpeed;
		}
		else if (direction.equals("left"))
		{
			speedX = -maxSpeed;
			speedY = 0;
		}
		else if (direction.equals("right"))
		{
			speedX = maxSpeed;
			speedY = 0;
		}
		updateHitbox();
	}
	
	public void updateHitbox()
	{
		hitbox.setX(this.getX());
		hitbox.setY(this.getY());
		hitbox.setWidth(this.getWidth());
		hitbox.setHeight(this.getHeight());
		if (speedY != 0)
		{
			hitbox.setX(this.getX()-15);
			hitbox.setY(this.getY()-15);
			hitbox.setWidth(this.getHeight());
			hitbox.setHeight(this.getWidth());
		}
		if (speedX < 0)
		{
			hitbox.setX(this.getX()-this.getWidth());
		}
		if (speedY < 0)
		{
			hitbox.setY(this.getY()-this.getWidth());
		}
	}
}
