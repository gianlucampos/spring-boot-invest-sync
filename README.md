# Spring Boot Invest Sync

A project to automatically update a financial portfolio in Google Sheets using **AWS Lambda** and **Spring Boot**.

---

## Features

- Updates **BR stock and REIT prices**.
- Centralizes data in **Google Sheets** (sheet as the single source of truth).
- Runs **once a week** (or as configured in EventBridge cron).
- Uses **Spring dependency injection** for easy extensibility.
- Fully **free-tier friendly** on AWS.

---

## Stack

- Java 21
- Spring Boot 4.0.2 (core, no web)
- AWS Lambda + EventBridge (cron)
- Google Sheets API
- Lombok

---

## Structure

- `UpdatePortfolio` → main Lambda function
- `PortfolioSyncService` → portfolio update logic
- `SheetsFactory` → Google Sheets service setup with credentials

---

## Build

```bash
./gradlew clean build

