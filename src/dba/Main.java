package dba;

import java.sql.*;

import java.util.Vector;

import sanchez.jdbc.driver.ScDriver;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{
    public static void run()
    {
        //try
        {
            //SQLService sql = new SQLService("172.29.7.82", "20101", "1", "xxx");
            //sql.connect();
            
            //Vector<TableInfo> fid = new Vector<TableInfo>();
            
            //sql.run_dba(fid, 4000, 10000);
            
            System.out.println("completed.");
        }
        //catch(ClassNotFoundException exc)
        {
            
        }
        //catch(SQLException exc)
        {
            
        }
        
        
        MainFrame main = new MainFrame();
    }
    
    public static void main(String[] args) 
    {
        try
        {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }  
        catch (UnsupportedLookAndFeelException e) {}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        
        run();
    }
}