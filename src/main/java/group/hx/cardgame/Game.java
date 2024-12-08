package group.hx.cardgame;

import java.util.*;

public class Game {
     ArrayList<Card> CLibrary = new ArrayList<>();// 牌库
     HashMap<Integer, Card> cards = new HashMap<>();// 手牌
     Player player1 = new Player(10);// 玩家
     Player computer;// 电脑
     Random rand = new Random();
     Scanner input = new Scanner(System.in);

     void init(int hp) {
        computer = new Player(hp);
        // 初始化牌库
        CLibrary.add(new Card("a", 5, 2,"attack.mp3"));
        CLibrary.add(new Card("d", 3, 2,"defense.mp3"));
        CLibrary.add(new Card("h", 2, 1,"health.mp3"));
        CLibrary.add(new Card("s", 4, 3,"both.mp3"));
    }

    void makeCard() {// 随机发牌
        for (int i = 0; i < 3; i++) {
            cards.put(i + 1, CLibrary.get(rand.nextInt(4)));
        }
    }

    void checkCard() {// 回合结算
        player1.health -= computer.attack > player1.defense ? computer.attack - player1.defense : 0;// 受伤=攻击-防御
        computer.health -= player1.attack > computer.defense ? player1.attack - computer.defense : 0;
        player1.attack = 0;
        player1.defense = 0;
        computer.attack = 0;
        computer.defense = 0;
    }

}
