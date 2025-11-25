package model;

public class Step {
    public enum Type { COMPARE, SWAP, SET, MARK_FINAL, PIVOT }
    public final Type type;
    public final int i, j;
    public final int[] snapshot;
    public final String message;
    public final int pseudocodeLine;

    public Step(Type type, int i, int j, int[] snapshot, String message, int pseudocodeLine) {
        this.type = type; this.i = i; this.j = j;
        this.snapshot = snapshot == null ? null : snapshot.clone();
        this.message = message;
        this.pseudocodeLine = pseudocodeLine;
    }
}