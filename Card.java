import java.util.HashMap;

public class Card {
	
	String suit;
	String value;
	
	final static int CARD_WIDTH = 72;
	final static int CARD_HEIGHT = 100;
	
	final static int DRAW_CARD_WIDTH = 100;
	final static int DRAW_CARD_HEIGHT = 140;
	
	HashMap<String, Integer> drawMapSuit = new HashMap<String, Integer>();
	HashMap<String, Integer> drawMapValue = new HashMap<String, Integer>();
	
	static HashMap<String, Integer> valueToInt= new HashMap<String, Integer>();
	static HashMap<Integer, String> intToCard = new HashMap<Integer, String>();
	
	
	public Card(){
		this.suit = "NULL";
		this.value = "NULL";
	}
	
	public Card(String suit, String value) {
		SetCard(suit, value);
		makeMap();
	}
	
	public String getCard() {
		return value+";"+suit;
	}
	
	public void makeMap() {
		drawMapSuit.put("Heart", 0);
		drawMapSuit.put("Diamond", 1);
		drawMapSuit.put("Club", 2);
		drawMapSuit.put("Spade", 3);
	    
		drawMapValue.put("Ace",0);
		drawMapValue.put("2",1);
		drawMapValue.put("3",2);
		drawMapValue.put("4",3);
		drawMapValue.put("5",4);
		drawMapValue.put("6",5);
		drawMapValue.put("7",6);
		drawMapValue.put("8",7);
		drawMapValue.put("9",8);
		drawMapValue.put("10",9);
		drawMapValue.put("Jack",10);
		drawMapValue.put("Queen",11);
		drawMapValue.put("King",12);
		
		valueToInt.put("Ace", 15);
		valueToInt.put("2", 2);
		valueToInt.put("3", 3);
		valueToInt.put("4", 4);
		valueToInt.put("5", 5);
		valueToInt.put("6", 6);
		valueToInt.put("7", 7);
		valueToInt.put("8", 8);
		valueToInt.put("9", 9);
		valueToInt.put("10", 10);
		valueToInt.put("Jack", 12);
		valueToInt.put("Queen", 13);
		valueToInt.put("King", 14);
		
		for(HashMap.Entry<String, Integer> entry: valueToInt.entrySet()){
		   intToCard.put(entry.getValue(), entry.getKey());
		}
	}
	
	public static String numToCard(int num){
		return intToCard.get(num);
		
	}
	
	public int getValueMap(String type) {
		return drawMapValue.get(type);
	}
	
	public int getValue() {
		return valueToInt.get(this.value);
	}
	
	public static int getValue(String value) {
		if(!value.equals("null")) {
			return valueToInt.get(value);
		}
		else return -1;
	}
	
	public String getSuit() {
		return this.suit;
	}
	
	public int getSuitRep() {
		return drawMapSuit.get(this.suit);
	}
	
	public static String rep2String(int rep) {	
		
		switch(rep) {
			case 0:
				return "Heart";
			case 1:
				return "Diamond";
			case 2:
				return "Club";
			case 3:
				return "Spade";
			default:
				return "null";
		}
		
	}
	
	public void SetCard(String suit, String value) {
		this.suit = suit;
		this.value = value;
	}
	
	public int[] computeDrawingData(int relx, int rely, double scale) {
		/* 1 - hearts
		 * 2 - diamonds
		 * 3 - clubs
		 * 4 - spades
		 * ace to king
		 */
		
		int[] map = new int[8];
		for(int i=0; i<8; i++) {
			map[i] = 0;
		}
		//calculating crop position
		map[0] = relx; 
		map[1] = rely;
		map[2] =  (int) (relx+(DRAW_CARD_WIDTH*scale));
		map[3] = (int) (rely+(DRAW_CARD_HEIGHT*scale));
								
		map[4] = drawMapValue.get(value)*CARD_WIDTH;
		map[5] = drawMapSuit.get(suit)*CARD_HEIGHT;
		map[6] = drawMapValue.get(value)*CARD_WIDTH + CARD_WIDTH;
		map[7] = drawMapSuit.get(suit)*CARD_HEIGHT + CARD_HEIGHT;
		
		return map;
		
	}
	
	public static int[] drawBackside(int relx, int rely, double scale) {
		int[] map = new int[8];
		for(int i=0; i<8; i++) {
			map[i] = 0;
		}
		
		map[0] = relx; 
		map[1] = rely;
		map[2] =  (int) (relx+(DRAW_CARD_WIDTH*scale));
		map[3] = (int) (rely+(DRAW_CARD_HEIGHT*scale));
								
		map[4] = 7*CARD_WIDTH;
		map[5] = 4*CARD_HEIGHT;
		map[6] = 7*CARD_WIDTH + CARD_WIDTH;
		map[7] = 4*CARD_HEIGHT + CARD_HEIGHT;
		
		return map;
		
	}
	
	@Override
	public String toString() {
		return this.value+" of "+this.suit+"s";
	}
	@Override
	public boolean equals(Object obj) {
		
		Card cardcmp;
		
		if(obj instanceof Card) {
			cardcmp = (Card) obj;
		}else {
			return false;
		}
		
		if(this.suit.equals(cardcmp.suit) && this.value.equals(cardcmp.value)) {
			return true;
		}else {
			return false;
		}
		
		
	}
	
	
}
