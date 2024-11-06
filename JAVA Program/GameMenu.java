import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameMenu extends Application {
    private int playerMoney = 100; // Initial balance
    private Label balanceLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gambling Platform");

        balanceLabel = new Label("Available Balance: $" + playerMoney);
        Button blackjackBtn = new Button("Play Blackjack");
        Button minesweeperBtn = new Button("Play Minesweeper");
        Button limboBtn = new Button("Play Limbo");

        blackjackBtn.setOnAction(e -> new Blackjack(playerMoney, this).start(new Stage()));
        minesweeperBtn.setOnAction(e -> new Minesweeper(playerMoney, this).start(new Stage()));
        limboBtn.setOnAction(e -> new Limbo(playerMoney, this).start(new Stage()));

        VBox vbox = new VBox(15, balanceLabel, blackjackBtn, minesweeperBtn, limboBtn);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateBalance(int newBalance) {
        playerMoney = newBalance;
        balanceLabel.setText("Available Balance: $" + playerMoney);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
