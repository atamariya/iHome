package com.at.iHome.api;

public class Context {
    private String name;
    private int rssi, range;

    public static final Context DEFAULT_CONTEXT = new Context("Default");

    public Context(String name) {
        if (name != null)
            name = name.trim();
        this.name = name;
    }

    public Context(String name, int rssi) {
        this(name);
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
            if ((name != null && that.getName() != null && name.equals(that.getName()))
                    || ((rssi == that.getRssi()) && rssi != 0)
                    || ((rssi + range > that.getRssi()) &&
                    (rssi - range < that.getRssi()))
                    || (that == DEFAULT_CONTEXT)
                    /* Generally all contexts are equal to default context; but default context is
                    ONLY equal to another default context instance
                     */
//                    || (this == DEFAULT_CONTEXT)
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

    /**
     * Be wary of using this method. You don't want to assign a name to default context.
     * @param name
     */
//    public void setName(String name) {
//        this.name = name;
//    }

}
