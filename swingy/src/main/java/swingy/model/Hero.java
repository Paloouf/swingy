package swingy.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Hero {
	private String name;
	private HeroClass heroClass;
	private int level;
	private int experience;
	private int hitPoints;
    private int baseHP;
	private int attack;
	private int defense;
    private Map<ArtifactType, Artifact> equippedArtifacts;
	private int gold;
    private int x;
    private int y;
    private int previousX;
    private int previousY;
    private static final int MAX_LEVEL = 100;
	//private Stats myStats;
    private static final String SAVE_FOLDER = "saves";

	public Hero(String name, HeroClass heroClass){
		this.level = 1;
		this.experience = 0;
		this.name = name;
		this.heroClass = heroClass;
		this.attack = heroClass.getAttack();
        this.defense = heroClass.getDefense();
        this.hitPoints = heroClass.getHitPoints();
        this.baseHP = hitPoints;
		this.gold = 0;
        this.equippedArtifacts = new HashMap<>();
	}

    public void equipArtifact(Artifact newArtifact){
        ArtifactType type = newArtifact.getType();
        Artifact current = equippedArtifacts.get(type);

        if (current == null || newArtifact.getBonusValue() > current.getBonusValue()) {
            // Remove current bonus if any
            if (current != null) {
                removeArtifactBonus(current);
            }

            // Equip new artifact
            equippedArtifacts.put(type, newArtifact);
            applyArtifactBonus(newArtifact);
            System.out.println("Equipped better " + type + " artifact: " + newArtifact.getBonusValue());
        } else {
            System.out.println("Kept current " + type + " artifact, new one was weaker.");
        }
    }

    private void applyArtifactBonus(Artifact artifact) {
        switch (artifact.getType()) {
            case WEAPON -> attack += artifact.getBonusValue();
            case BODY -> defense += artifact.getBonusValue();
            case HELMET -> hitPoints += artifact.getBonusValue();
        }
    }

    public void unequipArtifact(ArtifactType type) {
        Artifact removed = equippedArtifacts.remove(type);
        if (removed != null) {
            removeArtifactBonus(removed);
        }
    }

    private void removeArtifactBonus(Artifact artifact) {
        switch (artifact.getType()) {
            case WEAPON -> attack -= artifact.getBonusValue();
            case BODY -> defense -= artifact.getBonusValue();
            case HELMET -> hitPoints -= artifact.getBonusValue();
        }
    }

    public String getFormattedGear() {
        if (equippedArtifacts == null || equippedArtifacts.isEmpty()) return "No gear equipped";
        
        StringBuilder formattedGear = new StringBuilder();
        boolean first = true;
        
        for (Map.Entry<ArtifactType, Artifact> entry : equippedArtifacts.entrySet()) {
            if (!first) {
                formattedGear.append(", ");
            }
            ArtifactType type = entry.getKey();
            Artifact artifact = entry.getValue();
            String name = artifact.getName();
            int stat = artifact.getBonusValue();
            // Determine stat label based on ArtifactType
            String statLabel;
            switch (type) {
                case HELMET:
                    statLabel = "HP";
                    break;
                case BODY:
                    statLabel = "Def";
                    break;
                case WEAPON:
                    statLabel = "Att";
                    break;
                default:
                    statLabel = ""; // Default case if type is unrecognized
                    break;
            }
            
            formattedGear.append(name).append(" (+").append(stat).append(" ").append(statLabel).append(")");
            first = false;
        }
        
        return formattedGear.toString();
    }

    public String getGear(){
        return equippedArtifacts.toString();
    }

	public int getNextLevelXP() {
        return level * 1000 + (int) Math.pow(level - 1, 2) * 450;
    }

    public void gainExperience(int amount) {
        if (level >= MAX_LEVEL) {
            System.out.println(name + " has reached the maximum level!");
            return;
        }

        System.out.println(name + " gained " + amount + " experience!");
        experience += amount;

        // Check for level-up
        while (experience >= getNextLevelXP() && level < MAX_LEVEL) {
            levelUp();
        }
    }

    public void heal(){
        hitPoints = baseHP;
    }

    public int getMaxHealth(){
        return this.baseHP;
    }

	public void levelUp(){
		if (experience >= getNextLevelXP()) {
            level++;
            attack += 5; // Example growth
            defense += 5;
            hitPoints += 10;
            baseHP = hitPoints;
        }
        System.out.println("You leveled up!");
	}

    public int getExperience(){
        return experience;
    }

	public int getLevel(){
		return level;
	}

	public int getAttackPower() {
        return this.attack;
    }

    public int getDefense() {
        return this.defense;
    }

    public String getClassName() {
        return this.heroClass.getName();
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

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public int getPreviousX() {
        return previousX;
    }

    public void setPreviousX(int previousX) {
        this.previousX = previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void setPreviousY(int previousY) {
        this.previousY = previousY;
    }

    public void setPosition(int x, int y) {
        // Update previous position before moving
        this.previousX = this.x;
        this.previousY = this.y;
        this.x = x;
        this.y = y;
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
        json.append("\"gold\":").append(gold).append(",");

        json.append("\"equippedArtifacts\":[");
        Iterator<Map.Entry<ArtifactType, Artifact>> iterator = equippedArtifacts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ArtifactType, Artifact> entry = iterator.next();
            json.append(entry.getValue().toJSON());
            if (iterator.hasNext()) { // Check if there's another element after this one
                json.append(",");
            }
        }
        json.append("]");
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
        int gold = Integer.parseInt(json.split("\"gold\":")[1].split(",")[0]);

        Hero hero = new Hero(name, heroClass);
        String artifactsJson = parseArray(json, "equippedArtifacts");
        if (!artifactsJson.isEmpty()) {
            String[] artifactJsonArray = artifactsJson.split("},\\{"); // Split artifacts
            for (String artifactJson : artifactJsonArray) {
                hero.equipArtifact(Artifact.fromJSON(fixJsonBrackets(artifactJson)));
            }
        }
 
        hero.level = level;
        hero.experience = experience;
        hero.hitPoints = hitPoints;
        hero.attack = attack;
        hero.defense = defense;
        hero.gold = gold;

        return hero;
    }

    // Helper methods for parsing JSON
    public static String parseString(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = json.indexOf(pattern) + pattern.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    public static int parseInt(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern) + pattern.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start); // Handle last element
        return Integer.parseInt(json.substring(start, end));
    }

    public static String parseArray(String json, String key) {
        String pattern = "\"" + key + "\":[";
        int start = json.indexOf(pattern) + pattern.length();
        int end = json.indexOf("]", start);
        return json.substring(start, end);
    }

    public static String fixJsonBrackets(String json) {
        if (!json.startsWith("{")) json = "{" + json;
        if (!json.endsWith("}")) json = json + "}";
        return json;
    }

    public void saveHero() {
        try {
            Files.createDirectories(Paths.get(SAVE_FOLDER));
            String fileName = SAVE_FOLDER + "/" + this.getName() + ".json";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(this.toJSON());
            }

            System.out.println("Hero saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving hero: " + e.getMessage());
        }
    }
}
