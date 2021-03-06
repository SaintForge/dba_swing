package dba;

import java.sql.*;
import sanchez.jdbc.driver.ScDriver;
import sanchez.jdbc.pool.ScConnectionPoolDataSource;
import sanchez.jdbc.pool.ScJDBCConnectionPoolCache;
import sanchez.jdbc.pool.ScJdbcPool;
//import fisglobal.jdbc.driver.ScDriver;

import java.util.Properties;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame; 
import javax.swing.SwingWorker;

class SQLService extends SwingWorker<Object, Object>
{
    private static String driver_name = 
		"sanchez.jdbc.driver.ScDriver";
	
    private String address;
    private String username;
    private String password;
    private String port;
	private String url;
	private boolean isDBA;
	
	private Connection connection;
	
	private TableInfo tableInfoBuffer;
	private ArrayList<TableInfo> tableInfoArrayBuffer;
	
    SQLService() {}
    SQLService(String server_address, String server_port, 
               String username, String user_password)
		throws NullPointerException
    {
		this.address  = server_address;
		this.port     = server_port;
		this.username = username;
		this.password = user_password;
		
		this.url = "protocol=jdbc:sanchez/database="+ server_address + ":" + server_port + ":SCA$IBS/locale=US:ENGLISH/timeOut=2/transType=MTM/rowPrefetch=3000/signOnType=1/fileEncoding=UTF-8";
    }
	
	public void connect() throws ClassNotFoundException, SQLException
	{
		Class.forName(driver_name);
        ScDriver driver = new ScDriver();
		DriverManager.registerDriver(driver);
		DriverManager.setLoginTimeout(10);
		connection = DriverManager.getConnection(this.url, this.username, this. password);
	}
	
	public TableInfo run_query(String fileName, int fieldAmount, int indexAmount) throws SQLException
	{
			ResultSet result = null;
			String sql = "SELECT FID, ACCKEYS, DES, GLREF, GLOBAL, LISTDFT, LISTREQ, LTD, USER FROM DBTBL1 WHERE FID = '" 
			+ fileName + "'";
			
			PreparedStatement prep = connection.prepareStatement(sql);
		
		TableInfo tableInfo = new TableInfo();
		
			ResultSet rsFid = prep.executeQuery();
			if (rsFid.next())
			{
				 tableInfo = new TableInfo(fileName,              // File Name
									  rsFid.getString(2),   // Primary Keys
									  rsFid.getString(3),   // Description
									  rsFid.getString(4),   // Global Name
									  rsFid.getString(5),   // Global Reference
									  rsFid.getString(6),   // Default Data Item List
									  rsFid.getString(7),   // Required Data Item List
									  rsFid.getString(8),   // Last Updated
										  rsFid.getString(9));  // User ID
		}
		
			sql = "SELECT FID, DI, NOD, POS, DES, TYP, LEN, DEC, REQ, TBL, CMP FROM DBTBL1D WHERE FID = '" + tableInfo.getFileName() + "'";
			
			prep = connection.prepareStatement(sql);
			prep.setFetchSize(fieldAmount + 50);
			prep.setMaxRows(40000);
			
			ResultSet rsDi = prep.executeQuery();
			while(rsDi.next())
			{
			tableInfo.getFields().add(new FieldInfo(fileName,              // File Name
										 rsDi.getString(2),    // Field Name
										 rsDi.getString(3),    // Subscript Key
										 rsDi.getString(4),    // Field Position
										 rsDi.getString(5),    // Description
										 rsDi.getString(6),    // Data Type
										 rsDi.getString(7),    // Field Length
										 rsDi.getString(8),    // Decimal Precision
										 rsDi.getString(9),    // Required Indicator
										 rsDi.getString(10),   // Look-Up Table
													   rsDi.getString(11))); // Computed Expression
		}
		
		sql = "SELECT FID, INDEXNM, GLOBAL, ORDERBY, IDXDESC, UPCASE, PARFID FROM DBTBL8 WHERE FID = '" + fileName + "'";
		PreparedStatement prep2 = connection.prepareStatement(sql);
		prep2.setFetchSize(indexAmount + 5);
		
		ResultSet rsIndex = prep2.executeQuery();
		while(rsIndex.next())
		{
			tableInfo.getIndexes().add(new IndexInfo(rsIndex.getString(2),   // Index Name
													 rsIndex.getString(3),   // Global Name
													 rsIndex.getString(4),   // Order by
													 rsIndex.getString(5),   // Index Description
													 rsIndex.getString(6),   // Convert To Upper Case
													 rsIndex.getString(7))); // Super Type File Name
		}
		
		sql = "SELECT FID, DES FROM DBTBL1TBLDOC";
		PreparedStatement prep3 = connection.prepareStatement(sql);
		prep3.setFetchSize(7000);
		
		ResultSet rsDocs = prep3.executeQuery();
		while(rsDocs.next())
		{
			fileName = rsDocs.getString(1);
			
			String fileDocumentation = tableInfo.getFileDocumentation() + checkStringOrNull(rsDocs.getString(2)) + '\n';
			tableInfo.setFileDocumentation(fileDocumentation);
		}
		
		return (tableInfo);
	}
	
    public void run_dba(ArrayList<TableInfo> fid) throws SQLException
                        
