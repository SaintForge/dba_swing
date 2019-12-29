package dba;

import java.util.ArrayList;

class EnvironmentData
{
	SettingsData settings;
	ArrayList<TableInfo2> tableArray;
	
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
    
    public ArrayList<TableInfo2> getTableArray()
    {
        return tableArray;
    }
    
    public void setTableArray(ArrayList<TableInfo2> tableArray)
    {
        this.tableArray = tableArray;
    }
}