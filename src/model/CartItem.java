package model;

public class CartItem {
    private String kodeBarang;
    private String namaBarang;
    private int harga;
    private int jumlah;
    private int subtotal;

    public CartItem(String kode, String nama, int harga, int jumlah) {
        this.kodeBarang = kode;
        this.namaBarang = nama;
        this.harga = harga;
        this.jumlah = jumlah;
        this.subtotal = harga * jumlah;
    }

    public String getKodeBarang() { return kodeBarang; }
    public String getNamaBarang() { return namaBarang; }
    public int getHarga() { return harga; }
    public int getJumlah() { return jumlah; }
    public int getSubtotal() { return subtotal; }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
        this.subtotal = this.harga * jumlah;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}
