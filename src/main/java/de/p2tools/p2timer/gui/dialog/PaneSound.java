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
import de.p2tools.p2timer.controller.worker.TimerSoundFactory;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PaneSound extends VBox {

    public PaneSound() {
        initTimer();
    }

    private void initTimer() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        this.setPadding(new Insets(P2LibConst.PADDING_GRIDPANE));
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(), P2ColumnConstraints.getCcPrefSize());
        this.getChildren().add(gridPane);

        addTimer(gridPane);

    }

    private void addTimer(GridPane gridPane) {
        Text textSound = new Text("Musik abspielen");
        textSound.setFont(Font.font(null, FontWeight.BOLD, 15));
        textSound.setFill(ConfigDialogController.PROG_COLOR_MARK);

        // Sound
        P2ToggleSwitch tglNoSound = new P2ToggleSwitch("Einen Sound abspielen");
        tglNoSound.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_PLAY_SOUND);

        ComboBox<TimerSoundFactory.SOUND> cboSound = new ComboBox<>();
        cboSound.getItems().addAll(TimerSoundFactory.SOUND.values());
        cboSound.getSelectionModel().select(TimerSoundFactory.SOUND.getSound(ProgConfig.SYSTEM_TIMER_SOUND.getValue()));
        cboSound.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            ProgConfig.SYSTEM_TIMER_SOUND.setValue(cboSound.getSelectionModel().getSelectedItem().getPath());
        });
        cboSound.setMaxWidth(Double.MAX_VALUE);

        Button btnTest = new Button("Test");
        btnTest.setOnAction(a -> TimerSoundFactory.playSound());

        cboSound.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not());
        btnTest.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not());

        HBox hBoxSound = new HBox(20);
        hBoxSound.getChildren().addAll(cboSound, btnTest);
        HBox.setHgrow(cboSound, Priority.ALWAYS);

        // nur anzeigen, wenn nicht "aufwärts"
        int row = 0;
        gridPane.add(textSound, 1, row);
        GridPane.setHalignment(textSound, HPos.LEFT);

        gridPane.add(tglNoSound, 1, ++row, 2, 1);
        gridPane.add(new Label(" "), 1, ++row);
        gridPane.add(new Label("Sound:"), 1, ++row);
        gridPane.add(hBoxSound, 2, row);
    }
}
