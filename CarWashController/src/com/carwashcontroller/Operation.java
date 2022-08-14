package com.carwashcontroller;

import javax.swing.*;

public class Operation {

    private String name;
    private boolean active;
    private int station;
    private int time;
    private JRadioButton rbtn;
    private boolean done = false;

    public Operation () {

    }

    public Operation(String name, boolean active, int station, int time, JRadioButton rbtn) {
        this.name = name;
        this.active = active;
        this.station = station;
        this.time = time;
        this.rbtn = rbtn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public JRadioButton getRbtn() {
        return rbtn;
    }
}
