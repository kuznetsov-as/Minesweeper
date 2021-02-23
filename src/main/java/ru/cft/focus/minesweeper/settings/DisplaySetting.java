package ru.cft.focus.minesweeper.settings;

public final class DisplaySetting {
    private DisplaySetting() {
    }

    private static final int ACTIVE_CELL_WIDTH = 40;
    private static final int ACTIVE_CELL_HEIGHT = 40;

    public static final int WIDTH_GAP = 2;
    public static final int HEIGHT_GAP = 2;

    public static final int CELL_WIDTH = ACTIVE_CELL_WIDTH + WIDTH_GAP;
    public static final int CELL_HEIGHT = ACTIVE_CELL_HEIGHT + HEIGHT_GAP;

    public static final int GAME_INFO_PANEL_HEIGHT = 110;
    public static final int TIME_PANEL_HEIGHT = 25;
    public static final int RESTART_BUTTON_HEIGHT = 25;
    public static final int NUMBER_OF_MINES_PANEL_HEIGHT = 25;
}
