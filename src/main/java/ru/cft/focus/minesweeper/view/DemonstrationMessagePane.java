package ru.cft.focus.minesweeper.view;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.Map;

@Slf4j
class DemonstrationMessagePane extends MessagePane {

    void showAbout(String about) {
        JTextArea textArea = new JTextArea(30, 45);
        textArea.setEditable(false);

        textArea.setText(about);

        //Автоматическая прокрутка текстового поля вверх
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane,
                "Об игре", JOptionPane.INFORMATION_MESSAGE,
                null);
    }

    void showRecords(Map<String, Integer> recordsMap) {
        StringBuilder stringRecords = new StringBuilder();

        if (recordsMap.size() == 0) {
            stringRecords.append("Пока никто не побеждал!");
        } else {
            int number = 1;
            for (Map.Entry<String, Integer> entry : recordsMap.entrySet()) {
                stringRecords.append(number).append(": ").append(entry.getKey()).append(" - ").append(entry.getValue());
                stringRecords.append(System.lineSeparator());
                number++;
            }
        }

        JTextArea textArea = new JTextArea(15, 10);
        textArea.setEditable(false);
        textArea.setText(stringRecords.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Автоматическая прокрутка текстового поля вверх
        textArea.setCaretPosition(0);

        JOptionPane.showMessageDialog(this, scrollPane,
                "Рекорды", JOptionPane.INFORMATION_MESSAGE, null);
    }
}
