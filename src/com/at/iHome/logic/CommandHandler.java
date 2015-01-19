package com.at.iHome.logic;

import java.util.HashMap;
import java.util.Map;

import com.at.iHome.api.Device;

public class CommandHandler {
    private static CommandHandler instance = new CommandHandler();
    private Map<String, Device> devices = new HashMap<String, Device>();
    /**
     * Map of <common word, command>.
     */
    private Map<String, String> synonyms = new HashMap<String, String>();

    public CommandHandler() {
        Device avr = new DenonAVR("avr", "192.168.0.44");
        devices.put("play", avr);
        devices.put("light", new LightControl("53ff71066667574819442167"));

        // devices
        synonyms.put("watch", "play");
        synonyms.put("media", "play");

        // mis-spelling
        synonyms.put("of", "off");

        // commands
        synonyms.put("let there be light", "light on");
    }

    public static CommandHandler getInstance() {
        return instance;
    }

    public void execute(String cmd) {
        if (cmd == null)
            return;

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
                    device.execute(cmd);
                }
            }
//        } else if (token.startsWith("light")) {
//            // light or lights - both should work
//            for (Device device : devices.values()) {
//                if (device.isLightControl()) {
//                    device.execute(tokens[1]);
//                }
//            }
        } else {
            Device device = devices.get(token);
            if (device != null) {
                token = getSynonym(tokens[i]);
                device.setAll(all);
                device.execute(token);
            } else {
                // Handle "all off" case
                for (Device dev : devices.values()) {
                    dev.setAll(all);
                    dev.execute(token);
                }
            }
        }
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
                "let there be light"
        };
        for (String string : cmd) {
            CommandHandler.getInstance().execute(string);
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
