package ru.cft.focus.minesweeper.view;

import ru.cft.focus.minesweeper.api.ButtonNames;

import javax.swing.*;

class MenuHelper {

    static void initGameMenu(SwingMinesweeperView view) {


        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu(ButtonNames.GAME);

        JMenuItem newEasyGame = new JMenuItem(ButtonNames.GAME_WITH_EASY_SETTINGS);
        newEasyGame.addActionListener(view);
        menuGame.add(newEasyGame);

        JMenuItem newMediumGame = new JMenuItem(ButtonNames.GAME_WITH_MEDIUM_SETTINGS);
        newMediumGame.addActionListener(view);
        menuGame.add(newMediumGame);

        JMenuItem newHardGame = new JMenuItem(ButtonNames.GAME_WITH_HARD_SETTINGS);
        newHardGame.addActionListener(view);
        menuGame.add(newHardGame);

        JMenuItem newCustomSettingsGame = new JMenuItem(ButtonNames.GAME_WITH_CUSTOM_SETTINGS);
        newCustomSettingsGame.addActionListener(view);
        menuGame.add(newCustomSettingsGame);

        menuGame.addSeparator();

        JMenuItem about = new JMenuItem(ButtonNames.ABOUT);
        about.addActionListener(view);
        menuGame.add(about);

        JMenuItem exitGame = new JMenuItem(ButtonNames.EXIT);
        exitGame.addActionListener(view);
        menuGame.add(exitGame);

        JMenu menuRecords = new JMenu(ButtonNames.RECORDS);

        JMenuItem easyRecords = new JMenuItem(ButtonNames.EASY_RECORDS);
        easyRecords.addActionListener(view);
        menuRecords.add(easyRecords);

        JMenuItem mediumRecords = new JMenuItem(ButtonNames.MEDIUM_RECORDS);
        mediumRecords.addActionListener(view);
        menuRecords.add(mediumRecords);

        JMenuItem hardRecords = new JMenuItem(ButtonNames.HARD_RECORDS);
        hardRecords.addActionListener(view);
        menuRecords.add(hardRecords);

        menuBar.add(menuGame);
        menuBar.add(menuRecords);
        menuBar.setOpaque(true);
        view.setJMenuBar(menuBar);
    }
}
