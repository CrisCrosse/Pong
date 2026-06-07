package org.example;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PongConfig {
    private final int tableX;
    private final int tableY;
    private final int tableHeight;
    private final int tableWidth;
    private final int paddlesXOffsetFromTableEdge;
    private final int paddleWidth;
    private final int paddleHeight;
    private final int ballRadius;
    private final int ballSpeed;
    private final int playerMoveSpeed;
    private final int maxScore;

    public PongConfig(int tableX,
                      int tableY,
                      int tableHeight,
                      int tableWidth,
                      int paddlesXOffsetFromTableEdge,
                      int paddleWidth,
                      int paddleHeight,
                      int ballRadius,
                      int ballSpeed,
                      int playerMoveSpeed,
                      int maxScore) {
        this.tableX = tableX;
        this.tableY = tableY;
        this.tableHeight = tableHeight;
        this.tableWidth = tableWidth;
        this.paddlesXOffsetFromTableEdge = paddlesXOffsetFromTableEdge;
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.ballRadius = ballRadius;
        this.ballSpeed = ballSpeed;
        this.playerMoveSpeed = playerMoveSpeed;
        this.maxScore = maxScore;
    }

    public static PongConfig defaults() {
        return new PongConfig(20, 20, 300, 500, 20, 10, 50, 5, 2, 8, 0);
    }

}

