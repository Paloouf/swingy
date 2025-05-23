package swingy.model;

public class Enemy {
	private String name;
	private int defense;
	private int level;
	private int health;
	private int attackPower;

	public Enemy(String name, int level, int attack, int defense, int health){
		this.name = name;
		this.level = level;
		this.attackPower = attack;
		this.defense = defense;
		this.health = health;
	}

	 // Method to simulate taking damage
    public void takeDamage(int damage) {
        int effectiveDamage = Math.max(0, damage - defense); // Reduce damage by defense
        this.health = Math.max(0, this.health - effectiveDamage); // Ensure health doesn't drop below zero
        System.out.println(name + " takes " + effectiveDamage + " damage. Health: " + this.health);
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
