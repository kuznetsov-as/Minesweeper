package ru.cft.focus.minesweeper.model;

import java.io.Serializable;

public class Record implements Serializable, Comparable<Record> {
    private final String name;
    private final int time;

    Record(String name, int time) {
        this.name = name;
        this.time = time;
    }

    String getName() {
        return name;
    }

    int getTime() {
        return time;
    }

    @Override
    public int compareTo(Record record) {
        return Integer.compare(this.time, record.time);
    }
}
