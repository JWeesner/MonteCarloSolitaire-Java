package proj3;

import java.util.Random;

public class Deck {
	//variables
	private final int deckSize=52;
	Card[] deck = new Card[deckSize];
	private int next;
	
	//constructor
	public Deck(){
		int position=0;
		for(Suit suit: Suit.values()){
			for(Rank rank: Rank.values()){
				deck[position]= new Card(rank, suit);
				position++;
			}
		}
	}
	
	//return the next card in the deck and then make it's position null
	public Card drawCard(){
		Card temp = deck[next];
		deck[next] = null;
		next = findNextCardIndex();
		return temp;
	}

	//shuffle
	public void Shuffle(long seed){
		int size=deckSize-1;
		Random seedNum= new Random(seed);
		//seedNum.setSeed(seed);
		
		while(size>0){
		//seedNum.nextInt(deckSize);
		int randPos=seedNum.nextInt(size+1);
		Card temp = deck[size];
		deck[size]=deck[randPos];
		deck[randPos]= temp;
		size--;
		}
	}
	
	//return the next card in the deck
	private int findNextCardIndex(){
		for(int i = 0; i < deck.length; i++){
			if(!(deck[i] == null)){
				return i;
			}
		}
		return -1;
	}

	//override default tostring method
	public String toString(){
		String card="";
		for(int i=0; i<52; i++){
		card+=deck[i].getRank() + " " + deck[i].getSuit() + "\n";
		}
		return card;
	}
	
	//simnple mutator
	public void setCard(int pos, Card c){
		deck[pos]=c;
	}
	
	//simple accesser 
	public int getSize(){
		return deckSize;
	}
	
	//getNextCard
	public Card getCardAt(int next){
		return deck[next];
	}
	
	//Unit Testing
	public static void main(String[] args){
		//constructor
		Deck test = new Deck();
		
		//method tests
		System.out.println(test.findNextCardIndex()); //expecting 0
		System.out.println(test.drawCard()); //expecting Ace of Clubs
		System.out.println(test.getSize()); //expecting 52
		System.out.println(test.findNextCardIndex()); //expecting 1
		test.Shuffle(12345);
		System.out.println(test.drawCard()); //expecting random card
	}
}
