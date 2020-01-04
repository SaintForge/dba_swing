package dba;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.util.ArrayList;

class SplitPane extends JPanel implements MouseListener
{
    Table selectTable;
    Table valueTable;
    
    JSplitPane splitPane;
	
	ArrayList<IndexInfo> indexes;
    
    SplitPane() 
    {
        super(new BorderLayout());
        
        selectTable = new Table("Index Name");
        selectTable.getTable().addMouseListener(this);
        
        valueTable = new Table("Property", "Value");
        valueTable.getTable().addMouseListener(this);
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, selectTable.getScrollPane(), valueTable.getScrollPane());
        splitPane.setDividerLocation(150);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    public void mouseClicked(MouseEvent event) 
    {
        if (event.getSource() == selectTable.getTable()) 
        {
            int rowIndex = selectTable.getTable().getSelectedRow();
			if (rowIndex >= 0 && rowIndex < indexes.size())
			{
				String propertyNames[] =
				{
					"Index Description",
					"Order By",
					"Supertype File Name",
					"Convert to Uppercase",
					"Global Name"
				};
				
				String propertyValues[] = 
				{
					indexes.get(rowIndex).getIndexDescription(),
					indexes.get(rowIndex).getOrderBy(),
					indexes.get(rowIndex).getSuperTypeFileName(),
					indexes.get(rowIndex).getConvertToUpperCase(),
					indexes.get(rowIndex).getGlobalName()
				};
				
				valueTable.populateDataStringArray(propertyNames, propertyValues);
			}
        }
    }
	
	public void populateDataIndexArray(ArrayList<IndexInfo> indexes)
	{
		this.indexes = indexes;
		
		String[] indexNames = new String[indexes.size()];
		for (int i = 0; i < indexNames.length; i += 1){
			indexNames[i] = indexes.get(i).getIndexName();
		}
		
		selectTable.populateDataStringArray(indexNames);
		valueTable.populateDataStringArray(new String[]{});
	}
	
    public void mouseExited(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mousePressed(MouseEvent event) {}
	public void mouseReleased(MouseEvent event) {} 
}