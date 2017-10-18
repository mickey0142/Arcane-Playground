package game.arcaneplayground.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.arcaneplayground.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1350;
		config.height = 750;
		config.fullscreen = false;
		config.forceExit = true;
		config.addIcon("icon.png", FileType.Internal);
		new LwjglApplication(new MainGame(), config);
	}
}
