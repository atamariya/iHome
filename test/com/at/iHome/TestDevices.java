package com.at.iHome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.at.iHome.api.Command;
import com.at.iHome.api.Context;
import com.at.iHome.api.Device;
import com.at.iHome.api.Macro;
import com.at.iHome.api.NoDeviceException;
import com.at.iHome.api.ZoneException;
import com.at.iHome.logic.ColorLight;
import com.at.iHome.logic.CommandHandler;
import com.at.iHome.logic.DenonAVR;
import com.at.iHome.logic.IPCam;
import com.at.iHome.logic.LightControl;
import com.at.iHome.logic.SettingsCmd;
import com.at.iHome.logic.XBMC;

public class TestDevices {
	int count = 5;
	@Before
	public void setUp() {
		CommandHandler handler = CommandHandler.getInstance();
		handler.clear();

		Device device = new DenonAVR("avr", "192.168.0.44");
		device.setContext(new Context("2"));
		handler.addDevice(device);

		device = new LightControl("53ff71066667574819442167");
		device.setContext(new Context("2"));
		handler.addDevice(device);

		 device = new XBMC("xbmc", "192.168.0.26");
		 device.setContext(new Context("1"));
		 handler.addDevice(device);

		device = new IPCam("ipcam", "192.168.0.35");
		device.setContext(new Context("1"));
		device.setUsername("guest");
		device.setPassword("passw0rd");
		handler.addDevice(device);

		device = new SettingsCmd("settings");
		handler.addDevice(device);
		
		device = new ColorLight("one");
		device.setContext(new Context("4"));
		handler.addDevice(device);
	}

	@Test
	public void testLights() {

		Context context = Context.DEFAULT_CONTEXT;
		List<Command> chain;
		String string;

		// "lights on", "lights off",
		// "all lights on", "all lights off",
		// "let there be light",
		string = "lights on";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);

		string = "switch on 4";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		assertEquals("D4,HIGH", chain.get(0).getValue());
		assertEquals("params", chain.get(0).getName());
		System.out.println(chain);

