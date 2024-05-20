package com.mygdx.game.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    public final String playerImage = "images/img.png",
        floorImage = "images/floor.jpg",
        tankImage = "images/tank.png";

    public final String skin = "skin/glassy-ui.json";

    public void queueAddImages(){
        manager.load(playerImage, Texture.class);
        manager.load(floorImage, Texture.class);
        manager.load(tankImage, Texture.class);
    }


    public void queueAddSkin() {
        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
        manager.load(skin, Skin.class, params);
    }
}
