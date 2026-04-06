# Netty Proxy - Android HTTP/HTTPS Proxy Server

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![Netty](https://img.shields.io/badge/Netty-4.1-25A2C3?style=for-the-badge&logo=netty&logoColor=white)](https://netty.io)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)

A high-performance proxy server for Android devices built with **Netty framework**. Turn your Android device into a fully functional HTTP/HTTPS proxy server with auto-start capabilities and authentication support.

## Screenshots

<div style="overflow: hidden">
<img src="/assets/img/main.jpg" alt="Main" width="40%" align="bottom" />
<img src="/assets/img/about.jpg" alt="About" width="40%" align="bottom" />
</div>

## 📱 Features

- 🚀 **High Performance** - Built with Netty's asynchronous event-driven architecture
- 🔒 **HTTPS Support** - Full CONNECT method support for HTTPS tunneling
- 🔄 **Auto-Start** - Automatically starts on device boot and WiFi connection (Soon)
- 📡 **Background Service** - Runs as foreground service for reliable operation
- 🌐 **Network Sharing** - Share your device's internet connection with other devices
- ⚡ **Low Resource Usage** - Efficient memory management with Netty's ByteBuf pooling

## 🎯 Use Case

This app is perfect for networks where:
- Only one device has internet access
- You need to share internet connection without a router
- Network restrictions prevent direct internet access

## 🛠️ Technical Details

| Aspect | Specification |
|--------|---------------|
| **Language** | Java 8+ |
| **Framework** | Netty 4.1.108.Final |
| **Min SDK** | Android API 21 (Android 5.0) |
| **Target SDK** | Android API 34 (Android 14) |
| **Proxy Port** | 8080 (configurable) |
| **Protocols** | HTTP, HTTPS (via CONNECT), SOCKS5 |

## 📥 Installation

### From Source
```bash
git clone https://github.com/omar-haidar/Android-Netty-Proxy.git
cd Netty-Proxy
# Open in Android Studio and build