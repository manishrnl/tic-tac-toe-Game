module org.example.tictactoegame_vs_ai {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens org.example.tictactoegame_vs_ai to javafx.fxml;
    exports org.example.tictactoegame_vs_ai;
}