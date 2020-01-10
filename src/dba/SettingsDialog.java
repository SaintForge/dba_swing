package dba;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JFrame; 
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

class SettingsDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel"); 
    private JButton deleteButton = new JButton("Delete");
	private SettingsData settings = new SettingsData();
	
	private JPanel panel;
	private SpringLayout springLayout;
	private Component prevComponent;
	
	public void addElement(String labelName) 
	{
		JLabel label = new JLabel();
		label.setText(labelName);
		
		this.panel.add(label);
		this.springLayout.putConstraint("West", label, 10, "West", this.panel);
		this.springLayout.putConstraint("North", label, 30, "North", this.prevComponent);
		
		Component comp = new JTextField();
		comp.setName(labelName.substring(0, labelName.length()));
		comp.setFont( new Font( "Courier New", Font.BOLD, 12));
		
		this.panel.add(comp);
		this.springLayout.putConstraint("West", comp, 120, "West", this.panel);
		this.springLayout.putConstraint("East", comp, 0, "East", this.cancelButton);
		this.springLayout.putConstraint("North", comp, 30, "North", this.prevComponent);
		comp.setPreferredSize(new Dimension(80, 25));
		
		this.prevComponent = comp;
	}
	
	SettingsDialog(JFrame owner, String title, Boolean isUpdate)
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
	
	SettingsDialog(JFrame owner, String title, SettingsData settings)
	{
		this(owner,title,true);
		
		this.settings = settings;
		
		setProperty("Name:", settings.getName());
		setProperty("SQL Server:", settings.getSqlServer());
		setProperty("MTM Port:", settings.getMtmPort());
		setProperty("Profile User:", settings.getProfileUser());
		setProperty("Profile Password:", settings.getProfilePassword());
	}
	
	public static SettingsData createNewDialog(JFrame parent)
	{
		SettingsDialog dialog = new SettingsDialog(parent, "New Environment", false);
		
		SettingsDialog.setProperty(dialog.getPanel(), "SQL Server:", "172.29.7.82");
		SettingsDialog.setProperty(dialog.getPanel(), "Profile User:", "1");
		SettingsDialog.setProperty(dialog.getPanel(), "Profile Password:", "xxx");
		
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
		settings.setName(getProperty("Name:"));
		settings.setSqlServer(getProperty("SQL Server:"));
		settings.setMtmPort(getProperty("MTM Port:"));
		settings.setProfileUser(getProperty("Profile User:"));
		settings.setProfilePassword(getProperty("Profile Password:"));
	}
	
	public JPanel getPanel()
	{
		return panel;
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
	
	public static String setProperty(JPanel panel, String name, String property)
	{
		for (int i = 0; i < panel.getComponents().length; i++)
		{
			Component child = panel.getComponent(i);
			if (((child instanceof JTextField)) && (child.getName() != null) && (child.getName().equalsIgnoreCase(name))) 
			{
				
				((JTextField)child).setText(property);
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