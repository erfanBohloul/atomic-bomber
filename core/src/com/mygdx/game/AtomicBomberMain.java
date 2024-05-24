package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.controller.AppPreferences;
import com.mygdx.game.loader.GameAssetManager;
import com.mygdx.game.model.account.User;
import com.mygdx.game.views.*;

public class AtomicBomberMain extends Game {
	SpriteBatch batch;
	Texture img;
	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	public final static int MAIN = 4;
	public final static int LOGIN = 5;
	public final static int PROFILE = 6;
	public final static int AVATAR = 7;

	private LoadingScreen loadingScreen;
	private MenuScreen menuScreen;
	private MainScreen mainScreen;
	private LoginScreen loginScreen;
	private PreferenceScreen preferencesScreen;
	private ProfileMenu profileMenu;
	private AvatarScreen avatarScreen;

	public GameAssetManager assetManager = new GameAssetManager();

	private AppPreferences preferences;

	@Override
	public void create () {
		loadingScreen = new LoadingScreen(this);
		preferences = new AppPreferences();
		setScreen(loadingScreen);
	}

	@Override
	public void dispose() {
		assetManager.manager.dispose();
	}

	public void changScreen(int screen, User user, int caller) {
		switch(screen){
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this, user);
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if (preferencesScreen == null) preferencesScreen = new PreferenceScreen(this, user, caller);
				this.setScreen(preferencesScreen);
				break;
			case APPLICATION:
				if (mainScreen == null) mainScreen = new MainScreen(this, user);
				this.setScreen(mainScreen);
				break;
			case PROFILE:
				if (profileMenu == null) profileMenu = new ProfileMenu(this, user);
				this.setScreen(profileMenu);
				break;
			case LOGIN:
				if (loginScreen == null) loginScreen = new LoginScreen(this);
				this.setScreen(loginScreen);
				break;

			case AVATAR:
				if (avatarScreen == null) avatarScreen = new AvatarScreen(this, user);
				this.setScreen(avatarScreen);
				break;
		}
	}

	public void changeResetScreen(int screen, User user) {
		switch (screen) {
			case MENU:
				menuScreen = new MenuScreen(this, user);
				this.setScreen(menuScreen);
				break;
//			case PREFERENCES:
//				preferencesScreen = new PreferencesScreen(this);
//				this.setScreen(preferencesScreen);
//				break;
			case APPLICATION:
				mainScreen = new MainScreen(this, user);
				this.setScreen(mainScreen);
				break;
//			case ENDGAME:
//				endScreen = new EndScreen(this);
//				this.setScreen(endScreen);
//				break;
			case LOGIN:
				loginScreen = new LoginScreen(this);
				this.setScreen(loginScreen);
				break;
		}
	}

	public AppPreferences getPreferences() {
		return preferences;
	}
}
