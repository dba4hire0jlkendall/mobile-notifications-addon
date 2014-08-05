package org.exoplatform.mobile.notifications.model;

public class Registration {
	
	public final static String PLATFORM_TYPE_ANDROID = "android";
	public final static String PLATFORM_TYPE_IOS = "ios";

	public String device_id;
	public String platform;
	public String username;
	
	public Registration() {
		device_id = "";
		platform = "";
		username = "";
	}
	
	public Registration(String id, String plf, String usr)
	{
		device_id = (id == null ? "" : id);
		username = (usr == null ? "" : usr);
		
		if ("android".equalsIgnoreCase(plf) || "ios".equalsIgnoreCase(plf)) platform = plf.toLowerCase();
		else throw new RuntimeException("Incorrect platform "+plf);
		
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String id) {
		this.device_id = id;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Registration { 'deviceId' : '"+device_id+"' , 'platform' : '"+platform+"' , 'username' : '"+username+"' }";
	}

	@Override
	public boolean equals(Object obj) {
		Registration other = (Registration)obj;
		return this.device_id.equals(other.device_id) && this.platform.equals(other.platform) && this.username.equals(other.username);
	}
	
	
	
}
