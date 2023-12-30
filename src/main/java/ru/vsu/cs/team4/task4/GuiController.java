package ru.vsu.cs.team4.task4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.Model;
import ru.vsu.cs.team4.task4.objio.ObjReader;
import ru.vsu.cs.team4.task4.render_engine.Camera;
import ru.vsu.cs.team4.task4.render_engine.RenderEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuiController {
    final private float TRANSLATION = 0.5F;


    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private TitledPane transformationsPane;

    @FXML
    private TableView<LoadedModel> tableView;
    @FXML
    private SplitPane splitPane;

    @FXML
    private TableColumn<LoadedModel, String> modelPath;

    @FXML
    private TableColumn<LoadedModel, CheckBox> isActive;
    @FXML
    private TableColumn<LoadedModel, CheckBox> isEditable;


    private Model mesh = null;
    private LoadedModel loadedModel = new LoadedModel();

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {


        //creates table
        modelPath.setCellValueFactory(new PropertyValueFactory<>("modelName"));
        isActive.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIsActive()));
        isEditable.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIsEditable()));

        transformationsPane.setVisible(false);


        isActive.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(CheckBox checkBox, boolean empty) {
                super.updateItem(checkBox, empty);
                if (empty || checkBox == null) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                }
            }
        });


        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                try {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        onClickAddModel();
    }

    @FXML
    private void onClickAddModel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setInitialDirectory(new File("./3DModels"));
        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());
        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            loadedModel.setModelPath(fileName.toString());
            loadedModel.setModelName(loadedModel.getModelName());
            loadedModel.setIsActive(new CheckBox());
            loadedModel.setIsEditable(new CheckBox());
            // Update the existing ObservableList
            final ObservableList<LoadedModel> data = tableView.getItems();
            data.add(loadedModel); // Assuming Models has a constructor

            tableView.setItems(data);
        } catch (IOException exception) {
            System.out.println("Wrong arguments");
        }
    }

    @FXML
    private void onClickShowHide(){
        transformationsPane.setVisible(!transformationsPane.isVisible());
    }

    @FXML
    private void onClickShowHideModels(){
        tableView.setVisible(!tableView.isVisible());
    }

    @FXML
    private void onClickDeleteModel() {

    }

    //todo write implementation to save current model

    @FXML
    private void onSaveModelMenuItemClick() {

    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}