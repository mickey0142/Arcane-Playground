package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ItemDrop extends GameObject{
	boolean dropped = false;
	float attackWidth[] = new float[3], attackHeight[] = new float[3], attack[] = new float[3];
	TextureAtlas effectAtlas, weaponAtlas;
	Animation<TextureRegion> effectAnimation, weaponAnimation;
	String weaponName;
	
	static int dropCount = 0;
	static Texture swordDropTexture = new Texture(Gdx.files.internal("swordweapon.png"));// static and final ! all value and texture here
	static final float SWORD_ATTACK_WIDTH = 40;
	static final float SWORD_ATTACK_HEIGHT = 40;
	static final float SWORD_ATTACK = 5;
	static final float SWORDLV2_ATTACK_WIDTH = 40;
	static final float SWORDLV2_ATTACK_HEIGHT = 80;
	static final float SWORDLV2_ATTACK = 5;
	public ItemDrop()
	{
		this.setVisible(dropped);
		this.setWidth(30);
		this.setHeight(30);
		hitbox = new Rectangle(-1000, -1000, 30, 30);
	}
	public void draw(Batch batch, float alpha)
	{
		batch.draw(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		if (!dropped)
		{
			this.setVisible(dropped);
		}
	}
	public void setValue(float locationX, float locationY)
	{
		// set x y width height attackhitbox value here and set dropped and visible to true +1 dropcount here -1 dropcount when pick item
		int num = (int)(Math.random()*1);// random 0 to multiplier-1
		if (num == 0)
		{
			this.img = swordDropTexture;
			this.weaponName = "sword";
			//lv1
			attackWidth[0] = SWORD_ATTACK_WIDTH;
			attackHeight[0] = SWORD_ATTACK_HEIGHT;
			attack[0] = SWORD_ATTACK;
			effectAtlas = EffectRenderer.swordAtlas;
			effectAnimation = EffectRenderer.swordAnimation;
			weaponAtlas = PlayerWeapon.sword;
			weaponAnimation = PlayerWeapon.swordAnim;
			//lv2
			attackWidth[1] = SWORDLV2_ATTACK_WIDTH;
			attackHeight[1] = SWORDLV2_ATTACK_HEIGHT;
			attack[1] = SWORDLV2_ATTACK;
			//lv3
			
			
			
			// change player weapon somehow...?
			dropCount += 1;
			dropped = true;
			this.setX(locationX);
			this.setY(locationY);
			this.setVisible(dropped);
			hitbox.setX(this.getX());
			hitbox.setY(this.getY());
		}
		else
		{
			System.out.println("boom");
		}
	}
}
