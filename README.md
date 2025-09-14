# OOP & Database Project - Cooperative Data Management Application

This is a desktop application project built to fulfill the coursework for Object-Oriented Programming and Databases. The application functions as a management system for various types of data within an institution, such as student, employee, goods, and cooperative transaction data.

---
# Badge
![Status](https://img.shields.io/badge/Status-In%20Development-orange)
![Java](https://img.shields.io/badge/Java-19-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-20-blueviolet)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-informational)
![Maven](https://img.shields.io/badge/Build-Maven-critical)
---
## 👨‍💻 Team Members

The following group members contributed to this project:
* C14230180 - David Winata Halim
* C14230268 - Ryan Eka Tjiumena
* C14230174 - Daryanto Tanawi
* C14230150 - David Kristian Susanto

---

## 🛠️ Technologies Used

This project was built using the following technologies:
* **Programming Language:** Java 19
* **UI Framework:** JavaFX 20
* **Database:** PostgreSQL
* **Build Tool:** Apache Maven
* **Libraries:**
    * iText (For exporting data to PDF)
    * Apache POI (For exporting data to Excel)

---

## 🚀 Key Features

* **Complete Data Management (CRUD):** Add, view, update, and delete data for entities like Students, Employees, Cooperative Staff, Goods, and Departments.
* **Data Search:** A search feature to easily find specific data.
* **Export to PDF:** Every data table can be exported into a PDF document.
* **Cooperative Statistics:** Displays important statistics such as the number of transactions per month and total profit.

---

## ⚙️ Installation & Setup Guide

To run this project in your local development environment, follow these steps:

### I. Database Setup

1.  **Open pgAdmin**: Launch the latest version of pgAdmin.
2.  **Login to Server**: Log in to your database server.
3.  **Create a New Database**: Create a new database (e.g., named `projek_pbo`).
4.  **Execute Query**: Copy and run the SQL query from the `Query Project PBO DB.txt` file to create all the necessary tables and structures.

### II. Application Configuration

1.  **Open Project**: Open this project using IntelliJ IDEA or another Java IDE.
2.  **Configure Connection**:
    * Open the `ConnectionManager.java` file in the `src/main/java/com/example/projekpbobd/util/` directory.
    * Adjust the database name, username, and password to match your PostgreSQL configuration.
3.  **Install Fonts (If Needed)**:
    * If the fonts do not display correctly, open the `src/main/resources/com/example/projekpbobd/fontApp` directory.
    * Select all font files (`.ttf`), right-click, and choose "Install for all users".
4.  **Run the Application**:
    * Find and run the `MainApplication.java` class.

The application should now run correctly.
