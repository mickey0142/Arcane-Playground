package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class WaterTrap extends GameObject{
	float time = 0;
	
	static Animation<TextureRegion> currentAnim;
	static TextureAtlas waterTexture = new TextureAtlas(Gdx.files.internal("picture/watertrap.atlas"));
	static Animation<TextureRegion> waterAnim = new Animation<TextureRegion>(0.5f, waterTexture.getRegions());
	static TextureAtlas waterTexture2 = new TextureAtlas(Gdx.files.internal("picture/watertrap2.atlas"));
	static Animation<TextureRegion> waterAnim2 = new Animation<TextureRegion>(0.5f, waterTexture2.getRegions());
	static TextureAtlas waterTexture3 = new TextureAtlas(Gdx.files.internal("picture/watertrap3.atlas"));
	static Animation<TextureRegion> waterAnim3 = new Animation<TextureRegion>(0.5f, waterTexture3.getRegions());
	static TextureAtlas waterTexture4 = new TextureAtlas(Gdx.files.internal("picture/watertrap4.atlas"));
	static Animation<TextureRegion> waterAnim4 = new Animation<TextureRegion>(0.5f, waterTexture4.getRegions());
	static Sound waterTrapSound = Gdx.audio.newSound(Gdx.files.internal("audio/slow.ogg"));
	
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
		batch.draw(currentAnim.getKeyFrame(time, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
