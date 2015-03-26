package proj3;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Extends <code>JPanel</code> to provide a grid layout capability. Basically this class behaves much like
 * <code>GridLayout</code>, except that it is capable of having &ldquo;blank&rdquo; spaces in the grid.
 * 
 * <div style="border: 2px solid #F0E6A6;background-color: #FFF7C6;padding: 10px 20px;">
 * This class is provided for you &mdash; you must not change it. 
 * </div>
 * 
 * @see proj3.ClickableImage
 * 
 * @author Dennis L. Frey (Created: Unknown)
 * @author Daniel J. Hood (Modified: 23 Mar 2011)
 * 
 */
public class ClickableImageGrid extends JPanel {

	/**
	 * A class version number, per <code>Serializable</code>'s recommendation.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The number of rows in the grid.
	 */
	private int rows;
	
	/**
	 * The number of columns in the grid.
	 */
	private int columns;
	
	/**
	 * A 2D array of panels that wrap everything inserted into the grid.
	 */
	private JPanel[][] panels;
	
	/**
	 * Constructs a panel configured for the specified number of rows and columns.
	 * 
	 * @param rows
	 *            the number of rows
	 * @param columns
	 *            the number of columns
	 */
	public ClickableImageGrid(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		panels = new JPanel[rows][columns];
		setLayout(new GridLayout(rows, columns, 6, 6));
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < columns; col++) {
				// By wrapping every slot in a JPanel, we avoid having the grid collapse
				// when a row or column no longer contains elements.  This way each and
				// every slot will always have a widget (the JPanel) in it.
				panels[row][col] = new JPanel();
				panels[row][col].setOpaque(false);
				add(panels[row][col]);
			}
		}
	}

	/**
	 * Set the given <code>row</code>/<code>column</code> to contain the specified <code>image</code>. If an element
	 * previously existed at that location, it is first removed from the panel.
	 * 
	 * @param image
	 *            the image to store at the given slot
	 * @param row
	 *            the row number
	 * @param column
	 *            the column number
	 * @param border
	 *            the border to set on the <code>image</code>
	 */
	public void addImage(ClickableImage image, int row, int column, Border border) {
		image.setRow(row);
		image.setColumn(column);
		image.setBorder(border);
		panels[row][column].removeAll();
		panels[row][column].add(image);
		revalidate();
		repaint();
	}

	/**
	 * Removes the image from the grid at the given <code>row</code>/<code>column</code>.
	 * 
	 * @param row
	 *            the row number
	 * @param column
	 *            the column number
	 */
	public void removeImage(int row, int column) {
		Component old = panels[row][column].getComponent(0);
		JPanel dummyPanel = new JPanel();
		dummyPanel.setPreferredSize(old.getSize());
		dummyPanel.setBackground(old.getBackground());
		panels[row][column].removeAll();
		panels[row][column].add(dummyPanel);
		revalidate();
		repaint();
	}

	/**
	 * Removes all images from the grid.
	 */
	public void clearImages() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				if(panels[row][col].getComponentCount() != 0) {
					Component old = panels[row][col].getComponent(0);
					JPanel dummyPanel = new JPanel();
					dummyPanel.setPreferredSize(old.getSize());
					dummyPanel.setBackground(old.getBackground());
					dummyPanel.setOpaque(old.isOpaque());
					panels[row][col].removeAll();
					panels[row][col].add(dummyPanel);
				}
			}
		}
		revalidate();
		repaint();
	}

	/**
	 * Changes the <code>border</code> on the <code>image</code> at the specified <code>row</code>/<code>column</code>.
	 * 
	 * @param row
	 *            the row number
	 * @param column
	 *            the column number
	 * @param border
	 *            the border to set on the <code>image</code>
	 */
	public void setBorder(int row, int column, Border border) {
		((ClickableImage) panels[row][column].getComponent(0)).setBorder(border);
	}
	
}
