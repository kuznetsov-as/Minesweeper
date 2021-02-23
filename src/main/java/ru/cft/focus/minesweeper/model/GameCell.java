package ru.cft.focus.minesweeper.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class GameCell {
    private final int row;
    private final int column;
    private boolean flag;
    private boolean question;
    private boolean mine;
    private boolean open;
    private int neighbourMineCount;

    GameCell(int row, int column, boolean mine) {
        this.row = row;
        this.column = column;
        this.mine = mine;
    }
}
