package com.carwashcontroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

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
    private JCheckBox[] chbxArr = {chbx1, chbx2, chbx3, chbx4, chbx5, chbx6, chbx7, chbx8};
    private JRadioButton[] rbtnArr = {rbtn1, rbtn2, rbtn3, rbtn4, rbtn5, rbtn6, rbtn7, rbtn8, rbtn9};
    private ButtonGroup bg = new ButtonGroup();
    private boolean stopFlag = false;

    public Controller() {

        for (int i = 0; i < 9; i++)
        {
            bg.add(rbtnArr[i]);
        }

        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopFlag = false;
                start();
            }
        });

        btn_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopFlag = true;
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
        int actOps = 0;

        //Option checks and count how many active operations
        for (JCheckBox j : chbxArr) {
            if (j.isSelected()) {
                emptySelection = false;
                actOps++;
            }
        }
        if (emptySelection) {
            chbx1.setSelected(true);
            actOps++;
        }

        //Create an array of active operations
        Operation[] opArr = new Operation[actOps];
        int index = 0;
        for (int i = 0; i < chbxArr.length; i++)
        {
            JCheckBox tmpChbx = new JCheckBox();
            JRadioButton tmpRbtn = new JRadioButton();
            tmpChbx = chbxArr[i];
            tmpRbtn = rbtnArr[i];

            int station = 0;
            switch (i) {
                case 0, 1:
                    station = 1;
                    break;
                case 2, 3, 4, 5:
                    station = 2;
                    break;
                case 6, 7:
                    station = 3;
            }

            if (tmpChbx.isSelected())
            {
                Operation operation = new Operation(tmpChbx.getText(), true, station, 5, tmpRbtn);
                opArr[index] = operation;
                index++;
            }
        }

        for (JCheckBox j : chbxArr) {
            j.setEnabled(false);
        }

        //To disable the checkboxes, enable slider and stop button
        sld.setEnabled(true);
        btn_stop.setEnabled(true);


        Timer timer = new Timer();
        TimerTask taskDelay = new TimerTask() {
            @Override
            public void run() {
                washing(opArr);
            }
        };

        timer.schedule(taskDelay, 500);

    }

    public void washing (Operation[] opArr) {

        for (int i = 0; i < opArr.length; i++)
        {
            Timer checkTimer = new Timer();
            Timer washTimer = new Timer();

            Operation activeOp = new Operation();
            activeOp = opArr[i];

            int[] count = {-1};
            int[] counterLimit = {activeOp.getTime()};

            boolean firstRun = true;
            boolean[] washOK = new boolean[1];
            washOK[0] = ledChecker(activeOp);
            boolean started = false;

            int startCount = 0;

            while (activeOp.isDone() == false) {

                if (stopFlag)
                {
                    checkTimer.cancel();
                    washTimer.cancel();
                    activeOp.getRbtn().setSelected(false);
                    activeOp.getRbtn().setEnabled(false);
                    txtFld_time.setText("0.00");
                    rbtn9.setEnabled(false);
                    bg.clearSelection();
                    stop();
                    break;
                }

                Operation finalActiveOp = activeOp;

                //Checking if the slider is at the right location in small intervals
                TimerTask checkTask = new TimerTask() {
                    @Override
                    public void run() {
                        washOK[0] = ledChecker(finalActiveOp);
                    }
                };

                if (firstRun) {
                    checkTimer.schedule(checkTask, 0, 100);
                    firstRun = false;
                }

                TimerTask washTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (washOK[0] && count[0] < counterLimit[0]) {
                            count[0]++;
                            txtFld_time.setText("0.0" + count[0]);
                        }
                    }
                };

                //Washing start condition
                if (washOK[0] && !started) {
                    washTimer.schedule(washTask, startCount, 1000);
                    started = true;
                }

                //Washing done
                if (count[0] == counterLimit[0]) {
                    checkTimer.cancel();
                    washTimer.cancel();
                    activeOp.getRbtn().setSelected(false);
                    activeOp.getRbtn().setEnabled(false);
                    activeOp.setDone(true);
                }
            }
        }

        while (sld.getValue() != 4)
        {
            rbtn9.setEnabled(true);
            rbtn9.setSelected(true);
        }

        txtFld_time.setText("0.00");
        rbtn9.setEnabled(false);
        bg.clearSelection();
        stop();
    }

    public void stop() {

        //Wash Vacant indicator
        txtFld_washvacant.setBackground(Color.GREEN);
        txtFld_washvacant.setText("Wash Vacant");
        btn_start.setEnabled(true);

        //Enabling checkboxes
        for (JCheckBox j : chbxArr) {
            j.setEnabled(true);
        }

        //To enable the checkboxes and to disable slider and stop button
        sld.setEnabled(false);
        sld.setValue(0);
        btn_stop.setEnabled(false);

    }

    public boolean ledChecker(Operation operation) {

        if (operation.getStation() != sld.getValue()) {
            operation.getRbtn().setSelected(false);
            operation.getRbtn().setEnabled(false);
            rbtn9.setEnabled(true);
            rbtn9.setSelected(true);
            return false;
        }
        else
        {
            rbtn9.setEnabled(false);
            rbtn9.setSelected(false);
            operation.getRbtn().setSelected(true);
            operation.getRbtn().setEnabled(true);
            return true;
        }
    }
}



