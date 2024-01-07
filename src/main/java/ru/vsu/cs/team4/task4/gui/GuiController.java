package ru.vsu.cs.team4.task4.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

import java.awt.*;
import java.awt.event.ActionListener;
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
    private TableView<LoadedModel> tableView;

    @FXML
    private TableColumn<LoadedModel, String> modelPath;

    @FXML
    private TableColumn<LoadedModel, CheckBox> isActive;
    @FXML
    private TableColumn<LoadedModel, CheckBox> isEditable;

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


    private Camera camera = new Camera(
            new Vector3f(0, 100, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);
    private final static int CAMERA_ZOOM_STEP = 5;

    private Timeline timeline;

    @FXML
    private void initialize() {

        scene = new Scene();

        modelPath.setCellValueFactory(new PropertyValueFactory<>("modelName"));
        isActive.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getActivationCheckbox()));
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

        tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            } else if (event.getCode() == KeyCode.SHIFT) {
                tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            }
        });

        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
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
            float s = (float) Math.sqrt(Math.pow(camera.getPosition().getX(), 2) + Math.pow(camera.getPosition().getY(), 2) + Math.pow(camera.getPosition().getZ(), 2));
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
            int width = (int) imageView.getBoundsInParent().getWidth();
            int height = (int) imageView.getBoundsInParent().getHeight();

            IntBuffer buffer = IntBuffer.allocate(width * height);
            int[] pixels = buffer.array();
            PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(width, height, buffer, PixelFormat.getIntArgbPreInstance());

            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                try {
                    RenderEngine.renderScene(pixels, width, height, camera, scene);
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
            List<Vector3f> normals = NormalCalculator.recalculateNormals(mesh.getVertices(), mesh.getPolygons());
            for (Polygon polygon : mesh.getPolygons()) {
                polygon.setNormalIndices(new ArrayList<>(polygon.getVertexIndices()));
            }
            mesh.setNormals(normals);
            LoadedModel newModel = new LoadedModel(new ModelTriangulated(mesh), "name");
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
            final ObservableList<LoadedModel> data = tableView.getItems();
            data.add(newModel); // Assuming Models has a constructor
            scene.addModel(newModel);
            tableView.setItems(data);
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
    private void deleteSelected() {
        ObservableList<LoadedModel> selectedModels = tableView.getSelectionModel().getSelectedItems();
        // Удаляем выделенные модели из сцены и из таблицы
        scene.getModels().removeAll(selectedModels);
        tableView.getItems().removeAll(selectedModels);
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