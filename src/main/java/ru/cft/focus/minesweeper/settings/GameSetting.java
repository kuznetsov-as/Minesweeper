package ru.cft.focus.minesweeper.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum GameSetting {

    EASY(9, 9, 10),
    MEDIUM(16, 16, 40),
    HARD(16, 30, 99),
    CUSTOM(0, 0, 0);

    @Setter
    private int rowNumber;
    @Setter
    private int columnNumber;
    @Setter
    private int countMines;
}
