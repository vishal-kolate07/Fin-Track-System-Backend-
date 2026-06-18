# FinTrac – Finance Tracking System

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![React](https://img.shields.io/badge/React-18.x-blue)
![MySQL](https://img.shields.io/badge/MySQL-Aiven_Cloud-orange)

A comprehensive, production-grade personal finance tracking application built with Java Spring Boot and React.js. This project demonstrates a full-stack development approach — from secure JWT-based authentication to live income/expense tracking, Excel report generation, and email delivery — deployed entirely on cloud infrastructure.

---

## 🚀 Overview

FinTrac is designed to help users track their monthly income and expenses in real time. It includes modules for transaction management, category management, dashboard analytics, report downloads, and email delivery of financial summaries — all secured with JWT authentication and deployed on live cloud platforms.

### Key Features

- **JWT Authentication:** Secure registration, email-based account activation, and login with JWT token-based access control.
- **Income & Expense Tracking:** Add, view, and delete income and expense transactions for the current month.
- **Category Management:** Create and manage custom categories (income/expense type) with icons.
- **Dashboard Analytics:** Real-time summary of total balance, total income, total expense, income/outflow ratio chart, and weekly activity trend.
- **Advanced Filter & Search:** Filter transactions by type, date range, keyword, sort field, and sort order.
- **Excel Reports:** Download current month income/expense data as `.xlsx` files directly from the browser.
- **Email Reports:** Send income/expense Excel reports directly to the registered user's email.
- **Live Deployment:** Backend live on Render, database on Aiven Cloud (MySQL), frontend live on Netlify.

---

## 🛠️ Tech Stack

### Backend
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot, Spring MVC, Spring Security |
| Authentication | JWT (JSON Web Token) |
| Database Access | Spring Data JPA, Hibernate |
| Database | MySQL (hosted on Aiven Cloud) |
| Email | Spring Mail (JavaMailSender) |
| Excel Generation | Apache POI |
| Build Tool | Maven |
| Hosting | Render |

### Frontend
| Layer | Technology |
|---|---|
| Framework | React.js 18 |
| Styling | CSS3, Responsive Design |
| HTTP Client | Fetch API |
| Charts | Chart.js / Recharts |
| Hosting | Netlify |

### Infrastructure
| Component | Platform |
|---|---|
| Backend API | Render (Free Tier) |
| Database | Aiven Cloud – MySQL |
| Frontend | Netlify |
| Version Control | GitHub |

---

## 🏗️ API Modules

| Module | Base Path | Description |
|---|---|---|
| Auth / Profile | `/register`, `/login`, `/profile` | Registration, activation, login, profile |
| Income | `/income` | Add, get, delete current month incomes |
| Expense | `/expense` | Add, get, delete current month expenses |
| Category | `/category` | Create, read, update categories by type |
| Dashboard | `/dashboard` | Summary stats, recent transactions, charts data |
| Filter | `/filter` | Filter/search income or expense transactions |
| Excel | `/excel/dowload/income`, `/excel/dowload/expense` | Download Excel reports |
| Email | `/email/income-excel`, `/email/expense-excel` | Email Excel reports to user |

---

## 🔐 Authentication Flow

1. User registers via `POST /register` — activation email is sent.
2. User clicks the activation link — `GET /activate?token=xxx` activates the account.
3. User logs in via `POST /login` — receives a JWT token.
4. All subsequent API requests include the header: `Authorization: Bearer <token>`.

---

## 🌐 Live Demo

| Layer | URL |
|---|---|
| Frontend (Netlify) | https://fintrac-system-2141.netlify.app |
| Backend API (Render) | https://fin-track-system-backend.onrender.com/api/v1.0/ |
| Database | Aiven Cloud – MySQL (private) |

> **Note:** The backend is hosted on Render's free tier. On first request after inactivity, the server may take 30–60 seconds to wake up. Please wait and retry if you see a connection error.

---

## 🚦 Getting Started (Local Setup)

### Prerequisites

- Java 17+
- Node.js 18+ & npm
- MySQL (local or cloud)
- Maven
- Git

### Backend Setup

```bash
# Clone the repository
git clone https://github.com/<your-username>/Fin-Track-System-Backend.git

# Navigate to project directory
cd Fin-Track-System-Backend

# Configure application.properties
# Set your MySQL URL, username, password, JWT secret, and mail credentials

# Build and run
mvn spring-boot:run
```

### Frontend Setup

```bash
# Clone the frontend repository
git clone https://github.com/<your-username>/Fin-Track-System-Frontend.git

cd Fin-Track-System-Frontend

npm install
npm start
```

---

## 📁 Project Structure (Backend)

src/

├── controller/       # REST Controllers (Income, Expense, Category, Dashboard, Filter, Excel, Email, Profile)

├── service/          # Business logic layer

├── repository/       # Spring Data JPA repositories

├── entity/           # JPA Entity classes

├── dto/              # Data Transfer Objects

├── security/         # JWT filter, SecurityConfig

└── config/           # CORS, Mail, App configuration

---

## 👨‍💻 Developer

**Vishal Kolate**
Java Full Stack Developer | Spring Boot · React.js · REST APIs · JWT · MySQL
📍 Pune, Maharashtra, India
🔗 [LinkedIn](#) | [Portfolio](#) | [GitHub](#)

---

## 📄 License

This project is licensed under the MIT License.
