package model;

import java.time.LocalDate;

public class Transaksi {
    private int idTransaksi;
    private Barang barang;
    private Pelanggan pelanggan;
    private int jumlahBeli;
    private double totalHarga;
    private LocalDate tanggal;

    public Transaksi(int idTransaksi, Barang barang, Pelanggan pelanggan, int jumlahBeli, double totalHarga, LocalDate tanggal) {
        this.idTransaksi = idTransaksi;
        this.barang = barang;
        this.pelanggan = pelanggan;
        this.jumlahBeli = jumlahBeli;
        this.totalHarga = totalHarga;
        this.tanggal = tanggal;
    }

    // Getter...
}
