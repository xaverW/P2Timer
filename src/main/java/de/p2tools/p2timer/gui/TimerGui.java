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
import de.p2tools.p2timer.controller.config.ProgData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TimerGui extends VBox {
    private final ProgData progData;

    public TimerGui() {
        progData = ProgData.getInstance();
        pack();
    }

    private void pack() {
        setSpacing(P2LibConst.PADDING_VBOX);
        setPadding(new Insets(0));

        progData.lblTimer.getStyleClass().add("timer-text");
        HBox hBoxLbl = new HBox();
        hBoxLbl.setAlignment(Pos.CENTER);
        hBoxLbl.getChildren().add(progData.lblTimer);
        HBox.setHgrow(progData.lblTimer, Priority.ALWAYS);
        VBox.setVgrow(hBoxLbl, Priority.ALWAYS);
//        setStyle("-fx-border-color: green;");

        this.getChildren().addAll(hBoxLbl);
    }
}
