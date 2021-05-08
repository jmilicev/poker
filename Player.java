import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

public class Player {
	
	private String name;
	
	private int balance;
	private double agro;
	
	@SuppressWarnings("unused")
	private boolean playing;
	private int previousGamestate = -1;
	private int gamestate = 0;
	private int totalstake = 0;
	private boolean allin;
	private boolean financialStatus;
	
	private final int baserate = 200;
	
	private Card card1;
	private Card card2;
	
	public Player(String name, int balance, double agro) {
		this.name = name;
		this.balance = balance;
		this.agro = agro;
		this.playing = true;
		this.financialStatus = false;
	}
	public Player(String name, int balance, double agro, Card card1, Card card2) {
		this(name, balance, agro);
		this.card1 = card1;
		this.card2 = card2;
	}
	
	public Card getCard1() {
		return this.card1;
	}
	public Card getCard2() {
		return this.card2;
	}
	public void allin(boolean allin) {
		this.allin = allin;
	}
	public boolean isallin() {
		return this.allin;
	}
	
	public int getStake() {
		return this.totalstake;
	}
	
	public void addStake(int stake) {
		this.totalstake += stake;
	}
	
	public void resetStake() {
		this.totalstake = 0;
	}
	
	public void setCards(Card card1, Card card2) {
		this.card1 = card1;
		this.card2 = card2;
	}
	
