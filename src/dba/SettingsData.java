package dba;

import java.io.Serializable;

public class SettingsData implements Serializable
{
	private String name;
	private String sqlServer;
	private String mtmPort;
	private String profileUser;
	private String profilePassword;
	
	public SettingsData() {}
	
	public SettingsData(String name, String sqlServer, String mtmPort, String profileUser, String profilePassword)
	{
		this.name = name;
		this.sqlServer = sqlServer;
		this.mtmPort = mtmPort;
		this.profileUser = profileUser;
		this.profilePassword = profilePassword;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSqlServer() {
		return sqlServer;
	}
	public void setSqlServer(String sqlServer) {
		this.sqlServer = sqlServer;
	}
	
	public String getMtmPort() {
		return this.mtmPort;
	}
	public void setMtmPort(String mtmPort) {
		this.mtmPort = mtmPort;
	}
	
	public String getProfileUser() {
		return profileUser;
	}
	public void setProfileUser(String profileUser) {
		this.profileUser = profileUser;
	}
	
	public String getProfilePassword() {
		return profilePassword;
	}
	public void setProfilePassword(String profilePassword) {
		this.profilePassword = profilePassword;
	}
}