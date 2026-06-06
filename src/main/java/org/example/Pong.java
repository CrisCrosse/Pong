package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pong extends JPanel {
    public static final int TABLE_X = 20;
    public static final int TABLE_Y = 20;
    public static final int TABLE_HEIGHT = 300;
    public static final int TABLE_WIDTH = 500;
    public static final int TABLE_CENTRE_Y = TABLE_Y + TABLE_HEIGHT / 2;
    public static final int TABLE_CENTRE_X = TABLE_X + TABLE_WIDTH / 2;

    public static final int PADDLES_X_OFFSET_FROM_TABLE_EDGE = 20;
    public static final int PADDLE_WIDTH = 10;
    public static final int PADDLE_HEIGHT = 50;
    public static final int PADDLE_Y_START = TABLE_CENTRE_Y - PADDLE_HEIGHT / 2;
    public static final int MAX_PADDLE_Y = TABLE_Y + TABLE_HEIGHT - PADDLE_HEIGHT;
    public static final int PLAYER_PADDLE_X = TABLE_X + PADDLES_X_OFFSET_FROM_TABLE_EDGE;
    public static final int PLAYER_PADDLE_X_RIGHT_EDGE = PLAYER_PADDLE_X + PADDLE_WIDTH;
    public static final int COMPUTER_PADDLE_X = TABLE_X + TABLE_WIDTH - PADDLES_X_OFFSET_FROM_TABLE_EDGE - PADDLE_WIDTH;

    public static final int BALL_RADIUS = 5;
    public static final int BALL_DIAMETER = BALL_RADIUS * 2;
    public static final int BALL_SPEED = 2;

    public static final int BALL_X_START = TABLE_CENTRE_X - BALL_RADIUS;
    public static final int MAX_SCORE = 0;
    //    TODO: clean up static vs instance fields. They are effectively the same given singleton instance.
    public static final Pong INSTANCE = new Pong();
    @Setter
    private static volatile boolean isGameOngoing = true;
    @Setter
    @Getter
    private int playerPaddleY = PADDLE_Y_START;
    @Getter
    @Setter
    private int computerPaddleY = PADDLE_Y_START;

    @Getter
    @Setter
    private int ballX = BALL_X_START;
//    @Getter
//    @Setter
//    private int ballY = getRandomTableY();
    @Getter
    @Setter
    private int ballY = TABLE_CENTRE_Y;
    @Getter
    @Setter
    private boolean ballMovingLeft = true;
    @Getter
    @Setter
    private boolean ballMovingUp = true;
    @Getter
    @Setter
    private int playerScore = 0;
    @Getter
    @Setter
    private int computerScore = 0;
    @Getter
    private JTextField gameStatusMenuTextField;

    private Pong() {
        super();
        MoveAction up = new MoveAction(this, true);
        MoveAction down = new MoveAction(this, false);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                "movePlayerPaddleUp");
        this.getActionMap().put("movePlayerPaddleUp", up);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                "movePlayerPaddleDown");
        this.getActionMap().put("movePlayerPaddleDown", down);

        JPopupMenu menu = new JPopupMenu("Pong Menu");
        menu.setLocation(TABLE_CENTRE_X, TABLE_CENTRE_Y);
        menu.setUI(new BasicPopupMenuUI());
        JTextField gameStatusTextField = new JTextField();
        gameStatusMenuTextField = gameStatusTextField;
        menu.add(gameStatusTextField);
        JMenuItem startANewGame = new JMenuItem("Start a new Game");
        startANewGame.addMouseListener(new ResetGameMouseListener(this));
        menu.add(startANewGame);
        menu.add(new JMenuItem("Settings"));
        menu.add(new JMenuItem("Multiplayer"));
        this.setComponentPopupMenu(menu);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(this.getScoreAsString(), 10, 15);
