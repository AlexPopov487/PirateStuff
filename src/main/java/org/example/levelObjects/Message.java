package org.example.levelObjects;

import org.example.Config;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;
import org.example.types.MessageType;

import java.awt.*;

public class Message extends LevelObject implements Drawable {
    private MessageType messageType;

    public Message(float x, float y, MessageType messageType) {
        super(x, y, LevelObjectType.MESSAGE_ONBOARDING, false);
        initHitBox(Config.SCRIPT_MESSAGE_WIDTH, Config.SCRIPT_MESSAGE_HEIGHT);
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
    }
}
