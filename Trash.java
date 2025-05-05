import java.util.*;

public class Trash {
    // Represents cards with a numerical value
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
    static double playerMoney = 200; // Player starts with $200

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (playerMoney >= 50) { // Must have at least $50 to play
            System.out.println("\nYou have $" + playerMoney);
            System.out.println("Enter your bet (must be <= your balance, min $50 to play):");
            double betAmount = getValidBet(scanner, playerMoney);

            playerMoney -= 50; // Take the entry fee
            System.out.println("Entry fee paid! You have $" + playerMoney + " left.");

            // Choose a board size from normal to insane
            System.out.println("Choose a board size (10 or 12):");
            int boardSize = getValidInput(scanner, 10, 12);

            System.out.println("Choose a game mode: \n1. Normal (×2 win) \n2. Timed Mode (×4 win) \n3. Hard Mode (×6 win) \n4. Impossible Mode (×10 win)");
            int modeChoice = getValidInput(scanner, 1, 4);

            boolean playAgain;
            do {
                List<Card> deck = createDeck(boardSize);
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

                    if (modeChoice == 2 && (System.currentTimeMillis() - startTime) / 1000 > 60) {
                        System.out.println("Time's up! Game ends in a draw.");
                        break;
                    }
                    if (modeChoice == 3 && rounds > 20) {
                        System.out.println("Round limit reached! Game ends in a draw.");
                        break;
                    }

                    System.out.println("\nRound " + rounds);
                    System.out.println("Discard pile: " + (discardPile.isEmpty() ? "Empty" : discardPile.get(discardPile.size() - 1)));

                    String input;
                    while (true) {
                        System.out.print("Draw a card (type 'discard' or 'draw'): ");
                        input = scanner.nextLine().trim().toLowerCase();
                        if (input.equals("discard") || input.equals("draw")) break;
                        System.out.println("Invalid input. Please type 'discard' or 'draw'.");
                    }

                    Card drawnCard;
                    if (input.equalsIgnoreCase("discard")) {
                        if (!discardPile.isEmpty()) {
                            drawnCard = discardPile.remove(discardPile.size() - 1);
                        } else {
                            System.out.println("Discard pile is empty. Drawing from deck instead.");
                            drawnCard = deck.remove(0);
                        }
                    } else {
                        drawnCard = deck.remove(0);
                    }

                    System.out.println("You drew: " + drawnCard);

                    if (modeChoice == 4 && drawnCard.value > 5) {
                        System.out.println("Card doesn't fit in Impossible Mode. Discarding.");
                        discardPile.add(drawnCard);
                    } else if (canPlaceCard(drawnCard, playerBoard, playerBoard.size())) {
                        placeCard(drawnCard, playerBoard);
                    } else {
                        System.out.println("Card discarded.");
                        discardPile.add(drawnCard);
                    }

                    if (!deck.isEmpty()) {
                        Card computerCard;
                        if (!discardPile.isEmpty() && canPlaceCard(discardPile.get(discardPile.size() - 1), computerBoard, computerBoard.size())) {
                            computerCard = discardPile.remove(discardPile.size() - 1);
                            System.out.println("Computer picked from discard pile: " + computerCard);
                        } else {
                            computerCard = deck.remove(0);
                            System.out.println("Computer drew: " + computerCard);
                        }

                        if (modeChoice == 4 && computerCard.value > 5) {
                            discardPile.add(computerCard);
                        } else if (canPlaceCard(computerCard, computerBoard, computerBoard.size())) {
                            placeCard(computerCard, computerBoard);
                        } else {
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

    private static double getMultiplier(int mode) {
        return switch (mode) {
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 6;
            case 4 -> 10;
            default -> 1;
        };
    }

    private static boolean canPlaceCard(Card card, List<Card> board, int boardSize) {
        return card.value > 0 && card.value <= boardSize && board.get(card.value - 1).value == -1;
    }

    private static void placeCard(Card card, List<Card> board) {
        board.set(card.value - 1, card);
        System.out.println("Card placed in position " + card.value);
    }

    private static boolean isWinner(List<Card> board) {
        return board.stream().noneMatch(c -> c.value == -1);
    }

    private static int countPlacedCards(List<Card> board) {
        return (int) board.stream().filter(c -> c.value != -1).count();
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
}