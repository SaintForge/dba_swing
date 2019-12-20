package dba;



class EnvironmentData
{
	SettingsData settings;
	// ArrayList<T> data;
	
	EnvironmentData(SettingsData settings) 
	{
		this.settings = new SettingsData();
		
		this.settings.setName(settings.getName());
		this.settings.setSqlServer(settings.getSqlServer());
		this.settings.setMtmPort(settings.getMtmPort());
		this.settings.setProfileUser(settings.getProfileUser());
		this.settings.setProfilePassword(settings.getProfilePassword());
	}
	
	public SettingsData getSettings() 
	{
		return settings;
	}
	
	public void setSettings(SettingsData settings) 
	{
		this.settings = settings;
	}
}