# 🌊 Waves of Food - Modern Food Delivery & Utility App

**Waves of Food** ek high-performance Android application hai jo modern development practices aur clean architecture ko follow karta hai. Yeh app na sirf ek premium food delivery experience deta hai balki isme daily utility tools jaise Calculator aur Game bhi integrated hain.

---

## 🏗 1. MVVM Architecture

App ko **Clean Architecture** ke principles par design kiya gaya hai taaki code maintainable aur scalable rahe.

### **The Data Flow:**
`View (UI)` ➔ `ViewModel (State)` ➔ `Repository (Data Manager)` ➔ `Model (Data Structure)`

*   **View (Compose):** Sirf UI dikhane aur user events capture karne ka kaam karta hai.
*   **ViewModel:** UI State ko hold karta hai aur Business Logic process karta hai.
*   **Repository (The Data Manager):** Yeh app ka sabse "Smart" hissa hai. Yeh decide karta hai ki data kahan se aayega:
    *   **Remote Source:** Firebase Firestore (for Cart/Orders) ya Retrofit API (for Menu).
    *   **Local Source:** Jetpack DataStore (for Theme) ya In-memory Cache.
*   **Model:** Data structures ko define karne wali Data Classes.

---

## 🔥 2. Firebase & Backend Integration

App **Firebase Firestore** se fully linked hai real-time data handling ke liye.

*   **Configuration:** `google-services.json` file ke zariye linked hai.
*   **Security:** SHA-1 API Fingerprint ka use kiya gaya hai taaki cloud services authorised rahein.
*   **Live Sync:** Firestore `addSnapshotListener` ka use karke Cart aur Profile updates bina refresh kiye UI par dikhte hain.

---

## 🚀 Key Features

### 🍱 Food Delivery Engine
- **Retrofit API:** `dummyjson.com` se live recipes fetch ki jati hain.
- **Data Mapping:** Raw API data ko local model mein transform karke Custom Prices aur Restaurant Names add kiye gaye hain.
- **Smart Search:** Dish name aur cuisine ke basis par fast filtering logic.
- **Real-time Cart:** Firebase-based cart system jisme quantity control aur auto-billing integrated hai.

### 🛠 Utility & Experience
- **Pro Calculator:** Mathematical engine ke saath history tracking support.
- **Hand Cricket:** Memory-efficient logic par based ek fun mini-game.
- **Global Theming:** Dark/Light mode switching jo **Jetpack DataStore** se persistent hai.
- **Modern UI:** Glassmorphism effect, nested horizontal scrolling (Swiggy style), aur smooth animations.

---

## 🛠 Tech Stack

| Category | Technology Used |
| :--- | :--- |
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Networking** | Retrofit, GSON |
| **Database** | Firebase Firestore |
| **Local Storage** | Jetpack DataStore |
| **Image Loading** | Coil (AsyncImage) |
| **Threading** | Kotlin Coroutines & Flow |

---

## 📂 Project Structure

```text
com.yashwant
├── data            # Repositories, API Services, DataStore managers
├── model           # FoodItem, CartItem, OrderItem, ProfileState
├── navigation      # Screen routes aur NavHost setup
├── ui              # UI Layer (Components, Screens, Theme)
└── viewmodel       # State management logic

---

## ⚙️ Installation & Setup
- Clone the repository.
- Add your google-services.json in the app/ directory.
- Open in Android Studio Otter (2025.2.x) or latest.
- Sync Gradle and run on an emulator/device with API 35.
