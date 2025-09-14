package com.example.projekpbobd.controllers;

import com.example.projekpbobd.beans.Transaksi;
import com.example.projekpbobd.dao.TransaksiDAO;
import com.example.projekpbobd.util.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TableColumn<Transaksi, Integer> id_transaksi_main;
    @FXML private TableColumn<Transaksi, Date> tanggal_main;
    @FXML private TableColumn<Transaksi, Integer>total_jual_main;
    @FXML private TableColumn<Transaksi, Double> profit_main;
    @FXML private TableColumn<Transaksi, Integer> kuantitas_main;
    @FXML private TableColumn<Transaksi, Integer> staff_koperasi_main;
    @FXML private TableView<Transaksi> transaksi_view;
    @FXML private MenuItem showMahasiswa;
    @FXML private MenuItem showPegawai;
    @FXML private MenuItem showBarang;
    @FXML private MenuItem showStaffKoperasi;
    ObservableList<Transaksi> ObservableLists;

//    private void initTable() {
//        observableList = FXCollections.observableArrayList();
//        id_transaksi_main.setCellValueFactory(new PropertyValueFactory<>("id_transaksi"));
//        tanggal_main.setCellValueFactory(new  PropertyValueFactory<>("tanggal_transaksi"));
//        total_jual_main.setCellValueFactory(new  PropertyValueFactory<>("total_jual"));
//        profit_main.setCellValueFactory(new PropertyValueFactory<>("profit"));
//        kuantitas_main.setCellValueFactory(new  PropertyValueFactory<>("kuantitas"));
//    }

    @FXML private Button save;
    @FXML private Button delete;
    @FXML private Button update;


    @FXML
    public void dataMahasiswa(){
        Stage stage = (Stage) showMahasiswa.getParentPopup().getOwnerWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/dataMahasiswa_view.fxml"));
            Scene scene = new Scene(root);
            HashMap<String, String> data = new HashMap<>();
            data.put("mode", "add");
            scene.setUserData(data);
            stage.setScene(scene);
            stage.show();
            System.out.println("Data Mahasiswa");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void dataPegawai(){
        Stage stage = (Stage) showPegawai.getParentPopup().getOwnerWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/dataPegawai_view.fxml"));
            Scene scene = new Scene(root);
            HashMap<String, String> data = new HashMap<>();
            data.put("mode", "add");
            scene.setUserData(data);
            stage.setScene(scene);
            stage.show();
            System.out.println("Data Pegawai");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void dataBarang(){
        Stage stage = (Stage) showBarang.getParentPopup().getOwnerWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/dataBarang_view.fxml"));
            Scene scene = new Scene(root);
            HashMap<String, String> data = new HashMap<>();
            data.put("mode", "add");
            scene.setUserData(data);
            stage.setScene(scene);
            stage.show();
            System.out.println("Data Barang");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void dataStaffKoperasi(){
        Stage stage = (Stage) showStaffKoperasi.getParentPopup().getOwnerWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/staffKoperasi_view.fxml"));
            Scene scene = new Scene(root);
            HashMap<String, String> data = new HashMap<>();
            data.put("mode", "add");
            scene.setUserData(data);
            stage.setScene(scene);
            stage.show();
            System.out.println("Data Staff Koperasi");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TransaksiDAO transaksiDAO = new TransaksiDAO();
        List<Transaksi> transaksis = transaksiDAO.getAllTransaksi();

        ObservableList<Transaksi> observableTransaksis = FXCollections.observableArrayList(transaksis);


        ObservableLists = FXCollections.observableArrayList(transaksis);
        id_transaksi_main.setCellValueFactory(new PropertyValueFactory<>("id_transaksi"));
        tanggal_main.setCellValueFactory(new  PropertyValueFactory<>("tanggal_transaksi"));
        total_jual_main.setCellValueFactory(new  PropertyValueFactory<>("total_jual"));
        profit_main.setCellValueFactory(new PropertyValueFactory<>("profit"));
        kuantitas_main.setCellValueFactory(new  PropertyValueFactory<>("kuantitas"));
        staff_koperasi_main.setCellValueFactory(new  PropertyValueFactory<>("id_staff_koperasi"));
        transaksi_view.setItems(observableTransaksis);
    }
}