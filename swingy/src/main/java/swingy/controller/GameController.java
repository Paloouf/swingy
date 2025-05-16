package swingy.controller;

import java.util.Scanner;

import swingy.model.Hero;
import swingy.model.Map;
import swingy.view.View;
import swingy.view.console.ConsoleMenu;
import swingy.view.console.MenuAction;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class GameController implements InputContext{
	public boolean isRunning;
	public boolean pauseLoop;
	private Hero hero;
	private Map map;
	private View view;
	private View secondView;
	//private Scanner scanner;

	public GameController(View view, View secondView){
		this.view = view;
		this.secondView = secondView;
		this.isRunning = true;
		this.pauseLoop = true;
		//this.scanner = new Scanner(System.in);
	}

	public void gameloop(){
        GameState state = GameState.MENU;

        while (state != GameState.EXIT) {
            switch (state) {
                case MENU:
                    view.MenuDisplay();
                    MenuAction choice = view.getMenuChoice();
                    if (choice == MenuAction.NEW_GAME) {
                        state = GameState.CREATE_HERO;
                    } else if (choice == MenuAction.LOAD_SAVE) {
                        state = GameState.LOAD_SAVE;
                    } else if (choice == MenuAction.QUIT) {
                        state = GameState.EXIT;
                    }
                    break;
    
                case CREATE_HERO:
                    hero = view.createHero(); //this needs to have a inputmanager thing same as the menudisplay
                    if (hero != null) {
                        map = new Map(hero.getLevel());
                        state = GameState.MAP;
                    } else {
                        state = GameState.MENU; // Go back to menu if hero creation fails
                    }
                    break;
    
                case LOAD_SAVE:
                    hero = view.selectSave();
                    if (hero != null) {
                        map = new Map(hero.getLevel());
                        state = GameState.MAP;
                    } else {
                        state = GameState.MENU; // Go back to menu if loading fails
                    }
                    break;
    
                case MAP:
                    view.MapDisplay(map, hero); // Display the map and hero state
                    String input = view.getMoveInput(); // Handle movement input
                     if (processMovement(input)) {
                         state = GameState.EXIT; // Hero wins by reaching the map edge
                     }
                    break;
    
                default:
                    state = GameState.EXIT;
            }
        }
        System.out.println("Thanks for playing!");
    }

    private boolean processMovement(String input) {
        String direction;
        switch (input) {
            case "w": direction = "north"; break;
            case "s": direction = "south"; break;
            case "a": direction = "west";  break;
            case "d": direction = "east";  break;
            default:
                System.out.println("Invalid input!");
                return false;
        }
        
        boolean reachedEdge = map.moveHero(direction);
        if (reachedEdge) {
            System.out.println("You reached the edge of the map! Victory!");
        }
        return reachedEdge;
    }
    
	private void quit() {
        System.out.println("Quitting the game...");
        this.isRunning = false; // Ends the game loop
    }

	@Override
    public MenuAction handleInput(String input) {
        switch (input) {
            case "w":
                System.out.println("You moved forward!");
                break;
            case "a":
                System.out.println("You moved left!");
                break;
            case "s":
                System.out.println("You moved backward!");
                break;
            case "d":
                System.out.println("You moved right!");
                break;
            case "q":
                quit();
                return MenuAction.QUIT; // Exit application
            default:
                System.out.println("Invalid game command.");
        }
        return MenuAction.NONE; // Continue application
    }
}
