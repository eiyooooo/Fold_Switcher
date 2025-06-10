<div align="center">

<h1 align="center">Fold_Switcher（折叠屏切换器）</h1>

[![GitHub license][license-shield]][license-url]
[![GitHub release][releases-shield]][releases-url]
[![GitHub downloads][downloads-shield]][downloads-url]

[English](./README.md) / 简体中文

</div>

## 👋简介
- 本应用可实现折叠屏设备切换闭合模式、半开模式、帐篷模式和展开模式等折叠状态，部分设备可实现双屏同显
- 大部分设备可运行在用户模式进行切换（开箱即用），少数设备需Shizuku模式进行切换

## 📥安装包及更新日志

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/com.eiyooooo.foldswitcher/)

或在[Releases 页面](https://github.com/eiyooooo/Fold_Switcher/releases/latest)下载最新的APK文件。

## 🚀上手指南

1. [下载](#安装包及更新日志)并安装
2. 若提示需要Shizuku权限请[下载Shizuku](https://shizuku.rikka.app/zh-hans/download/)并参考[Shizuku手册](https://shizuku.rikka.app/zh-hans/guide/setup/)完成配置
3. 点击获取到的折叠模式进行切换

    #### 若无法正常使用请尝试切换运行模式

### ⚙️快捷切换设置

1. 长按获取到的折叠模式进行设置
2. 安卓13及以上会出现提示添加，其他安卓版本需“手动下拉状态栏-编辑开关”进行添加

## 🔄与其他应用集成

您可以通过Tasker、MacroDroid等自动化应用或ADB命令控制折叠状态。

### 方法一：使用Intent

```kotlin
// 设置特定折叠状态
val intent = Intent()
intent.setComponent(ComponentName("com.eiyooooo.foldswitcher", "com.eiyooooo.foldswitcher.ReceiverActivity"))
intent.putExtra("state_id", 1) // 替换为所需的状态ID（非负整数）
startActivity(intent)

// 重置为默认状态
val resetIntent = Intent()
resetIntent.setComponent(ComponentName("com.eiyooooo.foldswitcher", "com.eiyooooo.foldswitcher.ReceiverActivity"))
resetIntent.putExtra("state_id", -1)
startActivity(resetIntent)
```

### 方法二：使用ADB命令

```shell
# 设置特定折叠状态（将1替换为所需的状态ID，非负整数）
adb shell am start -n com.eiyooooo.foldswitcher/.ReceiverActivity --ei state_id 1

# 重置为默认状态
adb shell am start -n com.eiyooooo.foldswitcher/.ReceiverActivity --ei state_id -1
```

### 方法三：在Tasker、MacroDroid等自动化应用中集成

在自动化应用中创建一个新任务，然后添加"系统"→"发送意图"操作，填写以下参数：

**设置特定折叠状态：**
- 操作: `无需填写`
- 类别: `无需填写`
- 类型: `Activity`
- 包名: `com.eiyooooo.foldswitcher`
- 类名: `com.eiyooooo.foldswitcher.ReceiverActivity`
- 额外参数:
  - 名称: `state_id`
  - 类型: `整数`
  - 值: `[所需状态ID]` (非负整数，例如: `1`)

**重置为默认状态：**
- 操作: `无需填写`
- 类别: `无需填写`
- 类型: `Activity`
- 包名: `com.eiyooooo.foldswitcher`
- 类名: `com.eiyooooo.foldswitcher.ReceiverActivity`
- 额外参数:
  - 名称: `state_id`
  - 类型: `整数`
  - 值: `-1`

### 可用的状态ID可以在应用中长按获取到的状态查看。具体状态可能因设备而异。

## ⭐支持我
- 如果你喜欢本应用，请给本仓库点星星哦

<!-- links -->
[your-project-path]:eiyooooo/Fold_Switcher
[license-shield]: https://img.shields.io/github/license/eiyooooo/Fold_Switcher.svg
[license-url]: https://github.com/eiyooooo/Fold_Switcher/blob/main/LICENSE
[releases-shield]: https://img.shields.io/github/release/eiyooooo/Fold_Switcher.svg
[releases-url]: https://github.com/eiyooooo/Fold_Switcher/releases
[downloads-shield]: https://img.shields.io/github/downloads/eiyooooo/Fold_Switcher/total.svg
[downloads-url]: https://github.com/eiyooooo/Fold_Switcher/releases
