package fr.erwan.front;

import java.awt.Color;

/**
 * changement de couleur de la note en fonction de accordé, pas accordé, ou action par défaut
 */
public enum IsAccorded {
    DEFAUT(Color.gray), 
    ACCORDED(Color.green), 
    DESACCORDED(Color.red);

    private final Color col;       

    private IsAccorded(Color color) {
        col = color;
    }

    public boolean equalsName(Color otherName) {
        return col.equals(otherName);
    }

    public Color toColor() {
        return this.col;
    }  
}
