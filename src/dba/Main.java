package dba;

// import java.sql.*;
import sanchez.jdbc.driver.ScDriver;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.sql.*;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.SpringLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.*;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.JFrame; 
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.border.BevelBorder;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JPasswordField;
import javax.swing.SpringLayout;
import javax.swing.event.*;

import java.util.Date;
import java.util.Vector;

import java.io.*;

class SettingsData
{
	String name;
	String sqlServer;
	String mtmPort;
	String profileUser;
	String profilePassword;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSqlServer() {
		return sqlServer;
	}
	public void setSqlServer(String sqlServer) {
		this.sqlServer = sqlServer;
	}
	
	public String getMtmPort() {
		return this.mtmPort;
	}
	public void setMtmPort(String mtmPort) {
		this.mtmPort = mtmPort;
	}
	
	public String getProfileUser() {
		return profileUser;
	}
	public void setProfileUser(String profileUser) {
		this.profileUser = profileUser;
	}
	
	public String getProfilePassword() {
		return profilePassword;
	}
	public void setProfilePassword(String profilePassword) {
		this.profilePassword = profilePassword;
	}
}

class SettingsDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel"); 
    JButton deleteButton = new JButton("Delete");
	SettingsData settings = new SettingsData();
	
	JPanel panel;
	SpringLayout springLayout;
	Component prevComponent;
	
	public void addElement(String labelName) 
	{
		JLabel label = new JLabel();
		label.setText(labelName);
		
		this.panel.add(label);
		this.springLayout.putConstraint("West", label, 10, "West", this.panel);
		this.springLayout.putConstraint("North", label, 30, "North", this.prevComponent);
		
		Component comp = new JTextField();
		comp.setName(labelName.substring(0, labelName.length()));
		
		this.panel.add(comp);
		this.springLayout.putConstraint("West", comp, 120, "West", this.panel);
		this.springLayout.putConstraint("East", comp, 0, "East", this.cancelButton);
		this.springLayout.putConstraint("North", comp, 30, "North", this.prevComponent);
		comp.setPreferredSize(new Dimension(80, 25));
		
		this.prevComponent = comp;
	}
	
	SettingsDialog(Frame owner, String title, Boolean isUpdate)
	{
		super(owner, title, true);
        
		this.springLayout = new SpringLayout();
		this.panel = new JPanel(this.springLayout);
		this.prevComponent = this.panel;
		
		int w = 300;
		int h = 250;
		setPreferredSize(new Dimension(w, h));
		
		addElement("Name:");
		addElement("SQL Server:");
		addElement("MTM Port:");
		addElement("Profile User:");
		addElement("Profile Password:");
		
		this.okButton.addActionListener(new ActionListener()
										{
										public void actionPerformed(ActionEvent arg0)
										{
										SettingsDialog.this.saveSettings();
										
										String name = settings.getName();
										if (name != null && !name.isEmpty()) {
										System.out.println("not null");
										System.out.println(settings.getName());
										SettingsDialog.this.setVisible(false);
										}
										}
										});
        
        if (isUpdate)
        {
            panel.add(deleteButton);
            springLayout.putConstraint("South", this.deleteButton, -10, "South", this.panel);
            springLayout.putConstraint("East", this.deleteButton, -10, "West", this.okButton);
        }
		
		this.panel.add(this.okButton);
		this.springLayout.putConstraint("South", this.okButton, -10, "South", this.panel);
		this.springLayout.putConstraint("East", this.okButton, -10, "West", this.cancelButton);
		
		this.cancelButton.addActionListener(new ActionListener()
											{
											public void actionPerformed(ActionEvent arg0)
											{
											SettingsDialog.this.setVisible(false);
											}
											});
		
		this.panel.add(this.cancelButton);
		this.springLayout.putConstraint("South", this.cancelButton, -10, "South", this.panel);
		this.springLayout.putConstraint("East", this.cancelButton, -10, "East", this.panel);
		
		Container pane = getContentPane();
		pane.add(panel);
		
		Rectangle rect = owner.getBounds();
		int x = rect.x + (rect.width - w) / 2;
		int y = rect.y + (rect.height - h) / 2;
		
		setLocation(x, y);
		setResizable(false);
	}
	
	SettingsDialog(Frame owner, String title, SettingsData settings)
	{
		this(owner,title,true);
		
		this.settings = settings;
		
		setProperty("Name:", settings.getName());
		setProperty("SQL Server:", settings.getSqlServer());
		setProperty("MTM Port:", settings.getMtmPort());
		setProperty("Profile User:", settings.getProfileUser());
		setProperty("Profile Password:", settings.getProfilePassword());
		
		System.out.println("settings SQL Server:" + getProperty("SQL Server:"));
	}
	
	public static SettingsData createNewDialog(JFrame parent)
	{
		SettingsDialog dialog = new SettingsDialog(parent, "New Environment", false);
		
		dialog.pack();
		dialog.setVisible(true);
		
		return dialog.settings;
	}
	
	public static SettingsData updateNewDialog(JFrame parent, SettingsData settings)
	{
		SettingsDialog dialog = new SettingsDialog(parent, settings.getName(), settings);
		
		dialog.pack();
		dialog.setVisible(true);
		
		return dialog.settings;
	}
	
	public void saveSettings()
	{
		this.settings.setName(getProperty("Name:"));
		this.settings.setSqlServer(getProperty("SQL Server:"));
		this.settings.setMtmPort(getProperty("MTM Port:"));
		this.settings.setProfileUser(getProperty("Profile User:"));
		this.settings.setProfilePassword(getProperty("Profile Password:"));
	}
	
	public String getProperty(String name)
	{
		for (int i = 0; i < this.panel.getComponents().length; i++)
		{
			Component child = this.panel.getComponent(i);
			if (((child instanceof JTextField)) && (child.getName() != null) && (child.getName().equalsIgnoreCase(name))) 
			{
				return ((JTextField)child).getText();
			}
		}
		return "";
	}
	
	public String setProperty(String name, String property)
	{
		for (int i = 0; i < this.panel.getComponents().length; i++)
		{
			Component child = this.panel.getComponent(i);
			if (((child instanceof JTextField)) && (child.getName() != null) && (child.getName().equalsIgnoreCase(name))) 
			{
				
				((JTextField)child).setText(property);
			}
		}
		return "";
	}
}

