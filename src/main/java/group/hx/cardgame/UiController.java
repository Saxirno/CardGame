package group.hx.cardgame;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;


public class UiController  {
    Game game;//game逻辑类
    int Energy=2;//用于随着难度提升增加玩家能量下限和上限
    int comHp=10;//电脑生命值
    Image result;//对局结果图
    MediaPlayer click = new MediaPlayer(new Media(getClass().getResource("click.mp3").toExternalForm()));
    MediaPlayer pass = new MediaPlayer(new Media(getClass().getResource("pass.mp3").toExternalForm()));
    MediaPlayer cardClick = new MediaPlayer(new Media(getClass().getResource("card.mp3").toExternalForm()));

    @FXML
    private Text health;//玩家hp
    @FXML
    private Text comhealth;//电脑hp
    @FXML
    private Text attack;//玩家攻击力
    @FXML
    private Text comattack;//电脑攻击力
    @FXML
    private Text defense;//玩家防御力
    @FXML
    private Text comdefense;//电脑防御力
    @FXML
    private ImageView discard;//弃牌堆顶
    @FXML
    private ImageView comdiscard;//电脑弃牌堆
    @FXML
    private VBox left;//用于装能量球
    @FXML
    private BorderPane center;//中心出牌区
    @FXML
    private HBox CardBox;//玩家手牌区
    @FXML
    private ImageView comCard;//电脑打出的牌

    @FXML
    void Next(ActionEvent event) {//回合结束点击事件
        CardBox.getChildren().clear();//清除手牌
        click.play();
        end();
    }

    public void initialize() {//控制器初始化
        game = new Game();
        game.init(comHp);
        click.setOnEndOfMedia(()->{
            click.seek(click.getStartTime());
            click.stop();
        });
        cardClick.setOnEndOfMedia(()->{
            cardClick.seek(cardClick.getStartTime());
            cardClick.stop();
        });
        pass.setOnEndOfMedia(()->{
            pass.seek(pass.getStartTime());
            pass.stop();
        });

        start();
    }

    public void start(){//回合开始
        game.makeCard();
        game.player1.energy = game.rand.nextInt(3)+Energy;//玩家随机能量
        //电脑操作
        playCom();//电脑先出牌
        update();//刷新属性
        RoundCard();
        RoundEnergy(game.player1.energy);
    }

    public  void end(){//回合结束
        //回合结算
        /*考虑添加动画*/
        game.checkCard();
        result=isWin();
        if(result!=null){
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Button again = new Button("下一难度");
            Button still = new Button("当前难度");
            Button exit = new Button("退出游戏");
            again.setOnAction(e->{
                comHp+=10;
                Energy= Energy>=4?Energy:Energy+1;//上限最多2+4格能量
                initialize();
                stage.close();
            });
            still.setOnAction(e->{
                initialize();
                stage.close();
            });
            exit.setOnAction(e->{
                System.exit(0);
            });


            ImageView last = new ImageView(result);//图片
            last.setFitWidth(250);
            last.setFitHeight(150);

            HBox hbox = new HBox();//按钮水平排列
            hbox.getChildren().addAll(again,still,exit);
            hbox.setSpacing(10);
            FlowPane flowpane = new FlowPane();//行布局
            flowpane.getChildren().addAll(last,hbox);
            Scene scene = new Scene(flowpane,275,180);
            flowpane.setAlignment(Pos.CENTER);
            flowpane.setPadding(new Insets(0));
            flowpane.setHgap(0);

            stage.setScene(scene);
            stage.showAndWait();//弹出窗口
        }
        else start();

    }

    private Image isWin() {
        if(game.player1.health>0&&game.computer.health<=0)
            return new Image("win.png");//win
        else if(game.player1.health<=0&&game.computer.health>0)
            return new Image("lose.png");//lose
        else if(game.player1.health<=0)
            return new Image("draw.png");//平局
        return null;
    }

    private void playCom() {
        //电脑随机出一张牌
        Card skillCom = game.CLibrary.get(game.rand.nextInt(4));
        skillCom.mediaPlayer.play();
        comCard = CreateCard(skillCom);
        comCard.setOnMouseClicked(null);
        comCard.setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.millis(500), comCard);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        ft.setOnFinished(e->{
           FadeTransition ft2 = new FadeTransition(Duration.millis(500), comCard);
           ft2.setFromValue(1.0);
           ft2.setToValue(0.0);
           ft2.setDelay(Duration.millis(1000));
           ft2.play();
           ft2.setOnFinished(e2->{
               comdiscard.setImage(comCard.getImage());//动画完成后放入电脑弃牌堆
           });
        });

        center.setCenter(comCard);

        if (skillCom.attribute.equals("s")) {
            game.player1.health -= skillCom.value;
            game.computer.health -= skillCom.value;
        } else
            game.computer.effect(skillCom);
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
        //依据属性选择卡牌图片
        Image image = switch (card.attribute) {
            case "a" -> new Image("attack.png");
            case "d" -> new Image("defense.png");
            case "h" -> new Image("health.png");
            case "s" -> new Image("hurt.png");
            default -> null;
        };
        return getImageView(image,card);
    }

    //设置卡牌图片属性
    private ImageView getImageView(Image image,Card card2) {
        ImageView card = new ImageView(image);
        card.setFitHeight(100);
        card.setFitWidth(100);
        //设置鼠标悬浮样式（也可以用css设置）
        card.setCursor(Cursor.HAND);
        card.setOnMouseEntered(e->{
            card.setFitHeight(120);
            card.setFitWidth(120);
            pass.play();
        });
        card.setOnMouseExited(e->{
            card.setFitWidth(100);
            card.setFitHeight(100);
        });

        card.setOnMouseClicked(e->{//点击卡牌即打出
            //打入中央
            cardClick.play();
            if (game.player1.energy<card2.cost)
            {
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
            card2.mediaPlayer.play();
            center.setCenter(card);
            //设置打入中央后的淡出动画
            FadeTransition transition = new FadeTransition(Duration.seconds(2),card);
            transition.setToValue(0);
            transition.play();
            transition.setOnFinished(e2->{//动画完成后放入弃牌堆
                discard.setImage(card.getImage());
            });
            if(card2.attribute.equalsIgnoreCase("s"))
            {
                game.player1.energy-=card2.cost;
                game.player1.health-=card2.value;
                game.computer.health-=card2.value;
            }
            game.player1.effect(card2);//卡牌对palyer1生效
            //刷新属性和能量
            update();
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

    private void update() {//刷新属性
        attack.setText(Integer.toString(game.player1.attack));
        defense.setText(Integer.toString(game.player1.defense));
        health.setText(Integer.toString(game.player1.health));
        comhealth.setText(Integer.toString(game.computer.health));
        comattack.setText(Integer.toString(game.computer.attack));
        comdefense.setText(Integer.toString(game.computer.defense));
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