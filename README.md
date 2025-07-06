==============================
Aplikasi Penjualan Toko Kelontong
==============================

Deskripsi:
-----------
Aplikasi desktop berbasis JavaFX untuk membantu proses penjualan di toko kelontong. Aplikasi ini mencatat data barang, pelanggan, dan transaksi penjualan secara real-time. Semua data disimpan di database MySQL dengan struktur tabel relasional.

Fitur Utama:
------------
- Input data barang (nama, stok, harga)
- Input data pelanggan (nama, no HP)
- Transaksi penjualan dengan validasi stok
- Riwayat transaksi per pelanggan
- GUI JavaFX berbasis tombol menu angka

Teknologi:
----------
- Java (versi 11 atau 17)
- JavaFX
- MySQL
- JDBC Connector
- Library tambahan (dalam folder lib/)

Struktur Folder:
----------------
- /src              → Kode program Java
- /lib              → Berisi library eksternal (.jar)
- /database         → File SQL (jika ada)
- /screenshots      → Tangkapan layar aplikasi (opsional)
- README.txt        → Dokumentasi singkat

Cara Menjalankan Aplikasi:
--------------------------
1. Clone / download project ini:
   git clone https://github.com/[namarepoanda]/[namaproject].git

2. Buat database MySQL baru:
   Nama: toko
   Import struktur tabel jika ada file database_toko.sql

3. Pastikan koneksi database sudah disesuaikan:
   File: DatabaseConnector.java
   Sesuaikan baris berikut:
   - URL:      jdbc:mysql://localhost:3306/toko
   - USER:     root
   - PASSWORD: (isi sesuai MySQL kamu)

4. Pastikan semua library eksternal ada di folder /lib dan sudah dikenali oleh IDE (IntelliJ):
   - Klik kanan folder lib → Mark as Library
   - Tambahkan ke Project Structure → Dependencies

5. Jalankan aplikasi:
   - Di IDE: Klik kanan MainApp.java → Run
   - Atau via terminal:
     javac -cp ".;lib/*" MainApp.java
     java -cp ".;lib/*" MainApp

Catatan Tambahan:
-----------------
- Jangan lupa push folder /lib ke Git agar program bisa dijalankan di komputer lain.
- File .gitignore harus mengizinkan lib/, jangan dikecualikan.

Kontributor:
------------
Nama: Helmi  
Proyek akademik: Sistem Penjualan Toko Kelontong  
Email: (isi jika perlu)

Lisensi:
--------
Aplikasi ini dibuat untuk keperluan pembelajaran dan pengembangan akademik.
Gunakan dengan bijak.

