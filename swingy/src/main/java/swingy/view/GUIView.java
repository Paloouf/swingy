package swingy.view;

import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import swingy.controller.InputManager;
import swingy.model.Hero;
import swingy.model.HeroClass;
import swingy.view.gui.GUIMenu;
import swingy.view.console.MenuAction;
import swingy.model.Map;

public class GUIView extends View{
	private GUIMenu gui;
	//private Scanner scanner = new Scanner(System.in);

    public GUIView() {
        this.gui = new GUIMenu();
    }

	@Override
    public void MenuDisplay() {
		InputManager.setContext(gui);
        gui.launchGUI(); // Delegate to GUIMenu
		InputManager.startInputLoop();
    }

    @Override
    public void MapDisplay(Map map, Hero hero) {
        System.out.println("Displaying Map");
    }

	@Override
	public MenuAction getMenuChoice(){
		return gui.getLastAction();
	}


	@Override
	public Hero createHero(){
		System.out.println("Enter your hero's name:");
        //String name = scanner.nextLine();

        HeroClass selectedClass = selectHeroClass();
        return new Hero("name", selectedClass);
	}
	@Override
	public Hero selectSave(){
		System.out.println("Enter your hero's name:");
       // String name = scanner.nextLine();

        HeroClass selectedClass = selectHeroClass();
        return new Hero("name", selectedClass);
	}

	private HeroClass selectHeroClass() {
        HeroClass[] classes = HeroClass.values();
        int selectedIndex = 0;

        while (true) {
            displayHeroClassMenu(classes, selectedIndex);

            System.out.println("Use 'a' to move left, 'd' to move right, and 'enter' to select:");
           // String input = scanner.nextLine().trim().toLowerCase();

            if ("a".equals("input")) {
                selectedIndex = (selectedIndex - 1 + classes.length) % classes.length; // Move left
            } else if ("d".equals("input")) {
                selectedIndex = (selectedIndex + 1) % classes.length; // Move right
            } else if ("".equals("input")) {
                return classes[selectedIndex]; // Confirm selection
            } else {
                System.out.println("Invalid input! Use 'a', 'd', or 'enter'.");
            }
        }
    }

    public String getMoveInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Move (w/a/s/d): ");
		return scanner.nextLine().toLowerCase();
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
    public void CombatDisplay() {
        System.out.println("Displaying Combat");
    }

	
}
