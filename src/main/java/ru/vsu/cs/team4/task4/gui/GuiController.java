package ru.vsu.cs.team4.task4.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import ru.vsu.cs.team4.task4.affine.affineComposite.RotateCustom;
import ru.vsu.cs.team4.task4.affine.affineComposite.RotateY;
import ru.vsu.cs.team4.task4.math.vector.Vector2f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.Model;
import ru.vsu.cs.team4.task4.model.ModelTriangulated;
import ru.vsu.cs.team4.task4.model.NormalCalculator;
import ru.vsu.cs.team4.task4.model.Polygon;
import ru.vsu.cs.team4.task4.objio.ObjReader;
import ru.vsu.cs.team4.task4.render_engine.Camera;
import ru.vsu.cs.team4.task4.render_engine.RenderEngine;
import ru.vsu.cs.team4.task4.scene.LoadedModel;
import ru.vsu.cs.team4.task4.scene.Scene;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GuiController {
    final private float TRANSLATION = 0.5F;


    @FXML
    AnchorPane anchorPane;

    @FXML
    private ImageView imageView;

    @FXML
    private TitledPane transformationsPane;

    @FXML
    private TableView<LoadedModel> modelsTable;
    @FXML
    private TableColumn<LoadedModel, String> modelPathColumn;
    @FXML
    private TableColumn<LoadedModel, CheckBox> isActiveColumn;
    @FXML
    private TableColumn<LoadedModel, CheckBox> isEditableColumn;

    @FXML
    private TableView<Camera> camerasTable;
    @FXML
    private TableColumn<Camera, HBox> cameraColumn;




    @FXML
    private TextField scaleX;
    @FXML
    private TextField scaleY;
    @FXML
    private TextField scaleZ;

    @FXML
    private TextField translateX;
    @FXML
    private TextField translateY;
    @FXML
    private TextField translateZ;

    @FXML
    private TextField rotateX;
    @FXML
    private TextField rotateY;
    @FXML
    private TextField rotateZ;

    private Model mesh = null;
    private Scene scene = null;

    private Point2D mousePos;

    private final static int CAMERA_ZOOM_STEP = 5;

    private Timeline timeline;

    @FXML
    private void initialize() {

        scene = new Scene();
        modelPathColumn.setCellValueFactory(new PropertyValueFactory<>("modelName"));
        isActiveColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getActivationCheckbox()));
        isEditableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIsEditable()));

        cameraColumn.setCellValueFactory(cellData -> {
            Button button = new Button();
            button.setText("Choose");
            button.setOnAction(actionEvent -> {
                scene.setActiveCameraId(cellData.getValue().getId());
            });

            HBox hbox = new HBox();
            hbox.getChildren().add(button);
            Label label = new Label();

            label.setText("Camera " + cellData.getValue().getId());
            HBox.setMargin(button, new Insets(0,10,0,10));
            hbox.getChildren().add(label);
            return new SimpleObjectProperty<>(hbox);
        });

        ObservableList<Camera> list = camerasTable.getItems();
        list.addAll(scene.getCameras());

        transformationsPane.setVisible(false);


        isActiveColumn.setCellFactory(column -> new TableCell<>() {
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

        modelsTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                modelsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            } else if (event.getCode() == KeyCode.SHIFT) {
                modelsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            }
        });

        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                Camera camera = scene.getActiveCamera();
                float dx = (float) mouseEvent.getX() - (float) mousePos.getX();
                float dy = (float) mouseEvent.getY() - (float) mousePos.getY();
                float thetaY = -dx / 500;
                float theta2 = dy / 500;
                RotateCustom rotateCustom = new RotateCustom(new Vector3f(-camera.getPosition().getZ(), 0, camera.getPosition().getX()), theta2);
                RotateY rotateY = new RotateY(thetaY);
                Vector3f new_pos = rotateY.getMatrix3f().mul(rotateCustom.getMatrix3f()).mulV(camera.getPosition());
                if(new_pos.getX() * camera.getPosition().getX() > 0 || new_pos.getZ() * camera.getPosition().getZ() > 0){
                    camera.setPosition(new_pos);
                }
                mousePos = new Point2D((float) mouseEvent.getX(), (float) mouseEvent.getY());
            }
        });

        imageView.setOnMousePressed(e -> {
            mousePos = new Point2D(e.getX(), e.getY());
        });

        imageView.setOnScroll(scrollEvent -> {
            Camera camera = scene.getActiveCamera();
            float s = camera.getPosition().len();
            if (scrollEvent.getDeltaY() > 0) {
                if (s > CAMERA_ZOOM_STEP * 2) {
                    camera.setPosition(new Vector3f(camera.getPosition().getX() * (1 - CAMERA_ZOOM_STEP / s),
                            camera.getPosition().getY() * (1 - CAMERA_ZOOM_STEP / s),
                            camera.getPosition().getZ() * (1 - CAMERA_ZOOM_STEP / s)));
                }
            } else if(scrollEvent.getDeltaY() < 0){
                camera.setPosition(new Vector3f(camera.getPosition().getX() * (1 + CAMERA_ZOOM_STEP / s),
                        camera.getPosition().getY() * (1 + CAMERA_ZOOM_STEP / s),
                        camera.getPosition().getZ() * (1 + CAMERA_ZOOM_STEP / s)));
            }
        });

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(100), event -> {
            Camera camera = scene.getActiveCamera();

            int width = (int) imageView.getFitWidth();
            int height = (int) imageView.getFitHeight();

            if(width == 0 || height == 0){
                return;
            }

            IntBuffer buffer = IntBuffer.allocate(width * height);
            int[] pixels = buffer.array();
            PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(width, height, buffer, PixelFormat.getIntArgbPreInstance());

            camera.setAspectRatio(1f * width / height);

            if (mesh != null) {
                try {
                    RenderEngine.renderScene(pixels, width, height, scene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            pixelBuffer.updateBuffer(c -> null);
            WritableImage image = new WritableImage(pixelBuffer);
            imageView.setImage(image);
        });



        timeline.getKeyFrames().add(frame);
        timeline.play();

        anchorPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                imageView.setFitWidth(t1.doubleValue());
            }
        });

        anchorPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                imageView.setFitHeight(t1.doubleValue());
            }
        });
    }

    @FXML
    private void onClickAddCamera(){
        scene.addCamera();
        ObservableList<Camera> list = camerasTable.getItems();
        list.clear();
        list.addAll(scene.getCameras());
    }

    @FXML
    private void onClickDeleteCamera(){
        ObservableList<Camera> selected = camerasTable.getSelectionModel().getSelectedItems();
        ObservableList<Camera> items = camerasTable.getItems();
        int activeCameraId = scene.getActiveCamera().getId();
        if(selected.size() > 0 && items.size() > 1){
            if(selected.get(0).getId() == activeCameraId){
                for(Camera camera: camerasTable.getItems()){
                    if(camera.getId() != activeCameraId){
                        scene.setActiveCameraId(camera.getId());
                        break;
                    }
                }
            }
            scene.getCameras().remove(selected.get(0));
            camerasTable.getItems().remove(selected.get(0));
        }
        scene.setActiveCameraId(camerasTable.getItems().get(0).getId());
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
        File file = fileChooser.showOpenDialog((Stage) imageView.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());
        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            for (Vector2f tv : mesh.getTextureVertices()) {
                if (Math.abs(tv.getX() - 1) < 1E-5) {
                    tv.setX(0.9999f);
                }
                if (Math.abs(tv.getY() - 1) < 1E-5) {
                    tv.setY(0.9999f);
                }
            }

            LoadedModel newModel = new LoadedModel(mesh, "name");
            newModel.setModelPath(fileName.toString());
            newModel.setId(scene.getModels().size());

            CheckBox checkBox1 = new CheckBox();
            newModel.setIsActive(checkBox1);
            CheckBox checkBox2 = new CheckBox();
            checkBox2.setDisable(true);
            newModel.setIsEditable(checkBox2);
            checkBox1.setOnAction(event -> {
                // Если checkBox1 выбран, активируем checkBox2, иначе деактивируем
                checkBox2.setDisable(!checkBox1.isSelected());
                if (!checkBox1.isSelected()) {
                    checkBox2.setSelected(false);
                }

            });

            // Update the existing ObservableList
            final ObservableList<LoadedModel> data = modelsTable.getItems();
            data.add(newModel); // Assuming Models has a constructor
            scene.addModel(newModel);
            //modelsTable.setItems(data);
        } catch (IOException exception) {
            System.out.println("Wrong arguments");
        }
    }

    @FXML
    private void onClickShowHide() {
        transformationsPane.setVisible(!transformationsPane.isVisible());
    }

    @FXML
    private void onClickShowHideModels() {
        modelsTable.setVisible(!modelsTable.isVisible());
    }

    @FXML
    private void onClickDeleteModel() {

    }

    //todo write implementation to save current model

    @FXML
    private void onSaveModelMenuItemClick() {

    }

    @FXML
    private void onClickScale() {
        float x = Float.parseFloat(scaleX.getText());
        float y = Float.parseFloat(scaleY.getText());
        float z = Float.parseFloat(scaleZ.getText());

        for (LoadedModel lm : scene.getModels()) {
            if (lm.isEditable()) {
                lm.setScaleV(new Vector3f(x, y, z));
            }
        }
    }

    @FXML
    private void onClickTranslate() {
        float x = Float.parseFloat(translateX.getText());
        float y = Float.parseFloat(translateY.getText());
        float z = Float.parseFloat(translateZ.getText());

        for (LoadedModel lm : scene.getModels()) {
            if (lm.isEditable()) {
                lm.setTranslateV(new Vector3f(x, y, z));
            }
        }
    }

    @FXML
    private void onClickRotate() {
        float x = (float) Math.toRadians(Float.parseFloat(rotateX.getText()));
        float y = (float) Math.toRadians(Float.parseFloat(rotateY.getText()));
        float z = (float) Math.toRadians(Float.parseFloat(rotateZ.getText()));

        for (LoadedModel lm : scene.getModels()) {
            if (lm.isEditable()) {
                lm.setRotateV(new Vector3f(x, y, z));
            }
        }
    }

    @FXML
    private void deleteSelectedModels() {
        ObservableList<LoadedModel> selectedModels = modelsTable.getSelectionModel().getSelectedItems();
        // Удаляем выделенные модели из сцены и из таблицы
        scene.getModels().removeAll(selectedModels);
        modelsTable.getItems().removeAll(selectedModels);
    }
}