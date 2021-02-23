package ru.cft.focus.minesweeper.view;

import javax.swing.*;

class MinesweeperCell extends JLabel {
    private final int row;
    private final int column;

    MinesweeperCell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }
}