
4. Sync and build the project — you're ready to run! 🚀

---

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

## ❤️ Support

If you found this project useful, consider giving it a ⭐ and [following me on GitHub](https://github.com/shubhanshu24510) for more awesome projects!

---

## 📄 License

```text
Designed and developed by Shubhanshu Singh (2025)

Licensed under the Apache License, Version 2.0

http://www.apache.org/licenses/LICENSE-2.0
