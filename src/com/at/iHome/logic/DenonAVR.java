package com.at.iHome.logic;

import java.util.List;

import com.at.iHome.api.Command;
import com.at.iHome.api.Device;

public class DenonAVR extends Device {

	public DenonAVR(String name, String host) {
		super(name, host);
		uri = "/MainZone/index.put.asp";
		CMD = "cmd0";
		this.url = scheme + host + uri;

		// Mode control
		commands.put("game", "PutZone_InputFunction/GAME");
		commands.put("tv", "PutZone_InputFunction/SAT/CBL");
		commands.put("radio", "PutZone_InputFunction/TUNER");
		commands.put("internet radio", "PutZone_InputFunction/IRADIO");
		commands.put("movie", "PutZone_InputFunction/MPLAY");

		// Volume control
		commands.put("volume up", "PutMasterVolumeBtn/>");
		commands.put("volume down", "PutMasterVolumeBtn/<");
		commands.put("volume mute", "PutVolumeMute/on");
		commands.put("volume unmute", "PutVolumeMute/off");

		// Power control
		commands.put("on", "PutZone_OnOff/ON");
		commands.put("off", "PutZone_OnOff/OFF");
	}

	@Override
	protected List<Command> executeAll(String name) {
		List<Command> chain = super.executeAll(name);
		Command command = getCommand(CommandHandler.getInstance().getSynonym(
				name));
		if (command != null) {
			// command.doInBackground(scheme + host);
			// command.execute(scheme + host);
			chain.add(command);
		}
		return chain;
	}

	public boolean isAudioDevice() {
		return true;
	}

}
