/**
 * Launcher class required for JavaFX classpath compatibility.
 */
package kiwi.build;

import javafx.application.Application;

/**
 * Launcher for Kiwi JavaFX application.
 */
public class Launcher {

    /**
     * Main entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
