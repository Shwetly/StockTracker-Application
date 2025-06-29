# StockTracker - Android Kotlin Application

A beautiful, production-ready Android stock market tracking application built with Kotlin, Jetpack Compose, and modern Android architecture patterns.

## Features

### Core Functionality
- **Real-time Stock Search**: Search for stocks using company names or symbols
- **Live Stock Quotes**: Get current prices, changes, and volume data
- **Interactive Charts**: 30-day price history with detailed information
- **Personal Watchlist**: Save and track your favorite stocks
- **Offline Support**: Local caching with Room database

### Technical Features
- **Modern UI**: Built with Jetpack Compose
- **MVVM Architecture**: Clean architecture with ViewModels
- **Dependency Injection**: Hilt for dependency management
- **API Caching**: Smart caching with Room database
- **Error Handling**: Comprehensive error states with retry functionality
- **Loading States**: Beautiful loading indicators throughout the app
- **Dark/Light Theme**: Material 3 theming support

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Charts**: MPAndroidChart
- **API**: Alpha Vantage for real-time market data
- **Build System**: Gradle with Kotlin DSL

## API Integration

The application uses Alpha Vantage API with the following endpoints:

- **Symbol Search**: Find stocks by name or symbol
- **Global Quote**: Get real-time stock quotes
- **Time Series Daily**: Historical price data for charts

### Rate Limits
- Free tier: 5 API requests per minute, 500 per day
- The app implements intelligent caching to minimize API calls

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+
- Alpha Vantage API key (free at https://www.alphavantage.co/support/#api-key)

### Installation

1. Clone the repository

2. Open the project in Android Studio

3. Get your Alpha Vantage API key
   - Visit https://www.alphavantage.co/support/#api-key
   - Sign up for a free account
   - Copy your API key

4. Update the API key in `app/src/main/java/com/stocktracker/app/data/api/AlphaVantageApi.kt`


5. Build and run the project


### Key Design Patterns
- **MVVM**: Model-View-ViewModel architecture
- **Repository Pattern**: Centralized data access
- **Dependency Injection**: Hilt for loose coupling
- **Single Source of Truth**: Room database as local cache
- **Reactive Programming**: StateFlow and Flow for reactive UI

## Performance Optimizations

- **API Caching**: Room database reduces redundant requests
- **Lazy Loading**: Efficient list rendering with LazyColumn
- **Debounced Search**: Prevents excessive API calls during typing
- **State Management**: Efficient state updates with StateFlow
- **Memory Management**: Proper lifecycle-aware components

## Building APK

To build a release APK:

1. In Android Studio, go to **Build** → **Generate Signed Bundle / APK**
2. Choose **APK**
3. Create or select your keystore
4. Choose **release** build variant
5. Click **Finish**

The APK will be generated in `app/build/outputs/apk/release/`

## Testing

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Acknowledgments

- Alpha Vantage for providing free stock market data
- MPAndroidChart for beautiful chart components
- Material 3 for the design system
- Jetpack Compose for modern Android UI