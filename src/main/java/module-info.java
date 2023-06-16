module com.code.fictif {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;


    opens com.code.fictif to javafx.fxml;
    exports com.code.fictif;
    exports com.code.fictif.controller;
    opens com.code.fictif.controller to javafx.fxml;
    opens com.code.fictif.model to javafx.base;
}

