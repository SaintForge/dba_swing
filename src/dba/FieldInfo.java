package dba;

import java.io.Serializable;

class FieldInfo implements Serializable
{
	String fid;
	String di;
	String nod;
	String pos;
	String des;
	String typ;
	String len;
	String dec;
	String req;
	String tbl;
	String cmp;
	
	FieldInfo() {}
	
	FieldInfo(String fid, String di, String nod, String pos, String des, String typ, String len, String dec, String req, String tbl, String cmp)
	{
		this.fid = fid;
		this.di  = di;
		this.nod = nod;
		this.pos = pos;
		this.des = des;
		this.typ = typ;
		this.len = len;
		this.dec = dec;
		this.req = req;
		this.tbl = tbl;
		this.cmp = cmp;
	}
}
