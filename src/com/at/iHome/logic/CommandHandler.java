package com.at.iHome.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.at.iHome.api.Command;
import com.at.iHome.api.Context;
import com.at.iHome.api.Device;

public class CommandHandler {
    private static CommandHandler instance = new CommandHandler();
    private Map<String, Device> devices = new HashMap<String, Device>();
    /**
     * Map of <common word, command>.
     */
    private Map<String, String> synonyms = new HashMap<String, String>();

    public CommandHandler() {
        Device device = new DenonAVR("avr", "192.168.0.44");
        device.setContext(new Context("1"));
        devices.put("play", device);
        
        device = new LightControl("53ff71066667574819442167");
        device.setContext(new Context("2"));
        devices.put("light", device);
        
        device = new XBMC("xbmc", "192.168.0.26");
        device.setContext(new Context("1"));
        devices.put("play.1", device);

        // devices
        synonyms.put("watch", "play");
        synonyms.put("media", "play");

        // mis-spelling
        synonyms.put("of", "off");

        // commands
        synonyms.put("let there be light", "light on");
        synonyms.put("resume", "play");
    }

    public static CommandHandler getInstance() {
        return instance;
    }

    public List<Command> execute(Context context, String cmd) {
        List<Command> chain = new ArrayList<Command>();
        if (cmd == null)
            return chain;

        
        cmd = getSynonym(cmd);
        String[] tokens = cmd.split(" ");
        int i = 0; // token position
        String token = getSynonym(tokens[i++]);
        boolean all = false;

        if ("all".equals(token)) {
            token = getSynonym(tokens[i++]);
            all = true;
        }

        if ("volume".equals(token)) {
            for (Device device : devices.values()) {
                if (device.isAudioDevice()) {
                	chain.addAll(device.execute(context, cmd));
                }
            }
        } else {
            Device device = devices.get(token);
            if (device != null && tokens.length > 1) {
           		token = getSynonym(tokens[i]);
                device.setAll(all);
                chain.addAll(device.execute(context, token));
            } else {
            	// Single word command (play/pause/resume) - pass as is
                // Handle "all off" case
                for (Device dev : devices.values()) {
                    dev.setAll(all);
                    chain.addAll(dev.execute(context, token));
                }
            }
        }
        return chain;
    }

    public static void main(String args[]) {
        String cmd[] = new String[]{
//                "play internet radio", "play song no matter what",
//                "play song no matter what on youtube",
//                "lights on", "lights off",
//                "play tv", "play radio", "play game", "play movie",
//                "volume up", "volume down", "volume mute", "volume unmute",
//                "all on", "all off",
//                 "all lights on", "all lights off",
//                "let there be light",
        		"play", "pause",
        };
        Context context = new Context("1");
        List<Command> chain = new ArrayList<Command>();
        for (String string : cmd) {
        	chain.addAll(CommandHandler.getInstance().execute(context, string));
        }
        
        for (Command command : chain) {
			System.out.println(command);
		}
    }

    /**
     * @return the synonyms
     */
    public String getSynonym(String name) {
        String cmd = synonyms.get(name);
        if (name.startsWith("lights"))
            cmd = name.replace("lights", "light");
        return cmd == null ? name : cmd;
    }
}
