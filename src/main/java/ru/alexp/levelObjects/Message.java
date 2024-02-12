package ru.alexp.levelObjects;

import ru.alexp.Config;
import ru.alexp.gameState.Drawable;
import ru.alexp.types.LevelObjectType;
import ru.alexp.types.MessageType;

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
