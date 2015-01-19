package com.at.iHome.logic;

import com.at.iHome.api.Command;
import com.at.iHome.api.Device;

import java.util.List;

/**
 * curl https://api.spark.io/v1/devices/0123456789abcdef01234567/brew \
     -d access_token=9876987698769876987698769876987698769876 -d params=D7,HIGH -d params=D6,LOW
 *  curl -H "Authorization: Bearer 38bb7b318cc6898c80317decb34525844bc9db55"
  https://..
 * @author Anand.Tamariya
 *
 */
public class LightControl extends Device {
    private String token = "cb1b31f441b50a1f9e4496c44a15da5abb3cfe2b";

    /**
     *
     * @param name token of the device
     */
	public LightControl(String name) {
		super(name, "api.spark.io");
        scheme = "https://";
        uri = "/v1/devices/" + name + "/digitalwrite";
        CMD = "params";
        this.url = scheme + host + uri;
        
        // Special case
        commands.put("on", "D6,HIGH");
		commands.put("off", "D6,LOW");
		
		commands.put("1 on", "D6,HIGH");
		commands.put("1 off", "D6,LOW");
		
		commands.put("2 on", "D5,HIGH");
		commands.put("2 off", "D5,LOW");
		
		commands.put("3 on", "D4,HIGH");
		commands.put("3 off", "D4,LOW");
		
		commands.put("4 on", "D3,HIGH");
		commands.put("4 off", "D3,LOW");
	}

    public Command getCommand(String commandName) {
        Command cmd = super.getCommand(commandName);
        if (cmd != null) {
            String header = "Bearer " + token;
            cmd.setHeader("Authorization", header);
        }

        return cmd;
    }

    @Override
    protected List<Command> executeAll(String name) {
    	List<Command> chain = super.executeAll(name);
    	for (String cmd : commands.keySet()) {
    		if (cmd.contains(name)) {
//    			getCommand(cmd).doInBackground(scheme + host);
//			getCommand(cmd).execute(scheme + host);
    			chain.add(getCommand(name));
    		}
		}
    	return chain;
    }
    
	public boolean isLightControl() {
		return true;
	}

}
