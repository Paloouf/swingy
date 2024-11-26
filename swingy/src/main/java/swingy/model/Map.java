package swingy.model;

import java.util.Random;

public class Map {
    private int size;
    private int heroX;
    private int heroY;
    private char[][] grid; // The visible grid
    private boolean[][] enemies; // Tracks enemy positions

    public Map(int heroLevel) {
        size = (heroLevel - 1) * 5 + 10 - (heroLevel % 2);
        grid = new char[size][size];
        enemies = new boolean[size][size];
        heroX = size / 2;
        heroY = size / 2;
        generateMap();
        updateGrid();
    }

    // Generate enemies and place them on the map
    public void generateMap() {
        Random rand = new Random();
        int numberOfEnemies = size / 2; // Example value
        for (int i = 0; i < numberOfEnemies; i++) {
            int x, y;
            do {
                x = rand.nextInt(size);
                y = rand.nextInt(size);
            } while (enemies[x][y] || (x == heroX && y == heroY)); // Avoid placing on the hero's position
            enemies[x][y] = true; // Mark position as an enemy
        }
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
        grid[heroX][heroY] = 'H';
    }

    // Check if a villain is at the specified position
    public boolean isVillainAt(int x, int y) {
        return enemies[x][y];
    }

    // Move the hero and handle bounds checking
    public boolean moveHero(String direction) {
        int newX = heroX;
        int newY = heroY;

        switch (direction.toLowerCase()) {
            case "north": newX--; break;
            case "south": newX++; break;
            case "west":  newY--; break;
            case "east":  newY++; break;
        }

        // Check if hero is at the edge of the map
        if (newX < 0 || newX >= size || newY < 0 || newY >= size) {
            return true; // Reached the edge (victory)
        }

        // Update hero's position
        heroX = newX;
        heroY = newY;

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
