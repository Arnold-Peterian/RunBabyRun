
package com.example.journeytrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.journeytrack.ui.theme.JourneyTrackTheme

// Main green colour used throughout the application.
private val Green = Color(0xFF16865B)

// MainActivity is the entry point of the application.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Loads the Jetpack Compose interface using the app theme.
        setContent {
            JourneyTrackTheme {
                JourneyTrackApp()
            }
        }
    }
}

// Main composable function controlling the application screens and navigation.
@Composable
fun JourneyTrackApp() {

    // Stores the currently selected bottom navigation tab.
    var selectedTab by remember { mutableIntStateOf(0) }

    // Tracks whether a journey is currently active.
    var isTracking by remember { mutableStateOf(false) }

    // Stores the user's daily distance goal, initially set to 10 km.
    var dailyGoal by remember { mutableFloatStateOf(10f) }

    // Stores whether dark mode is enabled.
    var darkMode by remember { mutableStateOf(false) }

    // Changes the application colour scheme depending on the selected theme.
    MaterialTheme(
        colorScheme = if (darkMode) darkColorScheme() else lightColorScheme()
    ) {

        // Scaffold provides the main structure of the application.
        Scaffold(

            // Custom bottom navigation bar used to move between screens.
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    // List of available navigation tabs and their icons.
                    val tabs = listOf(
                        "Home" to "⌂",
                        "Track" to "●",
                        "History" to "▤",
                        "Stats" to "▥",
                        "Settings" to "⚙"
                    )

                    // Creates a navigation item for each tab.
                    tabs.forEachIndexed { index, tab ->

                        Column(
                            modifier = Modifier
                                .weight(1f)

                                // Changes the selected screen when a tab is clicked.
                                .clickable {
                                    selectedTab = index
                                }
                                .padding(4.dp)
                                .background(
                                    if (selectedTab == index) {
                                        Green.copy(alpha = 0.12f)
                                    } else {
                                        Color.Transparent
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Displays the navigation icon.
                            Text(
                                text = tab.second,
                                fontSize = 21.sp,
                                color = if (selectedTab == index) {
                                    Green
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )

                            // Displays the navigation label.
                            Text(
                                text = tab.first,
                                fontSize = 11.sp,
                                color = if (selectedTab == index) {
                                    Green
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }

        ) { padding ->

            // Main content area with scrolling enabled.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {

                // Displays the screen corresponding to the selected tab.
                when (selectedTab) {

                    // Home screen with an overview of the user's journeys.
                    0 -> HomeScreen(
                        onTrackClick = {
                            selectedTab = 1
                        }
                    )

                    // Tracking screen for starting and stopping journeys.
                    1 -> TrackScreen(
                        isTracking = isTracking,
                        onTrackingChange = {
                            isTracking = it
                        }
                    )

                    // Displays previous journey records.
                    2 -> HistoryScreen()

                    // Displays activity statistics.
                    3 -> StatsScreen()

                    // Displays application settings and user preferences.
                    4 -> SettingsScreen(
                        dailyGoal = dailyGoal,
                        onGoalChange = {
                            dailyGoal = it
                        },
                        darkMode = darkMode,
                        onDarkModeChange = {
                            darkMode = it
                        }
                    )
                }
            }
        }
    }
}

// HOME SCREEN
// Displays a summary of the user's activity and provides access to tracking.
@Composable
fun HomeScreen(onTrackClick: () -> Unit) {

    Text(
        "JourneyTrack",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )

    Text(
        "Your journeys, all in one place.",
        color = Color.Gray
    )

    Spacer(Modifier.height(25.dp))

    Text(
        "Today's Overview",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(12.dp))

    // Displays distance and duration summary cards.
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        SummaryCard("Distance", "0.0 km", Modifier.weight(1f))
        SummaryCard("Duration", "0 min", Modifier.weight(1f))
    }

    Spacer(Modifier.height(10.dp))

    // Displays total journeys and average speed.
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        SummaryCard("Journeys", "0", Modifier.weight(1f))
        SummaryCard("Avg. Speed", "0 km/h", Modifier.weight(1f))
    }

    Spacer(Modifier.height(25.dp))

    Text(
        "Ready for your next journey?",
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(10.dp))

    Text(
        "Track your walking, cycling or e-scooter journeys " +
                "and keep a record of your progress."
    )

    Spacer(Modifier.height(18.dp))

    // Button navigates directly to the tracking screen.
    Button(
        onClick = onTrackClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Green)
    ) {
        Text("Start a Journey")
    }

    Spacer(Modifier.height(20.dp))

    // Provides basic instructions for new users.
    SectionCard("Getting Started") {
        Text("• Choose your journey type")
        Text("• Start recording your trip")
        Text("• Review your journey history")
        Text("• Monitor your progress")
    }
}

// REUSABLE SUMMARY CARD
// Used to display activity information in a consistent card layout.
@Composable
fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            // Displays the name of the statistic.
            Text(title, color = Color.Gray, fontSize = 13.sp)

            Spacer(Modifier.height(8.dp))

            // Displays the statistic value.
            Text(
                value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// TRACK SCREEN
// Provides the interface for starting and stopping a journey.
@Composable
fun TrackScreen(
    isTracking: Boolean,
    onTrackingChange: (Boolean) -> Unit
) {

    Text(
        "Track Journey",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(8.dp))

    Text("Choose how you want to travel.")

    Spacer(Modifier.height(22.dp))

    // Shows the available types of journeys.
    SectionCard("Journey Type") {
        Text("🚶 Walking")
        Text("🚴 Cycling")
        Text("🛴 E-Scooter")
    }

    Spacer(Modifier.height(20.dp))

    // Displays the current journey information.
    SectionCard("Current Journey") {
        Text("Distance: 0.00 km")
        Text("Duration: 00:00")
        Text("Average speed: 0 km/h")
        Text("Location: Awaiting GPS")
    }

    Spacer(Modifier.height(25.dp))

    // Toggles between starting and stopping a journey.
    Button(
        onClick = {
            onTrackingChange(!isTracking)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isTracking) Color.Red else Green
        )
    ) {
        Text(if (isTracking) "Stop Journey" else "Start Journey")
    }

    Spacer(Modifier.height(12.dp))

    // Displays a message depending on the tracking state.
    Text(
        if (isTracking) {
            "Journey session started. GPS tracking will be added in a later stage."
        } else {
            "Your journey details will appear here when tracking begins."
        },
        color = Color.Gray
    )
}

// HISTORY SCREEN
// Displays previous journeys recorded by the user.
@Composable
fun HistoryScreen() {

    Text(
        "Journey History",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(10.dp))

    Text("Review your previous journeys.")

    Spacer(Modifier.height(20.dp))

    // Placeholder for future journey history records.
    SectionCard("Your Recent Journeys") {
        Text("No journeys recorded yet.")
        Text("Your completed journeys will appear here.")
    }
}

// STATS SCREEN
// Provides an overview of the user's activity statistics.
@Composable
fun StatsScreen() {

    Text(
        "Your Statistics",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(10.dp))

    Text("Keep track of your activity and progress.")

    Spacer(Modifier.height(20.dp))

    // Displays the user's overall activity totals.
    SummaryCard("Total Distance", "0.0 km")

    Spacer(Modifier.height(10.dp))

    SummaryCard("Total Journeys", "0")

    Spacer(Modifier.height(10.dp))

    SummaryCard("Total Duration", "0 hours")

    Spacer(Modifier.height(20.dp))

    // Placeholder for future statistics charts and comparisons.
    SectionCard("Progress Overview") {
        Text("Charts and activity comparisons will be introduced in a future update.")
    }
}

// SETTINGS SCREEN
// Allows the user to customise goals and application appearance.
@Composable
fun SettingsScreen(
    dailyGoal: Float,
    onGoalChange: (Float) -> Unit,
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {

    Text(
        "Settings",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(10.dp))

    Text("Personalise your JourneyTrack experience.")

    Spacer(Modifier.height(20.dp))

    // Allows the user to select a daily distance goal.
    SectionCard("Daily Distance Goal") {

        Text(
            "${dailyGoal.toInt()} km",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Slider allows the goal to be adjusted between 1 and 50 km.
        Slider(
            value = dailyGoal,
            onValueChange = onGoalChange,
            valueRange = 1f..50f
        )
    }

    Spacer(Modifier.height(15.dp))

    // Provides a switch for changing the application theme.
    SectionCard("Appearance") {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode")

            Switch(
                checked = darkMode,
                onCheckedChange = onDarkModeChange
            )
        }
    }

    Spacer(Modifier.height(15.dp))

    // Displays basic application information.
    SectionCard("About") {
        Text("JourneyTrack")
        Text("Version 1.0")
        Text("Your personal journey tracking application.")
    }
}

// REUSABLE SECTION CARD
// Creates a consistent card layout for grouping related content.
@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            // Displays the section heading.
            Text(
                title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            // Displays the content passed into the card.
            content()
        }
    }
}
