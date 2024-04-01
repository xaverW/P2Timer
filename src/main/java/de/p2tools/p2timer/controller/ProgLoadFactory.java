/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2timer.controller;

import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.ConfigReadFile;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2timer.controller.config.ProgConfig;
import de.p2tools.p2timer.controller.config.ProgData;
import de.p2tools.p2timer.controller.config.ProgInfos;
import de.p2tools.p2timer.controller.config.UpdateConfig;

import java.nio.file.Path;

public class ProgLoadFactory {

    private ProgLoadFactory() {
    }

    public static boolean loadProgConfigData() {
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        if (!loadProgConfig()) {
            //todo? teils geladene Reste entfernen
            P2Log.sysLog("-> konnte nicht geladen werden!");
            clearConfig();
            return false;
        } else {
            UpdateConfig.update(); // falls es ein Programmupdate gab, Configs anpassen
            P2Log.sysLog("-> wurde gelesen!");
            return true;
        }
    }

    private static void clearConfig() {
        ProgData progData = ProgData.getInstance();
    }

    private static boolean loadProgConfig() {
        final Path path = ProgInfos.getSettingsFile();
        P2Log.sysLog("Programmstart und ProgConfig laden von: " + path);
        ConfigFile configFile = new ConfigFile(path.toString(), true);
        ProgConfig.addConfigData(configFile);
        if (ConfigReadFile.readConfig(configFile)) {
            P2Log.sysLog("Konfig wurde geladen!");
            return true;

        } else {
            // dann hat das Laden nicht geklappt
            P2Log.sysLog("Konfig konnte nicht geladen werden!");
            return false;
        }
    }
}


