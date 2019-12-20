package dba;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

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