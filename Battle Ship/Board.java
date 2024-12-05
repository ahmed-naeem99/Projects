package battleShip;

public class Board {
    private char[][] grid;
    private boolean[][] hits;
    private boolean[][] misses;

    public Board() {
        grid = new char[10][10];
        hits = new boolean[10][10];
        misses = new boolean[10][10];
        initializeGrid();
    }

    void initializeGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = '-';
            }
        }
    }

    public boolean placeShip(Ship ship, int x, int y, char orientation) {
        int length = ship.getLength();
        if ((orientation == 'h' && x + length > 10) || (orientation == 'v' && y + length > 10)) {
            return false; // Ship would go out of bounds
        }

        // Check for overlapping ships
        for (int i = 0; i < length; i++) {
            if (orientation == 'h' && grid[x + i][y] != '-') return false;
            if (orientation == 'v' && grid[x][y + i] != '-') return false;
        }

        for (int i = 0; i < length; i++) {
            if (orientation == 'h') {
                grid[x + i][y] = 'S'; // Mark ship on grid
            } else {
                grid[x][y + i] = 'S'; // Mark ship on grid
            }
        }
        return true;
    }

    public void receiveAttack(int x, int y) {
        if (grid[x][y] == 'S') {
            hits[x][y] = true;
            grid[x][y] = 'X'; // Update grid to show hit
            System.out.println("Hit!");
        } else {
            misses[x][y] = true;
            grid[x][y] = 'O'; // Update grid to show miss
            System.out.println("Miss!");
        }
    }

    public void displayGrid(boolean showShips) {
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < grid.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < grid[i].length; j++) {
                if (showShips) {
                    System.out.print(grid[i][j] + " ");
                } else {
                    if (hits[i][j]) {
                        System.out.print("X ");
                    } else if (misses[i][j]) {
                        System.out.print("O ");
                    } else {
                        System.out.print("- ");
                    }
                }
            }
            System.out.println();
        }
    }

    public int displayStatus() {
        int shipsRemaining = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'S' && !hits[i][j]) {
                    shipsRemaining++;
                }
            }
        }
        System.out.println("Ships Remaining: " + shipsRemaining);
        return shipsRemaining;
    }

    public boolean allShipsSunk() {
        return displayStatus() == 0;  // Now this will work since displayStatus() returns an integer
    }

}
