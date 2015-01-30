package com.at.iHome.api;

import android.util.Base64;

import com.at.iHome.logic.CommandHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anand.Tamariya on 18-Jan-15.
 */
abstract public class Device {
    protected String uri;
    protected String CMD, paramTemplate;
    protected List<String> params;

    protected Map<String, String> commands = new HashMap<String, String>();
    protected String host;
    protected String url;

    protected String name;
    protected String username;
    protected String password;
    protected String scheme = "http://";

    protected DeviceType type;

    /**
     * Used for "all lights off/ all off" scenario
     */
    protected boolean all = false;
    protected boolean lightControl, audioDevice;

    /**
     * Logical devices (e.g. SettingsCmd) must not have a context. It is only meant for physical devices.
     */
    protected Context context;

    public Device(String name, String host) {
        this.name = name;
        this.host = host;
        this.url = scheme + host + uri;
    }

    public List<Command> execute(Context ctx, String cmd) {
        System.out.printf("device: %s %s\n", name, host);
        List<Command> chain = new ArrayList<Command>();
        if (isAll()) {
            chain.addAll(executeAll(cmd));
        } else {
            if (context != null && !context.equals(ctx)) {
                throw new ZoneException();
            }

            // Try twice - first with original keyword; next with synonym
            Command command = getCommand(cmd);
            if (command == null) {
                String str = CommandHandler.getInstance()
                        .getSynonym(cmd);
                if (str != null) {
                    String[] tokens = str.split(",");
                    int i = tokens.length;
                    for (String token : tokens) {
                        command = getCommand(token);
                        if (command != null) {
                            command.setChained(i-- > 1);
                            chain.add(command);
                        }
                    }
                }

            } else {
                // command.doInBackground(scheme + host);
                // command.execute(scheme + host);
                chain.add(command);
            }
        }
        return chain;
    }

    /**
     * Execute ALL commands matching name.
     *
     * @param name
     */
    protected List<Command> executeAll(String name) {
        // Empty method to be over-ridden by subclass
        return new ArrayList<Command>(1);
    }

    public Command getCommand(String commandName) {
        Command cmd = null;
        String param = commands.get(commandName);
        if (param != null) {
            cmd = new Command(url, CMD, param);

            if (username != null && password != null) {
                String header = "Basic " + Base64.encodeToString(
                        (username + ":" + password).getBytes(), Base64.DEFAULT);
                cmd.setHeader("Authorization", header);
            }
        }

        return cmd;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj != null) {
            result = host.equals(((Device) obj).host);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(name);
        str.append(": ").append(host);
        return str.toString();
    }

    public boolean isAudioDevice() {
        return this.audioDevice;
    }

    public boolean isLightControl() {
        return false;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    /**
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(Context context) {
        this.context = context;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = new ArrayList<String>();
        this.params.add(params);
    }

    /**
     * @return the paramTemplate
     */
    public String getParamTemplate() {
        return paramTemplate;
    }

    /**
     * @param paramTemplate the paramTemplate to set
     */
    public void setParamTemplate(String paramTemplate) {
        this.paramTemplate = paramTemplate;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    /**
     * @param lightControl the lightControl to set
     */
    public void setLightControl(boolean lightControl) {
        this.lightControl = lightControl;
    }

    /**
     * @param audioDevice the audioDevice to set
     */
    public void setAudioDevice(boolean audioDevice) {
        this.audioDevice = audioDevice;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public DeviceType getType() {
        return type;
    }

}
