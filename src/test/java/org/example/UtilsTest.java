package org.example;

import org.junit.jupiter.api.Test;

import static org.example.Utils.findBallCentrePointThatInterceptsComputerPaddle;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @Test
    void findBallInterception_whenBallGoingDownToRightWithOneBoundary_IsCorrect() {
        Pong pong = new Pong(PongConfig.builder()
                .ballRadius(5)
                .ballSpeed(1)
                .paddleHeight(10)
                .paddleWidth(10)
                .paddlesXOffsetFromTableEdge(10)
                .tableY(0)
                .tableX(0)
                .tableWidth(100)
                .tableHeight(50)
                .playerMoveSpeed(1)
                .build());

        // ball at top left corner, 20 in from left edge due to 10 X offset and 10 paddle width
        pong.setBallX(pong.getPlayerPaddleXRightEdge());
        pong.setBallY(pong.getTableY());
        // ball moving down and to the right
        pong.setBallMovingUp(false);
        pong.setBallMovingLeft(false);

        // right side of ball is therefore at 30 in from left, right side offset + paddle width = 80
        // ball needs to travel 50 units right
        // ball will collide with boundary after travelling 40 units
        // 10 units of movement left so the bottom co-ord of the ball at intercept should be 10 up from bottom of board
        // top of ball will be at 20 up, so centre point intercept should be 15 up
        int expected = (50) - 10 - 5;

        int actual = findBallCentrePointThatInterceptsComputerPaddle(pong);

        assertEquals(expected, actual);
    }


    @Test
    void findBallInterception_whenBallGoingDownToRightWithOneBoundaryAndJustBeforeBoundary_IsCorrect() {
        Pong pong = new Pong(PongConfig.builder()
                .ballRadius(5)
                .ballSpeed(1)
                .paddleHeight(10)
                .paddleWidth(10)
                .paddlesXOffsetFromTableEdge(10)
                .tableY(0)
                .tableX(0)
                .tableWidth(100)
                .tableHeight(50)
                .playerMoveSpeed(1)
                .build());

        // ball left at 50 in from left edge
        pong.setBallX(50);
        // ball bottom is 10 away from bottom boundary, ball top is 30 from boundary
        pong.setBallY(30);
        // ball moving down and to the right
        pong.setBallMovingUp(false);
        pong.setBallMovingLeft(false);

        // right side of ball is at 60 in from left, right side offset + paddle width = 80
        // ball needs to travel 20 units right
        // ball will collide with boundary after travelling 10 units
        // 10 units of movement left so the bottom co-ord of the ball at intercept should be 10 up from bottom of board
        // top of ball will be at 20 up, so centre point intercept should be 15 up
        int expected = (50) - 10 - 5;

        int actual = findBallCentrePointThatInterceptsComputerPaddle(pong);

        assertEquals(expected, actual);
    }

    @Test
    void findBallInterception_whenBallGoingDownToRightWithOneBoundaryAndJustBeforeBoundary_IsSameAsJustAfterBoundary() {
        Pong pong = new Pong(PongConfig.builder()
                .ballRadius(5)
                .ballSpeed(1)
                .paddleHeight(10)
                .paddleWidth(10)
                .paddlesXOffsetFromTableEdge(10)
                .tableY(0)
                .tableX(0)
                .tableWidth(100)
                .tableHeight(50)
                .playerMoveSpeed(1)
                .build());

        // ball left at 50 in from left edge
        pong.setBallX(50);
        // ball bottom is 10 away from bottom boundary, ball top is 30 from boundary
        pong.setBallY(30);
        // ball moving down and to the right
        pong.setBallMovingUp(false);
        pong.setBallMovingLeft(false);

        // right side of ball is at 60 in from left, right side offset + paddle width = 80
        // ball needs to travel 20 units right
        // ball will collide with boundary after travelling 10 units
        // 10 units of movement left so the bottom co-ord of the ball at intercept should be 10 up from bottom of board
        // top of ball will be at 20 up, so centre point intercept should be 15 up
        int expectedBeforeBoundary = (50) - 10 - 5;

        int beforeBoundaryIntercept = findBallCentrePointThatInterceptsComputerPaddle(pong);

        // ball travelled another 10 right and 10 down
        pong.setBallX(60);
        // ball bottom is 10 away from bottom boundary, ball top is 30 from boundary
        pong.setBallY(40);
        // ball moving down and to the right
        pong.setBallMovingUp(true);
        pong.setBallMovingLeft(false);

        // right side of ball is at 70 in from left, right side offset + paddle width = 80
        // ball needs to travel 10 units right
        // ball has just hit boundary
        // 10 units of movement left so the bottom co-ord of the ball at intercept should be 10 up from bottom of board
        // top of ball will be at 20 up, so centre point intercept should be 15 up
        int expectedAfterBoundary = (50) - 10 - 5;

        int afterBoundaryIntercept = findBallCentrePointThatInterceptsComputerPaddle(pong);

        assertEquals(beforeBoundaryIntercept, afterBoundaryIntercept);
    }

}