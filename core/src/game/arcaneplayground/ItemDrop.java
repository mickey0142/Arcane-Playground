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
	float attackWidth[] = new float[3], attackHeight[] = new float[3], attackChargeTime[] = new float[3], attackCooldown[] = new float[3], attack[] = new float[3];
	TextureAtlas effectAtlas, weaponAtlas[] = new TextureAtlas[3];
	Animation<TextureRegion> effectAnimation, weaponAnimation, weaponLV2Animation, weaponLV3Animation;
	String weaponName;
	
	static int dropCount = 0;
	static Texture swordDropTexture = new Texture(Gdx.files.internal("swordweapon.png"));// static and final ! all value and texture here
	static final float SWORD_ATTACK_WIDTH = 40;
	static final float SWORD_ATTACK_HEIGHT = 40;
	static final float SWORD_CHARGE_TIME = 1;
	static final float SWORD_ATTACK_COOLDOWN = 1.5f;
	static final float SWORD_ATTACK = 5;
	static final float SWORDLV2_ATTACK_WIDTH = 40;
	static final float SWORDLV2_ATTACK_HEIGHT = 40;
	static final float SWORDLV2_CHARGE_TIME = 1;
	static final float SWORDLV2_ATTACK_COOLDOWN = 1;
	static final float SWORDLV2_ATTACK = 10;
	static final float SWORDLV3_ATTACK_WIDTH = 40;
	static final float SWORDLV3_ATTACK_HEIGHT = 40;
	static final float SWORDLV3_CHARGE_TIME = 0.5f;
	static final float SWORDLV3_ATTACK_COOLDOWN = 1;
	static final float SWORDLV3_ATTACK = 15;
	
	static final float SPEAR_ATTACK_WIDTH = 40;
	static final float SPEAR_ATTACK_HEIGHT = 40;
	static final float SPEAR_CHARGE_TIME = 1;
	static final float SPEAR_ATTACK_COOLDOWN = 1.5f;
	static final float SPEAR_ATTACK = 5;
	static final float SPEARLV2_ATTACK_WIDTH = 80;
	static final float SPEARLV2_ATTACK_HEIGHT = 40;
	static final float SPEARLV2_CHARGE_TIME = 1;
	static final float SPEARLV2_ATTACK_COOLDOWN = 1;
	static final float SPEARLV2_ATTACK = 10;
	static final float SPEARLV3_ATTACK_WIDTH = 80;
	static final float SPEARLV3_ATTACK_HEIGHT = 40;
	static final float SPEARLV3_CHARGE_TIME = 0.5f;
	static final float SPEARLV3_ATTACK_COOLDOWN = 1;
	static final float SPEARLV3_ATTACK = 15;
	
	static final float AXE_ATTACK_WIDTH = 40;
	static final float AXE_ATTACK_HEIGHT = 40;
	static final float AXE_CHARGE_TIME = 1;
	static final float AXE_ATTACK_COOLDOWN = 1.5f;
	static final float AXE_ATTACK = 5;
	static final float AXELV2_ATTACK_WIDTH = 40;
	static final float AXELV2_ATTACK_HEIGHT = 80;
	static final float AXELV2_CHARGE_TIME = 1;
	static final float AXELV2_ATTACK_COOLDOWN = 1;
	static final float AXELV2_ATTACK = 10;
	static final float AXELV3_ATTACK_WIDTH = 40;
	static final float AXELV3_ATTACK_HEIGHT = 80;
	static final float AXELV3_CHARGE_TIME = 0.5f;
	static final float AXELV3_ATTACK_COOLDOWN = 1;
	static final float AXELV3_ATTACK = 15;
	
	static final float BOW_ATTACK_WIDTH = 40;
	static final float BOW_ATTACK_HEIGHT = 40;
	static final float BOW_CHARGE_TIME = 1;
	static final float BOW_ATTACK_COOLDOWN = 1.5f;
	static final float BOW_ATTACK = 5;
	static final float BOWLV2_ATTACK_WIDTH = 40;
	static final float BOWLV2_ATTACK_HEIGHT = 40;
	static final float BOWLV2_CHARGE_TIME = 1;
	static final float BOWLV2_ATTACK_COOLDOWN = 1;
	static final float BOWLV2_ATTACK = 10;
	static final float BOWLV3_ATTACK_WIDTH = 40;
	static final float BOWLV3_ATTACK_HEIGHT = 40;
	static final float BOWLV3_CHARGE_TIME = 0.5f;
	static final float BOWLV3_ATTACK_COOLDOWN = 1;
	static final float BOWLV3_ATTACK = 15;

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
		if (num >= 1 && num <= 20)
		{
			System.out.println("sword drop");
			this.img = swordDropTexture;
			this.weaponName = "sword";
			//lv1
			attackWidth[0] = SWORD_ATTACK_WIDTH;
			attackHeight[0] = SWORD_ATTACK_HEIGHT;
			attackCooldown[0] = SWORD_ATTACK_COOLDOWN;
			attackChargeTime[0] = SWORD_CHARGE_TIME;
			attack[0] = SWORD_ATTACK;
			effectAtlas = EffectRenderer.swordEffectAtlas;
			effectAnimation = EffectRenderer.swordAnimation;
			weaponAtlas[0] = PlayerWeapon.sword;
			weaponAnimation = PlayerWeapon.swordAnim;
			//lv2
			attackWidth[1] = SWORDLV2_ATTACK_WIDTH;
			attackHeight[1] = SWORDLV2_ATTACK_HEIGHT;
			attackCooldown[1] = SWORDLV2_ATTACK_COOLDOWN;
			attackChargeTime[1] = SWORDLV2_CHARGE_TIME;
			attack[1] = SWORDLV2_ATTACK;
			weaponAtlas[1] = PlayerWeapon.swordLV2;
			weaponLV2Animation = PlayerWeapon.swordLV2Anim;
			//lv3
			attackWidth[2] = SWORDLV3_ATTACK_WIDTH;
			attackHeight[2] = SWORDLV3_ATTACK_HEIGHT;
			attackCooldown[2] = SWORDLV3_ATTACK_COOLDOWN;
			attackChargeTime[2] = SWORDLV3_CHARGE_TIME;
			attack[2] = SWORDLV3_ATTACK;
			weaponAtlas[2] = PlayerWeapon.swordLV3;
			weaponLV3Animation = PlayerWeapon.swordLV3Anim;
		}
		else if (num >= 21 && num <= 40)//36 -> 70
		{
			System.out.println("spear drop");
			this.img = swordDropTexture;
			this.weaponName = "spear";
			//lv1
			attackWidth[0] = SPEAR_ATTACK_WIDTH;
			attackHeight[0] = SPEAR_ATTACK_HEIGHT;
			attackCooldown[0] = SPEAR_ATTACK_COOLDOWN;
			attackChargeTime[0] = SPEAR_CHARGE_TIME;
			attack[0] = SPEAR_ATTACK;
			effectAtlas = EffectRenderer.swordEffectAtlas;
			effectAnimation = EffectRenderer.swordAnimation;
			weaponAtlas[0] = PlayerWeapon.spear;
			weaponAnimation = PlayerWeapon.spearAnim;
			//lv2
			attackWidth[1] = SPEARLV2_ATTACK_WIDTH;
			attackHeight[1] = SPEARLV2_ATTACK_HEIGHT;
			attackCooldown[1] = SPEARLV2_ATTACK_COOLDOWN;
			attackChargeTime[1] = SPEARLV2_CHARGE_TIME;
			attack[1] = SPEARLV2_ATTACK;
			weaponAtlas[1] = PlayerWeapon.spearLV2;
			weaponLV2Animation = PlayerWeapon.spearLV2Anim;
			//lv3
			attackWidth[2] = SPEARLV3_ATTACK_WIDTH;
			attackHeight[2] = SPEARLV3_ATTACK_HEIGHT;
			attackCooldown[2] = SPEARLV3_ATTACK_COOLDOWN;
			attackChargeTime[2] = SPEARLV3_CHARGE_TIME;
			attack[2] = SPEARLV3_ATTACK;
			weaponAtlas[2] = PlayerWeapon.spearLV3;
			weaponLV3Animation = PlayerWeapon.spearLV3Anim;
		}
		else if (num >= 41 && num <= 60)
		{
			System.out.println("axe drop");
			this.img = swordDropTexture;
			this.weaponName = "axe";
			//lv1
			attackWidth[0] = AXE_ATTACK_WIDTH;
			attackHeight[0] = AXE_ATTACK_HEIGHT;
			attackCooldown[0] = AXE_ATTACK_COOLDOWN;
			attackChargeTime[0] = AXE_CHARGE_TIME;
			attack[0] = AXE_ATTACK;
			effectAtlas = EffectRenderer.swordEffectAtlas;
			effectAnimation = EffectRenderer.swordAnimation;
			weaponAtlas[0] = PlayerWeapon.axe;
			weaponAnimation = PlayerWeapon.axeAnim;
			//lv2
			attackWidth[1] = AXELV2_ATTACK_WIDTH;
			attackHeight[1] = AXELV2_ATTACK_HEIGHT;
			attackCooldown[1] = AXELV2_ATTACK_COOLDOWN;
			attackChargeTime[1] = AXELV2_CHARGE_TIME;
			attack[1] = AXELV2_ATTACK;
			weaponAtlas[1] = PlayerWeapon.axeLV2;
			weaponLV2Animation = PlayerWeapon.axeLV2Anim;
			//lv3
			attackWidth[2] = AXELV3_ATTACK_WIDTH;
			attackHeight[2] = AXELV3_ATTACK_HEIGHT;
			attackCooldown[2] = AXELV3_ATTACK_COOLDOWN;
			attackChargeTime[2] = AXELV3_CHARGE_TIME;
			attack[2] = AXELV3_ATTACK;
			weaponAtlas[2] = PlayerWeapon.axeLV3;
			weaponLV3Animation = PlayerWeapon.axeLV3Anim;
		}
		else if (num > 61 && num <= 80)
		{
			System.out.println("bow drop");
			this.img = swordDropTexture;
			this.weaponName = "bow";
			//lv1
			attackWidth[0] = BOW_ATTACK_WIDTH;
			attackHeight[0] = BOW_ATTACK_HEIGHT;
			attackCooldown[0] = BOW_ATTACK_COOLDOWN;
			attackChargeTime[0] = BOW_CHARGE_TIME;
			attack[0] = BOW_ATTACK;
			effectAtlas = EffectRenderer.punchAtlas;
			effectAnimation = EffectRenderer.punchAnimation;
			weaponAtlas[0] = PlayerWeapon.bow;
			weaponAnimation = PlayerWeapon.bowAnim;
			//lv2
			attackWidth[1] = BOWLV2_ATTACK_WIDTH;
			attackHeight[1] = BOWLV2_ATTACK_HEIGHT;
			attackCooldown[1] = BOWLV2_ATTACK_COOLDOWN;
			attackChargeTime[1] = BOWLV2_CHARGE_TIME;
			attack[1] = BOWLV2_ATTACK;
//			weaponAtlas[1] = PlayerWeapon.bowLV2;
//			weaponLV2Animation = PlayerWeapon.bowLV2Anim;
			weaponAtlas[1] = PlayerWeapon.bow;
			weaponLV2Animation = PlayerWeapon.bowAnim;
			//lv3
			attackWidth[2] = BOWLV3_ATTACK_WIDTH;
			attackHeight[2] = BOWLV3_ATTACK_HEIGHT;
			attackCooldown[2] = BOWLV3_ATTACK_COOLDOWN;
			attackChargeTime[2] = BOWLV3_CHARGE_TIME;
			attack[2] = BOWLV3_ATTACK;
//			weaponAtlas[2] = PlayerWeapon.bowLV3;
//			weaponLV3Animation = PlayerWeapon.bowLV3Anim;
			weaponAtlas[2] = PlayerWeapon.bow;
			weaponLV3Animation = PlayerWeapon.bowAnim;
		}
		if (num <= 80)
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
