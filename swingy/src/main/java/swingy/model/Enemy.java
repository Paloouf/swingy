package swingy.model;

import swingy.model.Hero;

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

	public void attack(Hero hero){}
}
