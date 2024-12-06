package group.hx.cardgame;

public class Player {
    int energy;
    int attack;
    int defense;
    int health;

    Player(int health1) {
        health = health1;
        attack = 0;
        energy = 0;
        defense = 0;
    }

    void effect(Card c) {// 卡牌作效函数
        switch (c.attribute) {
            case "a":
                attack += c.value;
                energy -= c.cost;
                break;
            case "d":
                defense += c.value;
                energy -= c.cost;
                break;
            case "h":
                health += c.value;
                energy -= c.cost;
                break;
            default:
                break;
        }
    }
}
