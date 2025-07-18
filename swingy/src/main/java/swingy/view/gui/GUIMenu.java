package swingy.view.gui;

import java.util.function.Consumer;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import swingy.controller.GameState;
import swingy.controller.GameStateManager;
import swingy.controller.InputContext;
import swingy.model.Hero;
import swingy.view.MenuAction;   

public class GUIMenu extends Application implements InputContext{
	private MenuAction lastAction = MenuAction.NONE;
    private Consumer<MenuAction> actionListener;
    private GameStateManager stateManager;
    public VBox layout;
    private static GameStateManager sharedStateManager;
    public Stage primaryStage;
    private GUIHeroMenu guiHeroMenu;
    public static boolean isGuiLaunched = false;
    private Scene mainMenuScene;
    private Hero newHero;

    public GUIMenu(){}

    public static void setSharedStateManager(GameStateManager stateManager) {
        sharedStateManager = stateManager;
    }

	@Override
    public void start(Stage primaryStage) {
        this.stateManager = sharedStateManager; // Assign the static value to the instance
        this.primaryStage = primaryStage;
        if (this.stateManager == null) {
            throw new IllegalStateException("GameStateManager not initialized!");
        }

        Button newGameButton = new Button("New Game");
		Button savedGameButton = new Button("Load Save");
        Button quitButton = new Button("Quit");

        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(newGameButton, savedGameButton, quitButton);
        mainMenuScene = new Scene(layout, 900, 600);

        // Button actions
        newGameButton.setOnAction(e -> {stateManager.setState(GameState.CREATE_HERO); newHero = createHero();}); // reset the scene and put the new scene to show up gang gang
        savedGameButton.setOnAction(e -> {stateManager.setState(GameState.LOAD_SAVE); newHero = selectSave();});//
        quitButton.setOnAction(e -> {stateManager.setState(GameState.EXIT); 
            System.out.println("Application is closing...");
            isGuiLaunched = false; // Allow relaunch if needed
            System.exit(0);});

        this.primaryStage.setScene(mainMenuScene);
        this.primaryStage.setTitle("Swingy Menu");
        this.primaryStage.show();
        this.primaryStage.setOnCloseRequest(event -> {
            System.out.println("Application is closing...");
            isGuiLaunched = false; // Allow relaunch if needed
            System.exit(0); // Ensure complete application shutdown
        });
    }

    public void returnToMainMenu(){
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    public void setStateManager(GameStateManager stateManager){
        this.stateManager = stateManager;
    }

    public static void launchGUI() {
        if (!isGuiLaunched){
            isGuiLaunched = true;
            Application.launch(GUIMenu.class);
        } else {
            System.out.println("GUI is already running, updating view.");
        }
        System.out.println("ici on sort de launchgui");
    }

    public Hero createHero(){
        guiHeroMenu = new GUIHeroMenu(this);     
        return guiHeroMenu.createHero();
    }
	public Hero selectSave(){
        guiHeroMenu = new GUIHeroMenu(this);
        return guiHeroMenu.selectSave();
    }
	public MenuAction getLastAction(){
		return lastAction;
	}

	@Override
    public MenuAction handleInput(String input) {
        switch (input) {
            case "q":
				lastAction = MenuAction.QUIT;
                return lastAction;
			default:
				break;
		}
        return MenuAction.NONE; // Continue application
    }
}
