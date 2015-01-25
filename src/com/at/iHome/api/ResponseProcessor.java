package com.at.iHome.api;

import org.json.JSONException;

public interface ResponseProcessor {
	String process(String response) throws JSONException;
}