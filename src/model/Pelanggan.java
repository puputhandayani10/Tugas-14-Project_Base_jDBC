package model;

public class Pelanggan {
    private int idPelanggan;
    private String namaPelanggan;
    private String noHp;

    public Pelanggan(int idPelanggan, String namaPelanggan, String noHp) {
        this.idPelanggan = idPelanggan;
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
    }

    public Pelanggan(String namaPelanggan, String noHp) {
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
    }

    public int getIdPelanggan() { return idPelanggan; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public String getNoHp() { return noHp; }
}
