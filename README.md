# FinWallet - Personal Finance Tracker

## Project Overview
FinWallet is a Java Spring Boot backend application designed for tracking personal finances. 

## Frontend / API Testing Strategy
**IMPORTANT NOTE:**
While the ultimate goal might be to use a modern frontend framework like React or Angular, they can be time-consuming to set up and learn when the main focus is on mastering backend Spring Boot concepts.

For faster learning and easier testing of the implemented REST APIs, this project uses **Thymeleaf**. 
Thymeleaf is set up to act as a simulated external client (just like React or Angular would). 

- **All Thymeleaf controllers and related code** are located strictly in the `com.rps.finwallet.thymeleaf` package.
- The Thymeleaf templates (HTML/JS) use standard frontend techniques (like `fetch` API) to call the existing Spring Boot REST APIs. This allows you to test your APIs directly from a web interface without the overhead of a separate frontend project.
