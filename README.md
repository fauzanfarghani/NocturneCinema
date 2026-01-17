# Nocturne Cinema 🎬

**Nocturne Cinema** is a feature-rich Android application designed to simulate a premium movie booking experience. It features a secure user authentication system, a dark "Cinema Theme" aesthetic, film browsing capabilities, and a robust transaction management system.

![App Icon](app/src/main/res/drawable/ic_app_icon.png)

## 📱 Features

*   **Authentication Module**:
    *   Secure **Login** & **Registration**.
    *   **OTP Verification**: Simulates SMS delivery for added security.
    *   **Auto-Login**: Remembers user session upon startup.
*   **Cinema Experience**:
    *   **Home Dashboard**: Browse a curated list of films with cover art and ratings.
    *   **Film Details**: View comprehensive info (rating, country, price) and book tickets.
    *   **Dynamic Quantity**: Easy-to-use increment/decrement controls for booking.
*   **Transaction Management**:
    *   **History**: View all booked tickets in a stylish dark card layout.
    *   **Update**: Modify ticket quantities via a custom dialog.
    *   **Delete**: Remove bookings with a confirmation popup.
*   **User Profile**:
    *   View account details (Username, Email, Phone).
    *   Secure **Logout** functionality.
*   **About Us**:
    *   Company information integrated with **Google Maps**.
*   **Premium UI**:
    *   Custom **Cinema Theme** (Dark Mode, Gold/Red Accents).
    *   Immersive layouts with `CardView` and rounded aesthetics.

## 🛠 Tech Stack

*   **Language**: Java
*   **Minimum SDK**: Android 5.0 (API Level 21)
*   **Architecture**: MVC (Model-View-Controller)
*   **Database**: SQLite (Local data persistence)
*   **Networking**: Volley (JSON data fetching)
*   **Location**: Google Maps SDK for Android
*   **UI Components**: `RecyclerView`, `CardView`, `ConstraintLayout`, Custom Dialogs

## 🚀 Getting Started

### Prerequisites
*   Android Studio Arctic Fox or newer.
*   JDK 8 or newer.
*   Android SDK Platform 31 or newer.

### Installation

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/NocturneCinema.git
    ```
2.  **Open in Android Studio**:
    *   Launch Android Studio -> `File` -> `Open` -> Select the `NocturneCinema` folder.
3.  **Sync Gradle**:
    *   Allow the project to sync dependencies.
4.  **Configure Maps API**:
    *   Open `AndroidManifest.xml`.
    *   Replace `YOUR_API_KEY` in the `com.google.android.geo.API_KEY` meta-data with your valid Google Maps API Key.
5.  **Run the App**:
    *   Connect a device or launch an emulator.
    *   Click the **Run** button (green play icon).

## 📸 Usage

1.  **Register** a new account to get started.
2.  **Verify** your account using the OTP code sent to your (simulated) SMS.
3.  **Browse** the "Now Showing" list on the Home screen.
4.  **Select** a film to view details and **Buy** tickets.
5.  Check your **Transactions** tab to manage your bookings.

---
*Developed by Fauzan Rahmat Farghani*
