package com.example.projekpbobd.controllers;

import com.example.projekpbobd.beans.Barang;
import com.example.projekpbobd.beans.Departemen;
import com.example.projekpbobd.dao.DepartmentDAO;
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

public class DepartmentController implements Initializable {
    @FXML
    private Button back;
    @FXML
    private TableView<Departemen> tblDepartemen;
    private ObservableList<Departemen> dataDepartemen;
    @FXML
    private TextField txtNamaDepartemen;
    @FXML
    private TextField txtKepalaDepartemen;
    @FXML
    private TextField txtRole;
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
        dataDepartemen = FXCollections.observableArrayList();
        tblDepartemen.setItems(dataDepartemen);
        setupTable();
        showDepartemen();
    }

    private void setupTable() {
        TableColumn<Departemen, Integer> idCol = new TableColumn<>("Id");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Departemen, String> namaCol = new TableColumn<>("Nama Departemen");
        namaCol.setMinWidth(150);
        namaCol.setCellValueFactory(new PropertyValueFactory<>("namaDepartemen"));

        TableColumn<Departemen, Integer> kepalaDepartemenCol = new TableColumn<>("Kepala Departemen");
        kepalaDepartemenCol.setMinWidth(150);
        kepalaDepartemenCol.setCellValueFactory(new PropertyValueFactory<>("kepalaDepartemen"));

        TableColumn<Departemen, String> roleCol = new TableColumn<>("Role");
        roleCol.setMinWidth(150);
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        tblDepartemen.getColumns().clear();
        tblDepartemen.getColumns().addAll(idCol, namaCol, kepalaDepartemenCol, roleCol);

    }

    public void showDepartemen() {
        dataDepartemen.clear();
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            dataDepartemen.addAll(DepartmentDAO.getAll(con));
            tblDepartemen.setItems(dataDepartemen);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.closeConnection(con);
        }
    }
    @FXML
    private void search(ActionEvent event) {
        String query = searchBar.getText().toLowerCase();
        if (query.isEmpty()) {
            tblDepartemen.setItems(FXCollections.observableArrayList(dataDepartemen)); // Show all data
        } else {
            ObservableList<Departemen> filteredData = FXCollections.observableArrayList();
            for (Departemen departemen : dataDepartemen) {
                if (departemen.getNamaDepartemen().toLowerCase().contains(query) ||
                        departemen.getRole().toLowerCase().contains(query) ||
                        String.valueOf(departemen.getId()).toLowerCase().contains(query) ||
                        String.valueOf(departemen.getKepalaDepartemen()).toLowerCase().contains(query) ||
                        String.valueOf(departemen.getJumlah()).toLowerCase().contains(query)) {
                    filteredData.add(departemen);
                }
            }
            tblDepartemen.setItems(filteredData);
        }
    }


        private void refreshData() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            dataDepartemen = FXCollections.observableArrayList(DepartmentDAO.getAll(connection));
            tblDepartemen.setItems(dataDepartemen);
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
            Departemen departemen = new Departemen();
            departemen.setNamaDepartemen(txtNamaDepartemen.getText());
            departemen.setKepalaDepartemen(Integer.parseInt(txtKepalaDepartemen.getText()));
            departemen.setRole(txtRole.getText());
            System.out.println(departemen);
            DepartmentDAO.save(con, departemen);
            dataDepartemen.add(departemen);
            tblDepartemen.refresh();
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
        Departemen selectedDepartemen = tblDepartemen.getSelectionModel().getSelectedItem();
        if (selectedDepartemen != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Departemen");
            dialog.setHeaderText("Update Data Departemen");

            TextField newNameField = new TextField();
            newNameField.setPromptText("Masukkan Nama Departemen Baru");
            TextField newKepalaField = new TextField();
            newKepalaField.setPromptText("Masukkan Kepala Departemen Baru");
            TextField newRoleField = new TextField();
            newRoleField.setPromptText("Masukkan role Baru");

            VBox content = new VBox();
            content.getChildren().addAll(newNameField, newKepalaField, newRoleField);
            dialog.getDialogPane().setContent(content);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButton) {
                    if (isValidInput(newNameField.getText(), Integer.valueOf(newKepalaField.getText()), newRoleField.getText())) {
                        selectedDepartemen.setNamaDepartemen(newNameField.getText());
                        selectedDepartemen.setKepalaDepartemen(Integer.parseInt(newKepalaField.getText()));
                        selectedDepartemen.setRole(newRoleField.getText());

                        Connection con = null;
                        try {
                            con = ConnectionManager.getConnection();
                            DepartmentDAO.update(con, selectedDepartemen);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionManager.closeConnection(con);
                        }
                        tblDepartemen.refresh();
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
            alert.setTitle("Tidak Ada Departemen yang Dipilih");
            alert.setHeaderText("Tidak ada data Departemen yang dipilih");
            alert.setContentText("Silakan pilih data departemen yang ingin diubah.");
            alert.showAndWait();
        }
    }

    // Metode untuk memvalidasi input
    private boolean isValidInput(String nama, int kepalaDepartemen, String role) {
        return !nama.isEmpty() && !(kepalaDepartemen < 0) && !role.isEmpty();
    }


    @FXML
    public void delete(ActionEvent event) {
        if (tblDepartemen.getSelectionModel().getSelectedItems().size() != 0) {
            Departemen selected = (Departemen) tblDepartemen.getSelectionModel().getSelectedItem();
            Connection connection = null;
            try {
                connection = ConnectionManager.getConnection();
                DepartmentDAO.delete(connection, selected);
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
    public void back3(ActionEvent event) {
        Stage stage = (Stage) back.getScene().getWindow();
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
    public void export() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Microsoft Excel Spreadsheet (*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Portable Document Format files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(pdfFilter);
        chooser.getExtensionFilters().add(excelFilter);

        chooser.setInitialDirectory(new File("C:\\Users"));
        File file = chooser.showSaveDialog(tblDepartemen.getScene().getWindow());
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
            for (int i = 0; i< tblDepartemen.getColumns().size(); i++) {
                TableColumn col = tblDepartemen.getColumns().get(i);
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
                Paragraph title = new Paragraph(col.getText());
                title.setTextAlignment(TextAlignment.CENTER);
                title.setBold();

                headerCell.add(title);
                table.addCell(headerCell);
            }
            com.itextpdf.layout.element.Cell emptyCell2 = new com.itextpdf.layout.element.Cell(1, 4);
            emptyCell2.setBorder(Border.NO_BORDER);
            table.addCell(emptyCell2);
            //Table Data
            int total = 0;
            for (int i = 0; i< tblDepartemen.getItems().size(); i++) {
                Departemen data = (Departemen) tblDepartemen.getItems().get(i);

                //Data id
                Paragraph idParagraph = new Paragraph(String.valueOf(data.getId()));
                idParagraph.setTextAlignment(TextAlignment.CENTER);
                com.itextpdf.layout.element.Cell idCell = new com.itextpdf.layout.element.Cell().add(idParagraph);
                table.addCell(idCell);

                //Data nama
                Paragraph namaDepartemen = new Paragraph(data.getNamaDepartemen());
                com.itextpdf.layout.element.Cell namacell = new com.itextpdf.layout.element.Cell().add(namaDepartemen);
                table.addCell(namacell);

                //Data nama
                Paragraph kepDepartemen = new Paragraph(String.valueOf(data.getKepalaDepartemen()));
                com.itextpdf.layout.element.Cell kepDepartCell = new com.itextpdf.layout.element.Cell().add(kepDepartemen);
                table.addCell(kepDepartCell);

                //Data bpom
                Paragraph role = new Paragraph(data.getRole());
                com.itextpdf.layout.element.Cell roleCell = new Cell().add(role);
                table.addCell(roleCell);

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
        XSSFSheet spreadsheet = workbook.createSheet("Data Departemen");

        FileOutputStream out = null;
        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            int rowid = 0;

            //Export Header
            XSSFRow headerRow = spreadsheet.createRow(rowid++);
            Object[] headerArr = tblDepartemen.getColumns().toArray();

            int cellCounter = 0;
            for (Object obj : headerArr) {
                XSSFCell cell = headerRow.createCell(cellCounter++);
                cell.setCellValue(((TableColumn) obj).getText());
            }

            //Export Data
            Map<String, Object[]> data = DepartmentDAO.getAllArrayObject(con);
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
