package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pong extends JPanel {
    private static final int TABLE_WIDTH = 500;
    @Getter
    private static final int TABLE_HEIGHT = 300;
    private static final int TABLE_X = 20;
    @Getter
    private static final int TABLE_Y = 20;
    private static final int TABLE_CENTRE_X = TABLE_X + TABLE_WIDTH / 2;
    private static final int PADDLE_X_OFFSET = 20;
    private static final int PADDLE_WIDTH = 10;
    @Getter
    private static final int PADDLE_HEIGHT = 50;

    @Setter
    @Getter
    private int playerPaddleY  = TABLE_Y + TABLE_HEIGHT / 2 - PADDLE_HEIGHT / 2;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        TODO: make this look nicer and bevelled --> round rectangle
        g.drawRect(TABLE_X, TABLE_Y, TABLE_WIDTH, TABLE_HEIGHT);
        g.drawLine(TABLE_CENTRE_X, TABLE_Y, TABLE_CENTRE_X, TABLE_Y + TABLE_HEIGHT);
        g.fillRect(TABLE_X + PADDLE_X_OFFSET, playerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(TABLE_X + TABLE_WIDTH - PADDLE_X_OFFSET - PADDLE_WIDTH, TABLE_Y + TABLE_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    @Override
    public Dimension getPreferredSize() {
        // so that our GUI is big enough
        return new Dimension(TABLE_WIDTH + 2 * TABLE_X, TABLE_HEIGHT + 2 * TABLE_Y);
    }

    private static void createAndShowGui() {
//        TODO: have initial menu screen --> set controls and colours, start game
        JFrame frame = new JFrame("DrawRect");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Pong pong = new Pong();
        MoveAction up = new MoveAction(pong, true);
        MoveAction down = new MoveAction(pong, false);

        pong.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                "movePlayerPaddleUp");
        pong.getActionMap().put("movePlayerPaddleUp", up);
        pong.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                "movePlayerPaddleDown");
        pong.getActionMap().put("movePlayerPaddleDown", down);
        frame.setContentPane(pong);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
            SwingUtilities.invokeLater(Pong::createAndShowGui);
    }
}
