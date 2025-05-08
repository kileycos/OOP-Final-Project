import java.util.Random;
import java.util.Scanner;

public class Blackjack {
    public static double playBlackjack(double money) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter your bet amount: ");
        double bet = scanner.nextDouble();

        if (bet > money || bet <= 0) {
            System.out.println("Invalid bet. Returning to menu...");
            return money;
        }

        // Function to draw a card and correctly handle face values

        int playerCard1 = getCard(random);
        int playerCard2 = getCard(random);
        int dealerCard1 = getCard(random);
        int dealerCard2 = getCard(random);

        int playerTotal = playerCard1 + playerCard2;
        int dealerTotal = dealerCard1 + dealerCard2;

        System.out.println("Your cards: " + playerCard1 + " and " + playerCard2 + " (Total: " + playerTotal + ")");
        System.out.println("Dealer's visible card: " + dealerCard1);

        while (playerTotal < 21) {
            System.out.print("Do you want to hit or stand? (h/s): ");
            char choice = scanner.next().charAt(0);
            if (choice == 'h') {
                int newCard = getCard(random);
                playerTotal += newCard;
                System.out.println("You drew: " + newCard + " (Total: " + playerTotal + ")");
            } else {
                break;
            }
        }

        while (dealerTotal < 17) {
            int newCard = getCard(random);
            dealerTotal += newCard;
        }

        System.out.println("Dealer's total: " + dealerTotal);

        if (playerTotal > 21) {
            System.out.println("You bust! You lost $" + bet);
            money -= bet;
        } else if (dealerTotal > 21 || playerTotal > dealerTotal) {
            System.out.println("You win! You won $" + bet);
            money += bet;
        } else {
            System.out.println("You lost $" + bet);
            money -= bet;
        }

        System.out.println("Your new balance: $" + money);
        return money;
    }

    public static int getCard(Random random) {
        int card = random.nextInt(13) + 1; // Generates values 1-13 (Ace-King)
        return card > 10 ? 10 : card; // Face cards (J, Q, K) count as 10
    }
}