package swingy.controller;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import swingy.view.MenuAction;

public class InputManager {
    private static InputContext currentContext;
    //private static final Scanner scanner = new Scanner(System.in);

    // Set the current context for input handling
    public static void setContext(InputContext context) {
        currentContext = context;
    }

    // Start the input loop
    public static void startInputLoop() {
        try (Terminal terminal = TerminalBuilder.terminal()) {
            //System.out.println("Input Loop Started. Use W (up), S (down), Enter, or Q (quit).");
            terminal.enterRawMode();
            while (true) {
                // Read a single keypress
                int inputChar = terminal.reader().read(10);

                if (inputChar == -1) {
                    continue; // No input detected; refresh loop continues
                }

                // Convert input to a string
                String input = Character.toString((char) inputChar).toLowerCase();

                // Pass input to the current context
                if (currentContext != null) {
                    MenuAction shouldExit = currentContext.handleInput(input);
                    if (shouldExit != shouldExit.NONE) {
                        terminal.puts(InfoCmp.Capability.clear_screen);
                        terminal.close();
                        return;
                        //System.exit(1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
