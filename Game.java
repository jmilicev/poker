
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

public class Game extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	MyDrawPanel myPanel;
	
	JButton DEALBUTTON;
    JButton CALLBUTTON;
    JButton RAISEBUTTON;
    JButton FOLDBUTTON;
    
    JLabel TITLE;
    JLabel BALANCE;
    JTextField RAISEENTRY;
    
	 Player[] aiPlayers = new Player[3];
	 Player player;
	 
	 final String gameTitle = "Press deal to start game";
    
    Deck deck;

    int turn;
    int maxbet = 0;
    int workspaceWidth=1080;
    int workspaceHeight=720;
    final int STARTING_BALANCE = 1000;
    
    int[] gamestate = new int[10];
    /* 0 - drawstate
     * 1 - balance
     * 2 - raise;
     * */
    String[] chatHolder = new String[4];
    
    ArrayList<Card> table = new ArrayList<Card>();
    boolean holdingForPlayer = false;

	public Game(){
		makeBoard();
	}
	
	public void makeBoard() {
		
		for(int i = 0; i<4; i++) {
			gamestate[i] = 0;
			chatHolder[i] = "";
		}
		
		final int NUMBER_OF_BUTTONS = 4;
		JPanel MainPanel = new JPanel();
	    JPanel TitlePanel = new JPanel();
	    
	    String name = "Default Player";
	    
	    Image icon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/recources/programicon.ico"));
	    
	    this.setIconImage(icon);
	    this.setTitle("Texas Speed-Hold 'em by Jovan");
	    
	    player = new Player(name, STARTING_BALANCE, 0);
	    
	     TITLE = new JLabel(gameTitle);
	    
	    BALANCE = new JLabel("Balance : "+player.getBalance());
	    RAISEENTRY= new JTextField("Insert Raise");
	    
	    ArrayList<JButton> Buttons = new ArrayList<JButton>();
	    
	    DEALBUTTON = new JButton(" DEAL ");
	    CALLBUTTON = new JButton(" CALL ");
	    RAISEBUTTON = new JButton(" RAISE ");
	    FOLDBUTTON = new JButton(" FOLD ");
	    
	    Buttons.add(DEALBUTTON);
	    Buttons.add(CALLBUTTON);
	    Buttons.add(RAISEBUTTON);
	    Buttons.add(FOLDBUTTON);
	    
	    TITLE.setForeground(Color.black);
	    TITLE.setBackground(Color.black);
	    
	    TitlePanel.add(TITLE);
	    
	    MainPanel.add(BALANCE);
	    
	    for(int i=0; i<NUMBER_OF_BUTTONS;i++) {
	    	MainPanel.add(Buttons.get(i));
	    	(Buttons.get(i)).addActionListener(this);
	    	
	    	if(Buttons.get(i) != DEALBUTTON) {
	    		(Buttons.get(i)).setEnabled(false);
	    	}
	    }
	    MainPanel.add(RAISEENTRY);
	    
	    this.setLayout(new BorderLayout());
	    this.setVisible(true);
	    
	    myPanel=new MyDrawPanel();
	    
	    this.add(TitlePanel,BorderLayout.NORTH);
	    this.add (myPanel,BorderLayout.CENTER);
	    this.add(MainPanel,BorderLayout.SOUTH);
	    
	    Random rng = new Random();
	    
		 aiPlayers[0] = new Player("John", STARTING_BALANCE, rng.nextDouble());
		 aiPlayers[1] = new Player("Dennis", STARTING_BALANCE, rng.nextDouble());
		 aiPlayers[2] = new Player("George", STARTING_BALANCE, rng.nextDouble());
	    
	    repaint();   
	    
	    RAISEENTRY.setPreferredSize(new Dimension(75,20));

	    this.setSize(workspaceWidth,workspaceHeight);
	    this.setVisible(true);
	    this.setResizable(false);
	    
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    name = JOptionPane.showInputDialog(MainPanel,"Please enter your name:", null);
	    int tutorial = JOptionPane.showConfirmDialog(
	            null,
	            "Hello "+name+", Would you like to view the tutorial?",
	            "INTRODUCTION",
	            JOptionPane.YES_NO_OPTION);
	    
	    if(tutorial == JOptionPane.YES_OPTION) {
	    	playTutorial();
	    }
	    
	}
	
	public void playTutorial() {
		JOptionPane.showMessageDialog(null, "You can begin playing a hand by pressing the deal button at any time" , "TUTORIAL: INTRO", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "When pressed, the deal button will deal out 2 cards for you, as well as 2 cards for 3 computer controlled players" , "TUTORIAL: INTRO", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "25 credits will automatically be deducted from each players account, this is called the \"buy in\"", "TUTORIAL: INTRO", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "The objective of the game is to make strong pairs with the center cards, and maximizing profits by strategically betting when your hand is strong" , "TUTORIAL: INTRO", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "The types of combinations can be viewed here: https://www.cardschat.com/poker-hands/ " , "TUTORIAL: INTRO", JOptionPane.INFORMATION_MESSAGE);	    	JOptionPane.showMessageDialog(null, "During each round, you will have the ability to raise, call, or fold." , "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "If you think your hand is strong, you can raise the bet by entering in a number to raise in the text field, and then by pressing the raise button." , "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "To continue playing, other players must match your raise by \"calling\" the raise.", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "A call means that you are willing to match the raise of another player, the amount of the raise will be deducted from your balance and you will be allowed to continue playing", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "If your hand is particularly weak, an alternative option is to fold.", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "Folding means you are no longer participating in the game, you forfiet all of your bets made up to this point, and the game continues without you.", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "If no player makes a raise, the player may continue without raising the stakes of the round, this is called a \"check\", which will be done automatically via the call button if applicable.", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "Every player can only raise once per round, so choose wisely.", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "Players decide their action in clockwise order, and when the stakes are settled, additional cards are revealed", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "For every betting round, additional cards are released. These new cards must be taken into consideration when making your decisions", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "After the first round, 3 cards are released, called the flop. 1 card is revealed per round after that, untill a total of 5 face up cards are on the table", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "Once the final betting round is completed (i.e. the round where 5 cards are face up on the table), all players still in the game reveal their cards and the winner is revealed", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "The player with the strongest hand will be awarded the entirety of the pot,  and the other players will have lost their stakes", "TUTORIAL: PLAYING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "To win, you must succesfully bankrupt the 3 other players, this is done by ensuring they can not meet the buy in requirement of 25 credits", "TUTORIAL: ENDING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "Once the balance of all three players is below 25, the game is won, and you may choose to reset the game", "TUTORIAL: ENDING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "If you however end up bankrupt, the game is lost, and you can press reset to play again.", "TUTORIAL: ENDING THE GAME", JOptionPane.INFORMATION_MESSAGE);
    	JOptionPane.showMessageDialog(null, "That is all for the tutorial, If need be, you can repeat it at any time by restarting the program.", "TUTORIAL: ENDING THE GAME", JOptionPane.INFORMATION_MESSAGE);	
	}
	
	public void playoutGame(boolean bypassAi){
		
		if(turn != 99) {
			turn = 0;
		}
		boolean equalstakes = false;
		
		int allfold = 0;
		for(int i = 0; i<3; i++) {
			if(!aiPlayers[i].isPlaying()) {
				allfold++;
			}
		}
		if(allfold == 3) {
			gamestate[0] = 5;
			for(int i=table.size(); i<=5; i++) {
				table.add(deck.Pop());
			}
			holdingForPlayer = true;
			equalstakes = true;
			turn = 99;
			payoutWinners(findWinner());
		}
		
		while( ((!equalstakes && !holdingForPlayer) || turn<3) && turn != 99) {
			System.out.println("turn = "+turn);
			setMaxBet();
			
			if(aiPlayers[turn].isBankrupt()) {
				aiPlayers[turn].setPlaying(false);
			}
			
			gamestate[2] = 0;
			if(aiPlayers[turn].isPlaying()) {
				String choice = aiPlayers[turn].ask(table, gamestate[1], maxbet-aiPlayers[turn].getStake(), gamestate[0]);
				String[] breakdown = choice.split(";");

				if(breakdown[0].equals("c")) {
					
					boolean bypass = false;
					
					if(maxbet-aiPlayers[turn].getStake() < aiPlayers[turn].getBalance()) {
						
						if(maxbet > aiPlayers[turn].getStake()) {
							aiPlayers[turn].deductBalance(maxbet-aiPlayers[turn].getStake());
							gamestate[1] += maxbet-aiPlayers[turn].getStake();
							aiPlayers[turn].addStake(maxbet-aiPlayers[turn].getStake());
						}
					}
					else if(aiPlayers[turn].isallin()) {/*do nothing*/}
					//go all in
					else {
						aiPlayers[turn].addStake(aiPlayers[turn].getBalance());
						gamestate[1] += aiPlayers[turn].getBalance();
						aiPlayers[turn].allin(true);
						aiPlayers[turn].deductBalance(aiPlayers[turn].getBalance());
						chatHolder[turn] = " All in";
					}
					
					if((maxbet==0 || bypass) && !aiPlayers[turn].isallin()) {
						chatHolder[turn] = " Check";
					}
					else if(!aiPlayers[turn].isallin()) {
							chatHolder[turn] = " Called to "+(maxbet);
					}else {
						chatHolder[turn] = " All in";
					}
				}

				else if(breakdown[0].equals("r")) {
					
					playSound(4);
					
					gamestate[2] = Integer.parseInt(breakdown[1]);
					if(gamestate[2]==aiPlayers[turn].getBalance()) {
						aiPlayers[turn].allin(true);
					}		
					aiPlayers[turn].deductBalance(gamestate[2]);
					gamestate[1] += gamestate[2];
					aiPlayers[turn].addStake(gamestate[2]);
					if(!aiPlayers[turn].isallin()) {
							chatHolder[turn] = " Raised to "+ aiPlayers[turn].getStake();
					}else {
						chatHolder[turn] = " All in";
					}
					
				}
				else if(breakdown[0].equals("f")){
					aiPlayers[turn].setPlaying(false);
					chatHolder[turn] = " Folded";
				}else {
					System.out.println("Critical System Error, please immediately restart game");
				}
			}
			
			repaint();
			turn++;
			
			ArrayList<Player> playingPlayers = new ArrayList<Player>();
			
			int allinplayercount = 0;
			for(int i =0; i<3; i++) {
				if(aiPlayers[i].isPlaying()) {
					playingPlayers.add(aiPlayers[i]);
					if(aiPlayers[i].isallin()) {
						allinplayercount++;
					}
				}
			}
			equalstakes = true;
			for(int i =0; i<playingPlayers.size(); i++) {		
				if( !(playingPlayers.get(i).getStake() == maxbet) && (!playingPlayers.get(i).isallin())) {
					equalstakes = false;
				}
			}

			equalstakes = (player.getStake() == maxbet) ? equalstakes : false;
			setMaxBet();
			
			
			if(allinplayercount == playingPlayers.size() && turn > 2) {
				gamestate[0] = 5;
				for(int i=table.size(); i<5; i++) {
					table.add(deck.Pop());
				}
				holdingForPlayer = true;
				equalstakes = true;
				turn = 99;
				payoutWinners(findWinner());
			}
			
			if(turn>2 && !player.isPlaying()) {
				
				for(int i=0; i<3; i++) {aiPlayers[i].resetStake();}
				
				player.resetStake();

				gamestate[0]++;
				gamestate[2] = 0;
				
				if(gamestate[0]==2) {
					TITLE.setText("The Flop");
				}else if(gamestate[0]==3) {
					TITLE.setText("The Turn");
				}else if(gamestate[0]==4) {
					TITLE.setText("The River");
				}
				
				repaint();
				turn = 0;
				askPlayer();
				playoutGame(false);	
				
			}else if(turn>2 && (equalstakes)) {
				
				for(int i=0; i<3; i++) {aiPlayers[i].resetStake();}
				
				player.resetStake();
				RAISEBUTTON.setEnabled(true);
				
				playSound(0);
				
				gamestate[0]++;
				gamestate[2] = 0;
				
				if(gamestate[0]==2) {
					TITLE.setText("The Flop");
				}else if(gamestate[0]==3) {
					TITLE.setText("The Turn");
				}else if(gamestate[0]==4) {
					TITLE.setText("The River");
				}
	
				repaint();
				askPlayer();
				holdingForPlayer = true;
				
			}else if(turn>2) {
				askPlayer();
				holdingForPlayer = true;
				}
			}
		}
	
	public void askPlayer() {
		
		
		if(player.isPlaying()) {
			CALLBUTTON.setEnabled(true);
			FOLDBUTTON.setEnabled(true);
		}
		
		if(gamestate[0]==2 && table.size() == 0) {
			for(int i=0;i<3;i++) {
				table.add(deck.Pop());
			}
		}
		
		else if(gamestate[0]==3 && table.size() == 3) {
			table.add(deck.Pop());
		}
		
		else if(gamestate[0]==4 && table.size() == 4) {
			table.add(deck.Pop());
		}
		
		else if(gamestate[0]==5 && table.size() == 5) {
			holdingForPlayer = true;
			turn = 99;
			CALLBUTTON.setEnabled(false);
			RAISEBUTTON.setEnabled(false);
			FOLDBUTTON.setEnabled(false);
			DEALBUTTON.setEnabled(true);
			
			Player winner = findWinner();
			payoutWinners(winner);
		}
		
		chatHolder[3] = hand2display(player.evaluateHand(table));
		
	}
	
	public void payoutWinners(Player winner) {
		winner.creditBalance(gamestate[1]);
		TITLE.setText(winner.getName()+" wins the pot of "+gamestate[1]+"!");
		CALLBUTTON.setEnabled(false);
		RAISEBUTTON.setEnabled(false);
		FOLDBUTTON.setEnabled(false);
		DEALBUTTON.setEnabled(true);
		
	}
	
	public String hand2display(String hand) {
		
		String displayer;
		
		String[] hands = hand.split(";");
		
		
		switch(hands[0]) {
		
		case "high":
			displayer = Card.numToCard(Integer.parseInt(hands[1]))+" High";
			break;
		case "pair":
			displayer = "Pair of "+Card.numToCard(Integer.parseInt(hands[1]))+"s";
			break;
		case "twopair":
			displayer = "Two Pairs";
			break;
		case "toak":
			displayer = "Three of a Kind ";
			break;		
		case "foak":
			displayer = "Four of a Kind";
			break;
		case "straightflush":
			displayer = "Straight Flush";
			break;
		case "fullhouse":
			displayer = "Full House";
			break;
		default:
			displayer = hands[0].substring(0,1).toUpperCase() + hands[0].substring(1);
		}
		
		return displayer;
	}
	
	public Player findWinner() {
		
		holdingForPlayer = true;
		turn = 99;
	
		chatHolder[3] = hand2display(player.evaluateHand(table));
	
		int[] scores = new int[4];
		
		/* 
		 * point score
		 * 1-15 - high card
		 * 101-15 - pair
		 * 200-215- twoak
		 * 300-315 - foak
		 * 400-415 - straight
		 * 500-515 - flush
		 * 600-615 - straight flush
		 */
		
		for(int i=0; i<4; i++) {
			
			String[] values = new String[5];
			
			if(i<3) {
			String value = aiPlayers[i].evaluateHand(table);
			values = value.split(";");
			
			if(aiPlayers[i].isPlaying()) {
				chatHolder[i] = hand2display(value);
			}
			
			}else {
				String value =player.evaluateHand(table);
				values = value.split(";");
				chatHolder[3] = hand2display(value);
			}
			
			switch(values[0]) {
			case "high":
				scores[i] = 0;
				scores[i] += Integer.parseInt(values[1]);
				break;
				
			case "pair":
				scores[i] = 100;
				scores[i] += Integer.parseInt(values[1]);
				break;
				
			case "twopair":
				scores[i] = 200;
				scores[i] += Integer.parseInt(values[1]);
				break;
				
			case "toak":
				scores[i] = 300;
				scores[i] += Integer.parseInt(values[1]);
				break;
			
			case "straight":
				scores[i] = 400;
				scores[i] += Integer.parseInt(values[1]);
				break;
				
			case "flush":
				scores[i] = 500;
				scores[i] += Integer.parseInt(values[1]);
				break;
				
			case "fullhouse":
				scores[i] = 600;
				scores[i] += Integer.parseInt(values[1]);
				break;
				
			case "foak":
				scores[i] = 700;
				scores[i] += Integer.parseInt(values[1]);
				break;
			
			case "straightflush":
				scores[i] = 800;
				scores[i] += Integer.parseInt(values[1]);
				break;
			}
		}
			int maxscore = 0;
			int scoreatpos = -1;
			
			for(int i=0; i<3; i++) {
				if(((scores[i] > maxscore) && aiPlayers[i].isPlaying())) {
						maxscore = scores[i];
						scoreatpos = i;
				}
			}
			//evading arrayoutofbounds for i=4 above
			if(scores[3] > maxscore && player.isPlaying()) {
				maxscore = scores[3];
				scoreatpos = 4;
			}
			
			
			
			int tiedetected = 0;
			int[] tieparticipants = new int[4];
			
			for(int i=0; i<4; i++) {
				tieparticipants[i] = 0;
				if(scores[i] == maxscore) {
					tieparticipants[i] = 1;
					tiedetected++;
				}
			}
			
			//tie has been detected
			
			ArrayList<Integer> tieresponses = new ArrayList<Integer>();
			
			if(tiedetected>1) {
				ArrayList<Card> blanktable = new ArrayList<Card>();
				for(int i=0; i<3; i++) {
					if(tieparticipants[i] == 1 && aiPlayers[i].isPlaying()) {
						
						String hand = aiPlayers[i].evaluateHand(blanktable);
						String[] value = hand.split(";");
						int intvalue = Integer.parseInt(value[1]);
						
						tieresponses.add(intvalue);
					}
				}
				
				if(tieparticipants[3] == 1 && player.isPlaying()) {
					String hand = player.evaluateHand(blanktable);
					String[] value = hand.split(";");
					int intvalue = Integer.parseInt(value[1]);
					tieresponses.add(intvalue);
				}
				
				Collections.sort(tieresponses);
				
				int highesthigh = tieresponses.get(tieresponses.size()-1);
				
				int winpos = -1;
				for(int i=0; i<4; i++) {
					
					if(i<3) {
						String hand = aiPlayers[i].evaluateHand(blanktable);
						String[] value = hand.split(";");
						int intvalue = Integer.parseInt(value[1]);
						
						if(highesthigh == intvalue) {
							winpos = i;
						}
					}
					else {
						String hand = player.evaluateHand(blanktable);
						String[] value = hand.split(";");
						int intvalue = Integer.parseInt(value[1]);
						if(highesthigh == intvalue) {
							winpos = i;
						}
					}
					
				}
				
	
				
				scoreatpos = winpos;

			}
			
			
			if(scoreatpos<3) {
				return aiPlayers[scoreatpos];
			}else {
				return player;
			}
	}
	
	public void setMaxBet() {
		maxbet = 0;
		for(int i=0; i<3;i++) {
			if(aiPlayers[i].getStake() > maxbet) {
				maxbet = aiPlayers[i].getStake();
			}
		}
		maxbet = (player.getStake() > maxbet) ? player.getStake() : maxbet;
	}
	
	public void playSound(int soundRequested) {
	    
	    String path = "/recources/";
	    
	    
	    switch(soundRequested) {
	    case 0:
	    	path  += "flip.wav";
	    	break;
	    case 1:
	    	path  += "shuffle.wav";
	    	break;
	    case 2:
	    	path += "sadchime.wav";
	    	break;
	    case 3:
	    	path += "win.wav";
	    	break;
	    case 4:
	    	path +="coin.wav";
	    }
	    
	    Sound.play(path);
	    }
	    

	public void actionPerformed(ActionEvent e)
	  {
		if(e.getSource()==DEALBUTTON){ 
		
			boolean aiBankrupt = true;
			for(int i=0; i<3;i++) {
				if(aiPlayers[i].getBalance() >= 25) {
					aiBankrupt = false;
				}else {
					aiPlayers[i].setBankrupt(true);
				}
			}
			if(aiBankrupt) {
				TITLE.setText("You have bankrupted all the other players! You win the game!");
				playSound(3);
				DEALBUTTON.setText("Reset");
				for(int i=0; i<3;i++) {
				aiPlayers[i].creditBalance(25);
				}
			}
			else {
				if(DEALBUTTON.getText().equals("Reset")) {
					for(int i=0; i<3;i++) {
						aiPlayers[i].deductBalance( aiPlayers[i].getBalance());
						aiPlayers[i].creditBalance(STARTING_BALANCE);
						aiPlayers[i].setBankrupt(false);
						}
					player.deductBalance(player.getBalance());
					player.creditBalance(STARTING_BALANCE);
				}

				DEALBUTTON.setText("Deal");
				final int BUYIN = 25;
				
				if(player.getBalance() > BUYIN) {
					playSound(1);
					
				DEALBUTTON.setEnabled(false);
	
				CALLBUTTON.setEnabled(true);
				RAISEBUTTON.setEnabled(true);
				FOLDBUTTON.setEnabled(true);
				
				TITLE.setText("Pre-Flop");
				gamestate[0] = 1;
				gamestate[1] = 0;
				gamestate[2] = 0;
				gamestate[3] = 0;
				
				 deck = new Deck();
				 deck.Shuffle();
				
				 for(int i=0; i<3; i++) {
					 aiPlayers[i].setCards(deck.Pop(), deck.Pop());
					 chatHolder[i] = "";
					 aiPlayers[i].allin(false);
					 
					 if(aiPlayers[i].getBalance() > BUYIN) {
						 aiPlayers[i].setPlaying(true);
						 aiPlayers[i].deductBalance(BUYIN);
						 aiPlayers[i].resetStake();
						 aiPlayers[i].allin(false);
						 gamestate[1] += BUYIN;
					 }
				 }
				 player.setPlaying(true);
				 player.resetStake();
				 player.allin(false);
				 player.deductBalance(BUYIN);
				 gamestate[1] += BUYIN;
	
				 player.setCards(deck.Pop(), deck.Pop());
				 
				 table = new ArrayList<Card>();
				chatHolder[3] = hand2display(player.evaluateHand(table));
				
				 repaint();
				}else {
					playSound(2);
					DEALBUTTON.setEnabled(true);
					DEALBUTTON.setText("Reset");
					CALLBUTTON.setEnabled(false);
					RAISEBUTTON.setEnabled(false);
					FOLDBUTTON.setEnabled(false);
					TITLE.setText("Game Over : You lack the sufficient balance for buy in ($25)");
				}
			}
		}
		else if (e.getSource()==CALLBUTTON){ 
			
			//convert balance to pay raise and add to pot
			setMaxBet();
			if(maxbet-player.getStake()>0) {
					player.deductBalance(maxbet-player.getStake());
					gamestate[1] += maxbet-player.getStake();
					player.addStake(maxbet-player.getStake());
			}
			
			turn = 0;
			holdingForPlayer = false;
			playoutGame(false);
			
	    }
		else if(e.getSource()==RAISEBUTTON){ 
			
			RAISEBUTTON.setEnabled(false);
			
			try {
			gamestate[2] = Integer.parseInt(RAISEENTRY.getText());
			if(gamestate[2] >= player.getBalance()) {
				if(gamestate[2] > player.getBalance()) {
					JOptionPane.showMessageDialog(myPanel, "The bet you entered is larger then your balance, going all in instead.", "Error", 0, null);
				}
				player.addStake(player.getBalance());
				gamestate[1] += player.getBalance();
				player.deductBalance(player.getBalance());
				player.allin(true);
			}else {
				player.deductBalance(gamestate[2]);
				player.addStake(gamestate[2]);
				gamestate[1] += gamestate[2];
			}
			
			setMaxBet();
			
			turn = 0;
			holdingForPlayer = false;
			playoutGame(false);
			}
			catch(Exception r) {
				gamestate[2] = 0;
				JOptionPane.showMessageDialog(myPanel, "You did not enter a valid bet, please try again.", "Error", 0, null);
				RAISEBUTTON.setEnabled(true);
				
			}
		}
		else if(e.getSource()==FOLDBUTTON){ 
			
			CALLBUTTON.setEnabled(false);
			RAISEBUTTON.setEnabled(false);
			FOLDBUTTON.setEnabled(false);
			
			player.setPlaying(false);
			
			holdingForPlayer = false;
			turn = 0;
			playoutGame(false);

		}
	}

class MyDrawPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
		public void paintComponent(Graphics g) {
			
			BALANCE.setText("Balance : "+player.getBalance());
			
			Graphics2D pane = (Graphics2D)g;
			pane.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			BufferedImage cards = null;
			BufferedImage bg = null;
			
			 double scale = workspaceWidth/1080;
			
		    try {
		       cards = ImageIO.read(this.getClass().getResource("/recources/cards.gif"));
		       bg = ImageIO.read(this.getClass().getResource("/recources/table.jpg"));
		    } 
		    catch (IOException e) {
		      System.out.println("Files not detected, please check cards.gif & table.jpg and try again.");
		    }
			pane.drawImage(bg, 0,0,myPanel.getWidth(),myPanel.getHeight(),null);
			
			//Card test = new Card("Heart", "King");
			//int[] map = test.computeDrawingData(myPanel.getWidth()/2, myPanel.getHeight()/2,1.0);
			
			//draw table cards
			int tablex = (int) (workspaceWidth/2*0.89);
			int tabley = (int) (workspaceHeight/2*0.6);
			
			int[] map;
			
			//draw players
			if(gamestate[0] != 0) {
				pane.setColor(Color.white);
			
				if(!aiPlayers[0].isBankrupt()) {
					pane.drawString(aiPlayers[0].getName()+" : "+aiPlayers[0].getBalance(), (int)(tablex*1.78), (int)(tabley*0.98));
					for(int i=(int)(tablex/2*1.4);  i<(tablex/2*1.4)+72; i+=36) {
						//right
						map = Card.drawBackside(tablex+i, tabley, scale);
						pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
					}
				}
				if(!aiPlayers[1].isBankrupt()) {
					pane.drawString(aiPlayers[1].getName()+" : "+aiPlayers[1].getBalance(), (int)(tablex*1.02), (int)(tabley*0.12));
					for(int i=(int)(tablex/1.05);  i<(tablex/1.05)+71; i+=36) {
						//top
						map = Card.drawBackside(i, tabley-185, scale);
						pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
					}
				}
				if(!aiPlayers[2].isBankrupt()) {
					pane.drawString(aiPlayers[2].getName()+" : "+aiPlayers[2].getBalance(), (int)(tablex*0.32), (int)(tabley*0.98));
					for(int i=(int)(tablex/4.2);  i<tablex/4.2+71; i+=36) {
						//left
						map = Card.drawBackside(i, tabley, scale);
						pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
					}
				}
				
				//player chat window3
				pane.drawString(chatHolder[3], (int)(tablex*1.06), (int)(tabley*2.58));
				
				//player cards
				
				map = (player.getCard1()).computeDrawingData((int)(tablex/1.05), tabley+185, scale);			
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				map = (player.getCard2()).computeDrawingData((int)(tablex/0.95), tabley+185, scale);			
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				
				pane.drawString("Pot : "+gamestate[1], (int)(tablex*1.04), (int)(tabley*0.98));
				
				pane.drawString(chatHolder[0], (int)(tablex*1.81-chatHolder[0].length()), (int)(tabley*1.72));
				pane.drawString(chatHolder[1], (int)(tablex*1.056-chatHolder[1].length()), (int)(tabley*0.87));
				pane.drawString(chatHolder[2], (int)(tablex*0.33-chatHolder[2].length()), (int)(tabley*1.72));
	
			}
			
			//draw table all backside
			
			if(gamestate[0] == 1) {
				for(int i =-144 ; i<216; i+=72) {
					map = Card.drawBackside(i+tablex, tabley, scale);
					pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				}
			}if(gamestate[0] > 1) {
				
				map = (table.get(0)).computeDrawingData(-144+tablex, tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				
				map = (table.get(1)).computeDrawingData(-72+tablex, tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				
				map = (table.get(2)).computeDrawingData(0+tablex, tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				
				for(int i =72; i<216; i+=72) {
					map = Card.drawBackside(i+tablex, tabley, scale);
					pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				}
			}
			if(gamestate[0] > 2) {	
				
				map = (table.get(3)).computeDrawingData(72+tablex, tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				
				map = Card.drawBackside(144+tablex, tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
			}
			if(gamestate[0] > 3) {	
				
				map = (table.get(4)).computeDrawingData(144+tablex, tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
			}
			if(gamestate[0] > 4) {
				//draw all ai cards;
				if(!aiPlayers[0].isBankrupt()) {
				map = (aiPlayers[0].getCard1()).computeDrawingData((int)(tablex*1.7), tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				map = (aiPlayers[0].getCard2()).computeDrawingData((int)(tablex*1.7+36), tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				}if(!aiPlayers[1].isBankrupt()) {
				map = (aiPlayers[1].getCard1()).computeDrawingData((int)(tablex/1.05), tabley-185, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				map = (aiPlayers[1].getCard2()).computeDrawingData((int)(tablex/1.05+36), tabley-185, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				}if(!aiPlayers[2].isBankrupt()) {
				map = (aiPlayers[2].getCard1()).computeDrawingData((int)(tablex/4.2), tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				map = (aiPlayers[2].getCard2()).computeDrawingData((int)(tablex/4.2+36), tabley, scale);
				pane.drawImage(cards, map[0],map[1],map[2],map[3],map[4],map[5],map[6],map[7],null);
				}
			}
			
	}
}
}