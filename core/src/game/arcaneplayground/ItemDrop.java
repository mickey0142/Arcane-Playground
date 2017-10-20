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
	float attackWidth[] = new float[3], attackHeight[] = new float[3], attackChargeTime[] = new float[3], attackCooldown[] = new float[3];
	TextureAtlas effectAtlas, weaponAtlas;
	Animation<TextureRegion> effectAnimation, weaponAnimation;
	String weaponName;
	
	static int dropCount = 0;
	static Texture swordDropTexture = new Texture(Gdx.files.internal("swordweapon.png"));// static and final ! all value and texture here
	static final float SWORD_ATTACK_WIDTH = 40;
	static final float SWORD_ATTACK_HEIGHT = 40;
	static final float SWORD_CHARGE_TIME = 1;
	static final float SWORD_ATTACK_COOLDOWN = 1.5f;
	static final float SWORDLV2_ATTACK_WIDTH = 40;
	static final float SWORDLV2_ATTACK_HEIGHT = 40;
	static final float SWORDLV2_CHARGE_TIME = 1;
	static final float SWORDLV2_ATTACK_COOLDOWN = 1;
	static final float SWORDLV3_ATTACK_WIDTH = 40;
	static final float SWORDLV3_ATTACK_HEIGHT = 40;
	static final float SWORDLV3_CHARGE_TIME = 0.5f;
	static final float SWORDLV3_ATTACK_COOLDOWN = 1;
	
	static final float SPEAR_ATTACK_WIDTH = 40;
	static final float SPEAR_ATTACK_HEIGHT = 40;
	static final float SPEAR_CHARGE_TIME = 1;
	static final float SPEAR_ATTACK_COOLDOWN = 1.5f;
	static final float SPEARLV2_ATTACK_WIDTH = 80;
	static final float SPEARLV2_ATTACK_HEIGHT = 40;
	static final float SPEARLV2_CHARGE_TIME = 1;
	static final float SPEARLV2_ATTACK_COOLDOWN = 1;
	static final float SPEARLV3_ATTACK_WIDTH = 80;
	static final float SPEARLV3_ATTACK_HEIGHT = 40;
	static final float SPEARLV3_CHARGE_TIME = 0.5f;
	static final float SPEARLV3_ATTACK_COOLDOWN = 1;
	
	static final float AXE_ATTACK_WIDTH = 40;
	static final float AXE_ATTACK_HEIGHT = 80;
	static final float AXE_CHARGE_TIME = 1;
	static final float AXE_ATTACK_COOLDOWN = 1.5f;
	static final float AXELV2_ATTACK_WIDTH = 40;
	static final float AXELV2_ATTACK_HEIGHT = 80;
	static final float AXELV2_CHARGE_TIME = 1;
	static final float AXELV2_ATTACK_COOLDOWN = 1;
	static final float AXELV3_ATTACK_WIDTH = 40;
	static final float AXELV3_ATTACK_HEIGHT = 80;
	static final float AXELV3_CHARGE_TIME = 0.5f;
	static final float AXELV3_ATTACK_COOLDOWN = 1;

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
		int num = (int)(Math.random()*100+1);// random 1 to multiplier
		System.out.println(num);
		if (num >= 1 && num <= 35)
		{
			this.img = swordDropTexture;
			this.weaponName = "sword";
			//lv1
			attackWidth[0] = SWORD_ATTACK_WIDTH;
			attackHeight[0] = SWORD_ATTACK_HEIGHT;
			attackCooldown[0] = SWORD_ATTACK_COOLDOWN;
			attackChargeTime[0] = SWORD_CHARGE_TIME;
			effectAtlas = EffectRenderer.swordAtlas;
			effectAnimation = EffectRenderer.swordAnimation;
			weaponAtlas = PlayerWeapon.sword;
			weaponAnimation = PlayerWeapon.swordAnim;
			//lv2
			attackWidth[1] = SWORDLV2_ATTACK_WIDTH;
			attackHeight[1] = SWORDLV2_ATTACK_HEIGHT;
			attackCooldown[1] = SWORDLV2_ATTACK_COOLDOWN;
			attackChargeTime[1] = SWORDLV2_CHARGE_TIME;
			//lv3
			attackWidth[2] = SWORDLV3_ATTACK_WIDTH;
			attackHeight[2] = SWORDLV3_ATTACK_HEIGHT;
			attackCooldown[2] = SWORDLV3_ATTACK_COOLDOWN;
			attackChargeTime[2] = SWORDLV3_CHARGE_TIME;
		}
		else if (num >= 36 && num <= 70)
		{
			this.img = swordDropTexture;
			this.weaponName = "spear";
			//lv1
			attackWidth[0] = SPEAR_ATTACK_WIDTH;
			attackHeight[0] = SPEAR_ATTACK_HEIGHT;
			attackCooldown[0] = SPEAR_ATTACK_COOLDOWN;
			attackChargeTime[0] = SPEAR_CHARGE_TIME;
			effectAtlas = EffectRenderer.swordAtlas;
			effectAnimation = EffectRenderer.swordAnimation;
			weaponAtlas = PlayerWeapon.spear;
			weaponAnimation = PlayerWeapon.spearAnim;
			//lv2
			attackWidth[1] = SPEARLV2_ATTACK_WIDTH;
			attackHeight[1] = SPEARLV2_ATTACK_HEIGHT;
			attackCooldown[1] = SPEARLV2_ATTACK_COOLDOWN;
			attackChargeTime[1] = SPEARLV2_CHARGE_TIME;
			//lv3
			attackWidth[2] = SPEARLV3_ATTACK_WIDTH;
			attackHeight[2] = SPEARLV3_ATTACK_HEIGHT;
			attackCooldown[2] = SPEARLV3_ATTACK_COOLDOWN;
			attackChargeTime[2] = SPEARLV3_CHARGE_TIME;
		}
		else if (num >= 71 && num <= 95)
		{
			this.img = swordDropTexture;
			this.weaponName = "axe";
			//lv1
			attackWidth[0] = AXE_ATTACK_WIDTH;
			attackHeight[0] = AXE_ATTACK_HEIGHT;
			attackCooldown[0] = AXE_ATTACK_COOLDOWN;
			attackChargeTime[0] = AXE_CHARGE_TIME;
			effectAtlas = EffectRenderer.swordAtlas;
			effectAnimation = EffectRenderer.swordAnimation;
			weaponAtlas = PlayerWeapon.axe;
			weaponAnimation = PlayerWeapon.axeAnim;
			//lv2
			attackWidth[1] = AXELV2_ATTACK_WIDTH;
			attackHeight[1] = AXELV2_ATTACK_HEIGHT;
			attackCooldown[1] = AXELV2_ATTACK_COOLDOWN;
			attackChargeTime[1] = AXELV2_CHARGE_TIME;
			//lv3
			attackWidth[2] = AXELV3_ATTACK_WIDTH;
			attackHeight[2] = AXELV3_ATTACK_HEIGHT;
			attackCooldown[2] = AXELV3_ATTACK_COOLDOWN;
			attackChargeTime[2] = AXELV3_CHARGE_TIME;
		}
		if (num <= 95)
		{
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
			System.out.println("nothing");
		}
	}
}
