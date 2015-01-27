package com.at.iHome;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.at.iHome.api.Command;
import com.at.iHome.api.Context;
import com.at.iHome.api.Device;
import com.at.iHome.logic.CommandHandler;

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
		assertEquals(1, list.size());
	}

}
