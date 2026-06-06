package org.example;

public class Utils {

    public static int getBallRightEdge(Pong pong) {
        return pong.getBallX() + Pong.BALL_RADIUS * 2;
    }

    public static int getBallLowerEdge(Pong pong) {
        return pong.getBallY() + Pong.BALL_RADIUS * 2;
    }

    public static int findBallCentrePointThatInterceptsComputerPaddle(Pong pong) {
//        assumes player hits the ball otherwise round will reset

        int minY = Pong.TABLE_Y;
        int maxY = minY + Pong.TABLE_HEIGHT;

        int ballTop = pong.getBallY();
        int ballBottom = getBallLowerEdge(pong);
        int horizontalDistanceToTravel = getHorizontalDistanceToTravel(pong);
        if (horizontalDistanceToTravel < 0) {
            System.out.println("less than 0 horizontal distance should not be possible where interception always occurs");
            return 0;
        }
        System.out.printf("Horizontal distance to travel: %d%n", horizontalDistanceToTravel);

//        X and Y of ball is always changing at same rate
        int verticalDistanceToTravel = horizontalDistanceToTravel;
        int maxVerticalDistanceBallCanTravelWithinTable = Pong.TABLE_HEIGHT - Pong.BALL_RADIUS * 2;

        int verticalDistanceToNextBoundary;
        if (pong.isBallMovingUp()) {
            verticalDistanceToNextBoundary = ballTop - minY;
        } else {
            verticalDistanceToNextBoundary = maxY - ballBottom;
        }

//        This actually works whereas the other way does not
//            case where ball will not hit boundary before reaching
        if (verticalDistanceToNextBoundary > verticalDistanceToTravel) {
            System.out.println("Ball will not intercept boundary again before interception");
            if (pong.isBallMovingUp()) {
                int interceptionCentrePoint = ballTop - verticalDistanceToTravel;
                System.out.printf("Ball going up, intercept as going up: %d%n", interceptionCentrePoint);
                return interceptionCentrePoint;
            } else {
                int interception = ballBottom + verticalDistanceToTravel;
                System.out.printf("Ball going down, intercept as going down: %d%n", interception);
                return interception;
            }
        }

        int verticalDistanceToTravelAfterNextBoundary = verticalDistanceToTravel - verticalDistanceToNextBoundary;
        int numberOfReversals = verticalDistanceToTravelAfterNextBoundary / maxVerticalDistanceBallCanTravelWithinTable;
        int remainder = verticalDistanceToTravelAfterNextBoundary % maxVerticalDistanceBallCanTravelWithinTable;

        int goingDownInterception = minY + remainder;
        int goingUpInterception = maxY - remainder;

        System.out.printf("%d away from next boundary, %d distance left once ball gets to that boundary, %d more reversals to go and final intercept will be %d distance away from boundary%n",
                verticalDistanceToNextBoundary, verticalDistanceToTravelAfterNextBoundary, numberOfReversals, remainder);
        // Still do not understand why even reversals so should be same way is actually a downwards oriented interception, should  be other way
        if (numberOfReversals % 2 == 0) {
            if (pong.isBallMovingUp()) {
                return goingDownInterception;
            } else {
                return goingUpInterception;
            }
        } else {
            if (pong.isBallMovingUp()) {
                return goingUpInterception;
            } else {
                return goingDownInterception;
            }
        }
    }

    private static int getHorizontalDistanceToTravel(Pong pong) {
        int ballLeft = pong.getBallX();
        int ballRight = getBallRightEdge(pong);

        int horizontalDistanceToTravel = 0;
        if (pong.isBallMovingLeft()) {
//            left side travel; ball left side X to paddle offset + paddle width
//            right side travel; table width - (2 * (paddle offset + paddle width))
             horizontalDistanceToTravel += ballLeft - Pong.PLAYER_PADDLE_X_RIGHT_EDGE;
             horizontalDistanceToTravel += Pong.COMPUTER_PADDLE_X - (Pong.PLAYER_PADDLE_X_RIGHT_EDGE + Pong.BALL_RADIUS * 2);
        } else {
            horizontalDistanceToTravel += Pong.COMPUTER_PADDLE_X - ballRight;
        }
        return horizontalDistanceToTravel;
    }
}
