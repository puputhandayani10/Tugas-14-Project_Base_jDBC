package model;

public class Barang {
    private String kodeBarang;
    private String namaBarang;
    private int harga;
    private int stok;
    private String satuan;

    // Constructor lengkap
    public Barang(String kode, String nama, int harga, int stok, String satuan) {
        this.kodeBarang = kode;
        this.namaBarang = nama;
        this.harga = harga;
        this.stok = stok;
        this.satuan = satuan;
    }

    // Getter & Setter
    public String getKodeBarang() {
        return kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public int getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
