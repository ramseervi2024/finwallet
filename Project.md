# FinWallet — Digital Wallet & Corporate Salary Banking System

A robust Spring Boot financial banking and payroll engine demonstrating enterprise transaction management, concurrency protection, idempotency, and double-entry ledger bookkeeping.

---

## 🚀 Features Implemented

* **Corporate Treasury Management**:
  * Master corporate payroll account (`CORP-TREASURY-01`)
  * Capital injection / treasury deposit API with concurrency write locks
* **Employee Salary Wallets**:
  * Auto-provisioned salary wallet for every employee (`WAL-EMP-XXXX`)
  * `BigDecimal` financial precision with zero IEEE 754 floating-point inaccuracies
* **Atomic Monthly Salary Disbursement**:
  * Verification of employee active status & base salary
  * Pre-execution corporate treasury liquidity check
  * **Monthly Idempotency Protection**: Enforced unique constraint on `(employee_id, salary_month)` to eliminate accidental duplicate salary payouts
* **Bulk / Batch Payroll Runner**:
  * Company-wide or department-specific automated payroll processing
  * Granular batch reporting (total targeted, successful, skipped duplicate, failed)
* **Peer-to-Peer (P2P) Wallet Transfers**:
  * Instant employee-to-employee wallet transfers
  * Validation against self-transfers and overdrafts
* **Bank Withdrawals**:
  * Outbound money withdrawal to external bank accounts
* **Double-Entry Transaction Ledger (Passbook)**:
  * Audit ledger logging every debit and credit with unique UUID tracking (`TXN-...`, `PAY-...`, `WTH-...`)

---

## 🛠️ Technology Stack

* **Java 17**
* **Spring Boot 3 / 4**
* **Spring Data JPA & Hibernate**
* **MySQL** (Automatic schema migrations with `ddl-auto=update`)
* **Spring Security & JJWT** (Stateless authentication & BCrypt password hashing)
* **Thymeleaf & Modern Glassmorphic CSS** (External client simulation & visual dashboard)
* **OpenAPI / Swagger 3**
* **JUnit 5 & Mockito** (16 automated unit & integration tests)

---

## 💰 Core Business & Banking Flows

### 1. Monthly Payroll Disbursement Flow

```text
[ HR / Admin API Trigger ]
           ↓
[ Check Employee is ACTIVE ]
           ↓
[ Idempotency Guard: salary_month already paid? ] ── YES ──> [ Reject: DuplicateDisbursementException ]
           ↓ NO
[ Acquire DB Pessimistic Lock on Treasury (SELECT ... FOR UPDATE) ]
           ↓
[ Treasury Balance >= Base Salary? ] ─────────────── NO ───> [ Reject: InsufficientBalanceException ]
           ↓ YES
[ Acquire DB Pessimistic Lock on Employee Wallet ]
           ↓
[ Debit Treasury Account (treasury - salary) ]
           ↓
[ Credit Employee Wallet (wallet + salary) ]
           ↓
[ Create SalaryDisbursement Slip (status=SUCCESS, ref=UUID) ]
           ↓
[ Create TransactionLedger Double-Entry Record ]
           ↓
[ Commit Transaction ]
```

---

## 🔒 Concurrency & Race Condition Defense

* **Pessimistic Write Locking (`@Lock(LockModeType.PESSIMISTIC_WRITE)`)**:
  Used on `CompanyTreasuryRepository` and `WalletRepository` to lock database rows at the MySQL level (`SELECT ... FOR UPDATE`).
  Prevents race conditions where two simultaneous transactions attempt to debit the same account or disburse salary concurrently.
* **Optimistic Locking (`@Version`)**:
  Configured on `CompanyTreasury` and `Wallet` entities to protect against lost updates.
* **Idempotency Keys**:
  Composite unique constraint `(employee_id, salary_month)` ensures guaranteed single-payment execution.

---

## 🌐 Endpoints & UI Views

| View / API | URL | Description |
| :--- | :--- | :--- |
| **Salary Banking Dashboard** | `/payroll` | Real-time GUI for treasury, payroll runs, P2P transfers, & live passbook |
| **Classic Directory** | `/ui` | Employee & Department CRUD directory |
| **OpenAPI / Swagger** | `/swagger-ui/index.html` | Interactive Swagger documentation for all REST APIs |
| **Corporate Treasury** | `GET, POST /api/treasury` | Inspect treasury balance and deposit corporate funds |
| **Single Payroll Disburse** | `POST /api/payroll/disburse` | Disburse monthly salary to an employee |
| **Bulk Department Payroll** | `POST /api/payroll/bulk-disburse` | Process batch payroll for department or company |
| **Employee Wallet** | `GET /api/wallets/employee/{id}` | Check employee wallet balance and status |
| **P2P Transfer** | `POST /api/wallets/{id}/transfer` | Transfer funds to another employee's wallet |
| **Withdrawal** | `POST /api/wallets/{id}/withdraw` | Withdraw funds to external bank account |
| **Passbook** | `GET /api/wallets/{id}/passbook` | Paginated double-entry transaction statement |

---

## 🧪 Automated Test Suite

All 16 unit tests run cleanly with zero failures:
```bash
./mvnw test
```
* `EmployeeServiceTest` (CRUD & Caching verification)
* `WalletServiceTest` (P2P transfers, insufficient balance rejection, self-transfer guards)
* `PayrollServiceTest` (Idempotency duplicate checks, treasury balance guards, atomic ledger records)
