package model;

public class DetailItem {
    private String kode;
    private String nama;
    private int jumlah;
    private int subtotal;

    public DetailItem(String kode, String nama, int jumlah, int subtotal) {
        this.kode = kode;
        this.nama = nama;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public int getJumlah() { return jumlah; }
    public int getSubtotal() { return subtotal; }
}
