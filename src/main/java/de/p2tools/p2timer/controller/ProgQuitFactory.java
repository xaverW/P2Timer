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

import de.p2tools.p2lib.tools.log.LogMessage;
import javafx.application.Platform;

public class ProgQuitFactory {

    private ProgQuitFactory() {
    }

    /**
     * Quit the application
     *
     * @param showOptionTerminate show options dialog when stations are running
     */
    public static void quit(boolean showOptionTerminate) {
        if (quit_(showOptionTerminate)) {

            // dann jetzt beenden -> Thüss
            Platform.runLater(() -> {
                Platform.exit();
                System.exit(0);
            });

        }
    }

    private static boolean quit_(boolean showOptionTerminate) {
        // und dann Programm beenden
        ProgSaveFactory.saveProgConfig();
        LogMessage.endMsg();
        return true;
    }
}
