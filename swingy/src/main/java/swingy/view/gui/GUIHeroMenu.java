package swingy.view.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import swingy.controller.InputValidator;
import swingy.controller.JsonParser;
import swingy.model.Hero;
import swingy.model.HeroClass;

public class GUIHeroMenu{
	private Hero newHero;
	public Stage primaryStage;
	private final GUIMenu app;
	private final Scene scene;
	private VBox layout;
	private static final String SAVE_FOLDER = "saves";

	public GUIHeroMenu(GUIMenu app){
		this.app = app;
		this.layout = new VBox(10);
		this.layout.setAlignment(Pos.CENTER);
		this.scene = new Scene(layout, 900, 600);
		this.primaryStage = app.primaryStage;
	}

	public void returnToMainMenu(){
		app.returnToMainMenu();
	}

	public Hero createHero() {
		// Clear existing menu
		//layout.getChildren().clear();
		if (primaryStage == null) {
            throw new IllegalStateException("PrimaryStage is not initialized in GUIHeroMenu!");
        }
		// Create input fields
		TextField heroNameField = new TextField();
		heroNameField.setPromptText("Enter Hero's Name");

		ComboBox<String> heroClassComboBox = new ComboBox<>();
		heroClassComboBox.getItems().addAll("Warrior", "Mage", "Assassin");
		heroClassComboBox.setPromptText("Select Hero Class");

		Button createHeroButton = new Button("Create Hero");
		Button backButton = new Button("Back");

		// Add event handler for hero creation
		createHeroButton.setOnAction(e -> {
			String inputName = heroNameField.getText().trim();
			String name = InputValidator.validateStringInput(inputName);
			String selectedClass = heroClassComboBox.getValue();

			if (name == null || name.isEmpty() || selectedClass == null) {
				System.out.println("Please fill out all fields.");
				return;
			}

			// Check if name already exists
			List<Path> saves = getSaveFiles();
			boolean nameExists = saves.stream()
				.map(path -> path.getFileName().toString().replace(".json", ""))
				.anyMatch(existingName -> existingName.equalsIgnoreCase(name));

			if (nameExists) {
				// Show confirmation dialog for overwrite
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Name Conflict");
				alert.setHeaderText("A hero with the name '" + name + "' already exists!");
				alert.setContentText("Do you want to overwrite it?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() != ButtonType.OK) {
					return; // Cancel overwrite
				}
				// Proceed to overwrite if OK is clicked
			}

			HeroClass heroClass = HeroClass.valueOf(selectedClass.toUpperCase());
			newHero = new Hero(name, heroClass);
			newHero.saveHero();
			System.out.println("Hero Created: " + newHero);
			GUIMap newMap = new GUIMap(this);
		});
		backButton.setOnAction(e -> app.returnToMainMenu());
		// Add components to the layout
		layout.getChildren().addAll(heroNameField, heroClassComboBox, createHeroButton, backButton);
		primaryStage.setScene(this.scene);
		primaryStage.show();
		return newHero;
	}

	public Hero selectSave(){
		//layout.getChildren().clear();
		if (primaryStage == null) {
            throw new IllegalStateException("PrimaryStage is not initialized in GUIHeroMenu!");
        }
		List<Path> saves = getSaveFiles();
        if (saves.isEmpty()) {
            System.out.println("No saved heroes found!");
			Label noSavesLabel = new Label("No saved heroes found!");
			Button backButton = new Button("Back to Main Menu");
			backButton.setOnAction(e -> app.returnToMainMenu());
			layout.getChildren().addAll(noSavesLabel, backButton);
			app.primaryStage.setScene(this.scene);
			app.primaryStage.show();
			return null;
        }

		ComboBox<String> heroClassComboBox = new ComboBox<>();
		for (Path save : saves) {
			try {
				String json = Files.readString(save);
				JsonParser.JsonStats stats = JsonParser.parseJsonStats(json);
				if (stats != null) {
					String fileName = save.getFileName().toString().replace(".json", "");
					heroClassComboBox.getItems().add(String.format("%s ( %s, Level: %d, HP: %d, Attack: %d, Defense: %d)",
						fileName, stats.className, stats.level, stats.health, stats.attackPower, stats.defense));
				} else {
					heroClassComboBox.getItems().add(save.getFileName().toString().replace(".json", "") + " (Error parsing data)");
				}
			} catch (IOException e) {
				System.err.println("Error reading file " + save.getFileName() + ": " + e.getMessage());
				heroClassComboBox.getItems().add(save.getFileName().toString().replace(".json", "") + " (Error loading data)");
			}
		}
		heroClassComboBox.setPromptText("Select a Hero");

		Button createHeroButton = new Button("Load Hero");
		Button backButton = new Button("Back");
        // Wrapper object to hold the hero
    	final Hero[] heroHolder = new Hero[1];
		createHeroButton.setOnAction(e -> {
			String selectedSave = heroClassComboBox.getValue();

			if (selectedSave == null) {
				System.out.println("Please fill out all fields.");
				return;
			}
			
			Path selectedPath = saves.stream()
                .filter(path -> path.getFileName().toString().equals(selectedSave + ".json"))
                .findFirst()
                .orElse(null);

			if (selectedPath != null) {
				heroHolder[0] = loadHero(selectedPath);
				newHero = heroHolder[0];
				System.out.println("Hero loaded: " + heroHolder[0]);
				GUIMap newMap = new GUIMap(this);
			} else {
				System.out.println("Error: Save file not found.");
			}
		});
		backButton.setOnAction(e -> app.returnToMainMenu());
		layout.getChildren().addAll(heroClassComboBox, createHeroButton, backButton);
		primaryStage.setScene(this.scene);
		primaryStage.show();
        return heroHolder[0];
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

	public Hero getHero(){
		return newHero;
	}
}
