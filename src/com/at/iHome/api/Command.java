package com.at.iHome.api;


import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anand.Tamariya on 18-Jan-15.
 */
public class Command extends AsyncTask<String, Void, Void> {
    private String uri, name, value;
    private Map<String, String> header;

    public Command(String uri, String name, String value) {
        this.uri = uri;
        this.name = name;
        this.value = value;
    }

    protected Void doInBackground(String... params) {
        System.out.printf("Command: %s %s %s %s\n", uri, name, value, header);
        AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        try {
            HttpPost post = new HttpPost(params[0] + uri);

            if (getHeader() != null) {
                for (String key : getHeader().keySet())
                    post.setHeader(key, getHeader().get(key));
            }

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(name, value));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
            post.setEntity(formEntity);

            client.execute(post);
//                publishProgress((int) ((i / (float) count) * 100));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Escape early if cancel() is called
//            if (isCancelled()) return;
        client.close();
        return null;
    }

//    protected void onProgressUpdate(Integer... progress) {
////        setProgressPercent(progress[0]);
//    }

//    protected void onPostExecute(Long result) {
////        showDialog("Downloaded " + result + " bytes");
//    }

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
}
