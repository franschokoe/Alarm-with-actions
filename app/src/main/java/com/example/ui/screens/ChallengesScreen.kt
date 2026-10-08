package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChallengeCompletionEntity
import com.example.model.DanceChallenge
import com.example.model.DanceChallengeRepository
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EnergeticMint
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SunriseGold

@Composable
fun ChallengesScreen(
    completions: List<ChallengeCompletionEntity>,
    onSelectAndStartChallenge: (DanceChallenge) -> Unit,
    modifier: Modifier = Modifier
) {
    val allChallenges = remember { DanceChallengeRepository.ALL_CHALLENGES }
    val todayChallenge = remember { DanceChallengeRepository.getTodayChallenge() }

    // Map completions by challenge ID
    val completionCounts = remember(completions) {
        completions.groupBy { it.challengeId }.mapValues { it.value.size }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Stats
        item {
            Column {
                Text(
                    text = "Dance Challenges 🕺",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color = Color.White
                )
                Text(
                    text = "Perform daily routines to conquer the sleep demon!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Stats Banner
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
                            text = "${completions.size}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = SunriseGold
                        )
                        Text(
                            text = "Challenges Beaten",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFF334155))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val uniqueMastered = completionCounts.keys.size
                        Text(
                            text = "$uniqueMastered / ${allChallenges.size}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = EnergeticMint
                        )
                        Text(
                            text = "Routines Mastered",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFF334155))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "100%",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonPink
                        )
                        Text(
                            text = "Awake Rate",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Weekly Dance Quest Progress
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = "Trophy", tint = SunriseGold)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Weekly Groove Quest",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        }
                        val weekTarget = 5
                        val weekCompleted = completions.size.coerceAtMost(weekTarget)
                        Text(
                            text = "$weekCompleted / $weekTarget this week",
                            color = SunriseGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    val progress = (completions.size.toFloat() / 5f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = SunriseGold,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (progress >= 1f) "🏆 Weekly Quest Achieved! You are a morning groove legend!" else "Beat 5 dance routines this week to maintain your Master Groover rank!",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Section header
        item {
            Text(
                text = "Select & Practice Routines",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        // Dance Challenges list
        items(allChallenges, key = { it.id }) { challenge ->
            val count = completionCounts[challenge.id] ?: 0
            val isToday = challenge.id == todayChallenge.id

            DanceChallengeCard(
                challenge = challenge,
                completionCount = count,
                isTodayChallenge = isToday,
                onStart = { onSelectAndStartChallenge(challenge) }
            )
        }
    }
}

@Composable
fun DanceChallengeCard(
    challenge: DanceChallenge,
    completionCount: Int,
    isTodayChallenge: Boolean,
    onStart: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (isTodayChallenge) androidx.compose.foundation.BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(NeonPink, ElectricPurple))) else null,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("challenge_card_${challenge.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(challenge.colorHex),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(challenge.emoji, fontSize = 26.sp)
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = challenge.name,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            if (isTodayChallenge) {
                                Spacer(Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = NeonPink
                                ) {
                                    Text(
                                        "TODAY",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = challenge.tagline,
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                // Completion count badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (completionCount > 0) Color(0x3300E676) else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        if (completionCount > 0) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Completed", tint = EnergeticMint, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                        }
                        Text(
                            text = if (completionCount > 0) "$completionCount Done" else "0 Done",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (completionCount > 0) EnergeticMint else Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Details tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "⏱️ ${challenge.targetDurationSeconds}s",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "⚡ ${challenge.difficulty}",
                        fontSize = 11.sp,
                        color = when (challenge.difficulty) {
                            "Easy" -> EnergeticMint
                            "Medium" -> SunriseGold
                            else -> NeonPink
                        },
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "🎵 ${challenge.rhythmBpm} BPM",
                        fontSize = 11.sp,
                        color = CyberCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Steps & Selfie instructions
            Spacer(Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF141226),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Dance Routine Steps:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SunriseGold
                    )
                    Spacer(Modifier.height(6.dp))
                    challenge.steps.forEachIndexed { idx, step ->
                        Text(
                            text = "${idx + 1}. $step",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "📸 Required Selfie Pose:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan
                    )
                    Text(
                        text = challenge.poseInstruction,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("start_challenge_${challenge.id}")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start", modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Start This Challenge!", fontWeight = FontWeight.Bold)
            }
        }
    }
}
