module com.example.li_studenttracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.li_studenttracker to javafx.fxml;
    exports com.example.li_studenttracker;
}