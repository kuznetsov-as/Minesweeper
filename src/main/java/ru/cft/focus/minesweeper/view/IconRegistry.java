package ru.cft.focus.minesweeper.view;

import lombok.Getter;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class IconRegistry {
    private final Map<String, ImageIcon> cellIconMap = new HashMap<>();
    @Getter
    private final Map<Integer, String> openedCellCodeMap = new HashMap<>();

    IconRegistry() {
        cellIconMap.put("restart", new ImageIcon(
                IconRegistry.class.getResource("/images/restart.png")));

        cellIconMap.put("pentagram", new ImageIcon(
                IconRegistry.class.getResource("/images/flag.png")));

        cellIconMap.put("question", new ImageIcon(
                IconRegistry.class.getResource("/images/question.png")));

        cellIconMap.put("grave", new ImageIcon(
                IconRegistry.class.getResource("/images/grave.png")));

        cellIconMap.put("zombie", new ImageIcon(
                IconRegistry.class.getResource("/images/zombie.png")));

        for (int i = 0; i < 9; i++) {
            cellIconMap.put("open grave with the number " + i, new ImageIcon(
                    IconRegistry.class.getResource("/images/grave-" + i + ".png")));
        }

        for (int i = 0; i < 9; i++) {
            openedCellCodeMap.put(i, "open grave with the number " + i);
        }
    }

    Optional<ImageIcon> getImageForCell(String code) {
        return Optional.ofNullable(cellIconMap.get(code));
    }
}