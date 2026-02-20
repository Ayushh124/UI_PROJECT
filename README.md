# SteadyCash

A dark-themed Android financial tracking app built with **Kotlin** and **Jetpack Compose**. It includes a landing screen, home screen with transaction list and draggable sheet, and an Insights screen with period-based charts and expense groups.

---

## Features

- **Landing screen** – Welcome / onboarding with "Proceed" navigation to Home
- **Home screen** – Welcome header, red card, draggable bottom sheet with transaction list (grouped by date)
- **Insights screen** – Available balance, period tabs (7 days / Month / Year), year selector, expenditure total, bar charts (7-day, 12-month, 10-year), and 4 expense group cards whose sum equals the expenditure
- **Bottom navigation** – Home, center Add icon (non-clickable), Insights
- **Theme** – Dark background, red accent, Material 3

---

## Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose, Material 3  
- **Navigation:** Navigation Compose  
- **State:** `remember` + `mutableStateOf`  
- **Data:** In-memory sample data (no backend)

---

## Project Structure

```
app/src/main/java/com/steadycash/app/
├── MainActivity.kt              # App entry, Compose theme + NavHost
├── data/
│   ├── DataModels.kt            # Transaction, TransactionGroup, ExpenseGroupSummary, DayAmount
│   └── SampleData.kt            # Mock data: balance, 7d, 12mo, 10y, expense groups, transactions
├── navigation/
│   └── SteadyCashNav.kt         # Routes, NavHost, bottom bar
└── ui/
    ├── theme/
    │   ├── Color.kt
    │   └── Theme.kt
    ├── screens/
    │   ├── LandingScreen.kt
    │   ├── HomeScreen.kt
    │   └── InsightsScreen.kt
    └── components/
        └── HomeBottomSheetContent.kt   # Quick add (Add income / Add expense) – not yet wired
```

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Ayushh124/UI_PROJECT.git
   cd UI_PROJECT
   ```
2. Open the project in **Android Studio** (or compatible IDE).
3. Sync Gradle and run on an **Android emulator** or device (min SDK as defined in `app/build.gradle.kts`).

---

## License

This project is for educational / portfolio use.
