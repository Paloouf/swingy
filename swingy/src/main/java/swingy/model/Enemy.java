package swingy.model;

import java.util.Random;

public class Enemy {
	private String name;
	private int defense;
	private int level;
	private int health;
    private int maxHealth;
	private int attackPower;
    private static final Random random = new Random();
    private static final String[] FIRST_NAMES = {"Glorgle", "Zorak", "Thrag", "Krum", "Vex"};
    private static final String[] LAST_NAMES = {"the Goblin", "the Ogre", "the Behemoth", "the Troll", "the Wraith"};

	public Enemy(String name, int level, int attack, int defense, int health){
		this.name = name;
		this.level = level;
		this.attackPower = attack;
		this.defense = defense;
		this.health = health;
        this.maxHealth = health;
	}

    public static Enemy createEnemy(Hero hero) {
        // Base level with random offset (-2 to +2)
        int baseLevel = hero.getLevel();
        int levelOffset = random.nextInt(5) - 2; // -2 to +2
        int level = Math.max(1, baseLevel + levelOffset); // Ensure level is at least 1

        // Randomize name
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String enemyName = firstName + " " + lastName;

        // Base stats with random variation (±20%)
        int attack = (int) (level * 2 * (0.8 + 0.4 * random.nextDouble())); // 1.6 to 2.4 * level
        int defense = (int) (level * (0.8 + 0.4 * random.nextDouble()));    // 0.8 to 1.2 * level
        int health = (int) (level * 10 * (0.8 + 0.4 * random.nextDouble())); // 8 to 12 * level

        return new Enemy(enemyName, level, attack, defense, health);
    }

	// Method to simulate taking damage
    public void takeDamage(int damage) {
        int effectiveDamage = Math.max(0, damage - defense); // Reduce damage by defense
        this.health = Math.max(0, this.health - effectiveDamage); // Ensure health doesn't drop below zero
        System.out.println(name + " takes " + effectiveDamage + " damage. Health: " + this.health);
    }

    public int getMaxHealth(){
        return maxHealth;
    }
    public int getLevel(){
        return level;
    }
    // Getters for the fields
    public int getHealth() {
        return this.health;
    }

    public String getName() {
        return this.name;
    }

    public int getAttackPower() {
        return this.attackPower;
    }
}
