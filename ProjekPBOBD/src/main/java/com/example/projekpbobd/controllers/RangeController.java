package com.example.projekpbobd.controllers;

import com.example.projekpbobd.beans.StaffKoperasi;
import com.example.projekpbobd.beans.Transaksi;
import com.example.projekpbobd.dao.StaffKoperasiDAO;
import com.example.projekpbobd.dao.TransaksiDAO;
import com.example.projekpbobd.util.ConnectionManager;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
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

public class RangeController implements Initializable {
    @FXML
    private Button back;
    @FXML
    private TableView<StaffKoperasi> tblRange;
    private ObservableList<StaffKoperasi> dataRange;
    @FXML
    private Pane frmPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataRange = FXCollections.observableArrayList();
        tblRange.setItems(dataRange);
        setupTableColumns();
        showRangeStaff();
    }
    private void setupTableColumns() {
        TableColumn<StaffKoperasi, Integer> id_staff = new TableColumn<>("id_staff_koperasi");
        id_staff.setMinWidth(50);
        id_staff.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<StaffKoperasi, LocalDate> tglMulaiCol = new TableColumn<>("Tanggal Mulai");
        tglMulaiCol.setMinWidth(100);
        tglMulaiCol.setCellValueFactory(new PropertyValueFactory<>("tanggalMulai"));

        TableColumn<StaffKoperasi, LocalDate> tglSelesaiCol = new TableColumn<>("Tanggal Selesai");
        tglSelesaiCol.setMinWidth(100);
        tglSelesaiCol.setCellValueFactory(new PropertyValueFactory<>("tanggalSelesai"));

        TableColumn<StaffKoperasi, String> durasi = new TableColumn<>("Range");
        durasi.setMinWidth(150);
        durasi.setCellValueFactory(new PropertyValueFactory<>("durasi"));

        tblRange.getColumns().clear();
        tblRange.getColumns().addAll(id_staff, tglMulaiCol, tglSelesaiCol, durasi);
    }

    private void showRangeStaff() {
        dataRange.clear();
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            dataRange.addAll(StaffKoperasiDAO.getRangeStaff(con));
            tblRange.setItems(dataRange);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.closeConnection(con);
        }
    }

    @FXML
    public void back(ActionEvent event) {
        Stage stage = (Stage) back.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/com/example/projekpbobd/statistik.fxml"));
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
        File file = chooser.showSaveDialog(tblRange.getScene().getWindow());
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

                com.itextpdf.layout.element.Cell emptyCell = new com.itextpdf.layout.element.Cell(1, 6);
                emptyCell.setBorder(Border.NO_BORDER);
                table.addCell(emptyCell);

                //Header Table
                for (int i = 0; i< tblRange.getColumns().size(); i++) {
                    TableColumn col = tblRange.getColumns().get(i);
                    com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
                    Paragraph title = new Paragraph(col.getText());
                    title.setTextAlignment(TextAlignment.CENTER);
                    title.setBold();

                    headerCell.add(title);
                    table.addCell(headerCell);
                }
                com.itextpdf.layout.element.Cell emptyCell2 = new com.itextpdf.layout.element.Cell(1, 6);
                emptyCell2.setBorder(Border.NO_BORDER);
                table.addCell(emptyCell2);
                //Table Data
                int total = 0;
                for (int i = 0; i< tblRange.getItems().size(); i++) {
                    StaffKoperasi data = (StaffKoperasi) tblRange.getItems().get(i);

                    //Data Kategori
                    Paragraph id = new Paragraph(String.valueOf(data.getId()));
                    com.itextpdf.layout.element.Cell kategoriCell = new com.itextpdf.layout.element.Cell().add(id);
                    table.addCell(kategoriCell);

                    //Data jumlah
                    Paragraph tglMulai = new Paragraph(String.valueOf(data.getTanggalMulai()));
                    com.itextpdf.layout.element.Cell tglMulaiCell = new com.itextpdf.layout.element.Cell().add(tglMulai);
                    table.addCell(tglMulaiCell);

                    Paragraph tglSelesai = new Paragraph(String.valueOf(data.getTanggalSelesai()));
                    com.itextpdf.layout.element.Cell tglSelesaiCell = new com.itextpdf.layout.element.Cell().add(tglSelesai);
                    table.addCell(tglSelesaiCell);

                    Paragraph range = new Paragraph(String.valueOf(data.getDurasi()));
                    com.itextpdf.layout.element.Cell rangeCell = new com.itextpdf.layout.element.Cell().add(range);
                    table.addCell(rangeCell);

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
        XSSFSheet spreadsheet = workbook.createSheet("Data Durasi");

        FileOutputStream out = null;
        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            int rowid = 0;

            //Export Header
            XSSFRow headerRow = spreadsheet.createRow(rowid++);
            Object[] headerArr = tblRange.getColumns().toArray();

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
