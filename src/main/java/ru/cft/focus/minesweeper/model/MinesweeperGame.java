package ru.cft.focus.minesweeper.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.cft.focus.minesweeper.settings.GameSetting;
import ru.cft.focus.minesweeper.view.MinesweeperView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class MinesweeperGame {

    private int rowNumber;
    private int columnNumber;
    private int countFlags;
    private int countMines;
    private int countClosedCells;
    private boolean isGameStopped;
    private boolean isFirstOpened;
    private GameCell[][] gameCells;

    @Getter
    private final MinesweeperViewNotifier viewNotifier = new MinesweeperViewNotifier();
    private final Timer timer = new Timer(getViewNotifier());

    public MinesweeperGame(int rowNumber, int columnNumber, int countMines) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.countMines = countMines;
        this.countClosedCells = this.rowNumber * this.columnNumber;
        this.gameCells = new GameCell[this.rowNumber][this.columnNumber];
        this.countFlags = this.countMines;
        this.isFirstOpened = true;
    }

    private void initializeGameCells(int firstOpenCellRow, int firstOpenCellColumn) {
        log.info("Производится инициализация ячеек на поле");
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                gameCells[i][j] = new GameCell(i, j, false);
            }
        }

        int installedMines = 0;
        while (installedMines != countMines) {
            int row = (int) (Math.random() * rowNumber);
            int column = (int) (Math.random() * columnNumber);

            // Если рандомная мина оказывается в первой ячейке, на которую кликнул пользователь,
            // или рандомная мина несколько раз попала в одну ячейку, то подбираем другие координаты
            if (gameCells[row][column].isMine() || (firstOpenCellRow == row && firstOpenCellColumn == column)) {
                continue;
            }

            gameCells[row][column] = new GameCell(row, column, true);
            log.info("В ячейке под координатами {} : {} теперь стоит мина", row, column);
            installedMines++;
        }

        log.info("Все мины расставлены на поле");

        countMineNeighbors();
    }

    public void startNewGame(GameSetting gameSetting) {
        viewNotifier.notifyViewsNewGame(gameSetting.getRowNumber(), gameSetting.getColumnNumber());
        viewNotifier.notifyViewAboutNumberOfMinesUpdate(countFlags);
    }

    public void attachView(MinesweeperView minesweeperView) {
        viewNotifier.attachView(minesweeperView);
    }

    public void markCell(int row, int column) {
        if (isFirstOpened) {
            return;
        }

        if (isGameStopped) {
            return;
        }

        if (row >= rowNumber || column >= columnNumber) {
            return;
        }

        if (gameCells[row][column].isOpen()) {
            return;
        }

        if (gameCells[row][column].isFlag()) {
            gameCells[row][column].setFlag(false);
            countFlags++;
            log.info("Пользователь поставил вопрос для ячейки под координатами {}:{}", row, column);
            gameCells[row][column].setQuestion(true);
            viewNotifier.notifyViewAboutCellStatusChanged(row, column, CellType.QUESTION);
        } else if (gameCells[row][column].isQuestion()) {
            gameCells[row][column].setQuestion(false);
            log.info("Пользователь убрал вопрос с ячейки под координатами {}:{}", row, column);
            viewNotifier.notifyViewAboutCellStatusChanged(row, column, CellType.CLOSED);
        } else {
            gameCells[row][column].setFlag(true);
            countFlags--;
            log.info("Пользователь поставил флаг для ячейки под координатами {}:{}", row, column);
            viewNotifier.notifyViewAboutCellStatusChanged(row, column, CellType.FLAG);
        }

        viewNotifier.notifyViewAboutNumberOfMinesUpdate(countFlags);
    }

    public void openCellNeighbors(int row, int column) {
        if (isFirstOpened) {
            return;
        }

        if (gameCells[row][column].isOpen() && !gameCells[row][column].isMine() && !gameCells[row][column].isFlag()) {
            List<GameCell> neighbors = getNeighbors(gameCells[row][column]);
            int countOfNeighborsWithAFlag = 0;
            for (GameCell neighbor : neighbors) {
                if (neighbor.isFlag()) {
                    countOfNeighborsWithAFlag++;
                }
            }

            if (gameCells[row][column].getNeighbourMineCount() == countOfNeighborsWithAFlag) {
                for (GameCell neighbor : neighbors) {
                    openCell(neighbor.getRow(), neighbor.getColumn());
                }
            }
        }
    }

    public void openCell(int row, int column) {
        if (isFirstOpened) {
            initializeGameCells(row, column);
            timer.startTimer();
            isFirstOpened = false;
        }

        log.info("Была открыта ячейка с координатами {}:{}", row, column);

        if (gameCells[row][column].isFlag() ||
                gameCells[row][column].isQuestion() ||
                isGameStopped ||
                gameCells[row][column].isOpen()) {
            return;
        }

        if (gameCells[row][column].isMine()) {
            viewNotifier.notifyViewAboutCellStatusChanged(row, column, CellType.MINE);

            //Открываем все мины на поле
            for (GameCell[] cellRow : gameCells) {
                for (GameCell cell : cellRow) {
                    if (cell.isMine()) {
                        viewNotifier.notifyViewAboutCellStatusChanged(cell.getRow(), cell.getColumn(), CellType.MINE);
                    }
                }
            }

            realizeLose();

            gameCells[row][column].setOpen(true);
            return;
        } else {
            countClosedCells--;
            gameCells[row][column].setOpen(true);
            if (gameCells[row][column].getNeighbourMineCount() != 0) {
                viewNotifier.notifyViewAboutCellStatusChanged(row, column, gameCells[row][column].getNeighbourMineCount());
            } else {
                viewNotifier.notifyViewAboutCellStatusChanged(row, column, CellType.OPENED);
                List<GameCell> neighbors = getNeighbors(gameCells[row][column]);
                for (GameCell cell : neighbors) {
                    if (!cell.isOpen() && !cell.isMine()) {
                        openCell(cell.getRow(), cell.getColumn());
                    }
                }
            }
        }

        if (countClosedCells == countMines && !isGameStopped) {
            realizeWin();
        }
    }

    public void restartGame(GameSetting gameSetting) {
        timer.stopTimer();
        log.info("Пользователь запустил новую игру с настройками: " + gameSetting.name());
        isGameStopped = false;
        rowNumber = gameSetting.getRowNumber();
        columnNumber = gameSetting.getColumnNumber();
        countMines = gameSetting.getCountMines();
        countClosedCells = rowNumber * columnNumber;
        gameCells = new GameCell[rowNumber][columnNumber];
        countFlags = this.countMines;
        isFirstOpened = true;
        viewNotifier.notifyViewsRestartGame(gameSetting);
        viewNotifier.notifyViewAboutNumberOfMinesUpdate(countFlags);
    }

    public void createRecordsMap(GameSetting gameSetting) {
        Record[] records = getRecordsObjects(gameSetting);

        Map<String, Integer> recordsMap = new LinkedHashMap<>();

        for (Record record : records) {
            recordsMap.put(record.getName(), record.getTime());
        }

        viewNotifier.notifyViewShowRecords(recordsMap);
    }

    public void createStringWithInformationAboutGame() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File about = new File((Objects.requireNonNull(classLoader.getResource("about/about.txt"))).getFile());

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(about), StandardCharsets.UTF_8))) {

            StringBuilder informationAboutGame = new StringBuilder();
            String textFieldReadable = bufferedReader.readLine();

            while (textFieldReadable != null) {
                informationAboutGame.append(textFieldReadable);
                informationAboutGame.append(System.lineSeparator());
                textFieldReadable = bufferedReader.readLine();
            }
            viewNotifier.notifyViewAboutGameRules(informationAboutGame.toString());
        } catch (FileNotFoundException e) {
            log.info("Не удалось найти файл с правилами игры", e);
            viewNotifier.notifyViewAboutGameRules("Не удалось найти файл с правилами игры");
        } catch (IOException e) {
            log.info("Не удалось получить данные из файла с правилами игры", e);
            viewNotifier.notifyViewAboutGameRules("Не удалось получить данные из файла с правилами игры");
        }
    }

    private Record[] getRecordsObjects(GameSetting gameSetting) {
        Record[] records = new Record[0];
        File fileWithRecords = defineRecordsFile(gameSetting);

        try (FileInputStream fileInputStream = new FileInputStream(fileWithRecords);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            records = (Record[]) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            log.error("При попытки чтения рекордов не удалось обнаружить файл с информацией о них", e);
        } catch (IOException e) {
            log.error("Не удалось прочесть данные рекордов", e);
        } catch (ClassNotFoundException e) {
            log.error("Не удалось найти подходящий для приведения класс", e);
        }

        return records;
    }

    public void createNewRecord(String name, GameSetting gameSetting) {
        addRecord(new Record(name, timer.getTime()), gameSetting);
    }

    private void addRecord(Record newRecord, GameSetting gameSetting) {
        log.info("Производится добавление нового рекорда");
        Record[] oldRecords = getRecordsObjects(gameSetting);
        Record[] newRecords = new Record[oldRecords.length + 1];
        for (int i = 0; i < newRecords.length; i++) {
            if (i == newRecords.length - 1 || oldRecords.length == 0) {
                newRecords[i] = newRecord;
            } else {
                newRecords[i] = oldRecords[i];
            }
        }

        Arrays.sort(newRecords);

        File fileWithRecords = defineRecordsFile(gameSetting);

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileWithRecords);
             ObjectOutputStream objectInputStream = new ObjectOutputStream(fileOutputStream)) {
            objectInputStream.writeObject(newRecords);
        } catch (FileNotFoundException e) {
            log.error("При добавлении нового рекорда не удалось обнаружить файл с информацией о них", e);
        } catch (IOException e) {
            log.error("Не удалось записать данные нового рекорда", e);
        }
    }

    private File defineRecordsFile(GameSetting gameSetting) {
        switch (gameSetting) {
            case EASY:
                return new File("easy_records.bin");
            case MEDIUM:
                return new File("medium_records.bin");
            case HARD:
                return new File("hard_records.bin");
            default:
                log.error("Не удалось определить файл с данными о рекордах");
                throw new IllegalArgumentException();
        }
    }

    private void countMineNeighbors() {
        for (GameCell[] cellRow : gameCells) {
            for (GameCell cell : cellRow) {
                if (!cell.isMine()) {
                    List<GameCell> neighbors = getNeighbors(cell);
                    for (GameCell neighbor : neighbors) {
                        if (neighbor.isMine()) {
                            cell.setNeighbourMineCount(cell.getNeighbourMineCount() + 1);
                        }
                    }
                }
            }
        }
    }

    private List<GameCell> getNeighbors(GameCell cell) {
        List<GameCell> result = new ArrayList<>();
        for (int row = cell.getRow() - 1; row <= cell.getRow() + 1; row++) {
            for (int column = cell.getColumn() - 1; column <= cell.getColumn() + 1; column++) {
                if (row < 0 || row >= rowNumber) {
                    continue;
                }
                if (column < 0 || column >= columnNumber) {
                    continue;
                }
                if (gameCells[row][column] == cell) {
                    continue;
                }
                result.add(gameCells[row][column]);
            }
        }
        return result;
    }

    private void realizeLose() {
        timer.stopTimer();
        log.info("Игра проиграна");
        isGameStopped = true;
        viewNotifier.notifyViewGameLose();
    }

    private void realizeWin() {
        timer.stopTimer();
        log.info("Игра выиграна");
        viewNotifier.notifyViewGameWon();
        isGameStopped = true;
        viewNotifier.notifyViewAboutNewRecord();
    }
}