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

package de.p2tools.p2timer.gui;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2timer.controller.ProgQuitFactory;
import de.p2tools.p2timer.controller.config.ProgData;
import de.p2tools.p2timer.controller.data.ProgIcons;
import de.p2tools.p2timer.gui.dialog.ConfigDialogController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TimerButton extends HBox {
    private final ProgData progData;

    public TimerButton() {
        progData = ProgData.getInstance();
        pack();
    }

    private void pack() {
        Button buttonStart = new Button();
        buttonStart.getStyleClass().add("timer-button");
        buttonStart.setGraphic(ProgIcons.ICON_T_BUTTON_START.getImageView());
        buttonStart.setOnAction(a -> progData.worker.startTimer());

        Button buttonStop = new Button();
        buttonStop.getStyleClass().add("timer-button");
        buttonStop.setGraphic(ProgIcons.ICON_T_BUTTON_STOP.getImageView());
        buttonStop.setOnAction(a -> progData.worker.stopTimer());

        Button buttonPause = new Button();
        buttonPause.getStyleClass().add("timer-button");
        buttonPause.setGraphic(ProgIcons.ICON_T_BUTTON_PAUSE.getImageView());
        buttonPause.setOnAction(a -> progData.worker.pauseTimer());
        progData.worker.timerIsPausedProperty().addListener((u) -> {
            if (progData.worker.timerIsPausedProperty().getValue()) {
                buttonPause.setGraphic(ProgIcons.ICON_T_BUTTON_RESTART.getImageView());
            } else {
                buttonPause.setGraphic(ProgIcons.ICON_T_BUTTON_PAUSE.getImageView());
            }
        });

        Button buttonConfig = new Button();
        buttonConfig.getStyleClass().add("timer-button");
        buttonConfig.setGraphic(ProgIcons.ICON_T_BUTTON_CONFIG.getImageView());
        buttonConfig.setOnAction(a -> {
            new ConfigDialogController(progData);
        });

        Button buttonQuitt = new Button();
        buttonQuitt.getStyleClass().add("timer-button");
        buttonQuitt.setGraphic(ProgIcons.ICON_T_BUTTON_QUITT.getImageView());
        buttonQuitt.setOnAction(a -> ProgQuitFactory.quit(true));

        setSpacing(P2LibConst.PADDING_HBOX);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(0));
        getStyleClass().add("timer-button-box");
        getChildren().addAll(buttonStart, buttonStop, buttonPause, P2GuiTools.getVDistance(10), buttonConfig, buttonQuitt);
        setVisible(false);
//        setStyle("-fx-border-color: red;");
    }
}
