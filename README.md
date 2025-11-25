# 🚀 DSA Game: Sorting Adventure

**Sorting Adventure** is an interactive Java Swing application designed to visualize how sorting algorithms work. It features step-by-step visualization, real-time pseudocode tracking, time complexity analysis, and auditory feedback (sonification) to make learning algorithms intuitive and fun.

![Project Screenshot](image.png)

## ✨ Features

* **📊 Algorithm Visualization:** Watch 8 different algorithms sort arrays in real-time.
    * Bubble, Insertion, Selection, Merge, Quick, Heap, Shell, and Counting Sort.
* **🎵 Auditory Feedback (Sonification):** Hear the sorting!
    * Pitch changes based on value.
    * Comparisons and Swaps have distinct sounds.
    * Satisfying "Success Sweep" sound when finished.
* **🎮 Interactive Controls:**
    * **Play/Pause:** `Spacebar` or on-screen button.
    * **Step-by-Step:** `Left Arrow` / `Right Arrow` for precise analysis.
    * **Speed Control:** Slider to adjust sorting speed.
* **📝 Educational Context:**
    * Displays **Pseudocode** highlighting the exact line being executed.
    * Shows **Time & Space Complexity** (Best, Average, Worst).
    * **Comparison Mode:** Compare the current algorithm against others in a detailed table.
* **🛠 Custom Inputs:** Randomize arrays or input your own comma-separated numbers.

## 📂 Project Structure

This project follows the **MVC (Model-View-Controller)** architectural pattern:

```text
SortingAdventure/
├── src/
│   ├── Main.java                  # Entry Point
│   ├── model/                     # Data logic (Step, SortingAlgorithm interface)
│   ├── view/ (or ui/)             # UI Components (VisualizerPanel, PseudocodePanel)
│   ├── controller/                # Logic (PlaybackController)
│   ├── algorithms/                # Sorting Implementations (Bubble, Quick, etc.)
│   └── utils/                     # Helpers (SoundManager)
└── README.md

