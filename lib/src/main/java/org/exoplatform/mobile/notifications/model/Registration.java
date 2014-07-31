package org.exoplatform.mobile.notifications.model;

public class Registration {
	
	public final static String PLATFORM_TYPE_ANDROID = "android";
	public final static String PLATFORM_TYPE_IOS = "ios";

	public String id;
	public String platform;
	public String username;
	
	public Registration() {
		id = "";
		platform = "";
		username = "";
	}
	
	public Registration(String _id, String _plf, String _usr)
	{
		id = (_id == null ? "" : _id);
		username = (_usr == null ? "" : _usr);
		
		if ("android".equalsIgnoreCase(_plf) || "ios".equalsIgnoreCase(_plf)) platform = _plf.toLowerCase();
		else throw new RuntimeException("Incorrect platform "+_plf);
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return "Registration { 'id' : '"+id+"' , 'platform' : '"+platform+"' , 'username' : '"+username+"' }";
	}

	@Override
	public boolean equals(Object obj) {
		Registration other = (Registration)obj;
		return this.id.equals(other.id) && this.platform.equals(other.platform) && this.username.equals(other.username);
	}
	
	
	
}
