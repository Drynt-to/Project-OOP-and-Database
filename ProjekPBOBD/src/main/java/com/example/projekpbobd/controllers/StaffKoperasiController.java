package com.example.projekpbobd.controllers;


import com.example.projekpbobd.beans.Pegawai;
import com.example.projekpbobd.beans.StaffKoperasi;
import com.example.projekpbobd.dao.StaffKoperasiDAO;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class StaffKoperasiController implements Initializable {
    @FXML
    private Button back;
    @FXML
    private TableView<StaffKoperasi> tblStaffKoperasi;
    private ObservableList<StaffKoperasi> dataStaffKoperasi;
    @FXML
    private DatePicker tglSelesai;
    @FXML
    private DatePicker tglMulai;
    @FXML
    private TextField txtIdDepartemen;
    @FXML
    private TextField searchBar;

    @FXML
    private Button idDept;
    @FXML
    private Button save;
    @FXML
    private Button delete;
    @FXML
    private Button update;
    @FXML
    private Pane frmPane;
    private HashMap<String, String> parameters = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataStaffKoperasi = FXCollections.observableArrayList();
        tblStaffKoperasi.setItems(dataStaffKoperasi);
        setupTable();
        showStaff();
    }

    private void setupTable() {
        TableColumn<StaffKoperasi, Integer> idCol = new TableColumn<>("Id");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<StaffKoperasi, LocalDate> tglMulaiCol = new TableColumn<>("Tanggal Mulai");
        tglMulaiCol.setMinWidth(150);
        tglMulaiCol.setCellValueFactory(new PropertyValueFactory<>("tanggalMulai"));

        TableColumn<StaffKoperasi, LocalDate> tglSelesaiCol = new TableColumn<>("Tanggal Selesai");
        tglSelesaiCol.setMinWidth(150);
        tglSelesaiCol.setCellValueFactory(new PropertyValueFactory<>("tanggalSelesai"));

        TableColumn<StaffKoperasi, Integer> departemenCol = new TableColumn<>("Id Departemen");
        departemenCol.setMinWidth(50);
        departemenCol.setCellValueFactory(new PropertyValueFactory<>("idDepartemen"));


        tblStaffKoperasi.getColumns().clear();
        tblStaffKoperasi.getColumns().addAll(idCol, tglMulaiCol, tglSelesaiCol, departemenCol);
    }


    public void showStaff() {
        dataStaffKoperasi.clear();
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            dataStaffKoperasi.addAll(StaffKoperasiDAO.getAll(con));
            tblStaffKoperasi.setItems(dataStaffKoperasi);
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
            dataStaffKoperasi = FXCollections.observableArrayList(StaffKoperasiDAO.getAll(connection));
            tblStaffKoperasi.setItems(dataStaffKoperasi);
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
            StaffKoperasi staffKoperasi = new StaffKoperasi();
            staffKoperasi.setTanggalMulai(tglMulai.getValue());
            staffKoperasi.setTanggalSelesai(tglSelesai.getValue());
            staffKoperasi.setIdDepartemen(Integer.parseInt(txtIdDepartemen.getText()));
            System.out.println(staffKoperasi);
            StaffKoperasiDAO.save(con, staffKoperasi);
            dataStaffKoperasi.add(staffKoperasi);
            tblStaffKoperasi.refresh();
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
        StaffKoperasi selectedStaff = tblStaffKoperasi.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Staff Koperasi");
            dialog.setHeaderText("Update Data Staff Koperasi");

            DatePicker tglMulaiPicker = new DatePicker(selectedStaff.getTanggalMulai());
            tglMulaiPicker.setPromptText("Masukkan Tanggal Mulai Baru");
            DatePicker tglSelesaiPicker = new DatePicker(selectedStaff.getTanggalSelesai());
            tglSelesaiPicker.setPromptText("Masukkan Tanggal Selesai Baru");

            VBox content = new VBox();
            content.getChildren().addAll(tglMulaiPicker, tglSelesaiPicker);
            dialog.getDialogPane().setContent(content);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButton) {
                    LocalDate tglMulaiBaru = tglMulaiPicker.getValue();
                    LocalDate tglSelesaiBaru = tglSelesaiPicker.getValue();

                    if (isValidInput(tglMulaiBaru, tglSelesaiBaru)) {
                        selectedStaff.setTanggalMulai(tglMulaiBaru);
                        selectedStaff.setTanggalSelesai(tglSelesaiBaru);
                        Connection con = null;
                        try {
                            con = ConnectionManager.getConnection();
                            StaffKoperasiDAO.update(con, selectedStaff);
                            tblStaffKoperasi.refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionManager.closeConnection(con);
                        }
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
            alert.setTitle("Tidak Ada Data yang Dipilih");
            alert.setHeaderText("Tidak ada data yang dipilih");
            alert.setContentText("Silakan pilih data yang ingin diubah.");
            alert.showAndWait();
        }
    }

    // Metode untuk memvalidasi input
    private boolean isValidInput(LocalDate tglMulai, LocalDate tglSelesai) {
        return tglMulai != null && tglSelesai != null;
    }


    @FXML
    public void delete(ActionEvent event) {
        if (tblStaffKoperasi.getSelectionModel().getSelectedItems().size() != 0) {
            StaffKoperasi selected = (StaffKoperasi) tblStaffKoperasi.getSelectionModel().getSelectedItem();
            Connection connection = null;
            try {
                connection = ConnectionManager.getConnection();
                StaffKoperasiDAO.delete(connection, selected);
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
    public void back(ActionEvent event) {
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
    public void back2(ActionEvent event) {
        Stage stage = (Stage) back.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/dataMahasiswa_view.fxml"));
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
    public void back3(ActionEvent event) {
        Stage stage = (Stage) back.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/dataPegawai_view.fxml"));
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
    public void idDepartemen(ActionEvent event) {
        Stage stage = (Stage) idDept.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/departemen2.fxml"));
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
    public void idDepartemen2(ActionEvent event) {
        Stage stage = (Stage) idDept.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/departemen3.fxml"));
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
    public void idDepartemen3(ActionEvent event) {
        Stage stage = (Stage) idDept.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/department_view.fxml"));
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
    public void export() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Microsoft Excel Spreadsheet (*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Portable Document Format files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(pdfFilter);
        chooser.getExtensionFilters().add(excelFilter);

        chooser.setInitialDirectory(new File("C:\\Users"));
        File file = chooser.showSaveDialog(tblStaffKoperasi.getScene().getWindow());
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
            logo.setWidth(UnitValue.createPercentValue(90));
            com.itextpdf.layout.element.Cell logoCell = new com.itextpdf.layout.element.Cell(1, 2).add(logo);
            logoCell.setBorder(Border.NO_BORDER);
            table.addCell(logoCell);

            com.itextpdf.layout.element.Cell emptyCell = new com.itextpdf.layout.element.Cell(1, 4);
            emptyCell.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell);

            //Header Table
            for (int i = 0; i< tblStaffKoperasi.getColumns().size(); i++) {
                TableColumn col = tblStaffKoperasi.getColumns().get(i);
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
                Paragraph title = new Paragraph(col.getText());
                title.setTextAlignment(TextAlignment.CENTER);
                title.setBold();

                headerCell.add(title);
                table.addCell(headerCell);
            }
            com.itextpdf.layout.element.Cell emptyCell2 = new Cell(1, 4);
            emptyCell2.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell2);
            //Table Data
            int total = 0;
            for (int i = 0; i< tblStaffKoperasi.getItems().size(); i++) {
                StaffKoperasi data = (StaffKoperasi) tblStaffKoperasi.getItems().get(i);

                //Data id
                Paragraph idParagraph = new Paragraph(String.valueOf(data.getId()));
                idParagraph.setTextAlignment(TextAlignment.CENTER);
                com.itextpdf.layout.element.Cell idCell = new com.itextpdf.layout.element.Cell().add(idParagraph);
                table.addCell(idCell);

                Paragraph tglMulai = new Paragraph(String.valueOf(data.getTanggalMulai()));
                com.itextpdf.layout.element.Cell tglMulaiCell = new com.itextpdf.layout.element.Cell().add(tglMulai);
                table.addCell(tglMulaiCell);

                //Data
                Paragraph tglSelesai = new Paragraph(String.valueOf(data.getTanggalSelesai()));
                com.itextpdf.layout.element.Cell tglSelesaiCell = new com.itextpdf.layout.element.Cell().add(tglSelesai);
                table.addCell(tglSelesaiCell);

                Paragraph idDept = new Paragraph(String.valueOf(data.getIdDepartemen()));
                com.itextpdf.layout.element.Cell idDeptCell = new com.itextpdf.layout.element.Cell().add(idDept);
                table.addCell(idDeptCell);


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
        XSSFSheet spreadsheet = workbook.createSheet("Data Staff Koperasi");

        FileOutputStream out = null;
        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            int rowid = 0;

            //Export Header
            XSSFRow headerRow = spreadsheet.createRow(rowid++);
            Object[] headerArr = tblStaffKoperasi.getColumns().toArray();

            int cellCounter = 0;
            for (Object obj : headerArr) {
                XSSFCell cell = headerRow.createCell(cellCounter++);
                cell.setCellValue(((TableColumn) obj).getText());
            }

            //Export Data
            Map<String, Object[]> data = StaffKoperasiDAO.getAllArrayObject(con);
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
