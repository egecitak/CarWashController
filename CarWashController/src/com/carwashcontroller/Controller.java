package com.carwashcontroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller extends JFrame {
    private JPanel wrapper;
    private JPanel topLeft;
    private JPanel topRight;
    private JPanel bottom;
    private JLabel lbl_topleftname;
    private JLabel lbl_toprightname;
    private JLabel lbl_bottomname;
    private JCheckBox chbx1;
    private JLabel lbl_options;
    private JCheckBox chbx3;
    private JCheckBox chbx4;
    private JCheckBox chbx5;
    private JCheckBox chbx6;
    private JCheckBox chbx7;
    private JCheckBox chbx8;
    private JLabel lbl_start;
    private JButton btn_start;
    private JCheckBox chbx2;
    private JLabel lbl_washentry;
    private JTextField txtFld_washvacant;
    private JLabel lbl_carwashindicators;

    JCheckBox[] chbxArr = {chbx1, chbx2, chbx3, chbx4, chbx5, chbx6, chbx7, chbx8};

    public Controller() {
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Wash Controller");
        frame.setContentPane(new Controller().wrapper);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Pre-determined sizes with deactivated resizing
        frame.setSize(600, 700);
        frame.setResizable(false);

        // To start the program in the middle of the screen
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - frame.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
    }

    public void start() {
        txtFld_washvacant.setBackground(Color.RED);
        btn_start.setEnabled(false);
        boolean emptySelect = true;

        //Option checks
        for (JCheckBox j : chbxArr) {
            if (j.isSelected()) {
                emptySelect = false;
                break;
            }
        }
        if (emptySelect) {
            chbx1.setSelected(true);
        }


        //To disable the checkboxes when washing starts
        for (JCheckBox j : chbxArr) {
            j.setEnabled(false);
        }
    }
}
