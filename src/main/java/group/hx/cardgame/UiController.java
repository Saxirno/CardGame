package group.hx.cardgame;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class UiController  {
    Game game = new Game();//game逻辑类

    @FXML
    private Button next;
    @FXML
    private Text health;
    @FXML
    private Text attack;
    @FXML
    private Text defense;

    @FXML
    private ImageView discard;

    @FXML
    private VBox left;

    @FXML
    private BorderPane center;

    @FXML
    private HBox CardBox;

    @FXML
    void Next(ActionEvent event) {
        CardBox.getChildren().clear();
    }

    @FXML
    void testClicked(MouseEvent event) {
    }

    public void initialize() {
//        Button bb1 = new Button("1");
//        Button bb2 = new Button("2");
//        Button bb3 = new Button("3");
//        CardBox.getChildren().addAll(bb1, bb2, bb3);


        game.init();
        game.makeCard();
        game.player1.energy = game.rand.nextInt(3)+2;
        updatePlayer();
        RoundCard();
        RoundEnergy(game.player1.energy);

    }

    public void start(){

    }

    public void update(){

    }

    public  void end(){

    }

    public void RoundCard(){
        CardBox.getChildren().clear();//先清除旧卡牌
        for(int c:game.cards.keySet())
        {
            //依照不同属性或得不同卡牌图片
            CardBox.getChildren().add(CreateCard(game.cards.get(c)));
        }
    }

    public ImageView CreateCard(Card card){
        Image image = switch (card.attribute) {
            case "a" -> new Image("attack.png");
            case "d" -> new Image("defense.png");
            case "h" -> new Image("health.png");
            case "s" -> new Image("hurt.png");
            default -> null;
        };
        return getImageView(image,card);
    }

    private ImageView getImageView(Image image,Card card2) {//设置卡牌图片属性
        ImageView card = new ImageView(image);
        card.setFitHeight(100);
        card.setFitWidth(100);
        //设置鼠标悬浮样式（也可以用css设置）
        card.setCursor(Cursor.HAND);
        card.setOnMouseEntered(e->{
            card.setFitHeight(120);
            card.setFitWidth(120);
        });
        card.setOnMouseExited(e->{
            card.setFitWidth(100);
            card.setFitHeight(100);
        });

        card.setOnMouseClicked(e->{//点击卡牌即打出
            //打入中央
            if (game.player1.energy<card2.cost)
            {
                System.out.println("not enough energy");
                //设置能量不足点击时的动画
                //能量动画
                left.getChildren().forEach(e5->{
                    ((Circle) e5).setFill(Color.RED);
                });
                //卡牌动画
                ParallelTransition pt = getParallelTransition(card);
                pt.setOnFinished(e6->{
                    left.getChildren().forEach(e7->{
                        ((Circle) e7).setFill(Color.BLUE);
                    });
                });
                pt.play();
                return;
            }
            center.setCenter(card);
            //设置淡出动画
            FadeTransition transition = new FadeTransition(Duration.seconds(1),card);
            transition.setToValue(0);
            transition.play();
            transition.setOnFinished(e2->{//动画完成后放入弃牌堆
                discard.setImage(card.getImage());
            });
            game.player1.effect(card2);//卡牌对palyer1生效
            System.out.println(game.player1.energy);
            //刷新属性
            updatePlayer();
            RoundEnergy(game.player1.energy);
        });
        return card;
    }

    private static ParallelTransition getParallelTransition(Node node) {
        //淡入淡出
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setFromValue(1);
        fade.setToValue(0.3);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        //缩放
        ScaleTransition st = new ScaleTransition(Duration.millis(500), node);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        return new ParallelTransition(fade, st);
    }

    private void updatePlayer() {//刷新属性
        attack.setText(Integer.toString(game.player1.attack));
        defense.setText(Integer.toString(game.player1.defense));
        health.setText(Integer.toString(game.player1.health));
    }


    public void RoundEnergy(int energy){
        FadeTransition fade = new FadeTransition(Duration.millis(500), left);
        fade.setFromValue(1);
        fade.setToValue(0.3);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        fade.play();
        left.getChildren().clear();//先清除再添加
        for(int i = 0; i < energy; i++){
            left.getChildren().add(CreateEnergy());
        }
    }

    public Circle CreateEnergy(){//创建能量圆
        Circle circle = new Circle(25);
        circle.setStroke(Color.GREENYELLOW);
        circle.setFill(Color.BLUE);
        return circle;
    }

}