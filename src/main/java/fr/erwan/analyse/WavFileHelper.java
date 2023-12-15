package fr.erwan.analyse;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import fr.erwan.config.Config;

public class WavFileHelper {    
 
    public static byte[] runRecord() {
        int recordingSampleRate = Config.recordingSampleRate;
        short recordingBitsPerSample = Config.recordingBitsPerSample;
        short recordingNumChannels = Config.recordingNumChannels;
        byte[] b = null;

        try {
            //initiate wave files
            WavData wavRecordData = new WavData();
            wavRecordData.put(WaveSection.SAMPLE_RATE, recordingSampleRate);
            wavRecordData.put(WaveSection.BITS_PER_SAMPLE, recordingBitsPerSample);
            wavRecordData.put(WaveSection.NUM_CHANNELS, recordingNumChannels);

            // record
            WavAudioRecorder recorder = new WavFileHelper.WavAudioRecorder(wavRecordData);
            recorder.startRecording();
            b = wavRecordData.getBytes(WaveSection.DATA);
        } catch (Exception ex) {
            ex.printStackTrace();
            return b;
        }
        return b;
    }

    public static enum WaveSection {
        // 12 Bytes
        CHUNK_ID(4, ByteOrder.BIG_ENDIAN),
        CHUNK_SIZE(4, ByteOrder.LITTLE_ENDIAN),
        FORMAT(4, ByteOrder.BIG_ENDIAN),

        // 24 Bytes
        SUBCHUNK1_ID(4, ByteOrder.BIG_ENDIAN),
        SUBCHUNK1_SIZE(4, ByteOrder.LITTLE_ENDIAN),
        AUDIO_FORMAT(2, ByteOrder.LITTLE_ENDIAN),
        NUM_CHANNELS(2, ByteOrder.LITTLE_ENDIAN),
        SAMPLE_RATE(4, ByteOrder.LITTLE_ENDIAN),
        BYTE_RATE(4, ByteOrder.LITTLE_ENDIAN),
        BLOCK_ALIGN(2, ByteOrder.LITTLE_ENDIAN),
        BITS_PER_SAMPLE(2, ByteOrder.LITTLE_ENDIAN),

        // 8 Bytes
        SUBCHUNK2_ID(4, ByteOrder.BIG_ENDIAN),
        SUBCHUNK2_SIZE(4, ByteOrder.LITTLE_ENDIAN),
        DATA(0, ByteOrder.LITTLE_ENDIAN),
        ;

        private Integer numBytes;
        private ByteOrder endian;
        WaveSection(Integer numBytes, ByteOrder endian){
            this.numBytes = numBytes;
            this.endian = endian;
        }
    }

    public static class WavData extends LinkedHashMap<WaveSection, byte[]>{
        static int HEADER_SIZE = 44; // There are 44 bits before the data section
        static int DEFAULT_SUBCHUNK1_SIZE = 16;
        static short DEFAULT_AUDIO_FORMAT = 1;
        static short DEFAULT_BLOCK_ALIGN = 4;
        static String DEFAULT_CHUNK_ID = "RIFF";
        static String DEFAULT_FORMAT = "WAVE";
        static String DEFAULT_SUBCHUNK1_ID = "fmt ";
        static String DEFAULT_SUBCHUNK2_ID = "data";

        public WavData(){
            this.put(WaveSection.CHUNK_ID, DEFAULT_CHUNK_ID);
            this.put(WaveSection.FORMAT, DEFAULT_FORMAT);
            this.put(WaveSection.SUBCHUNK1_ID, DEFAULT_SUBCHUNK1_ID);
            this.put(WaveSection.SUBCHUNK1_SIZE, DEFAULT_SUBCHUNK1_SIZE);
            this.put(WaveSection.AUDIO_FORMAT, DEFAULT_AUDIO_FORMAT);
            this.put(WaveSection.BLOCK_ALIGN, DEFAULT_BLOCK_ALIGN);
            this.put(WaveSection.SUBCHUNK2_ID, DEFAULT_SUBCHUNK2_ID);

            this.put(WaveSection.CHUNK_SIZE, 0);
            this.put(WaveSection.SUBCHUNK2_SIZE, 0);
            this.put(WaveSection.BYTE_RATE, 0);
        }

