/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2timer.controller.worker;

import de.p2tools.p2lib.tools.log.P2Log;

public class RuntimeExecPlay {

    public static final String TRENNER_PROG_ARRAY = "<>";
    private final String[] arrProgCallArray;
    private final String strProgCall;
    private Process process = null;

    public RuntimeExecPlay(String strings) {
        strProgCall = strings;
        arrProgCallArray = strings.split(TRENNER_PROG_ARRAY);
    }

    //===================================
    // Public
    //===================================
    public Process exec(boolean log) {
        try {
            if (log) {
                P2Log.sysLog("=====================");
                P2Log.sysLog("Starten: ");
                P2Log.sysLog(" -> " + strProgCall);
                P2Log.sysLog("=====================");
            }
            process = new ProcessBuilder(arrProgCallArray).inheritIO().start();
        } catch (final Exception ex) {
            P2Log.errorLog(784512589, ex, "Fehler beim Starten");
        }
        return process;
    }
}
