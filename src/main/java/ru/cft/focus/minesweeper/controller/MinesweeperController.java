package ru.cft.focus.minesweeper.controller;

import ru.cft.focus.minesweeper.model.MinesweeperGame;
import ru.cft.focus.minesweeper.settings.GameSetting;

public class MinesweeperController {
    private final MinesweeperGame minesweeperGame;

    public MinesweeperController(MinesweeperGame minesweeperGame) {
        this.minesweeperGame = minesweeperGame;
    }

    public void handleClickedRightButtonOnCell(int row, int column) {
        minesweeperGame.markCell(row, column);
    }

    public void handleClickedWheelButtonOnCell(int row, int column) {
        minesweeperGame.openCellNeighbors(row, column);
    }

    public void handleClickedLeftButtonOnCell(int row, int column) {
        minesweeperGame.openCell(row, column);
    }

    public void handleClickedRestartButton(GameSetting gameSetting) {
        minesweeperGame.restartGame(gameSetting);
    }

    public void handleRecordDisplayRequest(GameSetting gameSetting) {
        minesweeperGame.createRecordsMap(gameSetting);
    }

    public void handleAboutDisplayRequest() {
        minesweeperGame.createStringWithInformationAboutGame();
    }

    public void handleRecordData(String name, GameSetting gameSetting) {
        minesweeperGame.createNewRecord(name,gameSetting);
    }

    public void exit() {
        System.exit(0);
    }
}