class EnvironmentData
{
	SettingsData settings;
	// ArrayList<T> data;
	
	EnvironmentData(SettingsData settings) 
	{
		this.settings = new SettingsData();
		
		this.settings.setName(settings.getName());
		this.settings.setSqlServer(settings.getSqlServer());
		this.settings.setMtmPort(settings.getMtmPort());
		this.settings.setProfileUser(settings.getProfileUser());
		this.settings.setProfilePassword(settings.getProfilePassword());
	}
	
	public SettingsData getSettings() 
	{
		return settings;
	}
	
	public void setSettings(SettingsData settings) 
	{
		this.settings = settings;
	}
}

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
        //table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Courier New", Font.PLAIN, 14));
		table.setShowVerticalLines(true);
        
        scrollPane = new JScrollPane(table);
		
		add(scrollPane, BorderLayout.CENTER);
    }
    
    public void populateTable(String data[])
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
    
    public JScrollPane getScrollPane()
    {
        return scrollPane;
    }
    
    public JTable getTable()
    {
        return table;
    }
}

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

class TextArea extends JPanel
{
    JTextArea textArea;
    JScrollPane scrollPane;
    
    TextArea()
    {
        super(new BorderLayout());
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        populateData("This is data.");
        
        scrollPane = new JScrollPane(textArea);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void populateData(String data)
    {
        textArea.setText(data);
    }
}

class TabbedTable extends JPanel
{
    JTabbedPane tabs;
    
    TabbedTable()
    {
        super(new BorderLayout());
        
        tabs = new JTabbedPane(JTabbedPane.TOP);
        
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        add(tabs, BorderLayout.CENTER);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }
    
    public void addTab(String name, JPanel tab) 
    {
        tabs.addTab(name, null, tab, null);
    }
}

class DBForm implements ActionListener
{
	JFrame frame;
	EnvironmentData data;
    
	JPanel panel = new JPanel(new GridBagLayout());
    Table tableList;
    TabbedTable tableInfo;
    Table fieldList;
    //Table fieldInfo;
    TabbedTable fieldInfo;
    
	JButton refreshAllButton;
	JButton settingsButton;
	JButton refreshTableButton;
    