//        TODO: make this look nicer and bevelled --> round rectangle
        g.drawRect(TABLE_X, TABLE_Y, TABLE_WIDTH, TABLE_HEIGHT);
        g.drawLine(TABLE_CENTRE_X, TABLE_Y, TABLE_CENTRE_X, TABLE_Y + TABLE_HEIGHT);
        g.fillRect(PLAYER_PADDLE_X, playerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(COMPUTER_PADDLE_X, computerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillOval(ballX, ballY, BALL_RADIUS * 2, BALL_RADIUS * 2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(TABLE_WIDTH + 2 * TABLE_X, TABLE_HEIGHT + 2 * TABLE_Y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Pong::createAndShowGui);
        while (true) {
            try {
//                Loop infinitely and wait for updated state to retrigger game
                Thread.sleep(100);
                enterGameLoopIfGameOngoing();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createAndShowGui() {
        System.out.println("Creating GUI on event dispatching thread");
//        TODO: have initial menu screen --> set controls and colours, start game
        JFrame frame = new JFrame("Pong Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(Pong.INSTANCE);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private static void enterGameLoopIfGameOngoing() {
        while (isGameOngoing) {
            try {
                Pong pong = Pong.INSTANCE;
                boolean gameOngoing = moveBall(pong);
                if (!gameOngoing) {
                    Pong.setGameOngoing(false);
                }
                pong.repaint();
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean moveBall(Pong pong) {
        pong.setBallX(pong.getBallX() + (pong.isBallMovingLeft() ? -BALL_SPEED : BALL_SPEED));
        pong.setBallY(pong.getBallY() + (pong.isBallMovingUp() ? -BALL_SPEED : BALL_SPEED));
        //  some inaccuracy because when flips the vertical distance does not change but horizontal distance reduces
        boolean gameOngoing = detectCollisionWithBoardEdgesAndGameOver(pong);
        pong.moveComputerPaddle();
        detectCollisionWithPaddlesAndCorrect(pong);
        return gameOngoing;
    }

    private void moveComputerPaddle() {
        int ballCentrePointIntercept = Utils.findBallCentrePointThatInterceptsComputerPaddle(this);
        System.out.printf("Ball will hit at %s%n", ballCentrePointIntercept);
        int paddleYIntercept = ballCentrePointIntercept - (PADDLE_HEIGHT / 2);

        if (paddleYIntercept < TABLE_Y) {
            paddleYIntercept = TABLE_Y;
        } if (paddleYIntercept > MAX_PADDLE_Y) {
            paddleYIntercept = MAX_PADDLE_Y;
        }

        int currentY = this.getComputerPaddleY();

        if (paddleYIntercept > currentY) {
            this.setComputerPaddleY(currentY + 1);
        } else if (paddleYIntercept < currentY){
            this.setComputerPaddleY(currentY - 1);
        }
    }

    private static boolean detectCollisionWithBoardEdgesAndGameOver(Pong pong) {
        int minY = TABLE_Y;
        int maxY = minY + TABLE_HEIGHT;
        int minX = TABLE_X;
        int maxX = TABLE_X + TABLE_WIDTH;

        if (pong.getBallY() < minY) {
            System.out.println("Ball collided with top edge of game board");
            pong.setBallY(minY);
            pong.reverseBallYMovement();
        }
        int ballLowerEdge = Utils.getBallLowerEdge(pong);
        if (ballLowerEdge > maxY) {
            System.out.println("Ball collided with bottom edge of game board");
            pong.setBallY(maxY - BALL_RADIUS * 2);
            pong.reverseBallYMovement();
        }
        if (pong.getBallX() < minX) {
            System.out.println("Ball collided with left edge of game board");
            pong.setComputerScore(pong.getComputerScore() + 1);
            pong.resetBallAndPaddlePositions();
            if (pong.getComputerScore() > MAX_SCORE) {
                System.out.println("Exceeded max score, showing you lost menu");
                pong.setMenuGameStatusText("You lost!");
                pong.getComponentPopupMenu().setVisible(true);
                return false;
            }
        }
        int ballRightEdge = Utils.getBallRightEdge(pong);
        if (ballRightEdge > maxX) {
            System.out.println("Ball collided with right edge of game board");
            pong.setPlayerScore(pong.getPlayerScore() + 1);
            pong.resetBallAndPaddlePositions();
            if (pong.getPlayerScore() > MAX_SCORE) {
                System.out.println("Exceeded max score, showing you won menu");
                pong.setMenuGameStatusText("You won!");
                pong.getComponentPopupMenu().setVisible(true);
                return false;
            }
        }
        return true;
    }

    private static void detectCollisionWithPaddlesAndCorrect(Pong pong) {
        if (pong.getBallX() < PLAYER_PADDLE_X_RIGHT_EDGE && pong.getBallX() > PLAYER_PADDLE_X
                && pong.getBallY() > pong.getPlayerPaddleY() && pong.getBallY() < pong.getPlayerPaddleYLowerEdge()) {
            System.out.println("Ball collided with player paddle");
            pong.setBallX(PLAYER_PADDLE_X_RIGHT_EDGE);
            pong.reverseBallXMovement();
            } else if (Utils.getBallRightEdge(pong) > COMPUTER_PADDLE_X && Utils.getBallRightEdge(pong) < COMPUTER_PADDLE_X + PADDLE_WIDTH
        && pong.getBallY() > pong.getComputerPaddleY() && pong.getBallY() < pong.getComputerPaddleYLowerEdge()) {
            System.out.println("Ball collided with computer paddle");
            pong.setBallX(COMPUTER_PADDLE_X - BALL_RADIUS * 2);
            pong.reverseBallXMovement();
        }
    }

    private void resetBallAndPaddlePositions() {
        this.setBallX(BALL_X_START);
        this.setBallY(getRandomTableY());

        this.setPlayerPaddleY(PADDLE_Y_START);
        this.setComputerPaddleY(PADDLE_Y_START);
    }

    private void setMenuGameStatusText(String string) {
        this.gameStatusMenuTextField.setText(string);
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

    private int getComputerPaddleYLowerEdge() {
        return this.getComputerPaddleY() + PADDLE_HEIGHT;
    }

    private String getScoreAsString() {
        return playerScore + ":" + computerScore;
    }
}
