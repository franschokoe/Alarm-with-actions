package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AlarmEntity
import com.example.model.DanceChallengeRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("WakeGroove", appName)
    }

    @Test
    fun `verify dance challenge repository contains robot and jig routines`() {
        val challenges = DanceChallengeRepository.ALL_CHALLENGES
        assertTrue(challenges.isNotEmpty())
        val robot = challenges.find { it.id == "the_robot" }
        assertNotNull(robot)
        assertEquals("The Morning Robot", robot?.name)

        val jig = challenges.find { it.id == "morning_jig" }
        assertNotNull(jig)
        assertEquals("The Sunrise Jig", jig?.name)
    }

    @Test
    fun `verify alarm entity formatted time`() {
        val alarm = AlarmEntity(hour = 7, minute = 30)
        assertEquals("7:30 AM", alarm.formattedTime())

        val afternoonAlarm = AlarmEntity(hour = 16, minute = 5)
        assertEquals("4:05 PM", afternoonAlarm.formattedTime())
    }
}
