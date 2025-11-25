package utils;

import javax.sound.sampled.*;

public class SoundManager {
    private static final int SAMPLE_RATE = 44100;
    private static boolean muted = false;
    
    // Initialize the audio line safely
    private static SourceDataLine line = initAudioLine();

    private static SourceDataLine initAudioLine() {
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
            SourceDataLine newLine = AudioSystem.getSourceDataLine(format);
            newLine.open(format, 4096); // 4096 byte buffer
            newLine.start();
            return newLine;
        } catch (LineUnavailableException e) {
            System.err.println("Audio system unavailable: " + e.getMessage());
            return null;
        }
    }

    public static void setMuted(boolean isMuted) {
        muted = isMuted;
        if (muted) stopSound(); 
    }

    public static boolean isMuted() {
        return muted;
    }

    public static void stopSound() {
        if (line != null && line.isOpen()) {
            line.flush(); // Clear the buffer immediately
        }
    }

    public static void playTone(int value, int max, int durationMs) {
        if (muted || line == null || !line.isOpen()) return;

        // Calculate pitch based on value (Linear mapping between 150Hz and 1200Hz)
        double normalized = (double) value / max;
        int frequency = 150 + (int) (normalized * 1050);

        // Limit duration to prevent UI lag (max 80ms)
        int actualDuration = Math.min(durationMs, 80); 
        int numSamples = (int) ((actualDuration / 1000.0) * SAMPLE_RATE);
        byte[] buffer = new byte[numSamples];

        // Generate Sine Wave
        for (int i = 0; i < numSamples; i++) {
            double angle = 2.0 * Math.PI * i / (SAMPLE_RATE / (double) frequency);
            buffer[i] = (byte) (Math.sin(angle) * 100); // Volume ~80%
            
            // Fade out the last 20% to prevent clicking sounds
            if (i > numSamples * 0.8) {
                buffer[i] = (byte)(buffer[i] * ((numSamples - i) / (numSamples * 0.2)));
            }
        }

        // Only write if the line can accept data (prevents blocking the UI thread)
        if (line.available() >= numSamples) {
            line.write(buffer, 0, buffer.length);
        }
    }
    
    public static void close() {
        if (line != null) {
            line.drain();
            line.close();
            line = null; // Set to null to prevent accidental reuse
        }
    }
}