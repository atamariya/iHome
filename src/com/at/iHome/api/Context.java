package com.at.iHome.api;

public class Context {
    private String name;
    private int rssi, range;

    public static final Context DEFAULT_CONTEXT = new Context("default");

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

        if (obj != null && obj instanceof Context) {
            Context that = (Context) obj;
            if ((name != null && that.getName() != null && name.trim().equals(that.getName().trim()))
                    || ((rssi == that.getRssi()) && rssi != 0)
                    || ((rssi + range > that.getRssi()) &&
                    (rssi - range < that.getRssi()))
                    || (that == DEFAULT_CONTEXT)
                    || (this == DEFAULT_CONTEXT)
                    ) {
                result = true;
            }
        }

        return result;
    }
    
    @Override
    public String toString() {
    	return name;
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
