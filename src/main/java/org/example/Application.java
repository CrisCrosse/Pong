package org.example;

import javax.swing.*;

import static org.example.Pong.enterGameLoopIfGameOngoing;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Pong::createAndShowGui);
        while (true) {
            try {
//                Loop infinitely and wait for updated state to retrigger game
                Thread.sleep(100);
                enterGameLoopIfGameOngoing();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
