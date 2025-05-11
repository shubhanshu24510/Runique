<h1 align="center">🏃‍♂️ Runique - Running Tracker App</h1>

<p align="center">
  <b>Runique</b> is a multi-module, offline-first running tracker app that monitors your health, tracks your runs with real-time location, and supports both Android mobile and Wear OS platforms.
</p>

<p align="center">
  <img src="https://pl-coding.com/wp-content/uploads/2024/04/run-feature.png" alt="Run Feature" width="800"/>
</p>

<table align="center">

  <tr>
    <td align="center">
      <img src="https://pl-coding.com/wp-content/uploads/2024/04/auth-feature.png" alt="Auth Feature" width="400"/>
    </td>
    <td align="center">
      <img src="https://pl-coding.com/wp-content/uploads/2024/04/phone-watch-mockup.png" alt="Phone Watch Mockup" width="300"/>
    </td>
  </tr>
</table>

## 🔍 Preview

https://github.com/shubhanshu24510/Runiquee/assets/100926922/b0268e64-c22d-4db4-ae6c-1cfc077e7798

---

## ✨ Features

- 🔁 **Real-time Run Tracking**: Track your runs using Google Maps and draw running paths using google map drawlines.
- 📊 **Health Metrics**: View data such as Distance, Heart Rate, Pace, Speed, Elevation, and more.
- 📷 **Map Snapshots**: Capture and display run paths with data on the overview screen.
- 📶 **Offline-First**: All data is stored locally using Room DB and syncs with the server when online.
- ⌚ **Wear OS Support**: Real-time tracking and display optimized for Wear OS smartwatches.

---

## 🧩 Key Highlights

- Multi-module, clean MVI architecture
- OAuth authentication with token refresh
- Offline-first with Room Database
- Dynamic Feature Modules for modular builds
- Health & Fitness tracking using Google Health SDK
- Google Maps SDK integration
- Full support for Wear OS devices
- Gradle Version Catalogs & Convention Plugins

## 🛠️ Tech Stack

### Language & Concurrency
- **Kotlin**
- **Kotlin Coroutines + Flow**

### Jetpack Libraries
- Jetpack Compose (UI)
- Material3 Design System
- ViewModel
- Room Database
- Navigation (with Hilt support)
- Lifecycle-aware components
- DataStore (preferences)
- SplashScreen API

### Dependency Injection
- Hilt
- Koin (selective modules)

### Networking
- Ktor
- Kotlinx Serialization

### Database
- Room
- MongoDB (NoSQL)

### Image Loading & Logging
- Coil
- Timber

---

## 🧱 Architecture

Runique follows **MVI (Model-View-Intent)** with a **Repository Pattern**, and aligns with [Google's recommended app architecture](https://developer.android.com/topic/architecture).

### Layers:

#### UI Layer
- Compose UI
- ViewModels for state & event management
- Observes state via Kotlin Flows

#### Data Layer
- Repositories manage local/remote data sources
- Room DB & Network API abstraction
- Offline-first strategy

📐 Architecture Diagram  
![architecture](https://github.com/user-attachments/assets/09ca369a-968a-435e-bb89-f1856120bac5)

📦 Modular Diagram  
![modular architecture](https://github.com/user-attachments/assets/29f555f6-2339-40dc-899c-79835b0c7fb7)

📲 UI Layer  
![ui layer](https://github.com/user-attachments/assets/80d123e6-e72b-4ca8-998b-a9edec78ae19)

📡 Data Layer  
![data layer](https://github.com/user-attachments/assets/0bdebc42-69a1-41a2-ad8f-d57d3cbf9124)

---

## 🧱 Modularization Strategy

- 🔁 **Reusability**: Share logic across features
- 🏗 **Parallel Builds**: Improve build times
- 🚧 **Isolation**: Reduce tight coupling between modules
- 👥 **Team Focus**: Developers can work independently on dedicated modules

👉 Learn more: [Android App Modularization Guide](https://developer.android.com/topic/modularization)

---

## 🧰 Additional Tools & Libraries

- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [KSP (Kotlin Symbol Processing)](https://github.com/google/ksp)
- Compose Compiler Plugin
- Google Health Services
- Google Maps SDK
- Dynamic Feature Delivery
---

---
## How do you run the project?

In order to run the project on your phone, you'll need to first clone it and then add two API keys for:
1. ... the Runique API (access granted after course purchase)
2. ... Google Maps (needs to be got from Google Cloud Console - instructions in the course)

Then simply include them in `local.properties`:
```
API_KEY=<RUNIQUE_API_KEY>
MAPS_API_KEY=<GOOGLE_MAPS_API_KEY>
```
Afterwards, build the project and you're ready to use it.
---

## ❤️ Support

If you found this project useful, consider giving it a ⭐ and [following me on GitHub](https://github.com/shubhanshu24510) for more awesome projects!

---

## 📄 License

```text
Designed and developed by Shubhanshu Singh (2025)

Licensed under the Apache License, Version 2.0

http://www.apache.org/licenses/LICENSE-2.0
