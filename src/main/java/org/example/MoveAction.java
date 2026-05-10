package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class MoveAction implements Action {
    private static final int MOVE_SPEED = 4;

    private final Pong pong;
    private final boolean movesUp;

    MoveAction(Pong pong, boolean movesUp) {
        this.pong = pong;
        this.movesUp = movesUp;
    }

    @Override
    public Object getValue(String key) {
        System.out.println("getValue");
        return null;
    }

    @Override
    public void putValue(String key, Object value) {
        System.out.println("putValue");
    }

    @Override
    public void setEnabled(boolean b) {
        System.out.println("ZsetEnabled");
    }

    @Override
    public boolean isEnabled() {
        System.out.println("checking if action is enabled");
        return true;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        System.out.println("addPropertChangeListener");
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        System.out.println("removePropertyChangeListener");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int currentY = pong.getPlayerPaddleY();
        int minYPaddleValue = Pong.getTABLE_Y();
        int maxYPaddleValue = Pong.getTABLE_Y() + Pong.getTABLE_HEIGHT() - Pong.getPADDLE_HEIGHT();

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
