package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AlarmEntity
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SunriseGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditAlarmDialog(
    initialAlarm: AlarmEntity? = null,
    onDismiss: () -> Unit,
    onSave: (AlarmEntity) -> Unit
) {
    var hour by remember { mutableIntStateOf(initialAlarm?.hour ?: 7) }
    var minute by remember { mutableIntStateOf(initialAlarm?.minute ?: 0) }
    var label by remember { mutableStateOf(initialAlarm?.label ?: "Morning Dance Groove") }
    var targetSeconds by remember { mutableIntStateOf(initialAlarm?.danceTargetSeconds ?: 20) }
    var danceIntensity by remember { mutableStateOf(initialAlarm?.danceIntensity ?: "Medium Groove") }
    var requireSelfie by remember { mutableStateOf(initialAlarm?.requireSelfie ?: true) }

    // Repeat days: 1=Sun, 2=Mon, 3=Tue, 4=Wed, 5=Thu, 6=Fri, 7=Sat
    val initialDays = remember {
        initialAlarm?.repeatDays?.split(",")?.mapNotNull { it.trim().toIntOrNull() }?.toSet()
            ?: setOf(2, 3, 4, 5, 6)
    }
    var selectedDays by remember { mutableStateOf(initialDays) }

    val daysOfWeek = listOf(
        1 to "Sun",
        2 to "Mon",
        3 to "Tue",
        4 to "Wed",
        5 to "Thu",
        6 to "Fri",
        7 to "Sat"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialAlarm == null) "Set New Dance Alarm ⏰" else "Edit Alarm ⏰",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Time adjustment row
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Alarm Time",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Hour controls
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(
                                    onClick = { hour = (hour + 1) % 24 },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Hour up", tint = SunriseGold)
                                }
                                val displayH = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                                Text(
                                    text = String.format("%02d", displayH),
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                IconButton(
                                    onClick = { hour = if (hour == 0) 23 else hour - 1 },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Hour down", tint = SunriseGold)
                                }
                            }

                            Text(":", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp))

                            // Minute controls
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(
                                    onClick = { minute = (minute + 5) % 60 },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Min up", tint = SunriseGold)
                                }
                                Text(
                                    text = String.format("%02d", minute),
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                IconButton(
                                    onClick = { minute = if (minute < 5) 55 else minute - 5 },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Min down", tint = SunriseGold)
                                }
                            }

                            Spacer(Modifier.width(16.dp))

                            // AM/PM Toggle
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ElectricPurple,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        hour = if (hour >= 12) hour - 12 else hour + 12
                                    }
                            ) {
                                Text(
                                    text = if (hour < 12) "AM" else "PM",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }

                // Alarm Label
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Alarm Label") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricPurple,
                        focusedLabelColor = ElectricPurple,
                        unfocusedBorderColor = Color(0xFF4B5563)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("alarm_label_input"),
                    singleLine = true
                )

                // Repeat Days selector
                Column {
                    Text(
                        "Repeat Days",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        daysOfWeek.forEach { (dayNum, dayName) ->
                            val isSelected = selectedDays.contains(dayNum)
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ElectricPurple else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        selectedDays = if (isSelected) {
                                            selectedDays - dayNum
                                        } else {
                                            selectedDays + dayNum
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayName.take(1),
                                    color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // Dance Challenge Target Duration
                Column {
                    Text(
                        "Dance Target: $targetSeconds Seconds 🕺",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(10, 15, 20, 30, 45).forEach { sec ->
                            FilterChip(
                                selected = targetSeconds == sec,
                                onClick = { targetSeconds = sec },
                                label = { Text("${sec}s") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonPink,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Require Selfie Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Awake Selfie Mission 📸",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            "Must strike pose & open eyes to dismiss",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = requireSelfie,
                        onCheckedChange = { requireSelfie = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = SunriseGold)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val repeatDaysStr = selectedDays.sorted().joinToString(",")
                    val newOrUpdated = (initialAlarm ?: AlarmEntity(hour = hour, minute = minute)).copy(
                        hour = hour,
                        minute = minute,
                        label = label.ifBlank { "Dance Groove Alarm" },
                        isEnabled = true,
                        repeatDays = repeatDaysStr,
                        danceTargetSeconds = targetSeconds,
                        danceIntensity = danceIntensity,
                        requireSelfie = requireSelfie
                    )
                    onSave(newOrUpdated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                modifier = Modifier.testTag("save_alarm_button")
            ) {
                Text("Save Alarm", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF94A3B8))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}
