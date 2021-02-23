package ru.cft.focus.minesweeper.view;

import ru.cft.focus.minesweeper.settings.GameSetting;

import java.util.Map;

public interface MinesweeperView {
    void renderNewGame(int rowNumber, int columnNumber);

    void renderRestartGame(GameSetting gameSetting);

    void renderGameWon();

    void renderNewRecord();

    void showRecords(Map<String, Integer> recordsMap);

    void showInformationAboutGameRules(String informationAboutGameRules);

    void renderGameLose();

    void updateCell(int row, int column, String code);

    void updateCell(int row, int column, int neighbourMineCount);

    void updateTimerPanel(int seconds);

    void updateNumberOfMinesPanel(int numberOfRemainingMines);
}
