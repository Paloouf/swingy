package swingy.view.console;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import swingy.controller.InputContext;
import swingy.controller.InputValidator;
import swingy.model.Hero;
import swingy.model.HeroClass;

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
        System.out.println("Enter your hero's name:");
        String name = InputValidator.validateStringInput(scanner.nextLine()); //problem tidingtiding engine kaput
        HeroClass selectedClass = selectHeroClass();

        Hero newHero = new Hero(name, selectedClass);
        saveHero(newHero);
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
            System.out.printf("[%d] %s\n", i + 1, saves.get(i).getFileName().toString());
        }

        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice < 1 || choice > saves.size()) {
            System.out.print("Enter a number: ");
            choice = scanner.nextInt();
        }

        return loadHero(saves.get(choice - 1));
    }

    private void saveHero(Hero hero) {
        try {
            Files.createDirectories(Paths.get(SAVE_FOLDER));
            String fileName = SAVE_FOLDER + "/" + hero.getName() + ".json";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(hero.toJSON());
            }

            System.out.println("Hero saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving hero: " + e.getMessage());
        }
    }

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
