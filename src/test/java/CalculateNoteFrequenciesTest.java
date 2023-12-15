import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.erwan.notes.Accordage;
import fr.erwan.notes.CalculateNoteFrequencies;
import fr.erwan.notes.Frequences;
import fr.erwan.notes.Notes;

/**
 * tester la classe qui trouve note et écarts de fréquences
 */
public class CalculateNoteFrequenciesTest {
    
    @Test
    public void testGdiese() {
        Frequences freqs = CalculateNoteFrequencies.frequencies();
        Accordage a = CalculateNoteFrequencies.findNearsetNote(439.9, freqs.getNotes(), freqs.getNotesNames());

        double hz = Double.parseDouble(a.getHertz().split("Hz")[0]);
        double cents = Double.parseDouble(a.getCents().split("cents")[0]);
        
        boolean note = a.getNote().equals(Notes.LA);
        boolean hzCheck = hz > -0.5;
        boolean centsCheck = cents > -2;
        
        assertEquals(note, true);
        assertEquals(hzCheck, true);
        assertEquals(centsCheck, true);
    }

    @Test
    public void testA() {
        Frequences freqs = CalculateNoteFrequencies.frequencies();
        Accordage a = CalculateNoteFrequencies.findNearsetNote(440.1, freqs.getNotes(), freqs.getNotesNames());

        double hz = Double.parseDouble(a.getHertz().split("Hz")[0]);
        double cents = Double.parseDouble(a.getCents().split("cents")[0]);
        
        boolean note = a.getNote().equals(Notes.LA);
        boolean hzCheck = hz < 0.5;
        boolean centsCheck = cents < 2;
        
        assertEquals(note, true);
        assertEquals(hzCheck, true);
        assertEquals(centsCheck, true);
    }

    @Test 
    public void testEcart() {
        double d = 440.8978657799d;
        String suffix = "cents";

        String res = CalculateNoteFrequencies.ecart(d, suffix);
        boolean check = res.equals("440.9cents");
        
        assertEquals(check, true);
    }

}
