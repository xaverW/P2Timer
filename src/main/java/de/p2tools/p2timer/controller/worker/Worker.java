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

package de.p2tools.p2timer.controller.worker;

import de.p2tools.p2timer.controller.config.ProgConfig;
import de.p2tools.p2timer.controller.config.ProgConst;
import de.p2tools.p2timer.controller.config.ProgData;
import de.p2tools.p2timer.gui.tools.ProgListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Worker extends Thread {
    private final ProgData progData;
    private final IntegerProperty timerActValue = new SimpleIntegerProperty(0); // arbeitet mit Secunden!!
    private final BooleanProperty timerIsRunning = new SimpleBooleanProperty(false); // immer true, wenn der Timer läuft
    private final BooleanProperty timerIsPaused = new SimpleBooleanProperty(false); // immer true, wenn der Timer pausiert
    private boolean blinkText = false;

    public Worker(ProgData progData) {
        this.progData = progData;
        initTimer();
    }

    public BooleanProperty timerIsPausedProperty() {
        return timerIsPaused;
    }

    private void initTimer() {
        setTimerText();

        timerActValue.addListener((v, o, n) -> setTimerText());
        ProgListener.addListener(new ProgListener(ProgListener.EREIGNIS_TIMER, Worker.class.getSimpleName()) {
            // kommt alle Sekunde
            @Override
            public void pingFx() {
                if (!timerIsRunning.getValue() || timerIsPaused.getValue()) {
                    // dann läuft er nicht oder ist pausiert
                    blinkTimerText(); // zum evtl. ausschalten
                    return;
                }

                if (ProgConfig.SYSTEM_COUNT.getValue() == ProgConst.COUNT_CLOCK) {
                    // dann bis zur Uhrzeit zählen
                    if (timerActValue.getValue() > 0) {
                        timerActValue.setValue(timerActValue.getValue() - 1);
                    }
                    if (timerActValue.getValue() == 0) {
                        stopTimer();
                        TimerSoundFactory.playSound();
                    }

                } else if (ProgConfig.SYSTEM_COUNT.getValue() == ProgConst.COUNT_UP) {
                    // dann wird aufwärts gezählt
                    timerActValue.setValue(timerActValue.getValue() + 1);

                } else {
                    if (timerActValue.getValue() > 0) {
                        timerActValue.setValue(timerActValue.getValue() - 1);
                    }
                    if (timerActValue.getValue() == 0) {
                        if (ProgConfig.SYSTEM_TIMER_AUTO_RESTART.getValue()) {
                            startTimer();
                        } else {
                            stopTimer();
                        }
                        TimerSoundFactory.playSound();
                    }
                }
            }
        });
        ProgConfig.SYSTEM_COUNT_TIMER_MAX_VALUE_MINUTES.addListener((u) -> {
            initTimerValue();
        });
        ProgConfig.SYSTEM_COUNT_CLOCK_TIME.addListener((u) -> {
            initTimerValue();
        });
        ProgConfig.SYSTEM_COUNT.addListener(u -> {
            stopTimer();
            initTimerValue();
        });
    }

    private void initTimerValue() {
        if (ProgConfig.SYSTEM_COUNT.getValue() == ProgConst.COUNT_CLOCK) {
            // dann Startzeit der Uhr
            timerActValue.setValue(TimerFactory.getClockSeconds());

        } else if (ProgConfig.SYSTEM_COUNT.getValue() == ProgConst.COUNT_UP) {
            timerActValue.setValue(0);

        } else {
            if (ProgConfig.SYSTEM_COUNT_TIMER_MAX_VALUE_MINUTES.getValue() == 0) {
                ProgConfig.SYSTEM_COUNT_TIMER_MAX_VALUE_MINUTES.setValue(1); // min. 1 Minute
            }
            timerActValue.setValue(ProgConfig.SYSTEM_COUNT_TIMER_MAX_VALUE_MINUTES.getValue() * 60);
        }
    }

    private void blinkTimerText() {
        if (timerIsPaused.getValue()) {
            if (blinkText) {
                progData.lblTimer.setStyle("-fx-text-fill: #909090;");
            } else {
                progData.lblTimer.setStyle("-fx-text-fill: white;");
            }
            blinkText = !blinkText;
        } else {
            progData.lblTimer.setStyle("-fx-text-fill: white;");
        }
    }

    private void setTimerText() {
        blinkTimerText();
        progData.lblTimer.setText(TimerFactory.getTimerText(timerActValue.getValue(), false, true));
    }

    public void startTimer() {
        initTimerValue();
        timerIsPaused.setValue(false);
        timerIsRunning.setValue(true);
    }

    public void stopTimer() {
        initTimerValue();
        timerIsPaused.setValue(false);
        timerIsRunning.setValue(false);
    }

    public void pauseTimer() {
        if (!timerIsRunning.getValue()) {
            // dann bleibts
            return;
        }
        // da wird der Timer pausiert/wieder gestartet
        timerIsPaused.setValue(!timerIsPaused.get());
        if (ProgConfig.SYSTEM_COUNT.getValue() == ProgConst.COUNT_CLOCK) {
            // dann die Zeit neu setzen
            timerActValue.setValue(TimerFactory.getClockSeconds());
        }
    }
}