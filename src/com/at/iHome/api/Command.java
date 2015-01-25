package com.at.iHome.api;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anand.Tamariya on 18-Jan-15.
 */
public class Command {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    private String url;
    private String name;
    private String value;
    private String type, response;
    private Map<String, String> header;

    private boolean chained = false, view = false, setting = false;
	protected ResponseProcessor responseProcessor;

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
        str.append(chained).append(" ");
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

    public boolean isJson() {
        return APPLICATION_JSON.equals(type);
    }

    public void setType(String type) {
        this.type = type;
    }

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return the chained
	 */
	public boolean isChained() {
		return chained;
	}

	/**
	 * @param chained the chained to set
	 */
	public void setChained(boolean chained) {
		this.chained = chained;
	}

	/**
	 * @return the responseProcessor
	 */
	public ResponseProcessor getResponseProcessor() {
		return responseProcessor;
	}

	/**
	 * @param responseProcessor the responseProcessor to set
	 */
	public void setResponseProcessor(ResponseProcessor responseProcessor) {
		this.responseProcessor = responseProcessor;
	}

	/**
	 * @return the view
	 */
	public boolean isView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(boolean view) {
		this.view = view;
	}

	/**
	 * @return the setting
	 */
	public boolean isSetting() {
		return setting;
	}

	/**
	 * @param setting the setting to set
	 */
	public void setSetting(boolean setting) {
		this.setting = setting;
	}

}
