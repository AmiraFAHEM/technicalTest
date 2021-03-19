
## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) - 
- [Koin](https://github.com/InsertKoinIO/koin) - A pragmatic lightweight dependency injection framework for Kotlin developers..
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Picasso](https://square.github.io/picasso/) - A powerful image downloading and caching library for Android.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.


# Package Structure

    com.example.technicaltest    # Root Package
    .
    ├── data                # For data handling.
    │   ├── api           # Retrofit API for remote end point.
    │   ├── localdb          # Local Persistence Database. Room (SQLite) database
    |   │   ├── dao         # Data Access Object for Room
    │   └── repository      # Single source of data.
    ├── injection                  # Dependency Injection
    ├── model               # Model classes
    └── utils               # Utility Classes / Kotlin extensions
    ├── view                  # Activity/View layer
        ├── albums            # List Albums Screen Activity & ViewModel
        |   ├── adapter     # Adapter for RecyclerView
        |   └── viewmodel   # ViewHolder for RecyclerView
        ├── base            # Base View
        ├── photos            # List Photos Screen Activity & ViewModel
        │   ├── adapter     # Adapter for RecyclerView
        │   └── viewmodel   # ViewHolder for RecyclerView
        ├── users            # List Users Screen Activity & ViewModel
            ├── adapter     # Adapter for RecyclerView
            └── viewmodel   # ViewHolder for RecyclerView



## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)



