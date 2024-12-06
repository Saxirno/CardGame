package group.hx.cardgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CardGame extends Application {

    //加载ui
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Ui.fxml"));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //stage设置
        stage.setTitle("Card Game");
        stage.setResizable(false);


        //scene设置
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.show();

        //加载css样式
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
    }
}