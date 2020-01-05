package dba;

public class SettingsData
{
	private String name;
	private String sqlServer;
	private String mtmPort;
	private String profileUser;
	private String profilePassword;
	
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