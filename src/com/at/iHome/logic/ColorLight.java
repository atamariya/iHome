package com.at.iHome.logic;

import java.util.HashMap;
import java.util.Map;

import com.at.iHome.api.Command;
import com.at.iHome.api.Device;
import com.at.iHome.api.DeviceType;

/**
 * curl https://api.spark.io/v1/devices/0123456789abcdef01234567/brew \
     -d access_token=9876987698769876987698769876987698769876 -d params=D7,HIGH -d params=D6,LOW
 *  curl -H "Authorization: Bearer 38bb7b318cc6898c80317decb34525844bc9db55"
  https://..
 * @author Anand.Tamariya
 *
 */
public class ColorLight extends Device {
    private String token = "cb1b31f441b50a1f9e4496c44a15da5abb3cfe2b";
    protected Map<String, String> paramTemplate = new HashMap<String, String>();

    /**
     *
     * @param name token of the device
     */
	public ColorLight(String name) {
		super(name, "api.spark.io");
        scheme = "https://";
        uri = "/v1/devices/" + name + "/digitalwrite";
        CMD = "params";
        this.url = scheme + host + uri;
        type = DeviceType.SPARK_CORE;

        commands.put("red", "ffff0000");
        commands.put("green", "ff00ff00");
        commands.put("blue", "ff0000ff");
	}

    public Command getCommand(String commandName) {
        Command cmd = null;
        if (getName().equals(commandName) || "all".equals(commandName)) {
        	String color = null;
        	if (getParams() != null) {
        		color = getParams().get(0);
                if (commands.containsKey(color))
                    color = commands.get(color);
        	}
        	cmd = new Command(url, commandName, color);
        }

        return cmd;
    }

//    @Override
//    protected List<Command> executeAll(String name) {
//    	List<Command> chain = super.executeAll(name);
//    	for (String cmd : new String[] {"5", "6", "3", "4"}) {
//    		setParams(cmd);
//    		chain.add(getCommand(name));
//		}
//    	return chain;
//    }
    
	public boolean isLightControl() {
		return true;
	}

	@Override
	public boolean isPaintable() {
		return true;
	}
}
