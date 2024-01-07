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
import ru.vsu.cs.team4.task4.affine.affineComposite.RotateCustom;
import ru.vsu.cs.team4.task4.affine.affineComposite.RotateY;
import ru.vsu.cs.team4.task4.math.vector.Vector2f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.Model;
import ru.vsu.cs.team4.task4.objio.ObjReader;
import ru.vsu.cs.team4.task4.rasterization.ColorIntARGB;
import ru.vsu.cs.team4.task4.render_engine.Camera;
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

    private Color textureColor = Color.WHITE;

    private ColorIntARGB[][] curTexture = new ColorIntARGB[][]{{new ColorIntARGB(255, 255,255,255)}};


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
            /*newModel.setIsActive(checkBox1);*/

            CheckBox checkBox2 = new CheckBox();
            checkBox2.setId(newModel.getId());
            checkBox2.setDisable(true);
            /*newModel.setIsEditable(checkBox2);*/

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
                CheckBox checkBox5 = new CheckBox("Polygonal Mesh");

                checkBox5.setSelected(!currModel.getDisableMesh());
                checkBox5.setOnAction(eventMesh -> {
                    currModel.setDisableMesh(!checkBox5.isSelected());
                });

                CheckBox checkBox6 = new CheckBox("Show Texture");

                checkBox6.setSelected(!currModel.isDisableTexture());
                checkBox6.setOnAction(eventTexture -> {
                    currModel.setDisableTexture(!checkBox6.isSelected());
                    if (!currModel.isDisableTexture()) {
                        currModel.setTextureARGB(curTexture);
                    } else {
                       currModel.setTextureARGB(new ColorIntARGB[][]{{new ColorIntARGB(255, 255, 255, 255)}});
                    }

                });


                CheckBox checkBox7 = new CheckBox("Antialiasing");
                checkBox7.setSelected(!currModel.isDisableSmoothing());
                checkBox7.setOnAction(eventAntialiasing -> {
                    currModel.setDisableSmoothing(!checkBox7.isSelected());
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
                        throw new RuntimeException(e);
                    }
                    ColorIntARGB[][] texture = new ColorIntARGB[image.getHeight()][image.getWidth()];
                    for (int i = 0; i < image.getHeight(); i++) {
                        for (int j = 0; j < image.getWidth(); j++) {
                            texture[j][i] = new ColorIntARGB(255 << 24 | image.getRGB(j,image.getHeight() - 1 - i));
                        }
                    }
                    curTexture = texture;
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
                            curTexture = new ColorIntARGB[][]{{new ColorIntARGB(textureColor)}};
                            return dialog.getResult();
                        }
                        return null;
                    });

                    // Открываем диалоговое окно
                    dialog.showAndWait().ifPresent(selectedColor -> {
                    });

                });

                // Add the CheckBox and Button elements to the dialog content
                dialogContent.getChildren().addAll(checkBox5, checkBox6, checkBox7, loadTextureBtn,
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
            System.out.println("Wrong arguments");
        }
    }

    @FXML
    private void onClickShowHide() {
        transformationsPane.setVisible(!transformationsPane.isVisible());
    }

    @FXML
    private void onClickDeleteModel() {

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

    }

    @FXML
    private void onClickScale() {
        float x = Float.parseFloat(scaleX.getText());
        float y = Float.parseFloat(scaleY.getText());
        float z = Float.parseFloat(scaleZ.getText());

        for (LoadedModel lm : scene.getModels()) {

            if (scene.containsEditable(lm.getId())) {
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
            if (scene.containsEditable(lm.getId())) {
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

            if (scene.containsEditable(lm.getId())) {

         

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
            System.out.println("Selected Color: " + selectedColor);
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
}