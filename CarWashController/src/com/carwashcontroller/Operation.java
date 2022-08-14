package com.carwashcontroller;

public class Operation {

    private String name;
    private boolean active;
    private int station;
    private int counter = 0;
    private boolean done = false;

    public Operation(String name, boolean active, int station) {
        this.name = name;
        this.active = active;
        this.station = station;
    }

    public String getName() {
        return name;
    }

    public int getCounter() {
        return counter;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
