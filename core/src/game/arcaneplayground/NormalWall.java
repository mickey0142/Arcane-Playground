package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class NormalWall extends GameObject{
	double hp = 5;
	boolean dropItem = false;
	static ItemDrop itemdrop[];
	public NormalWall()
	{
		
	}
	public NormalWall(String path, float x, float y, float width, float height, boolean solid)
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
		if (hp <= 0)
		{
			if (!dropItem)
			{
				spawnItem(itemdrop);
				dropItem = true;
			}
			this.setX(-1000);
			this.setY(-1000);
			this.hitbox.setX(this.getX());
			this.hitbox.setY(this.getY());
		}
		batch.draw(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void spawnItem(ItemDrop itemdrop[])
	{
		itemdrop[ItemDrop.dropCount].setValue(this.getX(), this.getY());
	}
}
