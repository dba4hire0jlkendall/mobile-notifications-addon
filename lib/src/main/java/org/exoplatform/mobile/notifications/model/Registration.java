package org.exoplatform.mobile.notifications.model;

public class Registration {

	public String id;
	public String platform;
	public String username;
	
	public Registration()
	{
		id = "";
		platform = "";
		username = "";
	}
	
	public Registration(String _id, String _plf, String _usr)
	{
		id = (_id == null ? "" : _id);
		platform = (_plf == null ? "" : _plf);
		username = (_usr == null ? "" : _usr);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = (id == null ? "" : id);
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = (platform == null ? "" : platform);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = (username == null ? "" : username);
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
