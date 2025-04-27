package org.example.tictactoegame_vs_ai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("Tic-tac-toeGame_VS_AI.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Welcome to Tic-Tac-Toe Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}