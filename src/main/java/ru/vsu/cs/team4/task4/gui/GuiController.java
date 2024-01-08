package ru.vsu.cs.team4.task4.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.vsu.cs.team4.task4.affine.AffineBuilder;
import ru.vsu.cs.team4.task4.affine.affineComposite.RotateCustom;
import ru.vsu.cs.team4.task4.affine.affineComposite.RotateY;
import ru.vsu.cs.team4.task4.math.Point2f;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.vector.Vector2f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.Model;
import ru.vsu.cs.team4.task4.objio.ObjReader;
import ru.vsu.cs.team4.task4.objio.ObjWriter;
import ru.vsu.cs.team4.task4.rasterization.ColorIntARGB;
import ru.vsu.cs.team4.task4.render_engine.Camera;
import ru.vsu.cs.team4.task4.render_engine.GraphicConveyor;
import ru.vsu.cs.team4.task4.render_engine.RenderEngine;
import ru.vsu.cs.team4.task4.scene.LoadedModel;
import ru.vsu.cs.team4.task4.scene.Scene;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

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
    private TableColumn<LoadedModel, String> modelPath;
    @FXML
    private TableColumn<LoadedModel, CheckBox> isActive;
    @FXML
    private TableColumn<LoadedModel, CheckBox> isEditable;

    @FXML
    private TableColumn<LoadedModel, Button> displayOptions;

    @FXML
    private CheckBox polygonalMesh;

    @FXML
    private CheckBox antialiasingBox;

    @FXML
    private TitledPane displayPane;

    @FXML
    private TableView<Camera> camerasTable;
    @FXML
    private TableColumn<Camera, HBox> cameraColumn;

    @FXML
    private TextField scaleXTextField;
    @FXML
    private TextField scaleYTextField;
    @FXML
    private TextField scaleZTextField;

    @FXML
    private TextField translateXTextField;
    @FXML
    private TextField translateYTextField;
    @FXML
    private TextField translateZTextField;

    @FXML
    private TextField rotateXTextField;
    @FXML
    private TextField rotateYTextField;
    @FXML
    private TextField rotateZTextField;

    @FXML
    private Canvas coordinateSystemCanvas;

    private Model mesh = null;
    private Scene scene = null;

    private Color textureColor = Color.WHITE;

    private Point2D mousePos;

    private final static int CAMERA_ZOOM_STEP = 5;

    private Timeline timeline;

    @FXML
    private void initialize() {

        scene = new Scene();
        modelPath.setCellValueFactory(new PropertyValueFactory<>("modelName"));

        cameraColumn.setCellValueFactory(cellData -> {
            Button button = new Button();
            button.setText("Choose");
            button.setOnAction(actionEvent -> {
                scene.setActiveCameraId(cellData.getValue().getId());
            });
            Camera camera = cellData.getValue();

            HBox hbox = new HBox();
            hbox.getChildren().add(button);
            Label label = new Label();

            label.setText("Camera " + camera.getId() +
                    " (" + String.format("%.2f", camera.getPosition().getX()) + "; " +
                    String.format("%.2f", camera.getPosition().getY()) + "; " +
                    String.format("%.2f", camera.getPosition().getZ()) + ")");
            HBox.setMargin(button, new Insets(0, 10, 0, 10));
            hbox.getChildren().add(label);
            return new SimpleObjectProperty<>(hbox);
        });

        ObservableList<Camera> list = camerasTable.getItems();
        list.addAll(scene.getCameras());

        transformationsPane.setVisible(false);
        displayPane.setVisible(false);


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

        displayOptions.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Button button, boolean empty) {
                super.updateItem(button, empty);
                if (empty || button == null) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
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
                float thetaY = -dx / 250;
                float theta2 = dy / 250;
                RotateCustom rotateCustom = new RotateCustom(new Vector3f(-camera.getPosition().getZ(), 0, camera.getPosition().getX()), theta2);
                RotateY rotateY = new RotateY(thetaY);
                Vector3f new_pos = rotateY.getMatrix3f().mul(rotateCustom.getMatrix3f()).mulV(camera.getPosition());
                if (new_pos.getX() * camera.getPosition().getX() > 0 || new_pos.getZ() * camera.getPosition().getZ() > 0) {
                    camera.setPosition(new_pos);
                }
                camerasTable.refresh();
                mousePos = new Point2D((float) mouseEvent.getX(), (float) mouseEvent.getY());
                drawCoordinateSystem();
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
            } else if (scrollEvent.getDeltaY() < 0) {
                camera.setPosition(new Vector3f(camera.getPosition().getX() * (1 + CAMERA_ZOOM_STEP / s),
                        camera.getPosition().getY() * (1 + CAMERA_ZOOM_STEP / s),
                        camera.getPosition().getZ() * (1 + CAMERA_ZOOM_STEP / s)));
            }
            camerasTable.refresh();
            drawCoordinateSystem();
        });

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(100), event -> {
            Camera camera = scene.getActiveCamera();

            int width = (int) imageView.getFitWidth();
            int height = (int) imageView.getFitHeight();

            if (width == 0 || height == 0) {
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

        drawCoordinateSystem();
    }

    @FXML
    private void onClickAddCamera() {
        scene.addCamera();
        ObservableList<Camera> list = camerasTable.getItems();
        list.clear();
        list.addAll(scene.getCameras());
    }

    @FXML
    private void onClickDeleteCamera() {
        ObservableList<Camera> selected = camerasTable.getSelectionModel().getSelectedItems();
        ObservableList<Camera> items = camerasTable.getItems();
        int activeCameraId = scene.getActiveCamera().getId();
        if (selected.size() > 0 && items.size() > 1) {
            if (selected.get(0).getId() == activeCameraId) {
                for (Camera camera : camerasTable.getItems()) {
                    if (camera.getId() != activeCameraId) {
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
            newModel.setId(Integer.toString(scene.getModels().size()));

            CheckBox checkBox1 = new CheckBox();
            checkBox1.setId(newModel.getId());

            CheckBox checkBox2 = new CheckBox();
            checkBox2.setId(newModel.getId());
            checkBox2.setDisable(true);

            Button displayOptionsBtn = new Button();
            displayOptionsBtn.setText("Options");
            displayOptionsBtn.setPrefHeight(23);
            displayOptionsBtn.setMaxHeight(23);
            displayOptionsBtn.setMinHeight(23);
            displayOptionsBtn.setMinWidth(30);
            displayOptionsBtn.setPrefWidth(80);
            displayOptionsBtn.setId(newModel.getId());

            displayOptionsBtn.setOnAction(event -> {
                Dialog<Void> optionsDialog = new Dialog<>();
                optionsDialog.setTitle("Display Options");

                // Set the content of the dialog
                VBox dialogContent = new VBox();
                dialogContent.setSpacing(10);
                dialogContent.setMinWidth(300);
                dialogContent.setPrefWidth(300);

                LoadedModel currModel = scene.getModelByID(displayOptionsBtn.getId());


                // Create three CheckBox elements
                CheckBox polygonalMeshCheckBox = new CheckBox("Polygonal Mesh");

                polygonalMeshCheckBox.setSelected(!currModel.getDisableMesh());
                polygonalMeshCheckBox.setOnAction(eventMesh -> {
                    currModel.setDisableMesh(!polygonalMeshCheckBox.isSelected());
                });

                CheckBox showTextureCheckBox = new CheckBox("Show Texture");

                showTextureCheckBox.setSelected(!currModel.getDisableTexture());
                showTextureCheckBox.setOnAction(eventTexture -> {
                    currModel.setDisableTexture(!showTextureCheckBox.isSelected());
                });


                CheckBox antiAliasingCheckBox = new CheckBox("Antialiasing");
                antiAliasingCheckBox.setSelected(!currModel.isDisableSmoothing());
                antiAliasingCheckBox.setOnAction(eventAntialiasing -> {
                    currModel.setDisableSmoothing(!antiAliasingCheckBox.isSelected());
                });


                // Create two Button elements
                Button loadTextureBtn = new Button("LoadTexture");
                loadTextureBtn.setId(newModel.getId());
                loadTextureBtn.setOnAction(eventLoadTexture -> {
                    FileChooser textureChooser = new FileChooser();
                    textureChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
                    textureChooser.setInitialDirectory(new File("./3DModels"));
                    File textureFile = textureChooser.showOpenDialog((Stage) imageView.getScene().getWindow());
                    if (textureFile == null) {
                        return;
                    }

                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(textureFile);
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't read texture file", ButtonType.OK);
                        alert.showAndWait();
                        throw new RuntimeException(e);
                    }
                    ColorIntARGB[][] texture = new ColorIntARGB[image.getHeight()][image.getWidth()];
                    for (int i = 0; i < image.getHeight(); i++) {
                        for (int j = 0; j < image.getWidth(); j++) {
                            texture[j][i] = new ColorIntARGB(255 << 24 | image.getRGB(j, image.getHeight() - 1 - i));
                        }
                    }

                    //curTexture = texture;
                    newModel.setTextureARGB(texture);
                });


                Button chooseColorToFillBtn = new Button("ChooseColorToFill");
                chooseColorToFillBtn.setId(newModel.getId());
                chooseColorToFillBtn.setOnAction(eventChooseColor -> {
                    ColorPicker colorPicker = new ColorPicker();


                    colorPicker.setValue(textureColor); // Устанавливаем начальный цвет

                    // Создаем диалоговое окно с цветовым пикером
                    Dialog<Color> dialog = new Dialog<>();
                    dialog.setTitle("Choose Color");
                    dialog.getDialogPane().setContent(colorPicker);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    // Обработка нажатия кнопки OK
                    dialog.setResultConverter(buttonType -> {
                        if (buttonType == ButtonType.OK) {
                            textureColor = colorPicker.getValue();
                            //curTexture = new ColorIntARGB[][]{{new ColorIntARGB(textureColor)}};
                            currModel.setTextureARGB(new ColorIntARGB[][]{{new ColorIntARGB(textureColor)}});
                            return dialog.getResult();
                        }
                        return null;
                    });

                    // Открываем диалоговое окно
                    dialog.showAndWait().ifPresent(selectedColor -> {
                    });

                });

                // Add the CheckBox and Button elements to the dialog content
                dialogContent.getChildren().addAll(polygonalMeshCheckBox, showTextureCheckBox, antiAliasingCheckBox, loadTextureBtn,
                        chooseColorToFillBtn);

                // Set the content of the dialog
                optionsDialog.getDialogPane().setContent(dialogContent);


                // Add buttons to the dialog pane
                optionsDialog.getDialogPane().getButtonTypes().addAll(
                        ButtonType.CLOSE);

                optionsDialog.setResultConverter(buttonType -> {
                    return null;
                });

                // Show the dialog
                optionsDialog.showAndWait();
            });

            isActive.setCellValueFactory(cellData -> new SimpleObjectProperty<>(checkBox1));
            isEditable.setCellValueFactory(cellData -> new SimpleObjectProperty<>(checkBox2));
            displayOptions.setCellValueFactory(cellData -> new SimpleObjectProperty<>(displayOptionsBtn));
            checkBox1.setOnAction(event -> {
                // Если checkBox1 выбран, активируем checkBox2, иначе деактивируем
                checkBox2.setDisable(!checkBox1.isSelected());
                if (!checkBox1.isSelected()) {
                    checkBox2.setSelected(false);
                    scene.getActiveModels().remove(checkBox1.getId());
                    scene.getEditableModels().remove(checkBox2.getId());
                } else {
                    scene.addActiveModel(checkBox1.getId());
                }
            });

            checkBox2.setOnAction(event -> {
                if (checkBox2.isSelected()) {
                    scene.addEditableModel(checkBox2.getId());
                } else {
                    scene.getEditableModels().remove(checkBox2.getId());
                }
            });


            final ObservableList<LoadedModel> data = modelsTable.getItems();
            data.add(newModel);
            scene.addModel(newModel);
        } catch (IOException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong file format. Couldn't read model", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void onClickShowHide() {
        transformationsPane.setVisible(!transformationsPane.isVisible());
        transformationsPane.setManaged(!transformationsPane.isVisible());
    }

    @FXML
    private void onClickShowHideModels() {
        modelsTable.setVisible(!modelsTable.isVisible());
    }

    @FXML
    private void onClickShowHideOptions() {
        displayPane.setVisible(!displayPane.isVisible());
    }

    @FXML
    private void onSaveModelMenuItemClick() {


        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setInitialDirectory(new File("./3DModels"));
        File file = fileChooser.showOpenDialog((Stage) imageView.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            ObservableList<LoadedModel> selectedModels = modelsTable.getSelectionModel().getSelectedItems();
            if(selectedModels.size() == 0) return;
            Model res = Model.mergeModels(selectedModels.stream().
                    map(loadedModel -> {
                        Matrix4f rotateMatrix = new AffineBuilder().rotateX(loadedModel.getRotateV().getX()).rotateY(loadedModel.getRotateV().getY()).rotateZ(loadedModel.getRotateV().getZ()).build().getMatrix();

                        Matrix4f modelMatrix = GraphicConveyor.rotateScaleTranslate(loadedModel.getScaleV(), loadedModel.getRotateV(),
                                loadedModel.getTranslateV());
                        return GraphicConveyor.multiplyModelByAffineMatrix(loadedModel.getModel(), modelMatrix, rotateMatrix);
                    }).
                    toArray(Model[]::new));
            ObjWriter.write(res, file.getPath());
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't write into the file", ButtonType.OK);
            alert.showAndWait();
            exception.printStackTrace();
        }
    }

    @FXML
    private void onClickScale() {
        try {
            float x = Float.parseFloat(scaleXTextField.getText());
            float y = Float.parseFloat(scaleYTextField.getText());
            float z = Float.parseFloat(scaleZTextField.getText());

            for (LoadedModel lm : scene.getModels()) {

                if (scene.containsEditable(lm.getId())) {
                    lm.getScaleV().setX(lm.getScaleV().getX() * x);
                    lm.getScaleV().setY(lm.getScaleV().getY() * y);
                    lm.getScaleV().setZ(lm.getScaleV().getZ() * z);
                }
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong number format!", ButtonType.OK);
            alert.showAndWait();
        }

    }

    @FXML
    private void onClickTranslate() {
        try{
            float x = Float.parseFloat(translateXTextField.getText());
            float y = Float.parseFloat(translateYTextField.getText());
            float z = Float.parseFloat(translateZTextField.getText());

            for (LoadedModel lm : scene.getModels()) {
                if (scene.containsEditable(lm.getId())) {
                    lm.setTranslateV(lm.getTranslateV().add(new Vector3f(x, y, z)));
                }
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong number format!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void onClickRotate() {
        try {
            float x = (float) Math.toRadians(Float.parseFloat(rotateXTextField.getText()));
            float y = (float) Math.toRadians(Float.parseFloat(rotateYTextField.getText()));
            float z = (float) Math.toRadians(Float.parseFloat(rotateZTextField.getText()));

            for (LoadedModel lm : scene.getModels()) {

                if (scene.containsEditable(lm.getId())) {
                    lm.setRotateV(lm.getRotateV().add(new Vector3f(x, y, z)));
                }
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong number format!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteSelectedModels() {
        ObservableList<LoadedModel> selectedModels = modelsTable.getSelectionModel().getSelectedItems();
        // Удаляем выделенные модели из сцены и из таблицы
        scene.getModels().removeAll(selectedModels);
        modelsTable.getItems().removeAll(selectedModels);

    }

    @FXML
    private void onClickChooseColor() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(javafx.scene.paint.Color.WHITE); // Устанавливаем начальный цвет

        // Создаем диалоговое окно с цветовым пикером
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Choose Color");
        dialog.getDialogPane().setContent(colorPicker);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Обработка нажатия кнопки OK
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return null;
            }
            return null;
        });

        // Открываем диалоговое окно
        dialog.showAndWait().ifPresent(selectedColor -> {
            // Выбранный цвет доступен в переменной selectedColor
            //System.out.println("Selected Color: " + selectedColor);
        });
    }

    @FXML
    private void onClickToggleMesh() {
        if (polygonalMesh.isSelected()) {
            ObservableList<LoadedModel> selectedModels = modelsTable.getSelectionModel().getSelectedItems();
            scene.getModels().removeAll(selectedModels);
        }
    }

    @FXML
    private void onClickSmooth() {
        ObservableList<LoadedModel> selectedModels = modelsTable.getSelectionModel().getSelectedItems();
        for (LoadedModel lm : selectedModels) {
            lm.setDisableSmoothing(!antialiasingBox.isSelected());
        }
    }

    private void drawCoordinateSystem() {
        double AXIS_END_RADIUS = 4;
        double LETTER_HALF_HEIGHT = 5;
        double LETTER_HALF_WIDTH = 3;

        coordinateSystemCanvas.getGraphicsContext2D().setFill(Color.WHITE);
        coordinateSystemCanvas.getGraphicsContext2D().fillOval(0, 0, coordinateSystemCanvas.getWidth(), coordinateSystemCanvas.getHeight());

        Vector3f x = new Vector3f(0.15f, 0, 0);
        Vector3f y = new Vector3f(0, 0.15f, 0);
        Vector3f z = new Vector3f(0, 0, 0.15f);

        Matrix4f viewProjectionMatrix = GraphicConveyor.perspective(1, 1, 0.01f, 10).
                mul(GraphicConveyor.lookAt(new Vector3f(scene.getActiveCamera().getPosition()).normalized(), new Vector3f(0, 0, 0)));

        Vector3f xp = GraphicConveyor.multiplyMVPMatrixByVertex(viewProjectionMatrix, x);
        Vector3f yp = GraphicConveyor.multiplyMVPMatrixByVertex(viewProjectionMatrix, y);
        Vector3f zp = GraphicConveyor.multiplyMVPMatrixByVertex(viewProjectionMatrix, z);

        Point2f xLetterP = GraphicConveyor.vertexToPoint(Vector3f.mul(xp, 1.4f), (int) coordinateSystemCanvas.getWidth(), (int) coordinateSystemCanvas.getHeight());
        Point2f yLetterP = GraphicConveyor.vertexToPoint(Vector3f.mul(yp, 1.4f), (int) coordinateSystemCanvas.getWidth(), (int) coordinateSystemCanvas.getHeight());
        Point2f zLetterP = GraphicConveyor.vertexToPoint(Vector3f.mul(zp, 1.4f), (int) coordinateSystemCanvas.getWidth(), (int) coordinateSystemCanvas.getHeight());

        Point2f xpOnScreen = GraphicConveyor.vertexToPoint(xp, (int) coordinateSystemCanvas.getWidth(), (int) coordinateSystemCanvas.getHeight());
        Point2f ypOnScreen = GraphicConveyor.vertexToPoint(yp, (int) coordinateSystemCanvas.getWidth(), (int) coordinateSystemCanvas.getHeight());
        Point2f zpOnScreen = GraphicConveyor.vertexToPoint(zp, (int) coordinateSystemCanvas.getWidth(), (int) coordinateSystemCanvas.getHeight());

        double xC = coordinateSystemCanvas.getWidth() / 2;
        double yC = coordinateSystemCanvas.getHeight() / 2;

        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(xpOnScreen.getX(), xpOnScreen.getY(), xC, yC);
        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(ypOnScreen.getX(), ypOnScreen.getY(), xC, yC);
        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(zpOnScreen.getX(), zpOnScreen.getY(), xC, yC);

        coordinateSystemCanvas.getGraphicsContext2D().setFill(Color.BLACK);
        coordinateSystemCanvas.getGraphicsContext2D().fillOval(xpOnScreen.getX() - AXIS_END_RADIUS, xpOnScreen.getY() - AXIS_END_RADIUS, AXIS_END_RADIUS * 2, AXIS_END_RADIUS * 2);
        coordinateSystemCanvas.getGraphicsContext2D().fillOval(ypOnScreen.getX() - AXIS_END_RADIUS, ypOnScreen.getY() - AXIS_END_RADIUS, AXIS_END_RADIUS * 2, AXIS_END_RADIUS * 2);
        coordinateSystemCanvas.getGraphicsContext2D().fillOval(zpOnScreen.getX() - AXIS_END_RADIUS, zpOnScreen.getY() - AXIS_END_RADIUS, AXIS_END_RADIUS * 2, AXIS_END_RADIUS * 2);

        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(xLetterP.getX() - LETTER_HALF_WIDTH, xLetterP.getY() - LETTER_HALF_HEIGHT, xLetterP.getX() + LETTER_HALF_WIDTH, xLetterP.getY() + LETTER_HALF_HEIGHT);
        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(xLetterP.getX() + LETTER_HALF_WIDTH, xLetterP.getY() - LETTER_HALF_HEIGHT, xLetterP.getX() - LETTER_HALF_WIDTH, xLetterP.getY() + LETTER_HALF_HEIGHT);

        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(yLetterP.getX() - LETTER_HALF_WIDTH, yLetterP.getY() - LETTER_HALF_HEIGHT, yLetterP.getX(), yLetterP.getY());
        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(yLetterP.getX() + LETTER_HALF_WIDTH, yLetterP.getY() - LETTER_HALF_HEIGHT, yLetterP.getX() - LETTER_HALF_WIDTH, yLetterP.getY() + LETTER_HALF_HEIGHT);

        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(zLetterP.getX() - LETTER_HALF_WIDTH, zLetterP.getY() - LETTER_HALF_HEIGHT, zLetterP.getX() + LETTER_HALF_WIDTH, zLetterP.getY() - LETTER_HALF_HEIGHT);
        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(zLetterP.getX() - LETTER_HALF_WIDTH, zLetterP.getY() + LETTER_HALF_HEIGHT, zLetterP.getX() + LETTER_HALF_WIDTH, zLetterP.getY() + LETTER_HALF_HEIGHT);
        coordinateSystemCanvas.getGraphicsContext2D().strokeLine(zLetterP.getX() + LETTER_HALF_WIDTH, zLetterP.getY() - LETTER_HALF_HEIGHT, zLetterP.getX() - LETTER_HALF_WIDTH, zLetterP.getY() + LETTER_HALF_HEIGHT);

    }
}