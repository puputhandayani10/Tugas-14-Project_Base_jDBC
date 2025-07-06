package controller;

import app.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import model.Pelanggan;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import java.io.IOException;



import java.sql.*;

public class PelangganController {
    @FXML private TableView<Pelanggan> tablePelanggan;
    @FXML private TableColumn<Pelanggan, Integer> colId;
    @FXML private TableColumn<Pelanggan, String> colNama, colNoHp;
    @FXML private TextField tfNama, tfNoHp;

    private ObservableList<Pelanggan> dataPelanggan = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdPelanggan()).asObject());
        colNama.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaPelanggan()));
        colNoHp.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNoHp()));

        loadData();
    }
    @FXML
    private void hoverMasuk(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #BD0202; -fx-text-fill: white;");
    }

    @FXML
    private void hoverKeluar(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #0FCA4A; -fx-text-fill: white;");
    }
    private void loadData() {
        dataPelanggan.clear();
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tbl_pelanggan")) {
            while (rs.next()) {
                dataPelanggan.add(new Pelanggan(
                        rs.getInt("id_pelanggan"),
                        rs.getString("nama_pelanggan"),
                        rs.getString("no_hp")
                ));
            }
            tablePelanggan.setItems(dataPelanggan);
        } catch (SQLException e) {
            showAlert("Error load data: " + e.getMessage());
        }
    }

    @FXML
    private void tambahPelanggan() {
        String nama = tfNama.getText();
        String noHp = tfNoHp.getText();

        if (nama.isEmpty() || noHp.isEmpty()) {
            showAlert("Nama dan No HP wajib diisi.");
            return;
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tbl_pelanggan (nama_pelanggan, no_hp) VALUES (?, ?)")) {
            ps.setString(1, nama);
            ps.setString(2, noHp);
            ps.executeUpdate();

            showAlert("Pelanggan berhasil ditambahkan!");
            tfNama.clear(); tfNoHp.clear();
            loadData();
        } catch (SQLException e) {
            showAlert("Gagal tambah pelanggan: " + e.getMessage());
        }
    }

    @FXML
    private void kembaliKeMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/main_menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Utama");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Gagal kembali ke menu: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
