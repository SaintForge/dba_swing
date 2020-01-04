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

class SQLService
{
    private static String driver_name = 
		"sanchez.jdbc.driver.ScDriver";
	
    String address;
    String username;
    String password;
    String port;
	String url;
	
    public Connection connection = null;
    
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
	
	public void connect() 
		throws ClassNotFoundException, SQLException
	{
		Class.forName(driver_name);
        
        ScDriver driver = new ScDriver();
		DriverManager.registerDriver(driver);
		connection = DriverManager.getConnection(this.url, this.username, this. password);
		
		System.out.print("Connected.\n");
	}
	
	public ResultSet run_query(String sql, int fetch_size) 
		throws SQLException
	{
		ResultSet result = null;
		
		if(connection != null)
		{
			Statement stmt = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            // stmt.setMaxRows(35000);
			stmt.setFetchSize(fetch_size);
			
			if (sql.toLowerCase().startsWith("select"))
			{
				System.out.println("Executing query");
				result = stmt.executeQuery(sql);
                //result = stmt.getResultSet();
			}
		}
		
		return(result);
	}
    
    public void run_dba(ArrayList<TableInfo> fid) throws SQLException
                        
	{
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
		
		System.out.println("HashMap size: " + tableMap.size());
		System.out.println("tbl_counter: " + fileIndex);
		
        sql = "SELECT FID, DI, NOD, POS, DES, TYP, LEN, DEC, REQ, TBL, CMP FROM DBTBL1D" ;
        PreparedStatement prep1 = connection.prepareStatement(sql);
        prep1.setFetchSize(10000);
        prep1.setMaxRows(40000);
		
        ResultSet rs_di = prep1.executeQuery();
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
		
		sql = "SELECT FID, INDEXNM, GLOBAL, ORDERBY, IDXDESC, UPCASE, PARFID FROM DBTBL8";
		PreparedStatement prep2 = connection.prepareStatement(sql);
		prep2.setFetchSize(500);
		
		ResultSet rsIndex = prep2.executeQuery();
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
		
		sql = "SELECT FID, DES FROM DBTBL1TBLDOC";
		PreparedStatement prep3 = connection.prepareStatement(sql);
		prep3.setFetchSize(7000);
		
		ResultSet rsDocs = prep3.executeQuery();
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
        
        rs_fid.close();
        rs_di.close();
	}
    
	static String checkStringOrNull(String obj)
	{
		return (obj != null) ? obj : "";
	}
}
