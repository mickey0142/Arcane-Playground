package game.arcaneplayground;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class MainGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img;
	Vector2 mousePositionScreen = new Vector2();
	Vector2 mousePositionStage = new Vector2();
	String screen = "menu";
	String back = "";
	PlayerCharacter player[] = new PlayerCharacter[4];
	Stage menu;
	int cursorPosition = 1;
	UI menuBackground;
	UI temparrow;
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
	boolean arrowCharged[] = {false, false};
	Stage end;
	Stage pause;
	int pauseCursorPosition = 1;
	UI pauseBackground;
	UI pauseArrow;
	Stage setting;
	UI settingBackground;
	UI playerSetting[] = new UI[4];
	BitmapFont font12;
	String playerControlString[][] = new String[2][6];
	float gametime;//temp
	// default control for each player in this order {up, down, left, right, attack} change this in setting later
	int controlKeeper[][] = {{Keys.W, Keys.S, Keys.A, Keys.D, Keys.F}, {Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_LEFT}};
	int playerCount;

	@Override
	public void create () {
		batch = new SpriteBatch();

		menu = new Stage(new FitViewport(1350, 750));
		character = new Stage(new FitViewport(1350, 750));
		game = new Stage(new FitViewport(1350, 750));
		end = new Stage(new FitViewport(1350, 750));
		pause = new Stage(new FitViewport(1350, 750));
		setting = new Stage(new FitViewport(1350, 750));

		screen = "menu";

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = Color.BLACK;
		font12 = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose();
		
		itemDrop = new ItemDrop[126];
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
	}

	public void createInMenuStage()
	{
		menuBackground = new UI("menubackground.jpg", 0, 0, 1350, 750);
		temparrow = new UI("arrow.png", 250, 350, 32, 32);
		menu.addActor(menuBackground);
		menu.addActor(temparrow);
	}

	public void createInCharacterStage()
	{
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
		player[0] = new PlayerCharacter(50, 50, 60, 60, Keys.W, Keys.S, Keys.A, Keys.D, Keys.F, playerHPBar[0], playerArmorBar[0], PlayerWeapon.sword, PlayerWeapon.swordAnim);
		player[1] = new PlayerCharacter(1250, 550, 60, 60, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT, playerHPBar[1], playerArmorBar[1], PlayerWeapon.sword, PlayerWeapon.swordAnim);
		player[2] = new PlayerCharacter(50, 550, 60, 60, 999, 999, 999, 999, 999, playerHPBar[2], playerArmorBar[2], PlayerWeapon.sword, PlayerWeapon.swordAnim);
		player[3] = new PlayerCharacter(1250, 50, 60, 60, 999, 999, 999, 999, 999, playerHPBar[3], playerArmorBar[3], PlayerWeapon.sword, PlayerWeapon.swordAnim);

		//temppppp 
		//player[0].weaponName = "axe";
		//player[1].weaponName = "spear";

		playerArrow = new Arrow[4];
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
		normalWalls = new NormalWall[126];
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
		
		moveNormalWallZIndex();
	}

	public void createInEndStage()
	{

	}

	public void createInPauseStage()
	{
		pauseBackground = new UI("pausebackground.jpg", 0, 0, 1350, 750);
		pauseArrow = new UI("arrow.png", 335, 482, 32, 32);
		
		pause.addActor(pauseBackground);
		pause.addActor(pauseArrow);
	}
	
	public void createInSettingStage()
	{
		settingBackground = new UI("settingbackground.jpg", 0, 0, 1350, 750);
		playerSetting[0] = new UI("playersetting.jpg", 39, 50, 300, 400);
		playerSetting[1] = new UI("playersetting.jpg", 376, 50, 300, 400);
		playerSetting[2] = new UI("playersetting.jpg", 713, 50, 300, 400);
		playerSetting[3] = new UI("playersetting.jpg", 1050, 50, 300, 400);
		
		setting.addActor(settingBackground);
		setting.addActor(playerSetting[0]);
		setting.addActor(playerSetting[1]);
		setting.addActor(playerSetting[2]);
		setting.addActor(playerSetting[3]);
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		menu.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		character.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		end.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		gametime += Gdx.graphics.getDeltaTime();
		if (screen.equals("menu"))
		{
			menuStageRender();
		}
		else if (screen.equals("character"))
		{
			characterStageRender();
		}
		else if (screen.equals("game"))
		{
			gameStageRender();
			if (playerCount <= 1)
			{
				delay -= Gdx.graphics.getDeltaTime();
			}
			if (delay <= 0)
			{
				screen = "end";
				if (playerCount == 1)
				{
					int numPlayerWin = 0;
					for (PlayerCharacter allPlayer : player)
					{
						if (allPlayer.dead)
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
		//System.out.println("x " + player[0].getX() + " y " + player[0].getY() + " w " + player[0].getWidth() + "h" + player[0].getHeight());
		int loopCount = 0;
//		System.out.println("p1 " + playerArrow[0].getX() + " " + playerArrow[0].getY() + " " + playerArrow[0].getWidth() + " " + playerArrow[0].getHeight());
//		System.out.println("p2 " + playerArrow[1].getX() + " " + playerArrow[1].getY() + " " + playerArrow[1].getWidth() + " " + playerArrow[1].getHeight());
//		System.out.println();
		for (PlayerCharacter allPlayer : player) 
		{
			if (!allPlayer.moving || !allPlayer.isVisible())
			{
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
						item.dropped = false;
						item.setVisible(item.dropped);
						ItemDrop.dropCount -= 1;
						//set all variable about weapon here
						allPlayer.updateNewWeapon(item);
						item.hitbox.setX(-1000);
						item.hitbox.setY(-1000);
					}
				}
			}
			allPlayer.setX(allPlayer.getX() + allPlayer.speed_x);
			allPlayer.updateHitbox();
			allPlayer.setY(allPlayer.getY() + allPlayer.speed_y);
			allPlayer.updateHitbox();
		}
		int arrowCount = 0;
		for (PlayerCharacter allPlayer : player) 
		{
			if (!allPlayer.isVisible())
			{
				continue;
			}
			if (allPlayer.attacking)
			{
				if (allPlayer.weaponName.equals("bow"))
				{
					if (!allPlayer.arrow.isVisible())
					{
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
					if (allPlayer == otherPlayer || !allPlayer.isVisible())
					{
						continue;
					}
					if (checkCollision(otherPlayer, allPlayer.arrow) && !otherPlayer.hurt)
					{
						otherPlayer.regenDelay = 2f;
						if (otherPlayer.armor <= 0)
						{
							otherPlayer.hp -= 1;
							otherPlayer.hurt = true;
							otherPlayer.armor += 10;
							if (otherPlayer.hp <= 0)
							{
								otherPlayer.dead = true;
								playerCount -= 1;
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
			if (!allPlayer.isVisible())
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
		font12.draw(batch, Keys.toString(player[0].controlUp), 150, 420);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		menu.dispose();
		character.dispose();
		game.dispose();
		end.dispose();
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
		return true;
	}

	public void keyDownInMenuStage(int keycode)
	{
		if (keycode == Keys.ENTER)
		{
			if (cursorPosition == 1)
			{
				screen = "character";
			}
			else if (cursorPosition == 2)
			{
				screen = "setting";
			}
			else if (cursorPosition == 3)
			{
				Gdx.app.exit();
			}
		}
		for (PlayerCharacter allPlayer : player)
		{
			if (keycode == allPlayer.controlDown)
			{
				cursorPosition += 1;
				if (cursorPosition > 3)
				{
					cursorPosition = 1;
				}
			}
			else if (keycode == allPlayer.controlUp)
			{
				cursorPosition -= 1;
				if (cursorPosition <= 0)// change this if there is more than 2 button
				{
					cursorPosition = 3;
				}
			}
			if (keycode == allPlayer.controlAttack)
			{
				if (cursorPosition == 1)
				{
					screen = "character";
				}
				else if (cursorPosition == 2)
				{
					screen = "setting";
				}
				else if (cursorPosition == 3)
				{
					Gdx.app.exit();
				}
			}
		}
		if (cursorPosition == 1)
		{
			temparrow.setY(350);
		}
		else if (cursorPosition == 2)
		{
			temparrow.setY(180);
		}
		else if (cursorPosition == 3)
		{
			temparrow.setY(80);
		}
	}

	public void keyDownInCharacterStage(int keycode)
	{
		if (keycode == Keys.ENTER && playerCount >= 2)
		{
			createMap();
			screen = "game";

		}
		else if (keycode == Keys.ESCAPE)
		{
			screen = "menu";
			resetVariableInCharacterStage();
		}
		for (int i = 0; i<4; i++)// change this i<2 to i<numberofplayer later 
		{
			if (keycode == player[i].controlAttack)
			{
				if (!playerCharacterSelect[i].isVisible())
				{
					playerCharacterSelect[i].setVisible(true);
					playerCount += 1;
					player[i].setIngame(true);
				}
				else
				{
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
					}
				}
				else if (keycode == player[i].controlRight)
				{
					if (characterIndex[i]+1 <= 4)// change 4 to number of character texture here
					{
						characterIndex[i] += 1;
					}
				}
				if (characterIndex[i] == 0)
				{
					playerCharacterSelect[i].setAnimation(PlayerCharacter.character1, "0001");
					player[i].setTexture(PlayerCharacter.character1);
				}
				else if(characterIndex[i] == 1)
				{
					playerCharacterSelect[i].setAnimation(PlayerCharacter.character2, "0001");
					player[i].setTexture(PlayerCharacter.character2);
				}
				else if(characterIndex[i] == 2)
				{
					playerCharacterSelect[i].setAnimation(PlayerCharacter.character3, "0001");
					player[i].setTexture(PlayerCharacter.character3);
				}
				else if(characterIndex[i] == 3)
				{
					playerCharacterSelect[i].setAnimation(PlayerCharacter.character4, "0001");
					player[i].setTexture(PlayerCharacter.character4);
				}
			}
		}
	}

	public void keyDownInGameStage(int keycode)
	{
		if (keycode == Keys.ESCAPE)
		{
			screen = "pause";
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
			screen = "game";
			// end all lingering input
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
			if (pauseCursorPosition == 1)// continue
			{
				screen = "game";
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
				// change to setting screen
			}
			else if (pauseCursorPosition == 3)// go back to menu
			{
				screen = "menu";
				resetVariableInCharacterStage();
				resetVariableInGameStage();
			}
		}
		for (PlayerCharacter allPlayer : player)
		{
			if (keycode == allPlayer.controlDown)
			{
				pauseCursorPosition += 1;
				if (pauseCursorPosition > 3)
				{
					pauseCursorPosition -= 1;
				}
			}
			else if (keycode == allPlayer.controlUp)
			{
				pauseCursorPosition -= 1;
				if (pauseCursorPosition < 1)
				{
					pauseCursorPosition += 1;
				}
			}
			else if (keycode == allPlayer.controlAttack)
			{
				if (pauseCursorPosition == 1)// continue
				{
					screen = "game";
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
					// change to setting screen
				}
				else if (pauseCursorPosition == 3)// go back to menu
				{
					screen = "menu";
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
		return false;
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
		return false;
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
				item.dropped = false;
				item.setVisible(item.dropped);
				ItemDrop.dropCount -= 1;
				item.hitbox.setX(-1000);
				item.hitbox.setY(-1000);
			}
		}
		for (PlayerCharacter allPlayer : player) {
			if (allPlayer == playerAttack || !allPlayer.isVisible())
			{
				continue;
			}
			if (checkCollision(playerAttack, allPlayer, "attack") && !allPlayer.hurt)
			{
				allPlayer.regenDelay = 2f;
				if (allPlayer.armor <= 0)
				{
					allPlayer.hp -= 1;
					allPlayer.hurt = true;
					allPlayer.armor += 10;
					if (allPlayer.hp <= 0)
					{
						allPlayer.dead = true;
						playerCount -= 1;
					}
				}
				else
				{
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
			allPlayer.weaponLV = 0;
			//allPlayer.weaponAtlas = PlayerWeapon.fist;
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

}
