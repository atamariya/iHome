package com.at.iHome.logic;

import com.at.iHome.api.Command;
import com.at.iHome.api.Device;

public class DenonAVR extends Device {

    private final String uri = "/MainZone/index.put.asp";
    private final String CMD = "cmd0";

    public DenonAVR(String name, String host) {
		super(name, host);

		// Mode control
		Command cmd = new Command(uri, CMD, "PutZone_InputFunction/GAME");
		commands.put("game", cmd);
		
		cmd = new Command(uri, CMD, "PutZone_InputFunction/SAT/CBL");
		commands.put("tv", cmd);
		
		cmd = new Command(uri, CMD, "PutZone_InputFunction/TUNER");
		commands.put("radio", cmd);
		
		cmd = new Command(uri, CMD, "PutZone_InputFunction/IRADIO");
		commands.put("internet radio", cmd);
		
		// Volume control
		cmd = new Command(uri, CMD, "PutMasterVolumeBtn/>");
		commands.put("volume up", cmd);
		
		cmd = new Command(uri, CMD, "PutMasterVolumeBtn/<");
		commands.put("volume down", cmd);
		
		cmd = new Command(uri, CMD, "PutVolumeMute/on");
		commands.put("volume mute", cmd);
		
		cmd = new Command(uri, CMD, "PutVolumeMute/off");
		commands.put("volume unmute", cmd);
	}
	
	public boolean isAudioDevice() {
		return true;
	}

}
