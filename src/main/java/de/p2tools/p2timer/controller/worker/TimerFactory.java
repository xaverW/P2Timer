/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2timer.controller.worker;

import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2timer.controller.config.ProgConfig;

import java.io.File;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TimerFactory {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final ArrayList<String> winPath = new ArrayList<>();


    private TimerFactory() {
    }

    public static long getClockSeconds() {
        try {
            String saved = ProgConfig.SYSTEM_COUNT_CLOCK_TIME.getValueSafe();
            LocalTime savedTime = LocalTime.parse(saved, TimerFactory.formatter);
            long l = Duration.between(LocalTime.now(), savedTime).toSeconds();
            if (l < 0) {
                // dann ein Tag dazu
                l += 24 * 60 * 60;
            }
            return l;
        } catch (Exception ex) {
            return 10;
        }
    }

    public static String getTimerText(long all, boolean setHour, boolean setSeconds) {
        long h = all / (60 * 60);
        long m = (all - h * 60 * 60) / 60;
        long s = all % 60;
        String hour = String.format("%02d", h);
        String min = String.format("%02d", m);
        String sec = String.format("%02d", s);

        if (setHour) {
            return hour + ":" + min + (setSeconds ? ":" + sec : "");
        } else {
            return (h == 0 ? "" : hour + ":") + min + (setSeconds ? ":" + sec : "");
        }
    }

    public static String getTemplatePathVlc() {
        // liefert den Standardpfad für das entsprechende BS
        // Programm muss auf dem Rechner instelliert sein
        final String PATH_LINUX_VLC = "/usr/bin/vlc";
        final String PATH_FREEBSD = "/usr/local/bin/vlc";
        final String PATH_WIN = "\\VideoLAN\\VLC\\vlc.exe";
        String path = "";
        try {
            switch (P2ToolsFactory.getOs()) {
                case LINUX:
                    if (System.getProperty("os.name").toLowerCase().contains("freebsd")) {
                        path = PATH_FREEBSD;
                    } else {
                        path = PATH_LINUX_VLC;
                    }
                    break;
                default:
                    setWinProgPath();
                    for (final String s : winPath) {
                        path = s + PATH_WIN;
                        if (new File(path).exists()) {
                            break;
                        }
                    }
            }
            if (!new File(path).exists() && System.getenv("PATH_VLC") != null) {
                path = System.getenv("PATH_VLC");
            }
            if (!new File(path).exists()) {
                path = "";
            }
        } catch (final Exception ignore) {
        }
        return path;
    }

    private static void setWinProgPath() {
        String pfad;
        if (System.getenv("ProgramFiles") != null) {
            pfad = System.getenv("ProgramFiles");
            if (new File(pfad).exists() && !winPath.contains(pfad)) {
                winPath.add(pfad);
            }
        }
        if (System.getenv("ProgramFiles(x86)") != null) {
            pfad = System.getenv("ProgramFiles(x86)");
            if (new File(pfad).exists() && !winPath.contains(pfad)) {
                winPath.add(pfad);
            }
        }
        final String[] PATH = {"C:\\Program Files", "C:\\Programme", "C:\\Program Files (x86)"};
        for (final String s : PATH) {
            if (new File(s).exists() && !winPath.contains(s)) {
                winPath.add(s);
            }
        }
    }
}

