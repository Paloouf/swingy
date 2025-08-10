package swingy.model;

import java.util.Random;

import swingy.view.console.FightMenu;

public class Map {
    private int size;
    private int heroX;
    private int heroY;
    private char[][] grid; // The visible grid
    private boolean[][] enemies; // Tracks enemy positions
    private Hero hero;

    public Map(Hero heroMap) {
        System.out.println("map created with hero");
        size = (heroMap.getLevel() - 1) * 5 + 10 - (heroMap.getLevel() % 2);
        grid = new char[size][size];
        enemies = new boolean[size][size];
        hero = heroMap;
        hero.setX(size/2);
        hero.setY(size/2);
        generateMap();
        updateGrid();
    }

    public interface EncounterListener {
        void onEnemyEncountered(Hero hero);
    }

    private EncounterListener encounterListener;

    public void setEncounterListener(EncounterListener listener) {
        this.encounterListener = listener;
    }

    // Generate enemies and place them on the map
    public void generateMap() {
        Random rand = new Random();
        int numberOfEnemies = size * 2; // Example value
        for (int i = 0; i < numberOfEnemies; i++) {
            int x, y;
            do {
                x = rand.nextInt(size);
                y = rand.nextInt(size);
            } while (enemies[x][y] || (x == hero.getX() && y == hero.getY())); // Avoid placing on the hero's position
            enemies[x][y] = true; // Mark position as an enemy
        }
    }

    public void generateNewMap() {
        size = (hero.getLevel() - 1) * 5 + 10 - (hero.getLevel() % 2);
        Random rand = new Random();
        // Reset all values in the enemies array to false
        enemies = new boolean[size][size];
        grid = new char[size][size];
        int numberOfEnemies = size *2; // Example value
        hero.setX(size/2);
        hero.setY(size/2);
        for (int i = 0; i < numberOfEnemies; i++) {
            int x, y;
            do {
                x = rand.nextInt(size);
                y = rand.nextInt(size);
            } while (enemies[x][y] || (x == hero.getX() && y == hero.getY())); // Avoid placing on the hero's position
            enemies[x][y] = true; // Mark position as an enemy
        }
        
        updateGrid();
    }

    // Update the grid with hero and enemies
    public void updateGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (enemies[i][j]) {
                    grid[i][j] = 'V'; // Enemy position
                } else {
                    grid[i][j] = '.'; // Empty position
                }
            }
        }
        // Place the hero
        grid[hero.getX()][hero.getY()] = 'H';
    }

    // Check if a villain is at the specified position
    public boolean isVillainAt(int x, int y) {
        return enemies[x][y];
    }

    // Move the hero and handle bounds checking
    public boolean moveHero(String direction, boolean gui) {
        int newX = hero.getX();
        int newY = hero.getY();

        switch (direction.toLowerCase()) {
            case "north": newX--; break;
            case "south": newX++; break;
            case "west":  newY--; break;
            case "east":  newY++; break;
        }

        // Check if hero is at the edge of the map
        if (newX < 0 || newX >= size || newY < 0 || newY >= size) {
            generateNewMap();
            return true; // Reached the edge (victory)
        }
        hero.setPreviousX(hero.getX());
        hero.setPreviousY(hero.getY());
        // Update hero's position
        hero.setX(newX);
        hero.setY(newY);
        

        if (grid[hero.getX()][hero.getY()] == 'V') {
            if (!gui){
                FightMenu fightMenu = new FightMenu();
                fightMenu.initiateFight(hero);
            }
            else {
                if (encounterListener != null) {
                    encounterListener.onEnemyEncountered(hero); // Notify listener
                }
            }
            // After fight, optionally clear the V case
            enemies[hero.getX()][hero.getY()] = false;
        }

        // Refresh the grid to reflect the new hero position
        updateGrid();
        return false; // Hero is still inside the map
    }

    // Get the map size
    public int getSize() {
        return size;
    }

    // Get the current grid
    public char[][] getGrid() {
        return grid;
    }
}
