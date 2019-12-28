package dba;

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