package ru.cft.focus.minesweeper.model;

import ru.cft.focus.minesweeper.settings.GameSetting;
import ru.cft.focus.minesweeper.view.MinesweeperView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MinesweeperViewNotifier {
    private final List<MinesweeperView> minesweeperViews = new ArrayList<>();

    void attachView(MinesweeperView minesweeperView) {
        minesweeperViews.add(minesweeperView);
    }

    void notifyViewsNewGame(int rowNumber, int columnNumber) {
        minesweeperViews.forEach(minesweeperView -> minesweeperView.renderNewGame(rowNumber, columnNumber));
    }

    void notifyViewsRestartGame(GameSetting gameSetting) {
        minesweeperViews.forEach(minesweeperView -> minesweeperView.renderRestartGame(gameSetting));
    }

    void notifyViewAboutGameRules(String informationAboutGameRules) {
        minesweeperViews.forEach(minesweeperView -> minesweeperView.showInformationAboutGameRules(informationAboutGameRules));
    }

    void notifyViewAboutCellStatusChanged(int row, int column, CellType cellType) {
        minesweeperViews.forEach(minesweeperView -> minesweeperView.updateCell(row, column, cellType.getCode()));
    }

    void notifyViewAboutCellStatusChanged(int row, int column, int neighbourMineCount) {
        minesweeperViews.forEach(minesweeperView -> minesweeperView.updateCell(row, column, neighbourMineCount));
    }

    void notifyViewAboutTimerUpdate(int seconds) {
        for (MinesweeperView minesweeperView : minesweeperViews) {
            minesweeperView.updateTimerPanel(seconds);
        }
    }

    void notifyViewAboutNumberOfMinesUpdate(int numberOfRemainingMines) {
        minesweeperViews.forEach(minesweeperView -> minesweeperView.updateNumberOfMinesPanel(numberOfRemainingMines));
    }

    void notifyViewGameWon() {
        minesweeperViews.forEach(MinesweeperView::renderGameWon);
    }

    void notifyViewShowRecords(Map<String, Integer> recordsMap) {
        minesweeperViews.forEach(minesweeperView -> minesweeperView.showRecords(recordsMap));
    }

    void notifyViewAboutNewRecord() {
        minesweeperViews.forEach(MinesweeperView::renderNewRecord);
    }

    void notifyViewGameLose() {
        minesweeperViews.forEach(MinesweeperView::renderGameLose);
    }
}
