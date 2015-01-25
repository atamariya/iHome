package com.at.iHome.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.at.iHome.api.Command;
import com.at.iHome.api.Device;
import com.at.iHome.api.ResponseProcessor;

import org.json.JSONException;
import org.json.JSONObject;

public class XBMC extends Device {
    protected Map<String, String> values = new HashMap<String, String>();
    protected Map<String, String> paramTemplate = new HashMap<String, String>();

    public XBMC(String name, String host) {
		super(name, host);
		CMD = "{\"jsonrpc\": \"2.0\", \"method\": \"%s\", \"params\": { %s }, \"id\": 1}";
        uri = "/jsonrpc";
        this.url = scheme + host + uri;
        
		// Mode control
//		commands.put("music", "PutZone_InputFunction/GAME");
//		commands.put("tv", "PutZone_InputFunction/SAT/CBL");
//		commands.put("picture", "PutZone_InputFunction/TUNER");
//		commands.put("movie", "PutZone_InputFunction/MPLAY");

		// Volume control
        // {"jsonrpc":"2.0","method":"Application.SetMute","id":1,"params":{"mute":"toggle"}}
        // {"jsonrpc":"2.0","method":"Application.SetVolume","id":1,"params":{"volume":"increment"}}
     	commands.put("volume up", "PutMasterVolumeBtn/>");
		commands.put("volume down", "PutMasterVolumeBtn/<");
		commands.put("volume mute", "PutVolumeMute/on");
		commands.put("volume unmute", "PutVolumeMute/off");
        values.put("volume up", "\"volume\":\"increment\"");
        values.put("volume down", "\"volume\":\"decrement\"");
        values.put("volume mute", "\"mute\":\"toggle\"");
        values.put("volume unmute", "\"mute\":\"toggle\"");


		// Power control
        //{"jsonrpc":"2.0","method":"System.Shutdown","id":1}
		commands.put("off", "System.Shutdown");

		// Player control
        // { "jsonrpc": "2.0", "method": "Input.ExecuteAction", "params": { "action": "stop" }, "id": 1 }
		commands.put("play", "Input.ExecuteAction"); // { "action": "play" }
		commands.put("pause", "Input.ExecuteAction"); //  { "action": "pause" }
        commands.put("stop", "Input.ExecuteAction"); //{ "action": "stop" }
        values.put("play", "\"action\": \"play\""); // { "action": "play" }
        values.put("pause", "\"action\": \"pause\""); //  { "action": "pause" }
        values.put("stop", "\"action\": \"stop\""); //{ "action": "stop" }
        // {"jsonrpc":"2.0","id":"1","method":"Player.Open","params":{"item":{"file":"nfs://192.168.0.33/media/shared/Songs/F00/BYWW.mp3"}}}
        commands.put("open song", "Player.Open");
        values.put("open song", "\"item\":{%s}");
        commands.put("open movie", "Player.Open");
        values.put("open movie", "\"item\":{%s}");

        //{"jsonrpc":"2.0","method":"Player.GoTo","id":1,"params":{"playerid":0,"to":"next"}}
        commands.put("next", "Player.GoTo");
        commands.put("previous", "Player.GoTo");
        values.put("next", "\"playerid\":0,\"to\":\"next\"");
        values.put("previous", "\"playerid\":0,\"to\":\"previous\"");

        // {"jsonrpc":"2.0","method":"Player.SetSpeed","id":1,"params":{"playerid":0,"speed":"increment"}}
        commands.put("forward", "Player.SetSpeed");
        commands.put("rewind", "Player.SetSpeed");
        values.put("forward", "\"playerid\":0,\"speed\":\"increment\"");
        values.put("rewind", "\"playerid\":0,\"speed\":\"decrement\"");

        // {"jsonrpc":"2.0","method":"AudioLibrary.GetSongs","id":1,"params":{"filter":{"field":"title", "operator":"contains","value":"teri yaad"},"properties":["lastplayed"],"sort":{"order":"descending","method":"lastplayed"}}}
        commands.put("search song", "AudioLibrary.GetSongs");
        commands.put("search movie", "VideoLibrary.GetMovies");

        // { "jsonrpc": "2.0", "method": "GUI.ShowNotification", "params": { "title": "Scott's Message", "message": "I should save this movie." }, "id": 1 }
        commands.put("notify", "GUI.ShowNotification");
        
        String str = "\"filter\":{\"field\":\"title\", \"operator\":\"contains\",\"value\":\"%s\"}," +
                "\"properties\":[\"lastplayed\"],\"sort\":{\"order\":\"descending\",\"method\":\"lastplayed\"}" +
                ", \"limits\": {\"end\": 1}";
        paramTemplate.put("search song", str);
        paramTemplate.put("search movie", str);
        
        str = "\"title\": \"%s\", \"message\": \"%s\"";
        paramTemplate.put("notify", str);
        
        str = "\"item\":{\"songid\": %s}";
        paramTemplate.put("open song", str);
        str = "\"item\":{\"movieid\": %s}";
        paramTemplate.put("open movie", str);
        
	}
	
	public Command getCommand(String commandName) {
        final Command cmd = super.getCommand(commandName);
        if (cmd != null) {
            cmd.setHeader(Command.CONTENT_TYPE, Command.APPLICATION_JSON);
            cmd.setType(Command.APPLICATION_JSON);

            if (getParams() != null && paramTemplate.get(commandName) != null) {
                String[] arr = getParams().toArray(new String[getParams().size()]);
                values.put(commandName, String.format(paramTemplate.get(commandName), arr));
            }
            
            cmd.setValue(String.format(CMD, commands.get(commandName), values.get(commandName)));
            cmd.setName(commandName);
            
            cmd.setResponseProcessor(new ResponseProcessor() {
				
				@Override
                public String process(String response) throws JSONException {
                    JSONObject json = null;
					String commandName = cmd.getName();
                    if (response != null) {
                        json = new JSONObject(response);
                        if (json.get("result") instanceof String)
                            return null; // OK string

                        json = json.getJSONObject("result");
                        if ("open song".equals(commandName))
                            setParams(json.getJSONArray("songs").getJSONObject(0).getString("songid"));
                        else if ("open movie".equals(commandName))
                            setParams(json.getJSONArray("movies").getJSONObject(0).getString("movieid"));
                    }

		            if (getParams() != null && paramTemplate.get(commandName) != null) {
		                String[] arr = getParams().toArray(new String[getParams().size()]);
		                values.put(commandName, String.format(paramTemplate.get(commandName), arr));
		            }
		            
		            return (String.format(CMD, commands.get(commandName), values.get(commandName)));
					
				}
			});
            try {
                cmd.setValue(cmd.getResponseProcessor().process(null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return cmd;
    }

	@Override
	public boolean isAudioDevice() {
		return true;
	}
}
