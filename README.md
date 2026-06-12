# MVVM-implementation

A small Kotlin Android sample showing a phone-number / password login + register flow against a remote JSON API, organised as a thin **AndroidViewModel + Repository** split. The repository layer uses **Volley** for HTTP and the auth token is persisted in **SharedPreferences**. Branded "MVVM Login" (`applicationId = com.shayan.mvvm_login`).

> **Heads-up:** the previous README claimed Room, LiveData, Hilt, Flow, Material Design, JUnit/Espresso tests, and an MIT license. Most of that is not in the code — see [Honest limitations](#honest-limitations).

## Status

- Working tree clean on `master` (last commit `46c442d`, recent history is dominated by GitPulse marker commits).
- Remote: `https://github.com/shayann07/MVVM-implementation.git`.
- This README was rewritten from a code audit. The previous README's feature list did not match the actual code.

## How it works

- **`view/MainActivitySplash`** — launcher activity. Inflates a Lottie `animation_splash`, waits ~5.65 s, then reads `PrefsDatabase.user_token` from SharedPreferences and routes to either `MainActivityLogin` (no token) or `MainActivityHome` (token present).
- **`view/MainActivityLogin`** — phone + password form. Calls `AuthViewModel.loginUser(...)`. On success it writes the returned token to `PrefsDatabase.user_token` and starts `MainActivityHome`. Has a button that opens `MainActivityRegister`.
- **`view/MainActivityRegister`** — first/last name, phone, password, confirm-password. Builds a `ModelUser` and calls `AuthViewModel.registerUser(...)`.
- **`view/MainActivityHome`** — a static iCloud-Reminders-style dashboard with cards (Today / Scheduled / All / Flagged / Completed) and two collapsible sections ("iCloud", "Outlook"). The toolbar overflow opens a `PopupMenu` whose only item ("Log Out") clears prefs and pops back to login.
- **`viewmodel/AuthViewModel`** — `AndroidViewModel` that holds a single `AuthRepository` and re-issues the same callback-based API inside `viewModelScope.launch { … }`. There is no `LiveData`, `StateFlow`, `Flow`, or `Result` wrapper; success/error callbacks are forwarded straight through.
- **`repository/AuthRepository`** — builds `JSONObject` request bodies and POSTs to:
  - `https://cricdex.enfotrix.com/api/login`
  - `https://cricdex.enfotrix.com/api/register`

  via Volley `JsonObjectRequest`, parsing `success`, `message`, and `data.token` from the JSON response. A new `Volley.newRequestQueue(...)` is allocated per call.
- **`model/ModelUser`** — data class with `firstName`, `lastName`, `phone`, `password`, `cPass`, `token`.

## Tech stack

- **Language / build:** Kotlin, AGP via `libs.versions.toml`, JVM 11.
- **App config:** `applicationId = com.shayan.mvvm_login`, `compileSdk = 35`, `minSdk = 24`, `targetSdk = 34`, `versionCode = 1`, `versionName = "1.0"`. `viewBinding.enable = true`.
- **Dependencies:** `androidx.core.ktx`, `appcompat`, `material`, `volley`, `lottie`, `kotlinx-coroutines-android`, `androidx.activity`, `androidx.constraintlayout`, `androidx.lifecycle.viewmodel.ktx`, `androidx.fragment.ktx`. JUnit + AndroidX-JUnit + Espresso for the default test scaffolds.
- **Permissions:** `INTERNET` only.

The repo does **not** use Hilt/Dagger, Room, Retrofit/OkHttp, Gson/Moshi, LiveData, Flow, Jetpack Compose, Navigation Component, DataStore, or WorkManager.

## Project layout

```
MVVM-implementation/
├── app/
│   ├── build.gradle.kts                       # compileSdk 35, namespace com.shayan.mvvm_login
│   └── src/main/
│       ├── AndroidManifest.xml                # 4 activities, INTERNET only
│       ├── java/com/shayan/mvvm_login/
│       │   ├── model/ModelUser.kt
│       │   ├── repository/AuthRepository.kt   # Volley → cricdex.enfotrix.com
│       │   ├── viewmodel/AuthViewModel.kt     # AndroidViewModel
│       │   └── view/
│       │       ├── MainActivitySplash.kt      # 5.65 s token gate
│       │       ├── MainActivityLogin.kt
│       │       ├── MainActivityRegister.kt
│       │       └── MainActivityHome.kt        # PopupMenu logout
│       └── res/  layout/, drawable/, menu/, raw/animation_splash, values/
├── build.gradle.kts
└── gradle/libs.versions.toml
```

## Setup / run

1. Clone, then provide a normal `local.properties` (gitignored).
2. Open in Android Studio and let Gradle sync.
3. `./gradlew :app:assembleDebug` (or run from the IDE).

The login / register flow makes live calls to `https://cricdex.enfotrix.com/api/{login,register}` — without a valid account on that backend the flow cannot complete.

## Honest limitations

- **No Room, no LiveData, no Flow, no Hilt, no Retrofit.** The previous README listed all of these. The actual stack is Volley + plain callbacks + SharedPreferences, with an `AndroidViewModel` that adds a coroutine wrapper around synchronous Volley dispatches.
- **`viewModelScope.launch` is cosmetic.** Volley already runs work off the main thread and delivers callbacks on it; the wrapping coroutine in `AuthViewModel` does no suspending work.
- **Per-request Volley queue.** `AuthRepository` calls `Volley.newRequestQueue(context.applicationContext)` on every login / register. Should be a single shared queue.
- **Hardcoded API base URL.** `loginUrl` / `registerUrl` are constants in `AuthRepository`. No `BuildConfig` field, no debug / release split, no flavour swap.
- **Plaintext token storage.** `user_token` is written to plain SharedPreferences. Use `EncryptedSharedPreferences` or DataStore + `MasterKey` for anything real.
- **All four activities are `android:exported="true"`.** Only `MainActivitySplash` needs to be exported.
- **Splash delay is ~5.65 s.** Long for a launcher; consider `androidx.core:core-splashscreen` and a shorter timeout.
- **`MainActivityHome` is a static mockup.** No real reminder data — the cards and collapsible sections are pure UI.
- **No `LICENSE` file** at the repo root, despite the previous README claiming MIT.
- **No tests beyond defaults.** Only the generated `ExampleUnitTest` / `ExampleInstrumentedTest` are present.
- **GitPulse marker comments.** The previous `README.md` ended with `<!-- commit N -->` and `<!-- gitpulse:contribution … -->` lines from a contribution-marker tool; those are not present in this rewrite.
