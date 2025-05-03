import java.util.Scanner;
import java.util.Random;

public class Trash {
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
}