		string = "all lights on";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(4, chain.size());
		assertNotEquals(null, chain.get(0));
		System.out.println(chain);
	}

	@Test
	public void testCommands() {

		Context context = Context.DEFAULT_CONTEXT;
		List<Command> chain;
		String string;

		// "all on", "all off",
		string = "all on";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(5, chain.size());
		System.out.println(chain);

		string = "show camera one";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);

		// "lights on", "lights off",
		// "all lights on", "all lights off",
		// "let there be light",
		string = "lights on";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);

		string = "switch on 4";
		string = "open door";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		assertEquals("D4,HIGH", chain.get(0).getValue());
		System.out.println(chain);
		
		string = "close door";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		assertEquals("D4,LOW", chain.get(0).getValue());
		System.out.println(chain);

		string = "all lights on";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(4, chain.size());
		System.out.println(chain);

		string = "let there be light";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);

		string = "play radio"; // "play tv", "play radio", "play game",
								// "play movie",
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);

		string = "watch tv"; // "play tv", "play radio", "play game",
								// "play movie",
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);

		string = "volume up"; // "volume up", "volume down", "volume mute",
								// "volume unmute",
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);
	}

	@Test
	public void testContext() {
		Context context = Context.DEFAULT_CONTEXT;
		List<Device> list = CommandHandler.getInstance().getDevices(context);
		assertEquals(count, list.size());

		context = new Context("2");
		list = CommandHandler.getInstance().getDevices(context);
		assertEquals(2, list.size());

		// Space in context name is not allowed
		context = new Context("2 ");
		list = CommandHandler.getInstance().getDevices(context);
		assertEquals(2, list.size());

		// Non existing context should execute in default context
		context = new Context("NA");
		String string = "watch tv"; // "play tv", "play radio", "play game",
									// "play movie",
		List<Command> chain = CommandHandler.getInstance().execute(context,
				string);
		assertEquals(1, chain.size());
		System.out.println(chain);
	}

	@Test(expected = ZoneException.class)
	public void testDifferentZones() {
		// Difference in device and current zone should throw ZoneException
		Context context = new Context("2");
		String string = "show camera"; // "play tv", "play radio", "play game",
										// "play movie",
		List<Command> chain = CommandHandler.getInstance().execute(context,
				string);
		assertEquals(0, chain.size()); // This should not execute because of
										// exception in previous step
		System.out.println(chain);
	}

	@Test
	public void testDifferentZones1() {
		CommandHandler handler = CommandHandler.getInstance();

		// Don't throw an exception if there's atleast one device to handle the
		// command.
		String string = "watch tv"; // "play tv", "play radio", "play game",
									// "play movie",
		Context context = new Context("1");
		Device device = new DenonAVR("avr", "192.168.0.45");
		device.setContext(context);
		handler.addDevice(device);

		List<Command> chain = CommandHandler.getInstance().execute(context,
				string);
		assertEquals(1, chain.size());
		System.out.println(chain);

		context = new Context("2");
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);
	}

	@Test
	public void testDeviceAdd() {
		CommandHandler handler = CommandHandler.getInstance();
		Context context = new Context("3");

		Device device = new DenonAVR("avr", "192.168.0.45");
		device.setContext(context);
		handler.addDevice(device);

		// Add same device (same host) again
		device = new DenonAVR("avr", "192.168.0.45");
		device.setContext(new Context("3"));
		handler.addDevice(device);

		List<Device> list = handler.getDevices(context);
		assertEquals(1, list.size());

		device = new DenonAVR("avr", "192.168.0.46");
		device.setContext(new Context("3"));
		handler.addDevice(device);

		list = CommandHandler.getInstance().getDevices(context);
		assertEquals(2, list.size());
	}

	@Test(expected = NoDeviceException.class)
	public void testNoDevice() {
		Context context = Context.DEFAULT_CONTEXT;
		List<Command> chain;
		String string;

		// If no devices have been added, it should throw NoDeviceException
		string = "volume up"; // "volume up", "volume down", "volume mute",
								// "volume unmute",
		CommandHandler.getInstance().clear();
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(0, chain.size()); // Should not come here
	}

	@Test
	public void testDeviceRemoval() {
		CommandHandler handler = CommandHandler.getInstance();
		Context context = Context.DEFAULT_CONTEXT;
		List<Device> list = handler.getDevices(context);
		assertEquals(count, list.size());

		// Non existing device
		handler.removeDevice("denon");
		list = handler.getDevices(context);
		assertEquals(count, list.size());

		handler.removeDevice("avr");
		list = handler.getDevices(context);
		assertEquals(count - 1, list.size());
	}

	@Test
	public void testMacro() {
		CommandHandler handler = CommandHandler.getInstance();
		Context context = Context.DEFAULT_CONTEXT;

		Macro macro = new Macro("test macro");

		Device device = new DenonAVR("avr", "192.168.0.45");
		device.setContext(context);
		macro.addCommand("play tv");

		device = new DenonAVR("avr", "192.168.0.45");
		device.setContext(context);
		macro.addCommand("play radio");

		handler.addDevice(macro);

		List<Command> chain = handler.execute(context, "test macro");
		assertEquals(2, chain.size());
		System.out.println(chain);
	}
	
	@Test
	public void testAudioVideoControls() {
		Context context = Context.DEFAULT_CONTEXT;
		List<Command> chain;
		String string;

		string = "paint one red";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		assertEquals(true, context.isPaintable());
		System.out.println(chain);
		
		string = "paint all red";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		assertEquals(true, context.isPaintable());
		System.out.println(chain);
		
		string = "play radio";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		assertEquals(true, context.isAudioPlaying());
		assertEquals(false, context.isMediaPlaying());
		System.out.println(chain);
		
		string = "play song";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(2, chain.size());
		assertEquals(true, context.isAudioPlaying());
		assertEquals(true, context.isMediaPlaying()); // XBMC is playing
		System.out.println(chain);
		
		string = "play movie cars";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(3, chain.size());
		assertEquals(true, context.isAudioPlaying());
		assertEquals(true, context.isMediaPlaying());
		System.out.println(chain);
	}
}
