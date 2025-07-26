VotingApp
A Spring Boot-based voting application with admin and user functionalities, using H2 database for data storage.
Setup Instructions

Prerequisites:

Java 17 or later
Maven 3.5+
Git


Clone the Repository:
git clone <repository-url>
cd VotingApp


Build the Project:
mvn clean install


Run the Application:
mvn spring-boot:run


Access the App:

Open your browser and go to http://localhost:8080/.
Use the admin login at the top right (username: admin, password: 1234).


Initialize Admin User:

The admin user (admin, 1234) is preconfigured in SecurityConfig.java.



Features

Homepage: Cast vote button and admin login icon.
Admin Login: Secured with Spring Security (username: admin, password: 1234).
Admin Dashboard: View total votes, edit candidates, reset votes.
Edit Candidates: Add/remove candidates and start voting (minimum 2 candidates).
Voting: Users enter their name, select a candidate, and submit their vote.
Data Storage: Uses H2 in-memory database for voters, candidates, and votes.
Styling: Modern, simple CSS design.

Notes

Change the admin username/password in SecurityConfig.java for production.
Replace H2 with MySQL/PostgreSQL for production (update application.properties).
Enable CSRF protection in SecurityConfig.java for production.
