package com.example.projekpbobd.controllers;

import com.example.projekpbobd.beans.Barang;
import com.example.projekpbobd.beans.Pegawai;
import com.example.projekpbobd.dao.BarangDAO;
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

public class BarangController implements Initializable {
    @FXML
    private Button back;
    @FXML
    private TableView<Barang> tblBarang;
    private ObservableList<Barang> dataBarang;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtHargaPokok;
    @FXML
    private TextField txtHargaJual;
    @FXML
    private TextField txtKategori;
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
        dataBarang = FXCollections.observableArrayList();
        tblBarang.setItems(dataBarang);
        setupTable();
        showBarang();
    }

    private void setupTable() {
        TableColumn<Barang, Integer> idCol = new TableColumn<>("Id");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Barang, String> namaCol = new TableColumn<>("Nama Barang");
        namaCol.setMinWidth(100);
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Barang, Double> hargaPokokCol = new TableColumn<>("Harga Pokok");
        hargaPokokCol.setMinWidth(100);
        hargaPokokCol.setCellValueFactory(new PropertyValueFactory<>("hargaPokok"));

        TableColumn<Barang, Double> hargaJualCol = new TableColumn<>("Harga Jual");
        hargaJualCol.setMinWidth(100);
        hargaJualCol.setCellValueFactory(new PropertyValueFactory<>("hargaJual"));

        TableColumn<Barang, String> kategoriCol = new TableColumn<>("Kategori");
        kategoriCol.setMinWidth(100);
        kategoriCol.setCellValueFactory(new PropertyValueFactory<>("kategori"));

        TableColumn<Barang, Double> marginCol = new TableColumn<>("Profit Margin (%)");
        marginCol.setMinWidth(120);
        marginCol.setCellValueFactory(new PropertyValueFactory<>("profit_margin"));

        tblBarang.getColumns().clear();
        tblBarang.getColumns().addAll(idCol, namaCol, hargaPokokCol, hargaJualCol, kategoriCol, marginCol);

    }

    public void showBarang() {
        dataBarang.clear();
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            dataBarang.addAll(BarangDAO.getAll(con));
            tblBarang.setItems(dataBarang);
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
            dataBarang = FXCollections.observableArrayList(BarangDAO.getAll(connection));
            tblBarang.setItems(dataBarang);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(connection);
        }
    }

    @FXML
    private void search(ActionEvent event) {
        String query = searchBar.getText().toLowerCase();
        if (query.isEmpty()) {
            tblBarang.setItems(FXCollections.observableArrayList(dataBarang)); // Show all data
        } else {
            ObservableList<Barang> filteredData = FXCollections.observableArrayList();
            for (Barang barang : dataBarang) {
                if (barang.getNama().toLowerCase().contains(query) ||
                        barang.getKategori().toLowerCase().contains(query) ||
                        String.valueOf(barang.getId()).toLowerCase().contains(query) ||
                        String.valueOf(barang.getHargaJual()).toLowerCase().contains(query) ||
                        String.valueOf(barang.getHargaPokok()).toLowerCase().contains(query))  {
                    filteredData.add(barang);
                }
            }
            tblBarang.setItems(filteredData);
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
            Barang barang = new Barang();
            barang.setNama(txtNama.getText());
            barang.setHargaPokok(Double.parseDouble(txtHargaPokok.getText()));
            barang.setHargaJual(Double.parseDouble(txtHargaJual.getText()));
            barang.setKategori(txtKategori.getText());
            System.out.println(barang);
            BarangDAO.save(con, barang);
            dataBarang.add(barang);
            tblBarang.refresh();
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
        Barang selectedBarang = tblBarang.getSelectionModel().getSelectedItem();
        if (selectedBarang != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Barang");
            dialog.setHeaderText("Update Data Barang");

            TextField newNameField = new TextField();
            newNameField.setPromptText("Masukkan Nama Barang Baru");
            TextField newHargaPokokField = new TextField();
            newHargaPokokField.setPromptText("Masukkan Harga Pokok Baru");
            TextField newHargaJualField = new TextField();
            newHargaJualField.setPromptText("Masukkan Harga Jual Baru");
            TextField newKategoriField = new TextField();
            newKategoriField.setPromptText("Masukkan Kategori Baru");

            VBox content = new VBox();
            content.getChildren().addAll(newNameField, newHargaPokokField, newHargaJualField, newKategoriField);
            dialog.getDialogPane().setContent(content);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButton) {
                    if (isValidInput(newNameField.getText(), Double.valueOf(newHargaPokokField.getText()),Double.valueOf(newHargaJualField.getText()), newKategoriField.getText())) {
                        selectedBarang.setNama(newNameField.getText());
                        selectedBarang.setHargaPokok(Double.parseDouble(newHargaPokokField.getText()));
                        selectedBarang.setHargaJual(Double.parseDouble(newHargaJualField.getText()));
                        selectedBarang.setKategori(newKategoriField.getText());

                        Connection con = null;
                        try {
                            con = ConnectionManager.getConnection();
                            BarangDAO.update(con, selectedBarang);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionManager.closeConnection(con);
                        }
                        tblBarang.refresh();
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
            alert.setTitle("Tidak Ada Barang yang Dipilih");
            alert.setHeaderText("Tidak ada data barang yang dipilih");
            alert.setContentText("Silakan pilih data barang yang ingin diubah.");
            alert.showAndWait();
        }
    }

    // Metode untuk memvalidasi input
    private boolean isValidInput(String nama, Double hargaPokok, Double hargaJual, String kategori) {
        return !nama.isEmpty() && !(hargaPokok <0) && !(hargaJual <0) && !kategori.isEmpty();
    }


    @FXML
    public void delete(ActionEvent event) {
        if (tblBarang.getSelectionModel().getSelectedItems().size() != 0) {
            Barang selected = (Barang) tblBarang.getSelectionModel().getSelectedItem();
            Connection connection = null;
            try {
                connection = ConnectionManager.getConnection();
                BarangDAO.delete(connection, selected);
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
    public void export() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Microsoft Excel Spreadsheet (*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Portable Document Format files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(pdfFilter);
        chooser.getExtensionFilters().add(excelFilter);

        chooser.setInitialDirectory(new File("C:\\Users"));
        File file = chooser.showSaveDialog(tblBarang.getScene().getWindow());
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
            for (int i = 0; i< tblBarang.getColumns().size(); i++) {
                TableColumn col = tblBarang.getColumns().get(i);
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
                Paragraph title = new Paragraph(col.getText());
                title.setTextAlignment(TextAlignment.CENTER);
                title.setBold();

                headerCell.add(title);
                table.addCell(headerCell);
            }
            com.itextpdf.layout.element.Cell emptyCell2 = new Cell(1, 6);
            emptyCell2.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell2);
            //Table Data
            int total = 0;
            for (int i = 0; i< tblBarang.getItems().size(); i++) {
                Barang data = (Barang) tblBarang.getItems().get(i);

                //Data id
                Paragraph idParagraph = new Paragraph(String.valueOf(data.getId()));
                idParagraph.setTextAlignment(TextAlignment.CENTER);
                com.itextpdf.layout.element.Cell idCell = new com.itextpdf.layout.element.Cell().add(idParagraph);
                table.addCell(idCell);

                Paragraph nama = new Paragraph(data.getNama());
                com.itextpdf.layout.element.Cell namacell = new com.itextpdf.layout.element.Cell().add(nama);
                table.addCell(namacell);

                //Data hpp
                Paragraph hargaPokok = new Paragraph(String.valueOf(data.getHargaPokok()));
                com.itextpdf.layout.element.Cell hargaPokokCell = new com.itextpdf.layout.element.Cell().add(hargaPokok);
                table.addCell(hargaPokokCell);

                //Data harga jual
                Paragraph hargaJual = new Paragraph(String.valueOf(data.getHargaJual()));
                com.itextpdf.layout.element.Cell hargaJualCell = new com.itextpdf.layout.element.Cell().add(hargaJual);
                table.addCell(hargaJualCell);

                //Data
                Paragraph kategori = new Paragraph(data.getKategori());
                com.itextpdf.layout.element.Cell kategoriCell = new com.itextpdf.layout.element.Cell().add(kategori);
                table.addCell(kategoriCell);

                Paragraph profit = new Paragraph(String.valueOf(data.getProfit_margin()));
                com.itextpdf.layout.element.Cell profitCell = new com.itextpdf.layout.element.Cell().add(profit);
                table.addCell(profitCell);


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
        XSSFSheet spreadsheet = workbook.createSheet("Data Barang");

        FileOutputStream out = null;
        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            int rowid = 0;

            //Export Header
            XSSFRow headerRow = spreadsheet.createRow(rowid++);
            Object[] headerArr = tblBarang.getColumns().toArray();

            int cellCounter = 0;
            for (Object obj : headerArr) {
                XSSFCell cell = headerRow.createCell(cellCounter++);
                cell.setCellValue(((TableColumn) obj).getText());
            }

            //Export Data
            Map<String, Object[]> data = BarangDAO.getAllArrayObject(con);
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
