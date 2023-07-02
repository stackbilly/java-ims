module com.stacklink.inventory_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires mongo.java.driver;
    requires pdfbox;

    opens com.stacklink.inventory_management_system to javafx.fxml;
    exports com.stacklink.inventory_management_system;
}