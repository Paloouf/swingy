package swingy.view.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import swingy.model.Hero;
import swingy.model.Map;
public class GUIMap {
	public Map map;
	public GridPane mapGrid;
	public Hero hero;
	private Button upButton;
	private Button downButton;
	private Button leftButton;
	private Button rightButton;
	private BorderPane mainContent;
	private StackPane rootPane;
	private Stage stage;
	private Scene mapScene;

	//to update after fights
	private VBox heroInfo;
	private Label levelLabel, levelValue;
	private Label gearLabel, gearValue;
	private Label statsLabel;
	private VBox statsBox;

	private final GUIHeroMenu app;

	public GUIMap(GUIHeroMenu app){
		this.app = app;
		this.map = new Map(app.getHero());
		this.hero = app.getHero(); 
		this.stage = app.primaryStage;
		map.setEncounterListener(hero -> {
			Platform.runLater(() -> {
				//pauseInputs();
				startFight(hero);
			});
    	});
		displayMapGUI(app.primaryStage, map, hero);
	}

	public void displayMapGUI(Stage primaryStage, Map map, Hero hero) {
		// Root layout
		rootPane = new StackPane();
		mainContent = new BorderPane();
		
		// 1. Hero Info (Top)
		if (heroInfo == null) { // Create only if not initialized
			heroInfo = createHeroInfo(hero);
		} else {
			updateHeroInfo(hero); // Update existing heroInfo
		}
		mainContent.setTop(heroInfo);

		// Center: Map + Controls side-by-side	
		mapGrid = createMapGrid(map, hero, 10);
		VBox controls = createControls();
		GridPane centerContent = new GridPane();
		centerContent.setHgap(20);
		centerContent.setVgap(10);
		centerContent.setAlignment(Pos.CENTER);
		centerContent.add(mapGrid, 0, 0);
		centerContent.add(controls, 1, 0);
		//centerContent.setPadding(new Insets(10));
		mainContent.setCenter(centerContent);

		// 4. Fight/Flee Buttons (Bottom Left)
		HBox actionButtons = createActionButtons();
		mainContent.setBottom(actionButtons);

		rootPane.getChildren().add(mainContent);
		// Scene and Stage
		mapScene = new Scene(rootPane, 900, 600);

		// Load CSS from resources
		String cssPath = getClass().getResource("/assets/style.css").toExternalForm();
		if (cssPath != null) {
			mapScene.getStylesheets().add(cssPath);
		} else {
			System.err.println("CSS file not found at /assets/style.css");
		}

		stage.setTitle("Swingy Map");
		stage.setScene(mapScene);
		stage.show();
	}

	public void toggleActionButtons(HBox actions, boolean encounter) {
    	actions.getChildren().forEach(node -> node.setVisible(encounter));
	}

	private GridPane createMapGrid(Map map, Hero hero, int viewsize) {
		GridPane grid = new GridPane();
		char[][] gridData = map.getGrid();
		int mapHeight = gridData.length;
		int mapWidth = gridData[0].length;

		int halfView = viewsize / 2;

		for (int rowOffset = -halfView; rowOffset <= halfView; rowOffset++) {
			for (int colOffset = -halfView; colOffset <= halfView; colOffset++) {
				int mapRow = hero.getX() + rowOffset;
				int mapCol = hero.getY() + colOffset;

				char symbol;
				Label cell;
				if (mapRow >= 0 && mapRow < mapHeight && mapCol >= 0 && mapCol < mapWidth) { //inside map bounds
					symbol = gridData[mapRow][mapCol];
					cell = new Label(String.valueOf(symbol));
					cell.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-font-size: 14px;");
				} else { // outside map bounds
					cell = new Label(" ");
					cell.setStyle("-fx-background-color: #555; -fx-border-color: black; -fx-alignment: center; -fx-font-size: 14px;");
				}
				cell.setPrefSize(30, 30);
				grid.add(cell, colOffset + halfView, rowOffset + halfView);
			}
		}

		grid.setPadding(new Insets(10));
		grid.setHgap(1);
		grid.setVgap(1);
		grid.setMaxSize(viewsize * 31, viewsize * 31);

		return grid;
	}

	private void refreshMap(GridPane mapGrid, Map map, Hero hero) {
		mapGrid.getChildren().clear();
		GridPane newGrid = createMapGrid(map, hero, 10); // Example: 5x5 viewport
		mapGrid.getChildren().addAll(newGrid.getChildren());
	}


