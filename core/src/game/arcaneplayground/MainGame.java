package game.arcaneplayground;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class MainGame extends ApplicationAdapter implements InputProcessor, ControllerListener{
	SpriteBatch batch;
	Texture img;
	Vector2 mousePositionScreen = new Vector2();
	Vector2 mousePositionStage = new Vector2();
	String screen = "menu";
	String back = "menu";
	PlayerCharacter player[] = new PlayerCharacter[4];
	Stage menu;
	int cursorPosition = 1;
	UI menuBackground;
	UI menuArrow;
	UI menuButtonStart;
	UI menuButtonSetting;
	UI menuButtonHowto;
	UI menuButtonExit;
	UI menuPointerStart, menuPointerSetting, menuPointerHowTo, menuPointerExit;
	
	Stage character;
	int characterIndex[] = new int[4];// change 4 to number of character texture here
	UI characterBackground;
	UI playerCharacterSelect[];
	
	Stage game;
	float delay = 3; // this is how much time before switch to end stage reset this in end stage
	GameObject playGround;
	UI gameBackground;
	UnbreakableWall walls[];
	NormalWall normalWalls[];
	EffectRenderer attackEffectRenderer[] = new EffectRenderer[4];
	PlayerWeapon playerWeaponRenderer[] = new PlayerWeapon[4];
	GameObject checkBlock[] = new GameObject[4];
	ItemDrop itemDrop[];
	UI playerChargeBar[] = new UI[4];
	UI playerHPBar[] = new UI[4];
	UI playerArmorBar[] = new UI[4];
	Arrow playerArrow[];
	boolean arrowCharged[] = {false, false, false, false};
	UI playerSprite[];
	UI weaponSprite[];
	BitmapFont font10;
	SpikeTrap spikeTrap[];
	Balloon balloon[];
	
	Stage howTo;
	
	Stage end;
	
	Stage pause;
	int pauseCursorPosition = 1;
	UI pauseBackground;
	UI pauseArrow;
	
	Stage setting;
	UI settingBackground;
	UI playerSetting[] = new UI[4];
	boolean changeControl = false;
	Texture gray;
	int playerNumber = -1;
	String controlName = "";
	BitmapFont font12;
	TextureAtlas controlType;
	Animation<TextureRegion> controlTypeKeyboard;
	Animation<TextureRegion> controlTypeController1;
	Animation<TextureRegion> controlTypeController2;
	Animation<TextureRegion> controlTypeAnim;
	UI playerControlType[];
	int playerCount;
	
	Music menuMusic, gameMusic, endMusic;
	Sound damagedSound, hpDownSound, deadSound, trapHitSound, parryArrowSound, healSound, collectSound, cursorSound, cancelSound, confirmSound, victorySound;

	@Override
	public void create () {
		batch = new SpriteBatch();

		menu = new Stage(new FitViewport(1350, 750));
		character = new Stage(new FitViewport(1350, 750));
		game = new Stage(new FitViewport(1350, 750));
		end = new Stage(new FitViewport(1350, 750));
		pause = new Stage(new FitViewport(1350, 750));
		setting = new Stage(new FitViewport(1350, 750));
		howTo = new Stage(new FitViewport(1350, 750));

		screen = "menu";
		
		damagedSound = Gdx.audio.newSound(Gdx.files.internal("audio/damaged.ogg"));
		hpDownSound = Gdx.audio.newSound(Gdx.files.internal("audio/hpdown.ogg"));
		deadSound = Gdx.audio.newSound(Gdx.files.internal("audio/dead.ogg"));
		trapHitSound = Gdx.audio.newSound(Gdx.files.internal("audio/trap.ogg"));
		parryArrowSound = Gdx.audio.newSound(Gdx.files.internal("audio/parryarrow.ogg"));
		healSound = Gdx.audio.newSound(Gdx.files.internal("audio/heal.ogg"));
		collectSound = Gdx.audio.newSound(Gdx.files.internal("audio/collect.ogg"));
		cursorSound = Gdx.audio.newSound(Gdx.files.internal("audio/cursor.ogg"));
		cancelSound = Gdx.audio.newSound(Gdx.files.internal("audio/cancel.ogg"));
		confirmSound = Gdx.audio.newSound(Gdx.files.internal("audio/confirm.ogg"));
		victorySound = Gdx.audio.newSound(Gdx.files.internal("audio/victory.ogg"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = Color.BLACK;
		font12 = generator.generateFont(parameter); // font size 12 pixels
		parameter.size = 24;
		font10 = generator.generateFont(parameter);
		generator.dispose();
		
		itemDrop = new ItemDrop[126];
		normalWalls = new NormalWall[126];
		spikeTrap = new SpikeTrap[4];
		for (int i = 0; i < 126; i++)
		{
			itemDrop[i] = new ItemDrop();
		}
		NormalWall.itemdrop = itemDrop;

		createInMenuStage();
		createInCharacterStage();
		createInGameStage();
		createInEndStage();
		createInPauseStage();
		createInSettingStage();

		Gdx.input.setInputProcessor(this);
		Controllers.addListener(this);
	}

	public void createInMenuStage()
	{
		menuBackground = new UI("whitebox.png", 0, 0, 1350, 750);
		TextureAtlas temp = new TextureAtlas(Gdx.files.internal("menubackground.atlas"));
		menuBackground.animation = true;
		menuBackground.animationLoop = true;
		menuBackground.setAnimation(temp);
		menuBackground.currentAnim.setFrameDuration(0.2f);
		menuArrow = new UI("pointer.png", 1150, 305, 32, 32);
		menuButtonStart = new UI("whitebox.png", 800, 280, 285, 70);
		menuButtonSetting = new UI("whitebox.png", 200, 200, 50, 50);
		menuButtonHowto = new UI("whitebox.png", 200, 200, 50, 50);
		menuButtonExit = new UI("whitebox.png", 166, 70, 445, 100);
		menuPointerStart = new UI("whitebox.png", 1155, 305, 50, 50);
		menuPointerSetting = new UI("whitebox.png", 635, 435, 50, 50);
		menuPointerHowTo = new UI("whitebox.png", 635, 235, 50, 50);
		menuPointerExit = new UI("whitebox.png", 635, 95, 50, 50);
		menu.addActor(menuBackground);
		menu.addActor(menuArrow);
		menu.addActor(menuButtonStart);
		menu.addActor(menuButtonExit);
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menumusic.ogg"));
		menuMusic.setLooping(true);
	}

	public void createInCharacterStage()
	{
		weaponSprite = new UI[4];
		checkBlock[0] = new GameObject("box3.png", 0, 0, 100, 100, false);// remove box3.png and add null and don't add this actor to stage i think that can avoid nullpointerexception because this.draw don't get call
		checkBlock[1] = new GameObject("box3.png", 0, 0, 100, 100, false);
		checkBlock[2] = new GameObject("box3.png", 0, 0, 100, 100, false);
		checkBlock[3] = new GameObject("box3.png", 0, 0, 100, 100, false);
		playerHPBar[0] = new UI("heart.png", 100, 690, 150, 40);
		playerHPBar[1] = new UI("heart.png", 400, 690, 150, 40);
		playerHPBar[2] = new UI("heart.png", 700, 690, 150, 40);
		playerHPBar[3] = new UI("heart.png", 1000, 690, 150, 40);
		playerArmorBar[0] = new UI ("box3.png", 100, 660, 10, 15);
		playerArmorBar[1] = new UI ("box3.png", 400, 660, 10, 15);
		playerArmorBar[2] = new UI ("box3.png", 700, 660, 10, 15);
		playerArmorBar[3] = new UI ("box3.png", 1000, 660, 10, 15);
		playerChargeBar[0] = new UI("whitebox.png", 0, 0, 60, 10, true);
		playerChargeBar[1] = new UI("whitebox.png", 0, 0, 60, 10, true);
		playerChargeBar[2] = new UI("whitebox.png", 0, 0, 60, 10, true);
		playerChargeBar[3] = new UI("whitebox.png", 0, 0, 60, 10, true);
		weaponSprite[0] = new UI ("gray.png", 260, 680, 50, 50);
		weaponSprite[1] = new UI ("gray.png", 560, 680, 50, 50);
		weaponSprite[2] = new UI ("gray.png", 860, 680, 50, 50);
		weaponSprite[3] = new UI ("gray.png", 1160, 680, 50, 50);
		player[0] = new PlayerCharacter(50, 50, 60, 60, Keys.W, Keys.S, Keys.A, Keys.D, Keys.F, Keys.Q, playerHPBar[0], playerArmorBar[0], PlayerWeapon.fist, PlayerWeapon.fistAnim);
		player[1] = new PlayerCharacter(1250, 550, 60, 60, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT, Keys.ALT_RIGHT, playerHPBar[1], playerArmorBar[1], PlayerWeapon.fist, PlayerWeapon.fistAnim);
		player[2] = new PlayerCharacter(50, 550, 60, 60, 26, 26, 26, 26, 26, 26, playerHPBar[2], playerArmorBar[2], PlayerWeapon.fist, PlayerWeapon.fistAnim);
		player[3] = new PlayerCharacter(1250, 50, 60, 60, 26, 26, 26, 26, 26, 26, playerHPBar[3], playerArmorBar[3], PlayerWeapon.fist, PlayerWeapon.fistAnim);

		//temppppp 
		//player[0].weaponName = "axe";
		//player[1].weaponName = "spear";

		playerArrow = new Arrow[4];
		balloon = new Balloon[4];
		for (int i = 0; i < 4; i++)
		{
			attackEffectRenderer[i] = new EffectRenderer(player[i]);
			attackEffectRenderer[i].setValue(player[i].attackHitbox.getX(), player[i].attackHitbox.getY(), player[i].attackHitbox.getWidth(), player[i].attackHitbox.getHeight(), player[i].direction);
			playerWeaponRenderer[i] = new PlayerWeapon(player[i]);
			player[i].setPlayerAttackEffectRenderer(attackEffectRenderer[i]);
			player[i].setPlayerWeaponRenderer(playerWeaponRenderer[i]);
			player[i].setChargeBar(playerChargeBar[i]);
			player[i].setCheckBlockObject(checkBlock[i]);
			player[i].updateHitbox();
			player[i].updateCheckBlockPosition(player[i].hitbox.getX(), player[i].hitbox.getY(), player[i].hitbox.getWidth(), player[i].hitbox.getHeight());
			playerArrow[i] = new Arrow(-100, -100);
			player[i].setArrowRenderer(playerArrow[i]);
			player[i].setIngame(false);
			balloon[i] = new Balloon(player[i]);
			player[i].setBalloon(balloon[i]);
		}	
		
		// ui in stage
		characterBackground = new UI("characterbackground.jpg", 0, 0, 1350, 750);
		playerCharacterSelect =  new UI[4];
		playerCharacterSelect[0] = new UI("character1.png", 200, 600, 60, 60);
		playerCharacterSelect[0].animation = true;
		playerCharacterSelect[0].setAnimation(PlayerCharacter.character1, "0001");
		playerCharacterSelect[1] = new UI("character1.png", 400, 600, 60, 60);
		playerCharacterSelect[1].animation = true;
		playerCharacterSelect[1].setAnimation(PlayerCharacter.character1, "0001");
		playerCharacterSelect[2] = new UI("character1.png", 600, 600, 60, 60);
		playerCharacterSelect[2].animation = true;
		playerCharacterSelect[2].setAnimation(PlayerCharacter.character1, "0001");
		playerCharacterSelect[3] = new UI("character1.png", 800, 600, 60, 60);
		playerCharacterSelect[3].animation = true;
		playerCharacterSelect[3].setAnimation(PlayerCharacter.character1, "0001");

		character.addActor(characterBackground);
		character.addActor(playerCharacterSelect[0]);
		playerCharacterSelect[0].setVisible(false);
		character.addActor(playerCharacterSelect[1]);
		playerCharacterSelect[1].setVisible(false);
		character.addActor(playerCharacterSelect[2]);
		playerCharacterSelect[2].setVisible(false);
		character.addActor(playerCharacterSelect[3]);
		playerCharacterSelect[3].setVisible(false);
	}

	public void createInGameStage()
	{
		gameBackground = new UI("gamebackground.jpg", 0, 0, 1350, 750, false);
		playGround = new GameObject("playground.png", 25, 0, 1300, 650, false);
		gameMusic  = Gdx.audio.newMusic(Gdx.files.internal("audio/gamemusic.ogg"));
		gameMusic.setLooping(true);
		
		playerSprite = new UI[4];
		playerSprite[0] = new UI("whitebox.png", 30, 660, 60, 60);
		playerSprite[0].animation = true;
		playerSprite[0].setAnimation(PlayerCharacter.character1, "0001");
		playerSprite[1] = new UI("whitebox.png", 330, 660, 60, 60);
		playerSprite[1].animation = true;
		playerSprite[1].setAnimation(PlayerCharacter.character1, "0001");
		playerSprite[2] = new UI("whitebox.png", 630, 660, 60, 60);
		playerSprite[2].animation = true;
		playerSprite[2].setAnimation(PlayerCharacter.character1, "0001");
		playerSprite[3] = new UI("whitebox.png", 930, 660, 60, 60);
		playerSprite[3].animation = true;
		playerSprite[3].setAnimation(PlayerCharacter.character1, "0001");
		
		for (int i = 0; i < 4; i++)
		{
			spikeTrap[i] = new SpikeTrap(100, 100);
		}
		// add all actor for game stage in here. adding order should be background playground itemdrop wall player playerweapon playerattackeffect
		game.addActor(gameBackground);
		game.addActor(playGround);

		game.addActor(playerHPBar[0]);
		game.addActor(playerHPBar[1]);
		game.addActor(playerHPBar[2]);
		game.addActor(playerHPBar[3]);
		game.addActor(playerArmorBar[0]);
		game.addActor(playerArmorBar[1]);
		game.addActor(playerArmorBar[2]);
		game.addActor(playerArmorBar[3]);
		game.addActor(playerSprite[0]);
		game.addActor(playerSprite[1]);
		game.addActor(playerSprite[2]);
		game.addActor(playerSprite[3]);
		game.addActor(weaponSprite[0]);
		game.addActor(weaponSprite[1]);
		game.addActor(weaponSprite[2]);
		game.addActor(weaponSprite[3]);
		
		game.addActor(spikeTrap[0]);
		game.addActor(spikeTrap[1]);
		game.addActor(spikeTrap[2]);
		game.addActor(spikeTrap[3]);

		walls = new UnbreakableWall[136];
		int posX = 0;
		int posY = 0;
		int boxX = 50;
		int boxY = 50;
		for(int i=0;i<=26;i++) {
			walls[i] = new UnbreakableWall("block.png", posX, posY, boxX, boxY, true);
			game.addActor(walls[i]);
			posX += 50;
		}

		posX = 0;
		posY = 600;
		for(int i=27;i<=53;i++) {
			walls[i] = new UnbreakableWall("block.png", posX, posY, boxX, boxY, true);
			game.addActor(walls[i]);
			posX += 50;
		}

		posX = 0;
		posY = 50;
		for(int i=54;i<=64;i++) {
			walls[i] = new UnbreakableWall("block.png", posX, posY, boxX, boxY, true);
			game.addActor(walls[i]);
			posY += 50;
		}

		posX = 1300;
		posY = 50;
		for(int i=65;i<=75;i++) {
			walls[i] = new UnbreakableWall("block.png", posX, posY, boxX, boxY, true);
			game.addActor(walls[i]);
			posY += 50;
		}

		posX = 100;
		posY = 100;
		int num_count = 76;
		for(int i=0;i<5;i++) {
			for(int j=0;j<12;j++) {
				walls[j+num_count] = new UnbreakableWall("block.png", posX, posY, boxX, boxY, true);
				game.addActor(walls[j+num_count]);
				posX += 100;
			}
			posX = 100;
			num_count += 12;
			posY += 100;
		}

		for (ItemDrop item : itemDrop) {
			game.addActor(item);
		}
		
		//change this to loop later
		game.addActor(player[0]);
		game.addActor(player[1]);
		game.addActor(player[2]);
		game.addActor(player[3]);
		game.addActor(playerWeaponRenderer[0]);
		game.addActor(playerWeaponRenderer[1]);
		game.addActor(playerWeaponRenderer[2]);
		game.addActor(playerWeaponRenderer[3]);

		game.addActor(playerArrow[0]);
		game.addActor(playerArrow[1]);
		game.addActor(playerArrow[2]);
		game.addActor(playerArrow[3]);
		
		game.addActor(balloon[0]);
		game.addActor(balloon[1]);
		game.addActor(balloon[2]);
		game.addActor(balloon[3]);
		
		game.addActor(playerChargeBar[0]);
		game.addActor(playerChargeBar[1]);
		game.addActor(playerChargeBar[2]);
		game.addActor(playerChargeBar[3]);

		//temp remove this latertemp remove this latertemp remove this latertemp remove this latertemp remove this latertemp remove this latertemp remove this later
		//game.addActor(checkBlock[0]);
		//game.addActor(checkBlock[1]);

		game.addActor(attackEffectRenderer[0]);
		game.addActor(attackEffectRenderer[1]);
		game.addActor(attackEffectRenderer[2]);
		game.addActor(attackEffectRenderer[3]);
	}

	public void createMap()// maybe add argument in this method and set map and wall texture according to that
	{
		// set textureatlas for block here change wall1 to input
		NormalWall.wallTexture = NormalWall.wall1;
		NormalWall.hp3 = NormalWall.wall1.findRegion("0001");
		NormalWall.hp2 = NormalWall.wall1.findRegion("0002");
		NormalWall.hp1 = NormalWall.wall1.findRegion("0003");
		NormalWall.hp0 = NormalWall.wall1.findRegion("0004");
		int normalWallCount = 0;
		float[] possibleY1 = {50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550};
		float[] possibleY2 = {50, 150, 250, 350, 450, 550};
		float x = 50;
		for (int i = 0; i < 25; i++)
		{
			if (x % 100 == 0)
			{
				int[] y = new Random().ints(0, 6).distinct().limit(4).toArray();
				for (int j = 0; j < 4; j++)
				{
					normalWalls[normalWallCount] = new NormalWall("block2.png", x, possibleY2[y[j]], 50, 50, true);
					normalWalls[normalWallCount].setName("wall");
					game.addActor(normalWalls[normalWallCount]);
					normalWallCount += 1;
				}
			}
			else
			{
				int[] y = new Random().ints(0, 11).distinct().limit(6).toArray();
				if (x == 50 || x == 1250)
				{
					boolean playerLocation = false;
					do {
						playerLocation = false;
						for (int j = 0; j < 6; j++)
						{
							if (y[j] == 0 || y[j] == 10)
							{
								playerLocation = true;
								y = new Random().ints(0, 11).distinct().limit(6).toArray();
								break;
							}
						}
					}
					while (playerLocation);
				}
				for (int j = 0; j < 6; j++)
				{
					normalWalls[normalWallCount] = new NormalWall("block2.png", x, possibleY1[y[j]], 50, 50, true);
					game.addActor(normalWalls[normalWallCount]);
					normalWallCount += 1;
				}
			}
			x += 50;
		}
		// set spiketrap position
		int spikeTrapCount = 0;
		float x2 = 100, y2 = 100;
		float savePositionX[] = {-1, -1, -1, -1};
		float savePositionY[] = {-1, -1, -1, -1};
		GameObject mapChecker = new GameObject("whitebox.png", 100, 100, 40, 40, false);
		boolean skip = false;
		while (spikeTrapCount < 4)
		{
			for (int i = 0; i < 4; i++)
			{
				if (savePositionX[i] == -1)
				{
					break;
				}
				else
				{
					if (savePositionX[i] == x2 && savePositionY[i] == y2)
					{
						skip = true;
						break;
					}
				}
			}
			for (GameObject wall : normalWalls)
			{
				if (checkCollision(mapChecker, wall))
				{
					skip = true;
					break;
				}
			}
			for (GameObject wall : walls)
			{
				if (checkCollision(mapChecker, wall))
				{
					skip = true;
					break;
				}
			}
			if (!skip)
			{
				// set spiketraphere
				if ((int)(Math.random()*20) == 0)
				{
					savePositionX[spikeTrapCount] = x2;
					savePositionY[spikeTrapCount] = y2;
					spikeTrap[spikeTrapCount].setX(x2);
					spikeTrap[spikeTrapCount].setY(y2);
					spikeTrap[spikeTrapCount].updateHitbox();
					spikeTrapCount += 1;
				}
			}
			y2 += 50;
			if (y2 > 500)
			{
				y2 = 100;
				x2 += 50;
			}
			if (x2 > 1200)
			{
				x2 = 100;
			}
			mapChecker.hitbox.setX(x2);
			mapChecker.hitbox.setY(y2);
			skip = false;
		}
		moveNormalWallZIndex();
	}

	public void createInEndStage()
	{
		endMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/endmusic.ogg"));
		endMusic.setLooping(true);
	}

	public void createInPauseStage()
	{
		pauseBackground = new UI("pausebackground.jpg", 0, 0, 1350, 750);
		pauseArrow = new UI("pointer.png", 335, 482, 32, 32);
		
		pause.addActor(pauseBackground);
		pause.addActor(pauseArrow);
	}
	
	public void createInSettingStage()
	{
		settingBackground = new UI("settingbackground.jpg", 0, 0, 1350, 750);
		playerSetting[0] = new UI("playersetting.jpg", 15, 50, 300, 400);
		playerSetting[1] = new UI("playersetting.jpg", 355, 50, 300, 400);
		playerSetting[2] = new UI("playersetting.jpg", 695, 50, 300, 400);
		playerSetting[3] = new UI("playersetting.jpg", 1035, 50, 300, 400);
		gray = new Texture(Gdx.files.internal("gray.png"));
		controlType = new TextureAtlas(Gdx.files.internal("controltype.atlas"));
		controlTypeKeyboard = new Animation<TextureRegion>(1f, controlType.findRegions("0001"));
		controlTypeController1 = new Animation<TextureRegion>(1f, controlType.findRegions("0002"));
		controlTypeController1 = new Animation<TextureRegion>(1f, controlType.findRegions("0003"));
		playerControlType = new UI[4];
		playerControlType[0] = new UI("whitebox.png", 65, 500, 200, 200);
		playerControlType[0].animation = true;
		playerControlType[0].setAnimation(controlType, "0001");
		playerControlType[1] = new UI("whitebox.png", 405, 500, 200, 200);
		playerControlType[1].animation = true;
		playerControlType[1].setAnimation(controlType, "0001");
		playerControlType[2] = new UI("whitebox.png", 745, 500, 200, 200);
		playerControlType[2].animation = true;
		playerControlType[2].setAnimation(controlType, "0001");
		playerControlType[3] = new UI("whitebox.png", 1085, 500, 200, 200);
		playerControlType[3].animation = true;
		playerControlType[3].setAnimation(controlType, "0001");
		
		setting.addActor(settingBackground);
		setting.addActor(playerSetting[0]);
		setting.addActor(playerSetting[1]);
		setting.addActor(playerSetting[2]);
		setting.addActor(playerSetting[3]);
		setting.addActor(playerControlType[0]);
		setting.addActor(playerControlType[1]);
		setting.addActor(playerControlType[2]);
		setting.addActor(playerControlType[3]);
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		menu.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		character.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		end.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		setting.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		pause.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		howTo.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		if (screen.equals("menu"))
		{
			if (!menuMusic.isPlaying())
			{
				menuMusic.play();
				menuMusic.setVolume(0.2f);
			}
			gameMusic.stop();
			endMusic.stop();
			menuStageRender();
		}
		else if (screen.equals("character"))
		{
			characterStageRender();
		}
		else if (screen.equals("game"))
		{
			if (!gameMusic.isPlaying())
			{
				gameMusic.play();
				gameMusic.setVolume(0.3f);
			}
			menuMusic.stop();
			endMusic.stop();
			gameStageRender();
			batch.begin();
			if(player[0].isVisible())font10.draw(batch, "LV." + player[0].weaponLV, weaponSprite[0].getX(), weaponSprite[0].getY()-12);
			if(player[1].isVisible())font10.draw(batch, "LV." + player[1].weaponLV, weaponSprite[1].getX(), weaponSprite[1].getY()-12);
			if(player[2].isVisible())font10.draw(batch, "LV." + player[2].weaponLV, weaponSprite[2].getX(), weaponSprite[2].getY()-12);
			if(player[3].isVisible())font10.draw(batch, "LV." + player[3].weaponLV, weaponSprite[3].getX(), weaponSprite[3].getY()-12);
			batch.end();
			if (playerCount <= 1)
			{
				delay -= Gdx.graphics.getDeltaTime();
			}
			if (delay <= 0)
			{
				screen = "end";
				victorySound.play();
				if (playerCount == 1)
				{
					int numPlayerWin = 0;
					for (PlayerCharacter allPlayer : player)
					{
						if (allPlayer.dead || !allPlayer.isVisible())
						{
							numPlayerWin += 1;
							continue;
						}
						else
						{
							System.out.println("Player : " + numPlayerWin + " Win!");
							break;
						}
					}
				}
				else
				{
					System.out.println("Draw!");
				}
			}
		}
		else if (screen.equals("end"))
		{
			if (!endMusic.isPlaying())
			{
				endMusic.play();
				endMusic.setVolume(0.2f);
			}
			menuMusic.stop();
			gameMusic.stop();
			endStageRender();
		}
		else if (screen.equals("pause"))
		{
			pauseStageRender();
		}
		else if (screen.equals("setting"))
		{
			settingStageRender();
		}
		else if (screen.equals("howto"))
		{
			howToStageRender();
		}
	}

	public void menuStageRender()
	{
		menu.draw();
	}

	public void characterStageRender()
	{
		character.draw();
	}

	public void gameStageRender()
	{
		game.draw();
		int loopCount = 0;
		int weaponCount = 0;
		for (PlayerCharacter allPlayer : player) 
		{
			if (!allPlayer.moving || !allPlayer.isVisible() || allPlayer.dead)
			{
				weaponCount += 1;
				continue;
			}
			if (allPlayer.hitbox.getX() == allPlayer.checkBlock.hitbox.getX() && allPlayer.hitbox.getY() == allPlayer.checkBlock.hitbox.getY())
			{
				allPlayer.moving = false;
				allPlayer.speedUp = 0;
				allPlayer.speedDown = 0;
				allPlayer.speedLeft = 0;
				allPlayer.speedRight = 0;
				allPlayer.speed_x = 0;
				allPlayer.speed_y = 0;
			}
			for (ItemDrop item : itemDrop)
			{
				if (item.isVisible())
				{
					if (checkCollision(allPlayer, item))
					{
						collectSound.play();
						item.dropped = false;
						item.setVisible(item.dropped);
						ItemDrop.dropCount -= 1;
						//set all variable about weapon here
						allPlayer.updateNewWeapon(item);
						item.hitbox.setX(-1000);
						item.hitbox.setY(-1000);
						weaponSprite[weaponCount].img = item.img;
					}
				}
			}
			allPlayer.setX(allPlayer.getX() + allPlayer.speed_x);
			allPlayer.updateHitbox();
			allPlayer.setY(allPlayer.getY() + allPlayer.speed_y);
			allPlayer.updateHitbox();
			weaponCount += 1;
		}
		// checkcollision with trap
		for (PlayerCharacter allPlayer : player)
		{
			if (allPlayer.dead || !allPlayer.isVisible())
			{
				continue;
			}
			for (GameObject trap : spikeTrap)
			{
				if (checkCollision(allPlayer, trap) && ((SpikeTrap)trap).active && allPlayer.trapDelay <= 0)
				{
					trapHitSound.play();
					allPlayer.trapDelay = 2;
					allPlayer.balloon.runAnimation("1");
					if (allPlayer.armor > 0)
					{
						allPlayer.armor -= 50;
						allPlayer.regenDelay = 5f;
						if (allPlayer.armor < 0)
						{
							allPlayer.armor = 0;
						}
					}
					else
					{
						allPlayer.regenDelay = 5f;
						allPlayer.hp -= 1;
						allPlayer.hurt = true;
						allPlayer.armor += 10;
						if (allPlayer.hp <= 0)
						{
							allPlayer.dead = true;
							playerCount -= 1;
							deadSound.play();
						}
					}
				}
			}
		}
		int arrowCount = 0;
		for (PlayerCharacter allPlayer : player) 
		{
			if (!allPlayer.isVisible() || allPlayer.dead)
			{
				continue;
			}
			if (allPlayer.attacking)
			{
				if (allPlayer.weaponName.equals("bow"))
				{
					if (!allPlayer.arrow.isVisible())
					{
						allPlayer.attackSound.play();
						allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
						allPlayer.arrow.setVisible(true);
						if (allPlayer.chargeMax)
						{
							arrowCharged[arrowCount] = true;
						}
						allPlayer.chargeMax = false;
					}
				}
				else
				{
					checkPlayerAttack(allPlayer);
					attackEffectRenderer[loopCount].setValue(allPlayer.attackHitbox.getX(), allPlayer.attackHitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
					attackEffectRenderer[loopCount].check = true;
					attackEffectRenderer[loopCount].time = 0;
				}
				allPlayer.attacking = false;
			}
			// attack check for arrow
			if (allPlayer.arrow.isVisible())
			{
				int arrowCount2 = 0;
				// check collision between arrow
				for (GameObject arrow : playerArrow)
				{
					if (arrow == allPlayer.arrow)
					{
						arrowCount2 += 1;
						continue;
					}
					if (checkCollision(allPlayer.arrow, arrow))// attack effect bug may happen here because of arrowcount arrowcount2 loopcount three of these is confusing 
					{
						PlayerWeapon.fistSound.play();
						allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
						allPlayer.arrow.setVisible(false);
						((Arrow)arrow).setArrow(player[arrowCount2].getX()+25, player[arrowCount2].getY()+20, player[arrowCount2].direction, player[arrowCount2].weaponLV);
						((Arrow)arrow).setVisible(false);
						arrowCharged[arrowCount] = false;
						arrowCharged[arrowCount2] = false;
						if (allPlayer.arrow.speedX != 0)attackEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						else attackEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						attackEffectRenderer[arrowCount].check = true;
						attackEffectRenderer[arrowCount].time = 0;
						if (((Arrow)arrow).speedX != 0)attackEffectRenderer[arrowCount].setValue(((Arrow)arrow).hitbox.getX(), ((Arrow)arrow).hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						else attackEffectRenderer[arrowCount].setValue(((Arrow)arrow).hitbox.getX(), ((Arrow)arrow).hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						attackEffectRenderer[arrowCount].check = true;
						attackEffectRenderer[arrowCount].time = 0;
					}
					arrowCount2 += 1;
				}
				//checkcollision between arrow and unbreakable wall
				for (GameObject wall : walls)
				{
					if (checkCollision(allPlayer.arrow, wall))
					{
						PlayerWeapon.fistSound.play();
						if (allPlayer.arrow.speedX != 0)attackEffectRenderer[loopCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						else attackEffectRenderer[loopCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						attackEffectRenderer[loopCount].check = true;
						attackEffectRenderer[loopCount].time = 0;
						allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
						allPlayer.arrow.setVisible(false);
						arrowCharged[arrowCount] = false;
					}
				}
				//checkcollision between arrow and normal wall
				for (GameObject wall : normalWalls)
				{
					if (checkCollision(allPlayer.arrow, wall))
					{
						if (wall instanceof NormalWall)
						{
							PlayerWeapon.fistSound.play();
							if (arrowCharged[arrowCount])
							{
								((NormalWall) wall).hp -= 3;
							}
							else
							{
								((NormalWall) wall).hp -= 2;
							}
						}
						if (allPlayer.arrow.speedX != 0)attackEffectRenderer[loopCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						else attackEffectRenderer[loopCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						attackEffectRenderer[loopCount].check = true;
						attackEffectRenderer[loopCount].time = 0;
						allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
						allPlayer.arrow.setVisible(false);
						arrowCharged[arrowCount] = false;
					}
				}
				// checkcollision between arrow and itemdrop
				for (ItemDrop item : itemDrop)
				{
					if (checkCollision(allPlayer.arrow, item))
					{
						PlayerWeapon.fistSound.play();
						item.dropped = false;
						item.setVisible(item.dropped);
						ItemDrop.dropCount -= 1;
						item.hitbox.setX(-1000);
						item.hitbox.setY(-1000);
						if (allPlayer.arrow.speedX != 0)attackEffectRenderer[loopCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						else attackEffectRenderer[loopCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						attackEffectRenderer[loopCount].check = true;
						attackEffectRenderer[loopCount].time = 0;
						allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
						allPlayer.arrow.setVisible(false);
						arrowCharged[arrowCount] = false;
					}
				}
				//checkcollision between arrow and player
				for (PlayerCharacter otherPlayer : player)
				{
					if (allPlayer == otherPlayer || !allPlayer.isVisible() || allPlayer.dead)
					{
						continue;
					}
					if (checkCollision(otherPlayer, allPlayer.arrow) && !otherPlayer.hurt)
					{
						PlayerWeapon.fistSound.play();
						otherPlayer.regenDelay = 5f;
						if (otherPlayer.armor <= 0)
						{
							otherPlayer.hp -= 1;
							otherPlayer.hurt = true;
							otherPlayer.armor += 10;
							if (otherPlayer.hp <= 0)
							{
								otherPlayer.dead = true;
								playerCount -= 1;
								deadSound.play();
							}
						}
						else
						{
							if (arrowCharged[arrowCount])
							{
								otherPlayer.armor -= allPlayer.attack*2;						
							}
							else
							{
								otherPlayer.armor -= allPlayer.attack;
							}
							if (otherPlayer.armor < 0)
							{
								otherPlayer.armor = 0;
							}
						}
						if (allPlayer.arrow.speedX != 0)attackEffectRenderer[loopCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						else attackEffectRenderer[loopCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
						attackEffectRenderer[loopCount].check = true;
						attackEffectRenderer[loopCount].time = 0;
						allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
						allPlayer.arrow.setVisible(false);
						arrowCharged[arrowCount] = false;
					}
				}
			}
			arrowCount += 1;
			loopCount += 1;
		}
		for (PlayerCharacter allPlayer : player)
		{
			if (!allPlayer.isVisible() || allPlayer.dead)
			{
				continue;
			}
			if (!allPlayer.moving)
			{
				if (allPlayer.leftPressed)
				{
					allPlayer.speedLeft = 5;
					allPlayer.direction = "left";
					allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX()-50, allPlayer.hitbox.getY(), allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					allPlayer.moving = true;
				}
				if (allPlayer.rightPressed)
				{
					allPlayer.speedRight = 5;
					allPlayer.direction = "right";
					allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX()+50, allPlayer.hitbox.getY(), allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					allPlayer.moving = true;
				}
				if (allPlayer.upPressed)
				{
					allPlayer.speedUp = 5;
					allPlayer.direction = "up";
					allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX(), allPlayer.hitbox.getY()+50, allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					allPlayer.moving = true;
				}
				if (allPlayer.downPressed)
				{
					allPlayer.speedDown = 5;
					allPlayer.direction = "down";
					allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX(), allPlayer.hitbox.getY()-50, allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					allPlayer.moving = true;
				}
			}
			if (allPlayer.moving)
			{
				for (GameObject wall : walls)
				{
					if (checkCollision(allPlayer.checkBlock, wall))
					{
						allPlayer.moving = false;
						allPlayer.speedUp = 0;
						allPlayer.speedDown = 0;
						allPlayer.speedLeft = 0;
						allPlayer.speedRight = 0;
						allPlayer.speed_x = 0;
						allPlayer.speed_y = 0;
						allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX(), allPlayer.hitbox.getY(), allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					}
				}
				for (GameObject wall : normalWalls)
				{
					if (checkCollision(allPlayer.checkBlock, wall))
					{
						allPlayer.moving = false;
						allPlayer.speedUp = 0;
						allPlayer.speedDown = 0;
						allPlayer.speedLeft = 0;
						allPlayer.speedRight = 0;
						allPlayer.speed_x = 0;
						allPlayer.speed_y = 0;
						allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX(), allPlayer.hitbox.getY(), allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					}
				}
//				for (PlayerCharacter otherPlayer : player)// this check collision between player
//				{
//					if (otherPlayer == allPlayer || !allPlayer.isVisible())
//					{
//						continue;
//					}
//					if (checkCollision(otherPlayer, allPlayer, true))
//					{
//						allPlayer.moving = false;
//						allPlayer.speedUp = 0;
//						allPlayer.speedDown = 0;
//						allPlayer.speedLeft = 0;
//						allPlayer.speedRight = 0;
//						allPlayer.speed_x = 0;
//						allPlayer.speed_y = 0;
//						allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX(), allPlayer.hitbox.getY(), allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
//					}
//				}
			}
			if (allPlayer.moving)
			{
				allPlayer.speed_x = allPlayer.speedRight - allPlayer.speedLeft;
				allPlayer.speed_y = allPlayer.speedUp - allPlayer.speedDown;
			}
		}
	}

	public void endStageRender()
	{
		end.draw();
	}

	public void pauseStageRender()
	{
		pause.draw();
	}
	
	public void settingStageRender()
	{
		setting.draw();
		batch.begin();
		float xPosition = 200;
		for (int i = 0; i < 4; i++)
		{
			//if player[i] use keyboard
			if (player[i].controlType.equals("keyboard"))
			{
				font12.draw(batch, Keys.toString(player[i].controlUp), xPosition, 430);
				font12.draw(batch, Keys.toString(player[i].controlDown), xPosition, 370);
				font12.draw(batch, Keys.toString(player[i].controlLeft), xPosition, 310);
				font12.draw(batch, Keys.toString(player[i].controlRight), xPosition, 250);
				font12.draw(batch, Keys.toString(player[i].controlAttack), xPosition, 190);
				font12.draw(batch, Keys.toString(player[i].controlBack), xPosition, 130);
			}
			else
			{
					font12.draw(batch, Integer.toString(player[i].controlUp), xPosition, 430);
					font12.draw(batch, Integer.toString(player[i].controlDown), xPosition, 370);
					font12.draw(batch, Integer.toString(player[i].controlLeft), xPosition, 310);
					font12.draw(batch, Integer.toString(player[i].controlRight), xPosition, 250);
					font12.draw(batch, Integer.toString(player[i].controlAttack), xPosition, 190);
					font12.draw(batch, Integer.toString(player[i].controlBack), xPosition, 130);
			}
			xPosition += 350;
		}
		if(changeControl)
		{
			batch.draw(gray, 0, 0, 1350, 750);
			font12.draw(batch, "press button to change or press esc to cancel", 350, 600);
		}
		batch.end();
	}
	
	public void howToStageRender()
	{
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		menu.dispose();
		character.dispose();
		game.dispose();
		end.dispose();
		setting.dispose();
		pause.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (screen.equals("menu"))
		{
			keyDownInMenuStage(keycode);
		}
		else if (screen.equals("character"))
		{
			keyDownInCharacterStage(keycode);
		}
		else if (screen.equals("game"))
		{
			keyDownInGameStage(keycode);
		}
		else if (screen.equals("end"))
		{
			keyDownInEndStage(keycode);
		}
		else if (screen.equals("pause"))
		{
			keyDownInPauseStage(keycode);
		}
		else if (screen.equals("setting"))
		{
			keyDownInSettingStage(keycode);
		}
		else if (screen.equals("howto"))
		{
			keyDownInHowToStage(keycode);
		}
		return true;
	}

	public void keyDownInMenuStage(int keycode)
	{
		if (keycode == Keys.ENTER)
		{
			confirmSound.play();
			if (cursorPosition == 1)
			{
				screen = "character";
			}
			else if (cursorPosition == 2)
			{
				screen = "setting";
				back = "menu";
			}
			else if (cursorPosition == 3)
			{
				screen = "howto";
			}
			else if (cursorPosition == 4)
			{
				Gdx.app.exit();
			}
		}
		for (PlayerCharacter allPlayer : player)
		{
			if (keycode == allPlayer.controlRight)
			{
				cursorPosition = 1;
				cursorSound.play();
			}
			if (keycode == allPlayer.controlLeft)
			{
				cursorPosition = 2;
				cursorSound.play();
			}
			if (cursorPosition == 1)
			{
				continue;
			}
			if (keycode == allPlayer.controlDown)
			{
				cursorPosition += 1;
				cursorSound.play();
				if (cursorPosition > 4)
				{
					cursorPosition = 2;
				}
			}
			else if (keycode == allPlayer.controlUp)
			{
				cursorPosition -= 1;
				cursorSound.play();
				if (cursorPosition <= 1)// change this if there is more than 2 button
				{
					cursorPosition = 4;
				}
			}
			if (keycode == allPlayer.controlAttack)
			{
				confirmSound.play();
				if (cursorPosition == 1)
				{
					screen = "character";
				}
				else if (cursorPosition == 2)
				{
					screen = "setting";
					back = "menu";
				}
				else if (cursorPosition == 3)
				{
					screen = "howto";
				}
				else if (cursorPosition == 4)
				{
					Gdx.app.exit();
				}
			}
		}
		if (cursorPosition == 1)
		{
			menuArrow.setX(menuPointerStart.getX());
			menuArrow.setY(menuPointerStart.getY());
		}
		else if (cursorPosition == 2)
		{
			menuArrow.setX(menuPointerSetting.getX());
			menuArrow.setY(menuPointerSetting.getY());
		}
		else if (cursorPosition == 3)
		{
			menuArrow.setX(menuPointerHowTo.getX());
			menuArrow.setY(menuPointerHowTo.getY());
		}
		else if (cursorPosition == 4)
		{
			menuArrow.setX(menuPointerExit.getX());
			menuArrow.setY(menuPointerExit.getY());
		}
	}

	public void keyDownInCharacterStage(int keycode)
	{
		if (keycode == Keys.ENTER && playerCount >= 2)
		{
			createMap();
			screen = "game";
			confirmSound.play();
			for (int i = 0; i < 4; i++)
			{
				if (!player[i].isVisible())
				{
					playerSprite[i].setVisible(false);
					weaponSprite[i].setVisible(false);
					continue;
				}
				else
				{
					playerSprite[i].setVisible(true);
					playerSprite[i].setAnimation(player[i].walkingAtlas, "0001");
					weaponSprite[i].setVisible(true);
				}
			}
		}
		else if (keycode == Keys.ESCAPE)
		{
			screen = "menu";
			cancelSound.play();
			resetVariableInCharacterStage();
		}
		for (int i = 0; i<4; i++)
		{
			if (!player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (keycode == player[i].controlBack && playerCount == 0)
			{
				screen = "menu";
				cancelSound.play();
				resetVariableInCharacterStage();
			}
			if (keycode == player[i].controlAttack)
			{
				if (!playerCharacterSelect[i].isVisible())
				{
					confirmSound.play();
					playerCharacterSelect[i].setVisible(true);
					playerCount += 1;
					player[i].setIngame(true);
				}
			}
			else if (keycode == player[i].controlBack)
			{
				if (playerCharacterSelect[i].isVisible())
				{
					cancelSound.play();
					playerCharacterSelect[i].setVisible(false);
					playerCount -= 1;
					player[i].setIngame(false);
				}
			}
			if (playerCharacterSelect[i].isVisible())
			{
				if (keycode == player[i].controlLeft)
				{
					cursorSound.play();
					if (characterIndex[i]-1 >= 0)
					{
						characterIndex[i] -= 1;
					}
				}
				else if (keycode == player[i].controlRight)
				{
					cursorSound.play();
					if (characterIndex[i]+1 <= 4)// change 4 to number of character texture here
					{
						characterIndex[i] += 1;
					}
				}
				if (characterIndex[i] == 0)
				{
					playerCharacterSelect[i].setAnimation(PlayerCharacter.character1, "0001");
					player[i].setTexture(PlayerCharacter.character1, PlayerCharacter.character1Dead);
				}
				else if(characterIndex[i] == 1)
				{
					playerCharacterSelect[i].setAnimation(PlayerCharacter.character2, "0001");
					player[i].setTexture(PlayerCharacter.character2, PlayerCharacter.character2Dead);
				}
				else if(characterIndex[i] == 2)
				{
					playerCharacterSelect[i].setAnimation(PlayerCharacter.character3, "0001");
					player[i].setTexture(PlayerCharacter.character3, PlayerCharacter.character3Dead);
				}
				else if(characterIndex[i] == 3)
				{
					playerCharacterSelect[i].setAnimation(PlayerCharacter.character4, "0001");
					player[i].setTexture(PlayerCharacter.character4, PlayerCharacter.character4Dead);
				}
			}
		}
	}

	public void keyDownInGameStage(int keycode)
	{
		if (keycode == Keys.ESCAPE)
		{
			screen = "pause";
			cancelSound.play();
		}
		for (PlayerCharacter allPlayer : player) {
			//			if (allPlayer.charging)
			//			{
			//				continue;
			//			}// comment this make player able to change direction of attack while charging}
			if (allPlayer.dead  || playerCount <= 1 || !allPlayer.isVisible())
			{
				continue;
			}
			if (keycode == allPlayer.controlBack)
			{
				screen = "pause";
				cancelSound.play();
			}
			if (keycode == allPlayer.controlLeft)
			{
				allPlayer.leftPressed = true;
				allPlayer.upPressed = false;
				allPlayer.downPressed = false;
				allPlayer.rightPressed = false;
			}// use if or else if here?? use normal if will make character to walk diagonal and bug... i guess?
			else if (keycode == allPlayer.controlRight)
			{
				allPlayer.rightPressed = true;
				allPlayer.upPressed = false;
				allPlayer.downPressed = false;
				allPlayer.leftPressed = false;
			}
			else if (keycode == allPlayer.controlUp)
			{
				allPlayer.upPressed = true;
				allPlayer.leftPressed = false;
				allPlayer.rightPressed = false;
				allPlayer.downPressed = false;
			}
			else if (keycode == allPlayer.controlDown)
			{
				allPlayer.downPressed = true;
				allPlayer.leftPressed = false;
				allPlayer.rightPressed = false;
				allPlayer.upPressed = false;
			}
			
			if (keycode == allPlayer.controlAttack && allPlayer.currentChargeTime <= 0 && allPlayer.currentAttackCooldown <= 0)
			{
				//				if (allPlayer.currentAttackCooldown <= 0 && !allPlayer.charging)// change hereeeeeeeeeeeeeeee
				//				{
				//					allPlayer.currentChargeTime = allPlayer.attackChargeTime;
				//					allPlayer.charging = true;
				//				}
				allPlayer.charging = true;
			}
		}
	}

	public void keyDownInEndStage(int keycode)
	{
		if (keycode == Keys.ENTER)
		{
			screen = "menu";
			resetVariableInCharacterStage();
			resetVariableInGameStage();
		}
	}

	public void keyDownInPauseStage(int keycode)
	{
		if (keycode == Keys.ESCAPE)
		{
			cancelSound.play();
			screen = "game";
			// end all lingering input
			pauseCursorPosition = 1;
			for (PlayerCharacter allPlayer : player)
			{
				if (allPlayer.charging)
				{
					allPlayer.attacking = true;
					allPlayer.charging = false;
					if (allPlayer.currentChargeTime < 0.5f)
					{
						allPlayer.currentChargeTime = 0.5f;
					}
				}
				allPlayer.upPressed = false;
				allPlayer.downPressed = false;
				allPlayer.leftPressed = false;
				allPlayer.rightPressed = false;
			}
		}
		else if (keycode == Keys.ENTER)
		{
			confirmSound.play();
			if (pauseCursorPosition == 1)// continue
			{
				screen = "game";
				pauseCursorPosition = 1;
				// end all lingering input
				for (PlayerCharacter allPlayer2 : player)
				{
					if (allPlayer2.charging)
					{
						allPlayer2.attacking = true;
						allPlayer2.charging = false;
						if (allPlayer2.currentChargeTime < 0.5f)
						{
							allPlayer2.currentChargeTime = 0.5f;
						}
					}
					allPlayer2.upPressed = false;
					allPlayer2.downPressed = false;
					allPlayer2.leftPressed = false;
					allPlayer2.rightPressed = false;
				}
			}
			else if (pauseCursorPosition == 2)// setting
			{
				screen = "setting";
				back = "pause";
			}
			else if (pauseCursorPosition == 3)// go back to menu
			{
				screen = "menu";
				pauseCursorPosition = 1;
				resetVariableInCharacterStage();
				resetVariableInGameStage();
			}
		}
		for (PlayerCharacter allPlayer : player)
		{
			if (keycode == allPlayer.controlBack)
			{
				cancelSound.play();
				screen = "game";
				pauseCursorPosition = 1;
				// end all lingering input
				for (PlayerCharacter allPlayer2 : player)
				{
					if (allPlayer2.charging)
					{
						allPlayer2.attacking = true;
						allPlayer2.charging = false;
						if (allPlayer2.currentChargeTime < 0.5f)
						{
							allPlayer2.currentChargeTime = 0.5f;
						}
					}
					allPlayer2.upPressed = false;
					allPlayer2.downPressed = false;
					allPlayer2.leftPressed = false;
					allPlayer2.rightPressed = false;
				}
			}
			if (keycode == allPlayer.controlDown)
			{
				cursorSound.play();
				pauseCursorPosition += 1;
				if (pauseCursorPosition > 3)
				{
					pauseCursorPosition -= 1;
				}
			}
			else if (keycode == allPlayer.controlUp)
			{
				cursorSound.play();
				pauseCursorPosition -= 1;
				if (pauseCursorPosition < 1)
				{
					pauseCursorPosition += 1;
				}
			}
			else if (keycode == allPlayer.controlAttack)
			{
				confirmSound.play();
				if (pauseCursorPosition == 1)// continue
				{
					screen = "game";
					pauseCursorPosition = 1;
					// end all lingering input
					for (PlayerCharacter allPlayer2 : player)
					{
						if (allPlayer2.charging)
						{
							allPlayer2.attacking = true;
							allPlayer2.charging = false;
							if (allPlayer2.currentChargeTime < 0.5f)
							{
								allPlayer2.currentChargeTime = 0.5f;
							}
						}
						allPlayer2.upPressed = false;
						allPlayer2.downPressed = false;
						allPlayer2.leftPressed = false;
						allPlayer2.rightPressed = false;
					}
				}
				else if (pauseCursorPosition == 2)// setting
				{
					screen = "setting";
					back = "pause";
				}
				else if (pauseCursorPosition == 3)// go back to menu
				{
					screen = "menu";
					pauseCursorPosition = 1;
					resetVariableInCharacterStage();
					resetVariableInGameStage();
				}
			}
		}
		if (pauseCursorPosition == 1)
		{
			pauseArrow.setY(482);
		}
		else if (pauseCursorPosition == 2)
		{
			pauseArrow.setY(296);
		}
		else if (pauseCursorPosition == 3)
		{
			pauseArrow.setY(160);
		}
	}
	
	public void keyDownInSettingStage(int keycode)
	{
		if (changeControl)
		{
			if (keycode == Keys.ESCAPE)
			{
				cancelSound.play();
				changeControl = false;
			}
			else if (player[playerNumber].controlType.equals("keyboard"))
			{
				confirmSound.play();
				if (controlName.equals("up"))
				{
					player[playerNumber].controlUp = keycode;
				}
				else if (controlName.equals("down"))
				{
					player[playerNumber].controlDown = keycode;
				}
				else if (controlName.equals("left"))
				{
					player[playerNumber].controlLeft = keycode;
				}
				else if (controlName.equals("right"))
				{
					player[playerNumber].controlRight = keycode;
				}
				else if (controlName.equals("attack"))
				{
					player[playerNumber].controlAttack = keycode;
				}
				else if (controlName.equals("back"))
				{
					player[playerNumber].controlBack = keycode;
				}
				changeControl = false;
			}
		}
		else
		{
			if (keycode == Keys.ESCAPE)
			{
				cancelSound.play();
				screen = back;
			}
		}
	}
	
	public void keyDownInHowToStage(int keycode)
	{
		if (keycode == Keys.ESCAPE)
		{
			cancelSound.play();
			screen = "menu";
		}
		for (PlayerCharacter allPlayer : player)
		{
			if (keycode == allPlayer.controlBack)
			{
				cancelSound.play();
				screen = "menu";
			}
		}
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (screen.equals("game"))
		{
			keyUpInGameStage(keycode);
		}
		return true;
	}

	public void keyUpInGameStage(int keycode)
	{
		for (PlayerCharacter allPlayer : player) {
			if (keycode == allPlayer.controlLeft)
			{
				//				allPlayer.speedLeft = 0;
				allPlayer.leftPressed = false;
				if (allPlayer.speedRight > 0)
				{
					allPlayer.direction = "right";
				}
				if (allPlayer.speedUp > 0)
				{
					allPlayer.direction = "up";
				}
				if (allPlayer.speedDown > 0)
				{
					allPlayer.direction = "down";
				}
			}
			if (keycode == allPlayer.controlRight)
			{
				//				allPlayer.speedRight = 0;
				allPlayer.rightPressed = false;
				if (allPlayer.speedLeft > 0)
				{
					allPlayer.direction = "left";
				}
				if (allPlayer.speedUp > 0)
				{
					allPlayer.direction = "up";
				}
				if (allPlayer.speedDown > 0)
				{
					allPlayer.direction = "down";
				}
			}
			if (keycode ==  allPlayer.controlUp)
			{
				//				allPlayer.speedUp = 0;
				allPlayer.upPressed = false;
				if (allPlayer.speedDown > 0)
				{
					allPlayer.direction = "down";
				}
				if (allPlayer.speedLeft > 0)
				{
					allPlayer.direction = "left";
				}
				if (allPlayer.speedRight > 0)
				{
					allPlayer.direction = "right";
				}
			}
			if (keycode == allPlayer.controlDown)
			{
				//				allPlayer.speedDown = 0;
				allPlayer.downPressed = false;
				if (allPlayer.speedUp > 0)
				{
					allPlayer.direction = "up";
				}
				if (allPlayer.speedLeft > 0)
				{
					allPlayer.direction = "left";
				}
				if (allPlayer.speedRight > 0)
				{
					allPlayer.direction = "right";
				}
			}
			//			allPlayer.speed_x = allPlayer.speedRight - allPlayer.speedLeft;
			//			allPlayer.speed_y = allPlayer.speedUp - allPlayer.speedDown;
			if (keycode == allPlayer.controlAttack && allPlayer.charging)
			{
				allPlayer.attacking = true;
				allPlayer.charging = false;
				if (allPlayer.currentChargeTime < 0.5f)
				{
					allPlayer.currentChargeTime = 0.5f;
				}
				//allPlayer.currentAttackCooldown = allPlayer.attackCooldown;
			}
		}
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mousePositionScreen.x = screenX;
		mousePositionScreen.y = screenY;
		mousePositionStage = menu.screenToStageCoordinates(mousePositionScreen);
		System.out.println(mousePositionStage.x + " " + mousePositionStage.y);
		if (screen.equals("menu"))
		{
			touchDownInMenuStage(mousePositionStage, button);
		}
		else if (screen.equals("setting"))
		{
			touchDownInSettingStage(mousePositionStage, button);
		}
		return false;
	}

	public void touchDownInMenuStage(Vector2 mousePosition, int button)
	{
		if (mousePosition.x >= menuButtonStart.getX() && mousePosition.x <= menuButtonStart.getX()+menuButtonStart.getWidth())
		{
			if (mousePosition.y >= menuButtonStart.getY() && mousePosition.y <= menuButtonStart.getY()+menuButtonStart.getHeight())
			{
				screen = "character";
				confirmSound.play();
			}
		}
		else if (mousePosition.x >= menuButtonSetting.getX() && mousePosition.x <= menuButtonSetting.getX()+menuButtonSetting.getWidth())
		{
			if (mousePosition.y >= menuButtonSetting.getY() && mousePosition.y <= menuButtonSetting.getY()+menuButtonSetting.getHeight())
			{
				screen = "setting";
				back = "menu";
				confirmSound.play();
			}
		}
		else if (mousePosition.x >= menuButtonHowto.getX() && mousePosition.x <= menuButtonHowto.getX()+menuButtonHowto.getWidth())
		{
			if (mousePosition.y >= menuButtonHowto.getY() && mousePosition.y <= menuButtonHowto.getY()+menuButtonHowto.getHeight())
			{
				screen = "howto";
				confirmSound.play();
			}
		}
		else if (mousePosition.x >= menuButtonExit.getX() && mousePosition.x <= menuButtonExit.getX()+menuButtonExit.getWidth())
		{
			if (mousePosition.y >= menuButtonExit.getY() && mousePosition.y <= menuButtonExit.getY()+menuButtonExit.getHeight())
			{
				Gdx.app.exit();
			}
		}
	}
	
	public void touchDownInSettingStage(Vector2 mousePosition, int button)
	{
		if (button == Buttons.LEFT && !changeControl)
		{
			float xPosition = 165;
			for(int i = 0; i < 4; i++)
			{
				if (mousePosition.x >= xPosition && mousePosition.x <= xPosition + 150)
				{
					if (mousePosition.y >= 390 && mousePosition.y <= 450)
					{
						changeControl = true;
						playerNumber = i;
						controlName = "up";
						confirmSound.play();
					}
					else if (mousePosition.y >= 330 && mousePosition.y <= 389)
					{
						changeControl = true;
						playerNumber = i;
						controlName = "down";
						confirmSound.play();
					}
					else if (mousePosition.y >= 270 && mousePosition.y <= 329)
					{
						changeControl = true;
						playerNumber = i;
						controlName = "left";
						confirmSound.play();
					}
					else if (mousePosition.y >= 210 && mousePosition.y <= 269)
					{
						changeControl = true;
						playerNumber = i;
						controlName = "right";
						confirmSound.play();
					}
					else if (mousePosition.y >= 150 && mousePosition.y <= 209)
					{
						changeControl = true;
						playerNumber = i;
						controlName = "attack";
						confirmSound.play();
					}
					else if (mousePosition.y >= 90 && mousePosition.y <= 149)
					{
						changeControl = true;
						playerNumber = i;
						controlName = "back";
						confirmSound.play();
					}
				}
				xPosition += 350;
			}
			// check click control type
			for (int i = 0; i < 4; i++)
			{
				float controlTypeX = playerControlType[i].getX();
				float controlTypeY = playerControlType[i].getY();
				float controlTypeWidth = playerControlType[i].getWidth();
				float controlTypeHeight = playerControlType[i].getHeight();
				if (mousePosition.x >= controlTypeX && mousePosition.x <= controlTypeX+controlTypeWidth)
				{
					if (mousePosition.y >= controlTypeY && mousePosition.y <= controlTypeY+controlTypeHeight)
					{
						if (player[i].controlType.equals("keyboard"))
						{
							player[i].controlType = "controller1";
							playerControlType[i].setAnimation(controlType, "0002");
							player[i].controllerCount = 0;
							confirmSound.play();
						}
						else if (player[i].controlType.equals("controller1"))
						{
							player[i].controlType = "controller2";
							playerControlType[i].setAnimation(controlType, "0003");
							player[i].controllerCount = 1;
							confirmSound.play();
						}
						else if (player[i].controlType.equals("controller2"))
						{
							player[i].controlType = "keyboard";
							playerControlType[i].setAnimation(controlType, "0001");
							player[i].controllerCount = -1;
							confirmSound.play();
						}
					}
				}
			}
		}// if button left
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mousePositionScreen.x = screenX;
		mousePositionScreen.y = screenY;
		mousePositionStage = menu.screenToStageCoordinates(mousePositionScreen);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mousePositionScreen.x = screenX;
		mousePositionScreen.y = screenY;
		mousePositionStage = menu.screenToStageCoordinates(mousePositionScreen);
		//System.out.println(mousePositionStage.x + " " + mousePositionStage.y);
		if (screen.equals("menu"))
		{
			mouseMovedInMenuStage(mousePositionStage);
		}
		else if (screen.equals("character"))
		{
			mouseMovedInCharacterStage(mousePositionStage);
		}
		else if (screen.equals("setting"))
		{
			mouseMovedInSettingStage(mousePositionStage);
		}
		else if (screen.equals("pause"))
		{
			mouseMovedInPauseStage(mousePositionStage);
		}
		return false;
	}

	public void mouseMovedInMenuStage(Vector2 mousePosition)
	{
		if (mousePositionStage.x >= menuButtonStart.getX() && mousePositionStage.x <= menuButtonStart.getX()+menuButtonStart.getWidth())
		{
			if (mousePositionStage.y >= menuButtonStart.getY() && mousePositionStage.y <= menuButtonStart.getY()+menuButtonStart.getHeight())
			{
				cursorPosition = 1;
				menuArrow.setX(menuPointerStart.getX());
				menuArrow.setY(menuPointerStart.getY());
			}
		}
		else if (mousePositionStage.x >= menuButtonSetting.getX() && mousePositionStage.x <= menuButtonSetting.getX()+menuButtonSetting.getWidth())
		{
			if (mousePositionStage.y >= menuButtonSetting.getY() && mousePositionStage.y <= menuButtonSetting.getY()+menuButtonSetting.getHeight())
			{
				cursorPosition = 2;
				menuArrow.setX(menuPointerSetting.getX());
				menuArrow.setY(menuPointerSetting.getY());
			}
		}
		else if (mousePositionStage.x >= menuButtonHowto.getX() && mousePositionStage.x <= menuButtonHowto.getX()+menuButtonHowto.getWidth())
		{
			if (mousePositionStage.y >= menuButtonHowto.getY() && mousePositionStage.y <= menuButtonHowto.getY()+menuButtonHowto.getHeight())
			{
				cursorPosition = 3;
				menuArrow.setX(menuPointerHowTo.getX());
				menuArrow.setY(menuPointerHowTo.getY());
			}
		}
		else if (mousePositionStage.x >= menuButtonExit.getX() && mousePositionStage.x <= menuButtonExit.getX()+menuButtonExit.getWidth())
		{
			if (mousePositionStage.y >= menuButtonExit.getY() && mousePositionStage.y <= menuButtonExit.getY()+menuButtonExit.getHeight())
			{
				cursorPosition = 4;
				menuArrow.setX(menuPointerExit.getX());
				menuArrow.setY(menuPointerExit.getY());
			}
		}
	}

	public void mouseMovedInCharacterStage(Vector2 mousePosition)
	{
		
	}
	
	public void mouseMovedInSettingStage(Vector2 mousePosition)
	{
		
	}
	
	public void mouseMovedInPauseStage(Vector2 mousePosition)
	{
		
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public boolean checkContain(PlayerCharacter player)
	{
		return playGround.hitbox.contains(player.hitbox);
	}

	public boolean checkCollision(PlayerCharacter player, GameObject object)
	{
		return player.hitbox.overlaps(object.hitbox);
	}

	public boolean checkCollision(GameObject object1, GameObject object2)
	{
		return object1.hitbox.overlaps(object2.hitbox);
	}

	public boolean checkCollision(PlayerCharacter player, ItemDrop item)
	{
		return player.hitbox.overlaps(item.hitbox);
	}

	public boolean checkCollision(PlayerCharacter player, PlayerCharacter player2)
	{
		return player.hitbox.overlaps(player2.hitbox);
	}
	
	public boolean checkCollision(PlayerCharacter player, PlayerCharacter player2, boolean checkBlock)
	{
		return player.checkBlock.hitbox.overlaps(player2.checkBlock.hitbox);
	}
	
	public boolean checkCollision(PlayerCharacter player, GameObject object, String attack)
	{
		return player.attackHitbox.overlaps(object.hitbox);
	}

	public boolean checkCollision(PlayerCharacter player, PlayerCharacter player2, String attack)
	{
		return player.attackHitbox.overlaps(player2.hitbox);
	}

	public void checkPlayerAttack(PlayerCharacter playerAttack)
	{
		playerAttack.attackSound.play();
		for (GameObject wall : normalWalls) {
			if (checkCollision(playerAttack, wall, "attack"))
			{
				if (wall instanceof NormalWall)
				{
					if (playerAttack.weaponName.equals("fist"))
					{
						((NormalWall) wall).hp -= 1;
					}
					else
					{
						if (playerAttack.chargeMax)
						{
							((NormalWall) wall).hp -= 3;
						}
						else
						{
							((NormalWall) wall).hp -= 2;
						}
					}
				}
			}
		}
		for (ItemDrop item : itemDrop)
		{
			if (checkCollision(playerAttack, item, "attack"))
			{
				// add item destroy sound here
				item.dropped = false;
				item.setVisible(item.dropped);
				ItemDrop.dropCount -= 1;
				item.hitbox.setX(-1000);
				item.hitbox.setY(-1000);
			}
		}
		for (PlayerCharacter allPlayer : player) {
			if (allPlayer == playerAttack || !allPlayer.isVisible() || allPlayer.dead)
			{
				continue;
			}
			if (checkCollision(playerAttack, allPlayer, "attack") && !allPlayer.hurt)
			{
				allPlayer.regenDelay = 5f;
				if (allPlayer.armor <= 0)
				{
					hpDownSound.play();
					allPlayer.hp -= 1;
					allPlayer.hurt = true;
					allPlayer.armor += 10;
					if (allPlayer.hp <= 0)
					{
						allPlayer.dead = true;
						playerCount -= 1;
						deadSound.play();
					}
				}
				else
				{
					damagedSound.play();
					if (playerAttack.chargeMax)
					{
						allPlayer.armor -= playerAttack.attack*2;						
					}
					else
					{
						allPlayer.armor -= playerAttack.attack;
					}
					if (allPlayer.armor < 0)
					{
						allPlayer.armor = 0;
					}
				}
			}
		}
		int arrowCount = 0;
		for (Arrow arrow : playerArrow)
		{
			if (checkCollision(playerAttack, arrow, "attack"))
			{
				parryArrowSound.play();
				if (((Arrow)arrow).speedX != 0)attackEffectRenderer[arrowCount].setValue(((Arrow)arrow).hitbox.getX(), ((Arrow)arrow).hitbox.getY()-15, player[arrowCount].attackHitbox.getWidth(), player[arrowCount].attackHitbox.getHeight(), player[arrowCount].direction);
				else attackEffectRenderer[arrowCount].setValue(((Arrow)arrow).hitbox.getX(), ((Arrow)arrow).hitbox.getY(), player[arrowCount].attackHitbox.getWidth(), player[arrowCount].attackHitbox.getHeight(), player[arrowCount].direction);
				attackEffectRenderer[arrowCount].check = true;
				attackEffectRenderer[arrowCount].time = 0;
				((Arrow)arrow).setArrow(player[arrowCount].getX()+25, player[arrowCount].getY()+20, player[arrowCount].direction, player[arrowCount].weaponLV);
				((Arrow)arrow).setVisible(false);
				arrowCharged[arrowCount] = false;
			}
			arrowCount += 1;
		}
		playerAttack.chargeMax = false;
	}

	public void resetVariableInCharacterStage()
	{
		playerCount = 0;
		for (UI pcs : playerCharacterSelect)
		{
			pcs.setAnimation(PlayerCharacter.character1, "0001");
			pcs.setVisible(false);
		}
		for (int i = 0; i < 4; i++)
		{
			characterIndex[i] = 0;
		}
	}

	public void resetVariableInGameStage()
	{
		delay = 3;
		for (Actor wall : game.getActors())
		{
			if (wall instanceof NormalWall)
			{
				wall.addAction(Actions.removeActor());
			}
		}
		game.act();
		for (ItemDrop item : itemDrop) {
			item.dropped = false;
			item.setVisible(false);
			item.hitbox.setX(-1000);
			item.hitbox.setY(-1000);
		}
		ItemDrop.dropCount = 0;
		arrowCharged[0] = false;
		arrowCharged[1] = false;
		for (PlayerCharacter allPlayer : player) {
			allPlayer.hp = 3;
			allPlayer.armor = 100;
			allPlayer.weaponName = "fist";
			allPlayer.attackCooldown = 0.5f;
			allPlayer.attackChargeTime = 0.5f;
			allPlayer.currentAttackCooldown = 0;
			allPlayer.attackEffectAnim = EffectRenderer.punchAnimation;
			allPlayer.hurt = false;
			allPlayer.dead = false;
			allPlayer.blink = false;
			allPlayer.attacking = false;
			allPlayer.charging = false;
			allPlayer.blinkFrameCount = 0;
			allPlayer.currentBlinkTime = 0;
			allPlayer.attackHeight = 40;
			allPlayer.attackWidth = 40;
			allPlayer.direction = "right";
			//allPlayer.time // should i reset this?
			allPlayer.attack = 0;
			allPlayer.weapon.setVisible(true);
			allPlayer.weaponLV = 0;
			allPlayer.weaponAtlas = PlayerWeapon.fist;
			allPlayer.weaponAnim = PlayerWeapon.fistAnim;
			allPlayer.weapon.updateWeaponAnimation();
			allPlayer.leftPressed = false;
			allPlayer.rightPressed = false;
			allPlayer.upPressed = false;
			allPlayer.rightPressed = false;
			allPlayer.speed_x = 0;
			allPlayer.speed_y = 0;
			allPlayer.speedUp = 0;
			allPlayer.speedDown = 0;
			allPlayer.speedLeft = 0;
			allPlayer.speedRight = 0;
			allPlayer.arrow.setX(-100);
			allPlayer.arrow.setY(-100);
			allPlayer.attackSound = PlayerWeapon.fistSound;
			if (allPlayer == player[0])
			{
				allPlayer.setX(50);
				allPlayer.setY(50);
			}
			else if (allPlayer == player[1])
			{
				allPlayer.setX(1250);
				allPlayer.setY(550);
			}
			allPlayer.setIngame(false);
		}
	}

	public void moveNormalWallZIndex()
	{
		for (NormalWall wall : normalWalls)
		{
//			if (wall != null)
			wall.setZIndex(3);
		}
	}

	// controller
	@Override
	public void connected(Controller controller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected(Controller controller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (screen.equals("setting"))
		{
			buttonDownInSettingStage(controller, buttonCode);
		}
		else if (screen.equals("menu"))
		{
			buttonDownInMenuStage(controller, buttonCode);
		}
		else if (screen.equals("character"))
		{
			buttonDownInCharacterStage(controller, buttonCode);
		}
		else if (screen.equals("game"))
		{
			buttonDownInGameStage(controller, buttonCode);
		}
		else if (screen.equals("end"))
		{
			buttonDownInEndStage(controller, buttonCode);
		}
		else if (screen.equals("pause"))
		{
			buttonDownInPauseStage(controller, buttonCode);
		}
		else if (screen.equals("howto"))
		{
			buttonDownInHowToStage(controller, buttonCode);
		}
		return true;
	}

	public void buttonDownInSettingStage(Controller controller, int buttonCode)
	{
		if (changeControl && (player[playerNumber].controlType.equals("controller1") || player[playerNumber].controlType.equals("controller2")))
		{
			if (controller == Controllers.getControllers().get(player[playerNumber].controllerCount))
			{
				confirmSound.play();
				if (controlName.equals("up"))
				{
					player[playerNumber].controlUp = buttonCode;
				}
				else if (controlName.equals("down"))
				{
					player[playerNumber].controlDown = buttonCode;
				}
				else if (controlName.equals("left"))
				{
					player[playerNumber].controlLeft = buttonCode;
				}
				else if (controlName.equals("right"))
				{
					player[playerNumber].controlRight = buttonCode;
				}
				else if (controlName.equals("attack"))
				{
					player[playerNumber].controlAttack = buttonCode;
				}
				else if (controlName.equals("back"))
				{
					player[playerNumber].controlBack = buttonCode;
				}
				changeControl = false;
			}
		}
	}

	public void buttonDownInMenuStage(Controller controller, int buttonCode)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				// start if control here
				if (buttonCode == player[i].controlRight)
				{
					cursorPosition = 1;
					cursorSound.play();
				}
				if (buttonCode == player[i].controlLeft)
				{
					cursorPosition = 2;
					cursorSound.play();
				}
				if (buttonCode == player[i].controlDown && cursorPosition != 1)
				{
					cursorPosition += 1;
					cursorSound.play();
					if (cursorPosition > 4)
					{
						cursorPosition = 2;
					}
				}
				else if (buttonCode == player[i].controlUp && cursorPosition != 1)
				{
					cursorPosition -= 1;
					cursorSound.play();
					if (cursorPosition <= 1)// change this if there is more than 2 button
					{
						cursorPosition = 4;
					}
				}
				if (buttonCode == player[i].controlAttack)
				{
					confirmSound.play();
					if (cursorPosition == 1)
					{
						screen = "character";
					}
					else if (cursorPosition == 2)
					{
						screen = "setting";
						back = "menu";
					}
					else if (cursorPosition == 3)
					{
						screen = "howto";
					}
					else if (cursorPosition == 4)
					{
						Gdx.app.exit();
					}
				}
				if (cursorPosition == 1)
				{
					menuArrow.setX(menuPointerStart.getX());
					menuArrow.setY(menuPointerStart.getY());
				}
				else if (cursorPosition == 2)
				{
					menuArrow.setX(menuPointerSetting.getX());
					menuArrow.setY(menuPointerSetting.getY());
				}
				else if (cursorPosition == 3)
				{
					menuArrow.setX(menuPointerHowTo.getX());
					menuArrow.setY(menuPointerHowTo.getY());
				}
				else if (cursorPosition == 4)
				{
					menuArrow.setX(menuPointerExit.getX());
					menuArrow.setY(menuPointerExit.getY());
				}
			}
		}
	}
	
	public void buttonDownInCharacterStage(Controller controller, int buttonCode)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				// start if control here
				if (buttonCode == player[i].controlBack && playerCount == 0)
				{
					screen = "menu";
					resetVariableInCharacterStage();
					cancelSound.play();
				}
				if (buttonCode == player[i].controlAttack)
				{
					if (!playerCharacterSelect[i].isVisible())
					{
						playerCharacterSelect[i].setVisible(true);
						playerCount += 1;
						player[i].setIngame(true);
						confirmSound.play();
					}
				}
				else if (buttonCode == player[i].controlBack)
				{
					if (playerCharacterSelect[i].isVisible())
					{
						playerCharacterSelect[i].setVisible(false);
						playerCount -= 1;
						player[i].setIngame(false);
						cancelSound.play();
					}
				}
				if (playerCharacterSelect[i].isVisible())
				{
					if (buttonCode == player[i].controlLeft)
					{
						cursorSound.play();
						if (characterIndex[i]-1 >= 0)
						{
							characterIndex[i] -= 1;
						}
					}
					else if (buttonCode == player[i].controlRight)
					{
						cursorSound.play();
						if (characterIndex[i]+1 <= 4)// change 4 to number of character texture here
						{
							characterIndex[i] += 1;
						}
					}
					if (characterIndex[i] == 0)
					{
						playerCharacterSelect[i].setAnimation(PlayerCharacter.character1, "0001");
						player[i].setTexture(PlayerCharacter.character1, PlayerCharacter.character1Dead);
					}
					else if(characterIndex[i] == 1)
					{
						playerCharacterSelect[i].setAnimation(PlayerCharacter.character2, "0001");
						player[i].setTexture(PlayerCharacter.character2, PlayerCharacter.character2Dead);
					}
					else if(characterIndex[i] == 2)
					{
						playerCharacterSelect[i].setAnimation(PlayerCharacter.character3, "0001");
						player[i].setTexture(PlayerCharacter.character3, PlayerCharacter.character3Dead);
					}
					else if(characterIndex[i] == 3)
					{
						playerCharacterSelect[i].setAnimation(PlayerCharacter.character4, "0001");
						player[i].setTexture(PlayerCharacter.character4, PlayerCharacter.character4Dead);
					}
				}
			}//}
		}
	}
	
	public void buttonDownInGameStage(Controller controller, int buttonCode)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				// start if control here
				if (player[i].dead  || playerCount <= 1 || !player[i].isVisible())
				{
					continue;
				}
				if (buttonCode == player[i].controlBack)
				{
					screen = "pause";
				}
				if (buttonCode == player[i].controlLeft)
				{
					player[i].leftPressed = true;
					player[i].upPressed = false;
					player[i].downPressed = false;
					player[i].rightPressed = false;
				}// use if or else if here?? use normal if will make character to walk diagonal and bug... i guess?
				else if (buttonCode == player[i].controlRight)
				{
					player[i].rightPressed = true;
					player[i].upPressed = false;
					player[i].downPressed = false;
					player[i].leftPressed = false;
				}
				else if (buttonCode == player[i].controlUp)
				{
					player[i].upPressed = true;
					player[i].leftPressed = false;
					player[i].rightPressed = false;
					player[i].downPressed = false;
				}
				else if (buttonCode == player[i].controlDown)
				{
					player[i].downPressed = true;
					player[i].leftPressed = false;
					player[i].rightPressed = false;
					player[i].upPressed = false;
				}
				
				if (buttonCode == player[i].controlAttack && player[i].currentChargeTime <= 0 && player[i].currentAttackCooldown <= 0)
				{
					//				if (player[i].currentAttackCooldown <= 0 && !player[i].charging)// change hereeeeeeeeeeeeeeee
					//				{
					//					player[i].currentChargeTime = player[i].attackChargeTime;
					//					player[i].charging = true;
					//				}
					player[i].charging = true;
				}
			}
		}
	}
	
	public void buttonDownInEndStage(Controller controller, int buttonCode)
	{
		
	}
	
	public void buttonDownInPauseStage(Controller controller, int buttonCode)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				//start if control here
				if (buttonCode == player[i].controlBack)
				{
					screen = "game";
					cancelSound.play();
					// end all lingering input
					pauseCursorPosition = 1;
					for (PlayerCharacter allPlayer : player)
					{
						if (allPlayer.charging)
						{
							allPlayer.attacking = true;
							allPlayer.charging = false;
							if (allPlayer.currentChargeTime < 0.5f)
							{
								allPlayer.currentChargeTime = 0.5f;
							}
						}
						allPlayer.upPressed = false;
						allPlayer.downPressed = false;
						allPlayer.leftPressed = false;
						allPlayer.rightPressed = false;
					}
				}
				if (buttonCode == player[i].controlDown)
				{
					pauseCursorPosition += 1;
					cursorSound.play();
					if (pauseCursorPosition > 3)
					{
						pauseCursorPosition -= 1;
					}
				}
				else if (buttonCode == player[i].controlUp)
				{
					pauseCursorPosition -= 1;
					cursorSound.play();
					if (pauseCursorPosition < 1)
					{
						pauseCursorPosition += 1;
					}
				}
				else if (buttonCode == player[i].controlAttack)
				{
					confirmSound.play();
					if (pauseCursorPosition == 1)// continue
					{
						screen = "game";
						pauseCursorPosition = 1;
						// end all lingering input
						for (PlayerCharacter allPlayer2 : player)
						{
							if (allPlayer2.charging)
							{
								allPlayer2.attacking = true;
								allPlayer2.charging = false;
								if (allPlayer2.currentChargeTime < 0.5f)
								{
									allPlayer2.currentChargeTime = 0.5f;
								}
							}
							allPlayer2.upPressed = false;
							allPlayer2.downPressed = false;
							allPlayer2.leftPressed = false;
							allPlayer2.rightPressed = false;
						}
					}
					else if (pauseCursorPosition == 2)// setting
					{
						screen = "setting";
						back = "pause";
					}
					else if (pauseCursorPosition == 3)// go back to menu
					{
						screen = "menu";
						pauseCursorPosition = 1;
						resetVariableInCharacterStage();
						resetVariableInGameStage();
					}
				}
				if (pauseCursorPosition == 1)
				{
					pauseArrow.setY(482);
				}
				else if (pauseCursorPosition == 2)
				{
					pauseArrow.setY(296);
				}
				else if (pauseCursorPosition == 3)
				{
					pauseArrow.setY(160);
				}
			}// controller loop
		}
	}
	
	public void buttonDownInHowToStage(Controller controller, int buttonCode)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller) {
				//start if control here
				if (buttonCode == player[i].controlBack)
				{
					screen = "menu";
					cancelSound.play();
				}
			}
		}
	}
	
	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (screen.equals("game"))
		{
			buttonUpInGameStage(controller, buttonCode);
		}
		return true;
	}
	
	public void buttonUpInGameStage(Controller controller, int buttonCode)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				// start if control here
				if (buttonCode == player[i].controlLeft)
				{
					//				player[i].speedLeft = 0;
					player[i].leftPressed = false;
					if (player[i].speedRight > 0)
					{
						player[i].direction = "right";
					}
					if (player[i].speedUp > 0)
					{
						player[i].direction = "up";
					}
					if (player[i].speedDown > 0)
					{
						player[i].direction = "down";
					}
				}
				if (buttonCode == player[i].controlRight)
				{
					//				player[i].speedRight = 0;
					player[i].rightPressed = false;
					if (player[i].speedLeft > 0)
					{
						player[i].direction = "left";
					}
					if (player[i].speedUp > 0)
					{
						player[i].direction = "up";
					}
					if (player[i].speedDown > 0)
					{
						player[i].direction = "down";
					}
				}
				if (buttonCode ==  player[i].controlUp)
				{
					//				player[i].speedUp = 0;
					player[i].upPressed = false;
					if (player[i].speedDown > 0)
					{
						player[i].direction = "down";
					}
					if (player[i].speedLeft > 0)
					{
						player[i].direction = "left";
					}
					if (player[i].speedRight > 0)
					{
						player[i].direction = "right";
					}
				}
				if (buttonCode == player[i].controlDown)
				{
					//				player[i].speedDown = 0;
					player[i].downPressed = false;
					if (player[i].speedUp > 0)
					{
						player[i].direction = "up";
					}
					if (player[i].speedLeft > 0)
					{
						player[i].direction = "left";
					}
					if (player[i].speedRight > 0)
					{
						player[i].direction = "right";
					}
				}
				if (buttonCode == player[i].controlAttack && player[i].charging)
				{
					player[i].attacking = true;
					player[i].charging = false;
					if (player[i].currentChargeTime < 0.5f)
					{
						player[i].currentChargeTime = 0.5f;
					}
				}
			}// controller loop
		}
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (screen.equals("menu"))
		{
			povInMenuStage(controller, value);
		}
		else if (screen.equals("character"))
		{
			povInCharacterStage(controller, value);
		}
		else if (screen.equals("game"))
		{
			povInGameStage(controller, value);
		}
		else if (screen.equals("end"))
		{
			
		}
		else if (screen.equals("pause"))
		{
			povInPauseStage(controller, value);
		}
		return true;
	}

	public void povInMenuStage(Controller controller, PovDirection value)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				cursorSound.play();
				if (value == PovDirection.east)
				{
					cursorPosition = 1;
				}
				if (value == PovDirection.west)
				{
					cursorPosition = 2;
				}
				if (value == PovDirection.south && cursorPosition != 1)
				{
					cursorPosition += 1;
					if (cursorPosition > 4)
					{
						cursorPosition = 2;
					}
				}
				else if (value == PovDirection.north && cursorPosition != 1)
				{
					cursorPosition -= 1;
					if (cursorPosition <= 1)// change this if there is more than 2 button
					{
						cursorPosition = 4;
					}
				}
				if (cursorPosition == 1)
				{
					menuArrow.setX(menuPointerStart.getX());
					menuArrow.setY(menuPointerStart.getY());
				}
				else if (cursorPosition == 2)
				{
					menuArrow.setX(menuPointerSetting.getX());
					menuArrow.setY(menuPointerSetting.getY());
				}
				else if (cursorPosition == 3)
				{
					menuArrow.setX(menuPointerHowTo.getX());
					menuArrow.setY(menuPointerHowTo.getY());
				}
				else if (cursorPosition == 4)
				{
					menuArrow.setX(menuPointerExit.getX());
					menuArrow.setY(menuPointerExit.getY());
				}
			}
		}
	}
	
	public void povInCharacterStage(Controller controller, PovDirection value)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (playerCharacterSelect[i].isVisible())
				{
					if (value == PovDirection.west)
					{
						cursorSound.play();
						if (characterIndex[i]-1 >= 0)
						{
							characterIndex[i] -= 1;
						}
					}
					else if (value == PovDirection.east)
					{
						cursorSound.play();
						if (characterIndex[i]+1 <= 4)// change 4 to number of character texture here
						{
							characterIndex[i] += 1;
						}
					}
					if (characterIndex[i] == 0)
					{
						playerCharacterSelect[i].setAnimation(PlayerCharacter.character1, "0001");
						player[i].setTexture(PlayerCharacter.character1, PlayerCharacter.character1Dead);
					}
					else if(characterIndex[i] == 1)
					{
						playerCharacterSelect[i].setAnimation(PlayerCharacter.character2, "0001");
						player[i].setTexture(PlayerCharacter.character2, PlayerCharacter.character2Dead);
					}
					else if(characterIndex[i] == 2)
					{
						playerCharacterSelect[i].setAnimation(PlayerCharacter.character3, "0001");
						player[i].setTexture(PlayerCharacter.character3, PlayerCharacter.character3Dead);
					}
					else if(characterIndex[i] == 3)
					{
						playerCharacterSelect[i].setAnimation(PlayerCharacter.character4, "0001");
						player[i].setTexture(PlayerCharacter.character4, PlayerCharacter.character4Dead);
					}
				}
			}
		}
	}
	
	public void povInPauseStage(Controller controller, PovDirection value)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				//start check for input here
				if (value == PovDirection.south)
				{
					pauseCursorPosition += 1;
					cursorSound.play();
					if (pauseCursorPosition > 3)
					{
						pauseCursorPosition -= 1;
					}
				}
				else if (value == PovDirection.north)
				{
					pauseCursorPosition -= 1;
					cursorSound.play();
					if (pauseCursorPosition < 1)
					{
						pauseCursorPosition += 1;
					}
				}
				if (pauseCursorPosition == 1)
				{
					pauseArrow.setY(482);
				}
				else if (pauseCursorPosition == 2)
				{
					pauseArrow.setY(296);
				}
				else if (pauseCursorPosition == 3)
				{
					pauseArrow.setY(160);
				}
			}// controller loop
		}
	}

	public void povInGameStage(Controller controller, PovDirection value)
	{
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				//start check for input here
				if (value == PovDirection.north)
				{
					player[i].upPressed = true;
					player[i].leftPressed = false;
					player[i].rightPressed = false;
					player[i].downPressed = false;
				}
				else if (value == PovDirection.south)
				{
					player[i].downPressed = true;
					player[i].leftPressed = false;
					player[i].rightPressed = false;
					player[i].upPressed = false;
				}
				else if (value  == PovDirection.east)
				{
					player[i].rightPressed = true;
					player[i].upPressed = false;
					player[i].downPressed = false;
					player[i].leftPressed = false;
				}
				else if (value == PovDirection.west)
				{
					player[i].leftPressed = true;
					player[i].upPressed = false;
					player[i].downPressed = false;
					player[i].rightPressed = false;
				}
				else if (value == PovDirection.center)
				{
					player[i].leftPressed = false;
					player[i].upPressed = false;
					player[i].downPressed = false;
					player[i].rightPressed = false;
				}
			}// controller loop here
		}
	}
	
	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		// TODO Auto-generated method stub
		return false;
	}

}
