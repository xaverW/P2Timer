/*
 * P2Tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2timer.controller.config;

public class UpdateConfig {
    private UpdateConfig() {
    }

    public static void setUpdateDone() {
        ProgConfig.SYSTEM_UPDATE_STATE.set(ProgConst.SYSTEM_UPDATE_SATE);
    }

    public static void update() {
        if (ProgConfig.SYSTEM_UPDATE_STATE.get() == ProgConst.SYSTEM_UPDATE_SATE) {
            return;
        }

        switch (ProgConfig.SYSTEM_UPDATE_STATE.get()) {
            case 0:
                break;
            case 1:
                break;
            default:
        }
        setUpdateDone();
    }
}
