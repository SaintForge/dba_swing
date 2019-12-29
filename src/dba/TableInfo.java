package dba;

import java.util.ArrayList;

import java.io.Serializable;

class TableInfo implements Serializable
{
	String fid;
	String acckeys;
	String parfid;
	String des;
	String glref;
	String global;
	String listdft;
	String listreq;
	String ltd;
	String user;
	
	int index;
	int amount;
	
	TableInfo() {}
	
	TableInfo(String fid, String acckeys, String parfid, String des, String glref, String global,String listdft, String listreq, String ltd, String user)
	{
		this.fid = fid;
		this.acckeys = acckeys;
		this.parfid = parfid;
		this.des = des;
		this.glref = glref;
		this.global = global;
		this.listdft = listdft;
		this.listreq = listreq;
		this.ltd = ltd;
		this.user = user;
	}
}

class TableInfo2
{
    String name;
    String desc;
    
    String global;
    String keys;
    
    ArrayList<FieldInfo2> fields;
    
    TableInfo2(String name, String desc)
    {
        this.name = name;
        this.desc = desc;
        
        this.global = "global";
        this.keys = "keys";
        
        fields = new ArrayList<FieldInfo2>();
    }
    
    public void insertRow(String name, String desc)
    {
        fields.add(new FieldInfo2(name, desc));
    }
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getDesc()
    {
        return desc;
    }
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public void setFields(ArrayList<FieldInfo2> fieldArray)
    {
        this.fields = fieldArray;
    }
    public ArrayList<FieldInfo2> getFields()
    {
        return fields;
    }
    
    public String getGlobal()
    {
        return global;
    }
    
    public void setGlobal(String global)
    {
        this.global = global;
    }
    
    public String getKeys()
    {
        return keys;
    }
    
    public void setKeys()
    {
        this.keys = keys;
    }
}