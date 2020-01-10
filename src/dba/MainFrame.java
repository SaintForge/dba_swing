package dba;

import java.util.HashMap;
import java.util.ArrayList;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.WindowEvent;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.GridLayout;
import java.awt.Font;

class MainFrame extends JFrame implements ChangeListener
{
    private static final long serialVersionUID = 1L;
    
    final JTabbedPane tabs;
    
    MainFrame()
    {
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Profile DBA");
        
        tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.setFont( new Font( "Courier New", Font.BOLD, 12));
        
		GlobalData.readFromFile();
		  ArrayList<EnvironmentData> dataList = GlobalData.getInstance().data;
		
		for (int i = 0; i < dataList.size(); ++i) 
		{
			EnvironmentData data = dataList.get(i);
			
			DBForm form = new DBForm(data, this);
			tabs.addTab(data.getSettings().getName(), null, form, null);
			tabs.setTabComponentAt(i, new ButtonTabComponent(tabs));
		}
		
        tabs.addTab("+", null, new JPanel(), null);
        tabs.addChangeListener(this);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        JPanel tab_panel = new JPanel(new GridLayout(1, 1));
        tab_panel.add(tabs);
        
        getContentPane().add(tab_panel);
        setPreferredSize(new Dimension(1200, 768));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        if (tabs.getTabCount() == 1) 
        {
            createNewTab();
        }
    }
    
    private void createNewTab()
    {
        SettingsData settings = openSettingsDialog();
        
        if (settings.getName() == null || settings.getName().isEmpty()) {
            if (tabs.getTabCount() == 1) {
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
            
            tabs.setSelectedIndex(tabs.getSelectedIndex()-1);
            return;
        }
		else
		{
			ArrayList<EnvironmentData> dataList = GlobalData.getInstance().data;
			for (int i = 0; i < dataList.size(); i += 1)
			{
				SettingsData settingsData = dataList.get(i).getSettings();
				if (settingsData.getName().equals(settings.getName()))
				{
					tabs.setSelectedIndex(tabs.getSelectedIndex()-1);
					return;
				}
			}
		}
		
		EnvironmentData environmenData = new EnvironmentData(settings);
        tabs.addTab(settings.getName(), null, new DBForm(environmenData, this), null);
		
		GlobalData.getInstance().data.add(environmenData);
		GlobalData.writeToFile();
        
        tabs.addTab("+", null, new JPanel(), null);
        tabs.setSelectedIndex(tabs.getTabCount() - 2);
        tabs.remove(tabs.getTabCount() - 3);
		//tabs.setFont( new Font( "Courier New", Font.PLAIN, 12));
        
        tabs.setTabComponentAt(tabs.getSelectedIndex(), new ButtonTabComponent(tabs));
    }
    
    public void stateChanged(ChangeEvent event) 
    {
        JTabbedPane tabbedPane = (JTabbedPane)event.getSource();
        
        // Create a new Tab
        if (tabbedPane.getSelectedIndex() == (tabbedPane.getTabCount() - 1))
        {
            createNewTab();
        }
    }
    
    public SettingsData openSettingsDialog()
    {
        SettingsData result = SettingsDialog.createNewDialog(this);
        return (result);
    }
}