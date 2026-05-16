<div align="center">

# Player Statistics Tracker

### A JavaFX desktop application for tracking and analyzing video game performance.

**CSC 311 — Advanced Programming**  
**Farmingdale State College**

</div>

---

## Project Overview

**Player Statistics Tracker** is a Java-based desktop application that allows users to track their performance across different video games. Users can create an account, log in, add games to their personal profile, record match statistics, and view performance summaries through a dashboard and report page.

The goal of this project was to build a full-stack style Java application using a JavaFX frontend, Java backend logic, and an embedded Apache Derby database. The application stores user, game, and statistics data locally and uses that data to generate summaries, charts, and reports.

This project currently focuses on competitive gaming statistics such as kills, deaths, assists, and score. These metrics work well for shooter-style and multiplayer games, but the project architecture can be expanded in the future to support different stat templates for racing games, sports games, RPGs, puzzle games, and other genres.

---

## Main Features

### User Authentication
- Register a new user account
- Log in using saved credentials
- Store the current user session after login
- Keep each user's games and statistics separate

### Game Management
- Add new games to a user profile
- View all games connected to the logged-in user
- Edit selected game names
- Delete selected games and their connected statistics

### Statistics Tracking
- Select a game from a dropdown
- Add match statistics including:
  - Kills
  - Deaths
  - Assists
  - Score
- Load existing statistics for a game
- Update saved statistics
- Clear form fields when needed

### Dashboard
- Displays user-specific performance summaries
- Shows total games, total kills, and average score
- Lists saved games for the logged-in user
- Displays selected game details such as kills, deaths, assists, score, and K/D ratio

### Reports and Data Visualization
- Generates a performance report using stored database records
- Displays charts for kills, deaths, assists, and score
- Shows calculated values such as:
  - Total deaths
  - Total assists
  - Best score
  - K/D ratio
  - Average score

---

## Technology Stack

| Layer | Technology | Purpose |
|------|------------|---------|
| Frontend | JavaFX, FXML | Desktop user interface and screen layouts |
| Styling | CSS | Custom dark theme, buttons, panels, and cards |
| Backend Logic | Java | Controllers, services, validation, and application logic |
| Database | Apache Derby | Embedded local database for persistent storage |
| Build Tool | Maven | Dependency management and project running |
| IDE | IntelliJ IDEA | Development environment |
| Version Control | Git & GitHub | Branching, commits, and collaboration |
| Design | Figma | LoFi wireframes and HiFi design planning |

---

## Application Architecture

The project follows a layered structure to keep the interface, business logic, models, and database code organized.

```text
Player Statistics Tracker
│
├── Presentation Layer
│   ├── JavaFX Controllers
│   ├── FXML Views
│   └── CSS Styling
│
├── Business Logic Layer
│   ├── GameService
│   ├── StatsCalculator
│   └── Session Management
│
├── Model Layer
│   ├── User
│   └── Stats
│
└── Database Layer
    └── ConnDbOps
```
---

## Project Structure

```
src/main/java/org/example/javafxui
│
├── controller
│   ├── AddGameController.java
│   ├── DashboardController.java
│   ├── LoginController.java
│   ├── RegisterController.java
│   ├── ReportController.java
│   └── StatsController.java
│
├── db
│   └── ConnDbOps.java
│
├── model
│   ├── Stats.java
│   └── User.java
│
├── service
│   ├── GameService.java
│   └── StatsCalculator.java
│
├── MainApp.java
└── Session.java

src/main/resources
│
├── styles
│   └── app.css
│
└── view
    ├── AddGame.fxml
    ├── Dashboard.fxml
    ├── Login.fxml
    ├── Register.fxml
    ├── Report.fxml
    └── Stats.fxml
```
---

## Database Design

The application uses Apache Derby as an embedded local database. The database is created automatically when the application starts.

### Main Tables

```
USERS
- user_id
- username
- email
- password

GAMES
- game_id
- user_id
- game_name

STATS
- stat_id
- game_id
- kills
- deaths
- assists
- score
```
