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


import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2timer.controller.config.ProgConfig;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.nio.file.Path;

public class TimerSoundFactory {
    public static final ObjectProperty<Process> processProp = new SimpleObjectProperty<>(null);
    public static final ObjectProperty<MediaPlayer> mediaPlayerProp = new SimpleObjectProperty<>(null);

    private TimerSoundFactory() {
    }

    public static void stopSound() {
        if (processProp.get() != null) {
            processProp.get().destroy();
            processProp.setValue(null);
        }
        if (mediaPlayerProp.get() != null) {
            mediaPlayerProp.get().stop();
            mediaPlayerProp.setValue(null);
        }
    }

    public static void playSound() {
        if (!ProgConfig.SYSTEM_PLAY_SOUND.getValue()) {
            // dann will ers nicht
            return;
        }

        try {
            String sound;
            URL url;

            if (ProgConfig.SYSTEM_PLAY_OWN_PLAYER.getValue() &&
                    ProgConfig.SYSTEM_PLAY_OWN_SOUND.getValue() &&
                    !ProgConfig.SYSTEM_OWN_PLAYER_FILE.getValueSafe().isEmpty() &&
                    !ProgConfig.SYSTEM_OWN_SOUND_FILE.getValueSafe().isEmpty()) {

                // dann eigenen Player und eigenen Sound
                sound = ProgConfig.SYSTEM_OWN_SOUND_FILE.getValueSafe();
                String call = ProgConfig.SYSTEM_OWN_PLAYER_FILE.getValueSafe();
                String parameter = ProgConfig.SYSTEM_OWN_PLAYER_PARAMETER.getValueSafe();
                parameter = parameter.replace("%f", sound);
                call = call + "<>" + parameter;
                RuntimeExecPlay runtimeExecPlay = new RuntimeExecPlay(call);
                processProp.setValue(runtimeExecPlay.exec(true));

            } else if (ProgConfig.SYSTEM_PLAY_OWN_SOUND.getValue() &&
                    !ProgConfig.SYSTEM_OWN_SOUND_FILE.getValueSafe().isEmpty()) {

                // dann eigenen Sound mit Player
                sound = ProgConfig.SYSTEM_OWN_SOUND_FILE.getValueSafe();
                url = Path.of(sound).toUri().toURL();

                Media media = new Media(url.toString());
                MediaPlayer player = new MediaPlayer(media);
                mediaPlayerProp.setValue(player);
                player.play();
                player.setOnEndOfMedia(() -> mediaPlayerProp.setValue(null));

            } else {
                // dann Sound mit Player
                sound = SOUND.getSound(ProgConfig.SYSTEM_TIMER_SOUND.getValue()).getPath();
                url = ClassLoader.getSystemResource(sound);

                Media media = new Media(url.toString());
                MediaPlayer player = new MediaPlayer(media);
                mediaPlayerProp.setValue(player);
                player.play();
                player.setOnEndOfMedia(() -> mediaPlayerProp.setValue(null));
            }
        } catch (Exception ex) {
            P2Log.errorLog(604598712, ex);
        }
    }

    public enum SOUND {
        SOUND_01("de/p2tools/p2timer/res/sound/sound_01.wav"),
        SOUND_02("de/p2tools/p2timer/res/sound/sound_02.wav"),
        SOUND_03("de/p2tools/p2timer/res/sound/sound_03.wav"),
        SOUND_04("de/p2tools/p2timer/res/sound/sound_04.wav"),
        SOUND_05("de/p2tools/p2timer/res/sound/sound_05.wav"),
        SOUND_06("de/p2tools/p2timer/res/sound/sound_06.wav"),
        SOUND_07("de/p2tools/p2timer/res/sound/sound_07.wav"),
        SOUND_08("de/p2tools/p2timer/res/sound/sound_08.wav"),
        SOUND_09("de/p2tools/p2timer/res/sound/sound_09.wav"),
        SOUND_10("de/p2tools/p2timer/res/sound/sound_10.wav"),
        SOUND_11("de/p2tools/p2timer/res/sound/sound_11.wav"),
        SOUND_12("de/p2tools/p2timer/res/sound/sound_12.wav"),
        SOUND_13("de/p2tools/p2timer/res/sound/sound_13.wav"),
        SOUND_14("de/p2tools/p2timer/res/sound/sound_14.wav"),
        SOUND_15("de/p2tools/p2timer/res/sound/sound_15.wav"),
        SOUND_16("de/p2tools/p2timer/res/sound/sound_16.wav"),
        SOUND_17("de/p2tools/p2timer/res/sound/sound_17.wav");

        private final String path;

        SOUND(String path) {
            this.path = path;
        }

        public static SOUND getSound(String path) {
            for (SOUND s : SOUND.values()) {
                if (s.getPath().equals(path)) {
                    return s;
                }
            }
            return SOUND_01;
        }

        public String getPath() {
            return path;
        }
    }
}
