package controller;

import java.util.List;
import javax.swing.*;
import model.Step;
import ui.PseudocodePanel;
import ui.VisualizerPanel;
import utils.SoundManager;

public class PlaybackController {
    private final VisualizerPanel viz;
    private final PseudocodePanel pseudo;
    private final JLabel lblMessage;
    private final JLabel lblStats;
    private final JProgressBar progress;
    private final List<Step> steps;
    private final int[] initialArray;
    private final Timer timer;
    private int delay = 300;
    private int index = 0;
    private int comparisons = 0, swaps = 0;
    
    // For success animation
    private final Timer successTimer; 
    private int successIndex = 0;

    public PlaybackController(VisualizerPanel viz, PseudocodePanel pseudo, JLabel lblMessage, JLabel lblStats, JProgressBar progress, List<Step> steps, int[] initial) {
        this.viz = viz; this.pseudo = pseudo; this.lblMessage = lblMessage; 
        this.lblStats = lblStats; this.progress = progress; this.steps = steps; 
        this.initialArray = initial.clone();
        
        timer = new Timer(delay, e -> stepForward());
        
        // Success Animation Timer
        successTimer = new Timer(15, e -> {
            if (successIndex < initialArray.length) {
                viz.setSweepIndex(successIndex++);
                // Play sweep sound
                SoundManager.playTone(successIndex * 10, initialArray.length * 10, 15); 
            } else {
                stopAll(); // Stop everything immediately when done
                viz.setSweepIndex(initialArray.length);
                lblMessage.setText("<html><b style='color:#32cd32'>SORTED!</b></html>");
            }
        });
        
        resetMetrics();
    }

    public void setDelay(int ms) { this.delay = ms; timer.setDelay(ms); }
    public boolean isRunning() { return timer.isRunning(); }
    public void play() { if (!timer.isRunning() && !successTimer.isRunning()) timer.start(); }
    
    public void pause() { 
        if (timer.isRunning()) timer.stop();
        if (successTimer.isRunning()) successTimer.stop();
        SoundManager.stopSound(); // Kill sound on pause
    }
    
    // FIX: Ensure this kills the audio buffer
    public void stopAll() {
        if (timer.isRunning()) timer.stop();
        if (successTimer.isRunning()) successTimer.stop();
        SoundManager.stopSound(); // FORCE SILENCE
    }
    
    public void reset() {
        stopAll();
        index = 0; comparisons = swaps = 0; successIndex = 0;
        viz.setArray(initialArray); pseudo.highlightLine(-1);
        viz.setHighlight(-1, -1); viz.clearFinalMarks(); viz.setSweepIndex(-1);
        updateStats(); lblMessage.setText("Ready"); progress.setValue(0);
    }

    public void stepForward() {
        if (index >= steps.size()) { 
            if (timer.isRunning()) timer.stop();
            progress.setValue(100);
            
            // Trigger Success Animation if not already running
            if (!successTimer.isRunning() && successIndex == 0) {
                viz.clearFinalMarks();
                successTimer.start(); 
            }
            return; 
        }
        Step s = steps.get(index++); applyStep(s);
    }

    public void stepBack() {
        // Kill success effect immediately if stepping back
        if (successTimer.isRunning() || successIndex > 0) { 
            stopAll(); 
            successIndex = 0; 
            viz.setSweepIndex(-1); 
        }
        
        if (index <= 0) return;
        int target = Math.max(0, index - 1);
        viz.setArray(initialArray); viz.setHighlight(-1, -1); pseudo.highlightLine(-1); viz.clearFinalMarks();
        comparisons = swaps = 0;
        for (int k = 0; k < target; k++) applyStepInternal(steps.get(k), false); // No sound on rewind
        index = target; updateStats(); progress.setValue((int) (index * 100.0 / Math.max(1, steps.size())));
    }

    private void applyStep(Step s) {
        applyStepInternal(s, true);
        updateStats();
        lblMessage.setText(s.message == null ? "" : "<html><b>" + s.message + "</b></html>");
        progress.setValue((int) (index * 100.0 / Math.max(1, steps.size())));
    }

    private void applyStepInternal(Step s, boolean playSound) {
        if (s.snapshot != null) viz.setArray(s.snapshot);
        
        int maxVal = 100; 
        if (s.snapshot != null && s.snapshot.length > 0) maxVal = Math.max(10, s.snapshot.length * 3);

        // Dynamic sound duration based on speed
        int soundDur = Math.max(5, delay / 2);

        switch (s.type) {
            case COMPARE -> { 
                viz.setHighlight(s.i, s.j); comparisons++; 
                if (playSound && s.i >= 0 && s.i < s.snapshot.length) 
                    SoundManager.playTone(s.snapshot[s.i], maxVal, soundDur);
            }
            case SWAP -> { 
                viz.setHighlight(s.i, s.j); swaps++; 
                if (playSound && s.i >= 0 && s.i < s.snapshot.length)
                    SoundManager.playTone(s.snapshot[s.i], maxVal, soundDur * 2);
            }
            case SET -> {
                viz.setHighlight(s.i, s.j);
                if (playSound && s.i >= 0 && s.i < s.snapshot.length)
                    SoundManager.playTone(s.snapshot[s.i], maxVal, soundDur);
            }
            case MARK_FINAL -> viz.markFinal(s.i);
            case PIVOT -> viz.setHighlight(s.i, -1);
        }
        pseudo.highlightLine(s.pseudocodeLine);
    }

    private void resetMetrics() { comparisons = swaps = 0; updateStats(); }
    private void updateStats() { lblStats.setText("<html>Comparisons: " + comparisons + "<br/>Swaps: " + swaps + "<br/>Step: " + index + "/" + steps.size() + "</html>"); }
}