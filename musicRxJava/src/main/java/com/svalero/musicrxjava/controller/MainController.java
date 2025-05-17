package com.svalero.musicrxjava.controller;

import com.svalero.musicrxjava.constants.Constants;
import com.svalero.musicrxjava.domain.Album;
import com.svalero.musicrxjava.domain.Artist;
import com.svalero.musicrxjava.task.GetAlbumsByIdArtist;
import com.svalero.musicrxjava.task.GetAllArtistsTask;
import com.svalero.musicrxjava.utils.AutoResizeColumns;
import com.svalero.musicrxjava.utils.DateUtil;
import com.svalero.musicrxjava.utils.ErrorLogger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TabPane tabPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Artist> observableData = FXCollections.observableArrayList();
        // Create new table
        TableView<Artist> tableView = createArtistTableView();
        tableView.setItems(observableData);
        // Create new tab
        Tab tab = createNewTab(tableView, observableData);
        //Init task
        GetAllArtistsTask task = new GetAllArtistsTask(observableData);
        //Init thread
        Thread thread = new Thread(task);
        // Control states of task
        controlStatesArtistTask(task, tableView, tab);
        //Close thread if app closes
        thread.setDaemon(true);
        thread.start();
    }

    private void controlStatesArtistTask(
            GetAllArtistsTask task,
            TableView<Artist> tableView,
            Tab tab
    ){
        task.setOnSucceeded(event -> {
            tab.setText(Constants.LABEL_ARTIST + " ✅");
            AutoResizeColumns.autoResizeColumns(tableView);
        });
        //Task ends before complete sending
        task.setOnFailed(event -> {
            ErrorLogger.log("Failed listing artists");
            tab.setText(Constants.LABEL_ARTIST + " ⛔");
        });
    }

    private void controlStatesAlbumTask(GetAlbumsByIdArtist task, Artist clickedArtist, ObservableList<Album> observableAlbums){
        task.setOnSucceeded(event -> {
            // New widow to show detail data
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.NONE);
            popupStage.setTitle("Albums");

            VBox content = new VBox(10);
            content.setPadding(new Insets(15));
            Image image;
            try {
                String imageName = clickedArtist.getImage();
                System.out.println("Artist image: " + imageName);

                if (imageName == null || imageName.isEmpty()) {
                    imageName = "default.png";
                }

                String path = "/com/svalero/musicrxjava/assets/" + imageName;
                InputStream is = getClass().getResourceAsStream(path);

                if (is == null) {
                    is = getClass().getResourceAsStream("/com/svalero/musicrxjava/assets/default.png");
                }
                image = new Image(is);

            } catch (Exception e) {
                e.printStackTrace();
                image = new Image(getClass().getResourceAsStream("/com/svalero/musicrxjava/assets/default.png"));
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);

            // Initial empty grid
            ScrollPane scrollPane = createAlbumGrid(observableAlbums);
            content.getChildren().addAll(imageView, new Label(clickedArtist.getName()), scrollPane);

            Scene popupScene = new Scene(content, 600, 600);
            popupStage.setScene(popupScene);
            popupStage.show();

            observableAlbums.addListener((javafx.collections.ListChangeListener<Album>) change -> {
                ScrollPane newScrollPane = createAlbumGrid(observableAlbums);
                content.getChildren().set(2, newScrollPane); // Reemplaza el anterior
            });
        });
        //Task ends before complete sending
        task.setOnFailed(event -> {
            Throwable error = task.getException();
            if (error != null) {
                error.printStackTrace();
                ErrorLogger.log("Task failed: " + error.toString());
            } else {
                System.out.println("Task failed with unknown error");
                ErrorLogger.log("Task failed with unknown error");
            }
        });
    }

    private Tab createNewTab(TableView<Artist> tableView, ObservableList<Artist> data) {
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(0, 0, 10, 10));

        Tab tab = new Tab(Constants.LABEL_ARTIST + " ⌛");

        VBox container = new VBox(10, scrollPane);

        // Set components attributes to growth to expand all height
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        tableView.setPrefHeight(Region.USE_COMPUTED_SIZE);
        //Add new tab
        tab.setContent(container);
        tabPane.getTabs().add(tab);
        // Focus on new tab
        tabPane.getSelectionModel().select(tab);
        return tab;
    }

    private TableView<Artist> createArtistTableView(){
        TableView<Artist> tableView = new TableView<>();

        TableColumn<Artist, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));

        TableColumn<Artist, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));

        TableColumn<Artist, String> regDateColumn = new TableColumn<>("Registration date");
        regDateColumn.setCellValueFactory(cellData -> {
            String formattedDate = DateUtil.formatFromString(cellData.getValue().getRegistrationDate(), "dd/MM/yyyy", "yyyy-MM-dd");
            return new ReadOnlyStringWrapper(formattedDate);
        });

        TableColumn<Artist, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCountry()));

        TableColumn<Artist, String> soloistColumn = new TableColumn<>("Formation");
        soloistColumn.setCellValueFactory(cellData -> {
            String formation = cellData.getValue().isSoloist()
                    ? "Soloist"
                    : "Group";
            return new ReadOnlyStringWrapper(formation);
        });

        //Set columns in table
        tableView.getColumns().add(idColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(soloistColumn);
        tableView.getColumns().add(countryColumn);
        tableView.getColumns().add(regDateColumn);

        addEventListenerRows(tableView);

        return tableView;
    }

    private void addEventListenerRows(TableView<Artist> tableView){
        // Add row click listener to show popup
        tableView.setRowFactory(tv -> {
            TableRow<Artist> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) { //Simple click, 2 to double-click
                    Artist clickedArtist = row.getItem();
                    ObservableList<Album> observableAlbums = FXCollections.observableArrayList();
                    //Init task
                    GetAlbumsByIdArtist task = new GetAlbumsByIdArtist(observableAlbums, clickedArtist.getId());
                    //Init thread
                    Thread thread = new Thread(task);
                    // Control states of task
                    controlStatesAlbumTask(task, clickedArtist, observableAlbums);
                    //Close thread if app closes
                    thread.setDaemon(true);
                    thread.start();
                }
            });
            return row;
        });
    }

    private ScrollPane createAlbumGrid(ObservableList<Album> albums) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10));

        int column = 0;
        int row = 0;

        for (Album album : albums) {
            VBox albumBox = createAlbumBox(album);
            grid.add(albumBox, column, row);

            column++;
            if (column > 2) {
                column = 0;
                row++;
            }
        }

        return new ScrollPane(grid);
    }

    private VBox createAlbumBox(Album album) {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(5));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(150);

        //Image album
        Image image;
        try {
            String imageName = album.getFront();
            System.out.println("Album image: " + imageName);

            if (imageName == null || imageName.isEmpty()) {
                imageName = "default.png";
            }

            String path = "/com/svalero/musicrxjava/assets/" + imageName;
            InputStream is = getClass().getResourceAsStream(path);

            if (is == null) {
                is = getClass().getResourceAsStream("/com/svalero/musicrxjava/assets/default.png");
            }

            image = new Image(is);

        } catch (Exception e) {
            e.printStackTrace();
            image = new Image(getClass().getResourceAsStream("/com/svalero/musicrxjava/assets/default.png"));
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);

        Label titleLabel = new Label(album.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold;");

        String formattedDate = DateUtil.formatFromString(album.getReleaseDate(), "dd/MM/yyyy", "yyyy-MM-dd");
        Label dateLabel = new Label(formattedDate);

        vbox.getChildren().addAll(imageView, titleLabel, dateLabel);
        vbox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        return vbox;
    }



}
