package com.example.projekpbobd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class BarangController {
    @FXML
    private Button back;
    @FXML
    private Button save;
    @FXML
    private Button delete;
    @FXML
    private Button update;


    @FXML
    public void back(ActionEvent event){
        Stage stage = (Stage) back.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/main_view.fxml"));
            Scene scene = new Scene(root);
            HashMap<String, String> data = new HashMap<>();
            data.put("mode", "add");
            scene.setUserData(data);
            stage.setScene(scene);
            stage.show();
            System.out.println("MAIN VIEW(Penjualan)");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void save(ActionEvent event){

    }

    @FXML
    public void delete(ActionEvent event){

    }

}
