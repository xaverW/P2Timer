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

package de.p2tools.p2timer.gui.tools;

import de.p2tools.p2lib.tools.log.PLog;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.EventListener;


public class ProgListener implements EventListener {

    private static final ArrayList<ProgListener> PROG_LISTENERS = new ArrayList<>();
    //todo???
    static int count = 0;
    public static final int EREIGNIS_TIMER = count++;
    public static final int EREIGNIS_TIMER_HALF_SECOND = count++;
    public int[] event = {-1};
    public String eventClass = "";

    public ProgListener() {
    }

    public ProgListener(int event, String eventClass) {
        this.event = new int[]{event};
        this.eventClass = eventClass;
    }

    public ProgListener(int[] event, String eventClass) {
        this.event = event;
        this.eventClass = eventClass;
    }

    public static synchronized void addListener(ProgListener progListener) {
        PLog.sysLog("Anz. Listener: " + PROG_LISTENERS.size());
        PROG_LISTENERS.add(progListener);
    }

    public static synchronized void removeListener(ProgListener progListener) {
        PROG_LISTENERS.remove(progListener);
    }

    public static synchronized void notify(int eventNotify, String eventClass) {

        PROG_LISTENERS.stream().forEach(progListener -> {

            for (final int event : progListener.event) {
                // um einen Kreislauf zu verhindern
                if (event == eventNotify && !progListener.eventClass.equals(eventClass)) {
                    progListener.pingen();
                }

            }
        });
    }

    public void pingFx() {
        // das passiert im application thread
    }

    public void ping() {
        // das ist asynchron zum application thread
    }

    private void pingen() {
        //System.out.println("Ping: " + ereignis);
        try {
            ping();
            Platform.runLater(() -> pingFx());
        } catch (final Exception ex) {
            PLog.errorLog(698989743, ex);
        }
    }
}
