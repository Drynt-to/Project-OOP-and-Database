package com.example.projekpbobd.controllers;

import com.example.projekpbobd.beans.Mahasiswa;
import com.example.projekpbobd.dao.MahasiswaDAO;
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

public class MahasiswaController implements Initializable {
    @FXML
    private TableView<Mahasiswa> tblMahasiswa;
    private ObservableList<Mahasiswa> data;
    @FXML
    private TextField txtNamaDepan;
    @FXML
    private TextField txtNamaBelakang;
    @FXML
    private TextField txtNoTelp;
    @FXML
    private TextField txtIdStaff;
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
        data = FXCollections.observableArrayList();
        tblMahasiswa.setItems(data);

        //Init columns
        TableColumn idCol = new TableColumn("ID");
        idCol.setPrefWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<Mahasiswa, Integer>("id"));

        TableColumn namaDepanCol = new TableColumn("Nama Depan");
        namaDepanCol.setPrefWidth(100);
        namaDepanCol.setCellValueFactory(new PropertyValueFactory<Mahasiswa, String>("namaDepan"));

        TableColumn namaCol = new TableColumn("Nama Belakang");
        namaCol.setPrefWidth(100);
        namaCol.setCellValueFactory(new PropertyValueFactory<Mahasiswa, String>("namaBelakang"));

        TableColumn noTelpCol = new TableColumn("No Telpon");
        noTelpCol.setPrefWidth(150);
        noTelpCol.setCellValueFactory(new PropertyValueFactory<Mahasiswa, Integer>("noTelepon"));
        tblMahasiswa.getColumns().addAll(idCol, namaDepanCol, namaCol, noTelpCol);

        TableColumn idStaffKoperasi = new TableColumn("Id Staff Koperasi");
        idStaffKoperasi.setPrefWidth(150);
        idStaffKoperasi.setCellValueFactory(new PropertyValueFactory<Mahasiswa, Integer>("idStaffKoperasi"));
        tblMahasiswa.getColumns().clear();
        tblMahasiswa.getColumns().addAll(idCol, namaDepanCol, namaCol, noTelpCol, idStaffKoperasi);

        //Inisialisasi data
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            data = FXCollections.observableList(MahasiswaDAO.getAll(con));
            for (Mahasiswa mahasiswa: data) {
                System.out.println(mahasiswa);
            }
            tblMahasiswa.setItems(data);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.closeConnection(con);
        }
        ObservableList<Mahasiswa> obListRegion = FXCollections.observableArrayList();
        obListRegion.addAll(data);
        tblMahasiswa.setItems(obListRegion);
    }


    private void refreshData() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            data = FXCollections.observableArrayList(MahasiswaDAO.getAll(connection));
            tblMahasiswa.setItems(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(connection);
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
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/staffKoperasi2.fxml"));
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
    public void save(ActionEvent event){
        parameters = (HashMap<String, String>) frmPane.getScene().getWindow().getScene().getUserData();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setNamaDepan(txtNamaDepan.getText());
            mahasiswa.setNamaBelakang(txtNamaBelakang.getText());
            mahasiswa.setNoTelepon(txtNoTelp.getText());
            mahasiswa.setIdStaffKoperasi(Integer.parseInt(txtIdStaff.getText()));
            System.out.println(mahasiswa);
            MahasiswaDAO.save(con, mahasiswa);
            data.add(mahasiswa);
            tblMahasiswa.refresh();
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
        Mahasiswa selectedMahasiswa = tblMahasiswa.getSelectionModel().getSelectedItem();
        if (selectedMahasiswa != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Mahasiswa");
            dialog.setHeaderText("Update Data Mahasiswa");

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
                    if (isValidInput(newNameField.getText(), newNamaBelakangField.getText(), newNoTelpField.getText())) {
                        selectedMahasiswa.setNamaDepan(newNameField.getText());
                        selectedMahasiswa.setNamaBelakang(newNamaBelakangField.getText());
                        selectedMahasiswa.setNoTelepon(newNoTelpField.getText());

                        Connection con = null;
                        try {
                            con = ConnectionManager.getConnection();
                            MahasiswaDAO.update(con, selectedMahasiswa);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionManager.closeConnection(con);
                        }
                        tblMahasiswa.refresh();
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
            alert.setTitle("Tidak Ada Mahasiswa yang Dipilih");
            alert.setHeaderText("Tidak ada data Mahasiswa yang dipilih");
            alert.setContentText("Silakan pilih data mahasiswa yang ingin diubah.");
            alert.showAndWait();
        }
    }

    // Metode untuk memvalidasi input
    private boolean isValidInput(String nama, String namaBelakang, String noTelp) {
        return !nama.isEmpty() && !namaBelakang.isEmpty() && !noTelp.isEmpty();
    }

    @FXML
    public void delete(ActionEvent event) {
        if (tblMahasiswa.getSelectionModel().getSelectedItems().size() != 0) {
            Mahasiswa selected = (Mahasiswa) tblMahasiswa.getSelectionModel().getSelectedItem();
            Connection connection = null;
            try {
                connection = ConnectionManager.getConnection();
                MahasiswaDAO.delete(connection, selected);
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
    private void search(ActionEvent event) {
        String query = searchBar.getText().toLowerCase();
        if (query.isEmpty()) {
            tblMahasiswa.setItems(FXCollections.observableArrayList(data)); // Show all data
        } else {
            ObservableList<Mahasiswa> filteredData = FXCollections.observableArrayList();
            for (Mahasiswa mahasiswa : data) {
                if (mahasiswa.getNamaDepan().toLowerCase().contains(query) ||
                        mahasiswa.getNamaBelakang().toLowerCase().contains(query) ||
                        String.valueOf(mahasiswa.getId()).toLowerCase().contains(query) ||
                        String.valueOf(mahasiswa.getNoTelepon()).toLowerCase().contains(query) ||
                        String.valueOf(mahasiswa.getIdStaffKoperasi()).toLowerCase().contains(query)) {
                    filteredData.add(mahasiswa);
                }
            }
            tblMahasiswa.setItems(filteredData);
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
        File file = chooser.showSaveDialog(tblMahasiswa.getScene().getWindow());
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
            Cell logoCell = new Cell(1, 2).add(logo);
            logoCell.setBorder(Border.NO_BORDER);
            table.addCell(logoCell);

            Cell emptyCell = new Cell(1, 4);
            emptyCell.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell);

            //Header Table
            for (int i = 0; i< tblMahasiswa.getColumns().size(); i++) {
                TableColumn col = tblMahasiswa.getColumns().get(i);
                Cell headerCell = new Cell();
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
            for (int i = 0; i< tblMahasiswa.getItems().size(); i++) {
                Mahasiswa data = (Mahasiswa) tblMahasiswa.getItems().get(i);

                //Data id
                Paragraph idParagraph = new Paragraph(String.valueOf(data.getId()));
                idParagraph.setTextAlignment(TextAlignment.CENTER);
                Cell idCell = new Cell().add(idParagraph);
                table.addCell(idCell);

                //Data
                Paragraph namaDepanParagraph = new Paragraph(data.getNamaDepan());
                Cell merkCell = new Cell().add(namaDepanParagraph);
                table.addCell(merkCell);

                //Data nama
                Paragraph namaBelakangParagraph = new Paragraph(data.getNamaBelakang());
                Cell namaCell = new Cell().add(namaBelakangParagraph);
                table.addCell(namaCell);

                //Data
                Paragraph noTelpParagraph = new Paragraph(data.getNoTelepon());
                Cell bpomCell = new Cell().add(noTelpParagraph);
                table.addCell(bpomCell);

                //Data id
                Paragraph idStaff = new Paragraph(String.valueOf(data.getIdStaffKoperasi()));
                idStaff.setTextAlignment(TextAlignment.CENTER);
                Cell idStaffCell = new Cell().add(idStaff);
                table.addCell(idStaffCell);

                emptyCell2.setBorder(Border.NO_BORDER);
                table.addCell(emptyCell2);
            }

            //Table Footer
            //rowspan, colspan
//            Paragraph totalTitle = new Paragraph("Total").setTextAlignment(TextAlignment.RIGHT);
//            totalTitle.setBold();
//            Cell totalTitleCell = new Cell(1, 3).add(totalTitle);
//            table.addFooterCell(totalTitleCell);
//
//            Cell totalCell = new Cell().add(new Paragraph(String.valueOf(total)));
//            table.addFooterCell(totalCell);

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
        XSSFSheet spreadsheet = workbook.createSheet("Data Mahasiswa");

        FileOutputStream out = null;
        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            int rowid = 0;

            //Export Header
            XSSFRow headerRow = spreadsheet.createRow(rowid++);
            Object[] headerArr = tblMahasiswa.getColumns().toArray();

            int cellCounter = 0;
            for (Object obj : headerArr) {
                XSSFCell cell = headerRow.createCell(cellCounter++);
                cell.setCellValue(((TableColumn) obj).getText());
            }

            //Export Data
            Map<String, Object[]> data = MahasiswaDAO.getAllArrayObject(con);
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
