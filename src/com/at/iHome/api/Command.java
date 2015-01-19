package com.at.iHome.api;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anand.Tamariya on 18-Jan-15.
 */
public class Command {
    private String url;
    private String name;
    private String value;
    private Map<String, String> header;

    public Command(String uri, String name, String value) {
        this.url = uri;
        this.name = name;
        this.value = value;
    }

    /**
     * @return the header
     */
    public Map<String, String> getHeader() {
        return header;
    }

    /**
     * @param k the header to set
     */
    public void setHeader(String k, String v) {
        if (header == null) {
            header = new HashMap<String, String>();
        }
        this.header.put(k, v);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(url).append(" ");
        str.append(name).append("=");
        str.append(value).append(" ");
        str.append(header);
        return str.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
