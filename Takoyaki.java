import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Takoyaki {

    static class Card {
        String type;
        int value;

        public Card(String type, int value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return type + " (" + value + ")";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create deck of cards
        List<Card> deck = createDeck();

        // Shuffle the deck
        Collections.shuffle(deck);

        // Distribute cards to players
        List<Card> playerHand = new ArrayList<>();
        List<Card> computerHand = new ArrayList<>();
        distributeCards(deck, playerHand, computerHand);

        System.out.println("Welcome to Takoyaki Card Game!");
        System.out.println("Your hand:");
        printHand(playerHand);

        // Game loop
        while (!deck.isEmpty()) {
            System.out.println("Choose a card to play (enter index): ");
            int choice = scanner.nextInt();

            if (choice < 0 || choice >= playerHand.size()) {
                System.out.println("Invalid choice! Try again.");
                continue;
            }

            Card playedCard = playerHand.remove(choice);
            System.out.println("You played: " + playedCard);

            // Simulate computer's move
            if (!computerHand.isEmpty()) {
                Card computerCard = computerHand.remove(0);
                System.out.println("Computer played: " + computerCard);
            }

            // Display updated hands
            System.out.println("Your hand:");
            printHand(playerHand);
            System.out.println("Deck size: " + deck.size());
        }

        System.out.println("Game over!");
        scanner.close();
    }

    private static List<Card> createDeck() {
    List<Card> deck = new ArrayList<>();
    String[] types = {"Regular", "Special", "Bonus"};
    Random random = new Random();

    for (String type : types) {
        for (int i = 1; i <= 10; i++) { // Card values 1-10
            deck.add(new Card(type, random.nextInt(20) + 1)); // Random values for cards
        }
    }
    return deck;
}

private static void distributeCards(List<Card> deck, List<Card> playerHand, List<Card> computerHand) {
    for (int i = 0; i < 5; i++) { // Distribute 5 cards each
        playerHand.add(deck.remove(0));
        computerHand.add(deck.remove(0));
    }
}

private static void printHand(List<Card> hand) {
    for (int i = 0; i < hand.size(); i++) {
        System.out.println(i + ": " + hand.get(i));
    }
}

    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        String[] types = {"Regular", "Special", "Bonus"};
        Random random = new Random();

        for (String type : types) {
            for (int i = 1; i <= 10; i++) { // Card values 1-10
                deck.add(new Card(type, random.nextInt(20) + 1)); // Random values for cards
            }
        }
        return deck;
    }

    private static void distributeCards(List<Card> deck, List<Card> playerHand, List<Card> computerHand) {
        for (int i = 0; i < 5; i++) { // Distribute 5 cards each
            playerHand.add(deck.remove(0));
            computerHand.add(deck.remove(0));
        }
    }

    private static void printHand(List<Card> hand) {
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + ": " + hand.get(i));
        }
    }
}