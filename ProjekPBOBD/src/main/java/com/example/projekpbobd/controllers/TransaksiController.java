package com.example.projekpbobd.controllers;

import com.example.projekpbobd.beans.Departemen;
import com.example.projekpbobd.beans.Transaksi;
import com.example.projekpbobd.dao.TransaksiDAO;
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
import java.util.*;

public class TransaksiController implements Initializable {
    @FXML
    private Button back;
    @FXML
    private TableView<Transaksi> tblTransaksi;
    private ObservableList<Transaksi> dataTransaksi;
    @FXML
    private TextField txtTotalJual;
    @FXML
    private TextField txtProfit;
    @FXML
    private TextField txtKuantitas;
    @FXML
    private TextField txtIdStaff;
    @FXML
    private TextField txtIdBarang;
    @FXML
    private DatePicker tanggal;
    @FXML
    private TextField searchBar;

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
        dataTransaksi = FXCollections.observableArrayList();
        tblTransaksi.setItems(dataTransaksi);
        setupTable();
        showTransaksi();
    }

    private void setupTable() {
        TableColumn<Transaksi, Integer> idCol = new TableColumn<>("Id");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaksi, LocalDate> tanggal = new TableColumn<>("Tanggal");
        tanggal.setMinWidth(100);
        tanggal.setCellValueFactory(new PropertyValueFactory<>("tglTransaksi"));

        TableColumn<Transaksi, Double> totalJual = new TableColumn<>("Total Jual");
        totalJual.setMinWidth(100);
        totalJual.setCellValueFactory(new PropertyValueFactory<>("totalJual"));

        TableColumn<Transaksi, Double> profit = new TableColumn<>("Profit");
        profit.setMinWidth(100);
        profit.setCellValueFactory(new PropertyValueFactory<>("profit"));

        TableColumn<Transaksi, Integer> kuantitas = new TableColumn<>("Kuantitas");
        kuantitas.setMinWidth(100);
        kuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));

        TableColumn<Transaksi, Integer> idStaffCol = new TableColumn<>("ID Staff");
        idStaffCol.setMinWidth(50);
        idStaffCol.setCellValueFactory(new PropertyValueFactory<>("id_staff_koperasi"));

