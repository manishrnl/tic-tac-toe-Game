package org.example.tictactoegame_vs_ai;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Controller {
    @FXML
    private Text resultText, HumaOrAIText, player1Name, player2Name;
    @FXML
    private FlowPane flowPane;
    @FXML
    private TextField player1TextField, player2TextField;

    @FXML
    private Button HumanAIButton, startButton, b00, b01, b02, b10, b11, b12, b20, b21, b22;
    @FXML
    private AnchorPane anchorPane;

    public static int turn = 1, count = 0;
    boolean AIGame = false, gameTied = false;
    ArrayList<String> firstPerson = new ArrayList<>();
    ArrayList<String> secondPerson = new ArrayList<>();
    Random random = new Random();

    String[] AIChoice = {"b00", "b01", "b02", "b10", "b11", "b12", "b20", "b21", "b22"};
    // Define all possible winning combinations
    String[][] winningCombinations = {
            {"b00", "b11", "b22"}, // Diagonal 1
            {"b00", "b10", "b20"}, // Column 1
            {"b00", "b01", "b02"}, // Row 1
            {"b01", "b11", "b21"}, // Column 2
            {"b02", "b12", "b22"}, // Column 3
            {"b20", "b11", "b02"}, // Diagonal 2
            {"b10", "b11", "b12"}, // Row 2
            {"b20", "b21", "b22"}  // Row 3
    };
    Timeline timeline;

    @FXML
    private void handleGameButtonClick(MouseEvent event) {
        if (count >= 9) return; // Stop if board is full

        Button clickedButton = (Button) event.getSource();
        if (!clickedButton.getText().isEmpty()) return; // Ignore already filled buttons

        // Determine player turn
        if (AIGame) {
            clickedButton.setText("O");
            firstPerson.add(clickedButton.getId());
            count++;

            // AI move after player's turn
            if (count < 9 && !checkForGame()) {
                aiMove();
                count++;
                checkForGame();
            }
        } else {
            if (turn % 2 == 1) { // First player (O)
                clickedButton.setText("O");
                firstPerson.add(clickedButton.getId());
            } else { // Second player (X)
                clickedButton.setText("X");
                secondPerson.add(clickedButton.getId());
            }
            count++;
            turn++;
            // Check for winner
            if (checkForGame()) return;
            if (count == 9) {
                resultText.setText("Game Over! No result");
            }
        }
    }

    private void aiMove() {
        if (count >= 9) return; // Prevent AI move if board is full
        int randomAI;
        Button aiButton;
        do {
            randomAI = random.nextInt(9); // Pick a random index (0-8)
            aiButton = (Button) anchorPane.lookup("#" + AIChoice[randomAI]); // Find button by ID
        } while (aiButton == null || !aiButton.getText().isEmpty());
        aiButton.setText("X");
        secondPerson.add(aiButton.getId());
    }


    @FXML
    void changeHumanOrAI(ActionEvent event) {
        if (HumanAIButton.getText().equals("VS AI")) {
            HumaOrAIText.setText("Human Vs AI");
            AIGame = true;
            HumanAIButton.setText("VS Human");
            player2Name.setVisible(false);
            player2TextField.setVisible(false);
            player1Name.setText(" Player Name");
        } else {
            player2Name.setVisible(true);
            player2TextField.setVisible(true);
            player2Name.setText(" Player 2 Name");
            player1Name.setText(" Player 1 Name");
            HumaOrAIText.setText("Human Vs Human");
            AIGame = false;
            HumanAIButton.setText("VS AI");
        }
        stopGame();
    }

    private boolean checkForGame() {
        for (String[] combo : winningCombinations) {
            if (firstPerson.contains(combo[0]) && firstPerson.contains(combo[1]) && firstPerson.contains(combo[2]) && HumanAIButton.getText().equals("VS Human")) {
                resultText.setText("Congratulations " + player1TextField.getPromptText() + " ! You Won");
                stopGame();
                gameTied = false;
                return true;
            }
            if (firstPerson.contains(combo[0]) && firstPerson.contains(combo[1]) && firstPerson.contains(combo[2]) && HumanAIButton.getText().equals("VS AI")) {
                resultText.setText("Congratulations " + player1TextField.getPromptText() + " ! You" +
                        " Won");
                stopGame();
                gameTied = false;
                return true;
            }
            if (secondPerson.contains(combo[0]) && secondPerson.contains(combo[1]) && secondPerson.contains(combo[2]) && HumanAIButton.getText().equals("VS Human")) {
                resultText.setText("Sorry "+ player1TextField.getPromptText()+ " , this time" +
                        " " +
                        "AI Won!");
                stopGame();
                gameTied = false;
                return true;
            }
            if (secondPerson.contains(combo[0]) && secondPerson.contains(combo[1]) && secondPerson.contains(combo[2]) && HumanAIButton.getText().equals("VS AI")) {
                resultText.setText("Congratulations " + player2TextField.getPromptText() + " ! You  Won");
                stopGame();
                gameTied = false;
                return true;
            }
        }
        return false;
    }


    private void setStatus(boolean status) {
        b00.setDisable(status);
        b01.setDisable(status);
        b02.setDisable(status);
        b10.setDisable(status);
        b11.setDisable(status);
        b12.setDisable(status);
        b20.setDisable(status);
        b21.setDisable(status);
        b22.setDisable(status);
    }

    private void stopGame() {
        if (timeline != null) {
            timeline.stop();
        }
        setStatus(true);
        startButton.setText("Restart");
        startButton.setOnMouseClicked(event -> restartGame());
    }

    @FXML
    private void restartGame() {
        setStatus(false);

        for (String id : AIChoice) {
            Button btn = (Button) anchorPane.lookup("#" + id);
            if (btn != null) btn.setText("");
        }

        turn = 1;
        count = 0;
        firstPerson.clear();
        secondPerson.clear();
        resultText.setText("Game Restarted!");

        if (timeline != null) {
            timeline.stop();
            timeline.play();
        }
    }

    public void startGame(MouseEvent mouseEvent) {
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> checkForGame()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public void enableGame(ActionEvent actionEvent) {
        flowPane.setDisable(false);
    }
}