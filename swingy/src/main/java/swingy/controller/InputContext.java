package swingy.controller;

import swingy.view.console.MenuAction;

public interface InputContext {
    /**
     * Handles the user input.
     * 
     * @param input The input from the user.
     * @return true if the application should exit, false otherwise.
     */
    MenuAction handleInput(String input);
}
