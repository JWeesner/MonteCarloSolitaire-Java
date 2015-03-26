package proj3;

import proj3.Rank;
import proj3.Suit;

public class Card {
	private Rank rank;
	private Suit suit;
	
	//constuctor
	public Card(Rank rank, Suit suit){
		this.rank=rank;
		this.suit=suit;
	}
	
	//get rank
	public Rank getRank(){
		return rank;
	}
	//get suit
	public Suit getSuit(){
		return suit;
	}
	//rank mutator
	public void setRank(Rank rank){
		this.rank=rank;
	}
	//suit mutator
	public void setSuit(Suit suit) {
		this.suit=suit;
	}
	//override tostring method
	public String toString(){
		String card= this.rank.getName() + " of " + this.suit.getName();
		return card;
	}
	//unit testing
	public static void main(String[] args){
		Card test = new Card(Rank.ACE, Suit.CLUBS);
		System.out.println(test.toString());
		System.out.println(test.getSuit());
		System.out.println(test.getRank());
		test.setRank(Rank.KING);
		test.setSuit(Suit.DIAMONDS);
		System.out.println(test.toString());
	}
}