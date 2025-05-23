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

	public int getAttackPower() {
        return this.attack;
    }

	public int getHealth(){
		return this.hitPoints;
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
		hitPoints -= damage;
		if (hitPoints < 0) hitPoints = 0;
		System.out.println("You take " + damage + " damage. Health: " + this.hitPoints);
	}

	// Convert object to JSON string
    public String toJSON() {
        StringBuilder json = new StringBuilder("{");

        json.append("\"name\":\"").append(name).append("\",");
        json.append("\"heroClass\":\"").append(heroClass.name()).append("\","); // Convert enum to string
        json.append("\"level\":").append(level).append(",");
        json.append("\"experience\":").append(experience).append(",");
        json.append("\"hitPoints\":").append(hitPoints).append(",");
        json.append("\"attack\":").append(attack).append(",");
        json.append("\"defense\":").append(defense).append(",");
        json.append("\"gold\":").append(gold);

        json.append("}");
        return json.toString();
    }

    // Parse JSON string back into a Character object
    public static Hero fromJSON(String json) {
        String name = json.split("\"name\":\"")[1].split("\"")[0];
        String heroClassStr = json.split("\"heroClass\":\"")[1].split("\"")[0];
        HeroClass heroClass = HeroClass.valueOf(heroClassStr); // Convert string back to enum

        int level = Integer.parseInt(json.split("\"level\":")[1].split(",")[0]);
        int experience = Integer.parseInt(json.split("\"experience\":")[1].split(",")[0]);
        int hitPoints = Integer.parseInt(json.split("\"hitPoints\":")[1].split(",")[0]);
        int attack = Integer.parseInt(json.split("\"attack\":")[1].split(",")[0]);
        int defense = Integer.parseInt(json.split("\"defense\":")[1].split(",")[0]);
        int gold = Integer.parseInt(json.split("\"gold\":")[1].split("}")[0]);

        Hero hero = new Hero(name, heroClass);
        hero.level = level;
        hero.experience = experience;
        hero.hitPoints = hitPoints;
        hero.attack = attack;
        hero.defense = defense;
        hero.gold = gold;

        return hero;
    }
}
