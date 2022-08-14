package com.carwashcontroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class Controller extends JFrame {
    //All GUI elements are placed with the help of GUI designer of IntelliJ Idea
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

        //All radio buttons are grouped to prevent editing from the user
        for (int i = 0; i < 9; i++)
        {
            bg.add(rbtnArr[i]);
        }

        //Start button action listener, which resets stopFlag and starts washing
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopFlag = false;
                start();
            }
        });

        //Stop button action listener, which sets stopFlag and cuts washing in the middle
        btn_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopFlag = true;
            }
        });
    }

    public static void main(String[] args) {

        //Main function is only used for GUI
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
        //If no check box is selected, select first one and increment active ops counter
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
                //Creating operation objects only for active operations, they also hold their station and corresponding LED
                Operation operation = new Operation(tmpChbx.getText(), true, station, 5, tmpRbtn);
                opArr[index] = operation;
                index++;
            }
        }

        //To disable the checkboxes, enable slider and stop button
        for (JCheckBox j : chbxArr) {
            j.setEnabled(false);
        }
        sld.setEnabled(true);
        btn_stop.setEnabled(true);

        //The timer is necessary to create a buffet between start command and process, since otherwise program crashes
        Timer timer = new Timer();
        TimerTask taskDelay = new TimerTask() {
            @Override
            public void run() {
                washing(opArr);
            }
        };

        timer.schedule(taskDelay, 200); //runs once, creates 200 ms buffer

    }

    public void washing (Operation[] opArr) {

        //Iterates over active operations array
        for (int i = 0; i < opArr.length; i++)
        {
            //Check timer is necessary for slider position checking
            Timer checkTimer = new Timer();
            //Wash timer is to keep track of the elapsed time
            Timer washTimer = new Timer();

            Operation activeOp = new Operation();
            activeOp = opArr[i];

            //These all defined like this to use in the inner classes of TimerTasks
            int[] count = {-1};
            int[] counterLimit = {activeOp.getTime()}; //required time for an operation
            boolean[] washOK = new boolean[1];

            //Initial flags to seperate the first iterations
            boolean firstRun = true;
            boolean started = false;

            //Initial slider position checker
            washOK[0] = ledChecker(activeOp);

            int startCount = 0;

            while (activeOp.isDone() == false) {

                //If stop button is clicked, the timers are cancelled then function exits
                if (stopFlag)
                {
                    checkTimer.cancel();
                    washTimer.cancel();
                    stop();
                    return;
                }

                Operation finalActiveOp = activeOp;

                //Checking if the slider is at the right location in small intervals
                TimerTask checkTask = new TimerTask() {
                    @Override
                    public void run() {
                        washOK[0] = ledChecker(finalActiveOp);
                    }
                };

                //Starting check timer during first run for every active operation
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

                //Washing start condition which also starts timer
                if (washOK[0] && !started) {
                    washTimer.schedule(washTask, startCount, 1000);
                    started = true;
                }

                //Washing done, timers are cancelled, operation's "LED" is disabled and it is marked as done
                if (count[0] == counterLimit[0]) {
                    checkTimer.cancel();
                    washTimer.cancel();
                    activeOp.getRbtn().setSelected(false);
                    activeOp.getRbtn().setEnabled(false);
                    activeOp.setDone(true);
                }
            }
        }

        //After a normal process, keep out of pos. LED open until slider is in Exit
        while (sld.getValue() != 4)
        {
            rbtn9.setEnabled(true);
            rbtn9.setSelected(true);
        }

        //Then stop
        stop();
    }

    public void stop() {

        //Elapsed time text is reset, LEDs are back to their initial states
        txtFld_time.setText("0.00");
        rbtn9.setEnabled(false);
        bg.clearSelection();

        //Wash Vacant indicator
        txtFld_washvacant.setBackground(Color.GREEN);
        txtFld_washvacant.setText("Wash Vacant");

        //Start button enabled
        btn_start.setEnabled(true);

        //To enable the checkboxes and to disable slider and stop button
        for (JCheckBox j : chbxArr) {
            j.setEnabled(true);
        }
        sld.setEnabled(false);
        sld.setValue(0);
        btn_stop.setEnabled(false);

    }

    public boolean ledChecker(Operation operation) {

        /*Checks if the slider position is in accordance with the station required by the operation, if not
        open out of pos LED. */
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



