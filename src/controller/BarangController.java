package controller;

import app.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Barang;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BarangController {

    @FXML private TextField tfKode, tfNama, tfHarga, tfStok;
    @FXML private TableView<Barang> tableBarang;
    @FXML private TableColumn<Barang, String> colKode, colNama, colSatuan;
    @FXML private TableColumn<Barang, Integer> colStok;
    @FXML private TableColumn<Barang, String> colHarga;

    @FXML private TextField tfStokBaru;

    private ObservableList<Barang> dataBarang = FXCollections.observableArrayList();
    @FXML private ToggleGroup satuanGroup = new ToggleGroup();
    @FXML private RadioButton rbPcs, rbLiter, rbGalon, rbPax;
    @FXML
    public void initialize() {
        satuanGroup = new ToggleGroup();
        rbPcs.setToggleGroup(satuanGroup);
        rbLiter.setToggleGroup(satuanGroup);
        rbGalon.setToggleGroup(satuanGroup);
        rbPax.setToggleGroup(satuanGroup);

        colKode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKodeBarang()));
        colNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaBarang()));
        colHarga.setCellValueFactory(d ->
                new SimpleStringProperty("Rp" + String.format("%,d", d.getValue().getHarga()))
        );
        colStok.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getStok()).asObject());
        colSatuan.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSatuan()));

        tableBarang.setItems(dataBarang);
        tableBarang.setOnMouseClicked(event -> {
            Barang b = tableBarang.getSelectionModel().getSelectedItem();
            if (b != null) {
                tfKode.setText(b.getKodeBarang());
                tfNama.setText(b.getNamaBarang());
                tfHarga.setText(String.valueOf(b.getHarga()));
                tfStok.setText(String.valueOf(b.getStok()));

                switch (b.getSatuan()) {
                    case "pcs": satuanGroup.selectToggle(rbPcs); break;
                    case "liter": satuanGroup.selectToggle(rbLiter); break;
                    case "galon": satuanGroup.selectToggle(rbGalon); break;
                    case "pax": satuanGroup.selectToggle(rbPax); break;
                }
            }
        });

        loadData();
    }

    private void loadData() {
        dataBarang.clear();
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tbl_barang")) {

            while (rs.next()) {
                dataBarang.add(new Barang(
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("harga_barang"),
                        rs.getInt("stok_barang"),
                        rs.getString("satuan")
                ));
            }

        } catch (SQLException e) {
            showAlert("Error load data: " + e.getMessage());
        }
    }

    private String getSatuanTerpilih() {
        RadioButton selected = (RadioButton) satuanGroup.getSelectedToggle();
        return selected != null ? selected.getText() : "";
    }

    @FXML
    private void tambahBarang() {
        String kode = tfKode.getText();
        String nama = tfNama.getText();
        int harga = Integer.parseInt(tfHarga.getText());
        int stok = Integer.parseInt(tfStok.getText());
        String satuan = getSatuanTerpilih();

        if (satuan.isEmpty()) {
            showAlert("Pilih satuan terlebih dahulu.");
            return;
        }

        String sql = "INSERT INTO tbl_barang (kode_barang, nama_barang, harga_barang, stok_barang, satuan) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kode);
            ps.setString(2, nama);
            ps.setInt(3, harga);
            ps.setInt(4, stok);
            ps.setString(5, satuan);
            ps.executeUpdate();

            showAlert("Barang berhasil ditambahkan!");
            tfKode.clear(); tfNama.clear(); tfHarga.clear(); tfStok.clear(); satuanGroup.selectToggle(null);
            loadData();
        } catch (SQLException e) {
            showAlert("Gagal tambah barang: " + e.getMessage());
        }
    }
    @FXML
    private void hoverMasuk(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #BD0202; -fx-text-fill: white;");
    }

    @FXML
    private void hoverKeluar(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #0FCA4A; -fx-text-fill: white;");
    }

    @FXML
    private void updateBarang() {
        Barang selected = tableBarang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Pilih barang yang ingin diperbarui terlebih dahulu.");
            return;
        }

        String kode = selected.getKodeBarang(); // Tidak bisa diubah
        String nama = tfNama.getText();
        int harga = Integer.parseInt(tfHarga.getText());
        int stok = Integer.parseInt(tfStok.getText());
        String satuan = getSatuanTerpilih();

        String sql = "UPDATE tbl_barang SET nama_barang=?, harga_barang=?, stok_barang=?, satuan=? WHERE kode_barang=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setInt(3, stok);
            ps.setString(4, satuan);
            ps.setString(5, kode);
            ps.executeUpdate();

            showAlert("Barang berhasil diperbarui!");
            loadData();
        } catch (SQLException e) {
            showAlert("Gagal update barang: " + e.getMessage());
        }
    }
    @FXML
    private void hapusBarang() {
        Barang selected = tableBarang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Pilih barang yang ingin dihapus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText(null);
        confirm.setContentText("Yakin ingin menghapus barang dengan kode " + selected.getKodeBarang() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection conn = Database.getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "DELETE FROM tbl_barang WHERE kode_barang=?")) {
                    ps.setString(1, selected.getKodeBarang());
                    ps.executeUpdate();
                    showAlert("Barang berhasil dihapus.");
                    loadData();
                } catch (SQLException e) {
                    showAlert("Gagal hapus barang: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void kembaliKeMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/main_menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Utama");
        } catch (IOException e) {
            showAlert("Gagal kembali ke menu: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
