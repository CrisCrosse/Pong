package org.example;

import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void findBallInterception_whenBallGoingDownToRightWithOneBoundary_IsCorrect() {
        Pong pong = new Pong(PongConfig.defaults());
        pong.setBallX(pong.getPlayerPaddleXRightEdge());
        pong.setBallY(pong.getTableY());
//      // really need to be able to mock all these fields to make this a good test
        int horizontal_distance = pong.getComputerPaddleX() - (pong.getPlayerPaddleXRightEdge() + pong.getBallDiameter());
        int vertical_travel_distance = pong.getTableY() + pong.getTableHeight() - (pong.getTableY() + pong.getBallDiameter());



    }

}