module com.example.lab6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;


    opens com.example.lab6 to javafx.fxml;
    exports com.example.lab6;

    opens com.example.lab6.socialnetwork.domain to javafx.fxml;
    exports com.example.lab6.socialnetwork.domain;

}