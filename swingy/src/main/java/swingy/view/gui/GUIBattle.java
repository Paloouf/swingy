package swingy.view.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
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
        
        // Create health bars and labels
        Label heroLabel = new Label("Hero: " + hero.getName());
        heroHealthBar = new ProgressBar(hero.getHealth() / (double) hero.getMaxHealth());
        Label enemyLabel = new Label("Enemy: " + enemy.getName());
        enemyHealthBar = new ProgressBar(enemy.getHealth() / (double) enemy.getMaxHealth());

        // Fight log
        fightLog = new Label("A " + enemy.getName() + " appears!");

        // Action buttons
        attackButton = new Button("Attack");
        fleeButton = new Button("Flee");

        attackButton.setOnAction(e -> performAttack());
        fleeButton.setOnAction(e -> attemptFlight());

        // Add components to the layout
        root.getChildren().addAll(heroLabel, heroHealthBar, enemyLabel, enemyHealthBar, fightLog, attackButton, fleeButton);

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
        fightLog.setText("You attack the " + enemy.getName() + " for " + hero.getAttackPower() + " damage!");
        updateHealthBars();

        // Check if the enemy is defeated
        if (enemy.getHealth() <= 0) {
            fightLog.setText("You defeated the " + enemy.getName() + "!");
            rewardHero();
            return;
        }

        // Enemy counter-attacks
        enemyAttack();
    }

    private void enemyAttack() {
        hero.takeDamage(enemy.getAttackPower());
        fightLog.setText(fightLog.getText() + "\nThe " + enemy.getName() + " attacks you for " + enemy.getAttackPower() + " damage!");
        updateHealthBars();

        // Check if hero is defeated
        if (hero.getHealth() <= 0) {
            fightLog.setText("You were defeated by the " + enemy.getName() + "!");
            showGameOver();
        }
    }

    private void attemptFlight() {
        boolean successfulEscape = Math.random() > 0.5; // 50% chance

        if (successfulEscape) {
            fightLog.setText("You successfully fled from the " + enemy.getName() + "!");
            app.returnToMapMenu(true);            
        } else {
            fightLog.setText("You failed to escape! The " + enemy.getName() + " blocks your path.");
            fleeButton.setDisable(true);
            enemyAttack();
        }
    }

    private void updateHealthBars() {
        heroHealthBar.setProgress(hero.getHealth() / (double) hero.getMaxHealth());
        enemyHealthBar.setProgress(enemy.getHealth() / (double) enemy.getMaxHealth());
    }

    private void rewardHero() {
        // Reward the hero after defeating the enemy
        int xpAward = (int) Math.pow(enemy.getLevel(), 2) * 300;
        hero.gainExperience(xpAward);
        hero.heal();
        pauseInputs();
        Artifact loot = LootSystem.rollForLoot(); 
        if (loot != null) {
            hero.equipArtifact(loot);
            fightLog.setText(fightLog.getText() + "\nYou found and equipped " + loot.getName() + "!");
        }
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

    }

    public VBox getRoot() {
        return root;
    }
}
