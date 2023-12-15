package fr.erwan.analyse;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import fr.erwan.config.Config;
import fr.erwan.notes.Accordage;
import fr.erwan.notes.CalculateNoteFrequencies;
import fr.erwan.notes.Frequences;

public class MainFrequencyAnalyser {

    private final short MINFREQ = Config.minFreq;
    private final short MAXFREQ = Config.maxFreq;
    private Frequences freq;

    public MainFrequencyAnalyser(Frequences fre) {
        this.freq = fre;
    }

    public Accordage main(double[] audioSamples, double sampleRate) {
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] complexTransform = transformer.transform(audioSamples, TransformType.INVERSE);
        double[] frequencies = calculateFrequencies(sampleRate, audioSamples.length);
        double mainFrequency = findMainFrequency(complexTransform, frequencies);
        return CalculateNoteFrequencies.findNearsetNote(mainFrequency, freq.getNotes(), freq.getNotesNames());
    }



    private double[] calculateFrequencies(double sampleRate, int dataSize) {
        double[] frequencies = new double[dataSize];
        double deltaFreq = sampleRate / (double)dataSize;

        // break loop if fr Hz > Config.MAXFREQ
        for (int i = 0; i < dataSize; i++) {
            double fr = i * deltaFreq;
            frequencies[i] = fr;
            if (fr > MAXFREQ) {
                break;
            }
        }
        return frequencies;
    }

    private double findMainFrequency(Complex[] complexTransform, double[] frequencies) {
        double maxMagnitude = 0;
        double mainFrequency = 0;

        int ln = (int) complexTransform.length/2;

        for (int i = 0; i < ln ; i++) {
            double magnitude = complexTransform[i].abs();
            if (magnitude > maxMagnitude & frequencies[i]> MINFREQ & frequencies[i] < MAXFREQ) {
                maxMagnitude = magnitude;
                mainFrequency = frequencies[i];
            }
        }
        return mainFrequency;
    }
}