	private VBox createHeroInfo(Hero hero) {
		heroInfo = new VBox(5); // Make heroInfo a class field if not already
		heroInfo.getStyleClass().add("hero-info");

		// Title
		Label title = new Label("Hero Info");
		title.getStyleClass().add("hero-title");
		heroInfo.getChildren().add(title);

		// Level
		levelLabel = new Label("Level:"); // Class field
		levelLabel.getStyleClass().add("stat-label");
		levelValue = new Label(String.valueOf(hero.getLevel())); // Class field
		levelValue.getStyleClass().add("stat-value key-stat");
		HBox levelBox = new HBox(levelLabel, levelValue);
		heroInfo.getChildren().add(levelBox);

		// Gear
		gearLabel = new Label("Gear:"); // Class field
		gearLabel.getStyleClass().add("stat-label");
		gearValue = new Label(hero.getFormattedGear()); // Class field
		gearValue.getStyleClass().add("stat-value");
		HBox gearBox = new HBox(gearLabel, gearValue);
		heroInfo.getChildren().add(gearBox);

		// Stats
		statsLabel = new Label("Stats:"); // Class field
		statsLabel.getStyleClass().add("stat-label");
		statsBox = new VBox(2); // Class field
		statsBox.getChildren().addAll(
			createStatRow("Name:", hero.getName(), "Class:", hero.getClassName()),
			createStatRow("Level:", String.valueOf(hero.getLevel()), "Experience:", String.valueOf(hero.getExperience())),
			createStatRow("Attack:", String.valueOf(hero.getAttackPower()), "Defense:", String.valueOf(hero.getDefense())),
			createStatRow("HP:", String.valueOf(hero.getHealth()), "", "")
		);
		statsBox.getChildren().forEach(node -> {
			if (node instanceof HBox) {
				((HBox) node).getChildren().forEach(child -> {
					if (child instanceof Label) {
						((Label) child).getStyleClass().add("stat-value");
						if (((Label) child).getText().matches("\\d+")) {
							((Label) child).getStyleClass().add("key-stat");
						}
					}
				});
			}
		});
		heroInfo.getChildren().add(statsBox);

		return heroInfo;
	}

	// Helper method to create stat rows
	private HBox createStatRow(String label1, String value1, String label2, String value2) {
		Label l1 = new Label(label1);
		l1.getStyleClass().add("stat-label");
		Label v1 = new Label(value1);
		v1.getStyleClass().add("stat-value");
		if (value1.matches("\\d+")) v1.getStyleClass().add("key-stat");
		Label l2 = new Label(label2);
		l2.getStyleClass().add("stat-label");
		Label v2 = new Label(value2);
		v2.getStyleClass().add("stat-value");
		if (value2.matches("\\d+")) v2.getStyleClass().add("key-stat");
		return new HBox(l1, v1, l2, v2);
	}

	public void updateHeroInfo(Hero hero) {
		if (heroInfo == null) return; // Ensure heroInfo is initialized

		levelValue.setText(String.valueOf(hero.getLevel()));
		gearValue.setText(hero.getFormattedGear());
		
		// Update stats box
		statsBox.getChildren().clear();
		statsBox.getChildren().addAll(
			createStatRow("Name:", hero.getName(), "Class:", hero.getClassName()),
			createStatRow("Level:", String.valueOf(hero.getLevel()), "Experience:", String.valueOf(hero.getExperience())),
			createStatRow("Attack:", String.valueOf(hero.getAttackPower()), "Defense:", String.valueOf(hero.getDefense())),
			createStatRow("HP:", String.valueOf(hero.getHealth()), "", "")
		);
		statsBox.getChildren().forEach(node -> {
			if (node instanceof HBox) {
				((HBox) node).getChildren().forEach(child -> {
					if (child instanceof Label) {
						((Label) child).getStyleClass().add("stat-value");
						if (((Label) child).getText().matches("\\d+")) {
							((Label) child).getStyleClass().add("key-stat");
						}
					}
				});
			}
		});
	}

