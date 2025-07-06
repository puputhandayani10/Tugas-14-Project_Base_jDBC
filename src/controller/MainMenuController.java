package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;



public class MainMenuController {

    @FXML
    private void handleBarang(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/barang_view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Kelola Barang");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Gagal buka barang_view.fxml: " + e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void handlePelanggan(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/pelanggan.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Kelola Pelanggan");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Gagal buka pelanggan.fxml: " + e.getMessage());
            alert.showAndWait();
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
    private void handleTransaksi(ActionEvent event) {
        showInfo("Buat Transaksi dipilih");
    }

    @FXML
    private void handleKeluar(ActionEvent event) {
        System.exit(0);
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    @FXML
    private void keTransaksi(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/transaksi_view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transaksi Penjualan");

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal buka transaksi: " + e.getMessage()).showAndWait();
        }

    }
    @FXML
    private void bukaLaporan(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/laporan_view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Laporan Penjualan");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal buka laporan: " + e.getMessage()).showAndWait();
        }
    }


}
