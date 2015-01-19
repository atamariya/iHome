package com.at.iHome.logic;

import java.util.HashMap;
import java.util.Map;

import com.at.iHome.api.Device;

public class CommandHandler {
	private static CommandHandler instance = new CommandHandler();
	private Map<String, Device> devices = new HashMap<String, Device>();

	public CommandHandler() {
        Device avr = new DenonAVR("avr", "192.168.0.44");
		devices.put("play", avr);
        devices.put("watch", avr);
        devices.put("media", avr);
		devices.put("light", new LightControl("53ff71066667574819442167"));
	}

	public static CommandHandler getInstance() {
		return instance;
	}

	public void execute(String cmd) {
		if (cmd == null)
			return;

		String[] tokens = cmd.split(" ");

		if ("volume".equals(tokens[0])) {
			for (Device device : devices.values()) {
				if (device.isAudioDevice()) {
					device.execute(cmd);
				}
			}
		} else if (tokens[0].startsWith("light")) {
			// light or lights - both should work
			for (Device device : devices.values()) {
				if (device.isLightControl()) {
					device.execute(tokens[1]);
				}
			}
		} else {
			Device device = devices.get(tokens[0]);
			if (device != null) {
				device.execute(tokens[1]);
			}
		}
	}

	public static void main(String args[]) {
		String cmd[] = new String[] { 
				"play internet radio", "play song no matter what",
				"play song no matter what on youtube", "play movie",
				"lights on", "lights off",
				"play tv", "play radio", "play game",
				"volume up", "volume down", "volume mute", "volume unmute"
				};
		for (String string : cmd) {
			CommandHandler.getInstance().execute(string);
		}
	}
}
