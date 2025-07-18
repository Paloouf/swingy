package swingy.model;

import java.util.Random;

public class LootSystem {
    private static final Random random = new Random();

    public static Artifact rollForLoot() {
        // Step 1: Roll to determine if loot is acquired (50% chance)
        boolean lootAcquired = random.nextBoolean();
        if (!lootAcquired) {
            System.out.println("You found nothing.");
            return null; // No loot acquired
        }

        // Step 2: Roll for artifact type
        ArtifactType artifactType = rollArtifactType();

        // Step 3: Generate the artifact
        Artifact artifact = generateArtifact(artifactType);
        System.out.println("You found a " + artifact + "!");
        return artifact;
    }

    private static ArtifactType rollArtifactType() {
        int roll = random.nextInt(3); // 0, 1, or 2
        return switch (roll) {
            case 0 -> ArtifactType.WEAPON;
            case 1 -> ArtifactType.BODY;
            case 2 -> ArtifactType.HELMET;
            default -> throw new IllegalStateException("Unexpected value: " + roll);
        };
    }

    private static Artifact generateArtifact(ArtifactType type) {
        String name;
        int bonusValue;

        switch (type) {
            case WEAPON -> {
                String[] weaponNames = {"Excalibur", "Dragon Slayer", "Thunderblade"};
                name = weaponNames[random.nextInt(weaponNames.length)];
                bonusValue = random.nextInt(11) + 10; // Stat roll between 10 and 20
            }
            case BODY -> {
                String[] armorNames = {"Dragon Armor", "Shadow Robe", "Titan Plate"};
                name = armorNames[random.nextInt(armorNames.length)];
                bonusValue = random.nextInt(6) + 5; // Stat roll between 5 and 10
            }
            case HELMET -> {
                String[] helmetNames = {"Iron Helm", "Crown of Wisdom", "Helmet of Valor"};
                name = helmetNames[random.nextInt(helmetNames.length)];
                bonusValue = random.nextInt(16) + 5; // Stat roll between 5 and 20
            }
            default -> throw new IllegalStateException("Unexpected artifact type: " + type);
        }

        return new Artifact(name, type, bonusValue);
    }
}
