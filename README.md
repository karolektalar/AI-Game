# Jumper Game - LibGDX Hyper-Casual Mobile Game

A simple "tap to jump" hyper-casual mobile game built with LibGDX and Kotlin for Android.

## Features

- **Simple Gameplay**: Tap to jump and avoid obstacles
- **Score System**: Track your high score across sessions
- **Three Screens**: Main menu, gameplay, and game over
- **Physics System**: Gravity and collision detection
- **AdMob Integration**: Interstitial ads and rewarded ads
- **Clean Architecture**: Easy to extend with new levels and features

## Project Structure

```
AI-Game/
├── android/                          # Android-specific code
│   └── src/main/
│       ├── java/com/game/jumper/
│       │   ├── AndroidLauncher.kt    # Android entry point
│       │   └── AndroidAdManager.kt   # AdMob implementation
│       ├── res/                      # Android resources
│       └── AndroidManifest.xml       # Android configuration
├── core/                             # Core game logic (platform-independent)
│   └── src/main/kotlin/com/game/jumper/
│       ├── JumperGame.kt            # Main game class
│       ├── entities/                 # Game entities
│       │   ├── Player.kt            # Player with jump physics
│       │   ├── Obstacle.kt          # Moving obstacles
│       │   └── Ground.kt            # Ground platform
│       ├── screens/                  # Game screens
│       │   ├── MenuScreen.kt        # Main menu
│       │   ├── GameScreen.kt        # Gameplay screen
│       │   └── GameOverScreen.kt    # Game over screen
│       ├── managers/                 # Game managers
│       │   ├── AdManager.kt         # Ad interface
│       │   └── GameStateManager.kt  # Score management
│       └── utils/
│           └── Constants.kt         # Game configuration
└── build.gradle.kts                  # Project build configuration

```

## Setup Instructions

### Prerequisites

1. **Android Studio** (latest version recommended)
2. **Android SDK** (API 24-34)
3. **JDK 17** or higher

### Configuration Steps

1. **Set Android SDK Path**:
   - Open `local.properties`
   - Uncomment and set your Android SDK path:
     ```
     sdk.dir=/path/to/your/android/sdk
     ```

2. **Configure AdMob** (Important!):
   - Get your AdMob App ID from [AdMob Console](https://apps.admob.com/)
   - Replace the test App ID in `android/src/main/AndroidManifest.xml`:
     ```xml
     <meta-data
         android:name="com.google.android.gms.ads.APPLICATION_ID"
         android:value="ca-app-pub-YOUR_PUBLISHER_ID~YOUR_APP_ID"/>
     ```
   - Replace test ad unit IDs in `AndroidAdManager.kt`:
     ```kotlin
     private const val INTERSTITIAL_AD_UNIT_ID = "your-interstitial-ad-unit-id"
     private const val REWARDED_AD_UNIT_ID = "your-rewarded-ad-unit-id"
     ```

3. **Add App Icons** (Optional):
   - Add your app icons to `android/src/main/res/mipmap-*/` directories
   - Use Android Studio's Image Asset tool for easy icon generation

### Building and Running

1. **Open in Android Studio**:
   ```bash
   # Open Android Studio and import this project
   ```

2. **Sync Gradle**:
   - Android Studio should automatically sync Gradle
   - If not, click "Sync Project with Gradle Files"

3. **Run on Device/Emulator**:
   - Connect an Android device or start an emulator
   - Click Run (Shift + F10) or Debug (Shift + F9)

## Game Mechanics

### Player Controls
- **Tap anywhere** on the screen to make the player jump
- **Gravity** automatically pulls the player down
- Player can only jump when on the ground

### Obstacles
- Spawn from the right side at random heights
- Move from right to left at constant speed
- Player must avoid collision with obstacles

### Scoring
- **+1 point** for each obstacle passed
- **High score** is saved across sessions
- **Bonus +10 points** for watching rewarded ads

### Ads
- **Interstitial ads**: Shown every 3 game overs
- **Rewarded ads**: Optional, gives +10 score bonus

## Customization & Expansion

### Easy Tweaks (Constants.kt)

Modify game behavior by changing values in `core/src/main/kotlin/com/game/jumper/utils/Constants.kt`:

```kotlin
// Physics
const val GRAVITY = -980f              // Gravity strength
const val JUMP_VELOCITY = 450f         // Jump power

// Obstacles
const val OBSTACLE_SPEED = 200f        // Obstacle movement speed
const val OBSTACLE_SPAWN_INTERVAL = 2f // Seconds between obstacles

// Ads
const val SHOW_INTERSTITIAL_EVERY = 3  // Show ad every N game overs
```

### Adding New Features

**Adding Power-ups**:
1. Create `Powerup.kt` in `entities/` package
2. Add powerup spawning logic in `GameScreen.kt`
3. Implement collision detection with player
4. Add powerup effects (speed boost, shield, etc.)

**Adding Difficulty Progression**:
1. Add difficulty manager in `managers/` package
2. Increase obstacle speed based on score
3. Decrease spawn interval over time
4. Add different obstacle types

**Adding Sound Effects**:
1. Add sound files to `android/assets/` directory
2. Load sounds in `JumperGame.kt`:
   ```kotlin
   val jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"))
   ```
3. Play sounds on events (jump, collision, score)

**Adding Backgrounds**:
1. Add texture images to `android/assets/` directory
2. Load textures in `JumperGame.kt`
3. Render backgrounds in screens

## Technical Details

### Architecture
- **Core Module**: Platform-independent game logic (Kotlin)
- **Android Module**: Android-specific code and AdMob integration
- **Clean Separation**: Easy to add iOS/Desktop platforms later

### Dependencies
- **LibGDX 1.12.1**: Game framework
- **Kotlin 1.9.22**: Programming language
- **Google AdMob 22.6.0**: Monetization
- **Android SDK 34**: Target platform

### Performance
- Simple 2D rendering with ShapeRenderer
- Minimal memory usage
- 60 FPS target on modern devices

## Troubleshooting

### Build Issues
- **Gradle sync fails**: Check internet connection and Gradle settings
- **SDK not found**: Set `sdk.dir` in `local.properties`
- **Kotlin version mismatch**: Update Kotlin plugin in Android Studio

### Runtime Issues
- **Black screen**: Check if AdMob is properly initialized
- **Ads not showing**: Using test ad IDs? Check AdMob console for real ads
- **Crashes on launch**: Check logcat for error messages

### AdMob Issues
- **Test ads not showing**: Check internet connection
- **Real ads not showing**: Ensure app is properly configured in AdMob console
- **Ad loading errors**: Check AdMob app ID and ad unit IDs

## License

This project is provided as-is for educational and commercial use.

## Next Steps

1. **Add app icons** and splash screen
2. **Replace AdMob test IDs** with your real ad units
3. **Add sound effects** and music
4. **Implement difficulty progression**
5. **Add more obstacle types** and power-ups
6. **Create multiple levels** or game modes
7. **Add leaderboards** with Google Play Games Services
8. **Polish UI** with textures and animations

---

**Created with LibGDX + Kotlin** | Ready for Android Studio | AdMob Integrated