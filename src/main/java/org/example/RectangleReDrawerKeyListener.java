package org.example;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RectangleReDrawerKeyListener implements KeyListener {
    private final DrawRect rectangle;

    RectangleReDrawerKeyListener(DrawRect rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            rectangle.y = rectangle.y - 1;
            rectangle.repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            rectangle.y = rectangle.y + 1;
            rectangle.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
