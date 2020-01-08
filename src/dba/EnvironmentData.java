package dba;

import java.util.ArrayList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


class GlobalData
{
	public ArrayList<EnvironmentData> data;
	
	public GlobalData() 
	{
		data = new ArrayList<EnvironmentData>();
	}
	
	private static GlobalData instance;
	
	public static GlobalData getInstance()
	{
		if (instance == null) instance = new GlobalData();
		return instance;
	}
	
	public static void readFromFile()
	{
		try 
		{
			Kryo kryo = new Kryo();
			kryo.register(SettingsData.class);
			kryo.register(FieldInfo.class);
			kryo.register(TableInfo.class);
			kryo.register(IndexInfo.class);
			kryo.register(EnvironmentData.class);
			kryo.register(ArrayList.class);
			
			Input input = new Input(new FileInputStream("package.bin"));
			@SuppressWarnings("unchecked")
				ArrayList<EnvironmentData> data_ = kryo.readObject(input, ArrayList.class);
			input.close();
			
			GlobalData.getInstance().data = data_;
		}
		catch(FileNotFoundException exc)
		{
			
		}
	}
	
	public static void writeToFile()
	{
		try 
		{
			ArrayList<EnvironmentData> data = GlobalData.getInstance().data;
			
			Kryo kryo = new Kryo();
			kryo.register(SettingsData.class);
			kryo.register(FieldInfo.class);
			kryo.register(TableInfo.class);
			kryo.register(IndexInfo.class);
			kryo.register(EnvironmentData.class);
			kryo.register(ArrayList.class);
			
			Output output = new Output(new FileOutputStream("package.bin"));
			kryo.writeObject(output, data);
			output.close();
			
		}
		catch(FileNotFoundException exc)
		{
			
		}
	}
}

class EnvironmentData
{
	private SettingsData settings = new SettingsData();
	private ArrayList<TableInfo> tableArray = new ArrayList<TableInfo>();
	
	EnvironmentData() {}
	
	EnvironmentData(SettingsData settings) 
	{
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
    
    public ArrayList<TableInfo> getTableArray()
    {
        return tableArray;
    }
    
    public void setTableArray(ArrayList<TableInfo> tableArray)
    {
        this.tableArray = tableArray;
    }
}