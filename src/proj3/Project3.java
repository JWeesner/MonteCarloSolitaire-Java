package proj3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 * The GUI for the Monte Carlo Solitaire Project
 * 
 * Requires <code>Suit.java</code>, <code>Rank.java</code>, <code>ClicableImage.java</code> and
 * <code>ClickableImageGrid.java</code> which are provided.
 * 
 * Requires <code>Game.java</code> and its supporting classes written by students
 * 
 * <div style="border: 2px solid #F0E6A6;background-color: #FFF7C6;padding: 10px 20px;">
 * This class is provided for you &mdash; you must not change it. 
 * </div>
 * 
 */

public class Project3 {

	/**
	 * Title to be used on the JFrame
	 */
	private static final String GAME_NAME = "Monte Carlo Solitaire";

	/**
	 * Try to use system look and feel, and behave more sanely on Mac OS X 
	 */
	static {
		try {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", GAME_NAME);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// rely on defaults if not available
		}
	}

	/**
	 * The number of rows in the tableau
	 */
	private static final int TABLEAU_ROWS = 5;
	
	/**
	 * The number of columns in the tableau
	 */
	private static final int TABLEAU_COLS = 5;

	/**
	 * The border to be used for unselected cards
	 */
	private static final Border UNSELECTED_BORDER = new EmptyBorder(5, 5, 5, 5);
	
	/**
	 * The border to be used for selected cards
	 */
	private static final Border SELECTED_BORDER = new CompoundBorder(
			new LineBorder(new Color(255, 255, 0, 191), 3),
			new EmptyBorder(2, 2, 2, 2)
	);
	
	/**
	 * The border to be used for hinted cards
	 */
	private static final Border HINT_BORDER = new CompoundBorder(
			new LineBorder(new Color(127, 255, 255, 191), 3),
			new EmptyBorder(2, 2, 2, 2)
	);

	/**
	 * The color to be used on the score/cards left labels
	 */
	private static final Color LABEL_COLOR = new Color(255, 255, 0, 191);
	
	/**
	 * The small-sized font to be used underneath the consolidate card
	 */
	private static final Font SM_LABEL_FONT = new Font("Sans Serif", Font.PLAIN, 12);
	
	/**
	 * The medium-sized font to be used on the score/cards left titles 
	 */
	private static final Font MED_LABEL_FONT = new Font("Sans Serif", Font.BOLD, 14);
	
	/**
	 * The large-sized font to be used for the score/cards left values
	 */
	private static final Font LG_LABEL_FONT = new Font("Sans Serif", Font.BOLD, 32);
	
	/**
	 * The background color to show underneath the cards
	 */
	private static final Color CARD_BG_COLOR = new Color(35, 150, 70);
	
	/**
	 * Stores the listener for cards that detects clicks and hovers
	 */
	private CardListener cardListener = new CardListener();
	
	/**
	 * The random number generator used solely for creating random game numbers (not to be confused with the random
	 * number generators that will need to be present within the <code>Game</code> class to shuffle cards).
	 */
	private Random randomGameNumberGenerator = new Random();

	/**
	 * The underlying game object that performs all of the game logic
	 */
	private Game game = new Game(TABLEAU_ROWS, TABLEAU_COLS);

	/**
	 * Represents the top level window that makes up the GUI
	 */
	private JFrame mainFrame;
	
	/**
	 * Keeps a handle on the label used to display the number of cards left
	 */
	private JLabel cardsLeftLabel;
	
	/**
	 * Keeps a handle on the label used to display the score 
	 */
	private JLabel scoreLabel;
	
	/**
	 * Keeps a handle on the label which is pressed to consolidate the tableau
	 */
	private JLabel consolidator;

	/**
	 * Represents the tableau containing card images in the GUI
	 */
	private ClickableImageGrid tableau = null;

	/**
	 * Used to create the main (top-level) frame
	 */
	private void createMainFrame() {
		mainFrame = new JFrame();
		mainFrame.setIconImage(new ImageIcon(getClass().getResource("images/cards.png")).getImage());
		mainFrame.setContentPane(new FeltPanel(new Color(32, 128, 64)));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(new BorderLayout());
	}

