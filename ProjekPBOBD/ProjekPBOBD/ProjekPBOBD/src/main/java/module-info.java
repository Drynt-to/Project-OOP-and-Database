module com.example.projekpbobd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.projekpbobd to javafx.fxml;
    opens com.example.projekpbobd.beans to javafx.base;
    opens com.example.projekpbobd.controllers to javafx.fxml;

    exports com.example.projekpbobd;
    exports com.example.projekpbobd.controllers;
}