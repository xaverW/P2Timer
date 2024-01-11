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


package de.p2tools.p2timer.gui.tools;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class P2TimeSpinnerHMS extends Spinner<LocalTime> {

    // Mode represents the unit that is currently being edited.
    // For convenience expose methods for incrementing and decrementing that
    // unit, and for selecting the appropriate portion in a spinner's editor
    enum Mode {
        HOURS {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusHours(steps);
            }

            @Override
            void select(P2TimeSpinnerHMS spinner) {
                int index = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(0, index);
            }
        },
        MINUTES {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusMinutes(steps);
            }

            @Override
            void select(P2TimeSpinnerHMS spinner) {
                int hrIndex = spinner.getEditor().getText().indexOf(':');
                int minIndex = spinner.getEditor().getText().indexOf(':', hrIndex + 1);
                spinner.getEditor().selectRange(hrIndex + 1, minIndex);
            }
        },
        SECONDS {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusSeconds(steps);
            }

            @Override
            void select(P2TimeSpinnerHMS spinner) {
                int index = spinner.getEditor().getText().lastIndexOf(':');
                spinner.getEditor().selectRange(index + 1, spinner.getEditor().getText().length());
            }
        };

        abstract LocalTime increment(LocalTime time, int steps);

        abstract void select(P2TimeSpinnerHMS spinner);

        LocalTime decrement(LocalTime time, int steps) {
            return increment(time, -steps);
        }

    }

    // Property containing the current editing mode:
    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS);

    public P2TimeSpinnerHMS() {
        this(LocalTime.now());
    }

    public P2TimeSpinnerHMS(LocalTime time) {
        setEditable(true);

        // Create a StringConverter for converting between the text in the
        // editor and the actual value:
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        StringConverter<LocalTime> localTimeConverter = new StringConverter<>() {

            @Override
            public String toString(LocalTime time) {
                return formatter.format(time);
            }

            @Override
            public LocalTime fromString(String string) {
                String[] tokens = string.split(":");
                int hours = getIntField(tokens, 0);
                int minutes = getIntField(tokens, 1);
                int seconds = getIntField(tokens, 2);
                int totalSeconds = (hours * 60 + minutes) * 60 + seconds;
                return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60, seconds % 60);
            }

            private int getIntField(String[] tokens, int index) {
                if (tokens.length <= index || tokens[index].isEmpty()) {
                    return 0;
                }
                return Integer.parseInt(tokens[index]);
            }

        };

        // The textFormatter both manages the text <-> LocalTime conversion,
        // and vetoes any edits that are not valid. We just make sure we have
        // two colons and only digits in between:
        TextFormatter<LocalTime> textFormatter = new TextFormatter<>(localTimeConverter, time, c -> {
            String newText = c.getControlNewText();
            if (newText.matches("[0-9]{0,2}:[0-9]{0,2}:[0-9]{0,2}")) {
                return c;
            }
            return null;
        });

        // The spinner value factory defines increment and decrement by
        // delegating to the current editing mode:
        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<>() {
            {
                setConverter(localTimeConverter);
                setValue(time);
            }

            @Override
            public void decrement(int steps) {
                setValue(mode.get().decrement(getValue(), steps));
                mode.get().select(P2TimeSpinnerHMS.this);
            }

            @Override
            public void increment(int steps) {
                setValue(mode.get().increment(getValue(), steps));
                mode.get().select(P2TimeSpinnerHMS.this);
            }

        };

        this.setValueFactory(valueFactory);
        this.getEditor().setTextFormatter(textFormatter);

        // Update the mode when the user interacts with the editor.
        // This is a bit of a hack, e.g. calling spinner.getEditor().positionCaret()
        // could result in incorrect state. Directly observing the caretPostion
        // didn't work well though; getting that to work properly might be
        // a better approach in the long run.
        this.getEditor().addEventHandler(InputEvent.ANY, e -> {
            int caretPos = this.getEditor().getCaretPosition();
            int hrIndex = this.getEditor().getText().indexOf(':');
            int minIndex = this.getEditor().getText().indexOf(':', hrIndex + 1);
            if (caretPos <= hrIndex) {
                mode.set(Mode.HOURS);
            } else if (caretPos <= minIndex) {
                mode.set(Mode.MINUTES);
            } else {
                mode.set(Mode.SECONDS);
            }
        });

        // When the mode changes, select the new portion:
        mode.addListener((obs, oldMode, newMode) -> newMode.select(this));
    }
}
