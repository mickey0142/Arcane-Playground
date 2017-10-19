package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class UI extends Actor{
	Texture img;
	Animation<TextureRegion> currentAnim;
	boolean animation = false;
	boolean setColor = false;
	float red;
	float green;
	float blue;
	float time;
	public UI()
	{
		
	}
	public UI(String path, float x, float y, float width, float height)
	{
		img = new Texture(Gdx.files.internal(path));
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		setColor = false;
	}
	public UI(String path, float x, float y, float width, float height, boolean setColor)
	{
		img = new Texture(Gdx.files.internal(path));
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.setColor = setColor;
	}
	public void draw(Batch batch, float alpha)
	{
		if (setColor)
		{
			batch.setColor(red, green, blue, 1);
		}
		time += Gdx.graphics.getDeltaTime();
		if (animation)
		{
			batch.draw(currentAnim.getKeyFrame(time), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
		else
		{
			batch.draw(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
//		batch.setColor(Color.WHITE);
	}
	public void setAnimation(TextureAtlas textureatlas)
	{
		currentAnim = new Animation<TextureRegion>(0.5f, textureatlas.getRegions());
	}
	public void setAnimation(TextureAtlas textureatlas, String regionName)
	{
		currentAnim = new Animation<TextureRegion>(0.5f, textureatlas.findRegions(regionName));
	}
}
