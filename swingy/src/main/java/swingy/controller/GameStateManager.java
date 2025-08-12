package swingy.controller;


public class GameStateManager {
    // Static instance field with volatile to ensure visibility in multi-threaded environment
    private static volatile GameStateManager instance;

    // Private field to hold the current state
    private volatile GameState currentState = GameState.MENU;

    // Private constructor to prevent direct instantiation
    private GameStateManager() {
        // Private constructor to enforce singleton
    }

    // Public static method to get the singleton instance with double-checked locking for thread safety
    public static GameStateManager getInstance() {
        if (instance == null) {
            synchronized (GameStateManager.class) {
                if (instance == null) {
                    instance = new GameStateManager();
                }
            }
        }
        return instance;
    }

    // Synchronized method to get the current state
    public synchronized GameState getState() {
        return currentState;
    }

    // Synchronized method to set the new state
    public synchronized void setState(GameState newState) {
        currentState = newState;
    }
}