package battleShip;

public class Game {
    private Player currentPlayer;
    private Player opponentPlayer;
    private Board currentPlayerBoard;
    private Board opponentPlayerBoard;
    private boolean isGameOver;

    public Game(Player currentPlayer, Player opponentPlayer) {
        this.currentPlayer = currentPlayer;
        this.opponentPlayer = opponentPlayer;
        this.currentPlayerBoard = new Board();
        this.opponentPlayerBoard = new Board();
        this.isGameOver = false;
    }

    public void startGame() {
        System.out.println("Starting Battleship game...");
        currentPlayerBoard.initializeGrid();
        opponentPlayerBoard.initializeGrid();
        currentPlayer.placeShips(currentPlayerBoard);
        opponentPlayer.placeShips(opponentPlayerBoard);
        playGame();
    }

    private void playGame() {
        while (!isGameOver) {
            // Check if the opponent's ships are all sunk before starting the turn
            if (opponentPlayerBoard.allShipsSunk()) {
                System.out.println(opponentPlayer.getName() + " has lost the game!");
                isGameOver = true;
                break;
            }

            // Display the currentPlayer's view of opponent's board without ships
            System.out.println(currentPlayer.getName() + "'s turn:");
            System.out.println("Opponent's Board:");
            opponentPlayerBoard.displayGrid(false); // False to not show ships
            
            // Display the currentPlayer's board showing their own ships
            System.out.println("Your Board:");
            currentPlayerBoard.displayGrid(true); // True to show ships

            currentPlayerBoard.displayStatus();
            opponentPlayerBoard.displayStatus();
            currentPlayer.takeTurn(opponentPlayerBoard);
            
            // Swap players
            Player temp = currentPlayer;
            currentPlayer = opponentPlayer;
            opponentPlayer = temp;
            
            // Swap boards
            Board tempBoard = currentPlayerBoard;
            currentPlayerBoard = opponentPlayerBoard;
            opponentPlayerBoard = tempBoard;
        }
        System.out.println("Game Over!");
        currentPlayer.closeScanner();
        opponentPlayer.closeScanner();
    }

}
