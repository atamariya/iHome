package com.at.iHome.logic;

import com.at.iHome.api.Command;
import com.at.iHome.api.Device;

public class IPCam extends Device {

	public IPCam(String name, String host) {
		super(name, host);
//		uri = "/mjpeg.cgi";  // Video uri
		uri = "/image/jpeg.cgi"; // Image uri
        this.url = scheme + host + uri;
        
		// Mode control
		commands.put("camera", "dummy");
	}

	@Override
	public Command getCommand(String commandName) {
		Command command = super.getCommand(commandName);
		if (command != null) {
			command.setView(true);
		}
		return command;
	}
}
