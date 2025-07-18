package swingy.model;

public class Artifact {
    private String name;
    private ArtifactType type;
    private int bonusValue;

    public Artifact(String name, ArtifactType type, int bonusValue) {
        this.name = name;
        this.type = type;
        this.bonusValue = bonusValue;
    }

    public String getName() {
        return name;
    }

    public ArtifactType getType() {
        return type;
    }

    public int getBonusValue() {
        return bonusValue;
    }

	public String toJSON() {
        return "{\"name\":\"" + name + "\",\"type\":\"" + type + "\",\"bonusValue\":" + bonusValue + "}";
    }

	public static Artifact fromJSON(String json) {
        String name = Hero.parseString(json, "name");
        ArtifactType type = ArtifactType.valueOf(Hero.parseString(json, "type"));
        int bonusValue = Hero.parseInt(json, "bonusValue");
        return new Artifact(name, type, bonusValue);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, +%d)", name, type, bonusValue);
    }
}
