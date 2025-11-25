package model;

import java.util.List;

public interface SortingAlgorithm {
    List<Step> generateSteps(int[] input);
    String getName();
    String[] getPseudocodeLines();
    String getDefinition();
    String getAvgTime();
    String getWorstTime();
    String getSpace();
    String getTypicalUse();
    String getAdvantages();
    String getDisadvantages();
}