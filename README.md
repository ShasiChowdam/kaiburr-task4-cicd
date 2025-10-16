## Task 4 – CI/CD Pipeline (Kaiburr Assessment)

This repository demonstrates a CI/CD pipeline for the Task API backend built in **Task 1**.

### 🚀 Pipeline Overview
- **Tool:** GitHub Actions  
- **Build Step:** Maven `clean package`  
- **Artifact:** `target/task-api-0.0.1-SNAPSHOT.jar`  
- **Docker Step:** Builds Docker image using the JAR artifact

### 🟢 Results
- ✅ Code Build – SUCCESS  
- ✅ Docker Build – SUCCESS  

The pipeline triggers automatically on each push or pull-request to `main`.
