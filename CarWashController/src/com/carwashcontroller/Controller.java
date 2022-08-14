package com.carwashcontroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

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
    private JRadioButton rbtn1;
    private JRadioButton rbtn2;
    private JRadioButton rbtn3;
    private JRadioButton rbtn4;
    private JRadioButton rbtn5;
    private JRadioButton rbtn6;
    private JRadioButton rbtn7;
    private JRadioButton rbtn8;
    private JRadioButton rbtn9;
    private JTextField txtFld_time;
    private JLabel lbl_time;
    private JLabel lbl_slider;
    private JSlider sld;
    private JButton btn_stop;
    private JLabel lbl_entry;
    private JLabel lbl_stn1;
    private JLabel lbl_stn2;
    private JLabel lbl_stn3;
    private JLabel lbl_exit;
    private JCheckBox[] chbxArr;

    public Controller() {

        JCheckBox[] chbxArr = {chbx1, chbx2, chbx3, chbx4, chbx5, chbx6, chbx7, chbx8};

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
        /*
        Start button is pressed which turns the Wash Entry LED into red and disables
        the top left part of the panel for the time being
        */
        txtFld_washvacant.setBackground(Color.RED);
        txtFld_washvacant.setText("Wash in Progress");
        btn_start.setEnabled(false);
        boolean emptySelection = true;

        //Option checks
        for (JCheckBox j : chbxArr) {
            if (j.isSelected()) {
                emptySelection = false;
                break;
            }
        }
        if (emptySelection) {
            chbx1.setSelected(true);
        }


        //To disable the checkboxes when washing starts
        for (JCheckBox j : chbxArr) {
            j.setEnabled(false);
        }
    }
}
