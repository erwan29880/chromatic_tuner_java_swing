import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import fr.erwan.notes.Notes;

/**
 * test de l'enum Notes
 */
public class NotesTest {

    @Test 
    public void testNotes() {
        Notes n = Notes.values()[0];
        boolean check = n.toString().equals("A");
        assertEquals(check, true);
    }
}
