package dba;

import java.io.Serializable;

class IndexInfo implements Serializable
{
	private String indexName;
	private String globalName;
	private String orderBy;
	private String indexDescription;
	private String convertToUpperCase;
	private String superTypeFileName;
	
	IndexInfo() {}
	IndexInfo(String indexName, String globalName, String orderBy, String indexDescription, String convertToUpperCase,
			  String superTypeFileName)
	{
		this.indexName = indexName;
		this.globalName = globalName;
		this.orderBy = orderBy;
		this.indexDescription = indexDescription;
		this.convertToUpperCase = convertToUpperCase;
		this.superTypeFileName = superTypeFileName;
	}
	
	public String getIndexName()
		{
		return indexName;
	}
	public void setIndexName(String indexName)
		{
		this.indexName = indexName;
	}
	
	public String getGlobalName()
	{
		return globalName;
	}
	public void setGlobalName(String globalName)
	{
		this.globalName = globalName;
	}
	
	public String getOrderBy()
	{
		return orderBy;
	}
	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}
	
	public String getIndexDescription()
	{
		return indexDescription;
	}
	public void setIndexDescription(String indexDescription)
	{
		this.indexDescription = indexDescription;
	}
	
	public String getConvertToUpperCase()
	{
		return convertToUpperCase;
	}
	public void setConvertToUpperCase(String convertToUpperCase)
	{
		this.convertToUpperCase = convertToUpperCase;
	}
	
	public String getSuperTypeFileName()
	{
		return superTypeFileName;
	}
	public void setSuperTypeFileName(String superTypeFileName)
	{
		this.superTypeFileName = superTypeFileName;
	}
	
}