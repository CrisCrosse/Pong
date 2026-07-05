package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pong extends JPanel {
    @Getter
    private final int tableX;
    @Getter
    private final int tableY;
    @Getter
    private final int tableHeight;
    @Getter
    private final int tableWidth;
    @Getter
    private final int paddlesXOffsetFromTableEdge;
    @Getter
    private final int paddleWidth;
    @Getter
    private final int paddleHeight;
    @Getter
    private final int ballRadius;
    @Getter
    private final int ballSpeed;
    @Getter
    private final int playerMoveSpeed;
    @Getter
    private final int maxScore;

    @Getter
    private final int tableCentreY;
    @Getter
    private final int tableCentreX;
    @Getter
    private final int paddleYStart;
    @Getter
    private final int maxPaddleY;
    @Getter
    private final int playerPaddleX;
    @Getter
    private final int playerPaddleXRightEdge;
    @Getter
    private final int computerPaddleX;
    @Getter
    private final int ballDiameter;
    @Getter
    private final int ballXStart;
    //    TODO: clean up static vs instance fields. They are effectively the same given singleton instance.
    public static final Pong INSTANCE = new Pong(PongConfig.defaults());
    @Setter
    private static volatile boolean isGameOngoing = true;
    @Setter
    @Getter
    private int playerPaddleY;
    @Getter
    @Setter
    private int computerPaddleY;

    @Getter
    @Setter
    private int ballX;
//    @Getter
//    @Setter
//    private int ballY = getRandomTableY();
    @Getter
    @Setter
    private int ballY;
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

    Pong(PongConfig config) {
        super();
        this.tableX = config.getTableX();
        this.tableY = config.getTableY();
        this.tableHeight = config.getTableHeight();
        this.tableWidth = config.getTableWidth();
        this.paddlesXOffsetFromTableEdge = config.getPaddlesXOffsetFromTableEdge();
        this.paddleWidth = config.getPaddleWidth();
        this.paddleHeight = config.getPaddleHeight();
        this.ballRadius = config.getBallRadius();
        this.ballSpeed = config.getBallSpeed();
        this.playerMoveSpeed = config.getPlayerMoveSpeed();
        this.maxScore = config.getMaxScore();

        this.tableCentreY = this.tableY + this.tableHeight / 2;
        this.tableCentreX = this.tableX + this.tableWidth / 2;
        this.paddleYStart = this.tableCentreY - this.paddleHeight / 2;
        this.maxPaddleY = this.tableY + this.tableHeight - this.paddleHeight;
        this.playerPaddleX = this.tableX + this.paddlesXOffsetFromTableEdge;
        this.playerPaddleXRightEdge = this.playerPaddleX + this.paddleWidth;
        this.computerPaddleX = this.tableX + this.tableWidth - this.paddlesXOffsetFromTableEdge - this.paddleWidth;
        this.ballDiameter = this.ballRadius * 2;
        this.ballXStart = this.tableCentreX - this.ballRadius;

        this.playerPaddleY = this.paddleYStart;
        this.computerPaddleY = this.paddleYStart;
        this.ballX = this.ballXStart;
        this.ballY = this.tableCentreY;

        MoveAction up = new MoveAction(this, true, this.playerMoveSpeed);
        MoveAction down = new MoveAction(this, false, this.playerMoveSpeed);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                "movePlayerPaddleUp");
        this.getActionMap().put("movePlayerPaddleUp", up);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                "movePlayerPaddleDown");
        this.getActionMap().put("movePlayerPaddleDown", down);

        JPopupMenu menu = new JPopupMenu("Pong Menu");
        menu.setLocation(this.tableCentreX, this.tableCentreY);
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
        g.drawRect(this.tableX, this.tableY, this.tableWidth, this.tableHeight);
        g.drawLine(this.tableCentreX, this.tableY, this.tableCentreX, this.tableY + this.tableHeight);
        g.fillRect(this.playerPaddleX, playerPaddleY, this.paddleWidth, this.paddleHeight);
        g.fillRect(this.computerPaddleX, computerPaddleY, this.paddleWidth, this.paddleHeight);
        g.fillOval(ballX, ballY, this.ballDiameter, this.ballDiameter);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.tableWidth + 2 * this.tableX, this.tableHeight + 2 * this.tableY);
    }

    public static void createAndShowGui() {
        System.out.println("Creating GUI on event dispatching thread");
//        TODO: have initial menu screen --> set controls and colours, start game
        JFrame frame = new JFrame("Pong Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(Pong.INSTANCE);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void enterGameLoopIfGameOngoing() {
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
        int ballSpeed = pong.getBallSpeed();
        pong.setBallX(pong.getBallX() + (pong.isBallMovingLeft() ? -ballSpeed : ballSpeed));
        pong.setBallY(pong.getBallY() + (pong.isBallMovingUp() ? -ballSpeed : ballSpeed));
        boolean gameOngoing = detectCollisionWithBoardEdgesAndGameOver(pong);
        pong.moveComputerPaddle();
        detectCollisionWithPaddlesAndCorrect(pong);
        return gameOngoing;
    }

    private void moveComputerPaddle() {
        int ballCentrePointIntercept = Utils.findBallCentrePointThatInterceptsComputerPaddle(this);
        System.out.printf("Ball will hit at %s%n", ballCentrePointIntercept);
        int paddleYIntercept = ballCentrePointIntercept - (this.paddleHeight / 2);

        if (paddleYIntercept < this.tableY) {
            paddleYIntercept = this.tableY;
        } if (paddleYIntercept > this.maxPaddleY) {
            paddleYIntercept = this.maxPaddleY;
        }

        int currentY = this.getComputerPaddleY();

        if (paddleYIntercept > currentY) {
            this.setComputerPaddleY(currentY + 1);
        } else if (paddleYIntercept < currentY){
            this.setComputerPaddleY(currentY - 1);
        }
    }

    private static boolean detectCollisionWithBoardEdgesAndGameOver(Pong pong) {
        int minY = pong.getTableY();
        int maxY = minY + pong.getTableHeight();
        int minX = pong.getTableX();
        int maxX = pong.getTableX() + pong.getTableWidth();

        if (pong.getBallY() < minY) {
            System.out.println("Ball collided with top edge of game board");
            // intercept calculations predicated on ball always moving ball speed vertically and horizontally equally
            pong.setBallY(minY + pong.getBallSpeed());
            pong.reverseBallYMovement();
        }
        int ballLowerEdge = Utils.getBallLowerEdge(pong);
        if (ballLowerEdge > maxY) {
            System.out.println("Ball collided with bottom edge of game board");
            pong.setBallY(maxY - pong.getBallDiameter() - pong.getBallSpeed());
            pong.reverseBallYMovement();
        }
        if (pong.getBallX() < minX) {
            System.out.println("Ball collided with left edge of game board");
            pong.setComputerScore(pong.getComputerScore() + 1);
            pong.resetBallAndPaddlePositions();
            if (pong.getComputerScore() > pong.getMaxScore()) {
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
            if (pong.getPlayerScore() > pong.getMaxScore()) {
                System.out.println("Exceeded max score, showing you won menu");
                pong.setMenuGameStatusText("You won!");
                pong.getComponentPopupMenu().setVisible(true);
                return false;
            }
        }
        return true;
    }

    private static void detectCollisionWithPaddlesAndCorrect(Pong pong) {
        if (pong.getBallX() < pong.getPlayerPaddleXRightEdge() && pong.getBallX() > pong.getPlayerPaddleX()
                && pong.getBallY() > pong.getPlayerPaddleY() && pong.getBallY() < pong.getPlayerPaddleYLowerEdge()) {
            System.out.println("Ball collided with player paddle");
            pong.setBallX(pong.getPlayerPaddleXRightEdge());
            pong.reverseBallXMovement();
            } else if (Utils.getBallRightEdge(pong) > pong.getComputerPaddleX() && Utils.getBallRightEdge(pong) < pong.getComputerPaddleX() + pong.getPaddleWidth()
        && pong.getBallY() > pong.getComputerPaddleY() && pong.getBallY() < pong.getComputerPaddleYLowerEdge()) {
            System.out.println("Ball collided with computer paddle");
            pong.setBallX(pong.getComputerPaddleX() - pong.getBallDiameter());
            pong.reverseBallXMovement();
        }
    }

    private void resetBallAndPaddlePositions() {
        this.setBallX(this.ballXStart);
        this.setBallY(getRandomTableY());

        this.setPlayerPaddleY(this.paddleYStart);
        this.setComputerPaddleY(this.paddleYStart);
    }

    private void setMenuGameStatusText(String string) {
        this.gameStatusMenuTextField.setText(string);
    }

    private int getRandomTableY() {
        return (int) (this.tableY + (Math.random() * this.tableHeight));
    }

    private void reverseBallXMovement() {
        this.setBallMovingLeft(!this.isBallMovingLeft());
    }

    private void reverseBallYMovement() {
        this.setBallMovingUp(!this.isBallMovingUp());
    }

    private int getPlayerPaddleYLowerEdge() {
        return this.getPlayerPaddleY()+ this.paddleHeight;
    }

    private int getComputerPaddleYLowerEdge() {
        return this.getComputerPaddleY() + this.paddleHeight;
    }

    private String getScoreAsString() {
        return playerScore + ":" + computerScore;
    }
}
