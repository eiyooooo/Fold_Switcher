<div align="center">

<h1 align="center">Fold_Switcher（折叠屏切换器）</h1>

[![GitHub license][license-shield]][license-url]
[![GitHub release][releases-shield]][releases-url]
[![GitHub downloads][downloads-shield]][downloads-url]

English / [简体中文](./README_CN.md)

</div>

## 👋Introduction
- This application can switch between various folding states on foldable devices, such as closed mode, half-open mode, tent mode, and fully open mode. Some devices support dual-screen display.
- Most devices can switch modes in user mode (out-of-the-box), while a few devices require Shizuku mode for switching.

## 📥Download

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/com.eiyooooo.foldswitcher/)

Or download the latest APK from the [Releases Section](https://github.com/eiyooooo/Fold_Switcher/releases/latest).

## 🚀Getting Started Guide

1. [Download][downloads-url] and install the apk.
2. If prompted for Shizuku permissions, please [download Shizuku](https://shizuku.rikka.app/download/) and refer to the [Shizuku manual](https://shizuku.rikka.app/guide/setup/) for configuration instructions.
3. Click on the fold state to switch.

    #### If you are unable to switch fold state normally, please try switching to a different execute mode

### ⚙️Quick Switch Settings

1. Long press on the fold state to set it as Quick Switch.
2. For Android 13 and above, a request will appear. For other Android versions, you need manually pull down the status bar and edit the switch.

## 🔄Integration with Other Apps

You can control the fold state from automation apps like Tasker, MacroDroid, or through ADB commands.

### Method 1: Using Intents

```kotlin
// Set a specific fold state
val intent = Intent()
intent.setComponent(ComponentName("com.eiyooooo.foldswitcher", "com.eiyooooo.foldswitcher.ReceiverActivity"))
intent.putExtra("state_id", 1) // Replace with desired state ID (non-negative number)
startActivity(intent)

// Reset to default state
val resetIntent = Intent()
resetIntent.setComponent(ComponentName("com.eiyooooo.foldswitcher", "com.eiyooooo.foldswitcher.ReceiverActivity"))
resetIntent.putExtra("state_id", -1)
startActivity(resetIntent)
```

### Method 2: Using ADB Commands

```shell
# Set a specific fold state (replace 1 with desired state ID, non-negative number)
adb shell am start -n com.eiyooooo.foldswitcher/.ReceiverActivity --ei state_id 1

# Reset to default state
adb shell am start -n com.eiyooooo.foldswitcher/.ReceiverActivity --ei state_id -1
```

### Method 3: Integration with Automation Apps like Tasker, MacroDroid

Create a new task in your automation app and add the "System" → "Send Intent" action with the following parameters:

**To set a specific fold state:**
- Action: `Leave empty`
- Category: `Leave empty`
- Mime Type: `Activity`
- Package: `com.eiyooooo.foldswitcher`
- Class: `com.eiyooooo.foldswitcher.ReceiverActivity`
- Extra:
  - Name: `state_id`
  - Type: `Integer`
  - Value: `[desired state ID]` (non-negative integer, e.g., `1`)

**To reset to default state:**
- Action: `Leave empty`
- Category: `Leave empty`
- Mime Type: `Activity`
- Package: `com.eiyooooo.foldswitcher`
- Class: `com.eiyooooo.foldswitcher.ReceiverActivity`
- Extra:
  - Name: `state_id`
  - Type: `Integer`
  - Value: `-1`

### Available state IDs can be found by long-pressing the retrieved state in the app. The actual states may vary depending on your device.

## ⭐Support Me
- If you enjoy this application, please consider giving this repository a star.

<!-- links -->
[your-project-path]:eiyooooo/Fold_Switcher
[license-shield]: https://img.shields.io/github/license/eiyooooo/Fold_Switcher.svg
[license-url]: https://github.com/eiyooooo/Fold_Switcher/blob/main/LICENSE
[releases-shield]: https://img.shields.io/github/release/eiyooooo/Fold_Switcher.svg
[releases-url]: https://github.com/eiyooooo/Fold_Switcher/releases
[downloads-shield]: https://img.shields.io/github/downloads/eiyooooo/Fold_Switcher/total.svg
[downloads-url]: https://github.com/eiyooooo/Fold_Switcher/releases
