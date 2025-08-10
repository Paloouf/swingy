package swingy.view.console;

import java.util.Random;
import java.util.Scanner;

import swingy.controller.InputValidator;
import swingy.model.Artifact;
import swingy.model.Enemy;
import swingy.model.Hero;
import swingy.model.LootSystem;

public class FightMenu {

    public void initiateFight(Hero hero) {
		Enemy enemy = Enemy.createEnemy(hero);
        System.out.println("You encountered " + enemy.getName() + "!");

        Scanner scanner = new Scanner(System.in);
        boolean fightOngoing = true;

        while (fightOngoing) {
            System.out.println("What would you like to do?");
            System.out.println("1. Fight");
            System.out.println("2. Flee");
            System.out.print("Enter your choice: ");

			String input = scanner.nextLine();
			try{
				int choice = Integer.parseInt(input);
				InputValidator.validateIntegerInput(choice);
				switch (choice) {
					case 1 -> {
						fight(hero, enemy);
						if (enemy.getHealth() <= 0) {
							System.out.println("You defeated " + enemy.getName() + "!");
							fightOngoing = false;
						} else if (hero.getHealth() <= 0) {
							System.out.println("You were defeated by " + enemy.getName() + ".");
							fightOngoing = false; // This could end the game depending on your logic
						}
					}
					case 2 -> {
						flight(hero, enemy);
						System.out.println("Press enter to continue...");
						String waitforenter = scanner.nextLine();
						fightOngoing = false; // Exit the fight
					}
					default -> System.out.println("Invalid choice. Please try again.");
				}
			} catch (NumberFormatException e) {
            	System.out.println("Invalid input. Please enter a number."); // Handle non-numeric input
        	}
        }
    }

	private void flight(Hero hero, Enemy enemy) {
		Random random = new Random();
		boolean successfulEscape = random.nextBoolean(); // Simulates a 50/50 chance
		
		if (successfulEscape) {
			System.out.println("You successfully fled from " + enemy.getName() + "!");
			
			// Move the hero back to the previous position
			int previousX = hero.getPreviousX();
			int previousY = hero.getPreviousY();
			hero.setPosition(previousX, previousY); // Assume your Hero class has methods to set position
		} else {
			System.out.println("You failed to escape! " + enemy.getName() + " blocks your path.");
			System.out.println("Prepare to fight!");
			fight(hero,enemy);
		}
	}

    private void fight(Hero hero, Enemy enemy) {
		System.out.println("\n--- Combat Begins ---");
		System.out.println("You encounter " + enemy.getName() + "!");
		Scanner scanner = new Scanner(System.in);
		boolean inCombat = true;

		while (inCombat) {
			try {
				// Hero attacks
				System.out.println("\nYou attack " + enemy.getName() + "!");
				enemy.takeDamage(hero.getAttackPower());
				System.out.println("" + enemy.getName() + "'s health: " + enemy.getHealth());
				Thread.sleep(1000); // Pause for 1 second to show the attack

				// Check if enemy is defeated
				if (enemy.getHealth() <= 0) {
					System.out.println(enemy.getName() + " has been defeated!");
					int xpAward = hero.getXpReward(enemy);
					hero.gainExperience(xpAward);
					hero.heal();
					Artifact loot = LootSystem.rollForLoot(); // to implement 
					if (loot != null) {
						hero.equipArtifact(loot);
					}
					System.out.println("Press Enter to continue...");
					String input = scanner.nextLine();
					if (input.trim().isEmpty())
						inCombat = false; // Exit the combat loop
					break;
				}

				// Enemy attacks
				System.out.println("\n" + enemy.getName() + " attacks you!");
				hero.takeDamage(enemy.getAttackPower());
				System.out.println("Your health: " + hero.getHealth());
				Thread.sleep(1000); // Pause for 1 second to show the attack

				// Check if hero is defeated
				if (hero.getHealth() <= 0) {
					System.out.println("You have been defeated. Game Over!");
					inCombat = false; // Exit the combat loop
				}

			} catch (InterruptedException e) {
				System.out.println("Combat interrupted!");
				Thread.currentThread().interrupt(); // Restore interrupted status
				break;
			}
		}

		System.out.println("--- Combat Ends ---");
	}

}

