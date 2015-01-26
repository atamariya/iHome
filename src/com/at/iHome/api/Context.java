package com.at.iHome.api;

public class Context {
    private String name;
    private int rssi, range;

    public Context(String name) {
        this.name = name;
    }

    public Context(String name, int rssi) {
        this.name = name;
        this.rssi = rssi;
    }

    public Context(int rssi, int range) {
        this.rssi = rssi;
        this.range = range;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj == null) {
            // Case when zone is not created yet.
            result = true;
        } else if (obj instanceof Context) {
            Context that = (Context) obj;
            if ((name != null && name.equals(that.getName()))
                    // Case when zone is not created yet.
                    || that.getName() == null
                    || (rssi == that.getRssi())
                    || ((rssi + range > that.getRssi()) &&
                    (rssi - range < that.getRssi()))) {
                result = true;
            }
        }

        return result;
    }

    public String getName() {
        return name;
    }


    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setName(String name) {
        this.name = name;
    }

}
