import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class Limbo extends Application {
    private int playerMoney;
    private int betAmount;
    private GameMenu menu;

    public Limbo(int initialMoney, GameMenu menu) {
        this.playerMoney = initialMoney;
        this.menu = menu;
    }

    @Override
    public void start(Stage stage) {
        Label resultLabel = new Label("Enter a target multiplier (1-10):");
        TextField multiplierInput = new TextField();
        multiplierInput.setPromptText("Enter your guess");

        TextField betInput = new TextField();
        betInput.setPromptText("Enter your bet");

        Button playBtn = new Button("Play");

        playBtn.setOnAction(e -> {
            try {
                int targetMultiplier = Integer.parseInt(multiplierInput.getText());
                betAmount = Integer.parseInt(betInput.getText());

                if (betAmount <= 0 || betAmount > playerMoney) {
                    showAlert("Invalid Bet", "Bet amount must be valid.");
                    return;
                }

                if (targetMultiplier < 1 || targetMultiplier > 10) {
                    showAlert("Invalid Input", "Multiplier must be between 1 and 10.");
                    return;
                }

                int actualMultiplier = new Random().nextInt(10) + 1;
                if (targetMultiplier == actualMultiplier) {
                    showAlert("Win!", "You guessed right! Earned 10% of your bet.");
                    updateMoney((int) (0.1 * betAmount));
                } else {
                    showAlert("Loss", "Wrong guess! Lost 7% of your bet.");
                    updateMoney(-(int) (0.07 * betAmount));
                }
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers.");
            }
        });

        VBox vbox = new VBox(10, resultLabel, multiplierInput, betInput, playBtn);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
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
