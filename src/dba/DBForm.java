package dba;

import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JPanel; 
import javax.swing.JTextField; 
import javax.swing.JScrollPane;
import javax.swing.JTable; 
import javax.swing.JButton; 
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableColumn;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.sql.SQLException;

class DBForm extends JPanel implements ActionListener, DocumentListener, MouseListener
{
	private JFrame frame;
	private EnvironmentData environmentData;
    
    private JTextField tableSearch;
    private TableRowSorter<TableModel> tableSorter;
    
    private JTextField fieldSearch;
    private TableRowSorter<TableModel> fieldSorter;
    
    private Table tableList;
    private TabbedTable tableInfo;
    private Table fieldList;
    private TabbedTable fieldInfo;
    
	private JButton refreshAllButton;
	private JButton settingsButton;
	private JButton refreshTableButton;
	
    DBForm(EnvironmentData environmentData, JFrame frame, String data1[], String data2[])
    {
        this(environmentData, frame);
        tableList.populateDataStringArray(data1, data2);
    }
	
	DBForm(EnvironmentData environmentData, JFrame frame)
	{
		setLayout(new GridBagLayout());
		this.frame = frame;
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
        
        // initializing basic data settings
		this.environmentData = environmentData;
		
        // initializing table list
		tableList = new Table("Table Name", "Description");
        tableList.getTable().addMouseListener(this);
        
        // initializing table info
        tableInfo = new TabbedTable();
        tableInfo.addTab("Table Properties", new Table("Properties", "Description"));
        tableInfo.addTab("Index", new SplitPane());
        tableInfo.addTab("Table Documentation", new TextArea());
        
        // initializing field list
        fieldList = new Table("Field Name", "Description");
        fieldList.getTable().addMouseListener(this);
        
        // initializing field info
        fieldInfo = new TabbedTable();
        fieldInfo.addTab("Column Properties", new Table("Properties", "Description"));
        fieldInfo.addTab("Column Documentation", new TextArea());
        
        // initializing refresh button "Update All"
        refreshAllButton = new JButton("Update All");
		refreshAllButton.addActionListener(this);
		
        // initializing settings button 
		settingsButton = new JButton("Settings");
        
        // initializing refresh table button
        refreshTableButton = new JButton("Update Table");
		refreshTableButton.addActionListener(this);
		
        // initializing texfield for table list
		tableSearch = new JTextField();
        tableSorter = new TableRowSorter<TableModel>(tableList.getModel());
        tableSearch.getDocument().addDocumentListener(this);
        tableList.setRowSorter(tableSorter);
		
		populateData(environmentData.getTableArray());
        
        // initializing texfield for field list
        fieldSearch = new JTextField();
        fieldSorter = new TableRowSorter<TableModel>(fieldList.getModel());
        fieldSearch.getDocument().addDocumentListener(this);
        fieldList.setRowSorter(fieldSorter);
        
        // initializing all objects's layouts
		add(tableSearch,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.01, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		add(refreshAllButton,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 80), 0, 0));
		add(settingsButton,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		
		add(fieldSearch,  new GridBagConstraints(3, 0, 1, 1, 1.0, 0.01, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		add(new JLabel(""),  new GridBagConstraints(5, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 140), 0, 0));
		add(refreshTableButton,  new GridBagConstraints(5, 0, 2, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		
		add(tableList,  new GridBagConstraints(0, 1, 3, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        
        add(tableInfo, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(fieldList,  new GridBagConstraints(3, 1, 3, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(fieldInfo,  new GridBagConstraints(3, 2, 3, 1, 1.0, 0.4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
        // setting border
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }
    
	public void populateData(ArrayList<TableInfo> tableArray)
	{
        environmentData.setTableArray(tableArray);
        tableList.populateDataTableArray(tableArray);
    }
    
    // event handlers
	@Override
		public void actionPerformed(ActionEvent event) 
	{
        if (event.getSource() == refreshAllButton) {
            
			{
				SettingsData settings = environmentData.getSettings();
				
				int rowIndex = tableList.getTable().getSelectedRow();
				
				SQLService sql = new SQLService(settings.getSqlServer(), 
												settings.getMtmPort(), 
												settings.getProfileUser(),
												settings.getProfilePassword());
				sql.setIsDBA(true);
				
				
				ProgressDialog dialog = new ProgressDialog(frame, "Updating " + settings.getName());
				dialog.addLine("Connecting...            ");
				sql.addPropertyChangeListener(new PropertyChangeListener() {
												  
												  @Override
													  public void propertyChange(PropertyChangeEvent event) {
													  
													  String name = event.getPropertyName();
													  if (name.equals("progress"))
													  {
														  int progress = (int) event.getNewValue();
														  switch(progress)
														  {
															  case 1:
															  {
																  dialog.addLine("Done.\nLoading tables...        ");
															  } break;
															  case 2:
															  {
																  dialog.addLine("Done.\nLoading fields...        ");
															  } break;
															  case 3:
															  {
																  dialog.addLine("Done.\nLoading indexes...       ");
															  } break;
															  case 4:
															  {
																  dialog.addLine("Done.\nLoading documentation... ");
															  } break;
															  case 5:
															  {
																  dialog.addLine("Done.\nCompleted.");

															  } break;
															  case 6:
															  {
																  dialog.addLine("Failed.\nFailed to establish MTM connection. Connection timed out.");
															  } break;
														  }
														  
														  repaint();
													  }
													  else if (name.equals("state"))
													  {
														  SwingWorker.StateValue state = (SwingWorker.StateValue) event.getNewValue();
														  switch(state)
														  {
															  case DONE:
															  {
																  ArrayList<TableInfo> tableArray = sql.getTableInfoArrayBuffer();
																  
																  if (tableArray != null && tableArray.size() > 0)
																  {
																	  environmentData.setTableArray(tableArray);
																	  tableList.populateDataTableArray(tableArray);
																	  GlobalData.writeToFile();
																	  
																	  if (rowIndex != -1)
																	  {
																		  System.out.println("rawRowIndex: " + rowIndex);
																		  int rawRowIndex = tableList.getTable().convertRowIndexToModel(rowIndex);
																		  
																		  tableList.selectRow(rawRowIndex);
																	  }
																	  
																	  dialog.close();
																  }
																  else
																  {
																  }
																  
															  } break;
														  }
													  }
												  }
											  });
				sql.execute();
				
				dialog.pack();
				dialog.setVisible(true);
			}
		}
		else if (event.getSource() == refreshTableButton)
		{
			int rawRowIndex = tableList.getTable().getSelectedRow();
			if (rawRowIndex != -1)
			{
				int rowIndex = tableList.getTable().convertRowIndexToModel(rawRowIndex);
				
				try 
				{
					SettingsData settings = environmentData.getSettings();
					
					SQLService sql = new SQLService(settings.getSqlServer(), 
													settings.getMtmPort(), 
													settings.getProfileUser(),
													settings.getProfilePassword());
					sql.setIsDBA(false);
													
					
					sql.connect();
					if (sql.isConnected())
					{
						TableInfo tableInfoDataOld = environmentData.getTableArray().get(rowIndex);
						TableInfo tableInfoDataNew = sql.run_query(tableInfoDataOld.getFileName(),
																   tableInfoDataOld.getFields().size(),
																   tableInfoDataOld.getIndexes().size());
						environmentData.getTableArray().set(rowIndex, tableInfoDataNew);
						
						tableList.populateDataTableArray(environmentData.getTableArray());
						fieldList.populateDataFieldArray(tableInfoDataNew.getFields());
						tableList.selectRow(rawRowIndex);
						
						GlobalData.writeToFile();
					}
				}
				catch(ClassNotFoundException exc)
				{
					exc.printStackTrace();
				}
				catch(SQLException exc)
				{
					exc.printStackTrace();
				}
			}
		}
	}
    
    
    @Override
        public void insertUpdate(DocumentEvent event)
    {
        JTextField tmpSearch;
        if (event.getDocument() == tableSearch.getDocument()) 
        {
            String text = tableSearch.getText();
			text = text.replaceAll("[^a-zA-Z0-9]", "");
			
            if (text.trim().length() == 0)
            {
                tableSorter.setRowFilter(null);
            }
            else
            {
                tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            } 
        }
        else if (event.getDocument() == fieldSearch.getDocument())
        {
            String text = fieldSearch.getText();
			text = text.replaceAll("[^a-zA-Z0-9]", "");
			
            if (text.trim().length() == 0)
            {
                fieldSorter.setRowFilter(null);
            }
            else
            {
                fieldSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            } 
        }
    }
    
    @Override
        public void removeUpdate(DocumentEvent event)
    {
        insertUpdate(event);
    }
    
    @Override
        public void changedUpdate(DocumentEvent event)
    {
        
    }
    
    @Override
        public void mousePressed(MouseEvent event)
    {
        if (event.getSource() == tableList.getTable())
        {
            if (SwingUtilities.isLeftMouseButton(event))
            {
                int rowIndex = tableList.getTable().getSelectedRow();
                
                if (rowIndex != -1)
                {
					rowIndex = tableList.getTable().convertRowIndexToModel(rowIndex);
					
                    if (rowIndex < environmentData.getTableArray().size())
                    {
                        TableInfo tableInfoData = environmentData.getTableArray().get(rowIndex);
                        fieldList.populateDataFieldArray(tableInfoData.getFields());
						
                        JTabbedPane tableTabs = tableInfo.getTabs();
                        for (int tabIndex = 0; tabIndex < tableTabs.getTabCount(); ++tabIndex)
                        {
                            switch(tabIndex)
                            {
                                case 0: // Table Properties
                                {
                                    Table tableProps = (Table)tableTabs.getComponentAt(tabIndex);
                                    tableProps.populateDataTableInfo(tableInfoData);
                                    
                                } break;
                                
                                case 1: // Table Indexes
                                {
									SplitPane indexPane = (SplitPane)tableTabs.getComponentAt(tabIndex);
									indexPane.populateDataIndexArray(tableInfoData.getIndexes());
                                } break;
                                
                                case 2: // Table Documentation
                                {
									TextArea textPane = (TextArea)tableTabs.getComponentAt(tabIndex);
									textPane.populateDataString(tableInfoData.getFileDocumentation());
                                } break;
                            }
                        }
                    }
                }
            }
        }
        else if (event.getSource() == fieldList.getTable())
        {
			if (SwingUtilities.isLeftMouseButton(event))
			{
				int rowIndex = tableList.getTable().getSelectedRow();
				
				if (rowIndex != -1)
				{
					rowIndex = tableList.getTable().convertRowIndexToModel(rowIndex);
					
					if (rowIndex < environmentData.getTableArray().size())
					{
						TableInfo tableInfoData = environmentData.getTableArray().get(rowIndex);
						
						int rowFieldIndex = fieldList.getTable().getSelectedRow();
						
						if (rowFieldIndex != -1)
						{
							rowIndex = fieldList.getTable().convertRowIndexToModel(rowFieldIndex);
							
							if (rowFieldIndex < tableInfoData.getFields().size())
							{
							JTabbedPane fieldTabs = fieldInfo.getTabs();
							for (int tabIndex = 0; tabIndex < fieldTabs.getTabCount(); ++tabIndex)
							{
								switch(tabIndex)
								{
									case 0: // Column Properties
									{
										Table fieldProps = (Table)fieldTabs.getComponentAt(tabIndex);
										fieldProps.populateDataFieldInfo(tableInfoData.getFields().get(rowFieldIndex));
											
									} break;
									case 1: // Column Documentation
									{
										
									} break;
								}
								}
							}
						}
					}
				}
			}
        }
    }
    
    @Override
		public void mouseExited(MouseEvent event) {}
	@Override
		public void mouseEntered(MouseEvent event) {}
	@Override
		public void mouseClicked(MouseEvent event) {}
	@Override
		public void mouseReleased(MouseEvent event) {}
	
	private class MyDispatcher implements KeyEventDispatcher {
        @Override
			public boolean dispatchKeyEvent(KeyEvent event) {	
			
			if (event.getID() == KeyEvent.KEY_PRESSED) 
			{
				if (event.isControlDown())
				{
					if (event.getKeyCode() == KeyEvent.VK_1)
					{
						tableSearch.requestFocus();
					}
					else if(event.getKeyCode() == KeyEvent.VK_2)
					{
						fieldSearch.requestFocus();
					}
				}
            } 
			
			return false;
        }
    }
	
	public JButton getSettingsButton()
	{
		return settingsButton;
	}
}