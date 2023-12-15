package fr.erwan.notes;

import fr.erwan.config.Config;

public class CalculateNoteFrequencies {

    public CalculateNoteFrequencies() {}

    /**
     * trouver les fréquences des notes 
     * associer les noms de notes aux fréquences
     * @return un objet avec nom des notes et fréquences
     */
    public static Frequences frequencies() {
        final short LA3 = Config.la3;  // fréquence la
        final short NBOCTAVES = Config.nbOctoves;  // nombre d'octaves
        final short NBNOTES = Config.nbNotes;   // nombre de notes
        final double SEMITONE = Math.pow(2.0, 1/12.0);
        final int NBTOTALNOTES  = NBNOTES*NBOCTAVES;

        double[] notes = new double[NBTOTALNOTES];
        Notes[] notesNames = new Notes[NBTOTALNOTES];
        Frequences freq = new Frequences();
        
        int inc = 0;
        for (int i = 0; i < NBTOTALNOTES ; i ++) {

            // calculate notes
            double note = LA3 * Math.pow(SEMITONE, i);
            
            // arondir à 2 chiffres et entre les notes
            notes[i] = Math.round(note * 100) / 100.0;

            if (inc == 12 ) {
                inc = 0;
            }
            // enter Enum Notes value in array
            notesNames[i] = Notes.values()[inc];
            inc++;
        }

        freq.setNotes(notes);
        freq.setNotesNames(notesNames);
        return freq;
    }

    /**
     * Analyser la justesse de la note analysée par micro ou fichier wav
     * @param note la fréquence qui vient de MainFrequencyAnalyser
     * @param notes les fréquences des notes
     * @param notesNames le nom des notes (enum Notes)
     * @return un objet accordage avec le nom de la note, les différences de justesse en hertz et cents
     */
    public static Accordage findNearsetNote(double note, double[] notes, Notes[] notesNames) {
        System.out.println("--------------------");
        System.out.println(note);
        if (note < Config.la3 | note > Config.maxFreq) {
            return new Accordage();
        }

        Accordage accordage = new Accordage();
        int idx = 0;
        int idxNote = 0;

        // trouver la fréquence la plus proche de la note analysée
        for (int i = 0; i < notes.length ; i++) {
            if (notes[i] > note) {
                idx = i -1;
                break;
            }
        }

        // séparer l'écart de ton en deux pour évaluer de quelle note la note analysée est la plus proche
        double moy = (notes[idx] + notes[idx+1]) /2.0;
        idxNote = moy < note ? idx + 1 : idx;

        accordage.setNote(notesNames[idxNote]);

        // analyser la différence en hertz
        if (idxNote > idx) {
            accordage.setHertz(ecart(notes[idxNote] - note, "Hz"));
        } else {
            accordage.setHertz(ecart(note - notes[idxNote], "Hz"));
        }

        // analyser la différence en cents
        double delta = 1200 * Math.log(note / notes[idxNote]) / Math.log(2);
        accordage.setCents(ecart(delta, "cents"));

        return accordage;
    }

    /**
     * arrondir hertz ou cents à deux chiffres après la virgule
     * @param ecrt écart en cents ou en hertz
     * @param suffix hertz ou cents
     * @return une chaîne de caractère avec l'arrondi et hertz ou cents
     */
    public static String ecart(double ecrt, String suffix) {
        StringBuilder sb = new StringBuilder();
        ecrt = Math.round(ecrt * 100) / 100.0;
        sb.append(ecrt);
        sb.append(suffix);
        return sb.toString();
    }
}