	{
		setProgress(1);
		
        long time1      = System.currentTimeMillis();
        String fileName = "";
		int fileIndex   = 0;
		
		HashMap<String, Integer> tableMap = new HashMap<String, Integer>();
		
		String sql = "SELECT FID, ACCKEYS, DES, GLREF, GLOBAL, LISTDFT, LISTREQ, LTD, USER FROM DBTBL1 ORDER BY FID";
        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setFetchSize(4000);
		
        ResultSet rs_fid = prep.executeQuery();
		while(rs_fid.next())
		{
			fileName = rs_fid.getString(1);
            
			fid.add(new TableInfo(fileName,              // File Name
								  rs_fid.getString(2),   // Primary Keys
								  rs_fid.getString(3),   // Description
								  rs_fid.getString(4),   // Global Name
								  rs_fid.getString(5),   // Global Reference
								  rs_fid.getString(6),   // Default Data Item List
								  rs_fid.getString(7),   // Required Data Item List
								  rs_fid.getString(8),   // Last Updated
								  rs_fid.getString(9))); // User ID
            
			tableMap.put(fileName, fileIndex++);
		}
		
		setProgress(2);
		
        sql = "SELECT FID, DI, NOD, POS, DES, TYP, LEN, DEC, REQ, TBL, CMP FROM DBTBL1D" ;
        prep = connection.prepareStatement(sql);
        prep.setFetchSize(10000);
        prep.setMaxRows(40000);
		
        ResultSet rs_di = prep.executeQuery();
        while(rs_di.next())
        {
             fileName = rs_di.getString(1);
            
			if(tableMap.containsKey(fileName))
			{
				 fileIndex = tableMap.get(fileName);
				fid.get(fileIndex).getFields().add(new FieldInfo(fileName,              // File Name
																 rs_di.getString(2),    // Field Name
																 rs_di.getString(3),    // Subscript Key
																 rs_di.getString(4),    // Field Position
																 rs_di.getString(5),    // Description
																 rs_di.getString(6),    // Data Type
																 rs_di.getString(7),    // Field Length
																 rs_di.getString(8),    // Decimal Precision
																 rs_di.getString(9),    // Required Indicator
																 rs_di.getString(10),   // Look-Up Table
																 rs_di.getString(11))); // Computed Expression
			}
			
        }
		
		setProgress(3);
		
		sql = "SELECT FID, INDEXNM, GLOBAL, ORDERBY, IDXDESC, UPCASE, PARFID FROM DBTBL8";
		 prep = connection.prepareStatement(sql);
		prep.setFetchSize(500);
		
		ResultSet rsIndex = prep.executeQuery();
		while(rsIndex.next())
		{
			fileName = rsIndex.getString(1);
			
			if (tableMap.containsKey(fileName))
			{
				fileIndex = tableMap.get(fileName);
				fid.get(fileIndex).getIndexes().add(new IndexInfo(rsIndex.getString(2),   // Index Name
																  rsIndex.getString(3),   // Global Name
																  rsIndex.getString(4),   // Order by
																  rsIndex.getString(5),   // Index Description
																  rsIndex.getString(6),   // Convert To Upper Case
																  rsIndex.getString(7))); // Super Type File Name
			}
		}
		
		setProgress(4);
		
		sql = "SELECT FID, DES FROM DBTBL1TBLDOC";
		prep = connection.prepareStatement(sql);
		prep.setFetchSize(7000);
		
		ResultSet rsDocs = prep.executeQuery();
		while(rsDocs.next())
		{
			fileName = rsDocs.getString(1);
			
			if (tableMap.containsKey(fileName))
			{
				fileIndex = tableMap.get(fileName);
				
				TableInfo tableInfo = fid.get(fileIndex);
				String fileDocumentation = tableInfo.getFileDocumentation() + checkStringOrNull(rsDocs.getString(2)) + '\n';
				tableInfo.setFileDocumentation(fileDocumentation);
			}
		}
		
        long time2  = System.currentTimeMillis();
        float diff  = (time2 - time1) / 1024.0f;
        System.out.println("Time elapsed (sec): " + diff);
        
        if (rs_fid != null)  rs_fid.close();
        if (rs_di != null)   rs_di.close();
		if (rsIndex != null) rsIndex.close();
		if (rsDocs != null)  rsDocs.close();
		
		if (prep != null)    prep.close();
	}
    
	static String checkStringOrNull(String obj)
	{
		return (obj != null) ? obj : "";
	}
	
	public boolean isConnected()
	{
		return connection == null ? false : true;
	}
	
	@Override 
		protected Object doInBackground() 
		throws Exception 
	{
		try
		{
			tableInfoArrayBuffer = new ArrayList<TableInfo>();
			setProgress(0);
			
			connect();
			if (isConnected())
			{
				if (isDBA)
				{
					run_dba(tableInfoArrayBuffer);
				}
				
				if (connection != null) connection.close();
				
				 // Just for the gui to show final message
				setProgress(5);
				Thread.sleep(250);
			}
		}
		catch(SQLException exc)
		{
			exc.printStackTrace();
			setProgress(6);
		}
		catch(ClassNotFoundException exc)
		{
			exc.printStackTrace();
			setProgress(6);
		}
		
		return null;
	}
	
	public ArrayList<TableInfo> getTableInfoArrayBuffer()
	{
		return tableInfoArrayBuffer;
	}
	public void setTableInfoArrayBuffer(ArrayList<TableInfo> tableInfoArrayBuffer)
	{
		this.tableInfoArrayBuffer = tableInfoArrayBuffer;
	}
	
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getPort()
	{
		return port;
	}
	public void setPort(String port)
	{
		this.port = port;
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public boolean getIsDBA()
	{
		return isDBA;
	}
	public void setIsDBA(boolean isDBA)
	{
		this.isDBA = isDBA;
	}
}
