package com.at.iHome.api;

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
    protected String CMD;

    protected Map<String, String> commands = new HashMap<String, String>();
    protected String host, url, name;
    protected String scheme = "http://";

    /**
     * Used for "all lights off/ all off" scenario
     */
    protected boolean all = false;

    public Device(String name, String host) {
        this.name = name;
        this.host = host;
        this.url = scheme + host + uri;
    }

    public List<Command> execute(String cmd) {
        System.out.printf("device: %s %s\n", name, host);
        List<Command> chain = new ArrayList<Command>();
        if (isAll()) {
            chain.addAll(executeAll(cmd));
        } else {

            Command command = getCommand(CommandHandler.getInstance().getSynonym(cmd));
            if (command != null) {
//			command.doInBackground(scheme + host);
//            command.execute(scheme + host);
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
        }

        return cmd;
    }

    public boolean isAudioDevice() {
        return false;
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

}
