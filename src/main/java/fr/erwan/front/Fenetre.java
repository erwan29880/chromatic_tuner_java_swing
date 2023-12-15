package fr.erwan.front;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

import fr.erwan.analyse.FileToByte;
import fr.erwan.analyse.MainFrequencyAnalyser;
import fr.erwan.analyse.WavFileHelper;
import fr.erwan.config.Config;
import fr.erwan.notes.Accordage;
import fr.erwan.notes.CalculateNoteFrequencies;
import fr.erwan.notes.Frequences;

/**
 * la fenêtre de l'accordeur
 * initie la fenêtre 
 * appelle la classe Ovale (JLabel)
 * effectue la détection
 */
public class Fenetre extends JFrame {
    private final short windowsWidth = Config.fenetreSize;
    private final short windowsHeight = Config.fenetreSize;
    private FenetrePanel ovale;  // JPanel

    public Fenetre () {
        super();
        main();
    }

    private void init () {
        this.setTitle("Accordeur chromatique");
		this.setSize(this.windowsWidth, this.windowsHeight);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		Image icon = Toolkit.getDefaultToolkit().getImage(Config.iconPath);
		this.setIconImage(icon);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * ajout du JPanel
     */
    private void contenu () {
        this.ovale = new FenetrePanel();
        this.add(ovale);
    }

    /**
     * rendre la fenêtre visible
     */
    private void showWin () {
        this.setVisible(true);
    }

    /**
     * méthode appellée dans le constructeur
     */
    public void main() {
        this.init();
        this.contenu();
        this.showWin();
        this.detection();
    }

    /**
     * boucle de détection 
     */
    private void detection () {
        // initialiser les notes la première fois
        Frequences freqs = CalculateNoteFrequencies.frequencies();
        MainFrequencyAnalyser m = new MainFrequencyAnalyser(freqs);
        FileToByte atb = new FileToByte();

        // boucle de détection
        Accordage accordage = new Accordage();
        while (true) {
            try {
                byte[] bytes = WavFileHelper.runRecord();
                double[] dbl = atb.readFully(bytes, 2);
                accordage = m.main(dbl, 44100.0);
                System.out.println(accordage);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                this.ovale.setTextes(accordage);
                this.ovale.repaint();
            }
        }
    }
    
}
