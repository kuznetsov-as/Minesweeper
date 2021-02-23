package ru.cft.focus.minesweeper;

import lombok.extern.slf4j.Slf4j;
import ru.cft.focus.minesweeper.controller.MinesweeperController;
import ru.cft.focus.minesweeper.model.MinesweeperGame;
import ru.cft.focus.minesweeper.settings.GameSetting;
import ru.cft.focus.minesweeper.view.MinesweeperView;
import ru.cft.focus.minesweeper.view.SwingMinesweeperView;

@Slf4j
public class MinesweeperTask {
    public static void main(String[] args) {
        log.info("Программа запущена");
        new MinesweeperTask().run();
    }

    private void run() {
        MinesweeperGame minesweeperGame = new MinesweeperGame(GameSetting.EASY.getRowNumber(),
                GameSetting.EASY.getColumnNumber(),
                GameSetting.EASY.getCountMines());
        MinesweeperController minesweeperController = new MinesweeperController(minesweeperGame);
        MinesweeperView minesweeperView = new SwingMinesweeperView(minesweeperController);

        minesweeperGame.attachView(minesweeperView);
        minesweeperGame.startNewGame(GameSetting.EASY);
    }
}
