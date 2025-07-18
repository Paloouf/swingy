package swingy.view.console;

import swingy.controller.InputContext;
import swingy.view.MenuAction;

public class ConsoleMenu implements InputContext{
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

	public static final String[] MENU_OPTIONS = {
			"New Game",
			"Load save",
			"Quit"
	};

	private MenuAction lastAction = MenuAction.NONE;
	private static int selectedOption = 0;

	private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

	private static void printTitle() {
        System.out.println(TITLE);
    }

	private static void printMenu() {
        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            if (i == selectedOption) {
                // Highlight the selected option in green
                System.out.println("\033[32m> " + MENU_OPTIONS[i] + "\033[0m");
            } else {
                // Print unselected options in white
                System.out.println("  " + MENU_OPTIONS[i]);
            }
        }
    }

	public void menu(){
		clearScreen();
		printTitle();
		printMenu();
	}

	public int getSelectedOption(){
		return selectedOption;
	}

	@Override
    public MenuAction handleInput(String input) {
        switch (input) {
            case "w":
                selectedOption = (selectedOption - 1 + MENU_OPTIONS.length) % MENU_OPTIONS.length;
                menu();
                break;
            case "s":
                selectedOption = (selectedOption + 1) % MENU_OPTIONS.length;
                menu();
                break;
				case "\r": // Enter key
				switch (selectedOption) {
					case 0:
						lastAction = MenuAction.NEW_GAME;
						return lastAction;
					case 1:
						lastAction = MenuAction.LOAD_SAVE;
						return lastAction;
					case 2:
						lastAction = MenuAction.QUIT;
						return lastAction;
				}
				break;
			default:
				break;
		}
		return MenuAction.NONE; // Continue application
    }
	// New method to get the last action
	public MenuAction getLastAction() {
		return lastAction;
	}



}


