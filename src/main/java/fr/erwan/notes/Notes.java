package fr.erwan.notes;

/**
 * les notes de musique
 */
public enum Notes {
    LA("A"), SIB("Bb"), SI("B"), DO("C"), REB("C#"), RE("D"), MIB("Eb"), MI("E"), FA("F"), SOLB("Fa#"), SOL("G"), LAB("G#");


    private final String name;       

    private Notes(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
     }
}
