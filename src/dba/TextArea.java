package dba;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Font;

class TextArea extends JPanel
{
    JTextArea textArea;
    JScrollPane scrollPane;
    
    TextArea()
    {
        super(new BorderLayout());
        
        textArea = new JTextArea();
        textArea.setEditable(false);
		textArea.setFont( new Font( "Courier New", Font.PLAIN, 12));
        
        scrollPane = new JScrollPane(textArea);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void populateDataString(String data)
    {
        textArea.setText(data);
    }
}