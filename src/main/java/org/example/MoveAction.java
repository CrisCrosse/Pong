package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class MoveAction implements Action {
    private static final int MOVE_SPEED = 8;

    private final Pong pong;
    private final boolean movesUp;

    MoveAction(Pong pong, boolean movesUp) {
        this.pong = pong;
        this.movesUp = movesUp;
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public void putValue(String key, Object value) {
    }

    @Override
    public void setEnabled(boolean b) {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {}

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int currentY = pong.getPlayerPaddleY();
        int minYPaddleValue = Pong.getTABLE_Y();
        int maxYPaddleValue = Pong.MAX_PADDLE_Y;

        if (movesUp && currentY > minYPaddleValue) {
            if (currentY - MOVE_SPEED < minYPaddleValue) {
                pong.setPlayerPaddleY(minYPaddleValue);
            } else {
                pong.setPlayerPaddleY(currentY - MOVE_SPEED);
            }
        } else if (!movesUp && currentY < maxYPaddleValue) {
            if (currentY + MOVE_SPEED > maxYPaddleValue) {
                pong.setPlayerPaddleY(maxYPaddleValue);
            } else {
                pong.setPlayerPaddleY(currentY + MOVE_SPEED);
            }
        }
        pong.repaint();
    }
}
