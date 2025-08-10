package swingy.view.console;

import swingy.model.Hero;
import swingy.model.Map;

public class ConsoleMap {
    private static final int WINDOW_SIZE = 15; // Fixed window size (odd number for centering)
    private static final int HALF_WINDOW = WINDOW_SIZE / 2; // Half of window size for centering

    public void displayMap(Map map, Hero hero) {
        clearScreen();
        char[][] grid = map.getGrid();
        int heroX = hero.getX(); // Assume getX() and getY() exist
        int heroY = hero.getY();

       // Calculate the centered window start positions
       int startX = heroX - HALF_WINDOW; // Center hero at position 4
       int startY = heroY - HALF_WINDOW;

       // Display the fixed 9x9 window with '+' for out-of-bounds
       for (int i = 0; i < WINDOW_SIZE; i++) {
           for (int j = 0; j < WINDOW_SIZE; j++) {
               int mapX = startY + j; // Map x-coordinate
               int mapY = startX + i; // Map y-coordinate
               if (mapX < 0 || mapX >= grid.length || mapY < 0 || mapY >= grid[0].length) {
                   System.out.print("+ ");
               } else {
                   System.out.print(grid[mapY][mapX] + " "); // Note: y,x order due to grid[y][x]
               }
           }
           System.out.println();
       }

        displayHeroInfo(hero);
        displayControls();
    }

	// public void displayMap(Map map, Hero hero) {
    //     clearScreen();
    //     char[][] grid = map.getGrid();
    //     for (int i = 0; i < grid.length; i++) {
    //         for (int j = 0; j < grid[i].length; j++) {
    //             System.out.print(grid[i][j] + " ");
    //         }
    //         System.out.println();
    //     }
    //     displayHeroInfo(hero);
    //     displayControls();
    // }

    private void displayHeroInfo(Hero hero) {
        System.out.println("Hero: " + hero.getName() + " | Level: " + hero.getLevel() + " | HP: " + hero.getHealth() + " | Att: " + hero.getAttackPower() + " | XP: " + hero.getExperience() + "/" + hero.getNextLevelXP());
    }

    private void displayControls() {
        System.out.println("Controls: North (w), South (s), East (d), West (a), Quit (quit)");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
