module com.cgvsu {
    requires javafx.controls;
    requires javafx.fxml;
    requires vecmath;
    requires java.desktop;


    opens ru.vsu.cs.team4.task4 to javafx.fxml;
    exports ru.vsu.cs.team4.task4;
    exports ru.vsu.cs.team4.task4.scene;
    opens ru.vsu.cs.team4.task4.scene to javafx.fxml;
    exports ru.vsu.cs.team4.task4.gui;
    opens ru.vsu.cs.team4.task4.gui to javafx.fxml;
}