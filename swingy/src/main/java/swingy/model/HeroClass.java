package swingy.model;

public enum HeroClass {
	ASSASSIN("Assassin", 15, 5, 40),
    WARRIOR("Warrior", 10, 15, 50),
    MAGE("Mage", 20, 5, 30);

    private final String name;
    private final int attack;
    private final int defense;
    private final int hitPoints;

    HeroClass(String name, int attack, int defense, int hitPoints) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.hitPoints = hitPoints;
    }

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getHitPoints() {
        return hitPoints;
    }
}
