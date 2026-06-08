Yeh raha aapke "Waves of Food" app ke liye ek professional aur detailed
README.md file ka content. Aap ise copy karke apne project ke root folder mein
README.md naam se save kar sakte hain.

🌊 Waves of Food - Modern Food Delivery & Utility App

Waves of Food ek high-performance Android application hai jo modern development
practices aur clean architecture ko follow karta hai. Yeh app na sirf ek premium
food delivery experience deta hai balki isme daily utility tools jaise
Calculator aur Game bhi integrated hain.

🚀 Features

🍱 Food Delivery (Main Module)

  - Live Menu: Retrofit aur API integration ke zariye real-time food data load
    hota hai.
  - Smart Search: Dish name, cuisine, ya restaurant ke basis par fast filtering.
  - Advanced UI: Zomato/Swiggy style nested horizontal aur vertical scrolling
    layouts.
  - Real-time Cart: Firebase Firestore ke through managed live cart system (No
    Refresh required).
  - Checkout Flow: Seamless order placement logic with custom success
    animations.

🛠 Utility & Games

  - Pro Calculator: History support ke saath advanced mathematical operations.
  - Hand Cricket: Memory-efficient logic par based ek fun mini-game.
  - Profile Management: Glassmorphism UI ke saath user profile editing aur cloud
    sync.

🎨 App Experience

  - Modern UI: 100% Jetpack Compose aur Material 3 ka upyog.
  - Global Theming: Dark aur Light mode support, jo Jetpack DataStore se
    persisted hai.
  - Smooth Navigation: Splash Screen -> Onboarding -> Login -> Home ka
    professional flow.

🏗 Architecture (MVVM + Repository)

App ko Clean Architecture ke principles par design kiya gaya hai taaki code
maintainable aur scalable rahe.

The Flow:

View (UI) ➔ ViewModel (State) ➔ Repository (Data Manager) ➔ Model (Data
Structure)

1.  View (Compose): Sirf UI dikhane aur user events capture karne ka kaam karta
    hai.
2.  ViewModel: UI State ko hold karta hai aur Business Logic process karta hai.
3.  Repository (The Data Manager): Yeh app ka sabse "Smart" hissa hai. Yeh
    decide karta hai ki data kahan se aayega:
      - Remote Source: Firebase Firestore ya Retrofit API.
      - Local Source: Jetpack DataStore ya In-memory Cache.
4.  Model: Data structures ko define karne wali Data Classes.

🛠 Tech Stack

  - Kotlin: Primary programming language.
  - Jetpack Compose: Modern toolkit for building native UI.
  - Retrofit & GSON: REST API communication aur JSON parsing ke liye.
  - Firebase Firestore: Cloud-based NoSQL database real-time sync ke liye.
  - Firebase Auth: User identity aur security management.
  - Coil: Kotlin-first image loading library (AsyncImage).
  - Coroutines & Flow: Asynchronous programming aur real-time data streaming ke
    liye.
  - Jetpack DataStore: Local preference storage (Theme & Settings).

🔑 Firebase Integration

App ko successfully chalane ke liye Firebase configuration zaroori hai:

1.  google-services.json: Is file ko app/ directory mein rakha gaya hai. Isme
    Project ID aur API Keys maujood hain.
2.  SHA-1 Fingerprint: App ko Firebase Console mein SHA-1 debug key se link kiya
    gaya hai taaki Firebase Auth aur Google Services authorised rahein.
3.  Firestore Rules: Cloud data security ke liye custom rules implement kiye
    gaye hain.

📂 Project Structure

com.yashwant
├── calculator      # Calculator engine logic
├── data            # Repositories, API Services, DataStore managers
├── model           # FoodItem, CartItem, OrderItem, ProfileState
├── navigation      # Screen routes aur NavHost setup
├── ui              # UI Layer
│   ├── components  # Reusable UI elements (Cards, Buttons)
│   ├── profile     # Profile specific screens
│   ├── screen      # Main app screens (Home, Search, Cart, etc.)
│   └── theme       # Colors, Typography, aur Theme wrapper
└── viewmodel       # State management for all screens

🛠 Installation

1.  Clone this repository.
2.  Apna google-services.json file app/ folder mein daalein.
3.  Android Studio (Version Otter 2025.2.x ya latest) mein project open karein.
4.  Gradle Sync karein aur compileSdk 35 par run karein.

✨ Highlights of our Learning Journey:

  - Stable Data Mapping: Item name ke basis par price aur restaurants ko sync
    rakhne ki logic.
  - Shared ViewModels: Multiple screens ke beech data consistence rakhne ka
    professional tarika.
  - Edge-to-Edge: Status bar aur navigation bar ko app theme ke saath fully sync
    kiya gaya hai.

Developed with ❤️ by Yashwant Vashisth
