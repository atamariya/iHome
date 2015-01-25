package com.at.iHome.api;

public class Context {
	private String location;
	
	public Context(String location) {
		this.location = location;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		
		if (this.location == null)
			result = true;
		else 
			result = this.location.equals(((Context) obj).getLocation());
		
		return result;
	}
	
	public String getLocation() {
		return location;
	}

}
