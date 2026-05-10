package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

@Setter
@Getter
public class DrawRect extends JPanel {
    private int rectX;
    private int rectY;
    private int rectWidth;
    private int rectHeight;

    DrawRect(int rectX, int rectY, int rectWidth, int rectHeight) {
        this.rectX = rectX;
        this.rectY = rectY;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        this.addKeyListener(new RectangleReDrawerKeyListener(this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(rectX, rectY, rectWidth, rectHeight);
    }

    @Override
    public Dimension getPreferredSize() {
        // so that our GUI is big enough
        return new Dimension(rectWidth + 2 * rectX, rectHeight + 2 * rectY);
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
