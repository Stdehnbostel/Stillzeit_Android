package com.example.stillzeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stillzeit.ui.theme.StillzeitTheme
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        setContent {
            StillzeitTheme {
                // In Tutorials (auch offizielle von Google) wird hier Surface anstelle von Scaffold verwendet
                // Surface ist veraltet, da aktuelle "Foldable" Modelle nicht gut unterstützt wrden.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MeasureFeedingTime(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MeasureFeedingTime(modifier: Modifier = Modifier) {
    var feeding by remember { mutableStateOf(false) }

    var startTimeString by remember { mutableStateOf("") }
    var endTimeString by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DisplayTimeText(
            prefix = R.string.start_time,
            timeString = startTimeString,
            modifier = modifier
        )
        DisplayTimeText(
            prefix = R.string.end_time,
            timeString = endTimeString,
            modifier = modifier
        )
        Button(
            onClick = {
                val now = getSystemTimeStamp()
                if (feeding) {
                    endTimeString = createTimeString(now)
                } else {
                    startTimeString = createTimeString(now)
                }
                feeding = !feeding
            },
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.timestamp_button),
                modifier = modifier
            )
        }
    }
}

@Composable
fun DisplayTimeText(
    @StringRes prefix: Int,
    timeString: String,
    modifier: Modifier = Modifier) {
    Text(
        text = stringResource(prefix, timeString),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    StillzeitTheme {
        MeasureFeedingTime()
    }
}

@VisibleForTesting
internal fun getSystemTimeStamp(): ZonedDateTime {
    val zoneId = ZoneId.systemDefault()
    return ZonedDateTime.now(zoneId) // Der erzeugte Zeitstempel, enthält auch Nanosekunden. Wir benötigen nur Sekunden.
            .truncatedTo(ChronoUnit.SECONDS)
}

@VisibleForTesting
internal fun createTimeString(zonedDateTime: ZonedDateTime): String {
    // DateTimeFormatter erfordert API leven 26.
    // Da Zeitstemple für diese App von zentraler Bedeutung sind
    // stellt dies die untere Schrnake der zu Unterstützenden
    // Android Versionen dar. (Android 8.0)
    val locale = java.util.Locale.GERMANY
    val formatter = DateTimeFormatter
        .ofLocalizedTime(FormatStyle.MEDIUM) // Dieses Format ist für die Anziege geeignet.
        // für die Datenbank: .ISO_INSTANT
        .withLocale(locale)
    return zonedDateTime.format(formatter)
}

@VisibleForTesting
internal fun createTimeStringForDatabase(zonedDateTime: ZonedDateTime): String {
    // DateTimeFormatter erfordert API leven 26.
    // Da Zeitstemple für diese App von zentraler Bedeutung sind
    // stellt dies die untere Schrnake der zu Unterstützenden
    // Android Versionen dar. (Android 8.0)
    val locale = java.util.Locale.GERMANY
    val formatter = DateTimeFormatter
        .ISO_INSTANT // Zur Speicherung in der Datenbank wird das .ISO_Instant-Format benötigt
        .withLocale(locale)
    return zonedDateTime.format(formatter)
}

@Entity (tableName = "breastFeeding")
data class BreastFeeding(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "start") val start: String?,
    @ColumnInfo(name = "end") val end: String?,
    @ColumnInfo(name = "active") val active: Int,
)

@Dao
interface BreastFeedingDao {
    @Query("SELECT * FROM breastFeeding")
    fun getAll(): List<BreastFeeding>

    @Query("SELECT * FROM breastFeeding WHERE uid IN (:breastFeedingIds)")
    fun loadAllByIds(breastFeedingIds: IntArray): List<BreastFeeding>

    @Insert
    fun insertAll(vararg breastFeedings: BreastFeeding)

    @Delete
    fun delete(breastFeeding: BreastFeeding)
}

@Database(entities = [BreastFeeding::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breastFeedingDao(): BreastFeedingDao
}