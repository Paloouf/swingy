package swingy.controller;

import javafx.application.Platform;
import swingy.model.Hero;
import swingy.model.Map;
import swingy.view.MenuAction;
import swingy.view.View;
import swingy.view.gui.GUIMenu;

public class GameController implements InputContext{
	public boolean isRunning;
	public boolean pauseLoop;
	private Hero hero;
	private Map map;
	private View view;
	private View secondView;
    private GameState state;
    private GUIMenu guiMenu;
    public GameStateManager stateManager;

	public GameController(View view, View secondView){
		this.view = view;
		this.secondView = secondView;
		this.isRunning = true;
		this.pauseLoop = true;
        this.state = GameState.MENU;
        stateManager = GameStateManager.getInstance();
	}

	public void gameloop(){
        while (stateManager.getState() != GameState.EXIT) {
            state = stateManager.getState();
            //System.out.println(state);
            switch (state) {
                case MENU:
                    view.MenuDisplay(stateManager);
                    MenuAction choice = view.getMenuChoice();
                    if (choice == MenuAction.NEW_GAME) {
                        stateManager.setState(GameState.CREATE_HERO);
                    } else if (choice == MenuAction.LOAD_SAVE) {
                        stateManager.setState(GameState.LOAD_SAVE);
                    } else if (choice == MenuAction.QUIT) {
                        stateManager.setState(GameState.EXIT);
                    }
                    break;
    
                case CREATE_HERO:
                    hero = view.createHero();
                    if (hero != null) {
                        map = new Map(hero);
                        stateManager.setState(GameState.MAP);
                    } else {
                        stateManager.setState(GameState.MENU); // Go back to menu if hero creation fails
                    }
                    break;
    
                case LOAD_SAVE:
                    hero = view.selectSave();
                    if (hero != null) {
                        map = new Map(hero);
                        stateManager.setState(GameState.MAP);
                    } else {
                        stateManager.setState(GameState.MENU); // Go back to menu if loading fails
                    }
                    break;
    
                case MAP:
                    view.MapDisplay(map, hero); // Display the map and hero state
                    String input = view.getMoveInput(); // Handle movement input
                     if (processMovement(input)) {
                         //state = GameState.EXIT; // Hero wins by reaching the map edge
                     }
                    break;
                case DIED:
                     //game over screen thingy
                     break;
                case EXIT:
                     handleExit();
                default:
                    stateManager.setState(GameState.EXIT);;
            }
        }
        System.out.println("Thanks for playing!");
    }

    private void handleExit(){
        isRunning = false;
        Platform.exit();
        System.exit(0);
    }

    private boolean processMovement(String input) {
        String direction;
        if (input.equals("quit")){
            quit();
        }
        if (input.equals("back")){
            GameStateManager.getInstance().setState(GameState.MENU);
        }
        switch (input) {
            case "w": direction = "north"; break;
            case "s": direction = "south"; break;
            case "a": direction = "west";  break;
            case "d": direction = "east";  break;
            default:
                System.out.println("");
                return false;
        }
        
        boolean reachedEdge = map.moveHero(direction, false);
        // if (reachedEdge) {
        //     map.generateNewMap();
        // }
        return reachedEdge;
    }
    
    public void handleMenuAction(MenuAction action) {
        switch (action) {
            case NEW_GAME:
                stateManager.setState(GameState.CREATE_HERO);
                break;
            case LOAD_SAVE:
                stateManager.setState(GameState.LOAD_SAVE);
                break;
            case QUIT:
                stateManager.setState(GameState.EXIT);
                break;
            default:
                break;
        }
    }

	private void quit() {
        hero.saveHero();
        System.out.println("Quitting the game...");
        stateManager.setState(GameState.EXIT);
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
                System.out.println("");
        }
        return MenuAction.NONE; // Continue application
    }
}
