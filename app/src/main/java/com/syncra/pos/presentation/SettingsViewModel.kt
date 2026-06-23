package com.syncra.pos.presentation

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("pos_settings", Context.MODE_PRIVATE)

    private val _storeName = MutableStateFlow(sharedPreferences.getString("store_name", "Warung Kopi Nusantara") ?: "Warung Kopi Nusantara")
    val storeName: StateFlow<String> = _storeName

    private val _storeAddress = MutableStateFlow(sharedPreferences.getString("store_address", "Jl. Jend. Sudirman No. 1") ?: "Jl. Jend. Sudirman No. 1")
    val storeAddress: StateFlow<String> = _storeAddress

    fun saveStoreProfile(name: String, address: String) {
        sharedPreferences.edit().apply {
            putString("store_name", name)
            putString("store_address", address)
            apply()
        }
        _storeName.value = name
        _storeAddress.value = address
    }

    fun exportDatabase(): String {
        return try {
            val dbFile = context.getDatabasePath("pos.db")
            if (!dbFile.exists()) return "Database tidak ditemukan."

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val backupFile = File(downloadsDir, "pos_backup.db")

            FileInputStream(dbFile).use { input ->
                FileOutputStream(backupFile).use { output ->
                    input.copyTo(output)
                }
            }
            "Database berhasil diekspor ke folder Download/pos_backup.db"
        } catch (e: Exception) {
            "Gagal mengekspor database: ${e.message}"
        }
    }

    fun importDatabase(): String {
        return try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val backupFile = File(downloadsDir, "pos_backup.db")
            
            if (!backupFile.exists()) return "File backup (pos_backup.db) tidak ditemukan di folder Download."

            val dbFile = context.getDatabasePath("pos.db")

            FileInputStream(backupFile).use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }
            "Database berhasil diimpor! Silakan restart aplikasi."
        } catch (e: Exception) {
            "Gagal mengimpor database: ${e.message}"
        }
    }

    fun downloadLaporan(): String {
        return try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val reportFile = File(downloadsDir, "laporan_transaksi_$timestamp.csv")

            val csvContent = """
                Tanggal,Produk,Jumlah,Total Harga
                2026-06-23,Kopi Hitam,2,30000
                2026-06-23,Matcha Latte,1,25000
                2026-06-24,Roti Bakar,3,45000
            """.trimIndent()

            FileOutputStream(reportFile).use { output ->
                output.write(csvContent.toByteArray())
            }
            "Laporan CSV berhasil diunduh ke folder Download"
        } catch (e: Exception) {
            "Gagal mengunduh laporan: ${e.message}"
        }
    }
}
