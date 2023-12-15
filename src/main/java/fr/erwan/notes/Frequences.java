package fr.erwan.notes;

public class Frequences {
    private double[] notes;
    private Notes[] notesNames;

    public Frequences() {
    }

    public double[] getNotes() {
        return this.notes;
    }

    public void setNotes(double[] notes) {
        this.notes = notes;
    }

    public Notes[] getNotesNames() {
        return this.notesNames;
    }

    public void setNotesNames(Notes[] notesNames) {
        this.notesNames = notesNames;
    }
}
