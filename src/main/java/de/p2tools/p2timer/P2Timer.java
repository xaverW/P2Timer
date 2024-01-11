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

import de.p2tools.p2lib.P2LibInit;
import de.p2tools.p2lib.ProgIconsP2Lib;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.tools.IoReadWriteStyle;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2timer.controller.ProgQuitFactory;
import de.p2tools.p2timer.controller.ProgStartFactory;
import de.p2tools.p2timer.controller.config.ProgConfig;
import de.p2tools.p2timer.controller.config.ProgConst;
import de.p2tools.p2timer.controller.config.ProgData;
import de.p2tools.p2timer.controller.config.ProgInfos;
import de.p2tools.p2timer.controller.data.ProgIcons;
import de.p2tools.p2timer.gui.GuiFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class P2Timer extends Application {

    private static final String LOG_TEXT_PROGRAM_START = "Dauer Programmstart";
    protected ProgData progData;
    Scene scene = null;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        PDuration.counterStart(LOG_TEXT_PROGRAM_START);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        initLibs();
        ProgStartFactory.workBeforeGui();
        initRootLayout();
        ProgStartFactory.workAfterGui(progData);

        PDuration.onlyPing("Gui steht!");
        PDuration.counterStop(LOG_TEXT_PROGRAM_START);
    }

    private void initLibs() {
        ProgIcons.initIcons();
        ProgIconsP2Lib.initIcons();
        P2LibInit.initLib(primaryStage, ProgConst.PROGRAM_NAME,
                "", ProgConfig.SYSTEM_DARK_THEME, ProgData.debug, ProgData.duration);
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
    }

    private void initRootLayout() {
        try {
            addThemeCss(); // damit es für die 2 schon mal stimmt
            progData.p2TimerController = new P2TimerController();
            scene = new Scene(progData.p2TimerController);

            addThemeCss(); // und jetzt noch für die neue Scene

            if (ProgConfig.SYSTEM_STYLE.get()) {
                P2LibInit.setStyleFile(ProgInfos.getStyleFile().toString());
                IoReadWriteStyle.readStyle(ProgInfos.getStyleFile(), scene);
            }

            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                addThemeCss();
                //erst css ändern, dann
                ProgConfig.SYSTEM_THEME_CHANGED.setValue(!ProgConfig.SYSTEM_THEME_CHANGED.get());
            });

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                ProgQuitFactory.quit(true);
            });

            primaryStage.xProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));
            primaryStage.yProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));

            //Pos setzen
            P2GuiSize.setOnlyPos(ProgConfig.SYSTEM_SIZE_GUI, primaryStage);

            GuiFactory.addBorderListener(progData.primaryStage);
            progData.primaryStage.initStyle(StageStyle.TRANSPARENT);
            progData.p2TimerController.getStyleClass().add("smallGui");

            primaryStage.show();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void addThemeCss() {
        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            P2LibInit.addCssFile(ProgConst.CSS_FILE_DARK_THEME);
        } else {
            P2LibInit.removeCssFile(ProgConst.CSS_FILE_DARK_THEME);
        }
        P2LibInit.addP2CssToScene(scene);
    }
}
