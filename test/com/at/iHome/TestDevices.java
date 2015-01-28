package com.at.iHome;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.at.iHome.api.Command;
import com.at.iHome.api.Context;
import com.at.iHome.api.Device;
import com.at.iHome.logic.CommandHandler;
import com.at.iHome.logic.DenonAVR;

public class TestDevices {

	@Test
	public void testCommands() {

		Context context = Context.DEFAULT_CONTEXT;
		List<Command> chain;
		String string;
		
		// "all on", "all off",
		string = "all on";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(6, chain.size());
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
		
		string = "all lights on";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(5, chain.size());
		System.out.println(chain);
		
		string = "let there be light";
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);

		string = "play radio"; //"play tv", "play radio", "play game", "play movie",
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);

        // synonyms test
        string = "watch tv"; //"play tv", "play radio", "play game", "play movie",
        chain = CommandHandler.getInstance().execute(context, string);
        assertEquals(1, chain.size());
        System.out.println(chain);
		
		string = "volume up"; //"volume up", "volume down", "volume mute", "volume unmute",
		chain = CommandHandler.getInstance().execute(context, string);
		assertEquals(1, chain.size());
		System.out.println(chain);
		
		
	}

	@Test
	public void testContext() {
		Context context = Context.DEFAULT_CONTEXT;
		List<Device> list = CommandHandler.getInstance().getDevices(context);
		assertEquals(3, list.size());

		context = new Context("2");
		list = CommandHandler.getInstance().getDevices(context);
		assertEquals(2, list.size());
	}

	@Test
	public void testDeviceAdd() {
		CommandHandler handler = CommandHandler.getInstance();
		Context context = new Context("3");

		Device device = new DenonAVR("avr", "192.168.0.45");
		device.setContext(context);
		handler.addDevice("denon", device);
		
		device = new DenonAVR("avr", "192.168.0.45");
		device.setContext(new Context("3"));
		handler.addDevice("denon", device);
		
		List<Device> list = handler.getDevices(context);
		assertEquals(1, list.size());
		
		device = new DenonAVR("avr", "192.168.0.46");
		device.setContext(new Context("3"));
		handler.addDevice("denon", device);
		
		list = CommandHandler.getInstance().getDevices(context);
		assertEquals(2, list.size());
	}
}
