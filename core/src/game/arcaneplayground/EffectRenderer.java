package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EffectRenderer extends Actor{
	PlayerCharacter player;
	boolean check = false;
	float time;
	String direction;
	
	// keep all attack effect here
	static TextureAtlas punchAtlas = new TextureAtlas(Gdx.files.internal("punch.atlas"));
	static Animation<TextureRegion> punchAnimation = new Animation<TextureRegion>(0.2f, punchAtlas.getRegions());
	static TextureAtlas swordAtlas = new TextureAtlas(Gdx.files.internal("swordeffect.atlas"));
	static Animation<TextureRegion> swordAnimation = new Animation<TextureRegion>(0.1f, swordAtlas.getRegions());
	// keep all attack effect here
	
	public EffectRenderer()
	{
		
	}
	public EffectRenderer(PlayerCharacter player)
	{
		this.player = player;
	}
	public void draw(Batch batch, float alpha)
	{
		if (check)
		{
			time += Gdx.graphics.getDeltaTime();
			//batch.draw(player.attackEffectAnim.getKeyFrame(time), this.getX(), this.getY(), this.getWidth(), this.getHeight());
			if (direction.equals("up"))
			{
				batch.draw(player.attackEffectAnim.getKeyFrame(time), this.getX()+this.getWidth(), this.getY(), 0, 0, this.getHeight(), this.getWidth(), 1, 1, 90);
			}
			else if (direction.equals("down"))
			{
				batch.draw(player.attackEffectAnim.getKeyFrame(time), this.getX()+this.getWidth(), this.getY()+this.getHeight(), 0, 0, -this.getHeight(), this.getWidth(), 1, 1, 90);
			}
			else if (direction.equals("left"))
			{
				batch.draw(player.attackEffectAnim.getKeyFrame(time), this.getX()+this.getWidth(), this.getY(), 0, 0, -this.getWidth(), this.getHeight(), 1, 1, 0);
//				batch.draw(player.attackEffectAnim.getKeyFrame(time), this.getX()+this.getWidth(), this.getY(), this.getWidth()/2, this.getHeight()/2, -this.getWidth(), this.getHeight(), 1, 1, 0);
			}
			else if (direction.equals("right"))
			{
				batch.draw(player.attackEffectAnim.getKeyFrame(time), this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), 1, 1, 0);
//				batch.draw(player.attackEffectAnim.getKeyFrame(time), this.getX(), this.getY(), this.getWidth()/2, this.getHeight()/2, this.getWidth(), this.getHeight(), 1, 1, 0);
			}
			if (player.attackEffectAnim.isAnimationFinished(time))
			{
				check = false;
			}
		}
	}
	public void setValue(float x, float y, float width, float height, String direction)
	{
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.direction = direction;
	}
}
