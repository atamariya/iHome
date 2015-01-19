package com.at.iHome.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anand.Tamariya on 18-Jan-15.
 */
abstract public class Device {
    protected Map<String, Command> commands = new HashMap<String, Command>();
    protected String host, name;
    protected String scheme = "http://";

    public Device(String name, String host) {
    	this.name = name;
        this.host = host;
    }

	public void execute(String cmd) {
		System.out.printf("device: %s %s\n", name, host);
		Command command = commands.get(cmd);
		if (command != null) {
//			command.doInBackground(scheme + host);
            command.execute(scheme + host);
		}
		
	}

    abstract public Command getCommand(String commandName);

	public boolean isAudioDevice() {
		return false;
	}

	public boolean isLightControl() {
		return false;
	}

}
