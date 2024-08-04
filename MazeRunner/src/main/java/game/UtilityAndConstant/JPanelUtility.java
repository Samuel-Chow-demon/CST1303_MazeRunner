package game.UtilityAndConstant;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class JPanelUtility {
	
	// ---------------------- Panel Creator ---------------------- //
	// Improved ---- Setup a FlowLayoutPanel creator
	public static JPanel createFlowLayoutPanel(int alignOption, int hGap, int vGap, Color bg)
	{
		JPanel newPanel = new JPanel(new FlowLayout(alignOption, hGap, vGap));
		newPanel.setBackground(bg);
		return newPanel;
	}
	// Improved ---- Setup a BoxLayoutPanel creator
	public static JPanel createBoxLayoutPanel(int width, int height, int boxAxis, Color bkgrd)
	{
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new BoxLayout(newPanel, boxAxis));
		newPanel.setPreferredSize (new Dimension (width, height));
		newPanel.setBackground(bkgrd);
		return newPanel;
	}
	// Improved ---- Setup a GridLayoutPanel creator
	public static JPanel createGridLayoutPanel(Integer width, Integer height, GridLayout gridLayout, Border margin, Color bkgrd)
	{
		JPanel newPanel = new JPanel(gridLayout);
		
		int w = width == null ? newPanel.getPreferredSize().width : width;
		int h = height == null ? newPanel.getPreferredSize().height : height;
		
		if (margin != null)
		{
			newPanel.setBorder(margin);
		}
		newPanel.setPreferredSize(new Dimension(w, h));
		newPanel.setBackground(bkgrd);
		return newPanel;
	}
	public static JTextArea createTextArea(int row, int column, Boolean edit, Boolean wrap)
	{
		JTextArea msgArea = new JTextArea(row, column);
		msgArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		msgArea.setEditable(edit);
		msgArea.setLineWrap(wrap);
		msgArea.setWrapStyleWord(false);
		return msgArea;
	}
    
	// ---------------------- Panel Creator ---------------------- //
	// Improved ---- Setup a Text Label creator
	public static JLabel createTextLabel(String msg, Color foregd, Font font)
	{
		 JLabel newText = new JLabel(msg);
		 newText.setForeground(foregd);
		 newText.setFont(font);
		 return newText;
	}
	// Improved ---- Setup a Button creator
	public static JButton createButton(String title, int width, int height, Color border, int borderWidh, Color txtColor)
	{
		JButton newButton = new JButton(title);
		newButton.setPreferredSize(new Dimension(width, height));
	    newButton.setBorder(BorderFactory.createLineBorder(border, borderWidh));
	    newButton.setForeground(txtColor);
	    return newButton;
	}
	
	// ---------------------- Modifier ---------------------- //
	// Improved ---- Setup a Panel Size modifier method
	public static void setPanelSize(JPanel panel, Integer width, Integer height)
	{
		int w = width == null ? panel.getPreferredSize().width : width;
		int h = height == null ? panel.getPreferredSize().height : height;
		
		panel.setPreferredSize(new Dimension(w, h));
		panel.setMinimumSize(new Dimension(w, h));
		panel.setMaximumSize(new Dimension(w, h));
	}
	
	public static void setMargin(JComponent comp, int top, int down, int left, int right)
	{
		comp.setBorder(BorderFactory.createEmptyBorder(top, left, down, right));
	}
	
	public static void addTextAreaMessage(JTextArea area, String state, String action, String message)
	{
		Date currentTime = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//area.append(String.format("%-25s%-10s\t%-30s\t%-30s\n", "[" + timeFormat.format(currentTime) + "]", state, action, message));
		area.append(String.format("%-25s%-15s%-25s%-30s\n", "[" + timeFormat.format(currentTime) + "]", state, action, message));
	}
	public static void clearTextArea(JTextArea area)
	{
		area.setText("");
		area.setCaretPosition(0);	
	}
	
	public static Point drawSubWindow(Graphics2D g, Rectangle winRect, Color bkgrdColor,
									 int borderWidth, Color borderColor, int borderPadding)
	{
		g.setColor(bkgrdColor);
		g.fillRoundRect(winRect.x, winRect.y, winRect.width, winRect.height, 30, 30);
		
		int paddingSize = borderPadding;
		g.setColor(borderColor);
		g.setStroke(new BasicStroke(borderWidth));
		g.drawRoundRect(winRect.x + paddingSize, winRect.y + paddingSize, winRect.width - (paddingSize * 2), winRect.height - (paddingSize * 2), 25, 25);
		return new Point(winRect.x, winRect.y);
	}
	
	public static enum eALIGNMENT{
		eALIGN_CENTER, eALIGN_LEFT, eALIGN_RIGHT
	}
	
	public static Point drawHorizontalAlignStringToParent(Graphics2D g, String text, Color textColor, float fontSize, Rectangle parentRect, eALIGNMENT eAlign,
														int yPosToParent, int shadowOffset, Color shadowColor)
	{
		g.setFont(g.getFont().deriveFont(Font.PLAIN, fontSize));
		int textLengthInPixel = (int)g.getFontMetrics().getStringBounds(text, g).getWidth();
		int textXPos = (parentRect.width / 2 - textLengthInPixel / 2) + parentRect.x;
		
		switch (eAlign)
		{
			case eALIGN_CENTER:
				textXPos = (parentRect.width / 2 - textLengthInPixel / 2) + parentRect.x;
				break;
			case eALIGN_LEFT:
				textXPos = parentRect.x;
				break;
			case eALIGN_RIGHT:
				textXPos = parentRect.x + parentRect.width - textLengthInPixel;
				break;
		}
		
		int textYPos = (yPosToParent > 0) ? (yPosToParent + parentRect.y)  : (parentRect.height / 2 + parentRect.y);
		
		if (shadowOffset > 0)
		{
			g.setColor(shadowColor);
			g.drawString(text, textXPos + shadowOffset, textYPos + shadowOffset);
		}
		
		g.setColor(textColor);
		g.drawString(text, textXPos, textYPos);
		return new Point(textXPos, textYPos);
	}
	
	public static Point drawHorizontalAlignStringToParent(Graphics2D g, String text, Color textColor, float fontSize, Rectangle parentRect, eALIGNMENT eAlign,
														int yPosToParent)
	{
		return drawHorizontalAlignStringToParent(g, text, textColor, fontSize, parentRect, eAlign,
									      yPosToParent, 0, null);
	}
	
}
