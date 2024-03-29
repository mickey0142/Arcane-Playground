package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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

	static Sound currentBreakSound;
	static TextureAtlas wall1 = new TextureAtlas("picture/normalwall.atlas");
	static Sound wallBreakSound = Gdx.audio.newSound(Gdx.files.internal("audio/wallbreak.ogg"));
	static TextureAtlas wall2 = new TextureAtlas("picture/normalwall2.atlas");
	static Sound wallBreakSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/grassbreak.ogg"));
	static TextureAtlas wall3 = new TextureAtlas("picture/normalwall3.atlas");
	static Sound wallBreakSound3 = Gdx.audio.newSound(Gdx.files.internal("audio/icebreak.ogg"));
	static TextureAtlas wall4 = new TextureAtlas("picture/normalwall4.atlas");
	static Sound wallBreakSound4 = Gdx.audio.newSound(Gdx.files.internal("audio/lavabreak.ogg"));

	public NormalWall()
	{

	}

	public NormalWall(float x, float y, float width, float height)
	{
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
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
				currentBreakSound.play(0.5f);
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
