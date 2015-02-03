package com.at.iHome.logic;

import java.util.HashMap;
import java.util.List;
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
public class LightControl extends Device {
    private String token = "cb1b31f441b50a1f9e4496c44a15da5abb3cfe2b";
    protected Map<String, String> paramTemplate = new HashMap<String, String>();

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
        type = DeviceType.SPARK_CORE;
        
        // Special case
        commands.put("on", "D6,HIGH");
		commands.put("off", "D6,LOW");
		
		paramTemplate.put("on", "D%s,HIGH");
		paramTemplate.put("off", "D%s,LOW");

	}

    public Command getCommand(String commandName) {
        Command cmd = super.getCommand(commandName);
        if (cmd != null) {
            String header = "Bearer " + token;
            cmd.setHeader("Authorization", header);
            
            if (getParams() != null && paramTemplate.get(commandName) != null) {
                String[] arr = getParams().toArray(new String[getParams().size()]);
                cmd.setValue(String.format(paramTemplate.get(commandName), arr));
            }
            
        }

        return cmd;
    }

    @Override
    protected List<Command> executeAll(String name) {
    	List<Command> chain = super.executeAll(name);
    	for (String cmd : new String[] {"5", "6", "3", "4"}) {
    		setParams(cmd);
    		chain.add(getCommand(name));
		}
    	return chain;
    }
    
	public boolean isLightControl() {
		return true;
	}

}
