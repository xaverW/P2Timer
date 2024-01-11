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

package de.p2tools.p2timer;

import de.p2tools.p2timer.gui.TimerButton;
import de.p2tools.p2timer.gui.TimerGui;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class P2TimerController extends AnchorPane {
    private final TimerGui timerGui = new TimerGui();
    private final TimerButton timerButton = new TimerButton();

    public P2TimerController() {
        init();
    }

    private void init() {
        // Gui zusammenbauen
        StackPane stackPane = new StackPane();

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);

        VBox vBox = new VBox(0);
        vBox.getChildren().add(timerButton);
        vBox.setAlignment(Pos.CENTER);
        stackPane.getChildren().addAll(timerGui, vBox);

        setOnMouseEntered(mouseEvent -> {
            timerButton.setVisible(true);
        });
        setOnMouseExited(mouseEvent -> {
            timerButton.setVisible(false);
        });
    }

    public TimerGui getTimerGui() {
        return timerGui;
    }

    public TimerButton getTimerButton() {
        return timerButton;
    }
}