	/**
	 * Used to create the tableau in the GUI
	 */
	private void createTableau() {
		tableau = new ClickableImageGrid(TABLEAU_ROWS, TABLEAU_COLS);
		tableau.setBorder(new EmptyBorder(6, 6, 6, 6));
		tableau.setOpaque(false);
		tableau.setAlignmentY(.5f);
		populateTableau();
		mainFrame.add(tableau, BorderLayout.CENTER);
	}

	/**
	 * Used to create the menu bar in the frame
	 */
	private void createMainMenu() {

		// detect if we're running on Mac so we can make Java behave better
		boolean isMac = System.getProperty("os.name").contains("Mac");
		int mask = isMac ? InputEvent.META_MASK : InputEvent.CTRL_MASK;

		// main menu bar
		JMenuBar mainMenu = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenu controlMenu = new JMenu("Control");
		JMenu helpMenu = new JMenu("Help");

		// menu item, listener, accelerator for new random game
		JMenuItem randomGameItem = new JMenuItem("New Random Game");
		randomGameItem.addActionListener(new NewRandomGameListener());
		randomGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, mask));
		gameMenu.add(randomGameItem);
		
		// menu item, listener, accelerator for new game (with specified number)
		JMenuItem specificGameItem = new JMenuItem("New Game...");
		specificGameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String s = (String) JOptionPane.showInputDialog(
						Project3.this.mainFrame, 
						"Enter the game number",
						"Play Game Number", 
						JOptionPane.PLAIN_MESSAGE);
				try {
					newGame(Long.parseLong(s));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(
							mainFrame,
							"Unrecognizable number!", 
							"Error",
							JOptionPane.PLAIN_MESSAGE
					);
				}
			}
		});
		specificGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, mask));
		gameMenu.add(specificGameItem);
		gameMenu.add(new JSeparator());
		
		// menu item, listener, accelerator for replay
		JMenuItem replayGameItem = new JMenuItem("Replay Current Game");
		replayGameItem.addActionListener(new ReplayListener());
		replayGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, mask));
		gameMenu.add(replayGameItem);
		
		// since mac already has a built-in quit when running in useScreenMenuBar mode
		// only add quit item, listener and accelerator for non-Mac platforms
		if(!isMac) {
			JMenuItem quitItem = new JMenuItem("Quit");
			quitItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask));
			gameMenu.add(new JSeparator());
			gameMenu.add(quitItem);
		}

		// menu item, listener, accelerator for consolidate
		JMenuItem consolidateItem = new JMenuItem("Consolidate");
		consolidateItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consolidate();
			}
		});
		consolidateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, mask));
		controlMenu.add(consolidateItem);

		// menu item, listener, accelerator for hint 
		JMenuItem hintItem = new JMenuItem("Hint");
		hintItem.addActionListener(new HintListener());
		hintItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, mask | (isMac ? InputEvent.SHIFT_MASK : 0)));
		controlMenu.add(hintItem);
		
		// disable hint if not implemented
		if(!game.isHintImplemented()) {
			hintItem.setEnabled(false);
		}
		
		// menu item, listener, accelerator for help
		JMenuItem helpItem = new JMenuItem(GAME_NAME + " Help");
		helpItem.addActionListener(new HelpListener());
		helpMenu.add(helpItem);

		// add each sub-menu to top level menu & add to frame
		mainMenu.add(gameMenu);
		mainMenu.add(controlMenu);
		mainMenu.add(helpMenu);
		mainFrame.setJMenuBar(mainMenu);

	}
	
	/**
	 * Used to create the sidebar in the GUI that houses to consolidate label
	 * and score/cards left panels
	 */
	private void createSideBar() {

		// panel to wrap everything on the left side pane
		JPanel sidePanel = new JPanel(new BorderLayout());
		sidePanel.setBorder(new EmptyBorder(10, 10, 10, 64));
		sidePanel.setOpaque(false);
		mainFrame.add(sidePanel, BorderLayout.WEST);

		// the card deck pressed to consolidate the tableau
		consolidator = new JLabel();
		consolidator.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				consolidate();
			}
			public void mouseExited(MouseEvent e) {
				consolidator.setBorder(UNSELECTED_BORDER);
			}
			public void mouseEntered(MouseEvent e) {
				consolidator.setBorder(SELECTED_BORDER);
			}
			public void mousePressed(MouseEvent e) { }
			public void mouseClicked(MouseEvent e) { }
		});
		consolidator.setBackground(CARD_BG_COLOR);
		consolidator.setOpaque(true);
		consolidator.setBorder(UNSELECTED_BORDER);

		// wrap the card deck and add to the top of the side panel
		JPanel cardWrapper = new JPanel();
		cardWrapper.setOpaque(false);
		cardWrapper.setBorder(new EmptyBorder(0, 12, 0, 12));
		cardWrapper.add(this.consolidator, BorderLayout.CENTER);
		sidePanel.add(cardWrapper);

		// title wrapped panel for the number of cards remaining
		TitledBorder cardsLeftTitle = new TitledBorder(new LineBorder(LABEL_COLOR, 2), "Cards Left");
		cardsLeftTitle.setTitleFont(MED_LABEL_FONT);
		cardsLeftTitle.setTitleColor(LABEL_COLOR);
		cardsLeftTitle.setTitleJustification(TitledBorder.CENTER);
		this.cardsLeftLabel = new JLabel();
		this.cardsLeftLabel.setFont(LG_LABEL_FONT);
		this.cardsLeftLabel.setForeground(LABEL_COLOR);
		JPanel cardsLeftPanel = new JPanel();
		cardsLeftPanel.setBorder(cardsLeftTitle);
		cardsLeftPanel.setOpaque(false);
		cardsLeftPanel.add(this.cardsLeftLabel);

		// title wrapped panel for the score
		TitledBorder scoreTitle = new TitledBorder(new LineBorder(LABEL_COLOR, 2), "Score");
		scoreTitle.setTitleFont(MED_LABEL_FONT);
		scoreTitle.setTitleColor(LABEL_COLOR);
		scoreTitle.setTitleJustification(TitledBorder.CENTER);
		this.scoreLabel = new JLabel();
		this.scoreLabel.setFont(LG_LABEL_FONT);
		this.scoreLabel.setForeground(LABEL_COLOR);
		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(scoreTitle);
		scorePanel.setOpaque(false);
		scorePanel.add(this.scoreLabel);

		// wrap cards left & score panels and add to side pane
		JPanel bottomWrapperPanel = new JPanel(new BorderLayout());
		bottomWrapperPanel.setOpaque(false);
		bottomWrapperPanel.add(cardsLeftPanel, BorderLayout.NORTH);
		bottomWrapperPanel.add(scorePanel, BorderLayout.SOUTH);
		sidePanel.add(bottomWrapperPanel, BorderLayout.SOUTH);

	}

	/**
	 * Performs the consolidate operation for a Monte Carlo Solitaire game
	 * 
	 * @see Game#isHintImplemented()
	 */
	private void consolidate() {
		game.consolidate();
		populateTableau();
		cardsLeftLabel.setText("" + game.numberOfCardsLeft());
		if(game.numberOfCardsLeft() == 0) {
			consolidator.setPreferredSize(consolidator.getSize());
			consolidator.setIcon(null);
			consolidator.setText("Consolidate");
			consolidator.setHorizontalAlignment(SwingConstants.CENTER);
			consolidator.setFont(SM_LABEL_FONT);
			consolidator.setForeground(LABEL_COLOR);
		}
	}
	
	/**
	 * The sole constructor for Project 3 - creates & displays a new GUI for the Monte Carlo Solitaire game and creates
	 * & starts a new random game
	 */
	public Project3() {

		// create UI
		createMainFrame();
		createTableau();
		createSideBar();
		createMainMenu();

		newRandomGame();

		// make the GUI visible
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	/**
	 * Basically sets the GUI to reflect the state of the underlying <code>Game</code>
	 */
	private void populateTableau() {
		tableau.clearImages();
		for (int r = 0; r < TABLEAU_ROWS; r++) {
			for (int c = 0; c < TABLEAU_COLS; c++) {
				// get rank and suit of card at [r][c]
				// generate file name, read file, create image, add to grid
				Suit suit = game.getSuit(new Coordinate(r, c));
				Rank rank = game.getRank(new Coordinate(r, c));
				String cardFile;
				if (suit != null && rank != null) {
					cardFile = String.format("images/%s_%s.png", suit.getSymbol(), rank.getSymbol());
					// create the clickable card image and listen for it to be clicked
					ImageIcon cardIcon = new ImageIcon(getClass().getResource(cardFile));
					ClickableImage cImage = new ClickableImage(cardIcon);
					cImage.addMouseListener(cardListener);
					cImage.setBackground(CARD_BG_COLOR);
					cImage.setOpaque(true);
					tableau.addImage(cImage, r, c, UNSELECTED_BORDER);
				}
			}
		}
	}

	/**
	 * Creates a new game of the specified number
	 * 
	 * @param gameNumber
	 *            the game number to start
	 */
	private void newGame(long gameNumber) {
		mainFrame.setTitle(GAME_NAME + " Game #" + gameNumber);
		game.newGame(gameNumber);
		cardsLeftLabel.setText("" + game.numberOfCardsLeft());
		scoreLabel.setText("" + game.getScore());
		consolidator.setIcon(new ImageIcon(getClass().getResource("images/back.png")));
		populateTableau();
	}
	
	/**
	 * Creates a new game with a random number
	 */
	private void newRandomGame() {
		newGame(Math.abs(this.randomGameNumberGenerator.nextLong()) % 1000000000);// tame seed a little
	}

	/**
	 * The entry point for the app (no args required)
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args) {
		new Project3();
	}

	// **** inner class listeners for buttons and card clicks ****

	/**
	 * Used to display the help GUI when the menu item is selected
	 */
	private class HelpListener implements ActionListener {
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			JTextArea helpTextArea = new JTextArea();
			helpTextArea.setText(game.getHelpText());
			helpTextArea.setEditable(false);
			helpTextArea.setPreferredSize(new Dimension(480, 320));
			JOptionPane.showMessageDialog(
				mainFrame,
				new JScrollPane(helpTextArea),
				GAME_NAME + " Help",
				JOptionPane.PLAIN_MESSAGE
			);
		}
	}

	/**
	 * Used to start a new random game when menu item is selected
	 */
	private class NewRandomGameListener implements ActionListener {
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			newRandomGame();
		}
	}

	/**
	 * Used to replay the current game when the menu item is selected
	 */
	private class ReplayListener implements ActionListener {
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			game.replay();
			populateTableau();
			cardsLeftLabel.setText("" + game.numberOfCardsLeft());
			scoreLabel.setText("" + game.getScore());
		}
	}

	/**
	 * Used to display a hint when the menu item is selected
	 */
	private class HintListener implements ActionListener {
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			Coordinate[] coords = game.getHint();
			if (coords != null) {
				Project3.this.cardListener = new CardListener();
				populateTableau();
				tableau.setBorder(coords[0].getRow(), coords[0].getColumn(), HINT_BORDER);
				tableau.setBorder(coords[1].getRow(), coords[1].getColumn(), HINT_BORDER);
			} else {
				JOptionPane.showMessageDialog(mainFrame,
					    "No moves available - try consolidating",
					    "Hint",
					    JOptionPane.PLAIN_MESSAGE);
			}
		}
	}

	/**
	 * Handles the selection of cards 
	 */
	private class CardListener implements MouseListener {

		/**
		 * Used to count the number of cards currently selected
		 */
		private int cardsClicked = 0;
		
		/**
		 * Tracks the first card the user selected
		 */
		private ClickableImage firstCardClicked;
		
		/**
		 * The border of the first image that was hovered over (used to restore when hovering out)
		 */
		private Border oldBorder1;
		
		/**
		 * The border of the second image that was hovered over (used to restore when hovering out)
		 */
		private Border oldBorder2;

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			ClickableImage cImage = (ClickableImage) e.getSource();
			if (!cImage.isClicked()) {
				cImage.setClicked(true);
				// if this is the first (of two) cards to be clicked,
				// save the card and and change the border
				if (cardsClicked == 0) {
					cardsClicked++;
					firstCardClicked = cImage;
					firstCardClicked.setBorder(SELECTED_BORDER);
				} else {
					// if this is the 2nd card clicked, ask the game if they
					// should be removed
					int row1 = firstCardClicked.getRow();
					int col1 = firstCardClicked.getColumn();
					int row2 = cImage.getRow();
					int col2 = cImage.getColumn();
					if (game.removeCards(new Coordinate(row1, col1), new Coordinate(row2, col2))) {
						// remove the cards, update score and see if player wins
						tableau.removeImage(row1, col1);
						tableau.removeImage(row2, col2);

						// update the score on the screen and ask the game if
						// the player has won
						// if player wins, display a new window with message
						scoreLabel.setText("" + game.getScore());
						if (game.isWin()) {
							JOptionPane.showMessageDialog(
								mainFrame,
								"You Win!!!", 
								"Congratulations",
								JOptionPane.PLAIN_MESSAGE
							);
						}
					} else {
						// the two cards should not be removed, beep and
						// reset clicked status and first card's border
						Toolkit.getDefaultToolkit().beep();
						cImage.setClicked(false);
						firstCardClicked.setClicked(false);
						firstCardClicked.setBorder(oldBorder1);
						cImage.setBorder(oldBorder2);
						oldBorder1 = oldBorder2;
					}

					// whether removed or not, next card is considered
					// firstCardClicked
					cardsClicked = 0;
				}
			} else {
				cardsClicked = 0;
				firstCardClicked.setBorder(oldBorder1);
				firstCardClicked.setClicked(false);
			}
		}
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
			ClickableImage cImage = (ClickableImage) e.getSource();
			if(cardsClicked == 0) {
				oldBorder1 = cImage.getBorder();
				cImage.setBorder(SELECTED_BORDER);
			} else if(cardsClicked == 1) {
				oldBorder2 = cImage.getBorder();
				cImage.setBorder(SELECTED_BORDER);
			}
		}
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {
			ClickableImage cImage = (ClickableImage) e.getSource();
			if(!cImage.isClicked() && cardsClicked == 0 && !cImage.getBorder().equals(HINT_BORDER)) {
				cImage.setBorder(oldBorder1);
			} else if (!cImage.isClicked() && cardsClicked == 1) {
				cImage.setBorder(oldBorder2);
			}
		}
		public void mousePressed(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
	}

	/**
	 * A specialized version of <code>JPanel</code> that draws itself to look like felt.
	 */
	private class FeltPanel extends JPanel {

		/**
		 * A class version number, per <code>Serializable</code>'s recommendation.
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Used to create the initial texture (which will be repeated across the face of the JPanel)
		 */
		private BufferedImage texture;
		
		/**
		 * The sole constructor that specifies the (approximate) color to use to draw the felt background
		 * 
		 * @param color
		 *            the base color for the felt
		 */
		public FeltPanel(Color color) {
			texture = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
			for (int x = 0, xMax = texture.getWidth(); x < xMax; x++) {
				for (int y = 0, yMax = texture.getWidth(); y < yMax; y++) {
					int red = (int) (color.getRed() * (Math.random() * .1 + .9));
					int green = (int) (color.getGreen() * (Math.random() * .1 + .9));
					int blue = (int) (color.getBlue() * (Math.random() * .1 + .9));
					texture.setRGB(x, y, (red << 16) + (green << 8) + blue);
				}
			}
		}

		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint(new TexturePaint(texture, new Rectangle(0, 0, 128, 128)));
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
	};
	
}
