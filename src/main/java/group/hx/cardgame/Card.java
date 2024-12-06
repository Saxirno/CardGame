package group.hx.cardgame;

public class Card {
    String attribute;// 卡牌属性(a.攻击 d.防御 h.恢复 s.共享受伤)
    int value;// 数值
    int cost;// 需要能量

    Card(String c, int value1, int cost1) {
        attribute = c;
        value = value1;
        cost = cost1;
    }
}
