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

package de.p2tools.p2timer.controller;

import de.p2tools.p2lib.guitools.P2WindowIcon;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2LogMessage;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2timer.controller.config.ProgConfig;
import de.p2tools.p2timer.controller.config.ProgConst;
import de.p2tools.p2timer.controller.config.ProgData;
import de.p2tools.p2timer.controller.config.ProgInfos;

import java.util.ArrayList;

public class ProgStartFactory {

    private ProgStartFactory() {
    }

    public static void workBeforeGui() {
        boolean loadOk = ProgLoadFactory.loadProgConfigData();
        if (!loadOk) {
            P2Duration.onlyPing("Erster Start");
        }
    }

    /**
     * alles, was nach der GUI gemacht werden soll z.B.
     * Podcast beim Programmstart!! laden
     */
    public static void workAfterGui(ProgData progData) {
        P2WindowIcon.addWindowP2Icon(progData.primaryStage);
        startMsg();
        setTitle(progData);

        progData.initProgData();
    }

    private static void startMsg() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Verzeichnisse:");
        list.add("Programmpfad: " + ProgInfos.getPathJar());
        list.add("Verzeichnis Einstellungen: " + ProgInfos.getSettingsDirectoryString());
        list.add(P2Log.LILNE2);
        list.add("");
        ProgConfig.getConfigLog(list);
        P2LogMessage.startMsg(ProgConst.PROGRAM_NAME, list);
    }

    private static void setTitle(ProgData progData) {
        if (ProgData.debug) {
            progData.primaryStage.setTitle(ProgConst.PROGRAM_NAME + " " + P2ToolsFactory.getProgVersion() + " / DEBUG");
        } else {
            progData.primaryStage.setTitle(ProgConst.PROGRAM_NAME + " " + P2ToolsFactory.getProgVersion());
        }
    }
}