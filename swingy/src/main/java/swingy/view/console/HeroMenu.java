package swingy.view.console;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import swingy.controller.InputContext;
import swingy.controller.InputValidator;
import swingy.controller.JsonParser;
import swingy.model.Hero;
import swingy.model.HeroClass;
import swingy.view.MenuAction;

public class HeroMenu implements InputContext{
    private static final String TITLE = """
( ___ )--------------------------------------------------------------( ___ )
 |   |                                                                |   | 
 |   |    ▄████████  ▄█     █▄   ▄█  ███▄▄▄▄      ▄██████▄  ▄██   ▄   |   | 
 |   |   ███    ███ ███     ███ ███  ███▀▀▀██▄   ███    ███ ███   ██▄ |   | 
 |   |   ███    █▀  ███     ███ ███▌ ███   ███   ███    █▀  ███▄▄▄███ |   | 
 |   |   ███        ███     ███ ███▌ ███   ███  ▄███        ▀▀▀▀▀▀███ |   | 
 |   | ▀███████████ ███     ███ ███▌ ███   ███ ▀▀███ ████▄  ▄██   ███ |   | 
 |   |          ███ ███     ███ ███  ███   ███   ███    ███ ███   ███ |   | 
 |   |    ▄█    ███ ███ ▄█▄ ███ ███  ███   ███   ███    ███ ███   ███ |   | 
 |   |  ▄████████▀   ▀███▀███▀  █▀    ▀█   █▀    ████████▀   ▀█████▀  |   | 
 |___|                                                                |___| 
(_____)--------------------------------------------------------------(_____)
		""";

    public static final String[] HERO_OPTIONS = {
        "Assassin",
        "Warrior",
        "Mage"
    };

    private MenuAction lastAction = MenuAction.NONE;
	private static int selectedOption = 0;
    private static final String SAVE_FOLDER = "saves";


	public Hero createHero() {
        Scanner scanner = new Scanner(System.in);
        clearScreen();

        final String[] nameHolder = new String[1]; // Final array, mutable content
        while (true) {
            System.out.print("Enter your hero's name: ");
            String input = scanner.nextLine();
            nameHolder[0] = InputValidator.validateStringInput(input);
            if (nameHolder[0] != null) {
                List<Path> saves = getSaveFiles();
                boolean nameExists = saves.stream()
                    .map(path -> path.getFileName().toString().replace(".json", ""))
                    .anyMatch(existingName -> existingName.equalsIgnoreCase(nameHolder[0]));

                if (nameExists) {
                    System.out.print("A hero with the name '" + nameHolder[0] + "' already exists! Do you want to overwrite it? (yes/no): ");
                    String overwrite = scanner.nextLine().trim().toLowerCase();
                    if (!overwrite.equals("yes")) {
                        continue;
                    }
                }
                break; // Valid and unique name, or confirmed overwrite
            }
        }

        String name = nameHolder[0];
        
        HeroClass selectedClass = selectHeroClass();

        Hero newHero = new Hero(name, selectedClass);
        newHero.saveHero();
        return newHero;
    }

    public Hero selectSave() {
        clearScreen();
    
        List<Path> saves = getSaveFiles();
        if (saves.isEmpty()) {
            System.out.println("No saved heroes found!");
            return null;
        }
    
        System.out.println("Select a saved hero:");
        for (int i = 0; i < saves.size(); i++) {
            try {
                // Read JSON file as string
                String json = Files.readString(saves.get(i));
                // Parse JSON manually
                JsonParser.JsonStats stats = JsonParser.parseJsonStats(json);
    
                if (stats != null) {
                    // Remove .json extension from file name
                    String fileName = saves.get(i).getFileName().toString().replace(".json", "");
    
                    // Display with stats
                    System.out.printf(
                        "[%d] %s (%s, Level: %d, HP: %d, Attack: %d, Defense: %d)\n",
                        i + 1,
                        fileName,
                        stats.className,
                        stats.level,
                        stats.health,
                        stats.attackPower,
                        stats.defense
                    );
                } else {
                    System.out.printf("[%d] %s (Error parsing hero data)\n", i + 1, saves.get(i).getFileName());
                }
            } catch (IOException e) {
                System.err.println("Error reading file " + saves.get(i).getFileName() + ": " + e.getMessage());
                System.out.printf("[%d] %s (Error loading hero data)\n", i + 1, saves.get(i).getFileName());
            }
        }
    
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice < 1 || choice > saves.size()) {
            System.out.print("Enter a number: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next(); // Clear invalid input
                continue;
            }
        }
    
