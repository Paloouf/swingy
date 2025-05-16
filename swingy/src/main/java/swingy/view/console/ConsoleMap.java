package swingy.view.console;

import swingy.model.Hero;
import swingy.model.Map;

public class ConsoleMap {
	public void displayMap(Map map, Hero hero) {
        clearScreen();
        char[][] grid = map.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        displayHeroInfo(hero);
        displayControls();
    }

    private void displayHeroInfo(Hero hero) {
        System.out.println("Hero: " + hero.getName() + " | Level: " + hero.getLevel());
    }

    private void displayControls() {
        System.out.println("Controls: North (w), South (s), East (d), West (a)");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
