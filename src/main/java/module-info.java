module group.hx.cardgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens group.hx.cardgame to javafx.fxml;
    exports group.hx.cardgame;
}