package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class UnbreakableWall extends GameObject{
	static Texture wall1 = new Texture(Gdx.files.internal("blockcastle.png"));
	static Texture wall2 = new Texture(Gdx.files.internal("blockwood.png"));
	static Texture wall3 = new Texture(Gdx.files.internal("blockice.png"));
	static Texture wall4 = new Texture(Gdx.files.internal("blocklava.png"));
	public UnbreakableWall()
	{
		
	}
	public UnbreakableWall(String path, float x, float y, float width, float height)
	{
		img = new Texture(Gdx.files.internal(path));
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		hitbox = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void draw(Batch batch, float alpha)
	{
		batch.draw(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
