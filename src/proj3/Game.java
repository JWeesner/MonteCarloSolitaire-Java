package proj3;
import java.util.Random;

public class Game {
	//instance variables
	private long rngSeed;
	private int rows;
	private int cols;
	private Card[] monteCarlo;
	private Deck deck = new Deck();
	private int cardsRemoved;
	int cardsMissing;
	int empty;
	int cardsDealt=0;
	
	//constructor
	public Game(int rows, int cols){
		this.rows=rows;
		this.cols=cols;
		monteCarlo = new Card[rows*cols];
		//create a random number using the random library
		Random rand = new Random();
		rngSeed=rand.nextLong();
		newGame(rand.nextLong());
	}
	
	public boolean isHintImplemented(){
		return true;
	}
	
	//shifts all cards back of tableau then deals where necessary
	public void consolidate(){
		shift();
		int nullCount=0;
		//loop through keeping track of null spaces
		for(int i=0; i<monteCarlo.length; i++){
			if(monteCarlo[i]==null){
				nullCount++;
			}
		}
		//if there's cards left and nulls left deal cards till one is gone
		while(nullCount>0 && numberOfCardsLeft()>0){
			monteCarlo[rows*cols-nullCount]=deck.drawCard();
			cardsDealt++;
			nullCount--;
		}
	}
	
	//pass all non null cards into a temp array and then replace them in tableau
	public void shift(){
		Card[] temp = new Card[rows*cols];
		int tempCount=0;
		for(int i=0; i<monteCarlo.length; i++){
			if(monteCarlo[i] != null){
				temp[tempCount]=monteCarlo[i];
				tempCount++;
			}
		}
		for(int i=0; i<temp.length; i++){
			monteCarlo[i]=temp[i];
		}
	}
	
	//checks if cards should be cleared from tableau
	public boolean removeCards(Coordinate rowscols, Coordinate rowscols2){
		//check similar ranks
		if(monteCarlo[rowscols.getPos()].getRank()
				==monteCarlo[rowscols2.getPos()].getRank()){
			//ensure they're within 1 row and or 1 column away
			if(Math.abs(rowscols.getColumn()-rowscols2.getColumn())==1 
					|| rowscols.getColumn()-rowscols2.getColumn()==0){
				if(Math.abs(rowscols.getRow()-rowscols2.getRow())==1 
						|| rowscols.getRow()-rowscols2.getRow()==0){
					monteCarlo[rowscols.getPos()] = null;
					monteCarlo[rowscols2.getPos()] = null;
					cardsMissing+=2;
					cardsRemoved+=2;
					return true;
				}
			}
		}
		return false;
	}
	
	//getter for cards left total
	public int numberOfCardsLeft(){
		return (deck.getSize()-rows*cols-cardsDealt);
		//total cards-initial cards-dealt cards
	}
	
	//getter for suit at certain position
	public Suit getSuit(Coordinate rowscols){
		if(monteCarlo[rowscols.getPos()]==null){
			return null;
		}
		return monteCarlo[rowscols.getPos()].getSuit();
	}
	
	//rank getter
	public Rank getRank(Coordinate rowscols){
		if(monteCarlo[rowscols.getPos()]==null){
			return null;
		}
		return monteCarlo[rowscols.getPos()].getRank();
	}
	
	//new game method with random seed
	public void newGame(long gameSeed){
		cardsRemoved=0;     //reset counters
		cardsDealt=0;
		rngSeed=gameSeed;
		deck = new Deck();
		deck.Shuffle(gameSeed);   //create new deck and shuffle
		for(int i=0; i<monteCarlo.length; i++){
			monteCarlo[i] = deck.drawCard();    //fill tableau to start game
		}
	}
	
	public int getScore(){
		return cardsRemoved;
	}
	
	//explains rules of the game to user
	public String getHelpText(){
		String help = "This is a Monte Carlo Solitaire Game. The objective is to \n"
				+ "pair cards of the same rank(suit is irrelevant). Cards must \n"
				+ "be directly above, beside, or diagonal to be paired. \n" 
				+ "If there is no more pairs, consolidate to get new cards. \n"
				+ "Game is won when all cards are paired.";
		return help;
	}
	
	//replays same game using same rng seed
	public void replay(){
		cardsRemoved=0;
		cardsDealt=0;
		deck = new Deck();
		deck.Shuffle(rngSeed);
		for(int i=0; i<monteCarlo.length; i++){
			monteCarlo[i] = deck.drawCard();
		}
	}
	
	
	public Coordinate[] getHint(){
		for(int i=0; i<monteCarlo.length; i++){
			for(int j=0; j<monteCarlo.length; j++){
				if(j==i+cols || j==i-cols || (j==i-1 && j%cols!=cols-1) || 
						(j==i+1 && j%cols!=0) ||(j==i+4 && i%cols!=0) || (j==i-4 && i%(cols-1)!=0)
						|| (j==i+6 && i%(cols-1)!=0) || (j==i-6 && i%cols!=0)){
					if(monteCarlo[j]!=null && monteCarlo[i]!=null){
						if(monteCarlo[j].getRank()==monteCarlo[i].getRank()){
							int jRow = j/cols;
							int jCol = j%cols;
							int iRow = i/cols;
							int iCol = i%cols;
							Coordinate jCoord = new Coordinate(jRow, jCol);
							Coordinate iCoord = new Coordinate(iRow, iCol);
							Coordinate[] hint= new Coordinate[2];
							hint[0] = jCoord;
							hint[1] = iCoord;
							return hint;
						}
					}
				}
			}
		}
		return null;
		//Coordinate[] none= new Coordinate[1];
		//return none;
	}
	
	public boolean isWin(){
		if(getScore()==52){
			return true;
		}
		return false;
	}
	
	//unit testing
	public static void main(String[] args){
		Game test = new Game(3, 3);
		Game test1 = new Game(7,7);
		Game test2 = new Game(5, 5);
		System.out.println(test2.getScore()); //expecting 0 here
		System.out.println(test2.isWin()); //expecting false here
		System.out.println(test2.numberOfCardsLeft()); //expecting 27 here
		System.out.println(test1.numberOfCardsLeft()); //expecting 3 here
		System.out.println(test.numberOfCardsLeft()); //expecting 43 here
		System.out.println(test2.getHelpText()); //expecting help text output
	}
}
