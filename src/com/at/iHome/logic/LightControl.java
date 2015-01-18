package com.at.iHome.logic;

import com.at.iHome.api.Command;
import com.at.iHome.api.Device;

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

	public LightControl(String name, String host) {
		super(name, host);
        scheme = "https://";

		// Mode control
		String url = "/v1/devices/" + name + "/digitalwrite";
		Command cmd = new Command(url, "params", "D6,HIGH");
        String header = "Bearer " + token;
        cmd.setHeader("Authorization", header);
		commands.put("on", cmd);
		
		cmd = new Command(url, "params", "D6,LOW");
		cmd.setHeader("Authorization", header);
		commands.put("off", cmd);
	}
	
	public boolean isLightControl() {
		return true;
	}

}
