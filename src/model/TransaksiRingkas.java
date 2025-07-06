package model;

public class TransaksiRingkas {
    private int id;
    private String tanggal;
    private int total;

    public TransaksiRingkas(int id, String tanggal, int total) {
        this.id = id;
        this.tanggal = tanggal;
        this.total = total;
    }

    public int getId() { return id; }
    public String getTanggal() { return tanggal; }
    public int getTotal() { return total; }
}
