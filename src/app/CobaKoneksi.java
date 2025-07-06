package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CobaKoneksi {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/nama_database_kamu",
                    "root",
                    "password_anda"
            );

            if (conn != null) {
                System.out.println("✅ Koneksi berhasil!");
            } else {
                System.out.println("❌ Koneksi gagal.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
