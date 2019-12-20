package dba;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;

class SplitPane extends JPanel implements MouseListener
{
    Table selectTable;
    Table valueTable;
    
    JSplitPane splitPane;
    
    String exList[] = {"dog", "cat", "mouse"};
    
    SplitPane() 
    {
        super(new BorderLayout());
        
        selectTable = new Table("Index Name");
        selectTable.getTable().addMouseListener(this);
        
        valueTable = new Table("Property", "Value");
        valueTable.getTable().addMouseListener(this);
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, selectTable.getScrollPane(), valueTable.getScrollPane());
        splitPane.setDividerLocation(150);
        
        selectTable.populateTable(exList);
        add(splitPane, BorderLayout.CENTER);
    }
    
    public void mouseClicked(MouseEvent event) 
    {
        if (event.getSource() == selectTable.getTable()) 
        {
            int rowIndex = selectTable.getTable().getSelectedRow();
            if (rowIndex == 0) 
            {
                String data1[] = {"name", "size", "color"};
                String data2[] = {"Bucha", "big", "brown"};
                valueTable.populateTable(data1, data2);
            }
            else if (rowIndex == 1)
            {
                String data1[] = {"name", "size", "color"};
                String data2[] = {"Bonya", "middle", "black"};
                valueTable.populateTable(data1, data2);
            }
            else if (rowIndex == 2)
            {
                String data1[] = {"name", "size", "color"};
                String data2[] = {"Jerry", "small", "grey"};
                valueTable.populateTable(data1, data2);
            }
        }
        else if (event.getSource() == valueTable.getTable()) 
        {
            System.out.println("table2");
        }
    }
    
    public void mouseExited(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mousePressed(MouseEvent event) {}
	public void mouseReleased(MouseEvent event) {} 
}