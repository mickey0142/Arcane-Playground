package game.arcaneplayground;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class MainGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img;
	String screen = "menu";
	PlayerCharacter player[] = new PlayerCharacter[2];
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
	EffectRenderer attackEffectRenderer[] = new EffectRenderer[2];
	PlayerWeapon playerWeaponRenderer[] = new PlayerWeapon[2];
	GameObject checkBlock[] = new GameObject[2];

	ItemDrop itemDrop[];
	UI playerChargeBar[] = new UI[2];
	UI playerHPBar[] = new UI[2];
	UI playerArmorBar[] = new UI[2];
	Stage end;
	float gametime;//temp
	// default control for each player in this order {up, down, left, right, attack} change this in setting later
	int controlKeeper[][] = {{Keys.W, Keys.S, Keys.A, Keys.D, Keys.F}, {Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_LEFT}};
	int playerCount;

	@Override
	public void create () {
		batch = new SpriteBatch();

		menu = new Stage(new FitViewport(1350, 750));
		character = new Stage(new FitViewport(1350, 750));
		end = new Stage(new FitViewport(1350, 750));

		screen = "menu";

		itemDrop = new ItemDrop[10];
		for (int i = 0; i < 10; i++)
		{
			itemDrop[i] = new ItemDrop();
		}
		NormalWall.itemdrop = itemDrop;

		createInMenuStage();
		createInCharacterStage();
		createMap();
		createInGameStage();
		createInEndStage();

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
		playerHPBar[0] = new UI("heart.png", 100, 690, 150, 40);
		playerHPBar[1] = new UI("heart.png", 400, 690, 150, 40);
		playerArmorBar[0] = new UI ("box3.png", 100, 660, 10, 15);
		playerArmorBar[1] = new UI ("box3.png", 400, 660, 10, 15);
		playerChargeBar[0] = new UI("whitebox.png", 0, 0, 60, 10, true);
		playerChargeBar[1] = new UI("whitebox.png", 0, 0, 60, 10, true);
		player[0] = new PlayerCharacter(50, 50, 60, 60, Keys.W, Keys.S, Keys.A, Keys.D, Keys.F, playerHPBar[0], playerArmorBar[0], PlayerWeapon.sword, PlayerWeapon.swordAnim);

		// playerweapon.sword ^ here

		player[1] = new PlayerCharacter(1250, 550, 60, 60, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT, playerHPBar[1], playerArmorBar[1], PlayerWeapon.sword, PlayerWeapon.swordAnim);

		//temppppp 
		//player[0].weaponName = "axe";
		//player[1].weaponName = "spear";

		attackEffectRenderer[0] = new EffectRenderer(player[0]);
		attackEffectRenderer[0].setValue(player[0].attackHitbox.getX(), player[0].attackHitbox.getY(), player[0].attackHitbox.getWidth(), player[0].attackHitbox.getHeight(), player[0].direction);
		attackEffectRenderer[1] = new EffectRenderer(player[1]);
		attackEffectRenderer[1].setValue(player[1].attackHitbox.getX(), player[1].attackHitbox.getY(), player[1].attackHitbox.getWidth(), player[1].attackHitbox.getHeight(), player[1].direction);
		playerWeaponRenderer[0] = new PlayerWeapon(player[0]);
		playerWeaponRenderer[1] = new PlayerWeapon(player[1]);
		player[0].setPlayerAttackEffectRenderer(attackEffectRenderer[0]);
		player[1].setPlayerAttackEffectRenderer(attackEffectRenderer[1]);
		player[0].setPlayerWeaponRenderer(playerWeaponRenderer[0]);
		player[1].setPlayerWeaponRenderer(playerWeaponRenderer[1]);
		player[0].setChargeBar(playerChargeBar[0]);
		player[1].setChargeBar(playerChargeBar[1]);
		player[0].setCheckBlockObject(checkBlock[0]);
		player[1].setCheckBlockObject(checkBlock[1]);
		player[0].updateHitbox();
		player[1].updateHitbox();
		player[0].updateCheckBlockPosition(player[0].hitbox.getX(), player[0].hitbox.getY(), player[0].hitbox.getWidth(), player[0].hitbox.getHeight());
		player[1].updateCheckBlockPosition(player[1].hitbox.getX(), player[1].hitbox.getY(), player[1].hitbox.getWidth(), player[1].hitbox.getHeight());

		// ui in stage
		characterBackground = new UI("characterbackground.jpg", 0, 0, 1350, 750);
		playerCharacterSelect =  new UI[2];
		playerCharacterSelect[0] = new UI("character1.png", 200, 600, 60, 60);
		playerCharacterSelect[0].animation = true;
		playerCharacterSelect[0].setAnimation(PlayerCharacter.character1, "0001");
		playerCharacterSelect[1] = new UI("character1.png", 400, 600, 60, 60);
		playerCharacterSelect[1].animation = true;
		playerCharacterSelect[1].setAnimation(PlayerCharacter.character1, "0001");

		character.addActor(characterBackground);
		character.addActor(playerCharacterSelect[0]);
		playerCharacterSelect[0].setVisible(false);
		character.addActor(playerCharacterSelect[1]);
		playerCharacterSelect[1].setVisible(false);
	}

	public void createInGameStage()
	{
		game = new Stage(new FitViewport(1350, 750));
		gameBackground = new UI("gamebackground.jpg", 0, 0, 1350, 750, false);
		playGround = new GameObject("playground.png", 25, 0, 1300, 650, false);

		// add all actor for game stage in here. adding order should be background playground itemdrop wall player playerweapon playerattackeffect
		game.addActor(gameBackground);
		game.addActor(playGround);

		for (int i = 0; i < 10; i++)// change 10 to how many itemdrop hereeeeeee
		{
			game.addActor(itemDrop[i]);
		}

		game.addActor(playerHPBar[0]);
		game.addActor(playerHPBar[1]);
		game.addActor(playerArmorBar[0]);
		game.addActor(playerArmorBar[1]);

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

		//		int random = (int)(Math.random()*2);

		//		normalWalls = new NormalWall[50];
		//		normalWalls[0] = new NormalWall("block2.png", 500, 50, 50, 50, true);
		//		
		//		game.addActor(normalWalls[50]);
		//		game.addActor(normalWalls[51]);

		for(NormalWall normalWall : normalWalls)
		{
			game.addActor(normalWall);
		}

		game.addActor(player[0]);
		game.addActor(player[1]);
		game.addActor(playerWeaponRenderer[0]);
		game.addActor(playerWeaponRenderer[1]);

		game.addActor(playerChargeBar[0]);
		game.addActor(playerChargeBar[1]);

		//temp remove this latertemp remove this latertemp remove this latertemp remove this latertemp remove this latertemp remove this latertemp remove this later
		game.addActor(checkBlock[0]);
		game.addActor(checkBlock[1]);

		game.addActor(attackEffectRenderer[0]);
		game.addActor(attackEffectRenderer[1]);
	}

	public void createMap()
	{
		// set textureatlas for block here change wall1 to input
		NormalWall.wallTexture = NormalWall.wall1;
		NormalWall.hp3 = NormalWall.wall1.findRegion("0001");
		NormalWall.hp2 = NormalWall.wall1.findRegion("0002");
		NormalWall.hp1 = NormalWall.wall1.findRegion("0003");
		NormalWall.hp0 = NormalWall.wall1.findRegion("0004");
		//int unbreakWallCount = 3;
		int normalWallCount = 5;
		//		walls = new UnbreakableWall[unbreakWallCount];
		//		walls[0] = new UnbreakableWall("block.png", 400, 400, 64, 64, true);
		//		walls[1] = new UnbreakableWall("block.png", 128, 128, 64, 64, true);
		//		walls[2] = new UnbreakableWall("block.png", 0, 128, 64, 64, true);
		//		float wallLocation[][] = {{0,128},{128, 128},{400, 400}};
		//		for (int i = 0; i < unbreakWallCount; i++)
		//		{
		//			walls[i] = new UnbreakableWall("block.png", wallLocation[i][0], wallLocation[i][1], 50, 50, true);
		//		}
		normalWalls = new NormalWall[5];
		//		normalWalls[0] = new NormalWall("block2.png", 300, 300, 64, 64, true);
		//		normalWalls[1] = new NormalWall("block2.png", 500, 500, 64, 64, true);
		//		normalWalls[2] = new NormalWall("block2.png", 700, 300, 64, 64, true);
		float wallLocation2[][] = {{50, 150},{50, 200},{700, 350},{750, 350},{850, 300}};
		for (int i = 0; i < normalWallCount; i++)
		{
			normalWalls[i] = new NormalWall("block2.png", wallLocation2[i][0], wallLocation2[i][1], 50, 50, true);
		}
	}

	public void createInEndStage()
	{

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
			}
		}
		else if (screen.equals("end"))
		{
			endStageRender();
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
		for (PlayerCharacter allPlayer : player) 
		{
			if (!allPlayer.moving)
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
		for (PlayerCharacter allPlayer : player) 
		{
			if (allPlayer.attacking)
			{
				checkPlayerAttack(allPlayer);
				attackEffectRenderer[loopCount].setValue(allPlayer.attackHitbox.getX(), allPlayer.attackHitbox.getY(), allPlayer.attackHitbox.getWidth(), allPlayer.attackHitbox.getHeight(), allPlayer.direction);
				attackEffectRenderer[loopCount].check = true;
				attackEffectRenderer[loopCount].time = 0;
				allPlayer.attacking = false;
			}
			loopCount += 1;
		}
		for (PlayerCharacter allPlayer : player)
		{
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
//					if (otherPlayer == allPlayer)
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
				Gdx.app.exit();
			}
		}
		else if (keycode == Keys.DOWN)
		{
			cursorPosition += 1;
			if (cursorPosition > 2)
			{
				cursorPosition = 1;
			}
		}
		else if (keycode == Keys.UP)
		{
			cursorPosition -= 1;
			if (cursorPosition <= 0)// change this if there is more than 2 button
			{
				cursorPosition = 2;
			}
		}
		if (cursorPosition == 1)
		{
			temparrow.setY(350);
		}
		else if (cursorPosition == 2)
		{
			temparrow.setY(100);
		}
	}

	public void keyDownInCharacterStage(int keycode)
	{
		if (keycode == Keys.ENTER && playerCount >= 2)
		{
			screen = "game";

		}
		else if (keycode == Keys.ESCAPE)
		{
			screen = "menu";
			resetVariableInCharacterStage();
		}
		for (int i = 0; i<2; i++)// change this i<2 to i<numberofplayer later 
		{
			if (keycode == player[i].controlAttack)
			{
				if (!playerCharacterSelect[i].isVisible())
				{
					playerCharacterSelect[i].setVisible(true);
					playerCount += 1;
				}
				else
				{
					playerCharacterSelect[i].setVisible(false);
					playerCount -= 1;
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
		for (PlayerCharacter allPlayer : player) {
			//			if (allPlayer.charging)
			//			{
			//				continue;
			//			}// comment this make player able to change direction of attack while charging}
			if (allPlayer.dead  || playerCount <= 1)
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
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
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
			if (allPlayer == playerAttack)
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
		}
	}
}
