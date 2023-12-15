import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fr.erwan.analyse.WavFileHelper;

/**
 * tester l'enregistrement micro
 */
public class WavFileHelperTest {

    @Test 
    public void runRecordTest() {
        byte[] b = WavFileHelper.runRecord();
        boolean check = b.length > Math.pow(2, 16);
        assertEquals(check, true);
    }
}