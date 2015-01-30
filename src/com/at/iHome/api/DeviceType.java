package com.at.iHome.api;

/**
 * Created by Anand.Tamariya on 30-Jan-15.
 */
public enum DeviceType {
    IPCAM ("Cisco DCS-930L"),
    DENON ("Denon AVR"),
    SPARK_CORE ("Spark Core"),
    XBMC ("XBMC");

    String name;
    DeviceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
