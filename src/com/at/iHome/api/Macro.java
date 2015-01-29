package com.at.iHome.api;

import java.util.ArrayList;
import java.util.List;

import com.at.iHome.logic.CommandHandler;

public class Macro extends Group {
	List<String> cmdList = new ArrayList<String>();

	public Macro(String name) {
		super(name);
	}

	public void addCommand(String cmd) {
		cmdList.add(cmd);
	}
	
	@Override
	public List<Command> execute(Context ctx, String notUsed) {
		List<Command> commands = new ArrayList<Command>();
		for (String cmd : cmdList) {
			commands.addAll(CommandHandler.getInstance().execute(ctx, cmd));
		}
		return commands;
	}

}
