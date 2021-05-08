import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	
	ArrayList<Card> Deck = new ArrayList<Card>();
	final String[] suits = {"Spade", "Heart", "Club", "Diamond"};
	final String[] values = {"Ace","2", "3","4","5","6","7","8","9","10","Jack","Queen","King"};
	
	public Deck(){
		resetDeck();
	}
	
	public void resetDeck() {
		
		Deck = new ArrayList<Card>();
		
		for(int i=0;i<4;i++) {
			for(int j=0;j<13;j++) {
				Deck.add(new Card(suits[i], values[j]));
			}
		}
		
	}	
	
	
	public Card Pop() {
		//pop the top card from the deck
		
		Card topcard = Deck.get(0);
		Deck.remove(topcard);
		
		return topcard;
		
	}
	
	public void Shuffle() {
		Collections.shuffle(Deck);
	}
	
	@Override
	public String toString() {
		String buildstring = "";
		for(int i=0; i<Deck.size();i++) {
			buildstring += "["+(Deck.get(i).toString())+"]\n";
		}
		return buildstring;
	}
	
}
