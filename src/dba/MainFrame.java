package dba;

import java.util.ArrayList;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.WindowEvent;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.GridLayout;

class MainFrame extends JFrame implements ChangeListener
{
    private static final long serialVersionUID = 1L;
    
    final JTabbedPane tabs;
    
    MainFrame()
    {
        this.setSize(1200, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        
        SettingsData settings = new SettingsData();
        settings.setName("vtbdevnew");
        settings.setSqlServer("172.29.7.82");
        settings.setMtmPort("20101");
        settings.setProfileUser("1");
        settings.setProfilePassword("xxx");
        
        DBForm form = new DBForm(settings, this);
        //form.populateData(tableArray);
        
        tabs.addTab(settings.getName(), null, form.getPanel(), null);
        tabs.setMnemonicAt(0, KeyEvent.VK_1);
        
        tabs.setTabComponentAt(0, new ButtonTabComponent(tabs));
        
        settings.setName("vtbCR810bqa");
        settings.setSqlServer("172.29.7.82");
        settings.setMtmPort("19275");
        settings.setProfileUser("1");
        settings.setProfilePassword("xxx");
        
        tabs.addTab(settings.getName(), null, DBForm.createNewForm(settings, this).getPanel(), null);
        tabs.setMnemonicAt(1, KeyEvent.VK_2);
        tabs.setTabComponentAt(1, new ButtonTabComponent(tabs));
        
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
        
        if (tabs.getTabCount() == 1) 
        {
            createNewTab(tabs);
        }
    }
    
    private void createNewTab(JTabbedPane pane)
    {
        System.out.println("MainFrame.createNewTab");
        
        SettingsData settings = openSettingsDialog();
        
        if (settings.getName() == null || settings.getName().isEmpty()) {
            if (pane.getTabCount() == 1) {
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
            
            pane.setSelectedIndex(pane.getSelectedIndex()-1);
            return;
        }
        
        pane.addTab(settings.getName(), null, DBForm.createNewForm(settings, this).getPanel(), "new env");
        
        
        pane.addTab(" + ", null, new JPanel(), null);
        pane.setSelectedIndex(pane.getTabCount() - 2);
        pane.remove(pane.getTabCount() - 3);
        
        pane.setTabComponentAt(pane.getSelectedIndex(), new ButtonTabComponent(pane));
    }
    
    public void stateChanged(ChangeEvent event) 
    {
        System.out.println("MainFrame.stateChanged");
        JTabbedPane tabbedPane = (JTabbedPane)event.getSource();
        
        // Create a new Tab
        if (tabbedPane.getSelectedIndex() == (tabbedPane.getTabCount() - 1))
        {
            createNewTab(tabbedPane);
        }
    }
    
    public SettingsData openSettingsDialog()
    {
        SettingsData result = SettingsDialog.createNewDialog(this);
        return (result);
    }
}