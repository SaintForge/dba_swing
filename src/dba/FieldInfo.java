package dba;

import java.io.Serializable;

class FieldInfo implements Serializable
{
	private String fileName;
	private String fieldName;
	private String subscriptKey;
	private String fieldPosition;
	private String description;
	private String dataType;
	private String fieldLength;
	private String decimalPrecision;
	private String requiredIndicator;
	private String lookUpTable;
	private String computedExpression;
	
	FieldInfo() {}
	
		FieldInfo(String fileName, String fieldName, String subscriptKey, String fieldPosition, String description,
				  String dataType, String fieldLength, String decimalPrecision, String requiredIndicator, String lookUpTable,
				  String computedExpression)
	{
		this.fileName = fileName;
		this.fieldName = fieldName;
		this.subscriptKey = subscriptKey;
		this.fieldPosition = fieldPosition;
		this.description = description;
		this.dataType = dataType;
		this.fieldLength = fieldLength;
		this.decimalPrecision = decimalPrecision;
		this.requiredIndicator = requiredIndicator;
		this.lookUpTable = lookUpTable;
		this.computedExpression = computedExpression;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String getFieldName()
	{
		return fieldName;
	}
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
	
	public String getSubscriptKey()
	{
		return subscriptKey;
	}
	public void setSubscriptKey(String subscriptKey)
	{
		this.subscriptKey = subscriptKey;
	}
	
	public String getFieldPosition()
	{
		return fieldPosition;
	}
	public void setFieldPosition(String fieldPosition)
	{
		this.fieldPosition = fieldPosition;
	}
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getDataType()
	{
		return dataType;
	}
	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}
	
	public String getFieldLength()
	{
		return fieldLength;
	}
	public void setFieldLength(String fieldLength)
	{
		this.fieldLength = fieldLength;
	}
	
	public String getDecimalPrecision()
	{
		return decimalPrecision;
	}
	public void setDecimalPrecision(String decimalPrecision)
	{
		this.decimalPrecision = decimalPrecision;
	}
	
	public String getRequiredIndicator()
	{
		return requiredIndicator;
	}
	public void setRequiredIndicator(String requiredIndicator)
	{
		this.requiredIndicator = requiredIndicator;
	}
	
	public String getLookUpTable()
	{
		return lookUpTable;
	}
	public void setLookUpTable(String lookUpTable)
	{
		this.lookUpTable = lookUpTable;
	}
	
	public String getComputedExpression()
	{
		return computedExpression;
	}
	public void setComputedExpression(String computedExpression)
	{
		this.computedExpression = computedExpression;
	}
	
}

class FieldInfo2
{
    String name;
    String desc;
    
    FieldInfo2(String name, String desc)
    {
        this.name = name;
        this.desc = desc;
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
}