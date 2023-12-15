import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.util.List;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.junit.jupiter.api.Test;
import fr.erwan.analyse.FileToByte;

/**
 * tester que le fichier test est bien lu et retourne un double
 * tester le cast de bytes en double de l'enregistrement
 */
public class FileAudioToByteTest {
    
    FileToByte fb = new FileToByte();

    @Test
    public void reafFullyTest() throws UnsupportedAudioFileException, IOException{
        double[] d = fb.readFully();
        long len = d.length;
        
        assertDoesNotThrow(() -> UnsupportedAudioFileException.class);
        assertDoesNotThrow(() -> IOException.class);
        assertEquals(len, Math.pow(2, 15));
    }

    @Test 
    public void readFullyRecordTest() {
        byte[] p = new byte[(int) Math.pow(2, 17)];
        double[] d = fb.readFully(p, 2);
        long len = d.length;
        assertEquals(len, Math.pow(2, 15));
    }

    @Test 
    public void extract16BitsSingleChannelsTest() {
        byte[] b = new byte[2*256];
        List<byte[]> l = fb.extract16BitsSingleChannels(b, 2);
        assertEquals(2, l.size());
    }

    @Test
    public void getSampleRateTest() throws UnsupportedAudioFileException, IOException{
        fb.readFully();
        assertEquals(44100, fb.getSampleRate());
    } 
}
