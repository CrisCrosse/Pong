package org.example;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("Welcome to Chris Rs Pong!");

        //Create and set up the window.
        JFrame frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel pongLabel = new JLabel("Pong label?");
        Box box = new Box(BoxLayout.X_AXIS);
        box.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE));
        box.setBackground(Color.GREEN);
        box.setOpaque(true);


        pongLabel.setPreferredSize(new Dimension(175, 100));
        frame.getContentPane().add(pongLabel, BorderLayout.CENTER);
        box.setPreferredSize(new Dimension(600, 400));
        frame.getContentPane().add(box, BorderLayout.CENTER);
//        frame.setLocationRelativeTo(pongLabel);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        try {
            Graphics boxGraphics = box.getGraphics();
            boxGraphics.setColor(Color.BLACK);
            box.paintComponents(boxGraphics);
            boxGraphics.setClip(100, 100, 600, 400);
            boxGraphics.fillRect(100, 100, 600, 400);
            box.paintComponents(boxGraphics);
        } catch (Exception e) {
            System.out.print(e);
        }

        }
}
