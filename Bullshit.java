import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Bullshit{
    private deck myDeck;
    private takingTurn turn;
    private ArrayList<ArrayList<card>> allHands;
    private Scanner scan = new Scanner(System.in);

    public Bullshit() {
        myDeck = new deck();
        turn = new takingTurn();
    }

    public void play() {
        myDeck.buildDeck("My Deck");
        myDeck.shuffle(myDeck.getCards());
        myDeck.deal(myDeck.getCards());
        allHands = myDeck.getAllHands();

        myDeck.printPlayerHand();

        while (!myDeck.gameOver()) {
            for (int i = 0; i < 4; i++) {
                if (i < 3) {
                    turn.botTurn(allHands.get(i), i + 1);
                    System.out.print("call bullshit(1 for yes 2 for no)?");
                    while(!(scan.hasNextInt())){
                        System.out.println("invalid");
                        System.out.print("please try again: ");
                    }
                    int choice = scan.nextInt();
                    if (choice == 1){
                        boolean lying = turn.checkBullshit();
                        if (lying) {
                            System.out.println("the bot was lying, they now have the pile");
                            allHands.get(i).addAll(turn.clearPile());
                        }
                        else {
                            System.out.println("nope, you have to pick up the pile");
                            myDeck.getPlayerHand().addAll(turn.clearPile());
                        }
                    }
                } else {
                    
                    turn.playerTurn(myDeck.getPlayerHand());
                    for (int b = 0; b < 3; b++) {
                        if (new Random().nextInt(4) == 0) {
                            System.out.println("Bot " + (b + 1) + " calls bullshit on you!");
                            boolean lying = turn.checkBullshit();
                            if (lying) {
                                System.out.println("You were lying! You pick up the pile.");
                                myDeck.getPlayerHand().addAll(turn.clearPile());
                                break;
                            } else {
                                System.out.println("You were telling the truth! Bot " + (b + 1) + " picks up the pile.");
                                allHands.get(b).addAll(turn.clearPile());
                                break;
                            }
                        }
                    }
                }

                if (myDeck.gameOver()) {
                    break;
                }
            }
        }

        for (int i = 0; i < allHands.size(); i++) {
            if (allHands.get(i).isEmpty()) {
                if (i == 3) {
                    System.out.println("Congrats, you win!");
                } else {
                    System.out.println("Sorry, you lost");
                    System.out.println("Bot " + (i + 1) + " wins");
                    System.out.println("Better luck next time");
                }
            }
        }
        scan.close();
    }
}

class card{
    private String suit;
    private int rank;

    public card(String suit, int rank){
        this.suit = suit;
        this.rank = rank;
    } 

    public int getRank(){
        return this.rank;
    }

    public String getSuit(){
        return this.suit;
    }
    
    public String toString(){
        String rankName = "null";
        if (rank == 11){
            rankName = "jack";
        }
        else if (rank == 12){
            rankName = "queen";
        }
        else if (rank == 13){
            rankName = "king";
        }
        else {
            rankName = String.valueOf(rank);
        }
        return ("[" + rankName + ", " + suit + "]");
    }
}

class deck{
    private String name;
    private ArrayList<card> deck = new ArrayList<>();

    public void buildDeck(String name) {
        this.name = name;
        deck.clear();

        int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
            
        for (String suit : suits) {
            for (int rank : ranks) {
                card newCard = new card(suit, rank);
                deck.add(newCard);
            }
        }
    }

    public void printDeck(){
        StringBuilder subDeck = new StringBuilder();
        for (card c : deck){
            subDeck.append(c.toString()).append(", ");
        }
        System.out.println(subDeck.toString());
    }

    public ArrayList<card> getCards(){
        return deck;
    }

    public void shuffle(ArrayList<card> deck){
        Collections.shuffle(deck);
    }

    private int numHands = 4;
    private ArrayList<ArrayList<card>> hands;
    private ArrayList<card> playerHand;

    public void deal(ArrayList<card> deck){
        hands = new ArrayList<>();
        for (int i = 0; i < numHands; i++){
            hands.add(new ArrayList<>());
        }
        playerHand = hands.get(3);
        int player = 0;
        while (!deck.isEmpty()){
            card dealt = deck.remove(0);
            hands.get(player).add(dealt);
            player = (player + 1) % numHands;
        } 
    }

