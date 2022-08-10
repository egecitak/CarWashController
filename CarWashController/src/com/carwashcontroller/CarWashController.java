package com.carwashcontroller;

import javax.swing.*;

public class CarWashController {

    public static void main(String[] args) {

        //GUI Design
        //btn.setBounds(x, y, width, height)
        //btn.setText("");
        JFrame frame = new JFrame();
        frame.setSize(500, 600);

        JPanel panel = new JPanel();

        JButton btn = new JButton("button");
        panel.add(btn);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //quit program when window is closed
        frame.setVisible(true);
    }
}
