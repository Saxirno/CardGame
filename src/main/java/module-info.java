module group.hx.cardgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens group.hx.cardgame to javafx.fxml;
    exports group.hx.cardgame;
}