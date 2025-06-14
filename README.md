# Aplikasi Manajemen Hotel (Java Swing)

Aplikasi ini adalah sistem manajemen hotel berbasis Java Swing yang memungkinkan pengguna melakukan pemesanan kamar, melihat daftar kamar, dan mengelola data booking.

## Fitur

- Pemesanan kamar hotel dengan form input lengkap
- Cek ketersediaan dan detail kamar
- Update dan hapus data booking
- Perhitungan otomatis total harga berdasarkan lama menginap
- Pencarian data booking dan kamar
- Tampilan GUI interaktif

## Struktur Proyek

```
src/
  bgHotel.png
  Hotel/
    Admin_Page.java
    DatabaseConnection.java
    HalUtama.java
    login.java
    register.java
build.xml
manifest.mf
```

## Cara Menjalankan

1. Buka proyek di NetBeans IDE.
2. Pastikan database MySQL sudah tersedia dengan tabel `rooms` dan `booking`.
3. Edit konfigurasi koneksi database di `DatabaseConnection.java` sesuai pengaturan Anda.
4. Jalankan file `HalUtama.java` untuk memulai aplikasi.

## Dependensi

- Java JDK 8 atau lebih baru
- MySQL Database
- Library eksternal:
  - `com.toedter.calendar.JDateChooser` (untuk input tanggal)

## Catatan

- Pastikan library eksternal sudah ditambahkan ke project libraries.
- Untuk login, gunakan akun yang sudah terdaftar atau daftar melalui form register.
