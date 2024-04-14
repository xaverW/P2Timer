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
import de.p2tools.p2lib.dialogs.P2DirFileChooser;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2timer.controller.config.ProgConfig;
import de.p2tools.p2timer.controller.data.ProgIcons;
import de.p2tools.p2timer.controller.worker.TimerFactory;
import de.p2tools.p2timer.controller.worker.TimerSoundFactory;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PaneSound extends VBox {

    private final Stage stage;
    private int row = 0;
    private final GridPane gridPane = new GridPane();

    public PaneSound(Stage stage) {
        this.stage = stage;
        initTimer();

    }

    private void initTimer() {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        this.setPadding(new Insets(P2LibConst.PADDING_GRIDPANE));
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(), P2ColumnConstraints.getCcPrefSize());
        this.getChildren().add(gridPane);

        addTimer();
        addOwnFile();
        addOwnPlayer();
        addTest();
    }

    private void addTimer() {
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
        cboSound.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not());

        Label lblSound = new Label("Sound:");
        lblSound.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not());

        // nur anzeigen, wenn nicht "aufwärts"
        gridPane.add(textSound, 0, row, 2, 1);
        GridPane.setHalignment(textSound, HPos.LEFT);

        gridPane.add(new Label(""), 0, ++row);
        gridPane.add(tglNoSound, 0, ++row, 3, 1);
        ++row;
        gridPane.add(lblSound, 0, ++row);
        gridPane.add(cboSound, 1, row, 2, 1);
    }

    private void addOwnFile() {
        P2ToggleSwitch tglOwnSound = new P2ToggleSwitch("Eigenen Sound abspielen");
        tglOwnSound.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_PLAY_OWN_SOUND);
        tglOwnSound.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not());

        TextField txtOwn = new TextField();
        txtOwn.textProperty().bindBidirectional(ProgConfig.SYSTEM_OWN_SOUND_FILE);
        txtOwn.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not().or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not()));

        final Button btnFile = new Button();
        btnFile.setGraphic(ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Eine Sounddatei zum Abspielen auswählen"));
        btnFile.setOnAction(event -> P2DirFileChooser.FileChooserOpenFile(stage, txtOwn));
        btnFile.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not().or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not()));

        Label lblFile = new Label("Datei:");
        lblFile.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not().or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not()));

        gridPane.add(new Label(""), 0, ++row);

        gridPane.add(tglOwnSound, 0, ++row, 3, 1);
        ++row;
        gridPane.add(lblFile, 0, ++row);
        gridPane.add(txtOwn, 1, row);
        gridPane.add(btnFile, 2, row);
    }

    private void addOwnPlayer() {
        final Button btnHelp = P2Button.helpButton(stage, "Player Einstellungen",
                "Hier kann ein eigener Player eingerichtet werden. Die Vorgabe ist der VLC-Player " +
                        "Mit dem Button darunter (Zwei Pfeile) wird die Vorgabe wieder " +
                        "hergestellt." +
                        "\n\n" +
                        "Programmdatei: Hier steht der Pfad zum Programm.\n" +
                        "Parameter: Hier werden die Parameter die das Programm braucht angegeben. Zwischen den " +
                        "einzelnen Parameter müssen die Zeichen: \"<>\" stehen. Der Parameter \"%f\" wird durch " +
                        "den Pfad zur Sounddatei ersetzt." +
                        "\n\n" +
                        "Der Aufruf des VLC könnte also z.B. so aussehen:" +
                        "\n\n" +
                        "Programmdatei (Linux): /usr/bin/vlc\n" +
                        "Programmdatei (Windows): C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\n" +
                        "Parameter: %f<>--play-and-exit<>-I<>dummy\n" +
                        "\n" +
                        "Theoretisch kann aber jeder Programmaufruf im Feld \"Programmdatei\" stehen. Es wäre also auch " +
                        "möglich, den Browser mit einer bestimmten URL zu öffnen oder den Rechner damit herunter zu fahren:" +
                        "\n\n" +
                        "Programm (Linux):   firefox\n" +
                        "Programm (Windows):   C:\\Program Files\\Mozilla Firefox\\firefox.exe\n" +
                        "Schalter: http://www.google.de" +
                        "\n");
        btnHelp.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not()
                .or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not())
                .or(ProgConfig.SYSTEM_PLAY_OWN_PLAYER.not()));

        P2ToggleSwitch tglOwnPlayer = new P2ToggleSwitch("Dafür einen eigenen Player verwenden");
        tglOwnPlayer.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_PLAY_OWN_PLAYER);
        tglOwnPlayer.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not()
                .or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not()));

        TextField txtOwnPlayer = new TextField();
        txtOwnPlayer.textProperty().bindBidirectional(ProgConfig.SYSTEM_OWN_PLAYER_FILE);
        txtOwnPlayer.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not()
                .or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not())
                .or(ProgConfig.SYSTEM_PLAY_OWN_PLAYER.not()));

        TextField txtOwnParameter = new TextField();
        txtOwnParameter.textProperty().bindBidirectional(ProgConfig.SYSTEM_OWN_PLAYER_PARAMETER);
        txtOwnParameter.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not()
                .or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not())
                .or(ProgConfig.SYSTEM_PLAY_OWN_PLAYER.not()));

        final Button btnFile = new Button();
        btnFile.setGraphic(ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Eine Player zum Abspielen auswählen"));
        btnFile.setOnAction(event -> P2DirFileChooser.FileChooserOpenFile(stage, txtOwnPlayer));
        btnFile.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not()
                .or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not())
                .or(ProgConfig.SYSTEM_PLAY_OWN_PLAYER.not()));

        final Button btnReset = new Button();
        btnReset.setGraphic(ProgIcons.ICON_BUTTON_RESET_VLC.getImageView());
        btnReset.setTooltip(new Tooltip("VLC Player wieder herstellen"));
        btnReset.setOnAction(event -> {
            final String path = TimerFactory.getTemplatePathVlc();
            ProgConfig.SYSTEM_OWN_PLAYER_FILE.set(path);
            ProgConfig.SYSTEM_OWN_PLAYER_PARAMETER.set("%f<>--play-and-exit<>-I<>dummy");
        });
        btnReset.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not()
                .or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not())
                .or(ProgConfig.SYSTEM_PLAY_OWN_PLAYER.not()));

        Label lblPlayer = new Label("Programmdatei:");
        lblPlayer.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not()
                .or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not())
                .or(ProgConfig.SYSTEM_PLAY_OWN_PLAYER.not()));

        Label lblParameter = new Label("Parameter:");
        lblParameter.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not()
                .or(ProgConfig.SYSTEM_PLAY_OWN_SOUND.not())
                .or(ProgConfig.SYSTEM_PLAY_OWN_PLAYER.not()));

        gridPane.add(new Label(""), 0, ++row);

        gridPane.add(tglOwnPlayer, 0, ++row, 3, 1);
        ++row;
        gridPane.add(lblPlayer, 0, ++row);
        gridPane.add(txtOwnPlayer, 1, row);
        gridPane.add(btnFile, 2, row);
        gridPane.add(lblParameter, 0, ++row);
        gridPane.add(txtOwnParameter, 1, row);
        gridPane.add(btnReset, 2, row);
        gridPane.add(btnHelp, 2, ++row);
    }

    private void addTest() {
        Button btnTest = new Button("Test");
        btnTest.setOnAction(a -> TimerSoundFactory.playSound());
        btnTest.disableProperty().bind(ProgConfig.SYSTEM_PLAY_SOUND.not());

        Button btnStop = new Button("Stop");
        btnStop.disableProperty().bind(TimerSoundFactory.processProp.isNull().and(TimerSoundFactory.mediaPlayerProp.isNull()));
        btnStop.setOnAction(a -> TimerSoundFactory.stopSound());

        HBox hBox = new HBox(P2LibConst.PADDING_HBOX);
        hBox.getChildren().addAll(btnStop, btnTest);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(new Label(), 0, ++row);
        gridPane.add(hBox, 0, ++row, 3, 1);
    }
}