        public void put(WaveSection waveSection, String value){
            byte[] bytes = value.getBytes();
            this.put(waveSection, bytes);
        }

        public void put(WaveSection waveSection, int value) {
            byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
            this.put(waveSection, bytes);
        }

        public void put(WaveSection waveSection, short value) {
            byte[] bytes = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
            this.put(waveSection, bytes);
        }

        public byte[] getBytes(WaveSection waveSection) {
            return this.get(waveSection);
        }

        public String getString(WaveSection waveSection) {
            byte[] bytes = this.get(waveSection);
            return new String(bytes);
        }

        public int getInt(WaveSection waveSection) {
            byte[] bytes = this.get(waveSection);
            return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        }

        public short getShort(WaveSection waveSection) {
            byte[] bytes = this.get(waveSection);
            return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        }

        public void printByteInfo() {
            for (WaveSection waveSection : WaveSection.values()) {
                if (waveSection.numBytes == 4
                        && waveSection.endian == ByteOrder.BIG_ENDIAN) {
                    System.out.println("SECTION:" + waveSection + ":STRING:"
                            + this.getString(waveSection));
                } else if (waveSection.numBytes == 4
                        && waveSection.endian == ByteOrder.LITTLE_ENDIAN) {
                    System.out.println("SECTION:" + waveSection + ":INTEGER:"
                            + this.getInt(waveSection));
                } else if (waveSection.numBytes == 2
                        && waveSection.endian == ByteOrder.LITTLE_ENDIAN) {
                    System.out.println("SECTION:" + waveSection + ":SHORT:"
                            + this.getShort(waveSection));
                } else {
                    // Data Section
                }
            }
        }

      
        public AudioFormat createAudioFormat() {
            boolean audioSignedSamples = true; // Samples are signed
            boolean audioBigEndian = false;
            float sampleRate = (float) this.getInt(WaveSection.SAMPLE_RATE);
            int bitsPerSample = (int) this.getShort(WaveSection.BITS_PER_SAMPLE);
            int numChannels = (int) this.getShort(WaveSection.NUM_CHANNELS);
            return new AudioFormat(sampleRate, bitsPerSample,
                    numChannels, audioSignedSamples, audioBigEndian);
        }
    }

  

    public static class WavAudioRecorder implements Runnable {
        WavData waveData = new WavData();
        boolean recording = true;
        Thread runningThread;
        ByteArrayOutputStream byteArrayOutputStream;

        public WavAudioRecorder(WavData waveData){
            this.waveData = waveData;
        }


        public WavData startRecording(){
            this.recording = true;
            this.runningThread = new Thread(this);
            runningThread.start();
            try {
                Thread.sleep(Config.recordDuration);
                this.recording = false;
                waveData.put(WaveSection.DATA, byteArrayOutputStream.toByteArray());
                return waveData;
    
            } catch (InterruptedException ex) {
                //handle the exception
                return null;
            } catch (Exception e) {
                return null;
            }
        }

        public WavData stopRecording() throws Exception{
            this.recording = false;
            // runningThread.stop();

            waveData.put(WaveSection.DATA, byteArrayOutputStream.toByteArray());

            return waveData;
        }

        public void run() {
            try {
                // Create an audio output stream for byte array
                byteArrayOutputStream  = new ByteArrayOutputStream();

                // Write audio input stream to speaker source data line
                AudioFormat audioFormat = waveData.createAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
                TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
                targetDataLine.open(audioFormat);
                targetDataLine.start();

                // Loop through target data line to write to output stream
                int numBytesRead;
                byte[] data = new byte[targetDataLine.getBufferSize() / 5];
                while(recording) {
                    numBytesRead =  targetDataLine.read(data, 0, data.length);
                    byteArrayOutputStream.write(data, 0, numBytesRead);
                }

                // Cleanup
                targetDataLine.stop();
                targetDataLine.close();
                byteArrayOutputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
