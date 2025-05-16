package swingy.view.console;

import java.util.Scanner;

import swingy.model.Hero;
import swingy.model.HeroClass;
import swingy.controller.InputContext;

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

	public Hero createHero() {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("Enter your hero's name:");
        String name = scanner.nextLine().trim();
        HeroClass selectedClass = selectHeroClass();
        return new Hero(name, selectedClass);
    }

    public Hero selectSave() {
        clearScreen();
        System.out.println("Select a saved hero:");
        HeroClass selectedClass = selectHeroClass(); // Add logic to load saved heroes if applicable
        return new Hero("name", selectedClass); // Replace with actual save logic
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
