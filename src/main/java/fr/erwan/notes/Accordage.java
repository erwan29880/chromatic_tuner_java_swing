package fr.erwan.notes;

/**
 * Objet pour retourner le résultat de la détection
 */
public class Accordage {
    private Notes note;
    private String hertz;
    private String cents;

    public Accordage() {
    }

    public Notes getNote() {
        return this.note;
    }

    public void setNote(Notes note) {
        this.note = note;
    }

    public String getHertz() {
        return this.hertz;
    }

    public void setHertz(String hertz) {
        this.hertz = hertz;
    }

    public String getCents() {
        return this.cents;
    }

    public void setCents(String cents) {
        this.cents = cents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (hertz == null) {
            sb.append("{");
            sb.append("notes= 0 , ");
            sb.append("hertz= 0 , ");
            sb.append("cents= 0}");
            return sb.toString();
        }
        sb.append("{");
        sb.append("notes='");
        sb.append(getNote());
        sb.append("', hertz='");
        sb.append(getHertz());
        sb.append("', cents='");
        sb.append(getCents());
        sb.append("}");
        return sb.toString();
    }
}
