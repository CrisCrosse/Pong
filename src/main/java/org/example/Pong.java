package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pong extends JPanel {
    private static final Pong INSTANCE = new Pong();
    @Getter
    private static final int TABLE_WIDTH = 500;
    @Getter
    private static final int TABLE_HEIGHT = 300;
    @Getter
    private static final int TABLE_X = 20;
    @Getter
    private static final int TABLE_Y = 20;
    public static final int TABLE_CENTRE_Y = TABLE_Y + TABLE_HEIGHT / 2;
    private static final int TABLE_CENTRE_X = TABLE_X + TABLE_WIDTH / 2;
    private static final int PADDLE_X_OFFSET = 20;
    private static final int PADDLE_WIDTH = 10;
    @Getter
    private static final int PADDLE_HEIGHT = 50;
    @Getter
    private static final int PLAYER_PADDLE_X = TABLE_X + PADDLE_X_OFFSET;
    @Getter
    private static final int PLAYER_PADDLE_X_RIGHT_EDGE = PLAYER_PADDLE_X + PADDLE_WIDTH;
    private static final int BALL_RADIUS = 5;
    @Setter
    @Getter
    private int playerPaddleY = TABLE_CENTRE_Y - PADDLE_HEIGHT / 2;

    @Getter
    @Setter
    private int ballX = TABLE_CENTRE_X - BALL_RADIUS;
    @Getter
    @Setter
    private int ballY = getRandomTableY();
    @Getter
    @Setter
    private boolean ballMovingLeft = true;
    @Getter
    @Setter
    private boolean ballMovingUp = true;


    private Pong() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        TODO: make this look nicer and bevelled --> round rectangle
        g.drawRect(TABLE_X, TABLE_Y, TABLE_WIDTH, TABLE_HEIGHT);
        g.drawLine(TABLE_CENTRE_X, TABLE_Y, TABLE_CENTRE_X, TABLE_Y + TABLE_HEIGHT);
        g.fillRect(PLAYER_PADDLE_X, playerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(TABLE_X + TABLE_WIDTH - PADDLE_X_OFFSET - PADDLE_WIDTH, TABLE_Y + TABLE_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillOval(ballX, ballY, BALL_RADIUS * 2, BALL_RADIUS * 2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(TABLE_WIDTH + 2 * TABLE_X, TABLE_HEIGHT + 2 * TABLE_Y);
    }

    private static void createAndShowGui() {
        System.out.println("Creating GUI on event dispatching thread");
//        TODO: have initial menu screen --> set controls and colours, start game
        JFrame frame = new JFrame("DrawRect");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Pong pong = Pong.INSTANCE;
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

    private static int getRandomTableY() {
        return (int) (TABLE_Y + (Math.random() * TABLE_HEIGHT));
    }

    private void reverseBallXMovement() {
        this.setBallMovingLeft(!this.isBallMovingLeft());
    }

    private void reverseBallYMovement() {
        this.setBallMovingUp(!this.isBallMovingUp());
    }

    private int getPlayerPaddleYLowerEdge() {
        return this.getPlayerPaddleY()+ PADDLE_HEIGHT;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Pong::createAndShowGui);
        while (true) {
                try {
                    Pong pong = Pong.INSTANCE;
                    moveBall(pong, 2);
                    pong.repaint();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    private static void moveBall(Pong pong, int ballSpeed) {
        pong.setBallX(pong.getBallX() + (pong.isBallMovingLeft() ? -ballSpeed : ballSpeed));
        pong.setBallY(pong.getBallY() + (pong.isBallMovingUp() ? -ballSpeed : ballSpeed));

        detectCollisionWithBoardEdgesAndCorrect(pong);
        detectCollisionWithPlayerPaddleAndCorrect(pong);
    }

    private static void detectCollisionWithBoardEdgesAndCorrect(Pong pong) {
        int minY = getTABLE_Y();
        int maxY = minY + getTABLE_HEIGHT();
        int minX = getTABLE_X();
        int maxX = getTABLE_X() + getTABLE_WIDTH();

        if (pong.getBallY() < minY) {
            System.out.println("Ball collided with top edge of game board");
            pong.setBallY(minY);
            pong.reverseBallYMovement();
        }
        int ballLowerEdge = pong.getBallY() + BALL_RADIUS * 2;
        if (ballLowerEdge > maxY) {
            System.out.println("Ball collided with bottom edge of game board");
            pong.setBallY(maxY - BALL_RADIUS * 2);
            pong.reverseBallYMovement();
        }
        if (pong.getBallX() < minX) {
            System.out.println("Ball collided with left edge of game board");
            pong.setBallX(minX);
            pong.reverseBallXMovement();
        }
        int ballRightEdge = pong.getBallX() + BALL_RADIUS * 2;
        if (ballRightEdge > maxX) {
            System.out.println("Ball collided with right edge of game board");
            pong.setBallX(maxX - BALL_RADIUS * 2);
            pong.reverseBallXMovement();
        }
    }

    private static void detectCollisionWithPlayerPaddleAndCorrect(Pong pong) {
        if (pong.getBallX() < getPLAYER_PADDLE_X_RIGHT_EDGE() && pong.getBallX() > getPLAYER_PADDLE_X()
        && pong.getBallY() > pong.getPlayerPaddleY() && pong.getBallY() < pong.getPlayerPaddleYLowerEdge()) {
            System.out.println(pong.getPlayerPaddleYLowerEdge());
            System.out.println("Ball collided with player paddle");
            pong.setBallX(getPLAYER_PADDLE_X_RIGHT_EDGE());
            pong.reverseBallXMovement();
        }
    }
}
