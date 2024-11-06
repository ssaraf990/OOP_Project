import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Random;

public class Blackjack extends Application {
    private int playerMoney;
    private int betAmount;
    private int playerScore = 0, dealerScore = 0;
    private GameMenu menu;

    public Blackjack(int initialMoney, GameMenu menu) {
        this.playerMoney = initialMoney;
        this.menu = menu;
    }

    @Override
    public void start(Stage stage) {
        Label scoreLabel = new Label("Player Score: 0");
        Label dealerLabel = new Label("Dealer Score: 0");
        TextField betInput = new TextField();
        betInput.setPromptText("Enter your bet");

        Button betBtn = new Button("Place Bet");
        Button hitBtn = new Button("Hit");
        Button standBtn = new Button("Stand");

        hitBtn.setDisable(true);
        standBtn.setDisable(true);

        betBtn.setOnAction(e -> {
            try {
                betAmount = Integer.parseInt(betInput.getText());
                if (betAmount <= 0 || betAmount > playerMoney) {
                    showAlert("Invalid Bet", "Bet amount must be valid.");
                } else {
                    hitBtn.setDisable(false);
                    standBtn.setDisable(false);
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a number.");
            }
        });

        hitBtn.setOnAction(e -> {
            playerScore += drawCard();
            scoreLabel.setText("Player Score: " + playerScore);
            if (playerScore > 21) {
                showAlert("Bust!", "You lost $" + betAmount);
                updateMoney(-betAmount);
                stage.close();
            }
        });

        standBtn.setOnAction(e -> {
            dealerPlay();
            dealerLabel.setText("Dealer Score: " + dealerScore);
            if (dealerScore > 21 || playerScore > dealerScore) {
                showAlert("Win!", "You won $" + betAmount);
                updateMoney(betAmount);
            } else {
                showAlert("Loss", "Dealer wins! You lost $" + betAmount);
                updateMoney(-betAmount);
            }
            stage.close();
        });

        VBox vbox = new VBox(10, scoreLabel, dealerLabel, betInput, betBtn, hitBtn, standBtn);
        vbox.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(vbox, 300, 300));
        stage.show();
    }

    private int drawCard() {
        return new Random().nextInt(10) + 1;
    }

    private void dealerPlay() {
        while (dealerScore < 17) {
            dealerScore += drawCard();
        }
    }

    private void updateMoney(int amount) {
        playerMoney += amount;
        menu.updateBalance(playerMoney);
        if (playerMoney <= 0) {
            showAlert("Game Over", "You are out of money!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
