package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WakeUpRecordEntity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EnergeticMint
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SunriseGold
import java.io.File

@Composable
fun JournalScreen(
    records: List<WakeUpRecordEntity>,
    onDeleteRecord: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Column {
                Text(
                    text = "Wake-Up Journal 📸",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color = Color.White
                )
                Text(
                    text = "Your morning dance victories & awake proof!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Stats Summary
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${records.size}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = SunriseGold
                        )
                        Text(
                            text = "Awake Days",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFF334155)))

                    val totalMoves = records.sumOf { it.danceMovesCount }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$totalMoves",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = EnergeticMint
                        )
                        Text(
                            text = "Dance Moves",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFF334155)))

                    val avgSec = if (records.isNotEmpty()) records.sumOf { it.danceSecondsCompleted } / records.size else 0
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${avgSec}s",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonPink
                        )
                        Text(
                            text = "Avg Routine",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }

        // Empty state
        if (records.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📷", fontSize = 48.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No Awake Selfies Yet",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Complete your first morning alarm dance mission or test a routine to see your awake snapshots here!",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(records, key = { it.id }) { record ->
                WakeRecordCard(
                    record = record,
                    onDelete = { onDeleteRecord(record.id) }
                )
            }
        }
    }
}

@Composable
fun WakeRecordCard(
    record: WakeUpRecordEntity,
    onDelete: () -> Unit
) {
    val bitmap = remember(record.selfieImagePath) {
        record.selfieImagePath?.let { path ->
            val file = File(path)
            if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("journal_card_${record.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selfie image thumbnail
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF242042)),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Awake selfie thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("📸", fontSize = 32.sp)
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.formattedDate(),
                    fontSize = 11.sp,
                    color = SunriseGold,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = record.grooveRating,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0x3300E676)
                    ) {
                        Text(
                            text = "⏱️ ${record.danceSecondsCompleted}s",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EnergeticMint,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0x3300E5FF)
                    ) {
                        Text(
                            text = "⚡ ${record.danceMovesCount} moves",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberCyan,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
            }
        }
    }
}
