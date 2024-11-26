package swingy.model;

public class Stats {
	private int level;
	private int experience;
	private int gold;

	public Stats(int level, int exp, int gold){
		this.level = level;
		this.experience = exp;
		this.gold = gold;
	}

	public void addExp(int exp){
		this.experience += exp;
	}

	public void addGold(int amount){
		gold += amount;
	}

	public void addLevel(){
		level += 1;
	}

	public int getExperience(){
		return this.experience;
	}

	public int getLevel(){
		return this.level;
	}

	public int getGold(){
		return this.gold;
	}
}
