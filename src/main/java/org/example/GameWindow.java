package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JFrame jFrame;

    public GameWindow(GamePanel gamePanel) {
        log.debug("GameWindow initialized");

        this.jFrame = new JFrame();
        // kill app when jFrame window is closed by user
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(gamePanel);
        // show the window at the center of the screen
        jFrame.setLocationRelativeTo(null);
        // set the size of the window to the size of its components (i.e. GamePanel)
        jFrame.pack();
        jFrame.setResizable(false);
        jFrame.setVisible(true);

        jFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().pauseGame();
            }
        });
    }
}
