package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.example.Pong.*;
import static org.mockito.Mockito.when;

class UtilsTest {

    @Test
    void findBallInterception_whenBallGoingDownToRightWithOneBoundary_IsCorrect() {
        Pong pong = Mockito.mock(Pong.class);
//        Need to make static fields passed from constructor
        try (MockedStatic<Pong> mockedPong = Mockito.mockStatic(Pong.class)) {
            pong.when(Pong::BALL)
        }
//        Pong pong = Pong.INSTANCE;
        pong.setBallX(PLAYER_PADDLE_X_RIGHT_EDGE);
        pong.setBallY(TABLE_Y);
//      // really need to be able to mock all these fields to make this a good test
        int horizontal_distance = COMPUTER_PADDLE_X - (PLAYER_PADDLE_X_RIGHT_EDGE + BALL_DIAMETER);
        int vertical_travel_distance = TABLE_Y + TABLE_HEIGHT - (TABLE_Y + BALL_DIAMETER);



    }

}