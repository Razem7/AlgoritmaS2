# Kasir Sederhana - Aplikasi Web Kasir

Aplikasi kasir sederhana berbasis web menggunakan **Java Spring Boot** 

## Fitur

- Daftar produk dengan harga dan stok
- Keranjang belanja
- Perhitungan total otomatis
- Proses checkout dengan kembalian
- Struk pembayaran
- Riwayat transaksi
- Manajemen produk (tambah, edit, hapus)
- Laporan penjualan dengan statistik
- Export data transaksi ke CSV
- Update stok otomatis
- Desain responsif dan modern (tema putih-hijau)

## Teknologi yang Digunakan

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring MVC** (Controller)
- **Thymeleaf** (Template Engine untuk HTML)
- **Maven** (Build Tool)
- **MySQL** (Optional - untuk database persistent)
- HTML/CSS (Frontend)


**TIDAK PERLU XAMPP** untuk menjalankan aplikasi ini!

- Aplikasi ini **100% Pure Java Spring Boot**
- Punya web server sendiri (Apache Tomcat embedded)
- Bisa jalan tanpa XAMPP sama sekali

### Kalau Tetap Mau Pakai MySQL dari XAMPP:

1. Start MySQL di XAMPP
2. Buat database `kasir_db`
3. Edit `src/main/resources/application.properties`
4. Uncomment baris MySQL configuration

## Cara Menjalankan

### Prasyarat

1. **Java JDK 17 atau lebih tinggi**
```bash
# Cek Java version
java -version
```

### Langkah-langkah

1. Buka terminal di folder project

2. Jalankan aplikasi:
```bash
mvn spring-boot:run
```

3. Tunggu sampai muncul log:
```
Started KasirApplication in X.XXX seconds
```

4. Buka browser dan akses:
```
http://localhost:8080
```

### Atau Build JAR:

```bash
# Build project
mvn clean package

# Jalankan JAR
java -jar target/kasir-sederhana-1.0.0.jar
```

## Struktur Project

```
kasir-sederhana/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/kasir/
│       │       ├── KasirApplication.java       # Main class
│       │       ├── controller/
│       │       │   └── KasirController.java    # Controller (Java)
│       │       ├── model/
│       │       │   ├── Product.java            # Model Produk (Java)
│       │       │   ├── CartItem.java           # Model Item (Java)
│       │       │   └── Transaction.java        # Model Transaksi (Java)
│       │       └── service/
│       │           ├── ProductService.java     # Service Produk (Java)
│       │           └── TransactionService.java # Service Transaksi (Java)
│       └── resources/
│           ├── application.properties          # Konfigurasi
│           └── templates/
│               ├── index.html                  # Halaman kasir
│               ├── receipt.html                # Struk pembayaran
│               ├── history.html                # Riwayat transaksi
│               ├── products.html               # Manajemen produk
│               └── report.html                 # Laporan penjualan
└── pom.xml                                     # Maven dependencies
```

## Cara Penggunaan

1. **Halaman Kasir** (`/`): Pilih produk, tambah ke keranjang, checkout
2. **Manajemen Produk** (`/products`): Tambah, edit, hapus produk
3. **Laporan Penjualan** (`/report`): Lihat statistik dan top products
4. **Riwayat Transaksi** (`/history`): Lihat semua transaksi
5. **Export CSV**: Download data transaksi dari halaman laporan

## Konfigurasi MySQL (Opsional)

Untuk menggunakan MySQL dari XAMPP:

1. Start MySQL di XAMPP
2. Buat database baru:
```sql
CREATE DATABASE kasir_db;
```

3. Edit `src/main/resources/application.properties`:
```properties
# Uncomment baris berikut:
spring.datasource.url=jdbc:mysql://localhost:3306/kasir_db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. Restart aplikasi

## Data Produk Default

Aplikasi sudah dilengkapi dengan data produk dummy:
- Nasi Goreng - Rp 15.000
- Mie Goreng - Rp 12.000
- Es Teh - Rp 5.000
- Es Jeruk - Rp 7.000
- Ayam Goreng - Rp 20.000
- Sate Ayam - Rp 18.000

## Troubleshooting


### Error Maven?
```bash
# Clean dan rebuild
mvn clean install
```

### Java version salah?
```bash
# Pastikan Java 17+
java -version
```

## Teknologi Stack

- **Backend**: Java + Spring Boot
- **Frontend**: Thymeleaf + HTML + CSS
- **Build Tool**: Maven
- **Server**: Apache Tomcat (embedded)
- **Database**: In-memory (default) / MySQL (optional)

## Lisensi

Bebas digunakan untuk keperluan pembelajaran dan pengembangan.
