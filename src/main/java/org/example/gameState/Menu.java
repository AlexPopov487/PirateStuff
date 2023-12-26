package org.example.gameState;

import org.example.Game;
import org.example.GamePanel;
import org.example.ui.MenuButton;
import org.example.utils.AtlasType;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static org.example.Game.SCALE;
import static org.example.utils.ButtonUtils.isHoveredOverButton;

public class Menu extends StateBase implements GameStateActions, Drawable {

    private MenuButton[] menuButtons = new MenuButton[3];
    private BufferedImage menuTemplateImg;
    private BufferedImage menuBackground;
    private int menuX;
    private int menuY;
    private int menuHeight;
    private int menuWidth;

    public Menu(Game game) {
        super(game);
        preloadButtons();
        preloadImages();
    }

    @Override
    public void update() {
        for (MenuButton menuButton : menuButtons) {
            menuButton.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(menuBackground, 0, 0, GamePanel.getWindowWidth(), GamePanel.getWindowHeight(), null);

        g.drawImage(menuTemplateImg, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton menuButton : menuButtons) {
            menuButton.render(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton menuButton : menuButtons) {
            if (isHoveredOverButton(e, menuButton.getHitBox())) {
                menuButton.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton menuButton : menuButtons) {
            // make sure that user clicked and released(!) mouse while staying withing the menu button bounds
            if (isHoveredOverButton(e, menuButton.getHitBox()) && menuButton.isMousePressed()) {
                menuButton.updateGameState();
                break;
            }
        }
        resetMenuButtonStates();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton menuButton : menuButtons) {
            menuButton.setMouseOver(false);
        }

        for (MenuButton menuButton : menuButtons) {
            if (isHoveredOverButton(e, menuButton.getHitBox())) {
                menuButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void preloadButtons() {
        menuButtons[0] = new MenuButton(GamePanel.getWindowWidth() / 2, (int) (150 * SCALE), 0, GameState.PLAYING);
        menuButtons[1] = new MenuButton(GamePanel.getWindowWidth() / 2, (int) (220 * SCALE), 1, GameState.OPTIONS);
        menuButtons[2] = new MenuButton(GamePanel.getWindowWidth() / 2, (int) (290 * SCALE), 2, GameState.QUIT);

    }

    private void preloadImages() {
        menuTemplateImg = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_MENU_TEMPLATE);
        menuWidth = (int) (menuTemplateImg.getWidth() * SCALE);
        menuHeight = (int) (menuTemplateImg.getHeight() * SCALE);
        menuX = GamePanel.getWindowWidth() / 2 - menuWidth / 2;
        menuY = (int) (45 * SCALE);

        menuBackground = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_MENU_BACKGROUND);
    }

    private void resetMenuButtonStates() {
        for (MenuButton menuButton : menuButtons) {
            menuButton.resetMenuButtonState();
        }
    }
}
