package com.example.stillzeit.ui

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.example.stillzeit.data.BreastFeeding
import com.example.stillzeit.data.BreastFeedingDetail
import com.example.stillzeit.data.Feeding
import com.example.stillzeit.data.Meal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlin.uuid.ExperimentalUuidApi
import java.util.UUID
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.time.toKotlinDuration

class AddMealViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        AddMealUiState(null, false,getSystemTimeStamp())
    )

    val uiState: StateFlow<AddMealUiState> = _uiState.asStateFlow()

    fun processTimestampButton(side: String): String {
        val durationString: String
        val now = getSystemTimeStamp()
        if (_uiState.value.feeding) {

            val endTimeString = createTimeStringForDatabase(now)

            val currentSite = getLastFeeding()?.detail?.side ?: return "Error"
            durationString = createDurationString(_uiState.value.start, now, currentSite)
            endFeeding(endTimeString)

            if (currentSite != side) {
                val newSite = if (currentSite == "left") { "right" } else if (currentSite == "right") { "left" } else { return "error" }
                return processTimestampButton(newSite)
            }
        } else {
            _uiState.update {
                currentState ->
                currentState.copy(
                    start = now
                )
            }
            val startTimeString = createTimeStringForDatabase(now)

            val id = UUID.randomUUID()
            startFeeding(id, startTimeString, side)
            durationString = updateTimeString()
        }
        return durationString
    }

    fun endFeeding(end: String) {
        val feedings = _uiState.value.meal?.breastFeedings ?: return
        val index = feedings.size - 1
        val lastFeeding = feedings[index]
        val updatedFeedingDetail = BreastFeedingDetail(lastFeeding.detail.uid, lastFeeding.detail.start, end, false, lastFeeding.detail.side, lastFeeding.detail.feedingId)
        updateLastFeeding(BreastFeeding(lastFeeding.feeding, updatedFeedingDetail))
        _uiState.update {
                currentState ->
            currentState.copy(
                feeding = false
            )
        }
    }

    fun startFeeding(id: UUID, start: String, side: String) {
        if (_uiState.value.meal == null) {
            setMealStart(start)
        }

        val feeding = Feeding(id, 0, "breast", _uiState.value.meal?.uuid ?: return)
        val detail = BreastFeedingDetail(0, start, null, true, side, id)
        val breastFeeding = BreastFeeding(feeding, detail)
        addFeedingToMeal(breastFeeding)
        _uiState.update {
            currentState ->
            currentState.copy(
                feeding = true
            )
        }
    }

    fun updateLastFeeding(feeding: BreastFeeding) {
        val end = feeding.detail.end
        if (end != null) {
            setMealEnd(end)
        }
        _uiState.update { currentState ->
            val newMeal = currentState.meal
            if (newMeal == null) {
                currentState.copy()
            } else {
                var feedings = currentState.meal.breastFeedings.toMutableList()
                val index = feedings.size - 1
                feedings[index] = feeding

                feedings[feedings.size - 1] = feeding
                newMeal.breastFeedings = feedings
                currentState.copy(
                    meal = newMeal
                )
            }
        }
    }

    fun getLastFeeding(): BreastFeeding? {
        val breastFeedings = _uiState.value.meal?.breastFeedings
        if (breastFeedings != null) {
            val lastFeeding = breastFeedings[breastFeedings.size - 1]
            return lastFeeding
        } else {
            return null
        }
    }

    fun addFeedingToMeal(feeding: BreastFeeding) {
        _uiState.update { currentState ->
            val newMeal = currentState.meal
            if (newMeal != null) {
                newMeal.breastFeedings = newMeal.breastFeedings + listOf(feeding)
                newMeal.numberOfFeedings += 1
                currentState.copy(
                    meal = newMeal
                )
            } else {
                currentState.copy()
            }
        }
        val end = feeding.detail.end
        if (end != null) {
            setMealEnd(end)
        }
    }

    fun setMealStart(mealStart: String) {
        _uiState.update { currentState ->
            val oldMeal = currentState.meal
            currentState.copy(
                meal = oldMeal
                    ?: Meal(
                        UUID.randomUUID(),
                        0,
                        "breast",
                        mealStart,
                        "",
                        "oldMeal.comment",
                        listOf()
                    )
            )
        }
    }

    fun getAccumulatedDuration(side: String): kotlin.time.Duration {
        val feedings = _uiState.value.meal?.breastFeedings ?: return kotlin.time.Duration.ZERO

        val filteredFeedings = feedings.filter {
            feeding ->
            feeding.detail.side == side && !feeding.detail.active
        }

        val feedingTimes = filteredFeedings.map {
            feeding ->
            val start = ZonedDateTime.parse(feeding.detail.start)
            val end = ZonedDateTime.parse(feeding.detail.end)
            return@map calculateDuration(start, end)
        }

        return if (!feedingTimes.isEmpty()) {
            feedingTimes.reduce { acc, value ->
                acc + value
            }
        } else {
            kotlin.time.Duration.ZERO
        }
    }

    fun setMealEnd(mealEnd: String) {
        _uiState.update { currentState ->
            val oldMeal = currentState.meal
            currentState.copy(
                meal = if (oldMeal != null) {
                    Meal(
                        oldMeal.uuid,
                        oldMeal.numberOfFeedings,
                        oldMeal.type,
                        oldMeal.start,
                        mealEnd,
                        oldMeal.comment,
                        oldMeal.breastFeedings
                    )
                } else {
                    oldMeal
                }
            )
        }
    }

    fun endMeal() {
        _uiState.update {
            currentState -> currentState.copy(
                meal = null
            )
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
        val locale = Locale.GERMANY
        val formatter = DateTimeFormatter
            .ofLocalizedTime(FormatStyle.MEDIUM) // Dieses Format ist für die Anzeige geeignet.
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
        val locale = Locale.GERMANY
        val formatter = DateTimeFormatter
            .ISO_INSTANT // Zur Speicherung in der Datenbank wird das .ISO_Instant-Format benötigt
            .withLocale(locale)
        return zonedDateTime.format(formatter)
    }

    @VisibleForTesting
    internal fun calculateDuration(start: ZonedDateTime, end: ZonedDateTime): kotlin.time.Duration {
        val duration = Duration.between(start, end)
        // umwandlung in KotlinDuration, da meine Duration Umsetzung
        // Api level 31 benötigt hätte. Nicht sehr elegant. Wahrscheinlich geht das shöner.
        return duration.toKotlinDuration()
    }

    // Für das Label des Buttons. Später soll die Anzeige laufend aktualisiert werden.
    @VisibleForTesting
    internal fun createDurationString(start: ZonedDateTime, end: ZonedDateTime, side: String): String {
        val duration = calculateDuration(start, end) + getAccumulatedDuration(side)
        var seconds = duration.inWholeSeconds
        val hours = seconds / 3600
        seconds -= hours * 3600
        val minutes = seconds / 60
        seconds -= minutes * 60

        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }

    fun updateTimeString(): String {
        val now = getSystemTimeStamp()
        val currentSide = getLastFeeding()?.detail?.side ?: return "Error"
        return createDurationString(_uiState.value.start, now, currentSide)
    }
}

data class AddMealUiState(
    val meal: Meal?,
    val feeding: Boolean,
    val start: ZonedDateTime,
)