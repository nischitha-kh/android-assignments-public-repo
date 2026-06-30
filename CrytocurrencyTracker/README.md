# Cryptocurrency Tracker App

A modern Android application for tracking cryptocurrency prices, managing a portfolio, and setting price alerts.

## Architecture

The app follows **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** design pattern.

- **Domain Layer**: Contains core business logic, models, repository interfaces, and use cases. This layer is independent of any other layers.
- **Data Layer**: Implements repository interfaces. It handles data from remote (Retrofit) and local (Room) sources.
- **Presentation Layer**: Built with Jetpack Compose. ViewModels use Coroutines and Flow to manage UI state.

## Tech Stack

- **UI**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + Gson
- **Local Database**: Room
- **Image Loading**: Coil
- **Background Tasks**: WorkManager
- **Asynchronous Programming**: Coroutines + Flow

## Features

- **Dashboard**: View live market prices and 24h changes.
- **Search**: Quickly find coins by name or symbol.
- **Coin Details**: Detailed market statistics and price charts (Sparkline).
- **Portfolio**: Track your holdings, calculate total value, and see Profit/Loss.
- **Price Alerts**: Configure alerts for price thresholds.
- **Background Sync**: Periodic price updates and alert checking even when the app is closed.
- **Notifications**: Local notifications when a price alert is triggered.

## Setup Instructions

1. Clone the repository.
2. Open the project in Android Studio (Ladybug or newer).
3. Wait for Gradle Sync to complete.
4. Run the app on an emulator or physical device (Min SDK 24).

## Assumptions

- **API**: Uses the free CoinGecko API. Note that the free tier has rate limits.
- **Currency**: Currently supports USD only.
- **Refresh Rate**: Background sync is scheduled every 15 minutes (WorkManager minimum interval).

## Testing

- **Unit Tests**: ViewModels and Use Cases are tested using MockK.
- Run tests via terminal: `./gradlew test`
