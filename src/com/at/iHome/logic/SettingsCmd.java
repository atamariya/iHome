package com.at.iHome.logic;

import com.at.iHome.api.Command;
import com.at.iHome.api.Device;

public class SettingsCmd extends Device {

	public SettingsCmd(String name) {
		super(name, null);
		
		commands.put("zone", "zone");
	}
	
	public Command getCommand(String commandName) {
        Command cmd = super.getCommand(commandName);
        if (cmd != null && getParams() != null) {
        	cmd.setName(commands.get("zone"));
        	cmd.setSetting(true);
        	
        	String str;
			if (getParams().size() == 0)
        		str = "one";
        	else
        		str = getParams().get(0);
        	cmd.setValue(str);
        }
        return cmd;
	}
}
