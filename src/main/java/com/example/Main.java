package com.example;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private TextArea outputArea;
    private File selectedFile;
    int[] freq = new int[256];//frequency for each byte
    MinHeap heap = new MinHeap();
    Map<Byte , String> encodingTable = new HashMap<>();
    File compressedFile = null;

    public void getFrequencies(File file) throws IOException {

        try (FileInputStream in = new FileInputStream(file)) {
            int read;
            while ((read = in.read()) != -1) {
                byte b = (byte) read; //may be negative value
                freq[b & 0xFF]++;     //to gurantee the value in [0,255]
            }
        }
    }

    void buildCodeDFS(Node node, String code) {
        if (node == null) return;

        if (node.isLeaf()) {
            encodingTable.put(node.b, code);
            return;
        }

        buildCodeDFS(node.left, code + "0");
        buildCodeDFS(node.right, code + "1");
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Huffman Compressor / Decompressor");

        // setting up
        Label titleLabel = new Label("Huffman Coding Tool");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.DARKGREEN);

        // choosing file:
        Button chooseFileBtn = new Button("\uD83D\uDCC2 Choose File");
        Label fileLabel = new Label("No file selected.");
        chooseFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file to Compress");
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                fileLabel.setText("Selected: " + selectedFile.getName());
            }
        });

        HBox fileBox = new HBox(10, chooseFileBtn, fileLabel);
        fileBox.setAlignment(Pos.CENTER_LEFT);

        //  compress and decompress:
        Button compressBtn = new Button("Compress");
        Button decompressBtn = new Button("Decompress");

        compressBtn.setOnAction(e -> {
            outputArea.clear();
            if (selectedFile == null) {
                error("please choose a file first");
                return ;

            }

            try {
                getFrequencies(selectedFile);

                //build huffman coding tree based on heap
                for (int i = 0; i < 256; i++) {
                    if (freq[i] > 0) {//build node for each existing byte
                        Node node = new Node((byte) i, freq[i]);
                        heap.insert(node);
                    }
                }

                //build binary tree
                while (heap.size() > 1) {
                    Node n1 = heap.extractMin();
                    Node n2 = heap.extractMin();
                    Node parent = new Node(null, n1.freq + n2.freq, n1, n2);
                    heap.insert(parent);
                }

                Node huffmanRoot = heap.extractMin();
                //distribue codes
                buildCodeDFS(huffmanRoot, "");

                //compress file
                createCompressedFile();




            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        decompressBtn.setOnAction(e -> {
            outputArea.clear();
            if (selectedFile == null) {
                error("please choose a file first");
                return ;

            }
            outputArea.appendText("Decompressing file...\n");
            // Call your decompress logic here
        });

        HBox buttonsBox = new HBox(20, compressBtn, decompressBtn);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(10, 0, 10, 0));

        // output area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setFont(Font.font("Consolas", 14));
        outputArea.setPrefHeight(300);
        outputArea.setWrapText(true);
        outputArea.setStyle("-fx-control-inner-background: #f0f8ff; -fx-border-color: #ccc;");


        VBox root = new VBox(15, titleLabel, fileBox, buttonsBox, outputArea);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #fdfdfd;");

        Scene scene = new Scene(root, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void error(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


    public void createCompressedFile() {
        Stage stage = new Stage();

        stage.setTitle("create new file");

        Label label = new Label("Enter name of file to be compressed");
        TextField fileNameField = new TextField();
        Button createButton = new Button("create");

        Label resultLabel = new Label();

        createButton.setOnAction(e -> {
            String fileName = fileNameField.getText().trim();
            if (!fileName.isEmpty()) {
                try {
                    compressedFile= new File(fileName);
                    compressedFile.createNewFile();
                    success("Cretated successfully");
                    stage.close();

                } catch (IOException ex) {
                    resultLabel.setText("error occured when creating file");
                    ex.printStackTrace();
                }
            } else {
                resultLabel.setText("please enter name of file");
            }
        });

        VBox root = new VBox(10, label, fileNameField, createButton, resultLabel);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 350, 200);
        stage.setScene(scene);
        stage.show();
    }


    private void success(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
