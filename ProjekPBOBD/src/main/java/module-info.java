module com.example.projekpbobd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires kernel;
    requires layout;
    requires io;
    requires org.apache.poi.ooxml;


    opens com.example.projekpbobd to javafx.fxml;
    exports com.example.projekpbobd;
    exports com.example.projekpbobd.controllers;
    opens com.example.projekpbobd.controllers to javafx.fxml;
    exports com.example.projekpbobd.beans;
    opens com.example.projekpbobd.beans to javafx.fxml;
}