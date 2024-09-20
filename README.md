# Tennis Club Reservation System
A Spring Boot application created for InQool Czechia.

## Dependencies
The project was built and tested using the following tools
 - **Gradle 8**
 - **Java 21**

## Folder Structure
tennis-club/   
│ └── src/   
│       ├── main/   
│       │ ├── java/   
│       │ │     └── cz/inqool/tennis_club/   
│       │ │             ├── controller/ # REST controllers for API endpoints   
│       │ │             ├── service/ # Business logic and service layer   
│       │ │             ├── repository/ # Database repositories (Hibernate)   
│       │ │             ├── exception/ # Custom exceptions   
│       │ │             ├── model/ # Entity classes and domain models   
│       │ │             ├── util/ # Utility classes   
│       │ │             └── config/ # Configuration classes (JPA)   
│       │ └── resources/   
│       │       ├── META-INF # Persistence XML file for JPA and Hibernate   
│       │       ├── application.properties # Main application configuration   
│       │       └── db/changelog # SQL changelog for Liquibase   
│       └── test/   
│           ├── java/   
│           │      └── cz/inqool/tennis_club/ # Unit and integration tests   
│           └── resources/   
│                   └── application-test.properties # Test-specific properties   
├── docs/ # Diagrams and their source code   
├── gradle/ # Gradle wrapper JAR   
├── build.gradle # And other gradle files   
├── README.md   
└── .gitignore   

## How to run
1. **Clone the repository:**

    ```bash
    git clone https://github.com/ohtenkay/tennis-club.git
    cd tennis-club
    ```

2. **Run the application**

    To run the app:

    `./gradlew bootRun`

    By default, seeding the database is enabled in applicaion.properties.
    To disable it, set seedData=false.

    You can also seed manually using:

    `./gradlew bootRun --args='seed'`

3. **View the DB live**

    Go to http://localhost:8080/h2-console/ to view the live database.
    username: sa
    password: password

4. **Running tests**

    Run test using:

    `./gradlew test`

    And view the results at the following:

    Code coverage - file://PATH-TO-PROJECT/tennis-club/build/jacocoHtml/index.html
    Test results - file://PATH-TO-PROJECT/tennis-club/build/reports/tests/test/index.html

