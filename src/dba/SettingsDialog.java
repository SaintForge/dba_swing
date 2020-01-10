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
	
	private boolean okPressed = false;
	
	public void addElement(String labelName) 
	{
		JLabel label = new JLabel();
		label.setText(labelName);
		label.setFont(new Font("Courier New", Font.PLAIN, 12));
		
		panel.add(label);
		springLayout.putConstraint("West", label, 10, "West", panel);
		springLayout.putConstraint("North", label, 35, "North", prevComponent);
		
		Component comp = new JTextField();
		comp.setName(labelName.substring(0, labelName.length()));
		comp.setFont( new Font( "Courier New", Font.BOLD, 12));
		
		panel.add(comp);
		springLayout.putConstraint("West", comp, 130, "West", panel);
		springLayout.putConstraint("East", comp, 0, "East", cancelButton);
		springLayout.putConstraint("North", comp, 30, "North", prevComponent);
		comp.setPreferredSize(new Dimension(80, 25));
		
		prevComponent = comp;
	}
	
	SettingsDialog(JFrame owner, String title, Boolean isUpdate)
	{
		super(owner, title, true);
        
		springLayout = new SpringLayout();
		panel = new JPanel(springLayout);
		prevComponent = panel;
		
		int w = 300;
		int h = 260;
		setPreferredSize(new Dimension(w, h));
		
		addElement("Name:");
		addElement("SQL Server:");
		addElement("MTM Port:");
		addElement("Profile User:");
		addElement("Profile Password:");
		
		okButton.addActionListener(new ActionListener()
										{
										public void actionPerformed(ActionEvent arg0)
										{
										SettingsDialog.this.saveSettings();
										
										String name = settings.getName();
										if (name != null && !name.isEmpty()) {
											   SettingsDialog.this.setVisible(false);
											   okPressed = true;
										}
										}
										});
		
		panel.add(okButton);
		springLayout.putConstraint("South", okButton, -10, "South", panel);
		springLayout.putConstraint("East", okButton, -10, "West", cancelButton);
		
		cancelButton.addActionListener(new ActionListener()
											{
											public void actionPerformed(ActionEvent arg0)
											{
											SettingsDialog.this.setVisible(false);
											}
											});
		
		panel.add(cancelButton);
		springLayout.putConstraint("South", cancelButton, -10, "South", panel);
		springLayout.putConstraint("East", cancelButton, -10, "East", panel);
		
		Container pane = getContentPane();
		pane.add(panel);
		
		Rectangle rect = owner.getBounds();
		int x = rect.x + (rect.width - w) / 2;
		int y = rect.y + (rect.height - h) / 2;
		
		setLocation(x, y);
		//setResizable(false);
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
	
	public static boolean updateNewDialog(JFrame parent, SettingsData settings)
	{
		SettingsDialog dialog = new SettingsDialog(parent, settings.getName(), settings);
		
		dialog.pack();
		dialog.setVisible(true);
		
		settings = dialog.settings;
		
		return dialog.isOKPressed();
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
	
	public boolean isOKPressed()
	{
		return okPressed;
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