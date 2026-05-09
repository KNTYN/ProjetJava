package fr.github.tcgame.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.github.tcgame.terminal.GameTerminal;
import fr.github.tcgame.view.MainGame;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        // Lancer le jeu en terminal dans un thread
        new Thread(() -> {
            GameTerminal.main(args);
        }).start();

        // Lancer l'interface graphique
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MainGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("TCGame");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        var mode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        configuration.setWindowedMode(mode.width, mode.height);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        configuration.setResizable(false);
        configuration.setDecorated(false);
        return configuration;
    }
}
