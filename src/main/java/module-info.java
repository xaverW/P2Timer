module p2timer {

    opens de.p2tools.p2timer;
    exports de.p2tools.p2timer;

    requires javafx.controls;
    requires de.p2tools.p2lib;
    requires org.apache.commons.io;
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires commons.cli;
    
     requires org.controlsfx.controls;
}

