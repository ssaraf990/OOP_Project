import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Random;

public class Minesweeper extends Application {
    private int playerMoney;
    private int betAmount;
    private double winnings = 0;
    private boolean[][] mines = new boolean[5][5];
    private GameMenu menu;

    public Minesweeper(int initialMoney, GameMenu menu) {
        this.playerMoney = initialMoney;
        this.menu = menu;
    }

    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        // Get the bet amount
        TextInputDialog betDialog = new TextInputDialog();
        betDialog.setHeaderText("Enter your bet amount:");
        betDialog.showAndWait().ifPresent(input -> {
            try {
                betAmount = Integer.parseInt(input);
                if (betAmount <= 0 || betAmount > playerMoney) {
                    showAlert("Invalid Bet", "Bet amount must be valid.");
                    stage.close();
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number.");
                stage.close();
            }
        });

        placeMines();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button tile = new Button("?");
                tile.setMinSize(50, 50);
                int row = i, col = j;
                tile.setOnAction(e -> handleClick(row, col, tile));
                grid.add(tile, j, i);
            }
        }

        Button endGameBtn = new Button("End Game and Keep Winnings");
        endGameBtn.setOnAction(e -> endGame(stage));

        grid.add(endGameBtn, 0, 5, 5, 1);

        Scene scene = new Scene(grid, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void placeMines() {
        Random rand = new Random();
        int placedMines = 0;
        while (placedMines < 5) {
            int row = rand.nextInt(5);
            int col = rand.nextInt(5);
            if (!mines[row][col]) {
                mines[row][col] = true;
                placedMines++;
            }
        }
    }

    private void handleClick(int row, int col, Button tile) {
        if (mines[row][col]) {
            tile.setText("M");
            showAlert("Mine!", "You hit a mine! Lost all winnings and 20% of your bet.");
            updateMoney(-(int) (1.2 * betAmount));
            resetGame();
        } else {
            tile.setText("0");
            winnings += 0.1 * betAmount;
            showAlert("Safe!", "Winnings so far: $" + (int) winnings);
        }
    }

    private void endGame(Stage stage) {
        showAlert("Game Over", "You kept your winnings: $" + (int) winnings);
        updateMoney((int) winnings);
        stage.close();
    }

    private void updateMoney(int amount) {
        playerMoney += amount;
        menu.updateBalance(playerMoney);
        if (playerMoney <= 0) {
            showAlert("Game Over", "You are out of money!");
        }
    }

    private void resetGame() {
        menu.updateBalance(playerMoney);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