	public String evaluateHand(ArrayList<Card> table) {
		
		String evaluation = "";
		int arrsize = table.size() + 2;
		Card[] cards = new Card[7];
		
		cards[0] = this.card1;
		cards[1] = this.card2;
		
		for(int i=2;i<table.size()+2;i++) { 
			try {
			cards[i] = table.get(i-2);	
			}catch(Exception e) {
				System.out.println("critical system error, this round will not function properly");
				arrsize = 0;
			}
		} 
		
		//evaluate high card
		int highestvalue = cards[0].getValue() > cards[1].getValue() ? cards[0].getValue() : cards[1].getValue();
		
		evaluation = "high;"+highestvalue;
		
		//evaluate pairs, toak, foak
		int[] pairScore = new int[arrsize];
		
		for(int i = 0; i<arrsize; i++) {
			Card current = cards[i];
			for(int j = 0; j<arrsize; j++) {
				if(current.getValue() == cards[j].getValue()){
					pairScore[i]++;
				}
			}
		}
		boolean pair = false;
		int highestSet = 0;
		int position = 0;
		
		for(int i = 0; i<arrsize; i++) {
			if(pairScore[i] > highestSet) {
				highestSet = pairScore[i];
				position = i;
			}
		}
		
		int pairCounter = 0;
		int twoPairScorePos = 0;
		//two pair detection
		for(int i = 0; i<arrsize; i++) {
			if(pairScore[i] == 2) {
				pair = true;
				pairCounter++;
				if(pairCounter>=4) {
					twoPairScorePos = (cards[twoPairScorePos].getValue() > cards[i].getValue()) ? twoPairScorePos : i;
				}else {
				twoPairScorePos = i;
				}
			}
		}
		boolean twopair = (pairCounter >= 4) ? true : false;
		
		int foakstore = 0;
		boolean foak = false;
		boolean toak = false;
		
		switch(highestSet) {
			case 2:
				pair = true;
				evaluation = "pair;"+cards[position].getValue();
				
				if(twopair) {
					evaluation = "twopair;"+cards[twoPairScorePos].getValue();
				}
				
				break;
			case 3:
				toak = true;
				evaluation = "toak;"+cards[position].getValue();
				break;
			case 4:
				foak = true;
				foakstore = cards[position].getValue();
				break;
		}
		
			//flushes
			boolean flushDetected = false;
			String flushstore = "";
			int[] suitCounter = new int[4];
			ArrayList<Integer>membersOfFlush = new ArrayList<Integer>();
			/*
			 * 0 - heart
			 * 1 - diamond
			 * 2 - club
			 * 3 - spade
			 */
			for(int i=0; i<arrsize; i++) {
				for(int j=0; j<4; j++) {
					if(cards[i].getSuitRep() == j) {
						suitCounter[j]++;
					}
				}
			}
			int SuitFlushPos = 0;
			for(int i=0; i<4; i++) {
				if(suitCounter[i] >= 5) {
					flushDetected = true;
					SuitFlushPos = i;
				}
			}
			if(flushDetected) {
				String findSuit = Card.rep2String(SuitFlushPos);
				int highestValueOfFlush = -1;
				
				for(int i=0;i<arrsize;i++) {
					if((cards[i].getSuit().equals(Card.rep2String(SuitFlushPos))) && (cards[i].getValue() > highestValueOfFlush)){
						highestValueOfFlush = cards[i].getValue();
					}
					if(cards[i].getSuit() == findSuit) {
						membersOfFlush.add(cards[i].getValue());
					}
					
				}
					flushstore = "flush;"+highestValueOfFlush;
			}
			
			//straights
			
			TreeSet<Integer> cardAsValueDups = new TreeSet<Integer>();
			int highestValueOfStraight = 0;
			
			int mapsize = arrsize;
			
			for(int i=0; i<arrsize; i++) {
				cardAsValueDups.add(cards[i].getValue());
				if(cards[i].getValue() == 15) {
					//If there is an ace, it should be represented as 1 and 15 in straight detection.
					cardAsValueDups.add(1);
					mapsize++;
				}
			}
			
			//convert from set to list to remove duplicates, as they are not relevant in a straight
			ArrayList<Integer> cardAsValue = new ArrayList<Integer>(cardAsValueDups);
			
			Collections.sort(cardAsValue);
			mapsize = cardAsValue.size();
			
			int inLineCounter = 0;
			boolean foundConsecutive = false;
			boolean straightDetected = false;
			int maxpos=0;
			int highestPosOfStraight = -1;
			
			for(int i=0;i<mapsize-1;i++) {
				foundConsecutive = false;
				//straight are not consecutive at 10, as valid straight goes 8,9,10,12,13 etc
				if(  ((cardAsValue.get(i)+1) == (cardAsValue.get(i+1))) && cardAsValue.get(i) != 10) {
					inLineCounter++;
					foundConsecutive = true;
					highestPosOfStraight = i+1;
				}if( ((cardAsValue.get(i)+2) == (cardAsValue.get(i+1))) && cardAsValue.get(i) == 10) {
					inLineCounter++;
					foundConsecutive = true;
					highestPosOfStraight = i+1;
				}
				if(inLineCounter>=4) {
					straightDetected = true;
					highestValueOfStraight = cardAsValue.get(highestPosOfStraight);
					maxpos = i;
				}
				
				if(!foundConsecutive) {
					inLineCounter = 0;
				}	
			}
			
			if(straightDetected) {
				evaluation = "straight;"+highestValueOfStraight;	
			}
			
			if(flushDetected) {
				evaluation = flushstore;
			}
			if(pair && toak) {
				evaluation = "fullhouse;"+cards[position].getValue();
			}
			
			if(foak) {
				evaluation = "foak;"+foakstore;
			}
			
			if(straightDetected && flushDetected) {
				//straight flush detection;
				ArrayList<Integer> straightMembers = new ArrayList<Integer>();
					for(int i=maxpos; i!=maxpos-5; i--) {
						straightMembers.add(cardAsValue.get(i));
					}
					System.out.println(straightMembers.toString());
					System.out.println(membersOfFlush.toString());
					boolean straightflush=true;
					for(int i =0; i<5; i++) {
						if(!membersOfFlush.contains(straightMembers.get(i))) {
							straightflush = false;
						}
					}
					if(straightflush) {
						evaluation = "straightflush;"+highestValueOfStraight;	
					}
			}
		return evaluation;
	}
	private String raiseAlg(int seed, int raise) {
		Random rng = new Random();
		int returnraise = rng.nextInt((int)(seed*agro*2+baserate));
		if((returnraise+raise)<this.balance) {
			if(!(this.previousGamestate == gamestate)) {
				previousGamestate = gamestate;
				return "r;"+(returnraise+raise);
				
			}else {
				return "c;0";
			}
		}else {
			if(!(this.previousGamestate == gamestate)) {
				previousGamestate = gamestate;
				return "r;"+this.balance;
			}else {
				return "c;0";
			}
		}
	}
	
