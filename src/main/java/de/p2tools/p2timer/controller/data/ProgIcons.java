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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.icons.P2Icon;
import de.p2tools.p2timer.P2TimerController;
import de.p2tools.p2timer.controller.config.ProgConst;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProgIcons {

    public static String ICON_PATH = "res/program/";
    public static String ICON_PATH_LONG = "de/p2tools/p2timer/res/program/";

    private static final List<P2IconP2Timer> iconList = new ArrayList<>();

    public static P2IconP2Timer ICON_T_BUTTON_START = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "t-button-start.png", 16, 16);
    public static P2IconP2Timer ICON_T_BUTTON_STOP = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "t-button-stop.png", 16, 16);
    public static P2IconP2Timer ICON_T_BUTTON_PAUSE = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "t-button-pause.png", 16, 16);
    public static P2IconP2Timer ICON_T_BUTTON_RESTART = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "t-button-restart.png", 16, 16);
    public static P2IconP2Timer ICON_T_BUTTON_CONFIG = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "t-button-config.png", 16, 16);
    public static P2IconP2Timer ICON_T_BUTTON_QUITT = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "t-button-quitt.png", 16, 16);


    public static P2IconP2Timer ICON_BUTTON_RESET = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-reset.png", 14, 14);
    public static P2IconP2Timer ICON_BUTTON_PROPOSE = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-propose.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_QUIT = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-quit.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_FILE_OPEN = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-file-open.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_RESET_VLC = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-reset-vlc.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_NEXT = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-next.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_PREV = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-prev.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_FORWARD = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-forward.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_BACKWARD = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-backward.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_STOP = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-stop.png", 16, 16);
    public static P2IconP2Timer ICON_BUTTON_INFO = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "button-info.png", 16, 16);
    public static P2IconP2Timer ICON_DIALOG_QUIT = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "dialog-quit.png", 64, 64);


    public static P2IconP2Timer ICON_TOOLBAR_SMALL_RADIO_20 = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "toolbar-menu-smallRadio-20.png", 20, 20);


    //Menü
    public static P2IconP2Timer ICON_GUI_TIMER_MENU = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "gui-timer-menu.png", 18, 15);
    public static P2IconP2Timer ICON_GUI_TIMER_START = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "gui-timer-start.png", 20, 20);
    public static P2IconP2Timer ICON_GUI_TIMER_STOP = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "gui-timer-stop.png", 20, 20);
    public static P2IconP2Timer ICON_GUI_TIMER_PAUSE = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "gui-timer-pause.png", 20, 20);
    public static P2IconP2Timer ICON_GUI_TIMER_RESTART = new P2IconP2Timer(ICON_PATH_LONG, ICON_PATH, "gui-timer-restart.png", 20, 20);

    public static void initIcons() {
        iconList.forEach(p -> {
            String url = p.genUrl(P2IconP2Timer.class, P2TimerController.class, ProgConst.class, ProgIcons.class, P2LibConst.class);
            if (url.isEmpty()) {
                // dann wurde keine gefunden
                System.out.println("ProgIconsInfo: keine URL, icon: " + p.getPathFileNameDark() + " - " + p.getFileName());
            }
        });
    }

    public static class P2IconP2Timer extends P2Icon {
        public P2IconP2Timer(String longPath, String path, String fileName, int w, int h) {
            super(longPath, path, fileName, w, h);
            iconList.add(this);
        }

        public boolean searchUrl(String p, Class<?>... clazzAr) {
            URL url;
            url = P2TimerController.class.getResource(p);
            if (set(url, p, "P2TimerController.class.getResource")) return true;
            url = ProgConst.class.getResource(p);
            if (set(url, p, "ProgConst.class.getResource")) return true;
            url = ProgIcons.class.getResource(p);
            if (set(url, p, "ProgIcons.class.getResource")) return true;
            url = this.getClass().getResource(p);
            if (set(url, p, "this.getClass().getResource")) return true;

            url = ClassLoader.getSystemResource(p);
            if (set(url, p, "ClassLoader.getSystemResource")) return true;
            url = P2LibConst.class.getClassLoader().getResource(p);
            if (set(url, p, "P2LibConst.class.getClassLoader().getResource")) return true;
            url = ProgConst.class.getClassLoader().getResource(p);
            if (set(url, p, "ProgConst.class.getClassLoader().getResource")) return true;
            url = this.getClass().getClassLoader().getResource(p);
            if (set(url, p, "this.getClass().getClassLoader().getResource")) return true;

            return false;
        }
    }
}
