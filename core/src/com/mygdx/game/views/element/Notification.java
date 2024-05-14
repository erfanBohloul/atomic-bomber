package com.mygdx.game.views.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Notification  extends Dialog {

    public enum NotificationType {
        SUCCESS, FAIL, INFO;
    }

    static Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
    Label label;

    public Notification(String text, NotificationType type) {
        super("", skin);
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 10);
        setPosition(0, Gdx.graphics.getHeight() + getHeight());
        label = new Label(text, skin);
        add(label).uniformX();

        label.setColor(Color.BLUE);
        setKeepWithinStage(false);
        show();
    }

    public void show() {

        MoveToAction moveTo = new MoveToAction();
        moveTo.setPosition(getX(), Gdx.graphics.getHeight() - getHeight());
        moveTo.setDuration(2f);

        MoveToAction moveBack = new MoveToAction();
        moveBack.setPosition(getX(), Gdx.graphics.getHeight() + getHeight());
        moveBack.setDuration(2f);


        SequenceAction action = new SequenceAction(moveTo, Actions.delay(1.0f), moveBack, Actions.removeActor());
        addAction(action);

    }
}

/*
 take this code from:
 https://gamedev.stackexchange.com/questions/124441/libgdx-notification-popup-something-like-toast-on-both-desktop-and-android
*/