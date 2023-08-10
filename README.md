# School Management System

Welcome to the Compass Scholarship Program Management System! This project aims to simplify the administration of the Compass Scholarship program by providing tools to manage classes, instructors, students, coordinators, scrum masters, and squads effectively.

## Description

The Compass Scholarship Program Management System is a comprehensive solution to manage various aspects of a Scholarship Program environment. From creating classes and managing instructors to organizing students into squads, this system aims to simplify administrative tasks and foster better communication among stakeholders.

## Features

### Classroom Management

- Create and manage classes with details like names, coordinators, scrum masters, instructors, students, and squads.
- Initiate and finish classes, ensuring proper status transitions.
- Add students to classes and maintain an optimal student count.

### Personnel Management

- Manage different personnel roles, including instructors, coordinators, and scrum masters.
- View detailed information about personnel and their assignments.
- Easily add new personnel to the system.

### Squad Organization

- Automatically organize students into squads based on predefined criteria.
- Update squad names for better identification and organization.

### Technologies

- Java 17
- Spring Boot 3
- Hibernate
- Maven
- MySQL
- Lombok
- Documentation with OpenAi

## Documentation

API documentation is available on Postman. You can find detailed information about API endpoints, request and response examples, and more in the [API Documentation.](https://documenter.getpostman.com/view/23922939/2s9Xy3sBV6)

## Installation

Follow these steps to set up and run the project:

1. Clone the repository
   
```shell
git clone https://github.com/GabrielMorais2/CHALLENGE-WEEK-VIII-Spring-Boot.git
```

2. Navigate to the project directory

```shell
cd CHALLENGE-WEEK-VIII-Spring-Boot
```

4. Configure the application properties:

Open src/main/resources/application.yaml and set your MySQL database configuration.

4. Build and run the application:
   
```shell
./mvnw spring-boot:run
```

6. Access the application:

The application should be accessible at http://localhost:8080.

## API Endpoints

### Classroom

- GET /api/v1/classes/{id} - Get information about a specific class.
- POST /api/v1/classes - Create a new class.
- PATCH /api/v1/classes/{id}/start - Start a class.
- PATCH /api/v1/classes/{id}/add-students - Add students to a class.
- PATCH /api/v1/classes/{id}/finish - Finish a class.

### Coordinator

- GET /api/v1/coordinators/{id} - Get information about a specific coordinator.
- GET /api/v1/coordinators - Get a list of all coordinators.
- POST /api/v1/coordinators - Create a new coordinator.

### Instructor

- GET /api/v1/instructors/{id} - Get information about a specific instructor.
- GET /api/v1/instructors - Get a list of all instructors.
- POST /api/v1/instructors - Create a new instructor.

### Scrum Master

- GET /api/v1/scrum-masters/{id} - Get information about a specific scrum master.
- GET /api/v1/scrum-masters - Get a list of all scrum masters.
- POST /api/v1/scrum-masters - Create a new scrum master.

### Student

- GET /api/v1/students/{id} - Get information about a specific student.
- GET /api/v1/students - Get a list of all students.
- POST /api/v1/students - Create a new student.

### Squad

- PATCH /api/v1/squads/{classId}/update-squad/{squadId} - Update the name of a squad.
- POST /api/v1/squads/{id}/create-squad - Create squads for a class.

## Contributing

Contributions are highly encouraged! If you have any improvements, bug fixes, or new features to add, please submit a pull request. Make sure to follow the project's coding style and guidelines.
