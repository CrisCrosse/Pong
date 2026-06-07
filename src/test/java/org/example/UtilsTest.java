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

        // ball at top left corner, 20 in from left edge
        pong.setBallX(pong.getPlayerPaddleXRightEdge());
        pong.setBallY(pong.getTableY());
        // ball moving down and to the right
        pong.setBallMovingUp(false);
        pong.setBallMovingLeft(false);

        // ball needs to travel 50 units right
        // ball will collide with boundary after travelling 40 units
        // 10 units of movement left so the bottom co-ord of the ball at intercept should be 10 up from bottom of board
        // top of ball will be at 20 up, so centre point intercept should be 15 up
        int expected = (50) - 20 + 5;

        int actual = findBallCentrePointThatInterceptsComputerPaddle(pong);

        assertEquals(expected, actual);
    }

}