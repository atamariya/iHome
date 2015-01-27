package com.at.iHome.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class to group similar type of devices. e.g. all XBMC devices will be included
 * in a single Group object. The idea is same command is executed by all the devices in the
 * group unlike macros where devices execute different commands.
 * 
 * @author Anand.Tamariya
 *
 */
public class Group extends Device {
	private List<Device> devices = new ArrayList<Device>();

	public Group(String name) {
		super(name, null);
	}
	
	@Override
	public List<Command> execute(Context ctx, String cmd) {
		List<Command> commands = new ArrayList<Command>();
		for (Device device : devices) {
			device.setAll(isAll());
			commands.addAll(device.execute(ctx, cmd));
		}
		return commands;
	}

	/**
	 * @return the devices
	 */
	public List<Device> getDevices() {
		return devices;
	}

	/**
	 * @param devices the devices to set
	 */
	public void addDevice(Device device) {
		if (!devices.contains(device))
			this.devices.add(device);
	}

}
