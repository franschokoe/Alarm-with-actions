package com.example.model

data class DanceChallenge(
    val id: String,
    val name: String,
    val emoji: String,
    val tagline: String,
    val description: String,
    val steps: List<String>,
    val poseInstruction: String, // Final pose for the selfie
    val targetDurationSeconds: Int = 15,
    val difficulty: String = "Medium", // Easy, Medium, Wild
    val rhythmBpm: Int = 120,
    val colorHex: Long = 0xFF7C4DFF
)

object DanceChallengeRepository {
    val ALL_CHALLENGES = listOf(
        DanceChallenge(
            id = "the_robot",
            name = "The Morning Robot",
            emoji = "🤖",
            tagline = "Stiff pops, mechanical turns & electric wakes!",
            description = "Lock your joints and move in sharp 90-degree robotic beats. Beep-boop awake!",
            steps = listOf(
                "Lock elbows and punch out one robotic arm at a time.",
                "Turn head stiffly side to side on the beat.",
                "Hit quick robotic pauses every 2 seconds to prove motor control."
            ),
            poseInstruction = "Freeze in your stiffest Robot 🤖 pose and smile wide!",
            targetDurationSeconds = 15,
            difficulty = "Easy",
            rhythmBpm = 110,
            colorHex = 0xFF00E5FF
        ),
        DanceChallenge(
            id = "morning_jig",
            name = "The Sunrise Jig",
            emoji = "☘️",
            tagline = "Fast feet, bouncing knees & Celtic energy!",
            description = "High-stepping, bouncy Celtic jig to get blood pumping to your brain instantly.",
            steps = listOf(
                "Hop on your left foot while kicking right heel out.",
                "Switch legs rapidly with bouncy arm swings.",
                "Stomp 4 times in rhythm: 1, 2, 3, 4!"
            ),
            poseInstruction = "Strike a victorious Irish Jig hands-on-hips jig pose!",
            targetDurationSeconds = 18,
            difficulty = "Medium",
            rhythmBpm = 135,
            colorHex = 0xFF00E676
        ),
        DanceChallenge(
            id = "disco_fever",
            name = "Disco Fever Point",
            emoji = "🕺",
            tagline = "Saturday Night energy on a Monday morning!",
            description = "Point to the ceiling, point to the floor, and sway hips to the groovy 70s disco pulse.",
            steps = listOf(
                "Right hand points up to 45 degrees, left hand on hip.",
                "Slice across body pointing down to opposite knee.",
                "Alternate sides with rhythmic hip dips!"
            ),
            poseInstruction = "Strike the classic Tony Manero index finger pointing to the sky!",
            targetDurationSeconds = 20,
            difficulty = "Medium",
            rhythmBpm = 124,
            colorHex = 0xFFFF4081
        ),
        DanceChallenge(
            id = "funky_chicken",
            name = "The Funky Chicken",
            emoji = "🐔",
            tagline = "Flap your wings and strut out of bed!",
            description = "Tuck hands into armpits, flap your wings vigorously, and do the wake-up strut.",
            steps = listOf(
                "Tuck hands under armpits and flap elbows rapidly.",
                "Bend knees out and bob head back and forth.",
                "Spin around once while squawking away the sleepiness!"
            ),
            poseInstruction = "Full chicken wing flap with eyes wide open and tongue out!",
            targetDurationSeconds = 15,
            difficulty = "Easy",
            rhythmBpm = 128,
            colorHex = 0xFFFFD600
        ),
        DanceChallenge(
            id = "jumping_jack_groove",
            name = "Electric Jumping Jack",
            emoji = "⚡",
            tagline = "Cardio explosion to vaporize grogginess!",
            description = "Full body explosive power jumps with rhythmic clapping above your head.",
            steps = listOf(
                "Jump feet wide while clapping hands high overhead.",
                "Jump back together on the energetic beat.",
                "Keep a steady bouncing rhythm until the meter fills!"
            ),
            poseInstruction = "Clap high above your head with a victory morning grin!",
            targetDurationSeconds = 20,
            difficulty = "Wild",
            rhythmBpm = 140,
            colorHex = 0xFFFF6D00
        ),
        DanceChallenge(
            id = "salsa_spin",
            name = "Salsa Wake-Up Spin",
            emoji = "💃",
            tagline = "Latin rhythm, sharp turns & dazzling footwork!",
            description = "Quick 1-2-3 forward and back steps followed by a dazzling wake-up pirouette.",
            steps = listOf(
                "Step forward with left, step in place, together (1-2-3).",
                "Step back with right, step in place, together (5-6-7).",
                "Do a joyful 360-degree spin to start your day!"
            ),
            poseInstruction = "Give your best dramatic salsa flourish pose!",
            targetDurationSeconds = 20,
            difficulty = "Wild",
            rhythmBpm = 130,
            colorHex = 0xFFE040FB
        )
    )

    fun getTodayChallenge(): DanceChallenge {
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return ALL_CHALLENGES[dayOfYear % ALL_CHALLENGES.size]
    }

    fun getChallengeById(id: String): DanceChallenge {
        return ALL_CHALLENGES.find { it.id == id } ?: ALL_CHALLENGES.first()
    }
}
