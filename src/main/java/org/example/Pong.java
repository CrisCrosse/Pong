package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pong extends JPanel {
    private static final int TABLE_WIDTH = 500;
    private static final int TABLE_HEIGHT = 300;
    private static final int TABLE_X = 20;
    private static final int TABLE_Y = 20;
    private static final int TABLE_CENTRE_X = TABLE_X + TABLE_WIDTH / 2;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 50;
    private static final int BALL_SIZE = 10;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        TODO: make this look nicer and bevelled
        g.drawRect(TABLE_X, TABLE_Y, TABLE_WIDTH, TABLE_HEIGHT);
        g.drawLine(TABLE_CENTRE_X, TABLE_Y, TABLE_CENTRE_X, TABLE_Y + TABLE_HEIGHT);
    }

    @Override
    public Dimension getPreferredSize() {
        // so that our GUI is big enough
        return new Dimension(TABLE_WIDTH + 2 * TABLE_X, TABLE_HEIGHT + 2 * TABLE_Y);
    }

    private static void createAndShowGui() {
        Pong gameTable = new Pong();
        JFrame frame = new JFrame("DrawRect");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.add(gameTable);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        createAndShowGui();
    }
}
