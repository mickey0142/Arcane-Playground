package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class WaterTrap extends GameObject{
	static Texture waterTexture = new Texture(Gdx.files.internal("watertrap.png"));
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
		batch.draw(waterTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
