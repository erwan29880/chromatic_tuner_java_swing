package fr.erwan.analyse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import fr.erwan.config.Config;

public class FileToByte {

    private double sampleRate;
    private final static int maxByteSize = Config.maxByteSize;
      
    public FileToByte() {
    }

    /**
     * Lire un fichier audio wave et le convertit en double[] pour traitement par fft
     * @param file le fichier wave à lire
     * @return les bits lus et convertis en double
     * @throws UnsupportedAudioFileException si le format n'est pas supporté
     */
    public double[] readFully() throws UnsupportedAudioFileException {
        byte[] bytes;
        try (AudioInputStream in = AudioSystem.getAudioInputStream(Config.wavTest)) {

            // format du fichier audio
            AudioFormat fmt = in.getFormat();
            System.out.println(fmt);

            // si le format audio n'est pas supporté
            if(fmt.getEncoding() != Encoding.PCM_SIGNED) {
                throw new UnsupportedAudioFileException();
            }

            // enregistrer le frame rate nécessaire pour l'analyse fft
            this.sampleRate = fmt.getFrameRate();

            // variables pour passer les bytes en doubles
            int bits = fmt.getSampleSizeInBits();
            double max = Math.pow(2, bits - 1);
            
            // lire les bytes
            bytes = new byte[in.available()];
            in.read(bytes);

            // gérer le cas 2 channels
            if (fmt.getChannels() == 2) {
                bytes = extract16BitsSingleChannels(bytes, 2).get(1);
            }
            
            // tronquer l'array de byte à une puissance de 2 pour analyse fft
            bytes = Arrays.copyOfRange(bytes, 0, maxByteSize);
            
            // vérifier l'ordre des bytes
            ByteBuffer bb = ByteBuffer.wrap(bytes);
            bb.order(fmt.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

            // convertir les bytes en doubles
            double[] samples = new double[bytes.length * 8 / bits];
            for(int i = 0; i < samples.length; ++i) {
                switch(bits) {
                    case 8:  samples[i] = ( bb.get() / max );
                            break;
                    case 16: samples[i] = ( bb.getShort() / max );
                            break;
                    case 32: samples[i] = ( bb.getInt() / max );
                            break;
                    case 64: samples[i] = ( bb.getLong() / max );
                            break;
                    default: throw new UnsupportedAudioFileException();
                }
            }

            return samples;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    /**
     * 
     * @param bytes les données enregistrées par la classe WavFileHelper
     * @param nbChannels donnée de la classe WavFileHelper
     * @return les bits lus et convertis en double
     */
    public double[] readFully(byte[] bytes, int nbChannels) {
        int bits = 16;
        double max = Math.pow(2, bits - 1);
        
        if (nbChannels == 2) {
            bytes = extract16BitsSingleChannels(bytes, 2).get(1);
        }
        bytes = Arrays.copyOfRange(bytes, 0, (int)Math.pow(2, 16));
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        double[] samples = new double[bytes.length * 8 / bits];
        for(int i = 0; i < samples.length; ++i) {
            switch(bits) {
                case 8:  samples[i] = ( bb.get() / max );
                        break;
                case 16: samples[i] = ( bb.getShort() / max );
                        break;
                case 32: samples[i] = ( bb.getInt() / max );
                        break;
                case 64: samples[i] = ( bb.getLong() / max );
                        break;
                default: samples[i] = ( bb.getShort() / max );
            }
        }

        return samples;
    }


    /**
     * Convertir les 2 channels en 1 channel
     * @param audioBuffer l'array de byte
     * @param channels le nombre de channels
     * @return les bytes
     */
    public ArrayList<byte[]> extract16BitsSingleChannels(byte[] audioBuffer, int channels) {
        ArrayList<byte[]> channelsData = new ArrayList<byte[]>();
        final int channelLength=audioBuffer.length/channels;

        for (int c=0 ; c < channels ; c++) {
            byte[] channel=new byte[channelLength];
            channelsData.add(channel);
        }

        int byteIndex=0; 

        for(int i = 0; i < channelLength; i+=2) {
            for (int c=0 ; c < channels ; c++) {
                channelsData.get(c)[i]=audioBuffer[byteIndex];   // 1st Byte
                byteIndex++;
                channelsData.get(c)[i+1]=audioBuffer[byteIndex]; // 2nd Byte
                byteIndex++;
            }
        }

        return channelsData;   
    }

    // getter
    public double getSampleRate() {
        return sampleRate;
    }
}
