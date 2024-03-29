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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class ArcanePlayground extends ApplicationAdapter implements InputProcessor, ControllerListener{
	SpriteBatch batch;
	Vector2 mousePositionScreen = new Vector2();
	Vector2 mousePositionStage = new Vector2();
	String screen = "menu";
	String back = "menu";
	PlayerCharacter player[] = new PlayerCharacter[4];
	Stage menu;
	UI gameName;
	int cursorPosition = 1;
	UI menuBackground;
	UI menuStartButton;
	UI menuSettingButton;
	UI menuHowToButton;
	UI menuExitButton;

	Stage character;
	int characterIndex[] = new int[4];
	UI characterBackground;
	UI playerCharacterSelect[];
	UI selectCharacterTop;
	UI charSelectStartButton, charSelectBackButton;
	Texture char1Info, char2Info, char3Info, char4Info;

	Stage game;
	float startDelay = 3;
	float delay = 3; // this is how much time before switch to end stage reset this in end stage
	GameObject playGround;
	UI gameBackground;
	UI armorBar[];
	UnbreakableWall walls[];
	NormalWall normalWalls[];
	EffectRenderer attackEffectRenderer[] = new EffectRenderer[4];
	EffectRenderer arrowEffectRenderer[] = new EffectRenderer[4];
	PlayerWeapon playerWeaponRenderer[] = new PlayerWeapon[4];
	GameObject checkBlock[] = new GameObject[4];
	ItemDrop itemDrop[];
	UI playerChargeBar[] = new UI[4];
	UI playerHPBar[] = new UI[4];
	UI playerArmorBar[] = new UI[4];
	UI playerShadow[] = new UI[4];
	Arrow playerArrow[];
	boolean arrowCharged[] = {false, false, false, false};
	UI playerSprite[];
	UI weaponSprite[];
	Texture noWeapon;
	Texture unbreakWall;
	BitmapFont font24;
	BitmapFont font32y;
	SpikeTrap spikeTrap[];
	WaterTrap waterTrap[];
	Balloon balloon[];
	Texture playground1, playground2, playground3, playground4;

	Stage howTo;
	UI howToBackground;
	UI howToBackButton;

	Stage end;
	UI endPlayerSprite[];
	UI platform;
	UI medal;
	UI endBackground;
	UI endRibbon;
	Balloon winnerBalloon;
	String winner;
	UI endBackButton;

	Stage pause;
	int pauseCursorPosition = 1;
	UI pauseBackground;
	UI pauseResumeButton, pauseSettingButton, pauseBackButton;

	Stage setting;
	UI settingBackground;
	boolean changeControl = false;
	Texture gray;
	int playerNumber = -1;
	String controlName = "";
	BitmapFont font32, font128, font128r;
	UI playerControlType[];
	UI playerButtonSetting[][];
	UI settingBackButton;
	int playerCount;
	
	// debug item drop
	ItemDrop debugItem;

	boolean axisMove = false;
	Texture menuStart1, menuStart2, menuSetting1, menuSetting2, menuHowTo1, menuHowTo2, menuExit1, menuExit2, back1, back2;
	Texture pauseResume1, pauseResume2, pauseSetting1, pauseSetting2, pauseBack1, pauseBack2;

	Music menuMusic, gameMusic, endMusic;
	Sound damagedSound, hpDownSound, deadSound, trapHitSound, parryArrowSound, healSound, collectSound, cursorSound, cancelSound, confirmSound, victorySound;
	Sound skill1Sound, skill2Sound, skill3Sound, skill4Sound;

	@Override
	public void create () {
		batch = new SpriteBatch();

		//create all stage
		menu = new Stage(new FitViewport(1350, 750));
		character = new Stage(new FitViewport(1350, 750));
		game = new Stage(new FitViewport(1350, 750));
		end = new Stage(new FitViewport(1350, 750));
		pause = new Stage(new FitViewport(1350, 750));
		setting = new Stage(new FitViewport(1350, 750));
		howTo = new Stage(new FitViewport(1350, 750));

		// set current screen to menu
		screen = "menu";

		// load sound
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
		skill1Sound = Gdx.audio.newSound(Gdx.files.internal("audio/skill1.ogg"));
		skill2Sound = Gdx.audio.newSound(Gdx.files.internal("audio/skill2.ogg"));
		skill3Sound = Gdx.audio.newSound(Gdx.files.internal("audio/skill3.ogg"));
		skill4Sound = Gdx.audio.newSound(Gdx.files.internal("audio/skill4.ogg"));

		debugItem = new ItemDrop();
		
		// create font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = Color.BLACK;
		font32 = generator.generateFont(parameter);
		parameter.size = 24;
		font24 = generator.generateFont(parameter);
		parameter.size = 128;
		parameter.color = Color.YELLOW;
		font128 = generator.generateFont(parameter);
		parameter.color = Color.RED;
		font128r = generator.generateFont(parameter);
		parameter.size = 32;
		parameter.color = Color.WHITE;
		font32y = generator.generateFont(parameter);
		generator.dispose();

		itemDrop = new ItemDrop[126];
		normalWalls = new NormalWall[126];
		spikeTrap = new SpikeTrap[4];
		waterTrap = new WaterTrap[4];
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
		createInHowToStage();

		// set listener to get input from this class
		Gdx.input.setInputProcessor(this);
		Controllers.addListener(this);
	}

	public void createInMenuStage()
	{
		// load and set everything in menu stage
		menuBackground = new UI("picture/whitebox.png", 0, 0, 1350, 750);
		TextureAtlas temp = new TextureAtlas(Gdx.files.internal("picture/menubackground.atlas"));
		menuBackground.animation = true;
		menuBackground.animationLoop = true;
		menuBackground.setAnimation(temp);
		menuBackground.currentAnim.setFrameDuration(0.2f);
		gameName = new UI("picture/whitebox.png", 270, 500, 822, 221);
		temp = new TextureAtlas(Gdx.files.internal("picture/gamename.atlas"));
		gameName.animation = true;
		gameName.animationLoop = true;
		gameName.setAnimation(temp);
		gameName.currentAnim.setFrameDuration(0.3f);
		menuStart1 = new Texture(Gdx.files.internal("picture/start_button_1.png"));
		menuStart2 = new Texture(Gdx.files.internal("picture/start_button_2.png"));
		menuSetting1 = new Texture(Gdx.files.internal("picture/setting_button_1.png"));
		menuSetting2 = new Texture(Gdx.files.internal("picture/setting_button_2.png"));
		menuHowTo1 = new Texture(Gdx.files.internal("picture/howtoplay_button_1.png"));
		menuHowTo2 = new Texture(Gdx.files.internal("picture/howtoplay_button_2.png"));
		menuExit1 = new Texture(Gdx.files.internal("picture/exit_button_1.png"));
		menuExit2 = new Texture(Gdx.files.internal("picture/exit_button_2.png"));
		back1 = new Texture(Gdx.files.internal("picture/back_button1.png"));
		back2 = new Texture(Gdx.files.internal("picture/back_button2.png"));
		menuStartButton = new UI("picture/start_button_2.png", 497, 138, 354, 93);
		menuSettingButton = new UI("picture/setting_button_1.png", 497, 30, 354, 93);
		menuHowToButton = new UI("picture/howtoplay_button_1.png", 917, 30, 354, 93);
		menuExitButton = new UI("picture/exit_button_1.png", 70, 30, 354, 93);

		// add actor to menu stage
		menu.addActor(menuBackground);
		menu.addActor(gameName);
		menu.addActor(menuStartButton);
		menu.addActor(menuSettingButton);
		menu.addActor(menuHowToButton);
		menu.addActor(menuExitButton);
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menumusic.ogg"));
		menuMusic.setLooping(true);
	}

	public void createInCharacterStage()
	{
		// load and set everything in character stage and something for each character
		charSelectStartButton = new UI("picture/start_button_1.png", 1175, 670, 165, 60);
		charSelectBackButton = new UI("picture/back_button1.png", 15, 670, 165, 60);
		weaponSprite = new UI[4];
		checkBlock[0] = new GameObject("picture/box3.png", 0, 0, 100, 100);
		checkBlock[1] = new GameObject("picture/box3.png", 0, 0, 100, 100);
		checkBlock[2] = new GameObject("picture/box3.png", 0, 0, 100, 100);
		checkBlock[3] = new GameObject("picture/box3.png", 0, 0, 100, 100);
		playerHPBar[0] = new UI("picture/heart.png", 100, 695, 150, 40);
		playerHPBar[1] = new UI("picture/heart.png", 430, 695, 150, 40);
		playerHPBar[2] = new UI("picture/heart.png", 760, 695, 150, 40);
		playerHPBar[3] = new UI("picture/heart.png", 1090, 695, 150, 40);
		playerArmorBar[0] = new UI ("picture/box3.png", 127, 670, 10, 15);
		playerArmorBar[1] = new UI ("picture/box3.png", 457, 670, 10, 15);
		playerArmorBar[2] = new UI ("picture/box3.png", 787, 670, 10, 15);
		playerArmorBar[3] = new UI ("picture/box3.png", 1117, 670, 10, 15);
		playerChargeBar[0] = new UI("picture/whitebox.png", 0, 0, 60, 10, true);
		playerChargeBar[1] = new UI("picture/whitebox.png", 0, 0, 60, 10, true);
		playerChargeBar[2] = new UI("picture/whitebox.png", 0, 0, 60, 10, true);
		playerChargeBar[3] = new UI("picture/whitebox.png", 0, 0, 60, 10, true);
		weaponSprite[0] = new UI ("picture/gray.png", 260, 685, 50, 50);
		weaponSprite[1] = new UI ("picture/gray.png", 590, 685, 50, 50);
		weaponSprite[2] = new UI ("picture/gray.png", 920, 685, 50, 50);
		weaponSprite[3] = new UI ("picture/gray.png", 1250, 685, 50, 50);
		armorBar = new UI[4];
		armorBar[0] = new UI("picture/armor_bar.png", 95, 665, 160, 24);
		armorBar[1] = new UI("picture/armor_bar.png", 425, 665, 160, 24);
		armorBar[2] = new UI("picture/armor_bar.png", 755, 665, 160, 24);
		armorBar[3] = new UI("picture/armor_bar.png", 1085, 665, 160, 24);
		noWeapon = new Texture(Gdx.files.internal("picture/gray.png"));
		player[0] = new PlayerCharacter(50, 50, 60, 60, Keys.W, Keys.S, Keys.A, Keys.D, Keys.F, Keys.Q, playerHPBar[0], playerArmorBar[0], PlayerWeapon.fist, PlayerWeapon.fistAnim);
		player[1] = new PlayerCharacter(1250, 550, 60, 60, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT, Keys.ALT_RIGHT, playerHPBar[1], playerArmorBar[1], PlayerWeapon.fist, PlayerWeapon.fistAnim);
		player[2] = new PlayerCharacter(50, 550, 60, 60, Keys.Y, Keys.H, Keys.G, Keys.J, Keys.U, Keys.T, playerHPBar[2], playerArmorBar[2], PlayerWeapon.fist, PlayerWeapon.fistAnim);
		player[3] = new PlayerCharacter(1250, 50, 60, 60, Keys.O, Keys.L, Keys.K, Keys.SEMICOLON, Keys.P, Keys.I, playerHPBar[3], playerArmorBar[3], PlayerWeapon.fist, PlayerWeapon.fistAnim);

		playerArrow = new Arrow[4];
		balloon = new Balloon[4];
		playerShadow[0] = new UI("picture/shadow1.png", -100, -100, 50, 50);
		playerShadow[1] = new UI("picture/shadow2.png", -100, -100, 50, 50);
		playerShadow[2] = new UI("picture/shadow3.png", -100, -100, 50, 50);
		playerShadow[3] = new UI("picture/shadow4.png", -100, -100, 50, 50);
		player[0].setShadow(playerShadow[0]);
		player[1].setShadow(playerShadow[1]);
		player[2].setShadow(playerShadow[2]);
		player[3].setShadow(playerShadow[3]);
		// set everything for each player
		for (int i = 0; i < 4; i++)
		{
			attackEffectRenderer[i] = new EffectRenderer(player[i]);
			arrowEffectRenderer[i] = new EffectRenderer(player[i]);
			attackEffectRenderer[i].setValue(player[i].attackHitbox.getX(), player[i].attackHitbox.getY(), player[i].attackHitbox.getWidth(), player[i].attackHitbox.getHeight(), player[i].direction);
			playerWeaponRenderer[i] = new PlayerWeapon(player[i]);
			player[i].setPlayerAttackEffectRenderer(attackEffectRenderer[i]);
			player[i].setPlayerWeaponRenderer(playerWeaponRenderer[i]);
			player[i].setPlayerArrowEffectRenderer(arrowEffectRenderer[i]);
			player[i].attackEffect.updateCurrentAnim(EffectRenderer.punchAnimation);
			player[i].arrowEffect.updateCurrentAnim(EffectRenderer.punchAnimation);
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
		char1Info = new Texture(Gdx.files.internal("picture/char1info.png"));
		char2Info = new Texture(Gdx.files.internal("picture/char2info.png"));
		char3Info = new Texture(Gdx.files.internal("picture/char3info.png"));
		char4Info = new Texture(Gdx.files.internal("picture/char4info.png"));

		// ui in character stage
		characterBackground = new UI("picture/characterbackground.png", 0, 0, 1350, 750);
		selectCharacterTop = new UI("picture/selectcharactertop.png", 372, 655, 606, 90);
		playerCharacterSelect =  new UI[4];
		playerCharacterSelect[0] = new UI("picture/char1info.png", 52, 105, 273, 548);
		playerCharacterSelect[1] = new UI("picture/char1info.png", 377, 105, 273, 548);
		playerCharacterSelect[2] = new UI("picture/char1info.png", 702, 105, 273, 548);
		playerCharacterSelect[3] = new UI("picture/char1info.png", 1027, 105, 273, 548);

		// add actor to character stage
		character.addActor(characterBackground);
		character.addActor(charSelectStartButton);
		character.addActor(charSelectBackButton);
		character.addActor(selectCharacterTop);
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
		// load and set everything in game stage
		gameBackground = new UI("picture/status_back.png", 0, 650, 1350, 100, false);
		playGround = new GameObject("picture/mapcastle.png", 0, 0, 1350, 650);
		playground1 = new Texture(Gdx.files.internal("picture/mapcastle.png"));
		playground2 = new Texture(Gdx.files.internal("picture/mapwood.png"));
		playground3 = new Texture(Gdx.files.internal("picture/mapice.png"));
		playground4 = new Texture(Gdx.files.internal("picture/maplava.png"));
		gameMusic  = Gdx.audio.newMusic(Gdx.files.internal("audio/gamemusic.ogg"));
		gameMusic.setLooping(true);

		playerSprite = new UI[4];
		playerSprite[0] = new UI("picture/whitebox.png", 30, 665, 60, 60);
		playerSprite[0].animation = true;
		playerSprite[0].setAnimation(PlayerCharacter.character1, "0001");
		playerSprite[1] = new UI("picture/whitebox.png", 360, 665, 60, 60);
		playerSprite[1].animation = true;
		playerSprite[1].setAnimation(PlayerCharacter.character1, "0001");
		playerSprite[2] = new UI("picture/whitebox.png", 690, 665, 60, 60);
		playerSprite[2].animation = true;
		playerSprite[2].setAnimation(PlayerCharacter.character1, "0001");
		playerSprite[3] = new UI("picture/whitebox.png", 1020, 665, 60, 60);
		playerSprite[3].animation = true;
		playerSprite[3].setAnimation(PlayerCharacter.character1, "0001");

		for (int i = 0; i < 4; i++)
		{
			spikeTrap[i] = new SpikeTrap(100, 100);
			waterTrap[i] = new WaterTrap(100, 100);
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
		game.addActor(armorBar[0]);
		game.addActor(armorBar[1]);
		game.addActor(armorBar[2]);
		game.addActor(armorBar[3]);

		game.addActor(spikeTrap[0]);
		game.addActor(spikeTrap[1]);
		game.addActor(spikeTrap[2]);
		game.addActor(spikeTrap[3]);
		game.addActor(waterTrap[0]);
		game.addActor(waterTrap[1]);
		game.addActor(waterTrap[2]);
		game.addActor(waterTrap[3]);

		// create unbreakable wall
		walls = new UnbreakableWall[136];
		int posX = 0;
		int posY = 0;
		int boxX = 50;
		int boxY = 50;
		for(int i=0;i<=26;i++) {
			walls[i] = new UnbreakableWall("picture/blockcastle.png", posX, posY, boxX, boxY);
			game.addActor(walls[i]);
			posX += 50;
		}

		posX = 0;
		posY = 600;
		for(int i=27;i<=53;i++) {
			walls[i] = new UnbreakableWall("picture/blockcastle.png", posX, posY, boxX, boxY);
			game.addActor(walls[i]);
			posX += 50;
		}

		posX = 0;
		posY = 50;
		for(int i=54;i<=64;i++) {
			walls[i] = new UnbreakableWall("picture/blockcastle.png", posX, posY, boxX, boxY);
			game.addActor(walls[i]);
			posY += 50;
		}

		posX = 1300;
		posY = 50;
		for(int i=65;i<=75;i++) {
			walls[i] = new UnbreakableWall("picture/blockcastle.png", posX, posY, boxX, boxY);
			game.addActor(walls[i]);
			posY += 50;
		}

		posX = 100;
		posY = 100;
		int num_count = 76;
		for(int i=0;i<5;i++) {
			for(int j=0;j<12;j++) {
				walls[j+num_count] = new UnbreakableWall("picture/blockcastle.png", posX, posY, boxX, boxY);
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
		game.addActor(debugItem);

		game.addActor(playerShadow[0]);
		game.addActor(playerShadow[1]);
		game.addActor(playerShadow[2]);
		game.addActor(playerShadow[3]);
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

		game.addActor(playerChargeBar[0]);
		game.addActor(playerChargeBar[1]);
		game.addActor(playerChargeBar[2]);
		game.addActor(playerChargeBar[3]);

		game.addActor(balloon[0]);
		game.addActor(balloon[1]);
		game.addActor(balloon[2]);
		game.addActor(balloon[3]);

		game.addActor(attackEffectRenderer[0]);
		game.addActor(attackEffectRenderer[1]);
		game.addActor(attackEffectRenderer[2]);
		game.addActor(attackEffectRenderer[3]);
		game.addActor(arrowEffectRenderer[0]);
		game.addActor(arrowEffectRenderer[1]);
		game.addActor(arrowEffectRenderer[2]);
		game.addActor(arrowEffectRenderer[3]);
	}

	public void createMap()
	{
		// set trap and normal wall position
		// random map type
		int num = (int)(Math.random()*4);
		if (num == 0)
		{
			NormalWall.wallTexture = NormalWall.wall1;
			playGround.img = playground1;
			unbreakWall = UnbreakableWall.wall1;
			NormalWall.currentBreakSound = NormalWall.wallBreakSound;
			WaterTrap.currentAnim = WaterTrap.waterAnim;
		}
		else if (num == 1)
		{
			NormalWall.wallTexture = NormalWall.wall2;
			playGround.img = playground2;
			unbreakWall = UnbreakableWall.wall2;
			NormalWall.currentBreakSound = NormalWall.wallBreakSound2;
			WaterTrap.currentAnim = WaterTrap.waterAnim2;
		}
		else if (num == 2)
		{
			NormalWall.wallTexture = NormalWall.wall3;
			playGround.img = playground3;
			unbreakWall = UnbreakableWall.wall3;
			NormalWall.currentBreakSound = NormalWall.wallBreakSound3;
			WaterTrap.currentAnim = WaterTrap.waterAnim3;
		}
		else if (num == 3)
		{
			NormalWall.wallTexture = NormalWall.wall4;
			playGround.img = playground4;
			unbreakWall = UnbreakableWall.wall4;
			NormalWall.currentBreakSound = NormalWall.wallBreakSound4;
			WaterTrap.currentAnim = WaterTrap.waterAnim4;
		}
		NormalWall.hp3 = NormalWall.wallTexture.findRegion("0001");
		NormalWall.hp2 = NormalWall.wallTexture.findRegion("0002");
		NormalWall.hp1 = NormalWall.wallTexture.findRegion("0003");
		NormalWall.hp0 = NormalWall.wallTexture.findRegion("0004");
		for (UnbreakableWall wall : walls)
		{
			wall.img = unbreakWall;
		}
		// random normal wall position
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
					normalWalls[normalWallCount] = new NormalWall(x, possibleY2[y[j]], 50, 50);
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
					normalWalls[normalWallCount] = new NormalWall(x, possibleY1[y[j]], 50, 50);
					game.addActor(normalWalls[normalWallCount]);
					normalWallCount += 1;
				}
			}
			x += 50;
		}
		// random spiketrap and watertrap position
		int spikeTrapCount = 0;
		int waterTrapCount = 0;
		float x2 = 100, y2 = 100;
		float savePositionX[] = {-1, -1, -1, -1, -1, -1, -1, -1};
		float savePositionY[] = {-1, -1, -1, -1, -1, -1, -1, -1};
		GameObject mapChecker = new GameObject("picture/whitebox.png", 100, 100, 40, 40);
		boolean skip = false;
		while (spikeTrapCount < 4 || waterTrapCount < 4)
		{
			for (int i = 0; i < 8; i++)
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
				if (spikeTrapCount < 4)
				{
					// set spiketrap here
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
				else if (waterTrapCount < 4)
				{
					if ((int)(Math.random()*20) == 0)
					{
						savePositionX[spikeTrapCount+waterTrapCount] = x2;
						savePositionY[spikeTrapCount+waterTrapCount] = y2;
						waterTrap[waterTrapCount].setX(x2);
						waterTrap[waterTrapCount].setY(y2);
						waterTrap[waterTrapCount].updateHitbox();
						waterTrapCount += 1;
					}
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
		// load and set everything in end stage
		TextureAtlas temp = new TextureAtlas(Gdx.files.internal("picture/endbackground.atlas"));
		endBackground = new UI("picture/whitebox.png", 0, 0, 1350, 750);
		endBackground.animation = true;
		endBackground.animationLoop = true;
		endBackground.setAnimation(temp);
		endBackground.currentAnim.setFrameDuration(0.5f);
		temp = new TextureAtlas(Gdx.files.internal("picture/endbackgroundribbon.atlas"));
		endRibbon = new UI("picture/whitebox.png", 0, 0,1350, 750);
		endRibbon.animation = true;
		endRibbon.animationLoop = true;
		endRibbon.setAnimation(temp);
		endRibbon.currentAnim.setFrameDuration(0.2f);
		endMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/endmusic.ogg"));
		endMusic.setLooping(true);
		endPlayerSprite = new UI[4];
		endPlayerSprite[0] = new UI("picture/char1lose.png", 250, 220, 80, 80);
		endPlayerSprite[1] = new UI("picture/char1lose.png", 500, 220, 80, 80);
		endPlayerSprite[2] = new UI("picture/char1lose.png", 750, 220, 80, 80);
		endPlayerSprite[3] = new UI("picture/char1lose.png", 1000, 220, 80, 80);
		endPlayerSprite[0].setVisible(false);
		endPlayerSprite[1].setVisible(false);
		endPlayerSprite[2].setVisible(false);
		endPlayerSprite[3].setVisible(false);
		platform = new UI("picture/podium.png", -100, -100, 200, 80);
		winnerBalloon = new Balloon();
		winnerBalloon.loop = true;
		winnerBalloon.onPlayer = false;
		winnerBalloon.setWidth(50);
		winnerBalloon.setHeight(50);
		medal = new UI("picture/medal.png", -100, -100, 60, 60);
		endBackButton = new UI("picture/back_button1.png", 50, 30, 161, 60);

		// add actor to end stage
		end.addActor(endBackground);
		end.addActor(endPlayerSprite[0]);
		end.addActor(endPlayerSprite[1]);
		end.addActor(endPlayerSprite[2]);
		end.addActor(endPlayerSprite[3]);
		end.addActor(platform);
		end.addActor(winnerBalloon);
		end.addActor(medal);
		end.addActor(endRibbon);
		end.addActor(endBackButton);
	}

	public void createInPauseStage()
	{
		// load and set everything in pause stage
		pauseBackground = new UI("picture/pausebackground.png", 0, 0, 1350, 750);
		pauseResumeButton = new UI("picture/resume_button2.png", 450, 500, 460, 100);
		pauseSettingButton = new UI("picture/setting_button1.png", 450, 350, 460, 100);
		pauseBackButton = new UI("picture/backtomenu_button1.png", 450, 200, 460, 100);
		
		pauseResume1 = new Texture(Gdx.files.internal("picture/resume_button1.png"));
		pauseResume2 = new Texture(Gdx.files.internal("picture/resume_button2.png"));
		pauseSetting1 = new Texture(Gdx.files.internal("picture/setting_button1.png"));
		pauseSetting2 = new Texture(Gdx.files.internal("picture/setting_button2.png"));
		pauseBack1 = new Texture(Gdx.files.internal("picture/backtomenu_button1.png"));
		pauseBack2 = new Texture(Gdx.files.internal("picture/backtomenu_button2.png"));

		// add actor in pause stage
		pause.addActor(pauseBackground);
		pause.addActor(pauseResumeButton);
		pause.addActor(pauseSettingButton);
		pause.addActor(pauseBackButton);
	}

	public void createInSettingStage()
	{
		// load and set everything in setting stage
		settingBackground = new UI("picture/settingbackground.png", 0, 0, 1350, 750);
		gray = new Texture(Gdx.files.internal("picture/gray.png"));
		playerControlType = new UI[4];
		playerControlType[0] = new UI("picture/whitebox.png", 303, 450, 230, 60);
		playerControlType[1] = new UI("picture/whitebox.png", 560, 450, 230, 60);
		playerControlType[2] = new UI("picture/whitebox.png", 817, 450, 230, 60);
		playerControlType[3] = new UI("picture/whitebox.png", 1074, 450, 230, 60);
		playerButtonSetting = new UI[4][6];
		settingBackButton = new UI("picture/back_button1.png", 19, 19, 161, 60);
		// set button position for each control
		float posX = 303, posY = 394;
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				playerButtonSetting[i][j] = new UI("picture/whitebox.png", posX, posY, 230, 56);
				posY -= 56;
			}
			posX += 257;
			posY = 394;
		}

		// add actor in setting stage
		setting.addActor(settingBackground);
		setting.addActor(settingBackButton);
	}

	public void createInHowToStage()
	{
		// load everything in howto stage
		howToBackground = new UI("picture/howtobackground.png", 0, 0, 1350, 750);
		howToBackButton = new UI("picture/back_button1.png", 19, 19, 160, 60);

		// add actor in howto stage
		howTo.addActor(howToBackground);
		howTo.addActor(howToBackButton);
	}

	@Override
	public void render() {
		// clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// set each stage size to current screen size
		game.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		menu.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		character.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		end.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		setting.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		pause.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		howTo.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		// render current stage
		if (screen.equals("menu"))
		{
			if (!menuMusic.isPlaying())
			{
				menuMusic.play();
				menuMusic.setVolume(0.2f);
			}
			gameMusic.stop();
			endMusic.stop();
			victorySound.stop();
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
			// render font
			if(playerSprite[0].isVisible())font24.draw(batch, "LV." + player[0].weaponLV, weaponSprite[0].getX(), weaponSprite[0].getY()-12);
			if(playerSprite[1].isVisible())font24.draw(batch, "LV." + player[1].weaponLV, weaponSprite[1].getX(), weaponSprite[1].getY()-12);
			if(playerSprite[2].isVisible())font24.draw(batch, "LV." + player[2].weaponLV, weaponSprite[2].getX(), weaponSprite[2].getY()-12);
			if(playerSprite[3].isVisible())font24.draw(batch, "LV." + player[3].weaponLV, weaponSprite[3].getX(), weaponSprite[3].getY()-12);
			// if game is over start countdown to go to end stage
			if (playerCount <= 1)
			{
				delay -= Gdx.graphics.getDeltaTime();
			}
			// render font at the start of the game
			if (startDelay > 0)
			{
				batch.draw(gray, 0, 0, 1350, 750);
				String numString = String.valueOf(startDelay);
				if (numString.length() >= 4)
					numString = new String(numString.substring(0, 4));
				font128.draw(batch, "Get Ready " + numString, 300, 500);
				if(player[0].isVisible())font32y.draw(batch, "Player1", player[0].getX()-20, player[0].getY()+90);
				if(player[1].isVisible())font32y.draw(batch, "Player2", player[1].getX()-20, player[1].getY()+90);
				if(player[2].isVisible())font32y.draw(batch, "Player3", player[2].getX()-20, player[2].getY()+90);
				if(player[3].isVisible())font32y.draw(batch, "Player4", player[3].getX()-20, player[3].getY()+90);
				startDelay -= Gdx.graphics.getDeltaTime();
			}
			batch.end();
			// after game is over and countdown is zero set everything that is needed in end stage 
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
							winner = "Player " + (numPlayerWin+1) + " Win !!!";
							platform.setX(endPlayerSprite[numPlayerWin].getX()-60);
							platform.setY(endPlayerSprite[numPlayerWin].getY());
							winnerBalloon.setX(endPlayerSprite[numPlayerWin].getX()+60);
							winnerBalloon.setY(endPlayerSprite[numPlayerWin].getY()+140);
							winnerBalloon.runAnimation("winner");
							medal.setX(platform.getX()+70);
							medal.setY(endPlayerSprite[numPlayerWin].getY()+170);
							endPlayerSprite[numPlayerWin].setY(endPlayerSprite[numPlayerWin].getY()+80);
							if (characterIndex[numPlayerWin] == 0)
							{
								endPlayerSprite[numPlayerWin].img = PlayerCharacter.character1Win;								
							}
							else if (characterIndex[numPlayerWin] == 1)
							{
								endPlayerSprite[numPlayerWin].img = PlayerCharacter.character2Win;								
							}
							else if (characterIndex[numPlayerWin] == 2)
							{
								endPlayerSprite[numPlayerWin].img = PlayerCharacter.character3Win;								
							}
							else if (characterIndex[numPlayerWin] == 3)
							{
								endPlayerSprite[numPlayerWin].img = PlayerCharacter.character4Win;								
							}
							break;
						}
					}
				}
				else
				{
					System.out.println("Draw!");
					winner = "Draw !!!";
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
		int weaponCount = 0;
		for (PlayerCharacter allPlayer : player) 
		{
			if (!allPlayer.moving || !allPlayer.isVisible() || allPlayer.dead)
			{
				weaponCount += 1;
				continue;
			}
			// if player is moving set regen cooldown to 1
			if (allPlayer.regenDelay < 1)
			{
				allPlayer.regenDelay = 1;
			}
			// if player is at its destination stop moving
			if (allPlayer.speed_x < 0)
			{
				if (allPlayer.hitbox.getX() <= allPlayer.checkBlock.hitbox.getX() && allPlayer.hitbox.getY() == allPlayer.checkBlock.hitbox.getY())
				{
					allPlayer.moving = false;
					allPlayer.speedUp = 0;
					allPlayer.speedDown = 0;
					allPlayer.speedLeft = 0;
					allPlayer.speedRight = 0;
					allPlayer.speed_x = 0;
					allPlayer.speed_y = 0;
					allPlayer.setX(allPlayer.checkBlock.hitbox.getX()-15);
					allPlayer.updateHitbox();
				}
			}
			if (allPlayer.speed_x > 0)
			{
				if (allPlayer.hitbox.getX() >= allPlayer.checkBlock.hitbox.getX() && allPlayer.hitbox.getY() == allPlayer.checkBlock.hitbox.getY())
				{
					allPlayer.moving = false;
					allPlayer.speedUp = 0;
					allPlayer.speedDown = 0;
					allPlayer.speedLeft = 0;
					allPlayer.speedRight = 0;
					allPlayer.speed_x = 0;
					allPlayer.speed_y = 0;
					allPlayer.setX(allPlayer.checkBlock.hitbox.getX()-15);
					allPlayer.updateHitbox();
				}
			}
			if (allPlayer.speed_y < 0)
			{
				if (allPlayer.hitbox.getX() == allPlayer.checkBlock.hitbox.getX() && allPlayer.hitbox.getY() <= allPlayer.checkBlock.hitbox.getY())
				{
					allPlayer.moving = false;
					allPlayer.speedUp = 0;
					allPlayer.speedDown = 0;
					allPlayer.speedLeft = 0;
					allPlayer.speedRight = 0;
					allPlayer.speed_x = 0;
					allPlayer.speed_y = 0;
					allPlayer.setY(allPlayer.checkBlock.hitbox.getY()-2);
					allPlayer.updateHitbox();
				}
			}
			if (allPlayer.speed_y > 0)
			{
				if (allPlayer.hitbox.getX() == allPlayer.checkBlock.hitbox.getX() && allPlayer.hitbox.getY() >= allPlayer.checkBlock.hitbox.getY())
				{
					allPlayer.moving = false;
					allPlayer.speedUp = 0;
					allPlayer.speedDown = 0;
					allPlayer.speedLeft = 0;
					allPlayer.speedRight = 0;
					allPlayer.speed_x = 0;
					allPlayer.speed_y = 0;
					allPlayer.setY(allPlayer.checkBlock.hitbox.getY()-2);
					allPlayer.updateHitbox();
				}
			}
			// check for player picking up debug item
			if (checkCollision(allPlayer, debugItem))
			{
				collectSound.play();
				debugItem.setVisible(false);
				if (debugItem.dropType.equals("weapon"))
				{
					//set all variable about weapon here
					allPlayer.updateNewWeapon(debugItem);
					debugItem.hitbox.setX(-1000);
					debugItem.hitbox.setY(-1000);
					weaponSprite[weaponCount].img = debugItem.img;
					if (allPlayer.currentChargeTime > allPlayer.attackChargeTime)
					{
						allPlayer.currentChargeTime = allPlayer.attackChargeTime;
					}
				}
				else if (debugItem.dropType.equals("powerup"))
				{
					if (debugItem.powerUpName.equals("life"))
					{
						if (allPlayer.hp < 3)
						{
							allPlayer.hp += 1;
						}
					}
					else if (debugItem.powerUpName.equals("shoe"))
					{
						allPlayer.speedBoostTime = 3f;
					}
					debugItem.hitbox.setX(-1000);
					debugItem.hitbox.setY(-1000);
				}
			}
			// check player picking up item
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
						if (item.dropType.equals("weapon"))
						{
							//set all variable about weapon here
							allPlayer.updateNewWeapon(item);
							item.hitbox.setX(-1000);
							item.hitbox.setY(-1000);
							weaponSprite[weaponCount].img = item.img;
							if (allPlayer.currentChargeTime > allPlayer.attackChargeTime)
							{
								allPlayer.currentChargeTime = allPlayer.attackChargeTime;
							}
						}
						else if (item.dropType.equals("powerup"))
						{
							if (item.powerUpName.equals("life"))
							{
								if (allPlayer.hp < 3)
								{
									allPlayer.hp += 1;
								}
							}
							else if (item.powerUpName.equals("shoe"))
							{
								allPlayer.speedBoostTime = 3f;
							}
							item.hitbox.setX(-1000);
							item.hitbox.setY(-1000);
						}
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
					allPlayer.balloon.runAnimation("trap");
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
			for (GameObject trap : waterTrap)
			{

				if (checkCollision(allPlayer, trap))
				{
					if (allPlayer.slowTime <= 0)
					{
						WaterTrap.waterTrapSound.play(0.5f);
					}
					allPlayer.slowTime = 2;
					allPlayer.balloon.runAnimation("trap");
				}
			}
		}
		int arrowCount = 0;
		int loopCount = 0;
		for (PlayerCharacter allPlayer : player) 
		{
			if (!allPlayer.isVisible() || allPlayer.dead)
			{
				loopCount += 1;
				arrowCount += 1;
				continue;
			}
			if (allPlayer.attacking)
			{
				if (allPlayer.weaponName.equals("bow"))
				{
					if (!allPlayer.arrow.isVisible())
					{
						allPlayer.attackSound.play(0.4f);
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
				checkArrowAttack(allPlayer, arrowCount, arrowCount2);
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
					allPlayer.speedLeft = allPlayer.playerMoveSpeed;
					allPlayer.direction = "left";
					allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX()-50, allPlayer.hitbox.getY(), allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					allPlayer.moving = true;
				}
				if (allPlayer.rightPressed)
				{
					allPlayer.speedRight = allPlayer.playerMoveSpeed;
					allPlayer.direction = "right";
					allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX()+50, allPlayer.hitbox.getY(), allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					allPlayer.moving = true;
				}
				if (allPlayer.upPressed)
				{
					allPlayer.speedUp = allPlayer.playerMoveSpeed;
					allPlayer.direction = "up";
					allPlayer.updateCheckBlockPosition(allPlayer.hitbox.getX(), allPlayer.hitbox.getY()+50, allPlayer.hitbox.getWidth(), allPlayer.hitbox.getHeight());
					allPlayer.moving = true;
				}
				if (allPlayer.downPressed)
				{
					allPlayer.speedDown = allPlayer.playerMoveSpeed;
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
				if (allPlayer.speedBoostTime > 0)
				{
					allPlayer.speed_x *= 2;
					allPlayer.speed_y *= 2;
				}
			}
		}
	}

	public void endStageRender()
	{
		end.draw();
		float centerShift = winner.length()*25;
		batch.begin();
		font128.draw(batch, winner, end.getWidth()/2-centerShift, 630);
		batch.end();
	}

	public void pauseStageRender()
	{
		pause.draw();
	}

	public void settingStageRender()
	{
		setting.draw();
		batch.begin();
		for (int i = 0; i < 4; i++)
		{
			float centerShift = 0;
			String controlType = "Keyboard";
			centerShift = controlType.length()*8;
			if (player[i].controllerCount == 0)
			{
				controlType = "Controller1";
				centerShift = controlType.length()*7;
			}
			else if (player[i].controllerCount == 1)
			{
				controlType = "Controller2";
				centerShift = controlType.length()*7;
			}
			else if (player[i].controllerCount == 2)
			{
				controlType = "Controller3";
				centerShift = controlType.length()*7;
			}
			else if (player[i].controllerCount == 3)
			{
				controlType = "Controller4";
				centerShift = controlType.length()*7;
			}
			font32.draw(batch, controlType, playerControlType[i].getX()+playerControlType[i].getWidth()/2-centerShift, playerControlType[i].getY()+playerControlType[i].getHeight()/2);
			if (player[i].controlType.equals("keyboard"))
			{
				centerShift = Keys.toString(player[i].controlUp).length()*7;
				font32.draw(batch, Keys.toString(player[i].controlUp), playerButtonSetting[i][0].getX()+(playerButtonSetting[i][0].getWidth()/2)-centerShift, playerButtonSetting[i][0].getY()+playerButtonSetting[i][0].getHeight()/2);
				centerShift = Keys.toString(player[i].controlDown).length()*7;
				font32.draw(batch, Keys.toString(player[i].controlDown), playerButtonSetting[i][1].getX()+(playerButtonSetting[i][1].getWidth()/2)-centerShift, playerButtonSetting[i][1].getY()+playerButtonSetting[i][1].getHeight()/2);
				centerShift = Keys.toString(player[i].controlLeft).length()*7;
				font32.draw(batch, Keys.toString(player[i].controlLeft), playerButtonSetting[i][2].getX()+(playerButtonSetting[i][2].getWidth()/2)-centerShift, playerButtonSetting[i][2].getY()+playerButtonSetting[i][2].getHeight()/2);
				centerShift = Keys.toString(player[i].controlRight).length()*7;
				font32.draw(batch, Keys.toString(player[i].controlRight), playerButtonSetting[i][3].getX()+(playerButtonSetting[i][3].getWidth()/2)-centerShift, playerButtonSetting[i][3].getY()+playerButtonSetting[i][3].getHeight()/2);
				centerShift = Keys.toString(player[i].controlAttack).length()*7;
				font32.draw(batch, Keys.toString(player[i].controlAttack), playerButtonSetting[i][4].getX()+(playerButtonSetting[i][4].getWidth()/2)-centerShift, playerButtonSetting[i][4].getY()+playerButtonSetting[i][4].getHeight()/2);
				centerShift = Keys.toString(player[i].controlBack).length()*7;
				font32.draw(batch, Keys.toString(player[i].controlBack), playerButtonSetting[i][5].getX()+(playerButtonSetting[i][5].getWidth()/2)-centerShift, playerButtonSetting[i][5].getY()+playerButtonSetting[i][5].getHeight()/2);
			}
			else
			{
				centerShift = Integer.toString(player[i].controlUp).length()*7;
				font32.draw(batch, Integer.toString(player[i].controlUp), playerButtonSetting[i][0].getX()+(playerButtonSetting[i][1].getWidth()/2)-centerShift, playerButtonSetting[i][0].getY()+playerButtonSetting[i][0].getHeight()/2);
				centerShift = Integer.toString(player[i].controlDown).length()*7;
				font32.draw(batch, Integer.toString(player[i].controlDown), playerButtonSetting[i][1].getX()+(playerButtonSetting[i][1].getWidth()/2)-centerShift, playerButtonSetting[i][1].getY()+playerButtonSetting[i][1].getHeight()/2);
				centerShift = Integer.toString(player[i].controlLeft).length()*7;
				font32.draw(batch, Integer.toString(player[i].controlLeft), playerButtonSetting[i][2].getX()+(playerButtonSetting[i][2].getWidth()/2)-centerShift, playerButtonSetting[i][2].getY()+playerButtonSetting[i][2].getHeight()/2);
				centerShift = Integer.toString(player[i].controlRight).length()*7;
				font32.draw(batch, Integer.toString(player[i].controlRight), playerButtonSetting[i][3].getX()+(playerButtonSetting[i][3].getWidth()/2)-centerShift, playerButtonSetting[i][3].getY()+playerButtonSetting[i][3].getHeight()/2);
				centerShift = Integer.toString(player[i].controlAttack).length()*7;
				font32.draw(batch, Integer.toString(player[i].controlAttack), playerButtonSetting[i][4].getX()+(playerButtonSetting[i][4].getWidth()/2)-centerShift, playerButtonSetting[i][4].getY()+playerButtonSetting[i][4].getHeight()/2);
				centerShift = Integer.toString(player[i].controlBack).length()*7;
				font32.draw(batch, Integer.toString(player[i].controlBack), playerButtonSetting[i][5].getX()+(playerButtonSetting[i][5].getWidth()/2)-centerShift, playerButtonSetting[i][5].getY()+playerButtonSetting[i][5].getHeight()/2);
			}
		}
		if(changeControl)
		{
			batch.draw(gray, 0, 0, 1350, 750);
			font128.draw(batch, "press button to change\n" + "\nor press esc to cancel", 50, 600);
			float locationX;
			if (controlName.equals("right"))
			{
				locationX = 90;
			}
			else if (controlName.equals("down"))
			{
				locationX = 80;
			}
			else if (controlName.equals("attack"))
			{
				locationX = 50;
			}
			else if (controlName.equals("back"))
			{
				locationX = 110;
			}
			else
			{
				locationX = 150-(controlName.length()*8);
			}
			font128r.draw(batch, "Player " + (playerNumber+1) + " " + controlName + " control", locationX, 430);
		}
		batch.end();
	}

	public void howToStageRender()
	{
		howTo.draw();
	}

	@Override
	public void dispose () {
		// clear memory when game is closed
		batch.dispose();
		menu.dispose();
		character.dispose();
		game.dispose();
		end.dispose();
		setting.dispose();
		pause.dispose();
		howTo.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// handle input for current stage
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
		// handle enter input
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
		// handle input for each player control
		for (PlayerCharacter allPlayer : player)
		{
			if (keycode == allPlayer.controlRight)
			{
				if (cursorPosition == 1)
				{
					cursorPosition = 3;
					cursorSound.play();
				}
				else if (cursorPosition == 2)
				{
					cursorPosition = 3;
					cursorSound.play();
				}
				else if (cursorPosition == 3)
				{
					cursorPosition = 4;
					cursorSound.play();
				}
				else if (cursorPosition == 4)
				{
					cursorPosition = 2;
					cursorSound.play();
				}
			}
			if (keycode == allPlayer.controlLeft)
			{
				if (cursorPosition == 1)
				{
					cursorPosition = 4;
					cursorSound.play();
				}
				else if (cursorPosition == 2)
				{
					cursorPosition = 4;
					cursorSound.play();
				}
				else if (cursorPosition == 3)
				{
					cursorPosition = 2;
					cursorSound.play();
				}
				else if (cursorPosition == 4)
				{
					cursorPosition = 3;
					cursorSound.play();
				}
			}
			if (keycode == allPlayer.controlDown)
			{
				if (cursorPosition == 1)
				{
					cursorPosition = 2;
					cursorSound.play();
				}
				else if (cursorPosition == 2)
				{
					cursorPosition = 1;
					cursorSound.play();
				}
				else if (cursorPosition == 3)
				{
					cursorPosition = 1;
					cursorSound.play();
				}
				else if (cursorPosition == 4)
				{
					cursorPosition = 1;
					cursorSound.play();
				}
			}
			else if (keycode == allPlayer.controlUp)
			{
				if (cursorPosition == 1)
				{
					cursorPosition = 2;
					cursorSound.play();
				}
				else if (cursorPosition == 2)
				{
					cursorPosition = 1;
					cursorSound.play();
				}
				else if (cursorPosition == 3)
				{
					cursorPosition = 1;
					cursorSound.play();
				}
				else if (cursorPosition == 4)
				{
					cursorPosition = 1;
					cursorSound.play();
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
		// set img of each button
		if (cursorPosition == 1)
		{
			menuStartButton.img = menuStart2;
			menuHowToButton.img = menuHowTo1;
			menuSettingButton.img = menuSetting1;
			menuExitButton.img = menuExit1;
		}
		else if (cursorPosition == 2)
		{
			menuStartButton.img = menuStart1;
			menuHowToButton.img = menuHowTo1;
			menuSettingButton.img = menuSetting2;
			menuExitButton.img = menuExit1;
		}
		else if (cursorPosition == 3)
		{
			menuStartButton.img = menuStart1;
			menuHowToButton.img = menuHowTo2;
			menuSettingButton.img = menuSetting1;
			menuExitButton.img = menuExit1;
		}
		else if (cursorPosition == 4)
		{
			menuStartButton.img = menuStart1;
			menuHowToButton.img = menuHowTo1;
			menuSettingButton.img = menuSetting1;
			menuExitButton.img = menuExit2;
		}
	}

	public void keyDownInCharacterStage(int keycode)
	{
		// handle enter input, go to game stage if there is enough player
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
					endPlayerSprite[i].setVisible(false);
					armorBar[i].setVisible(false);
					continue;
				}
				else
				{
					playerSprite[i].setVisible(true);
					playerSprite[i].setAnimation(player[i].walkingAtlas, "0001");
					weaponSprite[i].setVisible(true);
					endPlayerSprite[i].setVisible(true);
					armorBar[i].setVisible(true);
				}
			}
		}
		// return to menu stage
		else if (keycode == Keys.ESCAPE)
		{
			screen = "menu";
			cancelSound.play();
			resetVariableInCharacterStage();
		}
		// handle input for each player control
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
					if (characterIndex[i]-1 >= 0)
					{
						characterIndex[i] -= 1;
						cursorSound.play();
					}
				}
				else if (keycode == player[i].controlRight)
				{
					if (characterIndex[i]+1 < 4)// change 4 to number of character texture here
					{
						characterIndex[i] += 1;
						cursorSound.play();
					}
				}
				if (characterIndex[i] == 0)
				{
					playerCharacterSelect[i].img = char1Info;
					player[i].setTexture(PlayerCharacter.character1, PlayerCharacter.character1Dead);
					endPlayerSprite[i].img = PlayerCharacter.character1Lose;
				}
				else if(characterIndex[i] == 1)
				{
					playerCharacterSelect[i].img = char2Info;
					player[i].setTexture(PlayerCharacter.character2, PlayerCharacter.character2Dead);
					endPlayerSprite[i].img = PlayerCharacter.character2Lose;
				}
				else if(characterIndex[i] == 2)
				{
					playerCharacterSelect[i].img = char3Info;
					player[i].setTexture(PlayerCharacter.character3, PlayerCharacter.character3Dead);
					endPlayerSprite[i].img = PlayerCharacter.character3Lose;
				}
				else if(characterIndex[i] == 3)
				{
					playerCharacterSelect[i].img = char4Info;
					player[i].setTexture(PlayerCharacter.character4, PlayerCharacter.character4Dead);
					endPlayerSprite[i].img = PlayerCharacter.character4Lose;
				}
			}
		}
	}

	public void keyDownInGameStage(int keycode)
	{
		// pause game
		if (keycode == Keys.ESCAPE)
		{
			screen = "pause";
			cancelSound.play();
		}
		// handle control for each player
		for (PlayerCharacter allPlayer : player) {
			//			if (allPlayer.charging)
			//			{
			//				continue;
			//			}// comment this make player able to change direction of attack while charging}
			if (allPlayer.dead  || playerCount <= 1 || !allPlayer.isVisible() || startDelay > 0)
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
		// return to menu
		if (keycode == Keys.ENTER || keycode == Keys.ESCAPE)
		{
			screen = "menu";
			resetVariableInCharacterStage();
			resetVariableInGameStage();
		}
		for (int i = 0; i<4; i++)
		{
			if (!player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (keycode == player[i].controlBack || keycode == player[i].controlAttack)
			{
				screen = "menu";
				resetVariableInCharacterStage();
				resetVariableInGameStage();
			}
		}
	}

	public void keyDownInPauseStage(int keycode)
	{
		// continue game
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
		// handle enter input
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
		// handle input for each player
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
		// set button img according to current select button
		if (pauseCursorPosition == 1)
		{
			pauseResumeButton.img = pauseResume2;
			pauseSettingButton.img = pauseSetting1;
			pauseBackButton.img = pauseBack1;
		}
		else if (pauseCursorPosition == 2)
		{
			pauseResumeButton.img = pauseResume1;
			pauseSettingButton.img = pauseSetting2;
			pauseBackButton.img = pauseBack1;
		}
		else if (pauseCursorPosition == 3)
		{
			pauseResumeButton.img = pauseResume1;
			pauseSettingButton.img = pauseSetting1;
			pauseBackButton.img = pauseBack2;
		}
	}

	public void keyDownInSettingStage(int keycode)
	{
		// if changing control change button value in that player
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
		// if not changing control
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
		// handle control for each player
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
		// temporary debug command
		if (character == '�')
		{
			//player[0].dead = true;
			player[1].dead = true;
			player[2].dead = true;
			player[3].dead = true;
			playerCount = 1;
		}
		else if (character == '�')
		{
			player[0].armor -= 5;
			if (player[0].armor <= 0)
			{
				player[0].hp -=1;
			}
			player[0].regenDelay = 2;
		}
		else if (character == '�')
		{
			debugItem.img = ItemDrop.swordDropTexture;
			debugItem.weaponName = "sword";
			debugItem.dropType = "weapon";
			//lv1
			debugItem.attackWidth[0] = ItemDrop.SWORD_ATTACK_WIDTH;
			debugItem.attackHeight[0] = ItemDrop.SWORD_ATTACK_HEIGHT;
			debugItem.attackChargeTime[0] = ItemDrop.SWORD_CHARGE_TIME;
			debugItem.attack[0] = ItemDrop.SWORD_ATTACK;
			debugItem.effectAtlas = EffectRenderer.swordEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.swordAnimation;
			debugItem.weaponAtlas[0] = PlayerWeapon.sword;
			debugItem.weaponAnimation = PlayerWeapon.swordAnim;
			//lv2
			debugItem.attackWidth[1] = ItemDrop.SWORDLV2_ATTACK_WIDTH;
			debugItem.attackHeight[1] = ItemDrop.SWORDLV2_ATTACK_HEIGHT;
			debugItem.attackChargeTime[1] = ItemDrop.SWORDLV2_CHARGE_TIME;
			debugItem.attack[1] = ItemDrop.SWORDLV2_ATTACK;
			debugItem.effectAtlas = EffectRenderer.swordEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.swordAnimation;
			debugItem.weaponAtlas[1] = PlayerWeapon.swordLV2;
			debugItem.weaponLV2Animation = PlayerWeapon.swordLV2Anim;
			//lv3
			debugItem.attackWidth[2] = ItemDrop.SWORDLV3_ATTACK_WIDTH;
			debugItem.attackHeight[2] = ItemDrop.SWORDLV3_ATTACK_HEIGHT;
			debugItem.attackChargeTime[2] = ItemDrop.SWORDLV3_CHARGE_TIME;
			debugItem.attack[2] = ItemDrop.SWORDLV3_ATTACK;
			debugItem.effectAtlas = EffectRenderer.swordEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.swordAnimation;
			debugItem.weaponAtlas[2] = PlayerWeapon.swordLV3;
			debugItem.weaponLV3Animation = PlayerWeapon.swordLV3Anim;
			debugItem.weaponSound = PlayerWeapon.swordSound;
			debugItem.setX(60);
			debugItem.setY(60);
			debugItem.hitbox.setX(debugItem.getX());
			debugItem.hitbox.setY(debugItem.getY());
			debugItem.dropped = true;
			debugItem.setVisible(true);
		}
		else if (character == '�')
		{
			debugItem.img = ItemDrop.spearDropTexture;
			debugItem.weaponName = "spear";
			debugItem.dropType = "weapon";
			//lv1
			debugItem.attackWidth[0] = ItemDrop.SPEAR_ATTACK_WIDTH;
			debugItem.attackHeight[0] = ItemDrop.SPEAR_ATTACK_HEIGHT;
			debugItem.attackChargeTime[0] = ItemDrop.SPEAR_CHARGE_TIME;
			debugItem.attack[0] = ItemDrop.SPEAR_ATTACK;
			debugItem.effectAtlas = EffectRenderer.spearEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.spearAnimation;
			debugItem.weaponAtlas[0] = PlayerWeapon.spear;
			debugItem.weaponAnimation = PlayerWeapon.spearAnim;
			//lv2
			debugItem.attackWidth[1] = ItemDrop.SPEARLV2_ATTACK_WIDTH;
			debugItem.attackHeight[1] = ItemDrop.SPEARLV2_ATTACK_HEIGHT;
			debugItem.attackChargeTime[1] = ItemDrop.SPEARLV2_CHARGE_TIME;
			debugItem.attack[1] = ItemDrop.SPEARLV2_ATTACK;
			debugItem.effectAtlas = EffectRenderer.spearEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.spearAnimation;
			debugItem.weaponAtlas[1] = PlayerWeapon.spearLV2;
			debugItem.weaponLV2Animation = PlayerWeapon.spearLV2Anim;
			//lv3
			debugItem.attackWidth[2] = ItemDrop.SPEARLV3_ATTACK_WIDTH;
			debugItem.attackHeight[2] = ItemDrop.SPEARLV3_ATTACK_HEIGHT;
			debugItem.attackChargeTime[2] = ItemDrop.SPEARLV3_CHARGE_TIME;
			debugItem.attack[2] = ItemDrop.SPEARLV3_ATTACK;
			debugItem.effectAtlas = EffectRenderer.spearEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.spearAnimation;
			debugItem.weaponAtlas[2] = PlayerWeapon.spearLV3;
			debugItem.weaponLV3Animation = PlayerWeapon.spearLV3Anim;
			debugItem.weaponSound = PlayerWeapon.spearSound;
			debugItem.setX(60);
			debugItem.setY(60);
			debugItem.hitbox.setX(debugItem.getX());
			debugItem.hitbox.setY(debugItem.getY());
			debugItem.dropped = true;
			debugItem.setVisible(true);
		}
		else if (character == '�')
		{
			debugItem.img = ItemDrop.axeDropTexture;
			debugItem.weaponName = "axe";
			debugItem.dropType = "weapon";
			//lv1
			debugItem.attackWidth[0] = ItemDrop.AXE_ATTACK_WIDTH;
			debugItem.attackHeight[0] = ItemDrop.AXE_ATTACK_HEIGHT;
			debugItem.attackChargeTime[0] = ItemDrop.AXE_CHARGE_TIME;
			debugItem.attack[0] = ItemDrop.AXE_ATTACK;
			debugItem.effectAtlas = EffectRenderer.swordEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.swordAnimation;
			debugItem.weaponAtlas[0] = PlayerWeapon.axe;
			debugItem.weaponAnimation = PlayerWeapon.axeAnim;
			//lv2
			debugItem.attackWidth[1] = ItemDrop.AXELV2_ATTACK_WIDTH;
			debugItem.attackHeight[1] = ItemDrop.AXELV2_ATTACK_HEIGHT;
			debugItem.attackChargeTime[1] = ItemDrop.AXELV2_CHARGE_TIME;
			debugItem.attack[1] = ItemDrop.AXELV2_ATTACK;
			debugItem.effectAtlas = EffectRenderer.swordEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.swordAnimation;
			debugItem.weaponAtlas[1] = PlayerWeapon.axeLV2;
			debugItem.weaponLV2Animation = PlayerWeapon.axeLV2Anim;
			//lv3
			debugItem.attackWidth[2] = ItemDrop.AXELV3_ATTACK_WIDTH;
			debugItem.attackHeight[2] = ItemDrop.AXELV3_ATTACK_HEIGHT;
			debugItem.attackChargeTime[2] = ItemDrop.AXELV3_CHARGE_TIME;
			debugItem.attack[2] = ItemDrop.AXELV3_ATTACK;
			debugItem.effectAtlas = EffectRenderer.swordEffectAtlas;
			debugItem.effectAnimation = EffectRenderer.swordAnimation;
			debugItem.weaponAtlas[2] = PlayerWeapon.axeLV3;
			debugItem.weaponLV3Animation = PlayerWeapon.axeLV3Anim;
			debugItem.weaponSound = PlayerWeapon.axeSound;
			debugItem.setX(60);
			debugItem.setY(60);
			debugItem.hitbox.setX(debugItem.getX());
			debugItem.hitbox.setY(debugItem.getY());
			debugItem.dropped = true;
			debugItem.setVisible(true);
		}
		else if (character == '�')
		{
			debugItem.img = ItemDrop.bowDropTexture;
			debugItem.weaponName = "bow";
			debugItem.dropType = "weapon";
			//lv1
			debugItem.attackWidth[0] = ItemDrop.BOW_ATTACK_WIDTH;
			debugItem.attackHeight[0] = ItemDrop.BOW_ATTACK_HEIGHT;
			debugItem.attackChargeTime[0] = ItemDrop.BOW_CHARGE_TIME;
			debugItem.attack[0] = ItemDrop.BOW_ATTACK;
			debugItem.effectAtlas = EffectRenderer.punchAtlas;
			debugItem.effectAnimation = EffectRenderer.punchAnimation;
			debugItem.weaponAtlas[0] = PlayerWeapon.bow;
			debugItem.weaponAnimation = PlayerWeapon.bowAnim;
			//lv2
			debugItem.attackWidth[1] = ItemDrop.BOWLV2_ATTACK_WIDTH;
			debugItem.attackHeight[1] = ItemDrop.BOWLV2_ATTACK_HEIGHT;
			debugItem.attackChargeTime[1] = ItemDrop.BOWLV2_CHARGE_TIME;
			debugItem.attack[1] = ItemDrop.BOWLV2_ATTACK;
			debugItem.effectAtlas = EffectRenderer.punchAtlas;
			debugItem.effectAnimation = EffectRenderer.punchAnimation;
			debugItem.weaponAtlas[1] = PlayerWeapon.bowLV2;
			debugItem.weaponLV2Animation = PlayerWeapon.bowLV2Anim;
			//lv3
			debugItem.attackWidth[2] = ItemDrop.BOWLV3_ATTACK_WIDTH;
			debugItem.attackHeight[2] = ItemDrop.BOWLV3_ATTACK_HEIGHT;
			debugItem.attackChargeTime[2] = ItemDrop.BOWLV3_CHARGE_TIME;
			debugItem.attack[2] = ItemDrop.BOWLV3_ATTACK;
			debugItem.effectAtlas = EffectRenderer.punchAtlas;
			debugItem.effectAnimation = EffectRenderer.punchAnimation;
			debugItem.weaponAtlas[2] = PlayerWeapon.bowLV3;
			debugItem.weaponLV3Animation = PlayerWeapon.bowLV3Anim;
			debugItem.weaponSound = PlayerWeapon.bowSound;
			debugItem.setX(60);
			debugItem.setY(60);
			debugItem.hitbox.setX(debugItem.getX());
			debugItem.hitbox.setY(debugItem.getY());
			debugItem.dropped = true;
			debugItem.setVisible(true);
		}
		else if (character == '�')
		{
			debugItem.dropType = "powerup";
			debugItem.img = ItemDrop.lifeDropTexture;
			debugItem.powerUpName = "life";
			debugItem.setX(60);
			debugItem.setY(60);
			debugItem.hitbox.setX(debugItem.getX());
			debugItem.hitbox.setY(debugItem.getY());
			debugItem.dropped = true;
			debugItem.setVisible(true);
		}
		else if (character == '�')
		{
			debugItem.dropType = "powerup";
			debugItem.img = ItemDrop.shoeDropTexture;
			debugItem.powerUpName = "shoe";
			debugItem.setX(60);
			debugItem.setY(60);
			debugItem.hitbox.setX(debugItem.getX());
			debugItem.hitbox.setY(debugItem.getY());
			debugItem.dropped = true;
			debugItem.setVisible(true);
		}
		else if (character == '�')
		{
			for (NormalWall wall : normalWalls) {
				if (wall.getX() == 50 || wall.getX() == 1250 || wall.getY() == 50 || wall.getY() == 550)
				{
					wall.hitbox.setX(-1000);
					wall.hitbox.setY(-1000);
					wall.setVisible(false);
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mousePositionScreen.x = screenX;
		mousePositionScreen.y = screenY;
		// convert y axis from screen coordinate to stage coordinate
		mousePositionStage = menu.screenToStageCoordinates(mousePositionScreen);
//		System.out.println(mousePositionStage.x + " " + mousePositionStage.y);
		if (screen.equals("menu"))
		{
			touchDownInMenuStage(mousePositionStage, button);
		}
		else if (screen.equals("setting"))
		{
			touchDownInSettingStage(mousePositionStage, button);
		}
		else if (screen.equals("character"))
		{
			touchDownInCharacterStage(mousePositionStage, button);
		}
		else if (screen.equals("howto"))
		{
			touchDownInHowToStage(mousePositionStage, button);
		}
		else if (screen.equals("pause"))
		{
			touchDownInPauseStage(mousePositionStage, button);
		}
		else if (screen.equals("end"))
		{
			touchDownInEndStage(mousePositionStage, button);
		}
		return false;
	}

	public void touchDownInMenuStage(Vector2 mousePosition, int button)
	{
		// check for clicking button
		if (button == Buttons.LEFT)
		{
			if (mousePosition.x >= menuStartButton.getX() && mousePosition.x <= menuStartButton.getX()+menuStartButton.getWidth())
			{
				if (mousePosition.y >= menuStartButton.getY() && mousePosition.y <= menuStartButton.getY()+menuStartButton.getHeight())
				{
					screen = "character";
					confirmSound.play();
				}
			}
			if (mousePosition.x >= menuSettingButton.getX() && mousePosition.x <= menuSettingButton.getX()+menuSettingButton.getWidth())
			{
				if (mousePosition.y >= menuSettingButton.getY() && mousePosition.y <= menuSettingButton.getY()+menuSettingButton.getHeight())
				{
					screen = "setting";
					back = "menu";
					confirmSound.play();
				}
			}
			if (mousePosition.x >= menuHowToButton.getX() && mousePosition.x <= menuHowToButton.getX()+menuHowToButton.getWidth())
			{
				if (mousePosition.y >= menuHowToButton.getY() && mousePosition.y <= menuHowToButton.getY()+menuHowToButton.getHeight())
				{
					screen = "howto";
					confirmSound.play();
				}
			}
			if (mousePosition.x >= menuExitButton.getX() && mousePosition.x <= menuExitButton.getX()+menuExitButton.getWidth())
			{
				if (mousePosition.y >= menuExitButton.getY() && mousePosition.y <= menuExitButton.getY()+menuExitButton.getHeight())
				{
					Gdx.app.exit();
				}
			}
		}
	}

	public void touchDownInSettingStage(Vector2 mousePosition, int button)
	{
		// check for clicking button
		if (button == Buttons.LEFT && !changeControl)
		{
			for(int i = 0; i < 4; i++)
			{
				for(int j = 0; j < 6; j++)
				{
					float buttonX = playerButtonSetting[i][j].getX();
					float buttonXWidth = buttonX+playerButtonSetting[i][j].getWidth();
					float buttonY = playerButtonSetting[i][j].getY();
					float buttonYHeight = buttonY+playerButtonSetting[i][j].getHeight();
					if (mousePosition.x >= buttonX && mousePosition.x <= buttonXWidth)
					{
						if (mousePosition.y >= buttonY && mousePosition.y <= buttonYHeight)
						{
							changeControl = true;
							playerNumber = i;
							confirmSound.play();
							if (j == 0)
							{
								controlName = "up";	
							}
							else if (j == 1)
							{
								controlName = "down";
							}
							else if (j == 2)
							{
								controlName = "left";
							}
							else if (j == 3)
							{
								controlName = "right";
							}
							else if (j == 4)
							{
								controlName = "attack";
							}
							else if (j == 5)
							{
								controlName = "back";
							}
						}
					}
				}
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
							player[i].controllerCount = 0;
							// set string to represent controltype here
							confirmSound.play();
						}
						else if (player[i].controlType.equals("controller1"))
						{
							player[i].controlType = "controller2";
							player[i].controllerCount = 1;
							confirmSound.play();
						}
						else if (player[i].controlType.equals("controller2"))
						{
							player[i].controlType = "controller3";
							player[i].controllerCount = 2;
							confirmSound.play();
						}
						else if (player[i].controlType.equals("controller3"))
						{
							player[i].controlType = "controller4";
							player[i].controllerCount = 3;
							confirmSound.play();
						}
						else if (player[i].controlType.equals("controller4"))
						{
							player[i].controlType = "keyboard";
							player[i].controllerCount = -1;
							confirmSound.play();
						}
					}
				}
			}
			// check click back to menu button
			if (mousePosition.x >= settingBackButton.getX() && mousePosition.x <= settingBackButton.getX()+settingBackButton.getWidth())
			{
				if (mousePosition.y >= settingBackButton.getY() && mousePosition.y <= settingBackButton.getY()+settingBackButton.getHeight())
				{
					cancelSound.play();
					screen = back;
				}
			}
		}// if button left
	}

	public void touchDownInCharacterStage(Vector2 mousePosition, int button)
	{
		// check for clicking button
		if (button == Buttons.LEFT)
		{
			if (mousePositionStage.x >= charSelectStartButton.getX() && mousePositionStage.x <= charSelectStartButton.getX()+charSelectStartButton.getWidth())
			{
				if (mousePositionStage.y >= charSelectStartButton.getY() && mousePositionStage.y <= charSelectStartButton.getY()+charSelectStartButton.getHeight())
				{
					if (playerCount >= 2)
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
								endPlayerSprite[i].setVisible(false);
								continue;
							}
							else
							{
								playerSprite[i].setVisible(true);
								playerSprite[i].setAnimation(player[i].walkingAtlas, "0001");
								weaponSprite[i].setVisible(true);
								endPlayerSprite[i].setVisible(true);
							}
						}
					}
				}
			}
			if (mousePositionStage.x >= charSelectBackButton.getX() && mousePositionStage.x <= charSelectBackButton.getX()+charSelectBackButton.getWidth())
			{
				if (mousePositionStage.y >= charSelectBackButton.getY() && mousePositionStage.y <= charSelectBackButton.getY()+charSelectBackButton.getHeight())
				{
					screen = "menu";
					cancelSound.play();
					resetVariableInCharacterStage();
				}
			}
		}
	}

	public void touchDownInHowToStage(Vector2 mousePosition, int button)
	{
		// check for clicking button
		if (button == Buttons.LEFT)
		{
			if (mousePosition.x >= howToBackButton.getX() && mousePosition.x <= howToBackButton.getX()+howToBackButton.getWidth())
			{
				if (mousePosition.y >= howToBackButton.getY() && mousePosition.y <= howToBackButton.getY()+howToBackButton.getHeight())
				{
					screen = "menu";
					cancelSound.play();
				}
			}
		}
	}

	public void touchDownInPauseStage(Vector2 mousePosition, int button)
	{
		// check for clicking button
		if (button == Buttons.LEFT)
		{
			if (mousePosition.x >= pauseResumeButton.getX() && mousePosition.x <= pauseResumeButton.getX()+pauseResumeButton.getWidth())
			{
				if (mousePosition.y >= pauseResumeButton.getY() && mousePosition.y <= pauseResumeButton.getY()+pauseResumeButton.getHeight())
				{
					screen = "game";
					confirmSound.play();
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
			}
			if (mousePosition.x >= pauseSettingButton.getX() && mousePosition.x <= pauseSettingButton.getX()+pauseSettingButton.getWidth())
			{
				if (mousePosition.y >= pauseSettingButton.getY() && mousePosition.y <= pauseSettingButton.getY()+pauseSettingButton.getHeight())
				{
					screen = "setting";
					back = "pause";
					confirmSound.play();
				}
			}
			if (mousePosition.x >= pauseBackButton.getX() && mousePosition.x <= pauseBackButton.getX()+pauseBackButton.getWidth())
			{
				if (mousePosition.y >= pauseBackButton.getY() && mousePosition.y <= pauseBackButton.getY()+pauseBackButton.getHeight())
				{
					screen = "menu";
					pauseCursorPosition = 1;
					resetVariableInCharacterStage();
					resetVariableInGameStage();
					confirmSound.play();
				}
			}
		}
	}
	
	public void touchDownInEndStage(Vector2 mousePosition, int button)
	{
		// check for clicking button
		if (button == Buttons.LEFT)
		{
			if (mousePosition.x >= endBackButton.getX() && mousePosition.x <= endBackButton.getX()+endBackButton.getWidth())
			{
				if (mousePosition.y >= endBackButton.getY() && mousePosition.y <= endBackButton.getY()+endBackButton.getHeight())
				{
					screen = "menu";
					cancelSound.play();
				}
			}
		}
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
		else if (screen.equals("pause"))
		{
			mouseMovedInPauseStage(mousePositionStage);
		}
		else if (screen.equals("setting"))
		{
			mouseMovedInSettingStage(mousePositionStage);
		}
		else if (screen.equals("howto"))
		{
			mouseMovedInHowToStage(mousePositionStage);
		}
		else if (screen.equals("end"))
		{
			mouseMovedInEndStage(mousePositionStage);
		}
		return false;
	}

	public void mouseMovedInMenuStage(Vector2 mousePosition)
	{
		// check for mouse moved to button
		if (mousePosition.x >= menuStartButton.getX() && mousePosition.x <= menuStartButton.getX()+menuStartButton.getWidth())
		{
			if (mousePosition.y >= menuStartButton.getY() && mousePosition.y <= menuStartButton.getY()+menuStartButton.getHeight())
			{
				if (cursorPosition != 1)
				{
					cursorSound.play();
				}
				cursorPosition = 1;
				menuStartButton.img = menuStart2;
				menuHowToButton.img = menuHowTo1;
				menuSettingButton.img = menuSetting1;
				menuExitButton.img = menuExit1;
			}
		}
		if (mousePosition.x >= menuSettingButton.getX() && mousePosition.x <= menuSettingButton.getX()+menuSettingButton.getWidth())
		{
			if (mousePosition.y >= menuSettingButton.getY() && mousePosition.y <= menuSettingButton.getY()+menuSettingButton.getHeight())
			{
				if (cursorPosition != 2)
				{
					cursorSound.play();
				}
				cursorPosition = 2;
				menuStartButton.img = menuStart1;
				menuHowToButton.img = menuHowTo1;
				menuSettingButton.img = menuSetting2;
				menuExitButton.img = menuExit1;
			}
		}
		if (mousePosition.x >= menuHowToButton.getX() && mousePosition.x <= menuHowToButton.getX()+menuHowToButton.getWidth())
		{
			if (mousePosition.y >= menuHowToButton.getY() && mousePosition.y <= menuHowToButton.getY()+menuHowToButton.getHeight())
			{
				if (cursorPosition != 3)
				{
					cursorSound.play();
				}
				cursorPosition = 3;
				menuStartButton.img = menuStart1;
				menuHowToButton.img = menuHowTo2;
				menuSettingButton.img = menuSetting1;
				menuExitButton.img = menuExit1;
			}
		}
		if (mousePosition.x >= menuExitButton.getX() && mousePosition.x <= menuExitButton.getX()+menuExitButton.getWidth())
		{
			if (mousePosition.y >= menuExitButton.getY() && mousePosition.y <= menuExitButton.getY()+menuExitButton.getHeight())
			{
				if (cursorPosition != 4)
				{
					cursorSound.play();
				}
				cursorPosition = 4;
				menuStartButton.img = menuStart1;
				menuHowToButton.img = menuHowTo1;
				menuSettingButton.img = menuSetting1;
				menuExitButton.img = menuExit2;
			}
		}
	}
	
	public void mouseMovedInCharacterStage(Vector2 mousePosition)
	{
		// check for mouse moved to button
		if (mousePosition.x >= charSelectBackButton.getX() && mousePosition.x <= charSelectBackButton.getX()+charSelectBackButton.getWidth())
		{
			if (mousePosition.y >= charSelectBackButton.getY() && mousePosition.y <= charSelectBackButton.getY()+charSelectBackButton.getHeight())
			{
				if (charSelectBackButton.img == back1)
				{
					cursorSound.play();
					charSelectBackButton.img = back2;
				}
			}
			else
			{
				charSelectBackButton.img = back1;
			}
		}
		else
		{
			charSelectBackButton.img = back1;
		}
		if (mousePosition.x >= charSelectStartButton.getX() && mousePosition.x <= charSelectStartButton.getX()+charSelectStartButton.getWidth())
		{
			if (mousePosition.y >= charSelectStartButton.getY() && mousePosition.y <= charSelectStartButton.getY()+charSelectStartButton.getHeight())
			{
				if (charSelectStartButton.img == menuStart1)
				{
					cursorSound.play();
					charSelectStartButton.img = menuStart2;
				}
			}
			else
			{
				charSelectStartButton.img = menuStart1;
			}
		}
		else
		{
			charSelectStartButton.img = menuStart1;
		}
	}

	public void mouseMovedInPauseStage(Vector2 mousePosition)
	{
		// check for mouse moved to button
		if (mousePosition.x >= pauseResumeButton.getX() && mousePosition.x <= pauseResumeButton.getX()+pauseResumeButton.getWidth())
		{
			if (mousePosition.y >= pauseResumeButton.getY() && mousePosition.y <= pauseResumeButton.getY()+pauseResumeButton.getHeight())
			{
				if (pauseCursorPosition != 1)
				{
					cursorSound.play();
				}
				pauseCursorPosition = 1;
				pauseResumeButton.img = pauseResume2;
				pauseSettingButton.img = pauseSetting1;
				pauseBackButton.img = pauseBack1;
			}
		}
		if (mousePosition.x >= pauseSettingButton.getX() && mousePosition.x <= pauseSettingButton.getX()+pauseSettingButton.getWidth())
		{
			if (mousePosition.y >= pauseSettingButton.getY() && mousePosition.y <= pauseSettingButton.getY()+pauseSettingButton.getHeight())
			{
				if (pauseCursorPosition != 2)
				{
					cursorSound.play();
				}
				pauseCursorPosition = 2;
				pauseResumeButton.img = pauseResume1;
				pauseSettingButton.img = pauseSetting2;
				pauseBackButton.img = pauseBack1;
			}
		}
		if (mousePosition.x >= pauseBackButton.getX() && mousePosition.x <= pauseBackButton.getX()+pauseBackButton.getWidth())
		{
			if (mousePosition.y >= pauseBackButton.getY() && mousePosition.y <= pauseBackButton.getY()+pauseBackButton.getHeight())
			{
				if (pauseCursorPosition != 3)
				{
					cursorSound.play();
				}
				pauseCursorPosition = 3;
				pauseResumeButton.img = pauseResume1;
				pauseSettingButton.img = pauseSetting1;
				pauseBackButton.img = pauseBack2;
			}
		}
	}

	public void mouseMovedInSettingStage(Vector2 mousePosition)
	{
		// check for mouse moved to button
		if (mousePosition.x >= settingBackButton.getX() && mousePosition.x <= settingBackButton.getX()+settingBackButton.getWidth())
		{
			if (mousePosition.y >= settingBackButton.getY() && mousePosition.y <= settingBackButton.getY()+settingBackButton.getHeight())
			{
				if (settingBackButton.img == back1)
				{
					cursorSound.play();
					settingBackButton.img = back2;
				}
			}
			else
			{
				settingBackButton.img = back1;
			}
		}
		else
		{
			settingBackButton.img = back1;
		}
	}
	
	public void mouseMovedInHowToStage(Vector2 mousePosition)
	{
		// check for mouse moved to button
		if (mousePosition.x >= howToBackButton.getX() && mousePosition.x <= howToBackButton.getX()+howToBackButton.getWidth())
		{
			if (mousePosition.y >= howToBackButton.getY() && mousePosition.y <= howToBackButton.getY()+howToBackButton.getHeight())
			{
				if (howToBackButton.img == back1)
				{
					cursorSound.play();
					howToBackButton.img = back2;
				}
			}
			else
			{
				howToBackButton.img = back1;
			}
		}
		else
		{
			howToBackButton.img = back1;
		}
	}
	
	public void mouseMovedInEndStage(Vector2 mousePosition)
	{
		// check for mouse moved to button
		if (mousePosition.x >= endBackButton.getX() && mousePosition.x <= endBackButton.getX()+endBackButton.getWidth())
		{
			if (mousePosition.y >= endBackButton.getY() && mousePosition.y <= endBackButton.getY()+endBackButton.getHeight())
			{
				if (endBackButton.img == back1)
				{
					cursorSound.play();
					endBackButton.img = back2;
				}
			}
			else
			{
				endBackButton.img = back1;
			}
		}
		else
		{
			endBackButton.img = back1;
		}
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	// check collision for each type of object
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
		// check if attack hit something and calculated result
		playerAttack.attackSound.play(0.4f);
		// check if attack hit any normal wall
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
		// check if attack hit any item drop
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
		// check if attack hit any other player
		for (PlayerCharacter allPlayer : player) {
			if (allPlayer == playerAttack || !allPlayer.isVisible() || allPlayer.dead)
			{
				continue;
			}
			if (checkCollision(playerAttack, allPlayer, "attack") && !allPlayer.hurt)
			{
				characterSkill(playerAttack, allPlayer, false);
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
		// check if attack hit any other arrow 
		for (Arrow arrow : playerArrow)
		{
			if (checkCollision(playerAttack, arrow, "attack") && arrow.isVisible())
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

	public void checkArrowAttack(PlayerCharacter allPlayer, int arrowCount, int arrowCount2)
	{
		// check collision between arrow
		for (GameObject arrow : playerArrow)
		{
			if (arrow == allPlayer.arrow || !arrow.isVisible())
			{
				arrowCount2 += 1;
				continue;
			}
			if (checkCollision(allPlayer.arrow, arrow))// attack effect bug may happen here because of arrowcount arrowcount2 loopcount three of these is confusing 
			{
				PlayerWeapon.fistSound.play(0.5f);
				arrowCharged[arrowCount] = false;
				arrowCharged[arrowCount2] = false;
				arrowEffectRenderer[arrowCount].updateCurrentAnim(EffectRenderer.punchAnimation);
				arrowEffectRenderer[arrowCount2].updateCurrentAnim(EffectRenderer.punchAnimation);
				if (allPlayer.arrow.speedX != 0)arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				else arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				arrowEffectRenderer[arrowCount].direction = allPlayer.arrow.arrowDirection;
				arrowEffectRenderer[arrowCount].check = true;
				arrowEffectRenderer[arrowCount].time = 0;
//				if (((Arrow)arrow).speedX != 0)arrowEffectRenderer[arrowCount].setValue(((Arrow)arrow).hitbox.getX(), ((Arrow)arrow).hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
//				else arrowEffectRenderer[arrowCount2].setValue(((Arrow)arrow).hitbox.getX(), ((Arrow)arrow).hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
//				arrowEffectRenderer[arrowCount2].check = true;
//				arrowEffectRenderer[arrowCount2].time = 0;
				allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
				allPlayer.arrow.setVisible(false);
				((Arrow)arrow).setArrow(player[arrowCount2].getX()+25, player[arrowCount2].getY()+20, player[arrowCount2].direction, player[arrowCount2].weaponLV);
				((Arrow)arrow).setVisible(false);
			}
			arrowCount2 += 1;
		}
		//check collision between arrow and unbreakable wall
		for (GameObject wall : walls)
		{
			if (checkCollision(allPlayer.arrow, wall) && allPlayer.arrow.isVisible())
			{
				PlayerWeapon.fistSound.play(0.5f);
				arrowEffectRenderer[arrowCount].updateCurrentAnim(EffectRenderer.punchAnimation);
				if (allPlayer.arrow.speedX != 0)arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				else arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				arrowEffectRenderer[arrowCount].direction = allPlayer.arrow.arrowDirection;
				arrowEffectRenderer[arrowCount].check = true;
				arrowEffectRenderer[arrowCount].time = 0;
				allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
				allPlayer.arrow.setVisible(false);
				arrowCharged[arrowCount] = false;
			}
		}
		//check collision between arrow and normal wall
		for (GameObject wall : normalWalls)
		{
			if (checkCollision(allPlayer.arrow, wall) && allPlayer.arrow.isVisible())
			{
				if (wall instanceof NormalWall)
				{
					PlayerWeapon.fistSound.play(0.5f);
					if (arrowCharged[arrowCount])
					{
						((NormalWall) wall).hp -= 3;
					}
					else
					{
						((NormalWall) wall).hp -= 2;
					}
				}
				arrowEffectRenderer[arrowCount].updateCurrentAnim(EffectRenderer.punchAnimation);
				if (allPlayer.arrow.speedX != 0)arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				else arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				arrowEffectRenderer[arrowCount].direction = allPlayer.arrow.arrowDirection;
				arrowEffectRenderer[arrowCount].check = true;
				arrowEffectRenderer[arrowCount].time = 0;
				allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
				allPlayer.arrow.setVisible(false);
				arrowCharged[arrowCount] = false;
				break;
			}
		}
		// checkcollision between arrow and itemdrop
		for (ItemDrop item : itemDrop)
		{
			if (checkCollision(allPlayer.arrow, item) && allPlayer.arrow.isVisible())
			{
				PlayerWeapon.fistSound.play(0.5f);
				item.dropped = false;
				item.setVisible(item.dropped);
				ItemDrop.dropCount -= 1;
				item.hitbox.setX(-1000);
				item.hitbox.setY(-1000);
				arrowEffectRenderer[arrowCount].updateCurrentAnim(EffectRenderer.punchAnimation);
				if (allPlayer.arrow.speedX != 0)arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				else arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				arrowEffectRenderer[arrowCount].direction = allPlayer.arrow.arrowDirection;
				arrowEffectRenderer[arrowCount].check = true;
				arrowEffectRenderer[arrowCount].time = 0;
				allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
				allPlayer.arrow.setVisible(false);
				arrowCharged[arrowCount] = false;
			}
		}
		//checkcollision between arrow and player
		for (PlayerCharacter otherPlayer : player)
		{
			if (allPlayer == otherPlayer || !otherPlayer.isVisible() || otherPlayer.dead)
			{
				continue;
			}
			if (checkCollision(otherPlayer, allPlayer.arrow) && !otherPlayer.hurt && allPlayer.arrow.isVisible())
			{
				arrowEffectRenderer[arrowCount].updateCurrentAnim(EffectRenderer.spearAnimation);
				characterSkill(allPlayer, otherPlayer, arrowCharged[arrowCount]);
				Arrow.arrowHit.play(0.7f);
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
				if (allPlayer.arrow.speedX != 0)arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY()-15, allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				else arrowEffectRenderer[arrowCount].setValue(allPlayer.arrow.hitbox.getX(), allPlayer.arrow.hitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				arrowEffectRenderer[arrowCount].direction = allPlayer.arrow.arrowDirection;
				arrowEffectRenderer[arrowCount].check = true;
				arrowEffectRenderer[arrowCount].time = 0;
				allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
				allPlayer.arrow.setVisible(false);
				arrowCharged[arrowCount] = false;
			}
		}
	}
	
	public void characterSkill(PlayerCharacter playerAttack, PlayerCharacter playerDamaged, boolean arrowCharged)
	{
		// random skill activation and calculate skill 
		if (playerAttack.chargeMax || arrowCharged)
		{
			int num = (int)(Math.random()*100);
			if (playerAttack.deadAtlas == PlayerCharacter.character1Dead)// character 1 skill
			{
				if (num <= 50)
				{
					if (playerDamaged.armor > 0)
					{
						playerDamaged.armor -= playerAttack.attack/2;
						if (playerDamaged.armor < 0)
						{
							playerDamaged.armor = 0;
						}
					}
					playerAttack.balloon.runAnimation("skill1");// play skill icon balloon here
					skill1Sound.play();
				}
			}
			else if (playerAttack.deadAtlas == PlayerCharacter.character2Dead)// character 2 skill
			{
				if (num <= 50)
				{
					playerDamaged.slowTime = 1.5f;
					playerAttack.balloon.runAnimation("skill2");// play skill icon balloon here
					skill1Sound.play();
				}
			}
			else if (playerAttack.deadAtlas == PlayerCharacter.character3Dead)// character 3 skill
			{
				if (num <= 50)
				{
					playerAttack.speedBoostTime = 1.5f;
					playerAttack.balloon.runAnimation("skill3");// play skill icon balloon here
					skill1Sound.play();
				}
			}
			else if (playerAttack.deadAtlas == PlayerCharacter.character4Dead)// character 4 skill
			{
				if (num <= 50)
				{
					playerAttack.armor += playerAttack.attack/2;
					if (playerAttack.armor > 100)
					{
						playerAttack.armor = 100;
					}
					playerAttack.balloon.runAnimation("skill4");// play skill icon balloon here
					skill1Sound.play();
				}
			}
		}
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
		startDelay = 3;
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
		debugItem.setVisible(false);
		debugItem.hitbox.setX(-1000);
		debugItem.hitbox.setY(-1000);
		ItemDrop.dropCount = 0;
		arrowCharged[0] = false;
		arrowCharged[1] = false;
		arrowCharged[2] = false;
		arrowCharged[3] = false;
		weaponSprite[0].img = noWeapon;
		weaponSprite[1].img = noWeapon;
		weaponSprite[2].img = noWeapon;
		weaponSprite[3].img = noWeapon;
		endPlayerSprite[0].setY(240);
		endPlayerSprite[1].setY(240);
		endPlayerSprite[2].setY(240);
		endPlayerSprite[3].setY(240);
		platform.setX(-1000);
		winnerBalloon.setX(-1000);
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
			allPlayer.attack = 2;
			allPlayer.weapon.setVisible(true);
			allPlayer.weaponLV = 0;
			allPlayer.weaponAtlas = PlayerWeapon.fist;
			allPlayer.weaponAnim = PlayerWeapon.fistAnim;
			allPlayer.weapon.updateWeaponAnimation();
			allPlayer.leftPressed = false;
			allPlayer.rightPressed = false;
			allPlayer.upPressed = false;
			allPlayer.rightPressed = false;
			allPlayer.moving = false;
			allPlayer.playerMoveSpeed = PlayerCharacter.moveSpeed;
			allPlayer.speed_x = 0;
			allPlayer.speed_y = 0;
			allPlayer.speedUp = 0;
			allPlayer.speedDown = 0;
			allPlayer.speedLeft = 0;
			allPlayer.speedRight = 0;
			allPlayer.arrow.setX(-100);
			allPlayer.arrow.setY(-100);
			allPlayer.arrow.setArrow(allPlayer.getX()+25, allPlayer.getY()+20, allPlayer.direction, allPlayer.weaponLV);
			allPlayer.arrow.setVisible(false);
			allPlayer.attackSound = PlayerWeapon.fistSound;
			allPlayer.speedBoostTime = 0;
			allPlayer.slowTime = 0;
			allPlayer.fadeTime = 2;
			allPlayer.attackEffect.updateCurrentAnim(EffectRenderer.punchAnimation);
			allPlayer.arrowEffect.updateCurrentAnim(EffectRenderer.punchAnimation);
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
			else if (allPlayer == player[2])
			{
				allPlayer.setX(50);
				allPlayer.setY(550);
			}
			else if (allPlayer == player[3])
			{
				allPlayer.setX(1250);
				allPlayer.setY(50);
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
		// handle button input for controller
		if (changeControl && (player[playerNumber].controlType.equals("controller1") || player[playerNumber].controlType.equals("controller2") || player[playerNumber].controlType.equals("controller3") || player[playerNumber].controlType.equals("controller4")))
		{
			if (player[playerNumber].controllerCount+1 <= Controllers.getControllers().size)
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
	}

	public void buttonDownInMenuStage(Controller controller, int buttonCode)
	{
		// handle button input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (buttonCode == player[i].controlRight)
				{
					if (cursorPosition == 1)
					{
						cursorPosition = 3;
						cursorSound.play();
					}
					else if (cursorPosition == 2)
					{
						cursorPosition = 3;
						cursorSound.play();
					}
					else if (cursorPosition == 3)
					{
						cursorPosition = 4;
						cursorSound.play();
					}
					else if (cursorPosition == 4)
					{
						cursorPosition = 2;
						cursorSound.play();
					}
				}
				if (buttonCode == player[i].controlLeft)
				{
					if (cursorPosition == 1)
					{
						cursorPosition = 4;
						cursorSound.play();
					}
					else if (cursorPosition == 2)
					{
						cursorPosition = 4;
						cursorSound.play();
					}
					else if (cursorPosition == 3)
					{
						cursorPosition = 2;
						cursorSound.play();
					}
					else if (cursorPosition == 4)
					{
						cursorPosition = 3;
						cursorSound.play();
					}
				}
				if (buttonCode == player[i].controlDown)
				{
					if (cursorPosition == 1)
					{
						cursorPosition = 2;
						cursorSound.play();
					}
					else if (cursorPosition == 2)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
					else if (cursorPosition == 3)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
					else if (cursorPosition == 4)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
				}
				else if (buttonCode == player[i].controlUp)
				{
					if (cursorPosition == 1)
					{
						cursorPosition = 2;
						cursorSound.play();
					}
					else if (cursorPosition == 2)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
					else if (cursorPosition == 3)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
					else if (cursorPosition == 4)
					{
						cursorPosition = 1;
						cursorSound.play();
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
					menuStartButton.img = menuStart2;
					menuHowToButton.img = menuHowTo1;
					menuSettingButton.img = menuSetting1;
					menuExitButton.img = menuExit1;
				}
				else if (cursorPosition == 2)
				{
					menuStartButton.img = menuStart1;
					menuHowToButton.img = menuHowTo1;
					menuSettingButton.img = menuSetting2;
					menuExitButton.img = menuExit1;
				}
				else if (cursorPosition == 3)
				{
					menuStartButton.img = menuStart1;
					menuHowToButton.img = menuHowTo2;
					menuSettingButton.img = menuSetting1;
					menuExitButton.img = menuExit1;
				}
				else if (cursorPosition == 4)
				{
					menuStartButton.img = menuStart1;
					menuHowToButton.img = menuHowTo1;
					menuSettingButton.img = menuSetting1;
					menuExitButton.img = menuExit2;
				}
			}
		}
	}

	public void buttonDownInCharacterStage(Controller controller, int buttonCode)
	{
		// handle button input for controller
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
						if (characterIndex[i]-1 >= 0)
						{
							characterIndex[i] -= 1;
							cursorSound.play();
						}
					}
					else if (buttonCode == player[i].controlRight)
					{
						if (characterIndex[i]+1 < 4)// change 4 to number of character texture here
						{
							characterIndex[i] += 1;
							cursorSound.play();
						}
					}
					if (characterIndex[i] == 0)
					{
						playerCharacterSelect[i].img = char1Info;
						player[i].setTexture(PlayerCharacter.character1, PlayerCharacter.character1Dead);
						endPlayerSprite[i].img = PlayerCharacter.character1Lose;
					}
					else if(characterIndex[i] == 1)
					{
						playerCharacterSelect[i].img = char2Info;
						player[i].setTexture(PlayerCharacter.character2, PlayerCharacter.character2Dead);
						endPlayerSprite[i].img = PlayerCharacter.character2Lose;
					}
					else if(characterIndex[i] == 2)
					{
						playerCharacterSelect[i].img = char3Info;
						player[i].setTexture(PlayerCharacter.character3, PlayerCharacter.character3Dead);
						endPlayerSprite[i].img = PlayerCharacter.character3Lose;
					}
					else if(characterIndex[i] == 3)
					{
						playerCharacterSelect[i].img = char4Info;
						player[i].setTexture(PlayerCharacter.character4, PlayerCharacter.character4Dead);
						endPlayerSprite[i].img = PlayerCharacter.character4Lose;
					}
				}
			}//}
		}
	}

	public void buttonDownInGameStage(Controller controller, int buttonCode)
	{
		// handle button input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				// start if control here
				if (player[i].dead  || playerCount <= 1 || !player[i].isVisible() || startDelay > 0)
				{
					continue;
				}
				if (buttonCode == player[i].controlBack)
				{
					screen = "pause";
					cancelSound.play();
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
		// handle button input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (buttonCode == player[i].controlAttack || buttonCode == player[i].controlBack)
				{
					screen = "menu";
					resetVariableInCharacterStage();
					resetVariableInGameStage();
				}
			}
		}
	}

	public void buttonDownInPauseStage(Controller controller, int buttonCode)
	{
		// handle button input for controller
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
					pauseResumeButton.img = pauseResume2;
					pauseSettingButton.img = pauseSetting1;
					pauseBackButton.img = pauseBack1;
				}
				else if (pauseCursorPosition == 2)
				{
					pauseResumeButton.img = pauseResume1;
					pauseSettingButton.img = pauseSetting2;
					pauseBackButton.img = pauseBack1;
				}
				else if (pauseCursorPosition == 3)
				{
					pauseResumeButton.img = pauseResume1;
					pauseSettingButton.img = pauseSetting1;
					pauseBackButton.img = pauseBack2;
				}
			}// controller loop
		}
	}

	public void buttonDownInHowToStage(Controller controller, int buttonCode)
	{
		// handle button input for controller
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
		// handle button input for controller
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
		if (screen.equals("game"))
		{
			axisInGameStage(controller, axisCode, value);
		}
		else if (screen.equals("menu"))
		{
			axisInMenuStage(controller, axisCode, value);
		}
		else if (screen.equals("character"))
		{
			axisInCharacterStage(controller, axisCode, value);
		}
		else if (screen.equals("pause"))
		{
			axisInPauseStage(controller, axisCode, value);
		}
		return true;
	}

	public void axisInGameStage(Controller controller, int axisCode, float value)
	{
		// handle analog stick input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (player[i].dead  || playerCount <= 1 || !player[i].isVisible() || startDelay > 0)
				{
					continue;
				}
				if (axisCode % 2 == 0 && !player[i].leftPressed && !player[i].rightPressed)//even axiscode is y
				{
					if (value > 0.7)// down
					{
						player[i].downPressed = true;
						player[i].leftPressed = false;
						player[i].rightPressed = false;
						player[i].upPressed = false;	
					}
					else if (value < -0.7)// up
					{
						player[i].upPressed = true;
						player[i].leftPressed = false;
						player[i].rightPressed = false;
						player[i].downPressed = false;
					}
					else if (value > -0.1 && value < 0.1)
					{
						player[i].leftPressed = false;
						player[i].upPressed = false;
						player[i].downPressed = false;
						player[i].rightPressed = false;
					}
				}
				else if (axisCode % 2 == 1 && !player[i].upPressed && !player[i].downPressed)
				{
					if (value > 0.7)// right
					{
						player[i].rightPressed = true;
						player[i].upPressed = false;
						player[i].downPressed = false;
						player[i].leftPressed = false;
					}
					else if (value < -0.7)// left
					{
						player[i].leftPressed = true;
						player[i].upPressed = false;
						player[i].downPressed = false;
						player[i].rightPressed = false;
					}
					else
					{
						player[i].leftPressed = false;
						player[i].upPressed = false;
						player[i].downPressed = false;
						player[i].rightPressed = false;
					}
				}
			}
		}
	}

	public void axisInMenuStage(Controller controller, int axisCode, float value)
	{
		// handle analog stick input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (axisCode%2 == 0)//up down
				{
					if (value <= -1.0) //up
					{
						if (cursorPosition == 1 && !axisMove)
						{
							cursorPosition = 2;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 2 && !axisMove)
						{
							cursorPosition = 1;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 3 && !axisMove)
						{
							cursorPosition = 1;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 4 && !axisMove)
						{
							cursorPosition = 1;
							cursorSound.play();
							axisMove = true;
						}
					}
					else if (value >= 1.0) // down
					{
						if (cursorPosition == 1 && !axisMove)
						{
							cursorPosition = 2;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 2 && !axisMove)
						{
							cursorPosition = 1;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 3 && !axisMove)
						{
							cursorPosition = 1;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 4 && !axisMove)
						{
							cursorPosition = 1;
							cursorSound.play();
							axisMove = true;
						}
					}
					else if (value > -0.1 && value < 0.1)
					{
						axisMove = false;
					}
					if (cursorPosition == 1)
					{
						menuStartButton.img = menuStart2;
						menuHowToButton.img = menuHowTo1;
						menuSettingButton.img = menuSetting1;
						menuExitButton.img = menuExit1;
					}
					else if (cursorPosition == 2)
					{
						menuStartButton.img = menuStart1;
						menuHowToButton.img = menuHowTo1;
						menuSettingButton.img = menuSetting2;
						menuExitButton.img = menuExit1;
					}
					else if (cursorPosition == 3)
					{
						menuStartButton.img = menuStart1;
						menuHowToButton.img = menuHowTo2;
						menuSettingButton.img = menuSetting1;
						menuExitButton.img = menuExit1;
					}
					else if (cursorPosition == 4)
					{
						menuStartButton.img = menuStart1;
						menuHowToButton.img = menuHowTo1;
						menuSettingButton.img = menuSetting1;
						menuExitButton.img = menuExit2;
					}
				}
				else if (axisCode%2 == 1) 
				{
					if (value >= 1.0)
					{
						if (cursorPosition == 1 && !axisMove)
						{
							cursorPosition = 2;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 2 && !axisMove)
						{
							cursorPosition = 3;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 3 && !axisMove)
						{
							cursorPosition = 4;
							cursorSound.play();
							axisMove = true;
						}
						else if (cursorPosition == 4 && !axisMove)
						{
							cursorPosition = 2;
							cursorSound.play();
							axisMove = true;
						}
					}
					else if (value <= -1.0 && !axisMove)
					{
						if (cursorPosition == 1)
						{
							cursorPosition = 4;
							cursorSound.play();
						}
						else if (cursorPosition == 2)
						{
							cursorPosition = 4;
							cursorSound.play();
						}
						else if (cursorPosition == 3)
						{
							cursorPosition = 2;
							cursorSound.play();
						}
						else if (cursorPosition == 4)
						{
							cursorPosition = 3;
							cursorSound.play();
						}
					}
					else if (value > -0.1 && value < 0.1)
					{
						axisMove = false;
					}
					if (cursorPosition == 1)
					{
						menuStartButton.img = menuStart2;
						menuHowToButton.img = menuHowTo1;
						menuSettingButton.img = menuSetting1;
						menuExitButton.img = menuExit1;
					}
					else if (cursorPosition == 2)
					{
						menuStartButton.img = menuStart1;
						menuHowToButton.img = menuHowTo1;
						menuSettingButton.img = menuSetting2;
						menuExitButton.img = menuExit1;
					}
					else if (cursorPosition == 3)
					{
						menuStartButton.img = menuStart1;
						menuHowToButton.img = menuHowTo2;
						menuSettingButton.img = menuSetting1;
						menuExitButton.img = menuExit1;
					}
					else if (cursorPosition == 4)
					{
						menuStartButton.img = menuStart1;
						menuHowToButton.img = menuHowTo1;
						menuSettingButton.img = menuSetting1;
						menuExitButton.img = menuExit2;
					}
				}
			}
		}
	}

	public void axisInCharacterStage(Controller controller, int axisCode, float value)
	{
		// handle analog stick input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (axisCode%2 == 1 && playerCharacterSelect[i].isVisible())
				{
					if (value >= 1.0 && !axisMove)
					{
						if (characterIndex[i]+1 < 4)
						{
							characterIndex[i] += 1;
							cursorSound.play();
						}
						axisMove = true;
					}
					else if (value <= -1.0 && !axisMove)
					{
						if (characterIndex[i]-1 >= 0)
						{
							characterIndex[i] -= 1;
							cursorSound.play();
						}
						axisMove = true;
					}
					else if (value > -0.1 && value < 0.1)
					{
						axisMove = false;
					}
					if (characterIndex[i] == 0)
					{
						playerCharacterSelect[i].img = char1Info;
						player[i].setTexture(PlayerCharacter.character1, PlayerCharacter.character1Dead);
						endPlayerSprite[i].img = PlayerCharacter.character1Lose;
					}
					else if(characterIndex[i] == 1)
					{
						playerCharacterSelect[i].img = char2Info;
						player[i].setTexture(PlayerCharacter.character2, PlayerCharacter.character2Dead);
						endPlayerSprite[i].img = PlayerCharacter.character2Lose;
					}
					else if(characterIndex[i] == 2)
					{
						playerCharacterSelect[i].img = char3Info;
						player[i].setTexture(PlayerCharacter.character3, PlayerCharacter.character3Dead);
						endPlayerSprite[i].img = PlayerCharacter.character3Lose;
					}
					else if(characterIndex[i] == 3)
					{
						playerCharacterSelect[i].img = char4Info;
						player[i].setTexture(PlayerCharacter.character4, PlayerCharacter.character4Dead);
						endPlayerSprite[i].img = PlayerCharacter.character4Lose;
					}
				}
			}
		}
	}
	
	public void axisInPauseStage(Controller controller, int axisCode, float value)
	{
		// handle analog stick input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (axisCode%2 == 0)
				{
					if (value <= -1.0 && !axisMove)
					{
						pauseCursorPosition -= 1;
						if (pauseCursorPosition < 1)
						{
							pauseCursorPosition = 3;
						}
						cursorSound.play();
						axisMove = true;
					}
					else if (value >= 1.0 && !axisMove)
					{
						pauseCursorPosition += 1;
						cursorSound.play();
						if (pauseCursorPosition > 3)
						{
							pauseCursorPosition = 1;
						}
						cursorSound.play();
						axisMove = true;
					}
					else if (value > -0.1 && value < 0.1)
					{
						axisMove = false;
					}
					if (pauseCursorPosition == 1)
					{
						pauseResumeButton.img = pauseResume2;
						pauseSettingButton.img = pauseSetting1;
						pauseBackButton.img = pauseBack1;
					}
					else if (pauseCursorPosition == 2)
					{
						pauseResumeButton.img = pauseResume1;
						pauseSettingButton.img = pauseSetting2;
						pauseBackButton.img = pauseBack1;
					}
					else if (pauseCursorPosition == 3)
					{
						pauseResumeButton.img = pauseResume1;
						pauseSettingButton.img = pauseSetting1;
						pauseBackButton.img = pauseBack2;
					}
				}
			}
		}
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
		// handle d-pad input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (value == PovDirection.east)
				{
					if (cursorPosition == 1)
					{
						cursorPosition = 3;
						cursorSound.play();
					}
					else if (cursorPosition == 2)
					{
						cursorPosition = 3;
						cursorSound.play();
					}
					else if (cursorPosition == 3)
					{
						cursorPosition = 4;
						cursorSound.play();
					}
					else if (cursorPosition == 4)
					{
						cursorPosition = 2;
						cursorSound.play();
					}
				}
				if (value == PovDirection.west)
				{
					if (cursorPosition == 1)
					{
						cursorPosition = 4;
						cursorSound.play();
					}
					else if (cursorPosition == 2)
					{
						cursorPosition = 4;
						cursorSound.play();
					}
					else if (cursorPosition == 3)
					{
						cursorPosition = 2;
						cursorSound.play();
					}
					else if (cursorPosition == 4)
					{
						cursorPosition = 3;
						cursorSound.play();
					}
				}
				if (value == PovDirection.south)
				{
					if (cursorPosition == 1)
					{
						cursorPosition = 2;
						cursorSound.play();
					}
					else if (cursorPosition == 2)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
					else if (cursorPosition == 3)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
					else if (cursorPosition == 4)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
				}
				else if (value == PovDirection.north)
				{
					if (cursorPosition == 1)
					{
						cursorPosition = 2;
						cursorSound.play();
					}
					else if (cursorPosition == 2)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
					else if (cursorPosition == 3)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
					else if (cursorPosition == 4)
					{
						cursorPosition = 1;
						cursorSound.play();
					}
				}
				if (cursorPosition == 1)
				{
					menuStartButton.img = menuStart2;
					menuHowToButton.img = menuHowTo1;
					menuSettingButton.img = menuSetting1;
					menuExitButton.img = menuExit1;
				}
				else if (cursorPosition == 2)
				{
					menuStartButton.img = menuStart1;
					menuHowToButton.img = menuHowTo1;
					menuSettingButton.img = menuSetting2;
					menuExitButton.img = menuExit1;
				}
				else if (cursorPosition == 3)
				{
					menuStartButton.img = menuStart1;
					menuHowToButton.img = menuHowTo2;
					menuSettingButton.img = menuSetting1;
					menuExitButton.img = menuExit1;
				}
				else if (cursorPosition == 4)
				{
					menuStartButton.img = menuStart1;
					menuHowToButton.img = menuHowTo1;
					menuSettingButton.img = menuSetting1;
					menuExitButton.img = menuExit2;
				}
			}
		}
	}

	public void povInCharacterStage(Controller controller, PovDirection value)
	{
		// handle d-pad input for controller
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
						if (characterIndex[i]+1 < 4)// change 4 to number of character texture here
						{
							characterIndex[i] += 1;
						}
					}
					if (characterIndex[i] == 0)
					{
						playerCharacterSelect[i].img = char1Info;
						player[i].setTexture(PlayerCharacter.character1, PlayerCharacter.character1Dead);
						endPlayerSprite[i].img = PlayerCharacter.character1Lose;
					}
					else if(characterIndex[i] == 1)
					{
						playerCharacterSelect[i].img = char2Info;
						player[i].setTexture(PlayerCharacter.character2, PlayerCharacter.character2Dead);
						endPlayerSprite[i].img = PlayerCharacter.character2Lose;
					}
					else if(characterIndex[i] == 2)
					{
						playerCharacterSelect[i].img = char3Info;
						player[i].setTexture(PlayerCharacter.character3, PlayerCharacter.character3Dead);
						endPlayerSprite[i].img = PlayerCharacter.character3Lose;
					}
					else if(characterIndex[i] == 3)
					{
						playerCharacterSelect[i].img = char4Info;
						player[i].setTexture(PlayerCharacter.character4, PlayerCharacter.character4Dead);
						endPlayerSprite[i].img = PlayerCharacter.character4Lose;
					}
				}
			}
		}
	}

	public void povInPauseStage(Controller controller, PovDirection value)
	{
		// handle d-pad input for controller
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
					pauseResumeButton.img = pauseResume2;
					pauseSettingButton.img = pauseSetting1;
					pauseBackButton.img = pauseBack1;
				}
				else if (pauseCursorPosition == 2)
				{
					pauseResumeButton.img = pauseResume1;
					pauseSettingButton.img = pauseSetting2;
					pauseBackButton.img = pauseBack1;
				}
				else if (pauseCursorPosition == 3)
				{
					pauseResumeButton.img = pauseResume1;
					pauseSettingButton.img = pauseSetting1;
					pauseBackButton.img = pauseBack2;
				}
			}// controller loop
		}
	}

	public void povInGameStage(Controller controller, PovDirection value)
	{
		// handle d-pad input for controller
		for (int i = 0; i < 4; i++)
		{
			if (player[i].controlType.equals("keyboard"))
			{
				continue;
			}
			if (Controllers.getControllers().get(player[i].controllerCount) == controller)
			{
				if (player[i].dead  || playerCount <= 1 || !player[i].isVisible() || startDelay > 0)
				{
					continue;
				}
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
