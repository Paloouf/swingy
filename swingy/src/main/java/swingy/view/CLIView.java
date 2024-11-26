package swingy.view;

import java.util.Scanner;

import swingy.controller.InputManager;
import swingy.view.console.ConsoleMap;
import swingy.view.console.ConsoleMenu;
import swingy.view.console.MenuAction;
import swingy.model.Hero;
import swingy.model.HeroClass;
import swingy.model.Map;

public class CLIView extends View{
	private ConsoleMenu console;
	private ConsoleMap map;


	public CLIView(){
		this.console = new ConsoleMenu();
		this.map = new ConsoleMap();
	}

	@Override
	public void MenuDisplay(){
		InputManager.setContext(console);
		console.menu();
		InputManager.startInputLoop();
	}

	@Override
	public MenuAction getMenuChoice(){
		return console.getLastAction();
	}

	@Override
	public void MapDisplay(Map map, Hero hero){
		System.out.println("\033[H\033[2J"); // Clear screen
		System.out.flush();

		char[][] grid = map.getGrid();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("Hero: " + hero.getName() + " | Level: " + hero.getLevel());
		System.out.println("Controls: North (w), South (s), East (d), West (a)");
	}

	public String getMoveInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Move (w/a/s/d): ");
		return scanner.nextLine().toLowerCase();
	}

	@Override
	public Hero createHero(){
		Scanner scanner = new Scanner(System.in);
		System.out.print("\033[H\033[2J");
        System.out.flush();
		System.out.println("Enter your hero's name:");
		
        String name = scanner.nextLine().replaceAll("\\r", "").trim();
		//scanner.close();
        HeroClass selectedClass = selectHeroClass();
		
        return new Hero(name, selectedClass);
	}

	private HeroClass selectHeroClass() {
        HeroClass[] classes = HeroClass.values();
		Scanner scanner = new Scanner(System.in);
        int selectedIndex = 0;

        while (true) {
			System.out.print("\033[H\033[2J");
        	System.out.flush();
            displayHeroClassMenu(classes, selectedIndex);

            System.out.println("Use 'a' to move left, 'd' to move right, and 'enter' to select:");
            String input = scanner.nextLine().trim().toLowerCase();

            if ("a".equals(input)) {
                selectedIndex = (selectedIndex - 1 + classes.length) % classes.length; // Move left
            } else if ("d".equals(input)) {
                selectedIndex = (selectedIndex + 1) % classes.length; // Move right
            } else if ("".equals(input)) {
                return classes[selectedIndex]; // Confirm selection
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

	
	@Override
	public Hero selectSave(){
		//Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your hero's name:");
       // String name = scanner.nextLine();

        HeroClass selectedClass = selectHeroClass();
        return new Hero("name", selectedClass);
	}

	@Override
	public void CombatDisplay(){}

}
