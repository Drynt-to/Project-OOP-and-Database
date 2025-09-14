package com.example.projekpbobd.controllers;

import com.example.projekpbobd.beans.Mahasiswa;
import com.example.projekpbobd.beans.Pegawai;
import com.example.projekpbobd.dao.PegawaiDAO;
import com.example.projekpbobd.util.ConnectionManager;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class PegawaiController implements Initializable {
    @FXML
    private TableView<Pegawai> tblPegawai;
    private ObservableList<Pegawai> listPegawai;
    @FXML
    private TextField txtNamaDepan;
    @FXML
    private TextField txtNamaBelakang;
    @FXML
    private TextField txtNoTelp;
    @FXML
    private TextField idStaff;
    @FXML
    private TextField searchBar;

    @FXML
    private Button back;
    @FXML
    private Button save;
    @FXML
    private Button delete;
    @FXML
    private Button update;
    @FXML
    private Button join;
    @FXML
    private Pane frmPane;

    private HashMap<String, String> parameters = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listPegawai = FXCollections.observableArrayList();
        tblPegawai.setItems(listPegawai);
        setupTable();
        showPegawai();
    }

    private void setupTable() {
        TableColumn<Pegawai, Integer> idCol = new TableColumn<>("Id");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Pegawai, String> namaCol = new TableColumn<>("Nama Depan");
        namaCol.setMinWidth(100);
        namaCol.setCellValueFactory(new PropertyValueFactory<>("namaDepan"));

        TableColumn<Pegawai, String> namaBelakangCol = new TableColumn<>("Nama Belakang");
        namaBelakangCol.setMinWidth(100);
        namaBelakangCol.setCellValueFactory(new PropertyValueFactory<>("namaBelakang"));

        TableColumn<Pegawai, String> noTelepon = new TableColumn<>("No Telepon");
        noTelepon.setMinWidth(150);
        noTelepon.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));

        TableColumn<Pegawai, String> idStaff = new TableColumn<>("Id Staff Koperasi");
        idStaff.setMinWidth(150);
        idStaff.setCellValueFactory(new PropertyValueFactory<>("idStaffKoperasi"));

        tblPegawai.getColumns().clear();
        tblPegawai.getColumns().addAll(idCol, namaCol, namaBelakangCol, noTelepon, idStaff);

    }

    public void showPegawai() {
        listPegawai.clear();
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            listPegawai.addAll(PegawaiDAO.getAll(con));
            tblPegawai.setItems(listPegawai);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.closeConnection(con);
        }
    }

    private void refreshData() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            listPegawai = FXCollections.observableArrayList(PegawaiDAO.getAll(connection));
            tblPegawai.setItems(listPegawai);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(connection);
        }
    }

    @FXML
    public void save(ActionEvent event) {
        parameters = (HashMap<String, String>) frmPane.getScene().getWindow().getScene().getUserData();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            Pegawai pegawai = new Pegawai();
            pegawai.setNamaDepan(txtNamaDepan.getText());
            pegawai.setNamaBelakang(txtNamaBelakang.getText());
            pegawai.setNoTelepon(txtNoTelp.getText());
            pegawai.setIdStaffKoperasi(Integer.parseInt(idStaff.getText()));
            System.out.println(pegawai);
            PegawaiDAO.save(con, pegawai);
            listPegawai.add(pegawai);
            tblPegawai.refresh();
            refreshData();
            refreshData();
            dialog.setContentText("Data berhasil ditambahkan !");
        } catch (SQLException e) {
            dialog.setContentText("Terjadi kesalahan: " + e.getMessage());
        } finally {
            ConnectionManager.closeConnection(con);
        }
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.showAndWait();
    }

    @FXML
    public void update() {
        Pegawai selectedPegawai = tblPegawai.getSelectionModel().getSelectedItem();
        if (selectedPegawai != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Pegawai");
            dialog.setHeaderText("Update Data Pegawai");

            TextField newNameField = new TextField();
            newNameField.setPromptText("Masukkan Nama Depan Baru");
            TextField newNamaBelakangField = new TextField();
            newNamaBelakangField.setPromptText("Masukkan Nama Belakang Baru");
            TextField newNoTelpField = new TextField();
            newNoTelpField.setPromptText("Masukkan No Telpon Baru");

            VBox content = new VBox();
            content.getChildren().addAll(newNameField, newNamaBelakangField, newNoTelpField);
            dialog.getDialogPane().setContent(content);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButton) {
                    if (isValidInput(newNameField.getText(), newNamaBelakangField.getText(),newNoTelpField.getText())) {
                        selectedPegawai.setNamaDepan(newNameField.getText());
                        selectedPegawai.setNamaBelakang(newNamaBelakangField.getText());
                        selectedPegawai.setNoTelepon(newNoTelpField.getText());

                        Connection con = null;
                        try {
                            con = ConnectionManager.getConnection();
                            PegawaiDAO.update(con, selectedPegawai);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionManager.closeConnection(con);
                        }
                        tblPegawai.refresh();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Invalid");
                        alert.setHeaderText("Input yang dimasukkan tidak valid");
                        alert.setContentText("Mohon pastikan semua field telah diisi dengan benar.");
                        alert.showAndWait();
                    }
                }
                return null;
            });
            dialog.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tidak Ada Pegawai yang Dipilih");
            alert.setHeaderText("Tidak ada data pegawai yang dipilih");
            alert.setContentText("Silakan pilih data pegawai yang ingin diubah.");
            alert.showAndWait();
        }
    }

    // Metode untuk memvalidasi input
    private boolean isValidInput(String nama, String namaBelakang, String noTelp) {
        return !nama.isEmpty() && !namaBelakang.isEmpty() && !noTelp.isEmpty();
    }


    @FXML
    public void delete(ActionEvent event) {
        if (tblPegawai.getSelectionModel().getSelectedItems().size() != 0) {
            Pegawai selected = (Pegawai) tblPegawai.getSelectionModel().getSelectedItem();
            Connection connection = null;
            try {
                connection = ConnectionManager.getConnection();
                PegawaiDAO.delete(connection, selected);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Data berhasil dihapus !");
                alert.show();

                refreshData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                ConnectionManager.closeConnection(connection);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Tidak ada data yang dipilih !");
            alert.show();
        }
    }
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
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void join(ActionEvent event){
        Stage stage = (Stage) join.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/staffKoperasi3.fxml"));
            Scene scene = new Scene(root);
            HashMap<String, String> data = new HashMap<>();
            data.put("mode", "add");
            scene.setUserData(data);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void search(ActionEvent event) {
        String query = searchBar.getText().toLowerCase();
        if (query.isEmpty()) {
            tblPegawai.setItems(FXCollections.observableArrayList(listPegawai)); // Show all data
        } else {
            ObservableList<Pegawai> filteredData = FXCollections.observableArrayList();
            for (Pegawai pegawai : listPegawai) {
                if (pegawai.getNamaDepan().toLowerCase().contains(query) ||
                        pegawai.getNamaBelakang().toLowerCase().contains(query) ||
                        String.valueOf(pegawai.getId()).toLowerCase().contains(query) ||
                        String.valueOf(pegawai.getNoTelepon()).toLowerCase().contains(query) ||
                        String.valueOf(pegawai.getIdStaffKoperasi()).toLowerCase().contains(query)) {
                    filteredData.add(pegawai);

                }
            }
            tblPegawai.setItems(filteredData);
        }
    }


    @FXML
    public void export() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Microsoft Excel Spreadsheet (*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Portable Document Format files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(pdfFilter);
        chooser.getExtensionFilters().add(excelFilter);

        chooser.setInitialDirectory(new File("C:\\Users"));
        File file = chooser.showSaveDialog(tblPegawai.getScene().getWindow());
        FileChooser.ExtensionFilter selectedFilter = chooser.getSelectedExtensionFilter();

        if (file != null) {
            if (selectedFilter.getExtensions().get(0).equalsIgnoreCase("*.xlsx")) {
                exportToExcel(file);
            } else if (selectedFilter.getExtensions().get(0).equalsIgnoreCase("*.pdf")) {
                exportToPdf(file);
            }
        }
    }


    private void exportToPdf(File file) {
        System.out.println(file.getAbsolutePath());
        PdfDocument pdfDoc = null;
        try {
            pdfDoc = new PdfDocument(new PdfWriter(file.getAbsolutePath()));
            Document doc = new Document(pdfDoc);

            Table table = new Table(UnitValue.createPercentArray(new float[] {20, 40, 80, 50, 70, 50})).useAllAvailableWidth();
            //Logo header
            Image logo = new Image(ImageDataFactory.create("src/main/resources/com/example/projekpbobd/images/newlogoukb.png"));
            logo.setWidth(UnitValue.createPercentValue(100));
            com.itextpdf.layout.element.Cell logoCell = new Cell(1, 2).add(logo);
            logoCell.setBorder(Border.NO_BORDER);
            table.addCell(logoCell);

            com.itextpdf.layout.element.Cell emptyCell = new com.itextpdf.layout.element.Cell(1, 4);
            emptyCell.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell);

            //Header Table
            for (int i = 0; i< tblPegawai.getColumns().size(); i++) {
                TableColumn col = tblPegawai.getColumns().get(i);
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
                Paragraph title = new Paragraph(col.getText());
                title.setTextAlignment(TextAlignment.CENTER);
                title.setBold();

                headerCell.add(title);
                table.addCell(headerCell);
            }
            Cell emptyCell2 = new Cell(1, 4);
            emptyCell2.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell2);
            //Table Data
            int total = 0;
            for (int i = 0; i< tblPegawai.getItems().size(); i++) {
                Pegawai data = (Pegawai) tblPegawai.getItems().get(i);

                //Data id
                Paragraph idParagraph = new Paragraph(String.valueOf(data.getId()));
                idParagraph.setTextAlignment(TextAlignment.CENTER);
                com.itextpdf.layout.element.Cell idCell = new com.itextpdf.layout.element.Cell().add(idParagraph);
                table.addCell(idCell);

//
                //Data nama
                Paragraph namaDepan = new Paragraph(String.valueOf(data.getNamaDepan()));
                com.itextpdf.layout.element.Cell namaDepanCell = new com.itextpdf.layout.element.Cell().add(namaDepan);
                table.addCell(namaDepanCell);

                //Data nama
                Paragraph namaBelakang = new Paragraph(String.valueOf(data.getNamaBelakang()));
                com.itextpdf.layout.element.Cell namaBelakangCell = new com.itextpdf.layout.element.Cell().add(namaBelakang);
                table.addCell(namaBelakangCell);

                //Data
                Paragraph noTelp = new Paragraph(data.getNoTelepon());
                com.itextpdf.layout.element.Cell noTelpCell = new com.itextpdf.layout.element.Cell().add(noTelp);
                table.addCell(noTelpCell);

                Paragraph idStaff = new Paragraph(String.valueOf(data.getIdStaffKoperasi()));
                com.itextpdf.layout.element.Cell idStaffCell = new com.itextpdf.layout.element.Cell().add(idStaff);
                table.addCell(idStaffCell);

                emptyCell2.setBorder(Border.NO_BORDER);
                table.addCell(emptyCell2);
            }

            doc.add(table);
            doc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void exportToExcel(File file) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Data Pegawai");

        FileOutputStream out = null;
        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            int rowid = 0;

            //Export Header
            XSSFRow headerRow = spreadsheet.createRow(rowid++);
            Object[] headerArr = tblPegawai.getColumns().toArray();

            int cellCounter = 0;
            for (Object obj : headerArr) {
                XSSFCell cell = headerRow.createCell(cellCounter++);
                cell.setCellValue(((TableColumn) obj).getText());
            }

            //Export Data
            Map<String, Object[]> data = PegawaiDAO.getAllArrayObject(con);
            Set<String> keyid = data.keySet();

            for (String key : keyid) {
                XSSFRow row = spreadsheet.createRow(rowid++);
                Object[] objectArr = data.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    XSSFCell cell = row.createCell(cellid++);
                    cell.setCellValue(String.valueOf(obj));
                }
            }
            out = new FileOutputStream(file);
            workbook.write(out);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(con);
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
