//this is silas doing his card game but not exactly knowing how to create it
import java.util.Scanner;
import java.util.Random;

public class TakoyakiGame {
    public static void main(String[] args) {
        final int GRID_SIZE = 3; // 3x3 grid
        int[][] takoyakiPan = new int[GRID_SIZE][GRID_SIZE];
        boolean[][] marked = new boolean[GRID_SIZE][GRID_SIZE];
        
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        // Generate randoms for the grid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                takoyakiPan[i][j] = random.nextInt(10) + 1; // Numbers 1-10
                marked[i][j] = false; // Mark all unselected
            }
        }

        // Instructions
        System.out.println("Welcome to the Takoyaki Game!");
        System.out.println("Select numbers to form a line (horizontal, vertical, or diagonal)!");
        System.out.println("Grid:");

        // Print initial grid
        printGrid(takoyakiPan, marked);

        boolean gameOver = false;

        while (!gameOver) {
            System.out.println("Enter the number you want to mark: ");
            int choice = scanner.nextInt();

            // Find and mark chosen number
            boolean found = false;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (takoyakiPan[i][j] == choice && !marked[i][j]) {
                        marked[i][j] = true;
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                System.out.println("Marked! Updated grid:");
                printGrid(takoyakiPan, marked);
            } else {
                System.out.println("Number not found or already marked!");
            }

            // Check win condition
            if (checkWin(marked)) {
                System.out.println("Congratulations! You've formed a line and won the game!");
                gameOver = true;
            }
        }
        scanner.close();
    }

    public static void printGrid(int[][] grid, boolean[][] marked) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (marked[i][j]) {
                    System.out.print(" X "); // Marked slot
                } else {
                    System.out.print(" " + grid[i][j] + " "); // Unmarked slot
                }
            }
            System.out.println();
        }
    }

    public static boolean checkWin(boolean[][] marked) {
        int size = marked.length;

        // Check rows and columns
        for (int i = 0; i < size; i++) {
            if ((marked[i][0] && marked[i][1] && marked[i][2]) || 
                (marked[0][i] && marked[1][i] && marked[2][i])) {
                return true;
            }
        }

        // Check diagonals
        if ((marked[0][0] && marked[1][1] && marked[2][2]) || 
            (marked[0][2] && marked[1][1] && marked[2][0])) {
            return true;
        }
        return false;
    }
}