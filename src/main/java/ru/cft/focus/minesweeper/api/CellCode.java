package ru.cft.focus.minesweeper.api;

public final class CellCode {
    private CellCode() {
    }

    public static final String MINE = "zombie";
    public static final String FLAG = "pentagram";
    public static final String QUESTION = "question";
    public static final String CLOSED = "grave";
    public static final String OPENED = "open grave with the number 0";
}
