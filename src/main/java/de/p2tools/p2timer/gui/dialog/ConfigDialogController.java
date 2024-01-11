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

package de.p2tools.p2timer.gui.dialog;


import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2timer.controller.config.ProgConfig;
import de.p2tools.p2timer.controller.config.ProgData;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;

public class ConfigDialogController extends PDialogExtra {

    final ProgData progData;
    private Button btnOk = new Button("_Ok");
    private TabPane tabPane = new TabPane();
    public static final Color PROG_COLOR_MARK = Color.rgb(244, 244, 255);
    PaneTimer paneTimer;
    PaneSound paneSound;

    public ConfigDialogController(ProgData progData) {
        super(progData.primaryStage, null, "Timer ändern",
                true, true, DECO.NO_BORDER);

        this.progData = progData;
        init(true);
    }

    public void close() {
        paneTimer.close();
        super.close();
    }

    @Override
    public void make() {
        final Button btnHelp = P2Button.helpButton(getStage(), "Timer Einstellungen",
                "** Timer **" +
                        "\n" +
                        "Hier kann eine Uhrzeit vorgegeben werden, dann zählt der " +
                        "Timer bis zu der Uhrzeit zurück." +
                        "\n\n" +
                        "Aufwärts zählen: Dann zählt der Timer von 0 endlos aufwärts." +
                        "\n\n" +
                        "Abwärts zählen macht genau das. Es wird die eingestellte " +
                        "Zeit abwärts bis 0 gezählt. " +
                        "Soll abwärts gezählt werden, gibt es dafür noch diese Optionen:" +
                        "\n" +
                        "Wird die 0 erreicht, kann der Timer wieder mit der eingestellten " +
                        "Zeit neu gestartet werden." +
                        "\n\n" +
                        "** Sound **" +
                        "\n" +
                        "Hier kann eingestellt werden, ob beim Erreichen der 0 ein Soundclip " +
                        "abgespielt werden soll oder nicht. Auch kann ausgewählt werden, " +
                        "welcher Clip abgespielt wird.");

        btnOk.setOnAction(a -> {
            progData.primaryStage.getScene().getWindow().sizeToScene(); // vorsichtshalber
            close();
        });
        Button btnUeber = new Button("Über");
        btnUeber.setOnAction(a -> new AboutDialogController(progData).showDialog());

        addOkButton(btnOk);
//        addHlpButton(btnHelp);
        getHboxLeft().getChildren().addAll(btnUeber, btnHelp);

        paneTimer = new PaneTimer();
        paneSound = new PaneSound();

        Tab tabTimer = new Tab("Timer");
        tabTimer.setClosable(false);
        tabTimer.setContent(paneTimer);

        Tab tabSound = new Tab("Sound");
        tabSound.setClosable(false);
        tabSound.setContent(paneSound);

        tabPane.getTabs().addAll(tabTimer, tabSound);

        getVBoxCont().getChildren().addAll(tabPane);
        ProgConfig.SYSTEM_THEME_CHANGED.addListener((u, o, n) -> updateCss());
    }
}