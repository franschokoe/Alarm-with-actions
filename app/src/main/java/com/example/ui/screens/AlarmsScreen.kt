package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AlarmEntity
import com.example.model.DanceChallenge
import com.example.ui.components.AddEditAlarmDialog
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EnergeticMint
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SunriseGold

@Composable
fun AlarmsScreen(
    alarms: List<AlarmEntity>,
    todayChallenge: DanceChallenge,
    streakCount: Int,
    onToggleAlarm: (AlarmEntity) -> Unit,
    onSaveAlarm: (AlarmEntity) -> Unit,
    onDeleteAlarm: (AlarmEntity) -> Unit,
    onStartTestMission: (AlarmEntity?) -> Unit,
    onScheduleQuickAlarm: (seconds: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingAlarm by remember { mutableStateOf<AlarmEntity?>(null) }
    var showQuickAlarmSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ElectricPurple,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_alarm_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Alarm", modifier = Modifier.size(28.dp))
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WakeGroove",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                            color = Color.White
                        )
                        Text(
                            text = "Dance & snap to silence your alarm!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0x33FFD600),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SunriseGold)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("🔥", fontSize = 16.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "$streakCount Days",
                                fontWeight = FontWeight.Bold,
                                color = SunriseGold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Hero Banner Card with Illustration
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_morning_dance_hero),
                            contentDescription = "Morning Dance Hero",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color(0xE60F0C20))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Never Hit Snooze Again",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "High-energy movement & smile verification to supercharge your morning.",
                                color = Color(0xFFE2E8F0),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Daily Dance Challenge Feature Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(NeonPink, ElectricPurple))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(todayChallenge.colorHex),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(todayChallenge.emoji, fontSize = 24.sp)
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "TODAY'S DANCE CHALLENGE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonPink,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = todayChallenge.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0x3300E5FF)
                            ) {
                                Text(
                                    text = "${todayChallenge.targetDurationSeconds}s",
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = todayChallenge.tagline,
                            fontSize = 13.sp,
                            color = Color(0xFFCBD5E1)
                        )
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onStartTestMission(null) },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("test_mission_button")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Test Mission", modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Test Routine Now", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    onScheduleQuickAlarm(5)
                                    showQuickAlarmSnackbar = true
                                },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.testTag("quick_alarm_button")
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = "Quick Alarm", tint = SunriseGold, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Ring in 5s", color = SunriseGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (showQuickAlarmSnackbar) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E3A8A),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "⏰ Quick Alarm set for 5 seconds! Keep app open or lock phone.",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { showQuickAlarmSnackbar = false }, modifier = Modifier.size(24.dp)) {
                                Text("✕", color = Color.White)
                            }
                        }
                    }
                }
            }

            // Alarms Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Alarms (${alarms.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            // Alarms List
            if (alarms.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("⏰", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "No alarms scheduled yet",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Tap the + button to create an energetic wake-up alarm!",
                                color = Color(0xFF94A3B8),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                items(alarms, key = { it.id }) { alarm ->
                    AlarmItemCard(
                        alarm = alarm,
                        onToggle = { onToggleAlarm(alarm) },
                        onEdit = { editingAlarm = alarm },
                        onDelete = { onDeleteAlarm(alarm) },
                        onTest = { onStartTestMission(alarm) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditAlarmDialog(
            initialAlarm = null,
            onDismiss = { showAddDialog = false },
            onSave = { newAlarm ->
                onSaveAlarm(newAlarm)
                showAddDialog = false
            }
        )
    }

    if (editingAlarm != null) {
        AddEditAlarmDialog(
            initialAlarm = editingAlarm,
            onDismiss = { editingAlarm = null },
            onSave = { updated ->
                onSaveAlarm(updated)
                editingAlarm = null
            }
        )
    }
}

@Composable
fun AlarmItemCard(
    alarm: AlarmEntity,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTest: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (alarm.isEnabled) MaterialTheme.colorScheme.surface else Color(0xFF131024)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .testTag("alarm_item_${alarm.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = alarm.formattedTime(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = if (alarm.isEnabled) Color.White else Color(0xFF64748B)
                    )
                    Text(
                        text = alarm.label,
                        fontWeight = FontWeight.SemiBold,
                        color = if (alarm.isEnabled) SunriseGold else Color(0xFF64748B),
                        fontSize = 14.sp
                    )
                }

                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ElectricPurple,
                        checkedTrackColor = Color(0xFF311B92)
                    ),
                    modifier = Modifier.testTag("alarm_toggle_${alarm.id}")
                )
            }

            Spacer(Modifier.height(12.dp))

            // Badges row: Repeat Days, Dance duration, Selfie
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = alarm.repeatDaysSummary(),
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0x33FF007A)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("🕺", fontSize = 11.sp)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${alarm.danceTargetSeconds}s Dance",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonPink
                        )
                    }
                }

                if (alarm.requireSelfie) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x3300E676)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("📸", fontSize = 11.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Selfie",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EnergeticMint
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                IconButton(onClick = onTest, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Test", tint = CyberCyan, modifier = Modifier.size(20.dp))
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
