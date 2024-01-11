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

import de.p2tools.p2timer.controller.config.ProgConfig;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimerFactory {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


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
}
