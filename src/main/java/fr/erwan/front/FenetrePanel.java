package fr.erwan.front;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import fr.erwan.config.Config;
import fr.erwan.notes.Accordage;

/**
 * Jpanel avec l'affichage des notes, Hz, cents
 */
public class FenetrePanel extends JPanel {

    private final short B_WIDTH = Config.fenetreSize;  // taille jpanel
    private final short B_HEIGHT = Config.fenetreSize;
    private final int CENTER = B_WIDTH/2; // calucl du centre pour centre le cercle
    private final short size = 300; // taille du cercle
    private final short lineWidth = 10; // épaisseur du cercle
    private final int x = CENTER - size/2; // calcul de l'absisse pour le cercle
    private IsAccorded isAccorded = IsAccorded.DEFAUT;  //enum pour changement de couleur

    // provient de la détection, texte : la note, ecart : Hz et cents
    private String texte = "-";
    private String ecart = "-";

    public FenetrePanel() {
        initBoard();
    }

    // configuration du JPanel
    private void initBoard() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
    }

    // afficher les éléments
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCircle(g);
        setTexteInCircle(g);
        setEcart(g);
    }

    // le cercle
    private void drawCircle(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(lineWidth));
        g2.drawOval(x, 10, size, size); 
    }

    // la note
    private void setTexteInCircle (Graphics g) {
        // config police
        int fontSize = 100; 
        g.setFont(new Font("serif", Font.BOLD, fontSize)); 
        g.setColor(isAccorded.toColor());

        // calcul centrage
        int stringWidthLength = (int) g.getFontMetrics().getStringBounds(this.texte, g).getWidth();
        int horizontalCenter = B_HEIGHT / 2 - stringWidthLength / 2;
        int verticalCenter = B_WIDTH / 2 - 20;

        g.drawString(this.texte, horizontalCenter, verticalCenter);
    }

    // Hz et cents
    private void setEcart (Graphics g) {
        // config police
        int fontSizeEcart = 20;
        g.setFont(new Font("serif", Font.BOLD, fontSizeEcart)); 
        g.setColor(Color.gray);

        // calcul position
        int stringWidthLength = (int) g.getFontMetrics().getStringBounds(this.ecart, g).getWidth();
        int horizontalCenter = B_HEIGHT / 2 - stringWidthLength / 2;

        g.drawString(this.ecart, horizontalCenter, B_HEIGHT- 30 - fontSizeEcart);
    }


    public void setTextes(Accordage accordage) {
        // en fonction de l'accord, changement de la couleur de la note
        if (accordage.getHertz() == null) isAccorded = IsAccorded.DEFAUT;
        else if (Double.parseDouble(accordage.getHertz().split("Hz")[0]) != 0.0 ) isAccorded = IsAccorded.DESACCORDED;
        else if (Double.parseDouble(accordage.getHertz().split("Hz")[0]) == 0.0 ) isAccorded = IsAccorded.ACCORDED;
        else isAccorded = IsAccorded.DEFAUT;

        // changement du texte de la note
        this.texte = accordage.getNote() == null ? "-" : accordage.getNote().toString();

        // changement du texte Hz et cents
        StringBuilder sb = new StringBuilder()
            .append(accordage.getHertz() == null ? 0 : accordage.getHertz())
            .append("    ")
            .append(accordage.getCents() == null ? 0 : accordage.getCents());
        this.ecart = sb.toString();
    }
}
