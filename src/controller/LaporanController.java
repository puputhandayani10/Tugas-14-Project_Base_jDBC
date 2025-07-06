package controller;

import app.Database;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.TransaksiRingkas;
import model.DetailItem;

import java.io.IOException;
import java.sql.*;

public class LaporanController {

    @FXML private TableView<TransaksiRingkas> tableTransaksi;
    @FXML private TableColumn<TransaksiRingkas, Integer> colIdTrans;
    @FXML private TableColumn<TransaksiRingkas, String> colTanggal;
    @FXML private TableColumn<TransaksiRingkas, Integer> colTotal;

    @FXML private TableView<DetailItem> tableDetail;
    @FXML private TableColumn<DetailItem, String> colKodeBarang;
    @FXML private TableColumn<DetailItem, String> colNamaBarang;
    @FXML private TableColumn<DetailItem, Integer> colJumlah;
    @FXML private TableColumn<DetailItem, Integer> colSubtotal;

    @FXML private Label labelTotalPendapatan;

    private final ObservableList<TransaksiRingkas> daftarTransaksi = FXCollections.observableArrayList();
    private final ObservableList<DetailItem> daftarDetail = FXCollections.observableArrayList();
    @FXML
    private void hoverMasuk(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #BD0202; -fx-text-fill: white;");
    }

    @FXML
    private void hoverKeluar(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #0FCA4A; -fx-text-fill: white;");
    }

    @FXML
    public void initialize() {
        // Kolom transaksi
        colIdTrans.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colTanggal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggal()));
        colTotal.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTotal()).asObject());

        // Kolom detail
        colKodeBarang.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKode()));
        colNamaBarang.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNama()));
        colJumlah.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getJumlah()).asObject());
        colSubtotal.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSubtotal()).asObject());

        tableTransaksi.setItems(daftarTransaksi);
        tableDetail.setItems(daftarDetail);

        tableTransaksi.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                tampilkanDetail(newSel.getId());
            }
        });

        loadTransaksiHariIni();
    }

    private void loadTransaksiHariIni() {
        daftarTransaksi.clear();
        int totalHarian = 0;

        String sql = "SELECT * FROM tbl_transaksi WHERE tanggal_transaksi = CURDATE()";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_transaksi");
                String tgl = rs.getString("tanggal_transaksi");
                int total = rs.getInt("total_harga");

                daftarTransaksi.add(new TransaksiRingkas(id, tgl, total));
                totalHarian += total;
            }

            labelTotalPendapatan.setText("Total Pendapatan Hari Ini: Rp " + totalHarian);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Gagal load transaksi: " + e.getMessage());
        }
    }

    private void tampilkanDetail(int idTransaksi) {
        daftarDetail.clear();

        String sql = "SELECT d.kode_barang, b.nama_barang, d.jumlah, d.subtotal " +
                "FROM tbl_detail_transaksi d " +
                "JOIN tbl_barang b ON d.kode_barang = b.kode_barang " +
                "WHERE d.id_transaksi = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTransaksi);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                daftarDetail.add(new DetailItem(
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("jumlah"),
                        rs.getInt("subtotal")
                ));
            }

            // Fix penting supaya TableView update
            tableDetail.setItems(null);
            tableDetail.setItems(daftarDetail);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Gagal load detail: " + e.getMessage());
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
            showAlert("Gagal kembali ke menu: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
