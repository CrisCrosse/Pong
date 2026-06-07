package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class MoveAction implements Action {
    private final Pong pong;
    private final boolean movesUp;
    private final int moveSpeed;

    MoveAction(Pong pong, boolean movesUp, int moveSpeed) {
        this.pong = pong;
        this.movesUp = movesUp;
        this.moveSpeed = moveSpeed;
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
        int minYPaddleValue = pong.getTableY();
        int maxYPaddleValue = pong.getMaxPaddleY();

        if (movesUp && currentY > minYPaddleValue) {
            if (currentY - moveSpeed < minYPaddleValue) {
                pong.setPlayerPaddleY(minYPaddleValue);
            } else {
                pong.setPlayerPaddleY(currentY - moveSpeed);
            }
        } else if (!movesUp && currentY < maxYPaddleValue) {
            if (currentY + moveSpeed > maxYPaddleValue) {
                pong.setPlayerPaddleY(maxYPaddleValue);
            } else {
                pong.setPlayerPaddleY(currentY + moveSpeed);
            }
        }
        pong.repaint();
    }
}
