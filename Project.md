# FinWallet — Digital Wallet & Banking System

A Spring Boot-based digital wallet application demonstrating financial transactions, transaction management, concurrency handling and database consistency.

## 🚀 Features

* User registration
* Wallet creation
* Deposit money
* Withdraw money
* Transfer money
* Check balance
* Transaction history
* Transaction status
* Transaction reference number
* Insufficient balance validation

## 🛠️ Technology Stack

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Thymeleaf
* Bootstrap
* Maven

## 🏗️ Architecture

```text
Browser
 ↓
Thymeleaf
 ↓
Controller
 ↓
Service
 ↓
Repository
 ↓
JPA/Hibernate
 ↓
MySQL
```

## 💰 Money Transfer Flow

```text
Sender
 ↓
Validate Account
 ↓
Validate Balance
 ↓
Debit Sender
 ↓
Credit Receiver
 ↓
Create Transaction Record
 ↓
Commit Transaction
```

If any operation fails:

```text
Rollback
```

## 🔐 Transaction Management

The application uses Spring's transaction management to ensure that money transfer operations are atomic.

Example:

```java
@Transactional
public void transferMoney(...) {
    debit();
    credit();
    saveTransaction();
}
```

## 🔒 Concurrency

The application explores:

* Optimistic locking
* Pessimistic locking
* Race conditions
* Transaction isolation
* Concurrent updates

Example:

```text
Balance = ₹10,000

Request A → Withdraw ₹8,000
Request B → Withdraw ₹8,000

System must prevent an invalid final balance.
```

## 📚 Concepts Learned

* ACID
* Transactions
* `@Transactional`
* Isolation levels
* Locking
* Concurrency
* Database consistency
* BigDecimal
* Transaction history
* Idempotency

## 🎯 Interview Topics

* Explain `@Transactional`.
* What happens when an exception occurs?
* What is transaction rollback?
* Optimistic vs pessimistic locking?
* What is a race condition?
* What is isolation level?
* How would you prevent double withdrawal?
* Why use BigDecimal for money?

## 🔮 Future Improvements

* Spring Security
* JWT
* Redis
* Kafka
* Transaction notifications
* Fraud detection
* Docker
