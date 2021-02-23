package ru.cft.focus.minesweeper.view;

import javax.swing.*;

class InterrogativeMessagePane extends MessagePane {
    /**
     * @return Возвращает имя, которое ввел пользователь. Если окно ввода было закрыто, метод возвращает "-1"
     */
    String askForName() {
        while (true) {
            JTextField responseTextField = new JTextField(10);
            int selectedOption = showQuestion("Введите ваше имя: ", responseTextField);
            if (selectedOption == -1) {
                return "-1";
            }
            if (responseTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Ты не ввел имя!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else if (responseTextField.getText().length() > 12) {
                JOptionPane.showMessageDialog(this, "Длина имени не должна превышать 12 символов",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                return responseTextField.getText();
            }
        }
    }

    /**
     * @return Возвращает количество рядов, которое ввел пользователь. Если окно ввода было закрыто,
     * метод возвращает "-1"
     */
    int askForTheCountOfRows() {
        while (true) {
            JTextField responseTextField = new JTextField(2);
            int selectedOption = showQuestion("Введите количество рядов:", responseTextField);
            if (selectedOption == -1) {
                return -1;
            }
            try {
                int rowNumber = Integer.parseInt(responseTextField.getText());
                if (rowNumber > 16 || rowNumber < 5) {
                    JOptionPane.showMessageDialog(this, "Количество рядов не может быть " +
                            "меньше 5 или больше 16!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else {
                    return rowNumber;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите целое " +
                        "число больше нуля", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @return Возвращает количество столбцов, которое ввел пользователь. Если окно ввода было закрыто,
     * метод возвращает "-1"
     */
    int askForTheCountOfColumns() {
        while (true) {
            JTextField responseTextField = new JTextField(2);
            int selectedOption = showQuestion("Введите количество столбцов:", responseTextField);
            if (selectedOption == -1) {
                return -1;
            }
            try {
                int columnNumber = Integer.parseInt(responseTextField.getText());
                if (columnNumber > 30 || columnNumber < 5) {
                    JOptionPane.showMessageDialog(this, "Количество столбцов не может быть " +
                            "меньше 5 или больше 30!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else {
                    return columnNumber;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите целое " +
                        "число больше нуля", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @return Возвращает количество мин, которое ввел пользователь. Если окно ввода было закрыто,
     * метод возвращает "-1"
     */
    int askForTheCountOfMines(int rowNumber, int columnNumber) {
        while (true) {
            JTextField responseTextField = new JTextField(2);
            int selectedOption = showQuestion("Введите количество зомби:", responseTextField);
            if (selectedOption == -1) {
                return -1;
            }
            try {
                int countMines = Integer.parseInt(responseTextField.getText());
                if (countMines >= rowNumber * columnNumber || countMines < 1) {
                    JOptionPane.showMessageDialog(this, "Невозможно установить " +
                            "такое количество " + "зомби на кладбище", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else {
                    return countMines;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите целое " +
                        "число больше нуля", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @return Если окно ввода было закрыто, то метод возвращает "-1"
     */
    private int showQuestion(String question, JTextField responseTextField) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(question);
        panel.add(label);
        panel.add(responseTextField);

        String[] options = {"OK"};

        int selectedOption = JOptionPane.showOptionDialog(null, panel,
                null, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selectedOption == JOptionPane.CLOSED_OPTION) {
            return -1;
        }

        return selectedOption;
    }
}
