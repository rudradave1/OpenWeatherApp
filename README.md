# OpenWeatherApp

The Open Weather App is an Android application written in Kotlin that utilizes Open Weather API keys to fetch current weather and 5-day weather forecast data for a user-specified location. The app is built using the MVVM clean architecture pattern, which helps maintain a separation of concerns and promotes testability.

## Features

- Fetches current weather data for a specific location using Open Weather API
- Retrieves 5-day weather forecast data for a user-specified location
- Displays weather information including temperature, humidity, wind speed, and more

## Technologies Used

- Kotlin: The programming language for Android app development
- Android Architecture Components: Including ViewModel and LiveData for implementing the MVVM pattern
- Retrofit: A type-safe HTTP client for making API requests
- Open Weather API: Provides weather data for various locations

## Getting Started

Follow the instructions below to get a copy of the project up and running on your local machine.

### Prerequisites

- Android Studio: The latest version of Android Studio must be installed on your machine.
- Open Weather API Key: Obtain an API key from [Open Weather](https://openweathermap.org/) by creating an account.

### Installation

1. Clone the repository:

git clone https://github.com/your-username/open-weather-app.git

2. Open Android Studio and select "Open an existing project".

3. Navigate to the cloned repository and select the project.

4. In the project, create a `local.properties` file and add your Open Weather API key:
   
openWeatherApiKey="YOUR_API_KEY"

5. Build and run the project on an Android device or emulator.

## Contribution

Contributions to the Open Weather App project are welcome. If you find any issues or want to enhance the app with new features, feel free to submit a pull request.

## Acknowledgments

- [Open Weather](https://openweathermap.org/): The Open Weather App utilizes the Open Weather API for fetching weather data.
