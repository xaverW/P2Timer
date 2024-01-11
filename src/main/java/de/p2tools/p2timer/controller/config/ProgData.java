/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */


package de.p2tools.p2timer.controller.config;

import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2timer.P2TimerController;
import de.p2tools.p2timer.controller.data.P2TimerShortCuts;
import de.p2tools.p2timer.controller.worker.Worker;
import de.p2tools.p2timer.gui.tools.ProgListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProgData {
    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean duration = false; // Duration ausgeben
    // Infos
    public static String configDir = ""; // Verzeichnis zum Speichern der Programmeinstellungen
    private static ProgData instance;
    // zentrale Klassen
    public P2TimerShortCuts pShortcut; // verwendete Shortcuts
    // Gui
    public Stage primaryStage = null;
    public P2TimerController p2TimerController = null;
    public final Label lblTimer = new Label();

    // Worker
    public Worker worker;

    // Programmdaten
    boolean oneSecond = false;

    private ProgData() {
        pShortcut = new P2TimerShortCuts();
        worker = new Worker(this);
    }

    public synchronized static final ProgData getInstance(String dir) {
        if (!dir.isEmpty()) {
            configDir = dir;
        }
        return getInstance();
    }

    public synchronized static final ProgData getInstance() {
        return instance == null ? instance = new ProgData() : instance;
    }

    public void initProgData() {
        startTimer();
    }

    private void startTimer() {
        // extra starten, damit er im Einrichtungsdialog nicht dazwischen funkt
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), ae -> {
            oneSecond = !oneSecond;
            if (oneSecond) {
                doTimerWorkOneSecond();
            }
            doTimerWorkHalfSecond();

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setDelay(Duration.seconds(5));
        timeline.play();
        PDuration.onlyPing("Timer gestartet");
    }

    private void doTimerWorkOneSecond() {
        ProgListener.notify(ProgListener.EREIGNIS_TIMER, ProgData.class.getName());
    }

    private void doTimerWorkHalfSecond() {
        ProgListener.notify(ProgListener.EREIGNIS_TIMER_HALF_SECOND, ProgData.class.getName());
    }
}