        return loadHero(saves.get(choice - 1)); // Full load only for the selected hero
    }
    
    // private static class JsonStats {
    //     String className;
    //     int level;
    //     int health;
    //     int attackPower;
    //     int defense;
    
    //     JsonStats(String className, int level, int health, int attackPower, int defense) {
    //         this.className = className;
    //         this.level = level;
    //         this.health = health;
    //         this.attackPower = attackPower;
    //         this.defense = defense;
    //     }
    // }
    
    // private JsonStats parseJsonStats(String json) {
    //     try {
    //         // Find start of JSON object
    //         int start = json.indexOf('{');
    //         int end = json.lastIndexOf('}');
    //         if (start == -1 || end == -1 || start >= end) return null;
    
    //         String content = json.substring(start + 1, end).trim();
    //         String[] pairs = content.split(",(?=\\s*[^\\s]*\\s*:)");
    
    //         String className = "Unknown";
    //         int level = 0, health = 0, attackPower = 0, defense = 0;
    
    //         for (String pair : pairs) {
    //             pair = pair.trim();
    //             if (pair.startsWith("\"heroClass\"")) {
    //                 className = extractValue(pair).replace("\"", "");
    //             } else if (pair.startsWith("\"level\"")) {
    //                 level = Integer.parseInt(extractValue(pair));
    //             } else if (pair.startsWith("\"baseHP\"")) {
    //                 health = Integer.parseInt(extractValue(pair));
    //             } else if (pair.startsWith("\"attack\"")) {
    //                 attackPower = Integer.parseInt(extractValue(pair));
    //             } else if (pair.startsWith("\"defense\"")) {
    //                 defense = Integer.parseInt(extractValue(pair));
    //             }
    //         }
    
    //         return new JsonStats(className, level, health, attackPower, defense);
    //     } catch (Exception e) {
    //         System.err.println("Failed to parse JSON: " + e.getMessage());
    //         return null;
    //     }
    // }
    
    // private String extractValue(String pair) {
    //     int colonIndex = pair.indexOf(':');
    //     if (colonIndex != -1) {
    //         return pair.substring(colonIndex + 1).trim();
    //     }
    //     return "";
    // }

    private Hero loadHero(Path saveFile) {
        try {
            String json = Files.readString(saveFile);
            return Hero.fromJSON(json);
        } catch (IOException e) {
            System.err.println("Error loading hero: " + e.getMessage());
        }
        return null;
    }

    private List<Path> getSaveFiles() {
        try {
            Path folder = Paths.get(SAVE_FOLDER);
            if (!Files.exists(folder)) {
                return Collections.emptyList();
            }

            return Files.list(folder)
                        .filter(file -> file.toString().endsWith(".json"))
                        .toList();
        } catch (IOException e) {
            System.err.println("Error retrieving save files: " + e.getMessage());
        }
        return Collections.emptyList();
    }


    private HeroClass selectHeroClass() {
        HeroClass[] classes = HeroClass.values();
        Scanner scanner = new Scanner(System.in);
        int selectedIndex = 0;

        while (true) {
            clearScreen();
            displayHeroClassMenu(classes, selectedOption);

            System.out.println("Use 'a' to move left, 'd' to move right, and 'enter' to select:");
            String input = scanner.nextLine().trim().toLowerCase();

            if ("a".equals(input)) {
                selectedOption = (selectedOption - 1 + classes.length) % classes.length; // Move left
            } else if ("d".equals(input)) {
                selectedOption = (selectedOption + 1) % classes.length; // Move right
            } else if ("".equals(input)) {
                return classes[selectedOption]; // Confirm selection
            } else {
                System.out.println("Invalid input! Use 'a', 'd', or 'enter'.");
            }
        }
    }

    private void displayHeroClassMenu(HeroClass[] classes, int selectedIndex) {
        System.out.println("\nSelect Your Hero Class:");
        for (int i = 0; i < classes.length; i++) {
            if (i == selectedIndex) {
                System.out.print("[" + classes[i].getName() + "] "); // Highlight selection
            } else {
                System.out.print(classes[i].getName() + " ");
            }
        }
        System.out.println("\n");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public MenuAction handleInput(String input){
        switch (input) {
            case "a":
                selectedOption = (selectedOption - 1 + HERO_OPTIONS.length) % HERO_OPTIONS.length;
                break;
            case "d":
                selectedOption = (selectedOption + 1) % HERO_OPTIONS.length;
                break;
				case "\r": // Enter key
				switch (selectedOption) {
					case 0:
						lastAction = MenuAction.START_NEW_CHAR;
						return lastAction;
					case 1:
						lastAction = MenuAction.START_NEW_CHAR;
						return lastAction;
					case 2:
						lastAction = MenuAction.START_NEW_CHAR;
						return lastAction;
				}
				break;
			default:
				break;
		}
		return MenuAction.NONE;
    }
}
