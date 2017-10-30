package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Balloon extends Actor{
	float time;
	PlayerCharacter player;
	Animation<TextureRegion> currentAnim;
	
	static TextureAtlas balloon1 = new TextureAtlas(Gdx.files.internal("character1.atlas"));//change name later
	static Animation<TextureRegion> balloon1Anim = new Animation<TextureRegion>(0.5f, balloon1.getRegions());
	public Balloon()
	{
		this.setX(-100);
		this.setY(-100);
		this.setWidth(30);
		this.setHeight(30);
		this.setVisible(false);
	}
	public Balloon(PlayerCharacter player)
	{
		this.player = player;
		this.setX(-100);
		this.setY(-100);
		this.setWidth(30);
		this.setHeight(30);
		this.setVisible(false);
	}
	public void draw(Batch batch, float alpha)
	{
		this.setX(player.getX()+15);
		this.setY(player.getY()+70);
		time += Gdx.graphics.getDeltaTime();
		batch.draw(currentAnim.getKeyFrame(time, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		if (currentAnim.isAnimationFinished(time))
		{
			this.setVisible(false);
			time = 0;
		}
	}
	public void runAnimation(String animationName)
	{
		if (animationName.equals("1"))
		{
			currentAnim = balloon1Anim;
			this.setVisible(true);
		}
	}
}
