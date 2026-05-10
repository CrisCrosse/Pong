package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class MoveAction implements Action {
    @Override
    public Object getValue(String key) {
        System.out.print("action performed?");
        return null;
    }

    @Override
    public void putValue(String key, Object value) {
        System.out.print("action performed?");
    }

    @Override
    public void setEnabled(boolean b) {
        System.out.print("action performed?");
    }

    @Override
    public boolean isEnabled() {
        System.out.print("action performed?");
        return false;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        System.out.print("action performed?");
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        System.out.print("action performed?");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.print("action performed?");
    }
}
