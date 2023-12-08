package org.example;

import org.example.inputs.KeyboardInputs;
import org.example.inputs.MouseInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Game game;


    public GamePanel(Game game) {
        this.game = game;
        addInputListeners();


        setPanelSize();
    }

    private void addInputListeners() {
        addKeyListener(new KeyboardInputs(this));
        MouseInputs mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    /*
     the resolution (or the size) should be set from game panel and not from game window. Otherwise, the size will
     include window borders
    */
    private void setPanelSize() {
        setPreferredSize(new Dimension(1280, 800));
    }

    /* For the sake of understanding, refer to a Graphics g as a Brush with which we can draw
    * something inside JPanel */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public void updateGameLogic() {

    }

    public Game getGame() {
        return game;
    }
}
