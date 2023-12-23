package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
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
        positionWindowAtTheCenter();

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

    private void positionWindowAtTheCenter() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();

        // Get the bounds of the first monitor
        Rectangle screen = gd[0].getDefaultConfiguration().getBounds();

        int screenCenterX = screen.width / 2;
        int screenCenterY = screen.height / 2;
        int jFrameWindowCenterX = jFrame.getPreferredSize().width / 2;
        int jFrameWindowCenterY = jFrame.getPreferredSize().height / 2;

        int x = screen.x + screenCenterX - jFrameWindowCenterX;
        int y = screen.y + screenCenterY - jFrameWindowCenterY;

        jFrame.setLocation(x, y);
    }
}
