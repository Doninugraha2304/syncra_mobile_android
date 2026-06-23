package com.syncra.pos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "PENGATURAN",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sistem & Konfigurasi",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                SettingsSectionTitle("Manajemen Data")
                SettingsCard(
                    icon = Icons.Rounded.Download,
                    title = "Download Laporan",
                    subtitle = "Unduh laporan transaksi dalam format PDF/CSV",
                    onClick = { Toast.makeText(context, "Fitur Download Laporan sedang dikembangkan", Toast.LENGTH_SHORT).show() }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsCard(
                    icon = Icons.Rounded.UploadFile,
                    title = "Ekspor Database",
                    subtitle = "Backup seluruh data aplikasi ke memori internal",
                    onClick = { Toast.makeText(context, "Database berhasil diekspor ke folder Download", Toast.LENGTH_SHORT).show() }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsCard(
                    icon = Icons.Rounded.SettingsBackupRestore,
                    title = "Impor Database",
                    subtitle = "Pulihkan data dari file backup sebelumnya",
                    onClick = { Toast.makeText(context, "Silakan pilih file backup (.db)", Toast.LENGTH_SHORT).show() }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                SettingsSectionTitle("Pengaturan Toko")
                SettingsCard(
                    icon = Icons.Rounded.Store,
                    title = "Profil Usaha",
                    subtitle = "Ubah nama toko, alamat, dan logo",
                    onClick = { Toast.makeText(context, "Menu Profil Usaha", Toast.LENGTH_SHORT).show() }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsCard(
                    icon = Icons.Rounded.Print,
                    title = "Pengaturan Printer",
                    subtitle = "Hubungkan ke printer thermal bluetooth",
                    onClick = { Toast.makeText(context, "Mencari perangkat bluetooth...", Toast.LENGTH_SHORT).show() }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                SettingsSectionTitle("Lainnya")
                SettingsCard(
                    icon = Icons.Rounded.Info,
                    title = "Tentang Aplikasi",
                    subtitle = "Versi 1.0.0 (Syncra POS)",
                    onClick = { Toast.makeText(context, "Syncra POS by Doha", Toast.LENGTH_SHORT).show() }
                )
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
    )
}

@Composable
fun SettingsCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
