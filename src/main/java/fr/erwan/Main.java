package fr.erwan;

import fr.erwan.config.Config;
import fr.erwan.front.Fenetre;

public class Main {
    public static void main(String[] args) throws InterruptedException { 
        new Fenetre();
        Thread.sleep(Config.recordDuration);
        System.exit(0);
    }
}