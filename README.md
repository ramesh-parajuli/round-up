RoundUpSavingApp README

Welcome to RoundUpSavingApp! This Android app helps you manage and track your savings goals, calculate round-up amounts from your transactions, and transfer the round-up savings to specific goals. It uses modern Android development techniques such as Hilt for Dependency Injection, Jetpack Compose for UI, and MVVM architecture for clean separation of concerns.

Features

Round-Up Savings: Calculate round-up savings from your transactions and transfer them to specific goals.
Goal Tracking: Set and track savings goals to monitor your progress.
Prerequisites

Before you begin, make sure you have the following installed:

Android Studio (latest version)
Kotlin 

1. Clone the repository
   Clone the repository to your local machine:

2. Open the project in Android Studio
   Open the project in Android Studio and let it sync the dependencies.

3. Install dependencies
   Install the necessary dependencies by running:

./gradlew build
4. Set up the ACCESS_TOKEN
   To get started with the app, sign up and create a customer from the provided link, and obtain an access token. Then, add the token to the constant file in the project:

// In Constants.kt
const val ACCESS_TOKEN = "your_access_token_here"
5. Run the app
   Connect a physical device or start an emulator, and click on Run in Android Studio to build and run the app on your device.

Architecture

The app follows the MVVM architecture pattern and uses Hilt for dependency injection to maintain clean separation of concerns. The UI is built using Jetpack Compose, and the app uses Kotlin Coroutines and Flow for handling asynchronous operations and streaming data.

Dependency Injection (DI) with Hilt
Hilt is used for DI to inject dependencies throughout the app, ensuring that components such as ViewModels and repositories are properly managed.

UI with Jetpack Compose
The UI is built using Jetpack Compose, providing a declarative way to design the user interface.

ViewModel
The app follows the MVVM architecture pattern, where the ViewModel manages UI-related data and interacts with the repository.

Unit Tests
The app includes unit tests for important features. You can add your tests using JUnit and MockK for mocking dependencies.
Technologies Used

Kotlin: Backend logic and Android functionality.
Jetpack Compose: UI development for Android.
Hilt: Dependency Injection.
Kotlin Coroutines: For asynchronous tasks and network operations.
Flow: For handling streams of data.
JUnit: For writing unit tests.
MockK: For mocking dependencies in unit tests.
Retrofit: For making API requests (if applicable).
