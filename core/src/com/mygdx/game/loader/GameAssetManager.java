package com.mygdx.game.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    public final String floorImage = "images/floor.jpg",
        tankImage = "images/tank.png",
        clusterImage = "images/cluster.png",
        treeImage = "images/tree.png",
        buildingImage = "images/building.png",
        nukeImage = "images/nukebomb.png",
        planeImage = "images/plane.png",
        truckImage = "images/truck.png",
        freezeImage = "images/freezeBar.png",
        bonusAtomic = "images/bonusnuke.png",
        bonusCluster = "images/bonuscluster.png",
        bunkerImage = "images/bunker.png",
        migImage = "images/mig.png";

    public final String avatar1 = "images/avatar1.png",
            avatar2 = "images/avatar2.jpeg",
            avatar3 = "images/avatar3.png",
            avatar4 = "images/avatar4.png";

    public final String skin = "skin/glassy-ui.json";

    public void queueAddImages(){
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
        manager.load(bunkerImage, Texture.class);
        manager.load(migImage, Texture.class);
    }

    public void queueAddAvatar() {
        manager.load(avatar1, Texture.class);
        manager.load(avatar2, Texture.class);
        manager.load(avatar3, Texture.class);
        manager.load(avatar4, Texture.class);
    }


    public void queueAddSkin() {
        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
        manager.load(skin, Skin.class, params);
    }
}
