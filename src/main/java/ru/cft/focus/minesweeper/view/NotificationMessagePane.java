package ru.cft.focus.minesweeper.view;

import lombok.extern.slf4j.Slf4j;
import ru.cft.focus.minesweeper.api.CellCode;

import javax.swing.*;
import java.util.Optional;

@Slf4j
class NotificationMessagePane extends MessagePane {
    void sayNoImageFound() {
        log.info("Не удалось найти иконку");
        JOptionPane.showMessageDialog(this, "Не удалось найти иконку", "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }

    void sayYouWin() {
        Optional<ImageIcon> imageIconOpt = iconRegistry.getImageForCell(CellCode.FLAG);
        if (imageIconOpt.isEmpty()) {
            sayNoImageFound();
            return;
        }

        JOptionPane.showMessageDialog(this, "Поздравляю с победой!",
                "Теперь зомби не доберутся до нас", JOptionPane.INFORMATION_MESSAGE, imageIconOpt.get());
    }

    void sayYouLose() {
        Optional<ImageIcon> imageIconOpt = iconRegistry.getImageForCell(CellCode.MINE);
        if (imageIconOpt.isEmpty()) {
            sayNoImageFound();
            return;
        }

        JOptionPane.showMessageDialog(this, "Тебя сожрали :(",
                "Суровые реалии", JOptionPane.INFORMATION_MESSAGE, imageIconOpt.get());
    }
}
