package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObject extends Actor{
	Texture img;
	Rectangle hitbox;
	boolean solid = false;
	public GameObject() 
	{
		
	}
	public GameObject(String path, float x, float y, float width, float height, boolean solid)
	{
		img = new Texture(Gdx.files.internal(path));
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.solid = solid;
		hitbox = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void draw(Batch batch, float alpha)
	{
		batch.draw(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void dispose()
	{
		img.dispose();
	}
}
