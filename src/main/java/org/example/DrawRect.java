package org.example;

import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

@Setter
public class DrawRect extends JPanel {
    public int x;
    public int y;
    public int width;
    public int height;

    DrawRect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.addKeyListener(new RectangleReDrawerKeyListener(this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(x, y, width, height);
    }

    @Override
    public Dimension getPreferredSize() {
        // so that our GUI is big enough
        return new Dimension(width + 2 * x, height + 2 * y);
    }

    private static void createAndShowGui() {
        DrawRect mainPanel = new DrawRect(20, 20, 100, 100);
        JFrame frame = new JFrame("DrawRect");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Action action = new MoveAction();
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke("F2"),
                "doSomething");
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                "doSomething");
        mainPanel.getActionMap().put("doSomething",
                action);



        Container contentPane = frame.getContentPane();
        contentPane.add(mainPanel);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        createAndShowGui();
    }
}
