module MySaper {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens SaperPackage.DataModel;
    opens SaperPackage;
}