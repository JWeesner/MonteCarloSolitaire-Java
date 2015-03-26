package proj3;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Extends <code>JLabel</code> to make the it clickable and row/column aware ( intended to be used with the a
 * <code>ClickableImageGrid</code>.
 *
 * <div style="border: 2px solid #F0E6A6;background-color: #FFF7C6;padding: 10px 20px;">
 * This class is provided for you &mdash; you must not change it. 
 * </div>
 * 
 * @see proj3.ClickableImageGrid
 * 
 * @author Dennis L. Frey (Created: Unknown)
 * @author Daniel J. Hood (Modified: 23 Mar 2011)
 * 
 */
public class ClickableImage extends JLabel {

	/**
	 * A class version number, per <code>Serializable</code>'s recommendation.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The row the image lives in on the grid.
	 */
	private int row;
	
	/**
	 * The column the image lives in on the grid.
	 */
	private int column;

	/**
	 * Records whether or not the label has been clicked
	 */
	private boolean clicked;

	/**
	 * Sole constructor that creates a <code>ClickableImage</code> using the specified icon
	 * 
	 * @param icon
	 *            the icon to use as the image
	 */
	public ClickableImage(ImageIcon icon) {
		super(icon);
		setClicked(false);
	}

	/**
	 * Sets the row of the clickable image
	 * 
	 * @param row
	 *            the row image "lives" in
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
	/**
	 * Sets the column of the clickable image
	 * 
	 * @param column
	 *            the column image "lives" in
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * Returns the row of the clickable image
	 * 
	 * @return the row the image "lives" in
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column of the clickable image
	 * 
	 * @return the column the image "lives" in
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Set the image click state as specified
	 * 
	 * @param clicked
	 *            <code>true</code> if the image should be flagged as being clicked, <code>false</code> otherwise
	 */
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	/**
	 * Returns the image clicked status
	 * 
	 * @return <code>true</code> if the image has been clicked, <code>false</code> otherwise
	 */
	public boolean isClicked() {
		return clicked;
	}

}
