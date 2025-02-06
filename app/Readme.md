This project contains 2 screens - Course screen and a Record screen. 

Course screen displays units within a course, organized by different days, in a scrollable list. This data is read from a static json file in the assets folder.
When any of these units (displayed as cards) is clicked, Record screen opens.

Record screen displays an upload icon. When clicked, the audio stream is uploaded via a WebSocket connection and text output is received. The base64 encoded audio data is also provided in the assets folder
I have added a back navigation button to return to the Course screen from the Record screen.

Architecture :

I have used a simple MVVM architecture here, using Jetpack Compose for UI elements, GSON for JSON parsing and OkHttp's WebSocket for streaming.
Model : 
    CourseService.kt and RecordService.kt
        These files contain data models for parsing courses, units, days for the Course screen 
        and a data model for parsing asrStream for the Record screen.
        They also contain associated service functions to load and parse JSON files.

View :
    CourseScreen.kt and RecordScreen.kt
        These are written in Jetpack Compose. All UI elements displayed in the app are defined here.

ViewModel :
    CourseVm.kt and RecordVM.kt
        These viewModel classes contain business logic and handle associated states - 
        course and text variables in CourseVM and RecordVM, respectively 
        This way, UI remains stateless.

MainActivity sets up navigation graph, observes and reacts to the state variables defined in the ViewModel classes.

Screenshots from the emulator (all three are added under assets folder) :

Course screen 
![1_CourseScreen.png](src%2Fmain%2Fassets%2F1_CourseScreen.png)

Record screen - before hitting upload
![2_RecordScreen.png](src%2Fmain%2Fassets%2F2_RecordScreen.png)

Record screen - after hitting upload and receiving text
![3_AfterUploadingAudioStream.png](src%2Fmain%2Fassets%2F3_AfterUploadingAudioStream.png)

Preview Tests :
For UI components, I have added @Preview tests for easy viewing of components during app development.
