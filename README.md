# Practical 5 — Chinese Municipalities

Android application built for Practical 5 (Resources, Intents and Dynamic UI).
The user types the name of a Chinese municipality on the main screen; the app validates it,
opens a second screen styled for that municipality (colour, image, summary) and can launch the
official website of the selected municipality.

## Features

- **Resources**
  - `res/drawable`: `chinese_municipalities.jpg`, `beijing.jpg`, `shanghai.jpg`, `tianjin.jpg`, `chongqing.jpg`
  - `res/mipmap-*`: adaptive launcher icon (`ic_launcher`) generated from `app_logo.png`
  - `res/values/strings.xml`: app name (`Chinese Municipalities`) and all UI text
  - `res/values/colors.xml`: named colours for the four municipalities plus shared text/background colours
  - `res/values/arrays.xml`: `municipalities_array` with a short summary of each city
- **Main screen** (`MainActivity`, `activity_main.xml`): title, illustration, labelled single-line
  `EditText` with the `Go` keyboard action. Enter submits the input.
- **Validation** (`TextProcessor`): plain Java class, case-insensitive, trims whitespace, returns the
  correctly capitalised name or `null`. Validation is not duplicated in the activities.
- **Explicit intent**: `MainActivity` sends the normalised name to `Municipality` in an intent extra.
- **Second screen** (`Municipality`, `activity_municipality.xml`): name header, city image, scrollable
  summary inside a `ScrollView`, and a `Visit site` button. Colours are applied at runtime with
  `setBackgroundColor()`, `setTextColor()` and `setBackgroundTintList()`.
- **Implicit intent**: `Visit site` fires `Intent.ACTION_VIEW` at the official website of the current
  municipality and handles the case where no app can open it.

## Summary array order

`municipalities_array` stores the summaries in this exact order, and
`TextProcessor.indexOfMunicipality()` maps names onto the same positions:

| Index | Municipality | Colour resource | Image | Official website |
|------:|--------------|-----------------|-------|------------------|
| 0 | Beijing | `municipality_beijing` (red) | `beijing.jpg` | https://english.beijing.gov.cn/ |
| 1 | Shanghai | `municipality_shanghai` (magenta) | `shanghai.jpg` | https://english.shanghai.gov.cn/ |
| 2 | Chongqing | `municipality_chongqing` (bright orange) | `chongqing.jpg` | https://english.cq.gov.cn/ |
| 3 | Tianjin | `municipality_tianjin` (charcoal) | `tianjin.jpg` | https://en.tj.gov.cn/ |

## Build and test

```bash
./gradlew :app:assembleDebug        # build the debug APK
./gradlew :app:testDebugUnitTest    # run the TextProcessor unit tests
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## Manual test checklist

| Input | Expected result |
|-------|-----------------|
| `Beijing`, `beijing`, `BEIJING` | Beijing screen |
| `Shanghai` | Shanghai screen |
| `Tianjin` | Tianjin screen |
| `Chongqing` | Chongqing screen |
| `Guangzhou` | invalid municipality error, stays on main screen |
| empty input | input validation error |
| spaces only | input validation error |

For each municipality check the name, image, summary, header/button colour, information text colour,
and that `Visit site` opens that municipality's official website.
