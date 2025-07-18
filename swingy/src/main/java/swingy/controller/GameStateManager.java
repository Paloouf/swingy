package swingy.controller;


public class GameStateManager {
    private volatile GameState currentState = GameState.MENU;

    public synchronized GameState getState() {
        return currentState;
    }

    public synchronized void setState(GameState newState) {
        currentState = newState;
    }
}
