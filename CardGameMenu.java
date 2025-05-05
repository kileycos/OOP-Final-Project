import java.util.Scanner;

public class CardGameMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       
        System.out.println("Welcome to the Card Game Menu!");
        System.out.print("Enter the amount of money you have: ");
        double money = scanner.nextDouble();

        while (money > 0) {
            System.out.println("\nMenu:");
            System.out.println("1. Play Blackjack");
            System.out.println("2. Play Card Game 2");
            System.out.println("3. Play Trash");
            System.out.println("4. Quit");
            System.out.print("Choose an option: ");
           
            int choice = scanner.nextInt();
            if (choice == 4) {
                System.out.println("You quit the game with $" + money);
                break;
            } else if (choice == 1) {
                money = Blackjack.playBlackjack(money);
            }else if (choice == 2){
                money = CardGame2.playCardGame2(double money);
            }
            else if (choice == 3) {
                money = Trash.playTrashCardGame(double money);
            }else{
                System.out.println("Invalid choice. Try again.");
            }
        }
               

        if (money <= 0) {
            System.out.println("You lost all your money. Game Over.");
        }

        scanner.close();
    }
}