	public String ask(ArrayList<Card> table, int pot, int raise, int gamestate) {

		Random rng = new Random();
		
		this.gamestate = gamestate;
		
		//pair
		//toak
		//foak
		//straight
		//flush
		//straightflush
		
		switch(table.size()) {
		case 0:
			//pre flop behaviour
			if(raise==0) {
				if(this.card1 == this.card2) {
						return raiseAlg(50, raise);
				}else {
					if(this.card1.getValue() + this.card2.getValue() > 20) {
						return raiseAlg(25, raise);
					}else {	
						if(rng.nextInt(100) <(agro*50)) {
							return raiseAlg(25, raise);	
						}
						return "c;0";
					}
				}
			}
			//needs to at least call
			else if(raise > 0) {
				if((this.card1.getValue() + this.card2.getValue()) >24) {
					return raiseAlg(10, raise);
				}else if((this.card1.getValue() + this.card2.getValue()) >(3*(1-agro))){
					return "c;0";
				}
				else {
					if(this.isallin()) {
						return "c;0";
					}else {
						return "f;0";
					}
				}
			}else {
				return "c;0";
			}
			
		default:
			//flop, river, turn, behaviour
			String[] breakdown = new String[3];
			String eval = evaluateHand(table);
			
			//high
			breakdown = eval.split(";");
			
			if(breakdown[0].equals("high")) {
				if(Integer.parseInt(breakdown[1]) > 10*(0.5-agro)) {
					if(raise==0) {
						return "c;0";
					}else if (raise>0) {
						if(raise>balance*0.5*agro) {
							if(this.isallin()) {
								return "c;0";
							}else {
								return "f;0";
							}
						}else {
								return "c;0";
						}
					}else {
						return "c;0";
					}
				}
			}
			
			//pairs			
			else if (breakdown[0].equals("pair")){
				return makeChoiceCombos(0.21, breakdown, raise);
			}else if (breakdown[0].equals("toak")){
				return makeChoiceCombos(0.55, breakdown, raise);
			}else if (breakdown[0].equals("foak")){
				return makeChoiceCombos(0.77, breakdown, raise);
			}
			//straight
			
			else if(breakdown[0].equals("straight")) {
				if(Integer.parseInt(breakdown[1]) > 2*(1-agro)) {
					return makeChoiceCombos(0.85, breakdown, raise);
				}else {
					return makeChoiceCombos(0.77, breakdown, raise);
				}
			}
			//flush
			else if(breakdown[0].equals("flush")) {
				if(Integer.parseInt(breakdown[1]) > 2*(1-agro)) {
					return makeChoiceCombos(0.93, breakdown, raise);
				}else {
					return makeChoiceCombos(0.89, breakdown, raise);
				}
			}
			
			//straightflush
				
			else if(breakdown[0].equals("straightflush")) {
				if(Integer.parseInt(breakdown[1]) > 2*(1-agro)) {
					makeChoiceCombos(0.99, breakdown, raise);
				}else {
					makeChoiceCombos(0.95, breakdown, raise);
				}
			}
			//bluff
				
		}
		
		if(raise == 0) {
			return "c;0";
		}else {
			if(this.isallin()) {
				return "c;0";
			}else {
				return "f;0";
			}
		}
		
	}
	
	
	private String makeChoiceCombos(double seed, String[] breakdown, int raise) {
		
		Random rng = new Random();
		
		double prob = rng.nextDouble();
		//raise criteria
		if(seed>prob) {
			int makeraise = (int)(baserate*agro*(rng.nextDouble()));
			if(!(this.previousGamestate == gamestate)) {
				previousGamestate = gamestate;
				if(raise+makeraise<this.balance) {
					return "r;"+(raise+makeraise);
				}else {
					return "r;"+this.balance;
				}
			}else {
				return "c;0";
			}
		}
	  else if(raise == 0) {
					return "c;0";
	  }
	  else if(raise < this.balance*agro) {
					return "c;0";
	  }
	else {
		if(this.isallin()) {
			return "c;0";
		}else {
			return "f;0";
		}
	}
	}
	
	
	public String getName() {
		return this.name;
	}
	
	public int getBalance() {
		return this.balance;
	}
	
	
	public void deductBalance(int amount){
		if(amount>balance) {
			System.out.println("amount too great, amount not deducted");
		}else if(amount<0){
			System.out.println("amount ["+amount+"] is negative, amount not deducted");
		}
		else {
			this.balance -= amount;
		}

	}
	
	public boolean isBankrupt() {
		return this.financialStatus;
	}
	public void setBankrupt(boolean status) {
		this.financialStatus = status;
	}
	public void creditBalance(int amount) {
		this.balance += amount;
	}
	
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
public boolean isPlaying() {
		return this.playing;
	}

	
	public String hand2string() {
		
		return this.card1.toString()+" / "+this.card2.toString();
	}

}
