package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camera.CameraSelfieView
import com.example.model.DanceChallenge
import com.example.sensor.DanceMotionState
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EnergeticMint
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SunriseGold
import com.example.viewmodel.WakeMissionStage

@Composable
fun WakeMissionScreen(
    stage: WakeMissionStage,
    challenge: DanceChallenge,
    alarmLabel: String,
    motionState: DanceMotionState,
    isRinging: Boolean,
    onSimulateDanceStep: () -> Unit,
    onSelfieConfirmed: (filePath: String) -> Unit,
    onDismissMission: () -> Unit,
    onSnooze: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Intercept back button during mission: user must complete routine or snooze!
    BackHandler {
        if (stage == WakeMissionStage.CELEBRATION) {
            onDismissMission()
        } else {
            onSnooze()
        }
    }

    var confirmedSelfieBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF130E26), Color(0xFF0A0714))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Alarm Title & Ringing Pulse
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "bellPulse")
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 0.9f,
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(400),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "bellPulseScale"
                    )

                    Icon(
                        Icons.Default.NotificationsActive,
                        contentDescription = "Ringing",
                        tint = NeonPink,
                        modifier = Modifier
                            .size(32.dp)
                            .scale(if (isRinging) pulseScale else 1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isRinging) "ALARM RINGING!" else "WAKE MISSION",
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = NeonPink,
                        letterSpacing = 1.5.sp
                    )
                }

                Text(
                    text = alarmLabel,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(12.dp))

                // Progress Step Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepChip(
                        number = "1",
                        label = "Dance Routine",
                        isActive = stage == WakeMissionStage.DANCE_CHALLENGE,
                        isComplete = stage == WakeMissionStage.AWAKE_SELFIE || stage == WakeMissionStage.CELEBRATION
                    )
                    Box(modifier = Modifier.width(20.dp).height(2.dp).background(Color(0xFF334155)))
                    StepChip(
                        number = "2",
                        label = "Awake Selfie",
                        isActive = stage == WakeMissionStage.AWAKE_SELFIE,
                        isComplete = stage == WakeMissionStage.CELEBRATION
                    )
                    Box(modifier = Modifier.width(20.dp).height(2.dp).background(Color(0xFF334155)))
                    StepChip(
                        number = "3",
                        label = "Dismissed",
                        isActive = stage == WakeMissionStage.CELEBRATION,
                        isComplete = stage == WakeMissionStage.CELEBRATION
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Main Mission Stage Content
            when (stage) {
                WakeMissionStage.READY, WakeMissionStage.DANCE_CHALLENGE -> {
                    DanceStageContent(
                        challenge = challenge,
                        motionState = motionState,
                        onSimulateStep = onSimulateDanceStep,
                        onSnooze = onSnooze
                    )
                }

                WakeMissionStage.AWAKE_SELFIE -> {
                    SelfieStageContent(
                        challenge = challenge,
                        onSelfieConfirmed = { path, bmp ->
                            confirmedSelfieBitmap = bmp
                            onSelfieConfirmed(path)
                        }
                    )
                }

                WakeMissionStage.CELEBRATION -> {
                    CelebrationStageContent(
                        challenge = challenge,
                        motionState = motionState,
                        selfieBitmap = confirmedSelfieBitmap,
                        onFinish = onDismissMission
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun StepChip(number: String, label: String, isActive: Boolean, isComplete: Boolean) {
    val bgColor = when {
        isComplete -> EnergeticMint
        isActive -> ElectricPurple
        else -> Color(0xFF1E293B)
    }
    val contentColor = when {
        isComplete -> Color.Black
        isActive -> Color.White
        else -> Color(0xFF64748B)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isComplete) "✓" else number,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = contentColor
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
        }
    }
}

@Composable
fun DanceStageContent(
    challenge: DanceChallenge,
    motionState: DanceMotionState,
    onSimulateStep: () -> Unit,
    onSnooze: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Challenge info card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(2.dp, Brush.horizontalGradient(listOf(Color(challenge.colorHex), NeonPink))),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(challenge.emoji, fontSize = 56.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = challenge.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = challenge.tagline,
                    fontSize = 13.sp,
                    color = SunriseGold,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                // Steps quick summary
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF141224),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        challenge.steps.take(2).forEachIndexed { i, step ->
                            Text(
                                text = "• $step",
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1),
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Large Circular Energy & Progress Gauge
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background track ring
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF242042),
                strokeWidth = 14.dp
            )

            // Dynamic progress ring
            CircularProgressIndicator(
                progress = { motionState.progress },
                modifier = Modifier.fillMaxSize(),
                color = if (motionState.progress >= 0.8f) EnergeticMint else NeonPink,
                strokeWidth = 14.dp
            )

            // Inner stats display
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val remainingSeconds = (motionState.targetSeconds - motionState.accumulatedDanceSeconds).toInt().coerceAtLeast(0)
                Text(
                    text = "${(motionState.progress * 100).toInt()}%",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "${remainingSeconds}s Left",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SunriseGold
                )
                Text(
                    text = "${motionState.totalMovesCount} Moves",
                    fontSize = 12.sp,
                    color = CyberCyan
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Rhythm Equalizer bars animation reflecting real-time intensity
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.height(36.dp)
        ) {
            listOf(0.4f, 0.7f, 1.0f, 0.6f, 0.9f, 0.5f, 0.8f, 1.0f, 0.7f).forEachIndexed { index, weight ->
                val barHeight = ((motionState.currentIntensity * weight + 0.15f).coerceIn(0.15f, 1.0f) * 36).dp
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .height(barHeight)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.verticalGradient(listOf(NeonPink, ElectricPurple, CyberCyan))
                        )
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Live Encouraging Status
        Text(
            text = motionState.statusText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (motionState.isDancing) EnergeticMint else Color.White
        )

        if (motionState.comboMultiplier > 1) {
            Text(
                text = "🔥 x${motionState.comboMultiplier} GROOVE COMBO!",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = SunriseGold
            )
        }

        Spacer(Modifier.height(20.dp))

        // Interactive Simulation Button for emulator or testing
        Button(
            onClick = onSimulateStep,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("simulate_dance_button")
        ) {
            Icon(Icons.Default.DirectionsRun, contentDescription = "Step", modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text("Tap Dance Step / Shake Phone! 📱", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(10.dp))

        // Emergency Snooze
        OutlinedButton(
            onClick = onSnooze,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.testTag("snooze_button")
        ) {
            Icon(Icons.Default.Snooze, contentDescription = "Snooze", tint = Color(0xFF94A3B8))
            Spacer(Modifier.width(6.dp))
            Text("Emergency Snooze (5 min penalty)", color = Color(0xFF94A3B8), fontSize = 12.sp)
        }
    }
}

@Composable
fun SelfieStageContent(
    challenge: DanceChallenge,
    onSelfieConfirmed: (filePath: String, bitmap: Bitmap) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0x3300E676),
            border = androidx.compose.foundation.BorderStroke(1.dp, EnergeticMint)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("🎉 Dance routine complete!", fontWeight = FontWeight.Bold, color = EnergeticMint)
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Step 2: Proof of Awakeness 📸",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Text(
            text = "Snap your selfie while holding the final pose!",
            fontSize = 13.sp,
            color = Color(0xFFCBD5E1),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        CameraSelfieView(
            poseInstruction = "${challenge.emoji} Pose: ${challenge.poseInstruction}",
            onSelfieConfirmed = onSelfieConfirmed,
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
        )
    }
}

@Composable
fun CelebrationStageContent(
    challenge: DanceChallenge,
    motionState: DanceMotionState,
    selfieBitmap: Bitmap?,
    onFinish: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = CircleShape,
            color = EnergeticMint,
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Check, contentDescription = "Success", tint = Color.Black, modifier = Modifier.size(44.dp))
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "ALARM SILENCED! 🔕",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = EnergeticMint
        )
        Text(
            text = "You danced, you smiled, you conquered sleep!",
            fontSize = 14.sp,
            color = Color(0xFFCBD5E1),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        // Polaroid celebration card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(2.dp, Brush.horizontalGradient(listOf(SunriseGold, NeonPink))),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selfieBitmap != null) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            bitmap = selfieBitmap.asImageBitmap(),
                            contentDescription = "Morning Awake Proof",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "${challenge.emoji} ${challenge.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Dance Duration: ${motionState.targetSeconds}s • Moves: ${motionState.totalMovesCount}",
                    fontSize = 12.sp,
                    color = SunriseGold
                )

                Spacer(Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0x3300E5FF)
                ) {
                    Text(
                        text = "100% WIDE AWAKE & ENERGIZED! ✨",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onFinish,
            colors = ButtonDefaults.buttonColors(containerColor = EnergeticMint),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("start_day_button")
        ) {
            Text("Start My Day! ☀️", color = Color.Black, fontSize = 17.sp, fontWeight = FontWeight.Black)
        }
    }
}
