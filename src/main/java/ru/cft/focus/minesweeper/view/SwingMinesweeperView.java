package ru.cft.focus.minesweeper.view;

import lombok.extern.slf4j.Slf4j;
import ru.cft.focus.minesweeper.api.ButtonNames;
import ru.cft.focus.minesweeper.api.CellCode;
import ru.cft.focus.minesweeper.controller.MinesweeperController;
import ru.cft.focus.minesweeper.settings.DisplaySetting;
import ru.cft.focus.minesweeper.settings.GameSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SwingMinesweeperView extends JFrame implements MinesweeperView, ActionListener {

    private MinesweeperCell[][] cells;
    private GameSetting gameSetting = GameSetting.EASY;

    private JPanel cellPanel;
    private JPanel timerPanel;
    private JButton restartButton;
    private JPanel numberOfMinesPanel;
    private final JLabel timerLabel;
    private final JLabel numberOfMinesLabel;
    private final JTextArea textAreaWithRemainingMines = new JTextArea();
    private final JTextArea textAreaWithTimer = new JTextArea();

    private final IconRegistry iconRegistry = new IconRegistry();
    private final InterrogativeMessagePane interrogativeMessagePane = new InterrogativeMessagePane();
    private final NotificationMessagePane notificationMessagePane = new NotificationMessagePane();
    private final DemonstrationMessagePane demonstrationMessagePane = new DemonstrationMessagePane();
    private final MinesweeperController minesweeperController;

    public SwingMinesweeperView(MinesweeperController minesweeperController) {
        super("Minesweeper: zombie");
        this.minesweeperController = minesweeperController;
        this.timerLabel = new JLabel();
        this.numberOfMinesLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new FlowLayout());
        setBackground(Color.BLACK);

        initMenuBar();

        setVisible(true);
    }

    private void initMenuBar() {
        MenuHelper.initGameMenu(this);
    }

    private int calcWidth(int columnNumber) {
        return columnNumber * DisplaySetting.CELL_WIDTH + DisplaySetting.CELL_WIDTH;
    }

    private int calcHeight(int rowNumber) {
        return rowNumber * DisplaySetting.CELL_HEIGHT + DisplaySetting.GAME_INFO_PANEL_HEIGHT;
    }

    private void generateCell(int rowNumber, int columnNumber) {
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                MinesweeperCell cell = new MinesweeperCell(i, j);
                cellPanel.add(cell);
                cell.setOpaque(true);
                cell.setBackground(Color.GRAY);

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            minesweeperController.handleClickedLeftButtonOnCell(cell.getRow(), cell.getColumn());
                        }

                        if (e.getButton() == MouseEvent.BUTTON2) {
                            minesweeperController.handleClickedWheelButtonOnCell(cell.getRow(), cell.getColumn());
                        }

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            minesweeperController.handleClickedRightButtonOnCell(cell.getRow(), cell.getColumn());
                        }
                    }
                });

                cells[i][j] = cell;
            }
        }
    }

    private void setTimerPanel(int columnNumber) {
        timerPanel = new JPanel();
        timerPanel.setLayout(new FlowLayout());
        //Делим ширину на 3, т.к. помимо панели с таймером нужно место для панели с количеством мин и кнопки рестарта
        timerPanel.setPreferredSize(new Dimension((columnNumber * DisplaySetting.CELL_WIDTH) / 3,
                DisplaySetting.TIME_PANEL_HEIGHT));

        //Устанавливаем фон текстового поля в цвет окна игры
        textAreaWithTimer.setBackground(new Color(238, 238, 238));

        textAreaWithTimer.setEditable(false);
        textAreaWithTimer.setBorder(BorderFactory.createEmptyBorder());
        updateTimerPanel(0);
        timerPanel.add(textAreaWithTimer);
        timerPanel.add(timerLabel);
        add(timerPanel);
    }

    private void setRestartButton(int columnNumber) {
        restartButton = new JButton();
        restartButton.setLayout(new FlowLayout());
        restartButton.addActionListener(e -> minesweeperController.handleClickedRestartButton(gameSetting));

        Optional<ImageIcon> imageIconOpt = iconRegistry.getImageForCell("restart");
        if (imageIconOpt.isEmpty()) {
            notificationMessagePane.sayNoImageFound();
            return;
        }
        restartButton.setIcon(imageIconOpt.get());

        //Делим ширину на 3, т.к. помимо кнопки тут будет панель с таймером и панель с количеством мин
        restartButton.setSize(new Dimension((columnNumber * DisplaySetting.CELL_WIDTH) / 3,
                DisplaySetting.RESTART_BUTTON_HEIGHT));

        add(restartButton);
    }

    private void setNumberOfMinesPanel(int columnNumber) {
        numberOfMinesPanel = new JPanel();
        numberOfMinesPanel.setLayout(new FlowLayout());
        //Делим ширину на 3, т.к. помимо панели с количеством мин нужно место для панели с таймером и кнопки рестарта
        numberOfMinesPanel.setPreferredSize(new Dimension((columnNumber * DisplaySetting.CELL_WIDTH) / 3,
                DisplaySetting.NUMBER_OF_MINES_PANEL_HEIGHT));

        //Устанавливаем фон текстового поля в цвет окна игры
        textAreaWithRemainingMines.setBackground(new Color(238, 238, 238));
        numberOfMinesPanel.add(textAreaWithRemainingMines);
        textAreaWithRemainingMines.setEditable(false);
        numberOfMinesPanel.add(numberOfMinesLabel);
        add(numberOfMinesPanel);
    }

    private void setGameGridPanel(int rowNumber, int columnNumber) {
        cellPanel = new JPanel();
        cellPanel.setBackground(Color.BLACK);
        cellPanel.setLayout(new GridLayout(rowNumber, columnNumber, DisplaySetting.WIDTH_GAP,
                DisplaySetting.HEIGHT_GAP));
        add(cellPanel);
    }

    @Override
    public void updateTimerPanel(int seconds) {
        textAreaWithTimer.setText("Время: " + seconds + " сек.");
    }

    @Override
    public void updateNumberOfMinesPanel(int numberOfRemainingMines) {
        log.info("Получено оповещение об измении количества оставшихся на кладбище зомби");
        textAreaWithRemainingMines.setText("Зомби: " + numberOfRemainingMines);
    }

    @Override
    public void renderRestartGame(GameSetting gameSetting) {
        this.remove(cellPanel);
        this.remove(timerPanel);
        this.remove(restartButton);
        this.remove(numberOfMinesPanel);
        renderNewGame(gameSetting.getRowNumber(), gameSetting.getColumnNumber());

        setLocationRelativeTo(null);
    }

    @Override
    public void renderNewGame(int rowNumber, int columnNumber) {
        log.info("Получено оповещение о начале новой игры");
        cells = new MinesweeperCell[rowNumber][columnNumber];
        setSize(calcWidth(columnNumber), calcHeight(rowNumber));
        setGameGridPanel(rowNumber, columnNumber);
        generateCell(rowNumber, columnNumber);
        setTimerPanel(columnNumber);
        setRestartButton(columnNumber);
        setNumberOfMinesPanel(columnNumber);

        for (MinesweeperCell[] cellRow : cells) {
            for (MinesweeperCell cell : cellRow) {
                Optional<ImageIcon> imageIconOpt = iconRegistry.getImageForCell(CellCode.CLOSED);
                if (imageIconOpt.isEmpty()) {
                    notificationMessagePane.sayNoImageFound();
                    return;
                }
                cell.setIcon(imageIconOpt.get());
            }
        }

        setLocationRelativeTo(null);
    }

    @Override
    public void renderGameWon() {
        notificationMessagePane.sayYouWin();
    }

    @Override
    public void renderNewRecord() {
        if (gameSetting != GameSetting.CUSTOM) {
            String name = interrogativeMessagePane.askForName();
            //Если пользователь закрыл окно с вводом имени, то не сообщаем модели о новом рекорде
            if (name.equals("-1")) {
                return;
            }
            minesweeperController.handleRecordData(name, gameSetting);
        }
    }

    @Override
    public void showRecords(Map<String, Integer> recordsMap) {
        log.info("Получено оповещение о необходимости отобразить рекорды");
        demonstrationMessagePane.showRecords(recordsMap);
    }

    @Override
    public void showInformationAboutGameRules(String informationAboutGameRules) {
        log.info("Получено оповещение о необходимости отобразить правила игры");
        demonstrationMessagePane.showAbout(informationAboutGameRules);
    }

    @Override
    public void renderGameLose() {
        notificationMessagePane.sayYouLose();
    }

    @Override
    public void updateCell(int row, int column, String code) {
        log.info("Получено оповещение об изменении иконки ячейки");
        Optional<ImageIcon> imageIconOpt = iconRegistry.getImageForCell(code);
        if (imageIconOpt.isEmpty()) {
            notificationMessagePane.sayNoImageFound();
            return;
        }

        if (code.equals(CellCode.MINE)) {
            cells[row][column].setBackground(Color.RED);
        }
        cells[row][column].setIcon(imageIconOpt.get());
    }

    @Override
    public void updateCell(int row, int column, int neighbourMineCount) {
        updateCell(row, column, iconRegistry.getOpenedCellCodeMap().get(neighbourMineCount));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();

        switch (command) {
            case ButtonNames.GAME_WITH_EASY_SETTINGS:
                minesweeperController.handleClickedRestartButton(GameSetting.EASY);
                gameSetting = GameSetting.EASY;
                break;
            case ButtonNames.GAME_WITH_MEDIUM_SETTINGS:
                minesweeperController.handleClickedRestartButton(GameSetting.MEDIUM);
                gameSetting = GameSetting.MEDIUM;
                break;
            case ButtonNames.GAME_WITH_HARD_SETTINGS:
                minesweeperController.handleClickedRestartButton(GameSetting.HARD);
                gameSetting = GameSetting.HARD;
                break;
            case ButtonNames.GAME_WITH_CUSTOM_SETTINGS:
                int rowNumber = interrogativeMessagePane.askForTheCountOfRows();
                //-1 возвращается, если пользователь закрыл окно ввода
                if (rowNumber == -1) {
                    break;
                }
                GameSetting.CUSTOM.setRowNumber(rowNumber);

                int columnNumber = interrogativeMessagePane.askForTheCountOfColumns();
                //-1 возвращается, если пользователь закрыл окно ввода
                if (columnNumber == -1) {
                    break;
                }
                GameSetting.CUSTOM.setColumnNumber(columnNumber);

                int countMines = interrogativeMessagePane.askForTheCountOfMines(rowNumber, columnNumber);
                //-1 возвращается, если пользователь закрыл окно ввода
                if (countMines == -1) {
                    break;
                }
                GameSetting.CUSTOM.setCountMines(countMines);

                minesweeperController.handleClickedRestartButton(GameSetting.CUSTOM);
                gameSetting = GameSetting.CUSTOM;
                break;
            case ButtonNames.ABOUT:
                log.info("Произведен запрос правил игры");
                minesweeperController.handleAboutDisplayRequest();
                break;
            case ButtonNames.EXIT:
                log.info("Пользователь вышел из игры");
                minesweeperController.exit();
                break;
            case ButtonNames.EASY_RECORDS:
                log.info("Произведен запрос рекордов для настроек: " + GameSetting.EASY);
                minesweeperController.handleRecordDisplayRequest(GameSetting.EASY);
                break;
            case ButtonNames.MEDIUM_RECORDS:
                log.info("Произведен запрос рекордов для настроек: " + GameSetting.MEDIUM);
                minesweeperController.handleRecordDisplayRequest(GameSetting.MEDIUM);
                break;
            case ButtonNames.HARD_RECORDS:
                log.info("Произведен запрос рекордов для настроек: " + GameSetting.HARD);
                minesweeperController.handleRecordDisplayRequest(GameSetting.HARD);
                break;
        }
    }
}
