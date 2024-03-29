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
	Animation<TextureRegion> currentAnim;

	static TextureAtlas punchAtlas = new TextureAtlas(Gdx.files.internal("picture/punch.atlas"));
	static Animation<TextureRegion> punchAnimation = new Animation<TextureRegion>(0.2f, punchAtlas.getRegions());
	static TextureAtlas swordEffectAtlas = new TextureAtlas(Gdx.files.internal("picture/swordeffect.atlas"));
	static Animation<TextureRegion> swordAnimation = new Animation<TextureRegion>(0.1f, swordEffectAtlas.getRegions());
	static TextureAtlas spearEffectAtlas = new TextureAtlas(Gdx.files.internal("picture/speareffect.atlas"));
	static Animation<TextureRegion> spearAnimation = new Animation<TextureRegion>(0.1f, spearEffectAtlas.getRegions());

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
			if (direction.equals("up"))
			{
				if (player.faceLeft && this.getWidth() > 0)
				{
					this.setWidth(-1*this.getWidth());
					this.setX(this.getX() - this.getWidth());
				}
				batch.draw(currentAnim.getKeyFrame(time), this.getX()+this.getWidth(), this.getY(), 0, 0, this.getHeight(), this.getWidth(), 1, 1, 90);
			}
			else if (direction.equals("down"))
			{
				if (player.faceLeft && this.getWidth() > 0)
				{
					this.setWidth(-1*this.getWidth());
					this.setX(this.getX() - this.getWidth());
				}
				batch.draw(currentAnim.getKeyFrame(time), this.getX()+this.getWidth(), this.getY()+this.getHeight(), 0, 0, -this.getHeight(), this.getWidth(), 1, 1, 90);
			}
			else if (direction.equals("left"))
			{
				batch.draw(currentAnim.getKeyFrame(time), this.getX()+this.getWidth(), this.getY(), 0, 0, -this.getWidth(), this.getHeight(), 1, 1, 0);
			}
			else if (direction.equals("right"))
			{
				batch.draw(currentAnim.getKeyFrame(time), this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), 1, 1, 0);
			}
			if (currentAnim.isAnimationFinished(time))
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
	
	public void updateCurrentAnim(Animation<TextureRegion> anim)
	{
		this.currentAnim = anim;
	}
}
