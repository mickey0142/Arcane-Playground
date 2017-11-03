package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class NormalWall extends GameObject{
	double hp = 3;
	boolean dropItem = false;
	TextureRegion currentRegion;
	float delay = 2;
	static ItemDrop itemdrop[];
	static TextureAtlas wallTexture;
	static TextureRegion hp3, hp2, hp1, hp0;

	static TextureAtlas wall1 = new TextureAtlas("normalwall.atlas");

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
			currentRegion = NormalWall.hp0;
			if (!dropItem)
			{
				spawnItem(itemdrop);
				dropItem = true;
				// add wall destroy sound here
			}
			this.hitbox.setX(-1000);
			this.hitbox.setY(-1000);
			delay -= Gdx.graphics.getDeltaTime();
			if (delay <= 0)
			{
				this.setX(-1000);
				this.setY(-1000);
				
				delay = 2;
			}
		}
		else if (hp == 3)
		{
			currentRegion = NormalWall.hp3;
		}
		else if (hp == 2)
		{
			currentRegion = NormalWall.hp2;
		}
		else if (hp == 1)
		{
			currentRegion = NormalWall.hp1;
		}
		batch.draw(currentRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	public void spawnItem(ItemDrop itemdrop[])
	{
		if (itemdrop[ItemDrop.dropCount].dropped)
		{
			ItemDrop.dropCount += 1;
		}
		itemdrop[ItemDrop.dropCount].setValue(this.getX(), this.getY());
	}
}
