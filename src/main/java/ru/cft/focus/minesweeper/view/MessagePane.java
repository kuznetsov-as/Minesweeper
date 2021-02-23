package ru.cft.focus.minesweeper.view;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
abstract class MessagePane extends JOptionPane {
    final IconRegistry iconRegistry = new IconRegistry();
}
