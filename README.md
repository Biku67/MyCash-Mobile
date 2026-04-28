<div align="center">

# MyCash.

**Aplikasi Pencatatan Kas Kelas — Android**

![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-API_24+-3DDC84?logo=android&logoColor=white)
![Material3](https://img.shields.io/badge/Material_Design_3-141419?logo=material-design&logoColor=white)
![License](https://img.shields.io/badge/License-Proprietary-D4FF00)

<br>

*Solusi modern untuk pencatatan keuangan kelas.*
*Dibangun dengan Kotlin, Material Design 3, dan arsitektur MVVM.*

</div>

---

## Tentang

**MyCash** adalah aplikasi Android native yang dirancang khusus untuk **Bendahara Kelas** agar dapat mencatat dan mengelola keuangan kelas secara digital, efisien, dan transparan. Aplikasi ini dibangun sebagai pendamping mobile dari platform [MyCash Web (Laravel)](https://github.com/Biku67).

Tidak perlu koneksi internet — semua data disimpan secara lokal di perangkat menggunakan **Room Database**.

---

## ✨ Fitur Utama

| Fitur | Deskripsi |
|:------|:----------|
| **Dashboard** | Ringkasan saldo, pemasukan, pengeluaran, dan grafik arus kas |
| **Pencatatan Kas** | Input pemasukan & pengeluaran dengan kategori dan tanggal |
| **Manajemen Siswa** | Kelola daftar siswa kelas (tambah, edit, hapus) |
| **Manajemen Iuran** | Buat tagihan, tandai lunas, pantau status pembayaran |
| **Laporan & Export** | Filter laporan per periode, export ke PDF & Excel |
| **Biometric Lock** | Kunci aplikasi dengan sidik jari / face unlock |
| **Share WhatsApp** | Bagikan bukti pembayaran langsung ke WhatsApp |
| **Backup & Restore** | Backup database untuk pindah perangkat |
| **Reminder Iuran** | Notifikasi otomatis untuk tagihan jatuh tempo |
| **Dark / Light Mode** | Pilih tema sesuai preferensi |

---

## Tech Stack

```
 Language        : Kotlin
 Architecture    : MVVM (Model-View-ViewModel)
 Database        : Room (SQLite)
 Navigation      : Jetpack Navigation Component
 Charts          : MPAndroidChart
 Animation       : Lottie
 Security        : AndroidX Biometric
 Export           : iTextPDF + Apache POI
 UI Framework    : Material Design 3
```

---

## Struktur Project

```
com.bily.mycash/
├── data/
│   ├── db/           # Room Database, DAO, Entity
│   └── repository/   # Repository pattern
├── ui/
│   ├── splash/       # Splash screen + biometric
│   ├── dashboard/    # Beranda & statistik
│   ├── transaction/  # Pencatatan kas
│   ├── dues/         # Manajemen iuran
│   ├── student/      # Manajemen siswa
│   ├── report/       # Laporan & export
│   └── settings/     # Pengaturan & profil
├── adapter/          # RecyclerView adapters
└── util/             # Helper classes
```

---

## Desain

Aplikasi menggunakan tema **premium dark mode** yang konsisten dengan branding MyCash:

| Elemen | Warna |
|:-------|:------|
| Background | `#0B0B0C` |
| Surface / Card | `#141419` |
| Aksen Utama (Lime) | `#D4FF00` |
| Income (Hijau) | `#34D399` |
| Expense (Merah) | `#EF4444` |
| Teks Utama | `#FFFFFF` |
| Teks Sekunder | `#888888` |

---

## Instalasi & Pengembangan

### Prasyarat
- Android Studio Meerkat (2025.x) atau lebih baru
- JDK 11+
- Android SDK API 24+ (Android 7.0)

### Langkah
```bash
# Clone repository
git clone https://github.com/Biku67/MyCash-Mobile.git

# Buka di Android Studio
# File → Open → Pilih folder MyCash-Mobile

# Sync Gradle & Run
# Klik tombol ▶️ Run di Android Studio
```

---

## Roadmap

- [x] Project setup & arsitektur dasar
- [ ] Database lokal (Room) & Entity
- [ ] Splash screen dengan Biometric
- [ ] Dashboard & grafik arus kas
- [ ] Pencatatan transaksi (CRUD)
- [ ] Manajemen siswa
- [ ] Manajemen iuran
- [ ] Laporan & export (PDF/Excel)
- [ ] Backup & restore
- [ ] Quick-add widget
- [ ] Integrasi API (Versi 2.0)

---

## Terkait

- **MyCash Web (Laravel)** — Platform SaaS 3-level untuk manajemen kas kelas berbasis web

---

## 👤 Developer

**Biku** — [@Biku67](https://github.com/Biku67)

---

<div align="center">

**MyCash.** — *Kelola Kas Kelas, Lebih Mudah.*

</div>
