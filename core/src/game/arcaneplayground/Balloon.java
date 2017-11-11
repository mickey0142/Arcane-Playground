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
	static TextureAtlas skill1Balloon = new TextureAtlas(Gdx.files.internal("skill1balloon.atlas"));
	static Animation<TextureRegion> skill1BalloonAnim = new Animation<TextureRegion>(1f, skill1Balloon.getRegions());
	static TextureAtlas skill2Balloon = new TextureAtlas(Gdx.files.internal("skill2balloon.atlas"));
	static Animation<TextureRegion> skill2BalloonAnim = new Animation<TextureRegion>(1f, skill2Balloon.getRegions());
	static TextureAtlas skill3Balloon = new TextureAtlas(Gdx.files.internal("skill3balloon.atlas"));
	static Animation<TextureRegion> skill3BalloonAnim = new Animation<TextureRegion>(1f, skill3Balloon.getRegions());
	static TextureAtlas skill4Balloon = new TextureAtlas(Gdx.files.internal("skill4balloon.atlas"));
	static Animation<TextureRegion> skill4BalloonAnim = new Animation<TextureRegion>(1f, skill4Balloon.getRegions());
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
		else if (animationName.equals("skill1"))
		{
			currentAnim = skill1BalloonAnim;
			this.setVisible(true);
		}
		else if (animationName.equals("skill2"))
		{
			currentAnim = skill2BalloonAnim;
			this.setVisible(true);
		}
		else if (animationName.equals("skill3"))
		{
			currentAnim = skill3BalloonAnim;
			this.setVisible(true);
		}
		else if (animationName.equals("skill4"))
		{
			currentAnim = skill4BalloonAnim;
			this.setVisible(true);
		}
	}
}
