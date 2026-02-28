# CoinScanner

CoinScanner is a modern Android application built to track cryptocurrency market data and manage your portfolio. It leverages the latest Android development tools and best practices, including Jetpack Compose, Material 3, and Clean Architecture.

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/jtorrestobena/coinscanner/tree/main.svg?style=svg&circle-token=16e29414ace5991b4ecf9bdc0eec6accbc078328)](https://dl.circleci.com/status-badge/redirect/gh/jtorrestobena/coinscanner/tree/main)
[![CircleCI](https://dl.circleci.com/insights-snapshot/gh/jtorrestobena/coinscanner/main/build-test/badge.svg?window=30d&circle-token=f1da131f6d960e5051f881a98bda6e6cd73f8197)](https://app.circleci.com/insights/github/jtorrestobena/coinscanner/workflows/build-test/overview?branch=main&reporting-window=last-30-days&insights-snapshot=true)

## Features

- **Market Overview:** Browse real-time cryptocurrency market data powered by CoinGecko.
- **Portfolio Tracking:** Keep track of your crypto holdings and performance.
- **Currency Selection:** Support for multiple fiat currencies.
- **Responsive Design:** Optimized for various screen sizes (Phones, Tablets, and Foldables) using WindowSizeClass.
- **Offline Support:** Local caching of data using Room database.
- **Dark/Light Mode:** Full support for Material 3 dynamic color and themes.

## Tech Stack

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a declarative UI.
- **Design System:** [Material 3](https://m3.material.io/) for modern look and feel.
- **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture principles.
- **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for robust DI.
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) for API communication.
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) for persistent data storage.
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/compose/) for optimized image loading.
- **Pagination:** [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data) for efficient list loading.
- **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for seamless screen transitions.
- **Static Analysis:** [Detekt](https://detekt.dev/) for code quality.

## Project Structure

```text
com.bytecoders.coinscanner
├── data          # Data layer: API services, Room DB, Repositories
├── di            # Dependency Injection modules
├── repository    # Repository implementations
├── ui            # UI layer: Compose screens, ViewModels, Theme
│   ├── home      # Market listing
│   ├── portfolio # User's assets
│   ├── navigation# Navigation graph and items
│   └── theme     # Material 3 theme definitions
└── service       # Services (Rest APIs)
```

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jtorrestobena/coinscanner.git
   ```

2. **API Keys:**
   The project requires a RapidAPI key for some features. Add it to your environment variables:
   ```bash
   export RAPID_API_KEY="your_api_key_here"
   ```
   Alternatively, you can configure it in your `local.properties`.

3. **Build:**
   Open the project in Android Studio (Electric Eel or newer recommended) and sync Gradle.

## CI/CD

The project uses **CircleCI** for continuous integration, ensuring that every PR passes:
- Unit Tests
- Android Lint
- Detekt Static Analysis
- Build checks

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
