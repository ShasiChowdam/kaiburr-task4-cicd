Kaiburr Task 1 — Java Backend and REST API
A Spring Boot + MongoDB REST API that manages and executes user-defined “Task” objects. Each Task represents a shell command that can be created, listed, searched, deleted, and executed.
Project Overview
This project implements Task 1 of the Kaiburr Assessment:

Implement an application in Java that provides a REST API for creating, deleting, searching, and running “task” objects, which represent shell commands to be run in a Kubernetes pod.
Technologies Used
• Java 22 (Spring Boot 3)
• MongoDB 6 (via Docker)
• Maven 3.9+
• Postman / cURL for testing
• Windows-safe command execution
Prerequisites
• JDK 17 or higher
• Maven installed and added to PATH
• Docker Desktop running
• Postman for API testing
Setup Instructions
1. Start MongoDB:
   docker run -d --name mongo -p 27017:27017 mongo:6

2. Build and Run the Application:
   mvn clean package
   mvn spring-boot:run

Server starts at http://localhost:8080
REST API Endpoints
GET /api/tasks - Get all tasks or one by ID
PUT /api/tasks - Create / update a task
GET /api/tasks/find?name={name} - Search task by name
PUT /api/tasks/{id}/execution - Execute a task command
DELETE /api/tasks/{id} - Delete a task by ID
Example Requests (Postman / cURL)
Create Task:
curl -X PUT http://localhost:8080/api/tasks -H "Content-Type: application/json" -d "{\"name\":\"Print Hello\",\"owner\":\"Ananya\",\"command\":\"echo Hello\"}"

Get All Tasks:
curl http://localhost:8080/api/tasks

Execute Task:
curl -X PUT http://localhost:8080/api/tasks/<task_id>/execution

Find by Name:
curl "http://localhost:8080/api/tasks/find?name=print"

Delete Task:
curl -X DELETE http://localhost:8080/api/tasks/<task_id>
Postman Results (Screenshots)
All request/response proofs are stored in the outputs/ folder:
1_get_all_tasks.png - List all tasks
2_put_create_task.png - Create task
3_put_execute_task.png - Execute task
4_get_find_task.png - Search by name
5_delete_task.png - Delete task
0_docker_running.png - MongoDB container running
Security
Commands are validated using CommandValidator to prevent unsafe input (e.g., rm -rf, |, ;, wget, etc.). ShellRunner automatically switches between /bin/sh and cmd.exe depending on OS.

