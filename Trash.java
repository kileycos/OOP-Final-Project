import java.util.*;

public class Trash {

    public Trash(){
    }

    public double playTrash(double money){
        Scanner scanner = new Scanner(System.in);

        while (playerMoney >= 50) {
            System.out.println("\nYou have $" + playerMoney);
            System.out.println("Enter your bet (must be <= your balance, min $50 to play):");
            double betAmount = getValidBet(scanner, playerMoney);

            playerMoney -= 50; 
            System.out.println("Entry fee paid! You have $" + playerMoney + " left.");

            System.out.println("Choose a board size (10 or 12):");
            int boardSize = getValidInput(scanner, 10, 12);

            System.out.println("Choose a game mode: \n1. Normal (2x win) \n2. Timed Mode (4x win) \n3. Hard Mode (6x win) \n4. Impossible Mode (10x win)");
            int modeChoice = getValidInput(scanner, 1, 4);

            boolean playAgain;
            do {
                List<Card> deck = createDeck();
                Collections.shuffle(deck);

                List<Card> playerBoard = setupBoard(modeChoice == 4 ? 5 : boardSize);
                List<Card> computerBoard = setupBoard(modeChoice == 4 ? 5 : boardSize);
                List<Card> discardPile = new ArrayList<>();
                int rounds = 0;

                System.out.println("\nWelcome to the Trash Card Game!");
                printBoard(playerBoard);

                long startTime = System.currentTimeMillis();

                while (!isWinner(playerBoard) && !isWinner(computerBoard)) {
                    if (deck.isEmpty()) {
                        System.out.println("Deck is empty! Game ends in a draw.");
                        break;
                    }

                    rounds++;

                    System.out.println("\nRound " + rounds);
                    System.out.println("Discard pile: " + (discardPile.isEmpty() ? "Empty" : discardPile.get(discardPile.size() - 1)));
                    printBoard(playerBoard);

                    Card drawnCard = deck.remove(0);
                    System.out.println("\nYou drew: " + drawnCard);

                    if (drawnCard.value > 10) {
                        System.out.println("Card is too high! Automatically discarding.");
                        discardPile.add(drawnCard);
                    } else {
                        System.out.print("Do you want to **place** this card or **discard** it? (place/discard): ");
                        String choice = scanner.nextLine().trim().toLowerCase();
                        if (choice.equals("place") && canPlaceCard(drawnCard, playerBoard, playerBoard.size())) {
                            placeCard(drawnCard, playerBoard);
                        } else {
                            System.out.println("Card discarded.");
                            discardPile.add(drawnCard);
                        }
                    }

                    if (!deck.isEmpty()) {
                        Card computerCard = deck.remove(0);
                        System.out.println("\nComputer's Turn...");
                        System.out.println("Computer drew: " + computerCard);
                        
                    if (computerCard.value > 10) {
                        System.out.println("Computer discards the card.");
                        discardPile.add(computerCard);
                    } 
                    else if (canPlaceCard(computerCard, computerBoard, computerBoard.size())) {
                        placeCard(computerCard, computerBoard);
                        System.out.println("Computer places the card on its board.");
                    } 
                    else {
                        System.out.println("Computer discards the card.");
                        discardPile.add(computerCard);
                    }
                }
                    System.out.println("Your board progress: " + countPlacedCards(playerBoard) + "/" + playerBoard.size());
                    System.out.println("Computer's board progress: " + countPlacedCards(computerBoard) + "/" + computerBoard.size());
                }

                if (isWinner(playerBoard)) {
                    leaderboard.add(rounds);
                    printVictory(rounds);
                    double winnings = betAmount * getMultiplier(modeChoice);
                    playerMoney += winnings;
                    System.out.println("\nYou won **$" + winnings + "!** Your new balance: **$" + playerMoney + "**");
                } else if (isWinner(computerBoard)) {
                    System.out.println("Computer wins in " + rounds + " rounds!");
                }

                displayLeaderboard();

                if (playerMoney < 50) {
                    System.out.println("\nYou don't have enough money to play again! Game over.");
                    break;
                }

                System.out.print("Wanna play again? (yes/no): ");
                playAgain = scanner.nextLine().trim().equalsIgnoreCase("yes");

            } while (playAgain);
        }

        System.out.println("Thanks for playing! See you next time!");
        scanner.close();
        return money;
    }
        
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

    static List<Integer> leaderboard = new ArrayList<>();
    static double playerMoney = 200;

    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        
        for (String suit : suits) {
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value));
            }
        }
        
        return deck;
    }

    private static List<Card> setupBoard(int boardSize) {
        List<Card> board = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            board.add(new Card("Empty", -1));
        }
        return board;
    }

    private static void printBoard(List<Card> board) {
        System.out.println("\nCurrent Board:");
        for (Card card : board) {
            System.out.print(card + " | ");
        }
        System.out.println();
    }

    private static void printVictory(int rounds) {
        System.out.println("\n🎉 Congrats! You won in " + rounds + " rounds! 🎉");
    }

    private static void displayLeaderboard() {
        System.out.println("\nLeaderboard:");
        leaderboard.sort(Comparator.naturalOrder());
        for (int i = 0; i < leaderboard.size(); i++) {
            System.out.println((i + 1) + ". " + leaderboard.get(i) + " rounds");
        }
    }

    private static boolean isWinner(List<Card> board) {
        return board.stream().noneMatch(c -> c.value == -1);
    }

    private static boolean canPlaceCard(Card card, List<Card> board, int boardSize) {
        return card.value > 0 && card.value <= boardSize && board.get(card.value - 1).value == -1;
    }

    private static void placeCard(Card card, List<Card> board) {
        board.set(card.value - 1, card);
        System.out.println("Card placed in position " + card.value);
    }

    private static int countPlacedCards(List<Card> board) {
        return (int) board.stream().filter(c -> c.value != -1).count();
    }

    private static double getMultiplier(int mode) {
        return switch (mode) {
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 6;
            case 4 -> 10;
            default -> 1;
        };
    }

    private static int getValidInput(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) break;
            } catch (NumberFormatException ignored) {}
            System.out.print("Invalid choice. Try again: ");
        }
        return choice;
    }

    private static double getValidBet(Scanner scanner, double balance) {
        double bet;
        while (true) {
            try {
                bet = Double.parseDouble(scanner.nextLine().trim());
                if (bet >= 50 && bet <= balance) break;
            } catch (NumberFormatException ignored) {}
            System.out.print("Invalid bet. Enter an amount between $50 and your balance: ");
        }
        return bet;
    }
}