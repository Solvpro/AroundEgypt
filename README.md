# Around Egypt Android App

A modern Android application showcasing virtual tour experiences in Egypt, built with Jetpack Compose and Clean Architecture.

## Features
- Browse recommended experiences
- View recent experiences
- Search for experiences
- Like experiences
- Offline support with caching
- Clean Architecture with MVVM pattern

## Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Networking**: Retrofit + OkHttp
- **Database**: Room
- **Image Loading**: Coil

## Project Structure
The project follows Clean Architecture principles with three main layers:
- **Data Layer**: Handles data operations (API, Database)
- **Domain Layer**: Business logic and use cases
- **Presentation Layer**: UI components and ViewModels

## Setup Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run the app

## API Endpoints
Base URL: `http://aroundegypt.34ml.com`
- GET `/api/v2/experiences?filter[recommended]=true` - Recommended experiences
- GET `/api/v2/experiences` - Recent experiences
- GET `/api/v2/experiences?filter[title]={search_text}` - Search
- GET `/api/v2/experiences/{id}` - Single experience
- POST `/api/v2/experiences/{id}/like` - Like experience

## Testing
Run tests using:
```bash
./gradlew test
./gradlew connectedAndroidTest