//        TableColumn<Transaksi, Integer> idBarangCol = new TableColumn<>("ID Barang");
//        idBarangCol.setMinWidth(50);
//        idBarangCol.setCellValueFactory(new PropertyValueFactory<>("id_barang"));

        tblTransaksi.getColumns().clear();
        tblTransaksi.getColumns().addAll(idCol, tanggal, totalJual, profit, kuantitas, idStaffCol);

    }

    public void showTransaksi() {
        dataTransaksi.clear();
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            dataTransaksi.addAll(TransaksiDAO.getAll(con));
            tblTransaksi.setItems(dataTransaksi);
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
            dataTransaksi = FXCollections.observableArrayList(TransaksiDAO.getAll(connection));
            tblTransaksi.setItems(dataTransaksi);
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
            Transaksi transaksi = new Transaksi();
            transaksi.setTglTransaksi(tanggal.getValue());
            transaksi.setTotalJual(Double.parseDouble(txtTotalJual.getText()));
            transaksi.setProfit(Double.parseDouble(txtProfit.getText()));
            transaksi.setKuantitas(Integer.parseInt(txtKuantitas.getText()));
            transaksi.setId_staff_koperasi(Integer.parseInt(txtIdStaff.getText()));
//            transaksi.setId_barang(Integer.parseInt(txtIdBarang.getText()));
            System.out.println(transaksi);
            TransaksiDAO.save(con, transaksi);
            dataTransaksi.add(transaksi);
            tblTransaksi.refresh();
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
        Transaksi selectedTransaksi = tblTransaksi.getSelectionModel().getSelectedItem();
        if (selectedTransaksi != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Transaksi");
            dialog.setHeaderText("Update Data Transaksi");

            DatePicker newTanggal = new DatePicker();
            newTanggal.setPromptText("Masukkan Tanggal Baru");
            TextField newTotalJual = new TextField();
            newTotalJual.setPromptText("Masukkan Total Jual Baru");
            TextField newProfit = new TextField();
            newProfit.setPromptText("Masukkan profit Baru");
            TextField newKuantitas = new TextField();
            newKuantitas.setPromptText("Masukkan Kuantitas Baru");
            TextField newIdStaff = new TextField();
            newIdStaff.setPromptText("Masukkan ID Staff Baru");
//            TextField newIdBarang = new TextField();
//            newIdBarang.setPromptText("Masukkan ID Barang Baru");

            VBox content = new VBox();
            content.getChildren().addAll(newTanggal, newTotalJual, newProfit, newKuantitas, newIdStaff);
            dialog.getDialogPane().setContent(content);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButton) {
                    if (isValidInput(newTanggal.getValue(), Double.valueOf(newTotalJual.getText()),Double.valueOf(newProfit.getText()), Integer.valueOf(newKuantitas.getText()),Integer.valueOf(newIdStaff.getText()))) {
                        selectedTransaksi.setTglTransaksi(newTanggal.getValue());
                        selectedTransaksi.setTotalJual(Double.parseDouble(newTotalJual.getText()));
                        selectedTransaksi.setProfit(Double.parseDouble(newProfit.getText()));
                        selectedTransaksi.setKuantitas(Integer.parseInt(newKuantitas.getText()));
                        selectedTransaksi.setId_staff_koperasi(Integer.parseInt(newIdStaff.getText()));
//                        selectedTransaksi.setId_barang(Integer.parseInt(newIdBarang.getText()));

                        Connection con = null;
                        try {
                            con = ConnectionManager.getConnection();
                            TransaksiDAO.update(con, selectedTransaksi);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionManager.closeConnection(con);
                        }
                        tblTransaksi.refresh();
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
            alert.setTitle("Tidak Ada Transaksi yang Dipilih");
            alert.setHeaderText("Tidak ada data transaksi yang dipilih");
            alert.setContentText("Silakan pilih data transaksi yang ingin diubah.");
            alert.showAndWait();
        }
    }

    // Metode untuk memvalidasi input
    private boolean isValidInput(LocalDate tanggal, double totalJual, double profit, int kuantitas, int idStaff) {
        return tanggal != null && !(totalJual <0) && !(profit<0) && !(kuantitas<0) && !(idStaff<0);
    }


    @FXML
    public void delete(ActionEvent event) {
        if (tblTransaksi.getSelectionModel().getSelectedItems().size() != 0) {
            Transaksi selected = (Transaksi) tblTransaksi.getSelectionModel().getSelectedItem();
            Connection connection = null;
            try {
                connection = ConnectionManager.getConnection();
                TransaksiDAO.delete(connection, selected);
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
    public void showTransaksiTerbanyak(ActionEvent event) {
        Stage stage = (Stage) back.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/transaksiTerbanyak.fxml"));
            Scene scene = new Scene(root);
            HashMap<String, String> data = new HashMap<>();
            data.put("mode", "add");
            scene.setUserData(data);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
        File file = chooser.showSaveDialog(tblTransaksi.getScene().getWindow());
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

            com.itextpdf.layout.element.Cell emptyCell = new com.itextpdf.layout.element.Cell(1, 7);
            emptyCell.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell);

            com.itextpdf.layout.element.Cell emptyCell3 = new com.itextpdf.layout.element.Cell(1, 7);
            emptyCell3.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell3);

            //Header Table
            for (int i = 0; i< tblTransaksi.getColumns().size(); i++) {
                TableColumn col = tblTransaksi.getColumns().get(i);
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
                Paragraph title = new Paragraph(col.getText());
                title.setTextAlignment(TextAlignment.CENTER);
                title.setBold();

                headerCell.add(title);
                table.addCell(headerCell);
            }
            com.itextpdf.layout.element.Cell emptyCell2 = new com.itextpdf.layout.element.Cell(1, 7);
            emptyCell2.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell2);
            //Table Data
            int total = 0;
            for (int i = 0; i< tblTransaksi.getItems().size(); i++) {
                Transaksi data = (Transaksi) tblTransaksi.getItems().get(i);

                //Data id
                Paragraph idParagraph = new Paragraph(String.valueOf(data.getId()));
                idParagraph.setTextAlignment(TextAlignment.CENTER);
                com.itextpdf.layout.element.Cell idCell = new com.itextpdf.layout.element.Cell().add(idParagraph);
                table.addCell(idCell);

                //Data tgl
                Paragraph tglTransaksi = new Paragraph(String.valueOf(data.getTglTransaksi()));
                com.itextpdf.layout.element.Cell tglTransaksiCell = new com.itextpdf.layout.element.Cell().add(tglTransaksi);
                table.addCell(tglTransaksiCell);

                //Data total jual
                Paragraph totalJual = new Paragraph(String.valueOf(data.getTotalJual()));
                com.itextpdf.layout.element.Cell totalJualCell = new com.itextpdf.layout.element.Cell().add(totalJual);
                table.addCell(totalJualCell);

                Paragraph profit = new Paragraph(String.valueOf(data.getProfit()));
                com.itextpdf.layout.element.Cell profitCell = new com.itextpdf.layout.element.Cell().add(profit);
                table.addCell(profitCell);

                Paragraph kuantitasParagraph = new Paragraph(String.valueOf(data.getKuantitas()));
                com.itextpdf.layout.element.Cell kuantitasCell = new com.itextpdf.layout.element.Cell().add(kuantitasParagraph);
                table.addCell(kuantitasCell);

                Paragraph staffParagraph = new Paragraph(String.valueOf(data.getId_staff_koperasi()));
                com.itextpdf.layout.element.Cell staffCell = new com.itextpdf.layout.element.Cell().add(staffParagraph);
                table.addCell(staffCell);

//                Paragraph barangParagraph = new Paragraph(String.valueOf(data.getId_barang()));
//                com.itextpdf.layout.element.Cell barangCell = new com.itextpdf.layout.element.Cell().add(barangParagraph);
//                table.addCell(barangCell);

                //Data harga
                Paragraph hargaParagraph = new Paragraph(String.valueOf(data.getProfit()));
                Cell hargaCell = new Cell().add(hargaParagraph);
                total += data.getProfit();

                emptyCell2.setBorder(Border.NO_BORDER);
                table.addCell(emptyCell2);
            }

            //Table Footer
//            rowspan, colspan
            Paragraph totalTitle = new Paragraph("Total").setTextAlignment(TextAlignment.RIGHT);
            totalTitle.setBold();
            Cell totalTitleCell = new Cell(1, 3).add(totalTitle);
            table.addFooterCell(totalTitleCell);

            Cell totalCell = new Cell().add(new Paragraph(String.valueOf(total)));
            table.addFooterCell(totalCell);

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
        XSSFSheet spreadsheet = workbook.createSheet("Data Transaksi");

        FileOutputStream out = null;
        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            int rowid = 0;

            //Export Header
            XSSFRow headerRow = spreadsheet.createRow(rowid++);
            Object[] headerArr = tblTransaksi.getColumns().toArray();

            int cellCounter = 0;
            for (Object obj : headerArr) {
                XSSFCell cell = headerRow.createCell(cellCounter++);
                cell.setCellValue(((TableColumn) obj).getText());
            }

            //Export Data
            Map<String, Object[]> data = TransaksiDAO.getAllArrayObject(con);
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
