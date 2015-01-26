package com.at.iHome.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		devices.put("denon", device);

		device = new LightControl("53ff71066667574819442167");
		device.setContext(new Context("2"));
		devices.put("light", device);

		device = new XBMC("xbmc", "192.168.0.26");
		device.setContext(new Context("1"));
		devices.put("xbmc", device);
		devices.put("play", device);
		
		device = new IPCam("ipcam", "192.168.0.35");
		device.setContext(new Context("1"));
		device.setUsername("guest");
		device.setPassword("passw0rd");
		devices.put("show", device);

        device = new SettingsCmd("settings");
        devices.put("set", device);

		// devices
		synonyms.put("watch", "play");
		synonyms.put("media", "play");

		// mis-spelling
		synonyms.put("of", "off");

		// commands
		synonyms.put("let there be light", "light on");
		synonyms.put("resume", "play");
		synonyms.put("mute", "volume mute");
		synonyms.put("unmute", "volume unmute");
		synonyms.put("song", "search song,open song");
		synonyms.put("movie", "search movie,open movie");
		// synonyms.put("play", "xbmc, denon");
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
			// Macro for devices is not supported yet
			Device device = devices.get(token);
			if (device != null && tokens.length > 1) {
				token = getSynonym(tokens[i++]);
				StringBuilder tmp = new StringBuilder();
				for (int j = i; j < tokens.length; j++) {
					tmp.append(tokens[j]).append(" ");
				}

				device.setParams(tmp.toString());
				device.setAll(all);

				// Expand macro for action
				tokens = token.split(",");
				if (tokens.length > 1) {
					List<Command> macroChain = new ArrayList<Command>();
					for (String str : tokens) {
						macroChain.addAll(device.execute(context, str));
					}
					for (Iterator<Command> iterator = macroChain.iterator(); iterator
							.hasNext();) {
						Command command = iterator.next();
						command.setChained(iterator.hasNext());
					}
					chain.addAll(macroChain);
				} else {
					chain.addAll(device.execute(context, token));
				}
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
		String cmd[] = new String[] {
		// "play internet radio",
//		"play song no matter what",
		// "play song", "play movie twilight",

		// Notification requires two arguments. Have to figure out how to parse
		// that from input.
		// "play notify message"

		// "play song no matter what on youtube",
		// "lights on", "lights off",
		// "play tv", "play radio", "play game", "play movie",
		// "volume up", "volume down", "volume mute", "volume unmute",

		// Ensure that executeAll() is implemented for appropriate devices
		// "all on", "all off",

		// "all lights on", "all lights off",
		// "let there be light",
		// "play", "pause",
				"show cam one"
		};
		Context context = new Context("1");
		List<Command> chain = new ArrayList<Command>();
		for (String string : cmd) {
			chain.addAll(CommandHandler.getInstance().execute(context, string));
		}

		for (Command command : chain) {
			System.out.println(command);
		}
		
//		context.setName("2");
		context = Context.DEFAULT_CONTEXT;
		List<Device> list =  CommandHandler.getInstance().getDevices(context);
		for (Device device : list) {
			System.out.println(device);
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

    public List<Device> getDevices(Context context) {
        List<Device> list = new ArrayList<Device>();
        for (Device device : devices.values()) {
            if (device.getContext() != null && device.getContext().equals(context))
                list.add(device);
        }

        return list;
    }
}