	DBForm(SettingsData settings, JFrame frame)
	{
		this.frame = frame;
        
		data = new EnvironmentData(settings);
        
		tableList = new Table("Table Name", "Description");
        tableInfo = new TabbedTable();
        tableInfo.addTab("Table Properties", new Table("Properties", "Description"));
        tableInfo.addTab("Index", new SplitPane());
        tableInfo.addTab("Table Documentation", new TextArea());
        
        fieldList = new Table("Field Name", "Description");
        fieldInfo = new TabbedTable();
        fieldInfo.addTab("Column Properties", new Table("Properties", "Description"));
        fieldInfo.addTab("Column Documentation", new TextArea());
        
        refreshAllButton = new JButton("Update All");
		refreshAllButton.addActionListener(this);
		
		settingsButton = new JButton("Settings");
		settingsButton.addActionListener(this);
        
        refreshTableButton = new JButton("Update Table");
		refreshTableButton.addActionListener(this);
		
		JTextField tableSearch = new JTextField();
		this.panel.add(tableSearch,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.01, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		this.panel.add(refreshAllButton,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 80), 0, 0));
		this.panel.add(settingsButton,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		
		JTextField fieldSearch = new JTextField();
		this.panel.add(fieldSearch,  new GridBagConstraints(3, 0, 1, 1, 1.0, 0.01, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		this.panel.add(new JLabel(""),  new GridBagConstraints(5, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 140), 0, 0));
		this.panel.add(refreshTableButton,  new GridBagConstraints(5, 0, 2, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		
		this.panel.add(tableList,  new GridBagConstraints(0, 1, 3, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        
        this.panel.add(tableInfo, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.panel.add(fieldList,  new GridBagConstraints(3, 1, 3, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.panel.add(fieldInfo,  new GridBagConstraints(3, 2, 3, 1, 1.0, 0.4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		this.panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
	}
    
    DBForm(SettingsData settings, JFrame frame, String data1[], String data2[])
    {
        this(settings, frame);
        tableList.populateTable(data1, data2);
    }
	
	public JPanel createNewTable(String firstName, String secondName)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());
		
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn(firstName);
		tableModel.addColumn(secondName);
		
		JTable table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Courier New", Font.PLAIN, 14));
		table.setShowVerticalLines(true);
		JScrollPane scrollTable = new JScrollPane(table);
		
		panel.add(scrollTable, BorderLayout.CENTER);
		
		return panel;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	public static DBForm createNewForm(SettingsData settings, JFrame frame)
	{
		DBForm dbForm = new DBForm(settings, frame);
		return dbForm;
	}
	
	public void actionPerformed(ActionEvent event) 
	{
		if (event.getSource() == refreshAllButton) {
			System.out.println("refreshAllButton");
		}
		if (event.getSource() == settingsButton) 
		{
			System.out.println("settingsButton");
			
            // NOTE(Sierra): Look for backward event in order to tell MainFrame that we want to delete the whole tab
			SettingsData newSettings = SettingsDialog.updateNewDialog(frame, data.getSettings());
			data.setSettings(newSettings);
		}
	}
}

class MainFrame extends JFrame implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	
	int active_tab = 0;
	int tab_amount = 0;
	JTabbedPane tabs;
	
	MainFrame()
	{
		this.setSize(1200, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabs = new JTabbedPane(JTabbedPane.TOP);
		
		SettingsData settings = new SettingsData();
		settings.setName("vtbdevnew");
		settings.setSqlServer("172.29.7.82");
		settings.setMtmPort("20101");
		settings.setProfileUser("1");
		settings.setProfilePassword("xxx");
        
        String data1[] = {"table1", "table2", "table3"};
        String data2[] = {"desc1", "desc2", "desc3"};
        
        DBForm form = new DBForm(settings, this, data1, data2);
		
		tabs.addTab(settings.getName(), null, form.getPanel(), null);
		tabs.setMnemonicAt(0, KeyEvent.VK_1);
		
		settings.setName("vtbCR810bqa");
		settings.setSqlServer("172.29.7.82");
		settings.setMtmPort("19275");
		settings.setProfileUser("1");
		settings.setProfilePassword("xxx");
		
		tabs.addTab(settings.getName(), null, DBForm.createNewForm(settings, this).getPanel(), null);
		tabs.setMnemonicAt(1, KeyEvent.VK_2);
		
		this.tab_amount = 3;
		tabs.addTab(" + ", null, new JPanel(), null);
		
		tabs.addChangeListener(this);
		
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		JPanel tab_panel = new JPanel(new GridLayout(1, 1));
		tab_panel.add(tabs);
		
		this.getContentPane().add(tab_panel);
		
		this.setPreferredSize(new Dimension(1200, 768));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void stateChanged(ChangeEvent event) 
	{
		JTabbedPane tabbedPane = (JTabbedPane)event.getSource();
		
		int prevIndex = active_tab;
		int tabIndex = tabbedPane.getSelectedIndex();
		
		// Create a new Tab
		if (tabIndex == (tab_amount - 1))
		{
			System.out.println("new tab");
			SettingsData settings = openSettingsDialog();
			
			if (settings.name == null || settings.name.isEmpty()) {
				tabbedPane.setSelectedIndex(active_tab);
				return;
			}
			
			tabbedPane.remove(tabbedPane.getTabCount() - 1);
			tab_amount++ ;
			
			tabbedPane.addTab(settings.name, null, DBForm.createNewForm(settings, this).getPanel(), "new env");
			tabbedPane.addTab(" + ", null, new JPanel(), null);
			tabbedPane.setSelectedIndex(tabs.getTabCount() - 2);
			System.out.println("hello");
		}
		else 
		{
			active_tab = tabIndex;
		}
	}
	
	public SettingsData openSettingsDialog()
	{
		SettingsData result = SettingsDialog.createNewDialog(this);
		return (result);
	}
}

public class Main
{
	public static void run()
	{
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