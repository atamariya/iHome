package com.at.iHome.api;

import java.util.ArrayList;
import java.util.Iterator;
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
		boolean executedOnce = false;
		for (Iterator<Device> it = devices.iterator(); it.hasNext();) {
			Device device = it.next();
			device.setAll(isAll());
			device.setParams(getParams());
			
			try {
				commands.addAll(device.execute(ctx, cmd));
				executedOnce = true;
			} catch (ZoneException e) {
				if (!it.hasNext() && !executedOnce) {
					throw e;
				}
			}
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
	 * @param device the devices to set
	 */
	public void addDevice(Device device) {
		if (!devices.contains(device))
			this.devices.add(device);
	}

}
