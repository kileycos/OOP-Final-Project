// //this is silas doing his card game but not exactly knowing how to create it
// import java.util.Scanner;
// import java.util.Random;

// public class TakoyakiGame {
//     public static void main(String[] args) {
//         final int GRID_SIZE = 3; // 3x3 grid
//         int[][] takoyakiPan = new int[GRID_SIZE][GRID_SIZE];
//         boolean[][] marked = new boolean[GRID_SIZE][GRID_SIZE];
        
//         Random random = new Random();
//         Scanner scanner = new Scanner(System.in);

//         // Generate randoms for the grid
//         for (int i = 0; i < GRID_SIZE; i++) {
//             for (int j = 0; j < GRID_SIZE; j++) {
//                 takoyakiPan[i][j] = random.nextInt(10) + 1; // Numbers 1-10
//                 marked[i][j] = false; // Mark all unselected
//             }
//         }

//         // Instructions
//         System.out.println("Welcome to the Takoyaki Game!");
//         System.out.println("Select numbers to form a line (horizontal, vertical, or diagonal)!");
//         System.out.println("Grid:");

//         // Print initial grid
//         printGrid(takoyakiPan, marked);

//         boolean gameOver = false;

//         while (!gameOver) {
//             System.out.println("Enter the number you want to mark: ");
//             int choice = scanner.nextInt();

//             // Find and mark chosen number
//             boolean found = false;
//             for (int i = 0; i < GRID_SIZE; i++) {
//                 for (int j = 0; j < GRID_SIZE; j++) {
//                     if (takoyakiPan[i][j] == choice && !marked[i][j]) {
//                         marked[i][j] = true;
//                         found = true;
//                         break;
//                     }
//                 }
//             }

//             if (found) {
//                 System.out.println("Marked! Updated grid:");
//                 printGrid(takoyakiPan, marked);
//             } else {
//                 System.out.println("Number not found or already marked!");
//             }

//             // Check win condition
//             if (checkWin(marked)) {
//                 System.out.println("Congratulations! You've formed a line and won the game!");
//                 gameOver = true;
//             }
//         }
//         scanner.close();
//     }

//     public static void printGrid(int[][] grid, boolean[][] marked) {
//         for (int i = 0; i < grid.length; i++) {
//             for (int j = 0; j < grid[i].length; j++) {
//                 if (marked[i][j]) {
//                     System.out.print(" X "); // Marked slot
//                 } else {
//                     System.out.print(" " + grid[i][j] + " "); // Unmarked slot
//                 }
//             }
//             System.out.println();
//         }
//     }

//     public static boolean checkWin(boolean[][] marked) {
//         int size = marked.length;

//         // Check rows and columns
//         for (int i = 0; i < size; i++) {
//             if ((marked[i][0] && marked[i][1] && marked[i][2]) || 
//                 (marked[0][i] && marked[1][i] && marked[2][i])) {
//                 return true;
//             }
//         }

//         // Check diagonals
//         if ((marked[0][0] && marked[1][1] && marked[2][2]) || 
//             (marked[0][2] && marked[1][1] && marked[2][0])) {
//             return true;
//         }
//         return false;
//     }
// }
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
}