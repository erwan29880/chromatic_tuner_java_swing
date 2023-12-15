import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.junit.jupiter.api.Test;
import fr.erwan.config.Config;

/**
 * test pour vérifier que le fichier test la1.wav existe
 */
public class ConfigTestPathFiles {
    
    @Test
    public void getWavFileTest() {
        assertEquals(true, Config.wavTest.exists());
    }

    @Test
    public void iconExistsTest() {
        assertEquals(true, new File(Config.iconPath).exists());
    }

}
