package swingy.model;

public class Hero {
	private String name;
	private HeroClass heroClass;
	private int level;
	private int experience;
	private int hitPoints;
	private int attack;
	private int defense;
	private int gold;
	//private Stats myStats;

	public Hero(String name, HeroClass heroClass){
		this.level = 1;
		this.experience = 0;
		this.name = name;
		this.heroClass = heroClass;
		this.attack = heroClass.getAttack();
        this.defense = heroClass.getDefense();
        this.hitPoints = heroClass.getHitPoints();
		this.gold = 0;
		System.out.println("HERO CREATED");
	}

	public int getNextLevelXP() {
        return level * 1000 + (int) Math.pow(level - 1, 2) * 450;
    }

	public void levelUp(){
		if (experience >= getNextLevelXP()) {
            level++;
            attack += 5; // Example growth
            defense += 5;
            hitPoints += 10;
        }
	}

	public int getLevel(){
		return level;
	}

	public String getStats() {
        return String.format(
            "Name: %s\nClass: %s\nLevel: %d\nExperience: %d\nAttack: %d\nDefense: %d\nHP: %d",
            name, heroClass.getName(), level, experience, attack, defense, hitPoints
        );
    }
	
	public String getName(){
		return name;
	}
	
	public void takeDamage(int damage){

	}
}
