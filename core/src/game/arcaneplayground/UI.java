package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class UI extends Actor{
	Texture img;
	boolean setColor = false;
	float red;
	float green;
	float blue;
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
		batch.draw(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		batch.setColor(Color.WHITE);
	}
	public void setHPBarColor(float hp, float hpMax)
	{
		if (hp > hpMax/2)
		{
			green = 1;
			red = 2*((hpMax-hp)/100);
		}
		else
		{
			red = 1;
			green = 2*hp/100;
		}
	}
}
