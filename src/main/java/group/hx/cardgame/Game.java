package group.hx.cardgame;

import java.util.*;

public class Game {
     ArrayList<Card> CLibrary = new ArrayList<>();// 牌库
     HashMap<Integer, Card> cards = new HashMap<>();// 手牌
     Player player1 = new Player(10);// 玩家
     Player computer = new Player(10);// 电脑
     Random rand = new Random();
     Scanner input = new Scanner(System.in);

     void init() {// 初始化牌库
        CLibrary.add(new Card("a", 5, 2));
        CLibrary.add(new Card("d", 3, 2));
        CLibrary.add(new Card("h", 2, 1));
        CLibrary.add(new Card("s", 4, 3));
    }

    void play(){
        while (player1.health > 0 && computer.health > 0) {
            player1.energy = rand.nextInt(3) + 2;
            makeCard();
            show();
            playCard();
            playCom();
            checkCard();
        }
        if (player1.health <= 0 && computer.health <= 0)
            System.out.println("平局！");
        else if (computer.health <= 0)
            System.out.println("你赢了！");
        else
            System.out.println("你输了！");
    }

     void playCom() {// 电脑每回合随机出一张牌
        Card skillCom = CLibrary.get(rand.nextInt(4));
        if (skillCom.attribute.equals("s")) {
            player1.health -= skillCom.value;
            computer.health -= skillCom.value;
            player1.energy -= skillCom.cost;
        } else
            computer.effect(skillCom);
        System.out.println("本回合电脑随机出牌为：" + skillCom.attribute);
    }

     void show() {// 回合初始展示
        System.out.println("-------------------------------");
        System.out.println("生命值：player1:" + player1.health + " computer:" + computer.health);
        System.out.println("本回合能量：" + player1.energy);
        System.out.println("-------------------------------");
    }

     void makeCard() {// 随机发牌
        for (int i = 0; i < 3; i++) {
            cards.put(i + 1, CLibrary.get(rand.nextInt(4)));
        }
    }

     void playCard() {// 出牌函数
        showCard();
        while (true) {
            System.out.print("请输入下一张 要出的牌:(输入4结束回合)");

            Card nCard;
            try {// 检测输入错误...
                int choice = input.nextInt();
                if (choice == 4) {// 结束回合
                    cards.clear();
                    return;
                }
                try {
                    nCard = cards.remove(choice);// 根据选择移除手牌
                }catch (ConcurrentModificationException e){
                    System.out.println("卡牌已经被打出！");
                    continue;
                }
                if (player1.energy < nCard.cost) {// 能量不足
                    System.out.println("能量不足。");
                    continue;
                }
                if (nCard.attribute.equals("s")) {// 群体受伤
                    computer.health -= nCard.value;
                    player1.health -= nCard.value;
                    player1.energy -= nCard.cost;
                } else
                    player1.effect(nCard);
            } catch (InputMismatchException e) {
                System.err.println("输入错误......");
                String x = input.next();// 吸收错误输入
            }

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

     void showCard() {// 展示当前手牌
        System.out.println("当前手牌：");
        for (int i = 1; i < 4; i++) {
            System.out.print(i);
            System.out.print("、" + cards.get(i).attribute + "\t");
        }
        System.out.println();
    }
}
