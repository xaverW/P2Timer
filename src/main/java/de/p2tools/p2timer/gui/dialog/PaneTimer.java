/*
 * P2Tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.p2timer.gui.dialog;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2timer.controller.config.ProgConfig;
import de.p2tools.p2timer.controller.config.ProgConst;
import de.p2tools.p2timer.controller.worker.TimerFactory;
import de.p2tools.p2timer.gui.tools.P2TimeSpinnerHM;
import de.p2tools.p2timer.gui.tools.ProgListener;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.LocalTime;

public class PaneTimer extends VBox {
    private final RadioButton rbClock = new RadioButton();
    private final RadioButton rbCount = new RadioButton();
    private final RadioButton rbTimer = new RadioButton();
    private final Text textClock = new Text("Bis zur Uhrzeit zählen");
    private final Text textCounter = new Text("Aufwärts zählen");
    private final Text textTimer = new Text("Abwärts zählen");
    private P2TimeSpinnerHM spinnerHM;
    private final Label lbl = new Label("20 Min.");
    private ChangeListener<LocalTime> spinnerListener;
    private ProgListener timerProgListener;

    public PaneTimer() {
        initTimer();
    }

    public void close() {
        spinnerHM.valueProperty().removeListener(spinnerListener);
        ProgListener.removeListener(timerProgListener);
    }

    private void initTimer() {
        final ToggleGroup group = new ToggleGroup();
        rbClock.setToggleGroup(group);
        rbCount.setToggleGroup(group);
        rbTimer.setToggleGroup(group);
        rbClock.setPadding(new Insets(0, 10, 0, 0));

        rbClock.setSelected(ProgConfig.SYSTEM_COUNT.getValue() == ProgConst.COUNT_CLOCK);
        rbCount.setSelected(ProgConfig.SYSTEM_COUNT.getValue() == ProgConst.COUNT_UP);
        rbTimer.setSelected(ProgConfig.SYSTEM_COUNT.getValue() == ProgConst.COUNT_DOWN);
        rbClock.setOnAction(a -> setCounter());
        rbCount.setOnAction(a -> setCounter());
        rbTimer.setOnAction(a -> setCounter());

        textClock.setOnMouseClicked(e -> {
            rbClock.setSelected(true);
            setCounter();
        });
        textCounter.setOnMouseClicked(e -> {
            rbCount.setSelected(true);
            setCounter();
        });
        textTimer.setOnMouseClicked(e -> {
            rbTimer.setSelected(true);
            setCounter();
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        this.setPadding(new Insets(P2LibConst.PADDING_GRIDPANE));
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(), P2ColumnConstraints.getCcPrefSize());
        this.getChildren().add(gridPane);

        int row = 0;
        row = addClock(gridPane, row);
        row = addCounter(gridPane, row);
        addTimer(gridPane, row);

    }

    private void setCounter() {
        if (rbClock.isSelected()) {
            ProgConfig.SYSTEM_COUNT.setValue(ProgConst.COUNT_CLOCK);
        } else if (rbCount.isSelected()) {
            ProgConfig.SYSTEM_COUNT.setValue(ProgConst.COUNT_UP);
        } else {
            ProgConfig.SYSTEM_COUNT.setValue(ProgConst.COUNT_DOWN);
        }
    }

    private int addClock(GridPane gridPane, int row) {
        textClock.setFont(Font.font(null, FontWeight.BOLD, 15));
        textClock.setFill(ConfigDialogController.PROG_COLOR_MARK);

        LocalTime savedTime;
        try {
            savedTime = LocalTime.parse(ProgConfig.SYSTEM_COUNT_CLOCK_TIME.getValueSafe(), TimerFactory.formatter);
        } catch (Exception ex) {
            savedTime = LocalTime.now();
        }
        setTimerLabel();

        spinnerListener = (observable, oldValue, newValue) -> {
            ProgConfig.SYSTEM_COUNT_CLOCK_TIME.setValue(newValue.format(TimerFactory.formatter));
            setTimerLabel();
        };

        spinnerHM = new P2TimeSpinnerHM(savedTime);
        spinnerHM.valueProperty().addListener(spinnerListener);
        timerProgListener = new ProgListener(ProgListener.EREIGNIS_TIMER, PaneTimer.class.getSimpleName()) {
            // kommt alle Sekunde
            @Override
            public void pingFx() {
                setTimerLabel();
            }
        };
        ProgListener.addListener(timerProgListener);

        lbl.disableProperty().bind(rbClock.selectedProperty().not());
        spinnerHM.disableProperty().bind(rbClock.selectedProperty().not());

        gridPane.add(rbClock, 0, row);
        gridPane.add(textClock, 1, row);
        gridPane.add(spinnerHM, 1, ++row, 2, 1);
        gridPane.add(lbl, 1, ++row, 2, 1);
        GridPane.setHalignment(spinnerHM, HPos.RIGHT);
        GridPane.setHalignment(lbl, HPos.RIGHT);
        return ++row;
    }

    private void setTimerLabel() {
        lbl.setText("Dauer:   " + TimerFactory.getTimerText(
                TimerFactory.getClockSeconds(), true, true));
    }

    private int addCounter(GridPane gridPane, int row) {
        textCounter.setFont(Font.font(null, FontWeight.BOLD, 15));
        textCounter.setFill(ConfigDialogController.PROG_COLOR_MARK);

        gridPane.add(new Label(), 0, row);
        gridPane.add(rbCount, 0, ++row);
        gridPane.add(textCounter, 1, row);
        return ++row;
    }

    private void addTimer(GridPane gridPane, int row) {
        textTimer.setFont(Font.font(null, FontWeight.BOLD, 15));
        textTimer.setFill(ConfigDialogController.PROG_COLOR_MARK);

        // Zeit
        Slider slider = new Slider();
        Label lblSlider = new Label();
        slider.setMin(1);
        slider.setMax(150);
        slider.setShowTickLabels(true);
        slider.setMinorTickCount(10);
        slider.setMajorTickUnit(20);
        slider.setBlockIncrement(5);
        slider.setSnapToTicks(false);
        slider.setValue(ProgConfig.SYSTEM_COUNT_TIMER_MAX_VALUE_MINUTES.getValue());
        slider.valueProperty().bindBidirectional(ProgConfig.SYSTEM_COUNT_TIMER_MAX_VALUE_MINUTES);
        setValueSlider(slider, lblSlider);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            setValueSlider(slider, lblSlider);
        });

        StackPane st = new StackPane();
        st.setPadding(new Insets(0));
        st.setAlignment(Pos.CENTER_RIGHT);
        Label lbl = new Label("100  Minuten");
        lbl.setVisible(false);
        st.getChildren().addAll(lblSlider, lbl);

        HBox hBoxSlider = new HBox(25);
        hBoxSlider.getChildren().addAll(slider, st);
        HBox.setHgrow(slider, Priority.ALWAYS);

        // restart
        P2ToggleSwitch pToggleRestart = new P2ToggleSwitch("Timer sofort wieder starten");
        pToggleRestart.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_TIMER_AUTO_RESTART);

        // nur anzeigen, wenn nicht "aufwärts"
        hBoxSlider.disableProperty().bind(rbTimer.selectedProperty().not());
        pToggleRestart.disableProperty().bind(rbTimer.selectedProperty().not());

        gridPane.add(new Label(), 0, row);
        gridPane.add(rbTimer, 0, ++row);
        gridPane.add(textTimer, 1, row);
        GridPane.setHalignment(textTimer, HPos.LEFT);
        gridPane.add(hBoxSlider, 1, ++row, 2, 1);

        gridPane.add(new Label(" "), 1, ++row);
        gridPane.add(pToggleRestart, 1, ++row, 2, 1);
    }

    private void setValueSlider(Slider slider, Label lblSlider) {
        int sl = (int) slider.getValue();
        lblSlider.setText(sl + " Minuten");
    }
}
