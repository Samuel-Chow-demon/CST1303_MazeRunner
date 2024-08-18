package game.UtilityAndConstant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.junit.jupiter.api.Test;

class JPanelUtilityTest {

	@Test
    public void testCreateFlowLayoutPanel() {
        int alignOption = FlowLayout.CENTER;
        int hGap = 10;
        int vGap = 5;
        Color bg = Color.RED;

        JPanel panel = JPanelUtility.createFlowLayoutPanel(alignOption, hGap, vGap, bg);

        assertNotNull(panel);
        assertTrue(panel.getLayout() instanceof FlowLayout);
        FlowLayout layout = (FlowLayout) panel.getLayout();
        assertEquals(alignOption, layout.getAlignment());
        assertEquals(hGap, layout.getHgap());
        assertEquals(vGap, layout.getVgap());
        assertEquals(bg, panel.getBackground());
    }

    @Test
    public void testCreateBoxLayoutPanel() {
        int width = 300;
        int height = 150;
        int boxAxis = BoxLayout.Y_AXIS;
        Color bg = Color.BLUE;

        JPanel panel = JPanelUtility.createBoxLayoutPanel(width, height, boxAxis, bg);

        assertNotNull(panel);
        assertTrue(panel.getLayout() instanceof BoxLayout);
        BoxLayout layout = (BoxLayout) panel.getLayout();
        assertEquals(boxAxis, layout.getAxis());
        assertEquals(bg, panel.getBackground());
        assertEquals(new Dimension(width, height), panel.getPreferredSize());
    }

    @Test
    public void testCreateGridLayoutPanel() {
        Integer width = 400;
        Integer height = 200;
        GridLayout gridLayout = new GridLayout(2, 3);
        Border margin = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Color bg = Color.GREEN;

        JPanel panel = JPanelUtility.createGridLayoutPanel(width, height, gridLayout, margin, bg);

        assertNotNull(panel);
        assertTrue(panel.getLayout() instanceof GridLayout);
        assertEquals(gridLayout, panel.getLayout());
        assertEquals(bg, panel.getBackground());
        assertEquals(new Dimension(width, height), panel.getPreferredSize());
        assertEquals(margin, panel.getBorder());
    }
    
    @Test
    public void testCreateTextArea() {
        int rows = 5;
        int columns = 20;
        Boolean editable = true;
        Boolean wrap = true;

        JTextArea textArea = JPanelUtility.createTextArea(rows, columns, editable, wrap);

        assertNotNull(textArea);
        assertEquals(rows, textArea.getRows());
        assertEquals(columns, textArea.getColumns());
        assertEquals(editable, textArea.isEditable());
        assertEquals(wrap, textArea.getLineWrap());
        assertEquals(false, textArea.getWrapStyleWord()); // as per your method
        assertEquals("Monospaced.plain", textArea.getFont().getFontName());
    }
    
    @Test
    public void testCreateTextLabel() {
        String msg = "Test Label";
        Color foregd = Color.RED;
        Font font = new Font("Arial", Font.PLAIN, 12);

        JLabel label = JPanelUtility.createTextLabel(msg, foregd, font);

        assertEquals("Test Label", label.getText());
        assertEquals(Color.RED, label.getForeground());
        assertEquals(font, label.getFont());
    }
    
    @Test
    public void testCreateButton() {
        String title = "Test Button";
        int width = 100;
        int height = 50;
        Color border = Color.BLUE;
        int borderWidth = 2;
        Color txtColor = Color.GREEN;

        JButton button = JPanelUtility.createButton(title, width, height, border, borderWidth, txtColor);

        assertEquals("Test Button", button.getText());
        assertEquals(new Dimension(100, 50), button.getPreferredSize());
        assertEquals(txtColor, button.getForeground());
        assertTrue(button.getBorder() instanceof javax.swing.border.LineBorder);
    }
    
    @Test
    public void testSetPanelSize() {
        JPanel panel = new JPanel();
        JPanelUtility.setPanelSize(panel, 300, 200);

        Dimension expectedSize = new Dimension(300, 200);
        assertEquals(expectedSize, panel.getPreferredSize());
        assertEquals(expectedSize, panel.getMinimumSize());
        assertEquals(expectedSize, panel.getMaximumSize());
    }
    
    @Test
    public void testSetMargin() {
        JButton button = new JButton();
        JPanelUtility.setMargin(button, 10, 10, 15, 15);

        assertTrue(button.getBorder() instanceof javax.swing.border.EmptyBorder);
    }
    
    @Test
    public void testAddTextAreaMessage() {
        JTextArea textArea = new JTextArea();
        String state = "INFO";
        String action = "Action1";
        String message = "This is a message";

        JPanelUtility.addTextAreaMessage(textArea, state, action, message);

        assertTrue(textArea.getText().contains("INFO"));
        assertTrue(textArea.getText().contains("Action1"));
        assertTrue(textArea.getText().contains("This is a message"));
    }

    @Test
    public void testClearTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setText("Some text");

        JPanelUtility.clearTextArea(textArea);

        assertEquals("", textArea.getText());
        assertEquals(0, textArea.getCaretPosition());
    }
    
    
}
