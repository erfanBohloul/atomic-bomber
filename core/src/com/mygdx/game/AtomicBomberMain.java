package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.loader.GameAssetManager;
import com.mygdx.game.views.LoadingScreen;
import com.mygdx.game.views.MainScreen;
import com.mygdx.game.views.MenuScreen;
import com.mygdx.game.views.PreferenceScreen;

public class AtomicBomberMain extends Game {
	SpriteBatch batch;
	Texture img;
	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	public final static int MAIN = 4;

	private LoadingScreen loadingScreen;
	private MenuScreen menuScreen;
	private MainScreen mainScreen;

	public GameAssetManager assetManager = new GameAssetManager();

	@Override
	public void create () {
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	@Override
	public void dispose() {
		assetManager.manager.dispose();
	}

	public void changScreen(int screen) {
		switch(screen){
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
//			case PREFERENCES:
//				if (preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
//				this.setScreen(preferencesScreen);
//				break;
			case APPLICATION:
				if (mainScreen == null) mainScreen = new MainScreen(this);
				this.setScreen(mainScreen);
				break;
//			case ENDGAME:
//				if (endScreen == null) endScreen = new EndScreen(this);
//				this.setScreen(endScreen);
//				break;
		}
	}
}
