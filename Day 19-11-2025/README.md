# 🏦 Banking Application

A fully functional full-stack **Banking Application** built using **Spring Boot (Backend)** and **React.js (Frontend)** with MySQL as the database.  
This application supports authentication, balance operations, protected routes, and persistent transaction history.

---

## 📌 Features

### 🔐 Authentication
- User Registration  
- User Login  
- Logout clears session  
- Protected Routes (Dashboard & History cannot be accessed without login)

### 💰 Banking Operations
- Deposit amount  
- Withdraw amount  
- Check current balance  

### 📜 Transaction History
- Every banking action (Deposit, Withdraw, BalanceCheck) is saved
- Data is stored in MySQL through Spring Data JPA  
- Users only see **their own transactions**

### 🚫 Route Protection
If a user is logged out and tries to access:
- `/dashboard`
- `/history`

They are automatically redirected to the Login page.

---

## 🛠 Tech Stack

### **Frontend**
- React.js  
- React Router  
- Fetch API  
- CSS  

### **Backend**
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- Hibernate ORM  
- MySQL Driver  

### **Database**
- MySQL (Port: 3306)

---

## 📁 Project Structure

```
banking-app/
│
├── backend/
│   ├── src/main/java/com/test/
│   │   ├── BankingApplication.java
│   │   ├── controller/
│   │   ├── model/
│   │   └── repository/
│   └── src/main/resources/application.properties
│
└── frontend/
    ├── src/
    │   ├── App.js
    │   ├── index.js
    │   ├── App.css
    │   ├── components/
    │   │   ├── Login.js
    │   │   ├── Register.js
    │   │   ├── Dashboard.js
    │   │   ├── TransactionHistory.js
    │   │   └── ProtectedRoute.js
    └── package.json
```

---

# 🖥 Backend Setup (Spring Boot)

## 1️⃣ Configure MySQL  
Create database (optional):

```sql
CREATE DATABASE bankdb;
```

## 2️⃣ Update `application.properties`

Location:  
```
backend/src/main/resources/application.properties
```

```properties
spring.application.name=BankingApplication

spring.datasource.url=jdbc:mysql://localhost:3306/bankdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8082
```

## 3️⃣ Run Backend

In terminal or IDE:

```bash
./mvnw spring-boot:run
```

Server starts at:

👉 http://localhost:8082

---

# 🌐 Frontend Setup (React)

## 1️⃣ Install dependencies

```bash
cd frontend
npm install
```

## 2️⃣ Start React App

```bash
npm start
```

Frontend runs at:

👉 http://localhost:3000

---

# 🔄 API Endpoints

## **Auth Endpoints**
| Method | Endpoint     | Description |
|--------|-------------|-------------|
| POST   | `/register` | Register a new user |
| POST   | `/login`    | Login existing user |

---

## **Banking Endpoints**
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/bank/balance/{username}` | Check balance |
| POST   | `/bank/deposit?username=&amount=` | Deposit money |
| POST   | `/bank/withdraw?username=&amount=` | Withdraw money |

---

## **Transaction Endpoints**
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/transactions/{username}` | Get user-specific transaction history |

---

# 🔒 Protected Routes

The following routes **require login**:
- `/dashboard`
- `/history`

React’s `ProtectedRoute.js` prevents page access unless `loggedIn === true`.
