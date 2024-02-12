package ru.alexp.gameState;

import ru.alexp.Config;
import ru.alexp.Game;
import ru.alexp.GamePanel;
import ru.alexp.types.AtlasType;
import ru.alexp.types.GameState;
import ru.alexp.ui.AudioOptions;
import ru.alexp.ui.UrmButton;
import ru.alexp.utils.ResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static ru.alexp.utils.ButtonUtils.isHoveredOverButton;

public class Settings extends StateBase implements GameStateActions, Drawable{
    private final AudioOptions audioOptions;
    private BufferedImage background;
    private BufferedImage settingsOverlayBackground;
    private int settingsBackgroundX;
    private int settingsBackgroundY;
    private int settingsBackgroundWidth;
    private int settingsBackgroundHeight;
    private UrmButton menuButton;

    public Settings(Game game) {
        super(game);
        audioOptions = game.getAudioOptions();
        preloadAssets();
        initButtons();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(background, 0, 0, GamePanel.getWindowWidth(), GamePanel.getWindowHeight(), null);

        graphics.drawImage(settingsOverlayBackground,
                settingsBackgroundX,
                settingsBackgroundY,
                settingsBackgroundWidth,
                settingsBackgroundHeight,
                null);

        menuButton.render(graphics);
        audioOptions.render(graphics);
    }

    @Override
    public void update() {
        menuButton.update();
        audioOptions.update();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isHoveredOverButton(e, menuButton.getHitBox()) && menuButton.isMousePressed()) {
            game.getPlaying().loadFirstLevelResources();
            GameState.setState(GameState.MENU);
        } else {
            audioOptions.mouseReleased(e);
        }

        menuButton.resetButtonState();
        audioOptions.resetAudioButtonsState();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // reset mouseHovered to update button animation
        menuButton.setMouseHovered(false);
        audioOptions.setHoverFalse();

        if (isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMouseHovered(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            game.getPlaying().loadFirstLevelResources();
            GameState.setState(GameState.MENU);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void preloadAssets() {
        background = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_MENU_BACKGROUND);
        settingsOverlayBackground = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_BACKGROUND_SETTINGS_OVERLAY);

        settingsBackgroundWidth = (int) (settingsOverlayBackground.getWidth() * Game.SCALE);
        settingsBackgroundHeight = (int) (settingsOverlayBackground.getHeight() * Game.SCALE);
        settingsBackgroundX = (GamePanel.getWindowWidth() / 2) - (settingsBackgroundWidth / 2);
        settingsBackgroundY = (GamePanel.getWindowHeight() / 2) - (settingsBackgroundHeight / 2);
    }

    private void initButtons() {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (325 * Game.SCALE);
        menuButton = new UrmButton(menuX, menuY, Config.URM_BUTTON_WIDTH, Config.URM_BUTTON_HEIGHT, 2);
    }
}
