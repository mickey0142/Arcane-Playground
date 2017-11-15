package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SpikeTrap extends GameObject{
	static TextureAtlas spikeTexture = new TextureAtlas(Gdx.files.internal("picture/spiketrap.atlas"));
	static Animation<TextureRegion> spikeAnim = new Animation<TextureRegion>(1f, spikeTexture.getRegions());
	float time;
	boolean active = false;
	public SpikeTrap()
	{
		
	}
	public SpikeTrap(float x, float y)
	{
		this.setX(x);
		this.setY(y);
		this.setWidth(50);
		this.setHeight(50);
		hitbox = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void draw(Batch batch, float color)
	{
		time += Gdx.graphics.getDeltaTime();
		if (time >= 1)
		{
			active = true;
		}
		if (time >= 2)
		{
			active = false;
			time = 0;
		}
		batch.draw(spikeAnim.getKeyFrame(time, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
