package org.example;

public class Utils {

    public static int getBallRightEdge(Pong pong) {
        return pong.getBallX() + pong.getBallDiameter();
    }

    public static int getBallLowerEdge(Pong pong) {
        return pong.getBallY() + pong.getBallDiameter();
    }

    public static int findBallCentrePointThatInterceptsComputerPaddle(Pong pong) {
//        assumes player hits the ball otherwise round will reset

        int minY = pong.getTableY();
        int maxY = minY + pong.getTableHeight();

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
        int maxVerticalDistanceBallCanTravelWithinTable = pong.getTableHeight() - pong.getBallDiameter();

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
        // this number of reversals is wrong, it outputs 0 reversals when you are close to a boundary, despite there being a boundary
        int numberOfReversals = 1 + (verticalDistanceToTravelAfterNextBoundary / maxVerticalDistanceBallCanTravelWithinTable);
        int remainder = verticalDistanceToTravelAfterNextBoundary % maxVerticalDistanceBallCanTravelWithinTable;

        int goingDownInterception = minY + remainder + pong.getBallRadius();
        int goingUpInterception = maxY - remainder - pong.getBallRadius();

        System.out.printf("%d away from next boundary, %d distance left once ball gets to that boundary, %d more reversals to go and final intercept will be %d distance away from boundary%n",
                verticalDistanceToNextBoundary, verticalDistanceToTravelAfterNextBoundary, numberOfReversals, remainder);
        // Still do not understand why even reversals so should be same way is actually a downwards oriented interception, should  be other way
        if (numberOfReversals % 2 == 0) {
            if (pong.isBallMovingUp()) {
                return goingUpInterception;
            } else {
                return goingDownInterception;
            }
        } else {
            if (pong.isBallMovingUp()) {
                return goingDownInterception;
            } else {
                return goingUpInterception;
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
             horizontalDistanceToTravel += ballLeft - pong.getPlayerPaddleXRightEdge();
             horizontalDistanceToTravel += pong.getComputerPaddleX() - (pong.getPlayerPaddleXRightEdge() + pong.getBallDiameter());
        } else {
            horizontalDistanceToTravel += pong.getComputerPaddleX() - ballRight;
        }
        return horizontalDistanceToTravel;
    }
}
