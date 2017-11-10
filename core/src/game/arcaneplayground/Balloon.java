package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Balloon extends Actor{
	float time;
	boolean loop = false;
	boolean onPlayer = true;
	PlayerCharacter player;
	Animation<TextureRegion> currentAnim;
	
	static TextureAtlas trapBalloon = new TextureAtlas(Gdx.files.internal("trapballoon.atlas"));
	static Animation<TextureRegion> trapBalloonAnim = new Animation<TextureRegion>(0.3f, trapBalloon.getRegions());
	static TextureAtlas winnerBalloon = new TextureAtlas(Gdx.files.internal("winnerballoon.atlas"));
	static Animation<TextureRegion> winnerBalloonAnim = new Animation<TextureRegion>(0.8f, winnerBalloon.getRegions());
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
		if (onPlayer)
		{
			this.setX(player.getX()+15);
			this.setY(player.getY()+70);
		}
		time += Gdx.graphics.getDeltaTime();
		if (loop)
		{
			batch.draw(currentAnim.getKeyFrame(time, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
		else
		{
			batch.draw(currentAnim.getKeyFrame(time, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
			if (currentAnim.isAnimationFinished(time))
			{
				this.setVisible(false);
				time = 0;
			}
		}
	}
	public void runAnimation(String animationName)
	{
		if (animationName.equals("trap"))
		{
			currentAnim = trapBalloonAnim;
			this.setVisible(true);
		}
		else if (animationName.equals("winner"))
		{
			currentAnim = winnerBalloonAnim;
			this.setVisible(true);
		}
	}
}
