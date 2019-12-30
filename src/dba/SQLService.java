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
    
    public ArrayList<TableInfo2> getData()
    {
        ArrayList<TableInfo2> tables = new ArrayList<TableInfo2>();
        
        tables.add(new TableInfo2("name1", "desc1"));
        tables.get(tables.size() - 1).insertRow("1field name1", "field desc1");
        tables.get(tables.size() - 1).insertRow("1field name2", "field desc2");
        tables.get(tables.size() - 1).insertRow("1field name3", "field desc3");
        
        tables.add(new TableInfo2("name2", "desc2"));
        tables.get(tables.size() - 1).insertRow("2field name1", "field desc1");
        tables.get(tables.size() - 1).insertRow("2field name2", "field desc2");
        tables.get(tables.size() - 1).insertRow("2field name3", "field desc3");
        
        return tables;
    }
    
    public void runDba(Vector<TableInfo2> fid, Vector<FieldInfo2> di, 
                       int fid_fetch, int di_fetch)
        throws SQLException
    {
        long time1 = System.currentTimeMillis();
        
        String sql = "SELECT FID, ACCKEYS, PARFID, DES, GLREF, GLOBAL, LISTDFT, LISTREQ, LTD, USER FROM DBTBL1 ORDER BY FID";
        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setFetchSize(fid_fetch);
        ResultSet rs_fid = prep.executeQuery();
		
        String FID = "";
        String LFID = "";
        
        int tbl_index = 0;
        int fid_index = 0;
        int counter = 0;
		
        int tbl_counter = 0;
		while(rs_fid.next())
		{
            FID = rs_fid.getString(1);
            
			fid.add(new TableInfo2(FID, rs_fid.getString(4)));
            
            String newSql = "SELECT FID, DI, NOD, POS, DES, TYP, LEN, DEC, REQ, TBL, CMP FROM DBTBL1D WHERE FID = ? ORDER BY DI";
            PreparedStatement prepNew = connection.prepareStatement(newSql);
            prepNew.setFetchSize(100);
            prepNew.setString(1, FID);
            
            ResultSet rs = prepNew.executeQuery();
            
            while(rs.next())
            {
                String DI = rs.getString("DI");
                System.out.println("FID: " + FID + " DI: " + DI);
            }
            
            System.out.println(FID);
            
			tbl_counter++;
		}
    }
	
	
    public void run_dba(Vector<TableInfo> fid, Vector<FieldInfo> di, 
                        int fid_fetch, int di_fetch)
		throws SQLException
	{
        long time1 = System.currentTimeMillis();
        
        String sql = "SELECT FID, ACCKEYS, PARFID, DES, GLREF, GLOBAL, LISTDFT, LISTREQ, LTD, USER FROM DBTBL1 ORDER BY FID";
        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setFetchSize(fid_fetch);
        ResultSet rs_fid = prep.executeQuery();
		
        String FID = "";
        String LFID = "";
        
        int tbl_index = 0;
        int fid_index = 0;
        int counter = 0;
		
        int tbl_counter = 0;
		while(rs_fid.next())
		{
            FID = rs_fid.getString(1);
            
			fid.add(new TableInfo(FID, rs_fid.getString(2), rs_fid.getString(3), rs_fid.getString(4), rs_fid.getString(5), rs_fid.getString(6), rs_fid.getString(7),rs_fid.getString(8),rs_fid.getString(9),rs_fid.getString(10)));
            
            System.out.println(FID);
            
			tbl_counter++;
		}
        
        sql = "SELECT FID, DI, NOD, POS, DES, TYP, LEN, DEC, REQ, TBL, CMP FROM DBTBL1D ORDER BY FID,DI";
        PreparedStatement prep1 = connection.prepareStatement(sql);
        prep1.setFetchSize(di_fetch);
        prep1.setMaxRows(35000);
        ResultSet rs_di = prep1.executeQuery();
        
        LFID = ""; FID = "";
        counter = 0;
        
        while(rs_di.next())
        {
            
            FID = rs_di.getString(1);
            
            if (LFID == "") LFID = FID;
            
            if (FID.equals(LFID))
            {
                counter = counter + 1;
            }
            else
            {
                /*
                System.out.println("fid_index = " + fid_index);
                System.out.println("tbl_index = " + tbl_index);
                System.out.println("tbl_counter = " + fid.size());
                System.out.println("FID = " + FID);
                System.out.println("LFID = " + LFID);
                System.out.println("fid.get(tbl_index).fid = " + fid.get(tbl_index).fid);
                */
                
                fid.get(tbl_index).index  = fid_index;
                fid.get(tbl_index).amount = counter;
                
                tbl_index = tbl_index + 1;
                fid_index = fid_index + counter;
                
                counter = 1;
            }
            
            di.add(new FieldInfo(FID, rs_di.getString(2), rs_di.getString(3), rs_di.getString(4), rs_di.getString(5), rs_di.getString(6), rs_di.getString(7), rs_di.getString(8), rs_di.getString(9), rs_di.getString(10), rs_di.getString(11)));
            
            System.out.println(FID);
            
            LFID = FID;
        }
        
        fid.get(tbl_index).index  = fid_index;
        fid.get(tbl_index).amount = counter;
        
        long time2 = System.currentTimeMillis();
        float diff  = (time2 - time1) / 1024.0f;
        System.out.println("Time elapsed (sec): " + diff);
        
        rs_fid.close();
        rs_di.close();
	}
    
}
