package group.hx.cardgame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Card {
    String attribute;// 卡牌属性(a.攻击 d.防御 h.恢复 s.共享受伤)
    int value;// 数值
    int cost;// 需要能量
    MediaPlayer mediaPlayer;

    Card(String c, int value1, int cost1,String mp3) {
        mediaPlayer = new MediaPlayer(new Media(getClass().getResource(mp3).toExternalForm()));
        mediaPlayer.setOnEndOfMedia(()->{
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.stop();
        });

        attribute = c;
        value = value1;
        cost = cost1;
    }
}
