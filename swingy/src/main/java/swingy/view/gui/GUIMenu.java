package swingy.view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import swingy.controller.InputContext;
import swingy.view.console.MenuAction;

public class GUIMenu extends Application implements InputContext{
	private MenuAction lastAction = MenuAction.NONE;

	@Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        Button newGameButton = new Button("New Game");
		Button savedGameButton = new Button("Load Save");
        Button quitButton = new Button("Quit");

        quitButton.setOnAction(e -> primaryStage.close());

        layout.getChildren().addAll(newGameButton, savedGameButton, quitButton);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Swingy Menu");
        primaryStage.show();
    }

    public static void launchGUI() {
        Application.launch(GUIMenu.class);
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
