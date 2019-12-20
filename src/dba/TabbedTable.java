package dba;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

import java.awt.BorderLayout;

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