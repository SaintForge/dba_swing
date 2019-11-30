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
	
	public String getProfieUser() {
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
	
	JButton okButton = new JButton("Create");
	JButton cancelButton = new JButton("Cancel"); 
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
	
	SettingsDialog(Frame owner, String title)
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
	
	public static SettingsData createDialog(JFrame parent)
	{
		SettingsDialog dialog = new SettingsDialog(parent, "New Environment");
		
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
}

class EnvironmentData
{
	SettingsData settings = new SettingsData();
	// ArrayList<T> data;
}

class DBForm
{
	EnvironmentData data;
	JPanel panel = new JPanel(new GridBagLayout());
	
	DBForm(SettingsData settings)
	{
		JPanel tableList = createNewTable("Table Name", "Description");
		JPanel tableInfo = createNewTable("Properties", "Value");
		JPanel fieldList = createNewTable("Field Name", "Description");
		JPanel fieldInfo = createNewTable("Properties", "Value");
		
		JTextField tableSearch = new JTextField();
		this.panel.add(tableSearch,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.01, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		this.panel.add(new JButton("Refresh"),  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 80), 0, 0));
		this.panel.add(new JButton("Settings"),  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		
		JTextField fieldSearch = new JTextField();
		this.panel.add(fieldSearch,  new GridBagConstraints(3, 0, 1, 1, 1.0, 0.01, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		this.panel.add(new JLabel(""),  new GridBagConstraints(5, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 140), 0, 0));
		this.panel.add(new JButton("Refresh"),  new GridBagConstraints(5, 0, 1, 1, 1.0, 0.01, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		
		this.panel.add(tableList,  new GridBagConstraints(0, 1, 3, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.panel.add(tableInfo,  new GridBagConstraints(0, 2, 3, 1, 1.0, 0.35, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.panel.add(fieldList,  new GridBagConstraints(3, 1, 3, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.panel.add(fieldInfo,  new GridBagConstraints(3, 2, 3, 1, 1.0, 0.35, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		this.panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	public JPanel createNewTable(String firstName, String secondName)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());
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
	
	public static DBForm createNewForm(SettingsData settings)
	{
		DBForm dbForm = new DBForm(settings);
		return dbForm;
	}
}

class PopUpDemo extends JPopupMenu {
    JMenuItem anItem;
    public PopUpDemo() {
        anItem = new JMenuItem("Click Me!");
        add(anItem);
    }
}

class MainFrame extends JFrame 
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
		
		tabs.addTab("vtbdevnew", null, DBForm.createNewForm(new SettingsData()).getPanel(), null);
		tabs.setMnemonicAt(0, KeyEvent.VK_1);
		
		tabs.addTab("vtbcr810bqa", null, DBForm.createNewForm(new SettingsData()).getPanel(), null);
		tabs.setMnemonicAt(1, KeyEvent.VK_2);
		
		this.tab_amount = 3;
		tabs.addTab(" + ", null, new JPanel(), null);
		
		tabs.addChangeListener(new ChangeListener()
							   {
							   public void stateChanged(ChangeEvent evt)
							   {
							   System.out.println("mouse input");
							   JTabbedPane tabbedPane = (JTabbedPane)evt.getSource();
							   
							   int prevIndex = active_tab;
							   int tabIndex = tabbedPane.getSelectedIndex();
							   System.out.println(tabIndex);
							   System.out.println(active_tab);
							   // Create a new Tab
							   if (tabIndex == (tab_amount - 1))
							   {
							   System.out.println("new tab");
							   SettingsData settings = openSettingsDialog();
							   
							   if (settings.name == null || settings.name.isEmpty()) {
							   tabbedPane.setSelectedIndex(active_tab);
							   System.out.println("no shit");
							   return;
							   }
							   
							   tabbedPane.remove(tabbedPane.getTabCount() - 1);
							   tab_amount++ ;
							   
							   tabbedPane.addTab(settings.name, null, DBForm.createNewForm(settings).getPanel(), "new env");
							   tabbedPane.addTab(" + ", null, new JPanel(), null);
							   tabbedPane.setSelectedIndex(tabs.getTabCount() - 2);
							   System.out.println("hello");
							   }
							   else 
							   {
							   System.out.println("hi");
							   active_tab = tabIndex;
							   }
							   }
							   });
		
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		JPanel tab_panel = new JPanel(new GridLayout(1, 1));
		tab_panel.add(tabs);
		
		this.getContentPane().add(tab_panel);
		
		this.setPreferredSize(new Dimension(1200, 768));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public SettingsData openSettingsDialog()
	{
		SettingsData result = SettingsDialog.createDialog(this);
		return (result);
	}
	
	// @Override
	// public void mouseClicked(MouseEvent e)
	// {
	// 	PopUpDemo menu = new PopUpDemo();
    //     menu.show(e.getComponent(), e.getX(), e.getY());
	
	// 	System.out.println("hello");
	
	// 	JTabbedPane tabbedPane = (JTabbedPane)e.getSource();
	
	// 	if (tabbedPane.getSelectedIndex() == (tab_amount - 1))
	// 	{
	// 		System.out.println("new tab");
	// 		SettingsData settings = openSettingsDialog();
	
	// 		if (settings.name == null || settings.name.isEmpty()) {
	// 			tabbedPane.setSelectedIndex(tabs.getTabCount() - 2);
	// 			return;
	// 		}
	
	// 		tabbedPane.remove(tabbedPane.getTabCount() - 1);
	// 		tab_amount++ ;
	
	// 		tabbedPane.addTab(settings.name, null, DBForm.createNewForm(settings).getPanel(), "new env");
	// 		tabbedPane.addTab(" + ", null, new JPanel(), null);
	// 		tabbedPane.setSelectedIndex(tabs.getTabCount() - 2);
	// 	}
	
	// }
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