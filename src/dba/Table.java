package dba;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableColumn;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


class Table extends JPanel implements KeyListener
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
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);
		table.addKeyListener(this);
        
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
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(true);
		//table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.addKeyListener(this);
        
        scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
		TableColumn column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(10);
		
		column = table.getColumnModel().getColumn(1);
		column.setPreferredWidth(200);
    }
	
	public void setColumnsWidth(int firstColumn, int secondColumn)
	{
		TableColumn column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(firstColumn);
		
		column = table.getColumnModel().getColumn(1);
		column.setPreferredWidth(secondColumn);
	}
	
	public void selectRow(int rowIndex)
	{
		table.setRowSelectionInterval(rowIndex, rowIndex);
	}
    
    // populate data functions
    public void populateDataTableInfo(TableInfo tableInfo)
    {
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
        
        populateDataStringArray(tableProperties, tableData);
    }
    
    public void populateDataTableArray(ArrayList<TableInfo> tableArray)
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
	
	public void populateDataFieldInfo(FieldInfo fieldInfo)
	{
		String fieldProperties[] =
		{
			"File Name",
			"Data Item Name",
			"Description",
			"Required indicator",
			"Maximum Field Length",
			"Decimal Precision",
			"Profile Data Type",
			"Computed expression",
			"Look-Up Table Name",
			"Field Position",
			"Subscript key"
		};
		
		String fieldValues[] =
		{
			fieldInfo.getFileName(),
			fieldInfo.getFieldName(),
			fieldInfo.getDescription(),
			fieldInfo.getRequiredIndicator(),
			fieldInfo.getFieldLength(),
			fieldInfo.getDecimalPrecision(),
			fieldInfo.getDataType(),
			fieldInfo.getComputedExpression(),
			fieldInfo.getLookUpTable(),
			fieldInfo.getFieldPosition(),
			fieldInfo.getSubscriptKey()
		};
		
		populateDataStringArray(fieldProperties, fieldValues);
	}
	
    public void populateDataFieldArray(ArrayList<FieldInfo> fieldArray)
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
    
    public void populateDataStringArray(String data[])
    {
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
    
    public void populateDataStringArray(String data1[], String data2[])
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
	
	
	@Override
        public void keyReleased(KeyEvent event)
	{
		if (event.isControlDown())	
		{
			if (event.getKeyCode() == KeyEvent.VK_C)
			{
				if (event.getSource() == table)
				{
					copy_cell_to_clipboard(table);
				}
			}
		}
	}
	
	void copy_cell_to_clipboard(JTable table)
	{
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
		
		if (row != -1 && col != -1)
		{
			Object value = table.getValueAt(row, col);
			value = value != null ? value : "";
			
			StringSelection string_selection = new StringSelection(value.toString());
			
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(string_selection, string_selection);
		}
	}
	
	@Override
        public void keyPressed(KeyEvent event) {}
	
	@Override
        public void keyTyped(KeyEvent event) {}
	
    
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