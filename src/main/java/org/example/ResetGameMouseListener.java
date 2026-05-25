package org.example;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ResetGameMouseListener implements MouseListener {
    private final Pong pong;

    public ResetGameMouseListener(Pong pong) {
        this.pong = pong;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Reset game button clicked");
        pong.setPlayerScore(0);
        pong.setComputerScore(0);
        pong.getComponentPopupMenu().setVisible(false);
        Pong.setGameOngoing(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
