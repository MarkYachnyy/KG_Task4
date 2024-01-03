package ru.vsu.cs.team4.task4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.vsu.cs.team4.task4.Affine.affineComposite.RotateZ;
import ru.vsu.cs.team4.task4.math.matrix.Matrix3f;
import ru.vsu.cs.team4.task4.math.vector.Vector2f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;
import ru.vsu.cs.team4.task4.model.Model;
import ru.vsu.cs.team4.task4.model.ModelTriangulated;
import ru.vsu.cs.team4.task4.model.NormalCalculator;
import ru.vsu.cs.team4.task4.model.Polygon;
import ru.vsu.cs.team4.task4.objio.ObjReader;
import ru.vsu.cs.team4.task4.render_engine.Camera;
import ru.vsu.cs.team4.task4.render_engine.RenderEngine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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


    private Model mesh = null;
    private Scene scene = null;

    private Camera camera = new Camera(
            new Vector3f(0, 100, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

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

        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            float xo = 800;
            int yo;
            @Override
            public void handle(MouseEvent mouseEvent) {
                float x = (float) mouseEvent.getX();
                camera.setPosition(new Vector3f(camera.getPosition().getX() + x - xo, camera.getPosition().getY(), camera.getPosition().getZ()));
                xo = x;
            }
        });

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.FORWARD){
                    camera.setPosition(new Vector3f(camera.getPosition().getX() * 0.9f, camera.getPosition().getY() * 0.9f, camera.getPosition().getZ() * 0.9f));
                } else if(mouseEvent.getButton() == MouseButton.BACK){
                    camera.setPosition(new Vector3f(camera.getPosition().getX() * 1.1f, camera.getPosition().getY() * 1.1f, camera.getPosition().getZ() * 1.1f));
                }
            }
        });


        //anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> imageView.setWidth(newValue.doubleValue()));
        //anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> imageView.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(33), event -> {
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
            newModel.setIsActive(new CheckBox());
            newModel.setIsEditable(new CheckBox());
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