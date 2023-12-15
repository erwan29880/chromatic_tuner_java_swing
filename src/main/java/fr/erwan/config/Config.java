package fr.erwan.config;

import java.io.File;

public class Config {
    public final static File wavTest = new File(new StringBuilder()
                                    .append(System.getProperty("user.dir"))
                                    .append(System.getProperty("file.separator"))
                                    .append("la1.wav")
                                    .toString());   // fichier wave pour test
    public final static String iconPath = new StringBuilder()
                                    .append(System.getProperty("user.dir"))
                                    .append(System.getProperty("file.separator"))
                                    .append("icon.png")
                                    .toString(); // icone de la JFrame
    public final static short recordDuration = 2000;  // en ms
    public final static int maxByteSize = (int) Math.pow(2, 16);  // doit être une puissance de 2 pour fft
    public final static short fenetreSize = 400;  // Jframe, Jlabel
    public final static short minFreq = 50;  // pas de détection en dessous
    public final static short maxFreq = 3000;  // pas de de détection au dessus
    public final static int recordingSampleRate = 44100;  // pour l'enregistrement
    public final static short recordingBitsPerSample = 16;  // pour l'enregistrement
    public final static short recordingNumChannels = 2;  // pour l'enregistrement
    public final static short la3 = 110;  // fréquence la   pour le calcul des fréquences des notes
    public final static short nbOctoves = 5;  // nombre d'octaves  pour le calcul des notes
    public final static short nbNotes = 12;   // nombre de notes  pour le calcul des notes
}
