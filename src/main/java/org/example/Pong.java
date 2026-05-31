package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Pong extends JPanel {
//    TODO: clean up static vs instance fields. They are effectively the same given singleton instance.
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
    private static final int COMPUTER_PADDLE_X = TABLE_X + TABLE_WIDTH - PADDLE_X_OFFSET - PADDLE_WIDTH;
    @Getter
    private static final int PADDLE_HEIGHT = 50;
    private static final int PADDLE_Y_START = TABLE_CENTRE_Y - PADDLE_HEIGHT / 2;
    @Getter
    private static final int PLAYER_PADDLE_X = TABLE_X + PADDLE_X_OFFSET;
    @Getter
    private static final int PLAYER_PADDLE_X_RIGHT_EDGE = PLAYER_PADDLE_X + PADDLE_WIDTH;
    private static final int BALL_RADIUS = 5;
    private static final int BALL_X_START = TABLE_CENTRE_X - BALL_RADIUS;
    private static final int MAX_SCORE = 0;
    public static final int BALL_SPEED = 2;
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
    @Getter
    @Setter
    private int ballY = getRandomTableY();
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
                pong.moveComputerPaddle();
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

        boolean gameOngoing = detectCollisionWithBoardEdgesAndGameOver(pong);
        detectCollisionWithPaddlesAndCorrect(pong);
        return gameOngoing;
    }

    private void moveComputerPaddle() {
        int ballYInterception = findBallInterceptionWithComputerPaddle();
        int currentY = this.getComputerPaddleY();

         if (ballYInterception > currentY) {
            this.setComputerPaddleY(currentY + 1);
        } else if (ballYInterception < currentY){
            this.setComputerPaddleY(currentY - 1);
        }
    }

    private int findBallInterceptionWithComputerPaddle() {
//        assumes player hits the ball otherwise round will reset

        int minY = getTABLE_Y();
        int maxY = minY + getTABLE_HEIGHT();

        int ballTop = this.getBallY();
        int ballBottom = getBallLowerEdge(this);
        int ballLeft = this.getBallX();
        int ballRight = getBallRightEdge(this);

        int horizontalDistanceToTravel = 0;
        if (isBallMovingLeft()) {
//            left side travel; ball left side X to paddle offset + paddle width
//            right side travel; table width - (2 * (paddle offset + paddle width))
             horizontalDistanceToTravel += ballLeft - getPLAYER_PADDLE_X_RIGHT_EDGE();
             horizontalDistanceToTravel += TABLE_WIDTH - (2 * (PADDLE_X_OFFSET + PADDLE_WIDTH));
        } else {
            horizontalDistanceToTravel += COMPUTER_PADDLE_X - ballRight;
        }
//            vertical distance travelled is 1 for every horizontal travel
        int verticalDistanceToNextBoundary;
        if (isBallMovingUp()) {
            verticalDistanceToNextBoundary = ballTop - minY;
        } else {
            verticalDistanceToNextBoundary = maxY - ballBottom;
        }
//        X and Y of ball is always changing at same rate
        int verticalDistanceToTravel = horizontalDistanceToTravel;
        int verticalDistanceBallCanTravel = TABLE_HEIGHT - BALL_RADIUS * 2;

//            case where ball will not hit boundary before reaching
        if (verticalDistanceToTravel < verticalDistanceBallCanTravel) {
            if (isBallMovingUp()) {
                int interceptionCentrePoint = ballTop - verticalDistanceToTravel + PADDLE_HEIGHT / 2;
                return interceptionCentrePoint;
            } else {
                return ballBottom + horizontalDistanceToTravel - PADDLE_HEIGHT / 2;
            }
        }

        int verticalDistanceToTravelAfterNextBoundary = verticalDistanceToTravel - verticalDistanceToNextBoundary;
        int numberOfReversals = verticalDistanceToTravelAfterNextBoundary / verticalDistanceBallCanTravel;
        int remainder = verticalDistanceToTravelAfterNextBoundary % TABLE_HEIGHT;

//            Think there might be some wierdness here with how i am calculating distances due to taking into
//            account ball and paddle thicknesses
        if (numberOfReversals % 2 == 0) {
//                ball will intercept topside + remainder if going up
            if (isBallMovingUp()) {
                return minY + remainder - PADDLE_HEIGHT / 2;
            } else {
                return maxY - remainder - PADDLE_HEIGHT / 2;
            }
//                ball will intercept bottomside - remainder if going down
        } else {
            if (isBallMovingUp()) {
                return maxY - remainder - PADDLE_HEIGHT / 2;
            } else {
                return minY + remainder - PADDLE_HEIGHT / 2;
            }
        }
//            if 400 horizontal distance to travel, 400 vertical as well
//            given table height of 75 and initial offset of 10 from top moving upwards
//            ball travels 10 up, so 390 vertical travel left
//            5 vertical reversals occur --> 390 / 75 = 5 w 15 remainder
//            target Y is therefore on the bottom side --> odd number of reversals and 15 offset

    }

    private static boolean detectCollisionWithBoardEdgesAndGameOver(Pong pong) {
        int minY = getTABLE_Y();
        int maxY = minY + getTABLE_HEIGHT();
        int minX = getTABLE_X();
        int maxX = getTABLE_X() + getTABLE_WIDTH();

        if (pong.getBallY() < minY) {
            System.out.println("Ball collided with top edge of game board");
            pong.setBallY(minY);
            pong.reverseBallYMovement();
        }
        int ballLowerEdge = getBallLowerEdge(pong);
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
        int ballRightEdge = getBallRightEdge(pong);
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

    private static int getBallRightEdge(Pong pong) {
        return pong.getBallX() + BALL_RADIUS * 2;
    }

    private static int getBallLowerEdge(Pong pong) {
        return pong.getBallY() + BALL_RADIUS * 2;
    }

    private static void detectCollisionWithPaddlesAndCorrect(Pong pong) {
        if (pong.getBallX() < getPLAYER_PADDLE_X_RIGHT_EDGE() && pong.getBallX() > getPLAYER_PADDLE_X()
                && pong.getBallY() > pong.getPlayerPaddleY() && pong.getBallY() < pong.getPlayerPaddleYLowerEdge()) {
            System.out.println("Ball collided with player paddle");
            pong.setBallX(getPLAYER_PADDLE_X_RIGHT_EDGE());
            pong.reverseBallXMovement();
            } else if (getBallRightEdge(pong) > COMPUTER_PADDLE_X && getBallRightEdge(pong) < COMPUTER_PADDLE_X + PADDLE_WIDTH
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