    public void printPlayerHand(){
        System.out.println("your hand: ");
        StringBuilder subDeck = new StringBuilder();
        for (card c : playerHand){
            subDeck.append(c.toString()).append(", ");
        }
        System.out.println(subDeck.toString());
    }

    public ArrayList<card> getPlayerHand() {
        return playerHand;
    }

    public ArrayList<ArrayList<card>> getAllHands(){
        return hands;
    }

    public boolean gameOver(){
        for (ArrayList<card> hand : hands) {
            if (hand.isEmpty()){
                return true;
            }
        }
        return false;
    }
}


class takingTurn {
    private Scanner scan = new Scanner(System.in);
    private Random rando = new Random();
    private ArrayList<card> pile = new ArrayList<>();
    private ArrayList<card> lastPlayed = new ArrayList<>();
    private int lastPlayer = -1;
    private int currentRank = 1;

    public void playerTurn(ArrayList<card> playerHand) {
        System.out.println("current rank: " + getRankName(currentRank));
        System.out.println("Your hand: " + playerHand);

        System.out.println("how many cards would you like to play(1-4)?: ");
        int numToPlay = getValidInt(1,4);

        ArrayList<card> cardsToPlay = new ArrayList<>();
        for (int i = 0; i < numToPlay; i++) {
            System.out.print("enter the index of the card you with to play(1,2,3...): ");
            int index = getValidIndex(playerHand.size()) - 1;
            cardsToPlay.add(playerHand.remove(index));
            System.out.println("Your hand: " + playerHand);
        }

        System.out.println("playing " + numToPlay + " " + getRankName(currentRank) + "(s)");
        lastPlayed.clear();
        lastPlayed.addAll(cardsToPlay);
        lastPlayer = 3;
        pile.addAll(cardsToPlay);
        currentRank = (currentRank % 13) + 1;
    }

    private String getRankName(int rank){
        switch (rank){
            case 11: 
            return "jack";
            case 12: 
            return "queen";
            case 13: 
            return "king";
            default: return String.valueOf(rank);
        }
    }

    public void botTurn(ArrayList<card> botHand, int botNum){
        ArrayList<card> toPlay = new ArrayList<>();
        for (card c : botHand) {
            if (c.getRank() == currentRank){
                toPlay.add(c);
            }
        }

        if (toPlay.isEmpty()){
            int numToPlay = rando.nextInt(4) + 1;
            for (int i = 0; i < numToPlay && !botHand.isEmpty(); i++){
                int randIndex = rando.nextInt(botHand.size());
                toPlay.add(botHand.remove(randIndex));
            }
        }
        else{
            for (card c : toPlay) {
                botHand.remove(c);
            }
        }
        
        System.out.println("Bot " + botNum + " played " + toPlay.size() + " " + getRankName(currentRank) + "(s)");
        lastPlayed.clear();
        lastPlayed.addAll(toPlay);
        lastPlayer = botNum - 1;
        pile.addAll(toPlay);
        currentRank = (currentRank % 13) + 1;
    }

    public boolean checkBullshit() {
        for (card c : lastPlayed) {
            if (c.getRank()!= ((currentRank + 11) % 13 + 1)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<card> clearPile() {
        ArrayList<card> toReturn = new ArrayList<>(pile);
        pile.clear();
        return toReturn;
    }
    
    private int getValidInt(int min, int max) {
        while (!scan.hasNextInt()) {
            System.out.print("Invalid input. Try again: ");
            scan.next();
        }
        int num = scan.nextInt();
        while (num < min || num > max) {
            System.out.print("Out of range. Try again: ");
            num = scan.nextInt();
        }
        return num;
    }

    private int getValidIndex(int size) {
        while (!scan.hasNextInt()) {
            System.out.print("Invalid input. Try again: ");
            scan.next();
        }
        int index = scan.nextInt();
        while (index < 0 || index >= size) {
            System.out.print("Invalid index. Try again: ");
            index = scan.nextInt();
        }
        return index;
    }
}