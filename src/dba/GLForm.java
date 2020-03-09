package dba;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

class GLForm extends JPanel implements DocumentListener, MouseListener
{
	JTextArea glText;
	
	Table tableInfo;
	Table fieldInfo;
	
	GLForm()
	{
		setLayout(new BorderLayout());
		
		glText = new JTextArea();
		glText.setFont( new Font( "Courier New", Font.PLAIN, 14));
		glText.getDocument().addDocumentListener(this);
		glText.addMouseListener(this);
		
		JScrollPane glTextScroll = new JScrollPane(glText);
		
		tableInfo = new Table("Properties", "Description");
		tableInfo.setColumnsWidth(20, 80);
		tableInfo.populateDataTableInfo(new TableInfo());
		
		fieldInfo = new Table("Properties", "Description");
		fieldInfo.setColumnsWidth(20, 80);
		fieldInfo.populateDataFieldInfo(new FieldInfo());
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		tablePanel.add(tableInfo);
		tablePanel.add(fieldInfo);
		
		add(glTextScroll, BorderLayout.CENTER);
		add(tablePanel, BorderLayout.LINE_END);
	}
	
	@Override
		public void insertUpdate(DocumentEvent event)
	{
		System.out.println("insertUpdate");
		
		if (event.getDocument() == glText.getDocument())
		{
			StringBuilder glData = new StringBuilder(glText.getText());
			//System.out.println(glData.toString());
			
			parseGLData(glData);
		}
	}
	
	@Override 
		public void removeUpdate(DocumentEvent event)
	{
		System.out.println("removeUpdate");
	}
	
	@Override
		public void changedUpdate(DocumentEvent event)
	{
		System.out.println("changedUpdate");
	}
	
	@Override 
		public void mousePressed(MouseEvent event)
	{
		System.out.println("mousePressed");
		
		if (event.getSource() == glText)
		{
			System.out.println("sas");
			
			int pos = glText.getCaretPosition();
			
			Highlighter highlighter = glText.getHighlighter();
			
			HighlightPainter painter = 
				new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
			try
			{
				if (glText.getText().length() > 0)
				{
					pos = glText.getCaretPosition();
					
					int range[] = getHightlightRange(glText.getText(), pos);
					
					highlighter.removeAllHighlights();
					highlighter.addHighlight(range[0], range[1], painter);
				}
			}
			catch(BadLocationException exc)
			{
				
			}
		}
	}
	
	private int[] getHightlightRange(String data, int pos)
	{
		int range[] = new int[2];
		
		for (int i = pos - 1; i >= 0; i--)
		{
			boolean exit = false;
			char nextChar = data.charAt(i);
			switch(nextChar)
			{
				case '^':
				case ',':
				case '(':
				case ')':
				case '|':
				{
					range[0] = i + 1;
					exit = true;
				} break;
			}
			
			if (exit) break;
		}
		
			for (int i = pos; i < data.length(); ++i)
			{
				boolean exit = false;
			char nextChar = data.charAt(i);
				switch(nextChar)
				{
					case '^':
					case ',':
					case '(':
				case ')':
				{
					range[1] = i;
					exit = true;
				} break;
					case '|':
				{
						range[1] = i;
					
						exit = true;
					} break;
				}
				
				if (exit) break;
			}
		
		return range;
	}
	
	@Override
		public void mouseExited(MouseEvent event) {}
	@Override
		public void mouseEntered(MouseEvent event) {}
	@Override
		public void mouseClicked(MouseEvent event) {}
	@Override
		public void mouseReleased(MouseEvent event) {}
	
	
	// Grammar:
	// Global Node:
	//        "^" Text_Token Keys_Node 
	//        Piped_Data
	
	// Text_Token:
	//        String
	
	// Keys_Node:
	//        "(" Key_Sequence ")"
	
	// Key_Sequence:
	//        Text_Token
	//        ","
	
	// Piped_Data:
	//        Text_Token
	//        "|"
	
	class GlobalToken
	{
		public String globalName;
		public KeysNode keyNode;
		public PipedData pipedData;
		
		GlobalToken()
		{
			keyNode    = new KeysNode();
			pipedData  = new PipedData();
			globalName = new String();
		}
		
		public int getGlobalToken(String data, int pos)
		{
			int result = pos;
			boolean foundToken = false;
			
			for (int i = pos; i < data.length(); ++i)
			{
				 char nextChar = data.charAt(i);
				if (nextChar == '^')
				{
					if (foundToken) 
					{
						result = i - 1;
						break;
					}
					
					foundToken = true;
					continue;
				}
				
				if (foundToken)
				{
						result = i;
					
					if (i + 1 == data.length())
					{
						result = i;
						break;
					}
					
					if (nextChar != '\n')
					{
						globalName += nextChar;
					}
				}
			}
			
			return result;
		}
	}
	
	class KeysNode
	{
		ArrayList<String> keyList;
		
		//public KeysNode getKeysNode()
	}
	
	class PipedData
	{
		ArrayList<String> data;
	}
	
	
	private void parseGLData(StringBuilder glData)
	{
		String glDataString = glData.toString();
		System.out.println(glDataString.length());
		
		for (int i = 0; i < glDataString.length(); ++i)
		{
			GlobalToken token = new GlobalToken();
			
			int nextPos = token.getGlobalToken(glDataString, i);
			i = nextPos;
			
			System.out.println(token.globalName);
		}
	}
}