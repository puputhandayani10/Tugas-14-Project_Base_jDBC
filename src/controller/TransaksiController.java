package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Barang;
import model.CartItem;
import app.Database;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class TransaksiController {

    @FXML private TableView<Barang> tableBarang;
    @FXML private TableColumn<Barang, String> colKode, colNama;
    @FXML private TableColumn<Barang, Integer> colHarga, colStok;
    @FXML private TableView<CartItem> tableKeranjang;
    @FXML private TableColumn<CartItem, String> colBarang;
    @FXML private TableColumn<CartItem, Integer> colJumlah;
    @FXML private TableColumn<CartItem, Integer> colSubtotal;
    @FXML private TextField tfJumlah;
    @FXML private Label labelTotal;

    private ObservableList<Barang> daftarBarang = FXCollections.observableArrayList();
    private ObservableList<CartItem> keranjang = FXCollections.observableArrayList();
    private int total = 0;

    @FXML
    public void initialize() {
        // Kolom Barang
        colKode.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKodeBarang()));
        colNama.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaBarang()));
        colHarga.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getHarga()).asObject());
        colStok.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStok()).asObject());

        // Kolom Keranjang
        colBarang.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaBarang()));
        colJumlah.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getJumlah()).asObject());
        colSubtotal.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSubtotal()).asObject());

        tableKeranjang.setItems(keranjang);
        labelTotal.setText("Total: Rp 0");

        loadBarangDariDatabase();
    }

    private void loadBarangDariDatabase() {
        try (Connection conn = Database.getConnection()) {
            daftarBarang.clear();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbl_barang");

            while (rs.next()) {
                daftarBarang.add(new Barang(
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("harga_barang"),
                        rs.getInt("stok_barang"),
                        rs.getString("satuan") // ⬅️ ini yang harus ditambah
                ));
            }

            tableBarang.setItems(daftarBarang);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal load data barang: " + e.getMessage()).showAndWait();
        }
    }
    @FXML private TextField tfNamaPelanggan;
    @FXML private TextField tfNoHpPelanggan;


    @FXML
    private void tambahKeKeranjang(ActionEvent event) {
        Barang selected = tableBarang.getSelectionModel().getSelectedItem();
        String input = tfJumlah.getText();

        if (selected == null || input.isEmpty()) {
            alert("Pilih barang dan isi jumlah dulu");
            return;
        }

        int jumlah;
        try {
            jumlah = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            alert("Jumlah harus angka");
            return;
        }

        if (jumlah <= 0) {
            alert("Jumlah harus lebih dari 0");
            return;
        }

        if (jumlah > selected.getStok()) {
            alert("Stok tidak mencukupi!");
            return;
        }

        // Cek apakah barang sudah ada di keranjang
        for (CartItem item : keranjang) {
            if (item.getKodeBarang().equals(selected.getKodeBarang())) {
                int totalJumlah = item.getJumlah() + jumlah;
                if (totalJumlah > selected.getStok()) {
                    alert("Jumlah total melebihi stok!");
                    return;
                }
                item.setJumlah(totalJumlah);
                item.setSubtotal(item.getHarga() * totalJumlah);
                tableKeranjang.refresh();
                hitungTotal();
                tfJumlah.clear();
                return;
            }
        }

        // Tambahkan item baru
        keranjang.add(new CartItem(
                selected.getKodeBarang(),
                selected.getNamaBarang(),
                selected.getHarga(),
                jumlah
        ));
        hitungTotal();
        tfJumlah.clear();
    }
    @FXML
    private void hapusItemKeranjang(ActionEvent event) {
        CartItem selected = tableKeranjang.getSelectionModel().getSelectedItem();
        if (selected != null) {
            keranjang.remove(selected);
            hitungTotal();
        } else {
            alert("Pilih item di keranjang yang ingin dihapus.");
        }
    }

    private void hitungTotal() {
        total = keranjang.stream().mapToInt(CartItem::getSubtotal).sum();
        labelTotal.setText("Total: Rp " + total);
    }

    @FXML
    private void bayarTransaksi(ActionEvent event) {
        if (keranjang.isEmpty()) {
            alert("Keranjang kosong!");
            return;
        }

        String nama = tfNamaPelanggan.getText();
        String noHp = tfNoHpPelanggan.getText();

        if (nama.isEmpty() || noHp.isEmpty()) {
            alert("Nama dan No HP pelanggan wajib diisi.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false); // transaksi dimulai

            // 1. Simpan pelanggan
            int idPelanggan = -1;
            PreparedStatement psPelanggan = conn.prepareStatement(
                    "INSERT INTO tbl_pelanggan (nama_pelanggan, no_hp) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            psPelanggan.setString(1, nama);
            psPelanggan.setString(2, noHp);
            psPelanggan.executeUpdate();

            ResultSet rsPelanggan = psPelanggan.getGeneratedKeys();
            if (rsPelanggan.next()) {
                idPelanggan = rsPelanggan.getInt(1);
            } else {
                conn.rollback();
                alert("Gagal simpan pelanggan.");
                return;
            }

            // 2. Simpan transaksi
            PreparedStatement psTrans = conn.prepareStatement(
                    "INSERT INTO tbl_transaksi (tanggal_transaksi, total_harga, id_pelanggan) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            psTrans.setDate(1, Date.valueOf(LocalDate.now()));
            psTrans.setInt(2, total);
            psTrans.setInt(3, idPelanggan);
            psTrans.executeUpdate();

            ResultSet rsTrans = psTrans.getGeneratedKeys();
            if (!rsTrans.next()) {
                conn.rollback();
                alert("Gagal simpan transaksi.");
                return;
            }

            int idTrans = rsTrans.getInt(1);

            // 3. Simpan detail & update stok
            PreparedStatement psDetail = conn.prepareStatement(
                    "INSERT INTO tbl_detail_transaksi (id_transaksi, kode_barang, jumlah, subtotal) VALUES (?, ?, ?, ?)"
            );

            PreparedStatement psStok = conn.prepareStatement(
                    "UPDATE tbl_barang SET stok_barang = stok_barang - ? WHERE kode_barang = ?"
            );

            for (CartItem item : keranjang) {
                psDetail.setInt(1, idTrans);
                psDetail.setString(2, item.getKodeBarang());
                psDetail.setInt(3, item.getJumlah());
                psDetail.setInt(4, item.getSubtotal());
                psDetail.addBatch();

                psStok.setInt(1, item.getJumlah());
                psStok.setString(2, item.getKodeBarang());
                psStok.addBatch();
            }

            psDetail.executeBatch();
            psStok.executeBatch();

            // 4. Commit all
            conn.commit();
            keranjang.clear();
            tfNamaPelanggan.clear();
            tfNoHpPelanggan.clear();
            tfJumlah.clear();
            hitungTotal();
            alert("Transaksi berhasil!");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Transaksi gagal: " + e.getMessage());
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
            alert("Gagal kembali ke menu: " + e.getMessage());
        }
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