	private VBox createControls() {
		GridPane controls = new GridPane();
		controls.setHgap(10);
		controls.setVgap(10);
		controls.setAlignment(Pos.CENTER);

		//Label controlsLabel = new Label("Controls");
		upButton = new Button("");
		downButton = new Button("");
		leftButton = new Button("");
		rightButton = new Button("");

		//controls.add(controlsLabel, 1, 0);
		controls.add(upButton, 1, 1);    // Center of top row
		controls.add(downButton, 1, 3);   // Center of bottom row
		controls.add(leftButton, 0, 2);   // Left of bottom row
		controls.add(rightButton, 2, 2);  // Right of bottom row
		

		upButton.getStyleClass().add("arrow-up");
		downButton.getStyleClass().add("arrow-down");
		leftButton.getStyleClass().add("arrow-left");
		rightButton.getStyleClass().add("arrow-right");

		upButton.setPrefSize(30, 30);
		downButton.setPrefSize(30, 30);
		leftButton.setPrefSize(30, 30);
		rightButton.setPrefSize(30, 30);

		upButton.setOnAction(e -> moveHero("north", mapGrid)); // Move up
        downButton.setOnAction(e -> moveHero("south", mapGrid)); // Move down
        leftButton.setOnAction(e -> moveHero("west", mapGrid)); // Move left
        rightButton.setOnAction(e -> moveHero("east", mapGrid)); // Move right

		controls.setMaxSize(200, 200);

		VBox controlsBox = new VBox(10, controls);  // 10px spacing
		controlsBox.setAlignment(Pos.CENTER);

		//controls.getChildren().addAll(controlsLabel, upButton, downButton, leftButton, rightButton);
		return controlsBox;
	}

	private void moveHero(String direction, GridPane mapGrid) {
        map.moveHero(direction, true);
        refreshMap(mapGrid, map, hero);
    }


	private HBox createActionButtons() {
		HBox actions = new HBox(10);
		actions.setPadding(new Insets(10));
		actions.setAlignment(Pos.BOTTOM_LEFT);

		Button fightButton = new Button("Fight");
		Button fleeButton = new Button("Flee");

		fightButton.setVisible(false); // Hide by default
		fleeButton.setVisible(false); // Hide by default

		actions.getChildren().addAll(fightButton, fleeButton);

		// Add logic to show/hide based on encounters
		return actions;
	}

	// private void pauseInputs() {
	// 	// Logic to pause the movement controls
	// 	upButton.setDisable(true);
	// 	downButton.setDisable(true);
	// 	leftButton.setDisable(true);
	// 	rightButton.setDisable(true);
	// }

	private void resumeInputs() {
		// Logic to resume the movement controls
		upButton.setDisable(false);
		downButton.setDisable(false);
		leftButton.setDisable(false);
		rightButton.setDisable(false);
	}

	private void displayFightMenu(Hero hero) {
		HBox fightMenu = new HBox(10);
		fightMenu.setPadding(new Insets(10));
		fightMenu.setAlignment(Pos.BOTTOM_LEFT);
		fightMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

		Label encounterLabel = new Label("You've encountered an enemy!");
		encounterLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
		Button fightButton = new Button("Fight");
		Button fleeButton = new Button("Flee");

		fightButton.setOnAction(event -> {
			startFight(hero);
			resumeInputs();
			//fightMenu.setVisible(false);
			rootPane.getChildren().remove(fightMenu);
		});

		fleeButton.setOnAction(event -> {
			fleeFromEnemy();
			resumeInputs();
			//fightMenu.setVisible(false);
			rootPane.getChildren().remove(fightMenu);
		});

		fightMenu.getChildren().addAll(encounterLabel, fightButton, fleeButton);
		rootPane.getChildren().add(fightMenu); // Add to main layout
		StackPane.setAlignment(fightMenu, Pos.CENTER);
	}

	public void returnToMapMenu(boolean fled){
		if (fled){
			fleeFromEnemy();
		}
        stage.setScene(mapScene);
        stage.show();
    }

	private void startFight(Hero hero) {
		// Logic for fighting the enemy
		System.out.println("Fighting enemy...");
		Scene guiMapScene = stage.getScene();
		GUIBattle battle = new GUIBattle(this, stage, hero, guiMapScene);
		// Example: Reduce enemy and hero health, update GUI, etc.
	}

	private void fleeFromEnemy() {
		// Logic for fleeing
		System.out.println("Fleeing from enemy...");
		// Example: Move hero back to the previous position
		System.out.println(hero.getX() + " " + hero.getY());
		hero.setX(hero.getPreviousX());
		hero.setY(hero.getPreviousY());
		System.out.println(hero.getX() + " " + hero.getY());
		refreshMap(mapGrid, map, hero);
	}
}
