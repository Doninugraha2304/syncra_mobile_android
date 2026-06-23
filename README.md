# Syncra POS (Warung Kopi Nusantara) ☕🛒

Syncra POS adalah aplikasi Kasir (Point of Sale) modern berbasis Android yang dirancang khusus untuk mempermudah operasional bisnis makanan dan minuman (F&B) seperti kedai kopi atau restoran. Aplikasi ini dilengkapi dengan antarmuka pengguna yang memukau, fitur canggih seperti pemesanan via suara, serta sistem manajemen stok tingkat lanjut berdasarkan resep bahan baku (Bill of Materials).

## ✨ Fitur Unggulan

- 🎙️ **AI Voice Ordering**: Sistem pemesanan cerdas menggunakan suara. Cukup ucapkan "Pesan Kopi Hitam dua", dan sistem akan otomatis mendeteksinya menggunakan NLP ringan untuk menambahkannya ke keranjang belanja.
- 📦 **Smart Inventory & Recipe System**: Setiap produk dapat diikat dengan resep bahan baku (HPP). Ketika produk terjual, stok bahan baku mentah akan otomatis berkurang dengan konversi satuan yang presisi (misal: HPP 1kg, terpakai 15 gram per cup).
- 🌗 **Dynamic Theme Toggle**: Mode Terang (Light) dan Gelap (Dark) yang sangat mulus dan *pixel-perfect* menyesuaikan dengan identitas *brand* modern.
- 🛒 **Real-time Cart & Checkout**: Pengalaman transaksi kasir *floating bar* yang intuitif dan responsif.
- ⚙️ **Modul Pengaturan Lengkap**:
  - **Ekspor & Impor Database**: Mudah mem-*backup* dan memulihkan data (file `.db`) kapan saja tanpa perlu internet.
  - **Download Laporan**: Unduh laporan transaksi langsung ke memori perangkat.
  - **Profil Usaha Dinamis**: Ubah nama toko dan alamat langsung dari aplikasi (menggunakan `SharedPreferences`).
  - **Pengaturan Printer Thermal**: (*Coming Soon*) Siap untuk integrasi cetak struk Bluetooth.

## 🛠️ Teknologi & Arsitektur

Aplikasi ini dibangun menggunakan arsitektur modern Android (MVVM) dengan standar *Clean Code*:
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Database**: [SQLDelight](https://cashapp.github.io/sqldelight/) (Type-safe SQLite)
- **Asynchronous & State**: Kotlin Coroutines & `StateFlow`
- **Speech Recognition**: Android Native `SpeechRecognizer` API

## 🚀 Cara Menjalankan Aplikasi

1. *Clone* repositori ini:
   ```bash
   git clone https://github.com/Doninugraha2304/syncra_mobile_android.git
   ```
2. Buka proyek menggunakan **Android Studio** versi terbaru (Ladybug / Koala).
3. Biarkan Gradle melakukan sinkronisasi *dependencies*.
4. Klik tombol **Run** (`Shift + F10`) untuk menjalankan aplikasi di Emulator atau Perangkat Android Fisik (disarankan Android 12+).
5. **Catatan Izin Akses**: Pastikan kamu memberikan izin akses Audio / Mikrofon saat pop-up muncul pertama kali untuk mencoba fitur *Voice Ordering*.

## 📁 Struktur Direktori Utama

- `app/src/main/java/com/syncra/pos/`
  - `data/` -> Implementasi repositori dan konfigurasi SQLDelight
  - `di/` -> Modul Dependency Injection (Koin)
  - `domain/` -> Entitas (Product, RawMaterial) dan *interface* repositori
  - `presentation/` -> Seluruh tampilan UI Compose (Kasir, Inventory, Settings) beserta ViewModel-nya.
- `app/src/main/sqldelight/` -> Skema *database* dan kueri SQL mentah.

## 👨‍💻 Kontributor

Proyek ini dirancang dan dikembangkan sebagai bagian dari solusi cerdas untuk UMKM.

---
*Dibuat dengan ❤️ untuk kemajuan UMKM Indonesia.*
