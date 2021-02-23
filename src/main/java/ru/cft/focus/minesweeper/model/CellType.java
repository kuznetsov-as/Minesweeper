package ru.cft.focus.minesweeper.model;

import ru.cft.focus.minesweeper.api.CellCode;

public enum CellType {
    MINE(CellCode.MINE),
    FLAG(CellCode.FLAG),
    QUESTION(CellCode.QUESTION),
    CLOSED(CellCode.CLOSED),
    OPENED(CellCode.OPENED);

    private final String code;

    CellType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
