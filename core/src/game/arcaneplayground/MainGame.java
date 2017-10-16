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
	UI menuBackground;
	UI temparrow;
	Stage character;
	UI characterBackground;
	Stage game;
	GameObject playGround;
	UI gameBackground;
	UnbreakableWall walls[];
	NormalWall normalWalls[];
	PlayerCharacter checkCollisionTemp;
	EffectRenderer attackEffectRenderer[] = new EffectRenderer[2];
	PlayerWeapon playerWeaponRenderer[] = new PlayerWeapon[2];
	ItemDrop itemDrop[];
	UI playerHPBar[] = new UI[2];
	Stage end;
	float gametime;//temp
	// default control for each player in this order {up, down, left, right, attack} change this in setting later
	int controlKeeper[][] = {{Keys.W, Keys.S, Keys.A, Keys.D, Keys.F}, {Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_LEFT}};
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		checkCollisionTemp = new PlayerCharacter();
		
		menu = new Stage(new FitViewport(1350, 750));
		character = new Stage(new FitViewport(1350, 750));
		end = new Stage(new FitViewport(1350, 750));
		
		screen = "menu";// use this to skip to game stage. remove this out after finishing menu and character stage
		
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
		playerHPBar[0] = new UI("hp.png", 20, 700, 100, 20, true);
		playerHPBar[1] = new UI("hp.png", 200, 700, 100, 20, true);
		player[0] = new PlayerCharacter(25, 5, 60, 60, Keys.W, Keys.S, Keys.A, Keys.D, Keys.F, playerHPBar[0], PlayerWeapon.fist, PlayerWeapon.fistAnim);
		player[1] = new PlayerCharacter(1270, 570, 60, 60, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT, playerHPBar[1], PlayerWeapon.fist, PlayerWeapon.fistAnim);
		player[0].setTexture(PlayerCharacter.pirate);
		player[1].setTexture(PlayerCharacter.cyclop);
		attackEffectRenderer[0] = new EffectRenderer(player[0]);
		attackEffectRenderer[0].setValue(player[0].attackHitbox.getX(), player[0].attackHitbox.getY(), player[0].attackHitbox.getWidth(), player[0].attackHitbox.getHeight(), player[0].direction);
		attackEffectRenderer[1] = new EffectRenderer(player[1]);
		attackEffectRenderer[1].setValue(player[1].attackHitbox.getX(), player[1].attackHitbox.getY(), player[1].attackHitbox.getWidth(), player[1].attackHitbox.getHeight(), player[1].direction);
		playerWeaponRenderer[0] = new PlayerWeapon(player[0]);
		playerWeaponRenderer[1] = new PlayerWeapon(player[1]);
		player[0].setPlayerAttackEffectRenderer(attackEffectRenderer[0]);
		player[1].setPlayerAttackEffectRenderer(attackEffectRenderer[1]);
		
		// ui in stage
		characterBackground = new UI("characterbackground.jpg", 0, 0, 1350, 750);
		character.addActor(characterBackground);
	}
	
	public void createInGameStage()
	{
		game = new Stage(new FitViewport(1350, 750));
		gameBackground = new UI("gamebackground.jpg", 0, 0, 1350, 750, false);
		playGround = new GameObject("playground.jpg", 25, 0, 1300, 650, false);
		
		// add all actor for game stage in here. adding order should be background playground itemdrop wall player playerweapon playerattackeffect
		game.addActor(gameBackground);
		game.addActor(playGround);
		
		game.addActor(itemDrop[0]);
		game.addActor(itemDrop[1]);
		game.addActor(itemDrop[2]);
		
		game.addActor(playerHPBar[0]);
		game.addActor(playerHPBar[1]);
		
		game.addActor(player[0]);
		game.addActor(player[1]);
		game.addActor(playerWeaponRenderer[0]);
		game.addActor(playerWeaponRenderer[1]);
		
		game.addActor(walls[0]);
		game.addActor(walls[1]);
		game.addActor(walls[2]);
		game.addActor(normalWalls[0]);
		game.addActor(normalWalls[1]);
		game.addActor(normalWalls[2]);
		game.addActor(normalWalls[3]);
		game.addActor(normalWalls[4]);
		game.addActor(attackEffectRenderer[0]);
		game.addActor(attackEffectRenderer[1]);
	}
	
	public void createMap()
	{
		int unbreakWallCount = 3;
		int normalWallCount = 5;
		walls = new UnbreakableWall[unbreakWallCount];
//		walls[0] = new UnbreakableWall("block.png", 400, 400, 64, 64, true);
//		walls[1] = new UnbreakableWall("block.png", 128, 128, 64, 64, true);
//		walls[2] = new UnbreakableWall("block.png", 0, 128, 64, 64, true);
		float wallLocation[][] = {{0,128},{128, 128},{400, 400}};
		for (int i = 0; i < unbreakWallCount; i++)
		{
			walls[i] = new UnbreakableWall("block.png", wallLocation[i][0], wallLocation[i][1], 50, 50, true);
		}
		normalWalls = new NormalWall[5];
//		normalWalls[0] = new NormalWall("block2.png", 300, 300, 64, 64, true);
//		normalWalls[1] = new NormalWall("block2.png", 500, 500, 64, 64, true);
//		normalWalls[2] = new NormalWall("block2.png", 700, 300, 64, 64, true);
		float wallLocation2[][] = {{300, 300},{500, 500},{700, 300},{750, 300},{800, 300}};
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
		for (PlayerCharacter allPlayer : player) 
		{				
			checkCollisionTemp.setWidth(allPlayer.getWidth());
			checkCollisionTemp.setHeight(allPlayer.getHeight());
			checkCollisionTemp.setNewRect();
			boolean movex = true;
			checkCollisionTemp.setX(allPlayer.getX()+allPlayer.speed_x);
			checkCollisionTemp.setY(allPlayer.getY());
			checkCollisionTemp.updateHitbox();
			if (!checkContain(checkCollisionTemp))
			{
				movex = false;
			}
			for (GameObject wall : walls) {
				if (checkCollision(checkCollisionTemp, wall))
				{
					movex = false;
					break;
				}
			}
			for (GameObject wall : normalWalls) {
				if (checkCollision(checkCollisionTemp, wall))
				{
					movex = false;
					break;
				}
			}
			for (PlayerCharacter OtherPlayer : player) {
				if (OtherPlayer == allPlayer)
				{
					continue;
				}
				else if (checkCollision(checkCollisionTemp, OtherPlayer))
				{
					movex = false;
					break;
				}
			}
			boolean movey = true;
			checkCollisionTemp.setX(allPlayer.getX());
			checkCollisionTemp.setY(allPlayer.getY()+allPlayer.speed_y);
			checkCollisionTemp.updateHitbox();
			if (!checkContain(checkCollisionTemp))
			{
				movey = false;
			}
			for (GameObject wall : walls) {
				if (checkCollision(checkCollisionTemp, wall))
				{
					movey = false;
					break;
				}
			}
			for (GameObject wall : normalWalls) {
				if (checkCollision(checkCollisionTemp, wall))
				{
					movey = false;
					break;
				}
			}
			for (PlayerCharacter OtherPlayer : player) {
				if (OtherPlayer == allPlayer)
				{
					continue;
				}
				else if (checkCollision(checkCollisionTemp, OtherPlayer))
				{
					movey = false;
					break;
				}
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
			if(movex && !allPlayer.charging)
			{
				allPlayer.setX(allPlayer.getX() + allPlayer.speed_x);
				allPlayer.updateHitbox();
			}
			if(movey && !allPlayer.charging)
			{
				allPlayer.setY(allPlayer.getY() + allPlayer.speed_y);
				allPlayer.updateHitbox();
			}
			//System.out.println(player[0].currentAttackCooldown);
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
	}
	
	public void endStageRender()
	{
		
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
			if (temparrow.getY() == 350)
			{
				screen = "character";
			}
			else if (temparrow.getY() == 100)
			{
				Gdx.app.exit();
			}
		}
		else if (keycode == Keys.DOWN)
		{
			temparrow.setY(100);
		}
		else if (keycode == Keys.UP)
		{
			temparrow.setY(350);
		}
	}
	
	public void keyDownInCharacterStage(int keycode)
	{
		if (keycode == Keys.ENTER)
		{
			screen = "game";
		}
	}
	
	public void keyDownInGameStage(int keycode)
	{
		for (PlayerCharacter allPlayer : player) {
//			if (allPlayer.charging)
//			{
//				continue;
//			}// comment this make player able to change direction of attack while charging}
			if (keycode == allPlayer.controlLeft)
			{
				allPlayer.speedLeft = 5;
				allPlayer.direction = "left";
			}
			if (keycode == allPlayer.controlRight)
			{
				allPlayer.speedRight = 5;
				allPlayer.direction = "right";
			}
			if (keycode ==  allPlayer.controlUp)
			{
				allPlayer.speedUp = 5;
				allPlayer.direction = "up";
			}
			if (keycode == allPlayer.controlDown)
			{
				allPlayer.speedDown = 5;
				allPlayer.direction = "down";
			}
			allPlayer.speed_x = allPlayer.speedRight - allPlayer.speedLeft;
			allPlayer.speed_y = allPlayer.speedUp - allPlayer.speedDown;
			if (keycode == allPlayer.controlAttack)
			{
				if (allPlayer.currentAttackCooldown <= 0 && !allPlayer.charging)
				{
					allPlayer.currentChargeTime = allPlayer.attackChargeTime;
					allPlayer.charging = true;
				}
			}
		}
	}
	
	public void keyDownInEndStage(int keycode)
	{
		
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
				allPlayer.speedLeft = 0;
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
				allPlayer.speedRight = 0;
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
				allPlayer.speedUp = 0;
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
				allPlayer.speedDown = 0;
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
			allPlayer.speed_x = allPlayer.speedRight - allPlayer.speedLeft;
			allPlayer.speed_y = allPlayer.speedUp - allPlayer.speedDown;
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
	
	public boolean checkCollision(PlayerCharacter player, ItemDrop item)
	{
		return player.hitbox.overlaps(item.hitbox);
	}
	
	public boolean checkCollision(PlayerCharacter player, PlayerCharacter player2)
	{
		return player.hitbox.overlaps(player2.hitbox);
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
					((NormalWall) wall).hp -= playerAttack.attack;// maybe change 1 to attack damage later
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
			if (checkCollision(playerAttack, allPlayer, "attack"))
			{
				allPlayer.hp -= playerAttack.attack;
				allPlayer.hurt = true;
			}
		}
	}
}
