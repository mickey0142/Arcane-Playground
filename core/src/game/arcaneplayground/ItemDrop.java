package game.arcaneplayground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ItemDrop extends GameObject{
	boolean dropped = false;
	float attackWidth[] = new float[3], attackHeight[] = new float[3], attackChargeTime[] = new float[3], attack[] = new float[3];
	TextureAtlas effectAtlas, weaponAtlas[] = new TextureAtlas[3];
	Animation<TextureRegion> effectAnimation, weaponAnimation, weaponLV2Animation, weaponLV3Animation;
	String weaponName;
	Sound weaponSound;
	String dropType, powerUpName;
	
	static int dropCount = 0;
	static Texture swordDropTexture = new Texture(Gdx.files.internal("sworddrop.png"));
	static Texture spearDropTexture = new Texture(Gdx.files.internal("speardrop.png"));
	static Texture axeDropTexture = new Texture(Gdx.files.internal("axedrop.png"));
	static Texture bowDropTexture = new Texture(Gdx.files.internal("bowdrop.png"));
	static Texture lifeDropTexture = new Texture(Gdx.files.internal("life.png"));
	static Texture shoeDropTexture = new Texture(Gdx.files.internal("shoe.png"));
	static final float SWORD_ATTACK_WIDTH = 40;
	static final float SWORD_ATTACK_HEIGHT = 40;
	static final float SWORD_CHARGE_TIME = 1;
	static final float SWORD_ATTACK = 5;
	static final float SWORDLV2_ATTACK_WIDTH = 40;
	static final float SWORDLV2_ATTACK_HEIGHT = 40;
	static final float SWORDLV2_CHARGE_TIME = 0.8f;
	static final float SWORDLV2_ATTACK = 10;
	static final float SWORDLV3_ATTACK_WIDTH = 40;
	static final float SWORDLV3_ATTACK_HEIGHT = 40;
	static final float SWORDLV3_CHARGE_TIME = 0.5f;
	static final float SWORDLV3_ATTACK = 15;
	
	static final float SPEAR_ATTACK_WIDTH = 40;
	static final float SPEAR_ATTACK_HEIGHT = 40;
	static final float SPEAR_CHARGE_TIME = 2f;
	static final float SPEAR_ATTACK = 6;
	static final float SPEARLV2_ATTACK_WIDTH = 100;
	static final float SPEARLV2_ATTACK_HEIGHT = 40;
	static final float SPEARLV2_CHARGE_TIME = 1.5f;
	static final float SPEARLV2_ATTACK = 12;
	static final float SPEARLV3_ATTACK_WIDTH = 100;
	static final float SPEARLV3_ATTACK_HEIGHT = 40;
	static final float SPEARLV3_CHARGE_TIME = 1f;
	static final float SPEARLV3_ATTACK = 18;
	
	static final float AXE_ATTACK_WIDTH = 40;
	static final float AXE_ATTACK_HEIGHT = 40;
	static final float AXE_CHARGE_TIME = 2.5f;
	static final float AXE_ATTACK = 8;
	static final float AXELV2_ATTACK_WIDTH = 40;
	static final float AXELV2_ATTACK_HEIGHT = 80;
	static final float AXELV2_CHARGE_TIME = 2;
	static final float AXELV2_ATTACK = 16;
	static final float AXELV3_ATTACK_WIDTH = 40;
	static final float AXELV3_ATTACK_HEIGHT = 80;
	static final float AXELV3_CHARGE_TIME = 1.5f;
	static final float AXELV3_ATTACK = 24;
	
	static final float BOW_ATTACK_WIDTH = 40;
	static final float BOW_ATTACK_HEIGHT = 40;
	static final float BOW_CHARGE_TIME = 1.5f;
	static final float BOW_ATTACK = 5;
	static final float BOWLV2_ATTACK_WIDTH = 40;
	static final float BOWLV2_ATTACK_HEIGHT = 40;
	static final float BOWLV2_CHARGE_TIME = 1.2f;
	static final float BOWLV2_ATTACK = 10;
	static final float BOWLV3_ATTACK_WIDTH = 40;
	static final float BOWLV3_ATTACK_HEIGHT = 40;
	static final float BOWLV3_CHARGE_TIME = 0.8f;
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
		int drop = (int)(Math.random()*100+1);
		if (drop <= 40)
		{
			dropType = "weapon";
			int num = (int)(Math.random()*100+1);// random 1 to multiplier
			if (num >= 1 && num <= 25)
			{
				this.img = swordDropTexture;
				this.weaponName = "sword";
				//lv1
				attackWidth[0] = SWORD_ATTACK_WIDTH;
				attackHeight[0] = SWORD_ATTACK_HEIGHT;
				attackChargeTime[0] = SWORD_CHARGE_TIME;
				attack[0] = SWORD_ATTACK;
				effectAtlas = EffectRenderer.swordEffectAtlas;
				effectAnimation = EffectRenderer.swordAnimation;
				weaponAtlas[0] = PlayerWeapon.sword;
				weaponAnimation = PlayerWeapon.swordAnim;
				//lv2
				attackWidth[1] = SWORDLV2_ATTACK_WIDTH;
				attackHeight[1] = SWORDLV2_ATTACK_HEIGHT;
				attackChargeTime[1] = SWORDLV2_CHARGE_TIME;
				attack[1] = SWORDLV2_ATTACK;
				effectAtlas = EffectRenderer.swordEffectAtlas;
				effectAnimation = EffectRenderer.swordAnimation;
				weaponAtlas[1] = PlayerWeapon.swordLV2;
				weaponLV2Animation = PlayerWeapon.swordLV2Anim;
				//lv3
				attackWidth[2] = SWORDLV3_ATTACK_WIDTH;
				attackHeight[2] = SWORDLV3_ATTACK_HEIGHT;
				attackChargeTime[2] = SWORDLV3_CHARGE_TIME;
				attack[2] = SWORDLV3_ATTACK;
				effectAtlas = EffectRenderer.swordEffectAtlas;
				effectAnimation = EffectRenderer.swordAnimation;
				weaponAtlas[2] = PlayerWeapon.swordLV3;
				weaponLV3Animation = PlayerWeapon.swordLV3Anim;
				weaponSound = PlayerWeapon.swordSound;
			}
			else if (num >= 26 && num <= 50)
			{
				this.img = spearDropTexture;
				this.weaponName = "spear";
				//lv1
				attackWidth[0] = SPEAR_ATTACK_WIDTH;
				attackHeight[0] = SPEAR_ATTACK_HEIGHT;
				attackChargeTime[0] = SPEAR_CHARGE_TIME;
				attack[0] = SPEAR_ATTACK;
				effectAtlas = EffectRenderer.spearEffectAtlas;
				effectAnimation = EffectRenderer.spearAnimation;
				weaponAtlas[0] = PlayerWeapon.spear;
				weaponAnimation = PlayerWeapon.spearAnim;
				//lv2
				attackWidth[1] = SPEARLV2_ATTACK_WIDTH;
				attackHeight[1] = SPEARLV2_ATTACK_HEIGHT;
				attackChargeTime[1] = SPEARLV2_CHARGE_TIME;
				attack[1] = SPEARLV2_ATTACK;
				effectAtlas = EffectRenderer.spearEffectAtlas;
				effectAnimation = EffectRenderer.spearAnimation;
				weaponAtlas[1] = PlayerWeapon.spearLV2;
				weaponLV2Animation = PlayerWeapon.spearLV2Anim;
				//lv3
				attackWidth[2] = SPEARLV3_ATTACK_WIDTH;
				attackHeight[2] = SPEARLV3_ATTACK_HEIGHT;
				attackChargeTime[2] = SPEARLV3_CHARGE_TIME;
				attack[2] = SPEARLV3_ATTACK;
				effectAtlas = EffectRenderer.spearEffectAtlas;
				effectAnimation = EffectRenderer.spearAnimation;
				weaponAtlas[2] = PlayerWeapon.spearLV3;
				weaponLV3Animation = PlayerWeapon.spearLV3Anim;
				weaponSound = PlayerWeapon.spearSound;
			}
			else if (num >= 51 && num <= 75)
			{
				this.img = axeDropTexture;
				this.weaponName = "axe";
				//lv1
				attackWidth[0] = AXE_ATTACK_WIDTH;
				attackHeight[0] = AXE_ATTACK_HEIGHT;
				attackChargeTime[0] = AXE_CHARGE_TIME;
				attack[0] = AXE_ATTACK;
				effectAtlas = EffectRenderer.swordEffectAtlas;
				effectAnimation = EffectRenderer.swordAnimation;
				weaponAtlas[0] = PlayerWeapon.axe;
				weaponAnimation = PlayerWeapon.axeAnim;
				//lv2
				attackWidth[1] = AXELV2_ATTACK_WIDTH;
				attackHeight[1] = AXELV2_ATTACK_HEIGHT;
				attackChargeTime[1] = AXELV2_CHARGE_TIME;
				attack[1] = AXELV2_ATTACK;
				effectAtlas = EffectRenderer.swordEffectAtlas;
				effectAnimation = EffectRenderer.swordAnimation;
				weaponAtlas[1] = PlayerWeapon.axeLV2;
				weaponLV2Animation = PlayerWeapon.axeLV2Anim;
				//lv3
				attackWidth[2] = AXELV3_ATTACK_WIDTH;
				attackHeight[2] = AXELV3_ATTACK_HEIGHT;
				attackChargeTime[2] = AXELV3_CHARGE_TIME;
				attack[2] = AXELV3_ATTACK;
				effectAtlas = EffectRenderer.swordEffectAtlas;
				effectAnimation = EffectRenderer.swordAnimation;
				weaponAtlas[2] = PlayerWeapon.axeLV3;
				weaponLV3Animation = PlayerWeapon.axeLV3Anim;
				weaponSound = PlayerWeapon.axeSound;
			}
			else if (num >= 76 && num <= 100)
			{
				this.img = bowDropTexture;
				this.weaponName = "bow";
				//lv1
				attackWidth[0] = BOW_ATTACK_WIDTH;
				attackHeight[0] = BOW_ATTACK_HEIGHT;
				attackChargeTime[0] = BOW_CHARGE_TIME;
				attack[0] = BOW_ATTACK;
				effectAtlas = EffectRenderer.punchAtlas;
				effectAnimation = EffectRenderer.punchAnimation;
				weaponAtlas[0] = PlayerWeapon.bow;
				weaponAnimation = PlayerWeapon.bowAnim;
				//lv2
				attackWidth[1] = BOWLV2_ATTACK_WIDTH;
				attackHeight[1] = BOWLV2_ATTACK_HEIGHT;
				attackChargeTime[1] = BOWLV2_CHARGE_TIME;
				attack[1] = BOWLV2_ATTACK;
				effectAtlas = EffectRenderer.punchAtlas;
				effectAnimation = EffectRenderer.punchAnimation;
				weaponAtlas[1] = PlayerWeapon.bowLV2;
				weaponLV2Animation = PlayerWeapon.bowLV2Anim;
				//lv3
				attackWidth[2] = BOWLV3_ATTACK_WIDTH;
				attackHeight[2] = BOWLV3_ATTACK_HEIGHT;
				attackChargeTime[2] = BOWLV3_CHARGE_TIME;
				attack[2] = BOWLV3_ATTACK;
				effectAtlas = EffectRenderer.punchAtlas;
				effectAnimation = EffectRenderer.punchAnimation;
				weaponAtlas[2] = PlayerWeapon.bowLV3;
				weaponLV3Animation = PlayerWeapon.bowLV3Anim;
				weaponSound = PlayerWeapon.bowSound;
			}
			dropCount += 1;
			dropped = true;
			this.setX(locationX+10);
			this.setY(locationY+10);
			this.setVisible(dropped);
			hitbox.setX(this.getX());
			hitbox.setY(this.getY());
		}
		else if (drop >= 41 && drop <= 50)
		{
			dropType = "powerup";
			int num = (int)(Math.random()*100+1);// random 1 to multiplier
			if (num >= 1 && num <= 25)
			{
				this.img = lifeDropTexture;
				powerUpName = "life";
			}
			else if (num >= 26 && num <= 100)
			{
				this.img = shoeDropTexture;
				powerUpName = "shoe";
			}
			dropCount += 1;
			dropped = true;
			this.setX(locationX+10);
			this.setY(locationY+10);
			this.setVisible(dropped);
			hitbox.setX(this.getX());
			hitbox.setY(this.getY());
		}
	}
}
