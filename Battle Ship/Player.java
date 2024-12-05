package battleShip;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Player {
    private String name;
    private Ship[] ships;
    private Scanner scanner; // Use one scanner for the whole class

    public Player(String name) {
        this.name = name;
        this.ships = new Ship[5]; // Assuming 5 ships in total
        this.scanner = new Scanner(System.in);
    }

    public String getName() {
        return name;
    }

    public void placeShips(Board board) {
        System.out.println(name + ", place your ships on a 10x10 grid (0-9 for both x and y coordinates):");
        for (int i = 0; i < ships.length; i++) {
            System.out.println("Placing Ship " + (i + 1) + " of length " + (i + 2));
            Ship ship = new Ship(i + 2); // Length of ship i+2
            boolean isValidPlacement = false;
            while (!isValidPlacement) {
                int x = -1, y = -1;
                char orientation = '\0';
                while (x < 0 || x > 9) {
                    System.out.print("Enter x coordinate for Ship " + (i + 1) + " (0-9): ");
                    x = readValidInteger();
                }
                while (y < 0 || y > 9) {
                    System.out.print("Enter y coordinate for Ship " + (i + 1) + " (0-9): ");
                    y = readValidInteger();
                }
                while (orientation != 'h' && orientation != 'v') {
                    System.out.print("Enter orientation for Ship " + (i + 1) + " (h for horizontal, v for vertical): ");
                    orientation = readValidChar(scanner);
                }
                isValidPlacement = board.placeShip(ship, x, y, orientation);
                if (!isValidPlacement) {
                    System.out.println("Invalid placement. Please try again.");
                }
            }
            ships[i] = ship;
        }
    }

    public char readValidChar(Scanner scanner) {
        String input = "";
        while (input.length() == 0 || (input.charAt(0) != 'h' && input.charAt(0) != 'v')) {
            input = scanner.next().trim().toLowerCase();
            if (input.length() == 0 || (input.charAt(0) != 'h' && input.charAt(0) != 'v')) {
                System.out.println("Please enter 'h' for horizontal or 'v' for vertical.");
            }
        }
        return input.charAt(0);
    }

    public void takeTurn(Board opponentBoard) {
        System.out.println(name + ", it's your turn to attack. Enter coordinates within 0-9.");
        int x = -1, y = -1;
        while (x < 0 || x > 9) {
            System.out.print("Enter x coordinate to attack (0-9): ");
            x = readValidInteger();
        }
        while (y < 0 || y > 9) {
            System.out.print("Enter y coordinate to attack (0-9): ");
            y = readValidInteger();
        }
        opponentBoard.receiveAttack(x, y);
    }

    public int readValidInteger() {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (input >= 0 && input <= 9) {
                    return input;
                } else {
                    System.out.println("Please enter a number between 0 and 9.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Consume the invalid input
            }
        }
    }


    // Make sure to close the Scanner when the Player object is no longer used
    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
