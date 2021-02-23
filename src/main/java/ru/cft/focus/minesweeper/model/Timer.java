package ru.cft.focus.minesweeper.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Timer implements Runnable {

    private int seconds = 0;
    private final MinesweeperViewNotifier viewNotifier;
    private final ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;

    Timer(MinesweeperViewNotifier viewNotifier) {
        this.viewNotifier = viewNotifier;
    }

    void startTimer() {
        seconds = 0;
        scheduledFuture = timer.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
        log.info("Таймер запущен");
    }

    void stopTimer() {
        scheduledFuture.cancel(false);
        log.info("Таймер остановлен");
    }

    int getTime() {
        return seconds;
    }

    @Override
    public void run() {
        seconds++;
        viewNotifier.notifyViewAboutTimerUpdate(seconds);
    }
}
