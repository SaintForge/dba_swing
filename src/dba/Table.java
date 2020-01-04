package dba;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.Font;

class Table extends JPanel
{
    DefaultTableModel model;
    JTable table;
    JScrollPane scrollPane;
    
    Table(String columnName)
    {
        super(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        
        model = new DefaultTableModel();
        model.addColumn(columnName);
        
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Courier New", Font.PLAIN, 14));
		table.setShowVerticalLines(true);
        
        scrollPane = new JScrollPane(table);
		
		add(scrollPane, BorderLayout.CENTER);
    }
    
    Table(String firstColumnName, String secondColumnName)
    {
        super(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        
        model = new DefaultTableModel();
        model.addColumn(firstColumnName);
        model.addColumn(secondColumnName);
        
        table = new JTable(model);
        table.setFillsViewportHeight(true);
		table.setFont(new Font("Courier New", Font.PLAIN, 14));
		table.setShowVerticalLines(true);
        
        scrollPane = new JScrollPane(table);
		
		add(scrollPane, BorderLayout.CENTER);
    }
    
    // populate data functions
    public void populateTableProps(TableInfo tableInfo)
    {
        System.out.println("populateTableProps");
        
        String tableProperties[] = 
		{
			"File Name",
			"Description",
			"Primary Keys",
			"Global Name",
			"Global Reference",
			"Default Data Item List",
			"Required Data Item List",
			"Last Updated",
			"User ID"
		};
        
        String tableData[] = 
        {
            tableInfo.getFileName(),
            tableInfo.getDescription(),
            tableInfo.getPrimaryKeys(),
            tableInfo.getGlobalName(),
            tableInfo.getGlobalReference(),
            tableInfo.getDefaultDataItemList(),
            tableInfo.getRequiredDataItemList(),
            tableInfo.getLastUpdated(),
            tableInfo.getUserID()
        };
        
        populateTable(tableProperties, tableData);
        
        //table.setModel(model);
        //model.fireTableDataChanged();
    }
    
    public void populateTableArray(ArrayList<TableInfo> tableArray)
    {
        model.setRowCount(0);
        
        for (int i = 0; i < tableArray.size(); ++i)
        {
            String row[] = new String[2];
            row[0] = tableArray.get(i).getFileName();
            row[1] = tableArray.get(i).getDescription();
            model.addRow(row);
        }
        
        table.setModel(model);
        model.fireTableDataChanged();
    }
    public void populateFieldArray(ArrayList<FieldInfo> fieldArray)
    {
        model.setRowCount(0);
        
        for (int i = 0; i < fieldArray.size(); ++i)
        {
            String row[] = new String[2];
            row[0] = fieldArray.get(i).getFieldName();
            row[1] = fieldArray.get(i).getDescription();
            model.addRow(row);
        }
        
        table.setModel(model);
        model.fireTableDataChanged();
    }
    
    public void populateTable(String data[])
    {
        System.out.println("populateTable");
        model.setRowCount(0);
        
        for(int i = 0; i < data.length; ++i)
        {
            String row[] = new String[2];
            row[0] = data[i];
            row[1] = "";
            model.addRow(row);
        }
        
        table.setModel(model);
        model.fireTableDataChanged();
    }
    
    public void populateTable(String data1[], String data2[])
    {
        model.setRowCount(0);
        
        for(int i = 0; i < data1.length; ++i)
        {
            String row[] = new String[2];
            row[0] = data1[i];
            row[1] = data2[i];
            
            model.addRow(row);
        }
        
        table.setModel(model);
        model.fireTableDataChanged();
    }
    
    public void setRowSorter(TableRowSorter<TableModel> tableSorter)
    {
        table.setRowSorter(tableSorter);
    }
    
    public JScrollPane getScrollPane()
    {
        return scrollPane;
    }
    
    public JTable getTable()
    {
        return table;
    }
    
    public DefaultTableModel getModel()
    {
        return model;
    }
}