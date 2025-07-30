package swingy.view.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import swingy.model.Artifact;
import swingy.model.Enemy;
import swingy.model.Hero;
import swingy.model.LootSystem;

public class GUIBattle {

    private Stage stage; // Reference to the main game stage
    private Hero hero;
    private Enemy enemy;
    private VBox root;
    private ProgressBar heroHealthBar;
    private ProgressBar enemyHealthBar;
    private Button attackButton;
    private Button fleeButton;
    private Scene scene;
    private Label fightLog;
    private final GUIMap app;
    private Label heroHpLabel; // For HP text inside hero bar
    private Label enemyHpLabel; // For HP text inside enemy bar

    public GUIBattle(GUIMap app, Stage stage, Hero hero, Scene previousScene) {
        this.app = app;
        this.stage = stage;
        this.hero = hero;
        this.scene = previousScene;
        this.enemy = Enemy.createEnemy(hero); // Logic from Enemy class
        this.root = new VBox(10);
        setupBattleScreen();
    }

    private void setupBattleScreen() {
        root.getChildren().clear();
        root.setAlignment(Pos.CENTER);
        
        StackPane heroHealthPane = new StackPane();
        Label heroLabel = new Label("Hero: " + hero.getName());
        heroHealthBar = new ProgressBar(hero.getHealth() / (double) hero.getMaxHealth());
        heroHealthBar.setPrefWidth(300); // Wider bar
        heroHealthBar.setStyle("-fx-accent: green;"); // Green for hero
        heroHpLabel = new Label(hero.getHealth() + " / " + hero.getMaxHealth());
        heroHpLabel.setTextFill(Color.BLACK); // White text for contrast
        heroHealthPane.getChildren().addAll(heroHealthBar, heroHpLabel);
        StackPane.setAlignment(heroHpLabel, Pos.CENTER); // Center the HP label

        // Enemy health bar with HP label
        StackPane enemyHealthPane = new StackPane();
        Label enemyLabel = new Label("Enemy: " + enemy.getName());
        enemyHealthBar = new ProgressBar(enemy.getHealth() / (double) enemy.getMaxHealth());
        enemyHealthBar.setPrefWidth(300); // Wider bar
        enemyHealthBar.setStyle("-fx-accent: red;"); // Red for enemy
        enemyHpLabel = new Label(enemy.getHealth() + " / " + enemy.getMaxHealth());
        enemyHpLabel.setTextFill(Color.BLACK); // White text for contrast
        enemyHealthPane.getChildren().addAll(enemyHealthBar, enemyHpLabel);
        StackPane.setAlignment(enemyHpLabel, Pos.CENTER); // Center the HP label

        // Fight log
        fightLog = new Label(enemy.getName() + " appears!");
        fightLog.setAlignment(Pos.CENTER);

        // Action buttons
        attackButton = new Button("Attack");
        fleeButton = new Button("Flee");

        attackButton.setOnAction(e -> performAttack());
        fleeButton.setOnAction(e -> attemptFlight());

        // Add components to the layout
        root.getChildren().addAll(heroLabel, heroHealthBar, heroHpLabel, enemyLabel, enemyHealthBar, enemyHpLabel, fightLog, attackButton, fleeButton);

        // Set the new scene
        scene = new Scene(root, 900, 600);
        stage.setScene(scene);
    }

    private void pauseInputs() {
		// Logic to pause the movement controls
		attackButton.setDisable(true);
		fleeButton.setDisable(true);
	}

    private void performAttack() {
        // Hero attacks enemy
        enemy.takeDamage(hero.getAttackPower());
        fightLog.setText("You attack " + enemy.getName() + " for " + hero.getAttackPower() + " damage!");
        updateHealthBars();

        // Check if the enemy is defeated
        if (enemy.getHealth() <= 0) {
            fightLog.setText("You defeated " + enemy.getName() + "!");
            rewardHero();
            return;
        }

        // Enemy counter-attacks
        enemyAttack();
    }

    private void enemyAttack() {
        hero.takeDamage(enemy.getAttackPower());
        fightLog.setText(fightLog.getText() + "\n" + enemy.getName() + " attacks you for " + enemy.getAttackPower() + " damage!");
        updateHealthBars();

        // Check if hero is defeated
        if (hero.getHealth() <= 0) {
            fightLog.setText("You were defeated by " + enemy.getName() + "!");
            showGameOver();
        }
    }

    private void attemptFlight() {
        boolean successfulEscape = Math.random() > 0.5; // 50% chance

        if (successfulEscape) {
            fightLog.setText("You successfully fled from " + enemy.getName() + "!");
            app.returnToMapMenu(true);            
        } else {
            fightLog.setText("You failed to escape! " + enemy.getName() + " blocks your path and attacks you first.");
            fleeButton.setDisable(true);
            enemyAttack();
        }
    }

    private void updateHealthBars() {
        double heroHealthRatio = hero.getHealth() / (double) hero.getMaxHealth();
        double enemyHealthRatio = enemy.getHealth() / (double) enemy.getMaxHealth();
        System.out.println("Hero Health: " + hero.getHealth() + "/" + hero.getMaxHealth() + " = " + heroHealthRatio);
        System.out.println("Enemy Health: " + enemy.getHealth() + "/" + enemy.getMaxHealth() + " = " + enemyHealthRatio);
        heroHealthBar.setProgress(heroHealthRatio);
        enemyHealthBar.setProgress(enemyHealthRatio);
        heroHpLabel.setText(hero.getHealth() + " / " + hero.getMaxHealth()); // Update HP text
        enemyHpLabel.setText(enemy.getHealth() + " / " + enemy.getMaxHealth());
    }

    private void rewardHero() {
        // Reward the hero after defeating the enemy
        int xpAward = hero.getXpReward(enemy);
        hero.gainExperience(xpAward);
        hero.heal();
        pauseInputs();
        Artifact loot = LootSystem.rollForLoot(); 
        if (loot != null) {
            boolean equipped = hero.equipArtifact(loot);
            if (equipped) {
                fightLog.setText(fightLog.getText() + "\nYou found and equipped " + loot.getName() + "!");
            } else {
                fightLog.setText(fightLog.getText() + "\nYou found " + loot.getName() + " but didn’t equip it because the stats were worse!");
            }
        }
        fightLog.setText(fightLog.getText()+ "\nYou character is healed to full life.");
        hero.saveHero();
        // Transition back to the main map or menu
        app.updateHeroInfo(hero);
        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> app.returnToMapMenu(false));
        root.getChildren().add(continueButton);
    }

    private void showGameOver() {
        Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
        gameOverAlert.setTitle("Game Over");
        gameOverAlert.setHeaderText(null);
        gameOverAlert.setContentText("You have been defeated. Better luck next time!");
        gameOverAlert.showAndWait();
        //go back to start screen here

    }

    public VBox getRoot() {
        return root;
    }
}
