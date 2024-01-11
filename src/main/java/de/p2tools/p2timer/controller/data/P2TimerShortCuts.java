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


package de.p2tools.p2timer.controller.data;

import de.p2tools.p2lib.tools.shortcut.P2ShortcutKey;
import de.p2tools.p2timer.controller.config.ProgConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class P2TimerShortCuts {
    // Menü
    public static final P2ShortcutKey SHORTCUT_QUIT_PROGRAM =
            new P2ShortcutKey(ProgConfig.SHORTCUT_QUIT_PROGRAM, ProgConfig.SHORTCUT_QUIT_PROGRAM_INIT,
                    "Programm beenden",
                    "Das Programm wird beendet.");


    private static ObservableList<P2ShortcutKey> shortcutList = FXCollections.observableArrayList();

    public P2TimerShortCuts() {
        shortcutList.add(SHORTCUT_QUIT_PROGRAM);
    }

    public static synchronized ObservableList<P2ShortcutKey> getShortcutList() {
        return shortcutList;
    }
}
