package com.mygdx.game.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    public final String playerImage = "images/img.png",
        floorImage = "images/floor.jpg",
        tankImage = "images/tank.png",
        clusterImage = "images/cluster.png",
        treeImage = "images/tree.png",
        buildingImage = "images/building.png",
        nukeImage = "images/nukebomb.png",
        planeImage = "images/plane.png",
        truckImage = "images/truck.png",
        freezeImage = "images/freezeBar.png",
        bonusAtomic = "images/bonusnuke.png",
        bonusCluster = "images/bonuscluster.png";

    public final String skin = "skin/glassy-ui.json";

    public void queueAddImages(){
        manager.load(playerImage, Texture.class);
        manager.load(floorImage, Texture.class);
        manager.load(tankImage, Texture.class);
        manager.load(clusterImage, Texture.class);
        manager.load(treeImage, Texture.class);
        manager.load(buildingImage, Texture.class);
        manager.load(nukeImage, Texture.class);
        manager.load(truckImage, Texture.class);
        manager.load(planeImage, Texture.class);
        manager.load(freezeImage, Texture.class);
        manager.load(bonusAtomic, Texture.class);
        manager.load(bonusCluster, Texture.class);
    }


    public void queueAddSkin() {
        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
        manager.load(skin, Skin.class, params);
    }
}
