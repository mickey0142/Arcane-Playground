package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class WaterTrap extends GameObject{
	float time = 0;
	static TextureAtlas waterTexture = new TextureAtlas(Gdx.files.internal("watertrap.atlas"));
	static Animation<TextureRegion> waterAnim = new Animation<TextureRegion>(1f, waterTexture.getRegions());
	public WaterTrap()
	{
		
	}
	public WaterTrap(float x, float y)
	{
		this.setX(x);
		this.setY(y);
		this.setWidth(50);
		this.setHeight(50);
		hitbox = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void draw(Batch batch, float alpha)
	{
		time += Gdx.graphics.getDeltaTime();
		batch.draw(waterAnim.getKeyFrame(time, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
