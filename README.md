# Project overview
This project contains 2 screens - Course screen and a Record screen. 

## Course screen
- Displays units within a course, organized by different days, in a scrollable list.
- This data is read from a static JSON file in the assets folder.
- Open Record screen when any of these units (displayed as cards) is clicked.

## Record screen
- Displays an upload icon. 
- When clicked, the audio stream is uploaded via a WebSocket connection and text output is received. 
- The base64 encoded audio data is read from JSON file in the assets folder 
- A back navigation button is added to return to the Course screen from the Record screen.

# Architecture

I have used a simple MVVM architecture here, using Jetpack Compose for UI elements, GSON for JSON parsing and OkHttp's WebSocket for streaming.

## Model
    ### CourseService.kt and RecordService.kt
        - These files contain data models for parsing courses, units, days for the Course screen 
        and a data model for parsing asrStream for the Record screen.
        - They also contain associated service functions to load and parse JSON files.
        
## View
    ### CourseScreen.kt and RecordScreen.kt
        - These are written in Jetpack Compose. All UI elements displayed in the app are defined here.

## ViewModel
    ### CourseVm.kt and RecordVM.kt
        - These viewModel classes contain business logic and handle associated states - 
        course and text variables in CourseVM and RecordVM, respectively. 
        - This way, UI remains stateless.

### MainActivity sets up navigation graph, observes and reacts to the state variables defined in the ViewModel classes.

# Preview Tests
    For UI components, I have added @Preview tests for easy viewing of components during app development.

# Improvements to make this app production ready :
    - Implement WebSocket reconnection logic if it errors out.
    - Move WebSocket URLs and headers into config files.
    - Improve exception handling and give more meaningful insights into errors.
    - Use a logging library for centralized logging - differentiate between debug and production logs.
    - Make the UI adaptable to multiple screen sizes and different form factors.
    - Provide log-in screen to customize courses and track progress for each user.
    - Add accessibility support by providing more meaningful descriptions for UI elements and actions and improve contrast for easier usage.
    - Define CI/CD pipeline for continuous integration, testing and deployment as new features are added.
    - Add automation and Integration tests, increase unit test coverage.
    - Add analytics on number of clicks, usability of a feature etc.
    - Define an alert system for exceptions or crashes in production, probably using Grafana and Prometheus dashboards for monitoring.

# Running the project
    - Open the project in Android Studio
    - It has predefined WebSocket URL and Headers, can change to your preferred URL in RevordService.openWebSocket function.
    - Configure an emulator instance
    - Build and hit Run (app)

# Screenshots from the emulator (all three are added under assets folder) :

## Course screen 

![1_CourseScreen.png](app%2Fsrc%2Fmain%2Fassets%2F1_CourseScreen.png)


## Record screen - before hitting upload

![2_RecordScreen.png](app%2Fsrc%2Fmain%2Fassets%2F2_RecordScreen.png)


## Record screen - after hitting upload and receiving text

![3_AfterUploadingAudioStream.png](app%2Fsrc%2Fmain%2Fassets%2F3_AfterUploadingAudioStream.png)

