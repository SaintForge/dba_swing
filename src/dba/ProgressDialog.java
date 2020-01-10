package dba;

import java.lang.StringBuilder;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JFrame; 
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;  
import javax.swing.text.BadLocationException;  
import javax.swing.text.Document;  
import javax.swing.text.SimpleAttributeSet;  
import javax.swing.text.StyleConstants;  
import javax.swing.text.StyledDocument;  

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;


class ProgressDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	
	//private JPanel panel;
	private StringBuilder progressText = new StringBuilder();
	private JTextPane textPane;
	
	ProgressDialog(JFrame owner, String title)
	{
		super(owner, title, true);
		
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		
		textPane = new JTextPane();
		textPane.setFont(new Font("Courier New", Font.PLAIN, 12));
		textPane.setEditable(false);
		
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_LEFT);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		pane.add(textPane, BorderLayout.CENTER);
		
		Rectangle rect = owner.getBounds();
		int w = 250;
		int h = 150;
		int x = rect.x + (rect.width - w) / 2;
		int y = rect.y + (rect.height - h) / 2;
		
		setPreferredSize(new Dimension(w, h));
		setLocation(x, y);
		setResizable(true);
	}
	
	public void addLine(String message)
	{
		progressText.append(message);
		textPane.setText(progressText.toString());
	}
	
	public void close()
	{
		setVisible(false);
	}
	
}