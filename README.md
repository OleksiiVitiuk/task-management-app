# Task management app

Task management App is a portfolio project designed to streamline task and project organization. It empowers users to effortlessly create projects, manage tasks, and delegate work to team members.

Core Features:
* Seamlessly create and oversee projects and their tasks
* Assign tasks to teammates with ease
* Get instant email notifications whenever tasks are assigned, updated, or removed
* Upload, download, and manage task files securely through cloud storage integration (Dropbox)

## Tech Stack & Dependencies

### Java & Spring

    Java: 17
    Spring Boot:3.4.4
    Spring Data JPA
    Spring Security
    Spring Web
    Spring Validation
    Spring Boot Mail Starter
    SpringDoc OpenAPI: 2.8.4

### Database

    MySQL Connector/J (runtime)
    H2 Database (for tests/runtime)
    Liquibase: Schema versioning tool

### Testing

    Spring Boot Starter Test
    Spring Security Test
    Spring Boot Testcontainers
    Testcontainers (MySQL)

### Functional

User capabilities:

1. Register and log in.
2. View all projects, tasks, labels and user info.
3. Change current user's info.
4. Upload, download and delete files in cloud storage.
5. Get info about task's changes by email`s notifications.

Admin capabilities:

1. Update user`s role.
2. Create, update and delete projects/tasks/labels.

### Assignment management`s endpoints

    Auth (/auth):
        GET: /registration - Register a new user
        GET: /login - Retrieve a token for Bearer authentication

    Users (/users):
        GET: /me - Retrieve current user's profile
        PATCH: /me - Update current user's profile
        PATCH: /{id}/role - Update a user's role by ID (ADMIN only)

    Projects (/projects):
        GET: / - Retrieve paginated list of projects
        POST: / - Create a new project (ADMIN only)
        PUT: /{id} - Update a project by ID (ADMIN only)
        GET: /{id} - Retrieve a project by I
        DELETE: /{id} - Soft delete a project by ID (ADMIN only)

    Tasks (/tasks):
        GET: / - Retrieve paginated list of tasks
        POST: / - Create a new task and notify assignee (ADMIN only)
        PUT: /{id} - Update a task and notify assignee (ADMIN only)
        GET: /{id} - Retrieve a task by ID
        DELETE: /{id} - Soft delete a task by ID (ADMIN only)

    Attachments (/attachments):
        POST: / - Upload file to cloud storage and save info
        GET: / - Retrieve paginated list of attachments by task
        DELETE: /{id} - Delete attachment from DB and cloud storage
        
    Labels (/labels):
        GET: / - Retrieve paginated list of labels
        POST: / - Create a new label (ADMIN only)
        PUT: /{id} - Update a label by ID (ADMIN only)
        DELETE: /{id} - Soft delete a label by ID (ADMIN only)
    
    Comments (/comments):
        POST: / - Add a comment to a task
        GET: / - Retrieve paginated list of comments by task ID

## Model diagram

![img.png](README_files/img.png)

## How to Clone the Project

To copy this project to your local machine, follow these steps:

1. Open your terminal or command prompt.
2. Navigate to the directory where you want to save the project.
3. Run the clone command:

    `git clone https://github.com/OleksiiVitiuk/task-management-app.git`

4. Change into the project directory: 

    `cd task-management-app`
5. Open the project in your IDE
6. Create a new branch for your work:

    `git checkout -b my-feature-branch`

This project is built with Spring Boot. To run it, make sure you have installed:
* Java 17+
* Maven
* Docker and MySQL

### Running project

**Docker:**

1. Install Docker Desktop (if not installed).

2. Configure the .env file with your settings.

3. Open dropbox [site](https://www.dropbox.com/developers/apps/):
    1. Open or create your dropbox project.
    2. In permissions set:

        1. [X] files.content.write
        2. [X] files.content.read

    3. Then generate access token.
    4. In application.properties set:
       `dropbox.token=your_generated_dropbox_token.`

4. In application.properties write your real email and password:

`spring.mail.username=your@gmail.com`
`spring.mail.password=your_password`

* Can be needed:
    * Go to [Gmail security page](https://myaccount.google.com/security#signin).
    * Find 2 step verification from there. open it.
    * Select "Turn off"


5. Open the terminal and run the following commands:


    docker-compose build  # Build the images
    
    docker-compose up     # Start the project
    
    docker-compose down   # Stop the project

6. After starting the web server, download `task.postman_collection.json`(in `README_files`).

7. Import it into Postman to test the functionality.

**Running Locally**

Open application.properties.

1. Set the required database properties:

   `Database URL`

   `Username`

   `Password`

2. Same algorithm as docker: 3 - 4.

3. Run application.
