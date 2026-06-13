# 🚀 RikLogEngine-Pro

> | Pure Client-Tier Green Software Engineering & Performance Analytics | RikMakersHub | 

An ultra-performant, dual-layer system utility built to parse massive, unstructured production server logs, automatically mask dynamic variables, and cluster repetitive anomalies into structured visual analytics.

## 🛠️ System Architecture

The engine is decoupled into two lightweight, high-performance layers to optimize computing resources:
1. **Java Core Backend (`LogParserEngine.java`)**: A raw command-line utility that handles heavy string processing, tokenizes variable fields using compiled regular expressions, and exports structured data.
2. **Web Core Frontend (`index.html` & `style.css`)**: A crisp, responsive hybrid-light theme visualizer dashboard that reads telemetry profiles and builds interactive data tables completely client-side.

Raw Logs ➔ Java Regex Engine ([TIMESTAMP]/[UUID]/[NUM]) ➔ Output Asset (summary.json) ➔ Pro Matrix Dashboard

## 🏎️ Features & Performance Metrics

* **Tokenization Pipeline**: Automatically intercepts and strips out changing parameters (`Timestamps`, `UUIDs`, `IP Addresses`, `Numeric IDs`) to find the core structural signatures of application bugs.
* **Extreme Processing Latency**: Processed execution runs local parsing arrays in as low as **13 milliseconds**.
* **100% Client-Side Privacy**: Log evaluations are processed entirely inside the local browser sandbox via the HTML5 `FileReader` API. No server uploads, no data leaks.
* **Open Source Standard**: Distributed under the permissive **MIT License** for maximum community integration.

## ⚙️ Core Deployment Guide

### 1. Compile the Java Engine
Launch your machine terminal or Command Prompt inside your working directory and execute the compiler loop:
```cmd
javac LogParserEngine.java
```

### 2. Parse Your Production Logs
Pass your target raw log file path directly into the generated runtime byte-code executable:
```cmd
java LogParserEngine text.log.txt
```
*This command completes the evaluation pass and instantly drops a clean data profile named `summary.json` into your workspace folder.*

### 3. Render Visual Analytics
Open the live hosted deployment link or load `index.html` locally in your web browser. Drag and drop your compiled `summary.json` file directly into the sandbox box to instantly display the anomaly charts and frequency tags.

## 📄 License

This project is open-source and licensed under the terms of the **MIT License**. See the `LICENSE` file for more details.


**Come Check it out in:** [🚀 RikLogEngine-Pro](https://rikmakershub.github.io/RikLogEngine-Pro/)
