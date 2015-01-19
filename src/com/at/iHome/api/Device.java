package com.at.iHome.api;

import java.util.HashMap;
import java.util.Map;

import com.at.iHome.logic.CommandHandler;

/**
 * Created by Anand.Tamariya on 18-Jan-15.
 */
abstract public class Device {
    protected String uri;
    protected String CMD;

    protected Map<String, String> commands = new HashMap<String, String>();
    protected String host, name;
    protected String scheme = "http://";

    /**
     * Used for "all lights off/ all off" scenario
     */
    protected boolean all = false;

    public Device(String name, String host) {
    	this.name = name;
        this.host = host;
    }

	public void execute(String cmd) {
		System.out.printf("device: %s %s\n", name, host);
		if (isAll()) {
			executeAll(cmd);
		}
		
		Command command = getCommand(CommandHandler.getInstance().getSynonym(cmd));
		if (command != null) {
//			command.doInBackground(scheme + host);
            command.execute(scheme + host);
		}
		
	}

	/**
	 * Execute ALL commands matching name.
	 * @param name
	 */
    protected void executeAll(String name) {
		// Empty method to be over-ridden by subclass
		
	}

	public Command getCommand(String commandName) {
        Command cmd = null;
        String param = commands.get(commandName);
        if (param != null) {
            cmd = new Command(uri, CMD, param);
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
