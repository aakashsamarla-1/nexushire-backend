# NexusHire AI — AI-Powered Resume Intelligence Platform

NexusHire AI is a full-stack resume intelligence platform that analyzes resumes against job descriptions using **Spring Boot, PostgreSQL, RAG and Large Language Models (LLMs)**.

The platform extracts resume content, compares it with a target job description, generates an ATS compatibility score, identifies skill gaps and provides AI-powered recommendations.

## 🚀 Live Demo

**Frontend:**
https://2-omega-eight.vercel.app/

## 🏗️ Architecture

```text
React.js / Vite
        |
        v
Spring Boot REST API
        |
        +----------------------+
        |                      |
        v                      v
Resume Parser             Job Description
PDF / DOCX                    |
        |                      |
        +----------+-----------+
                   |
                   v
          Resume Analysis
                   |
                   v
          RAG / Vector Search
                   |
                   v
             Groq LLM
                   |
                   v
        Hybrid ATS Scoring
                   |
                   v
        Analysis & Recommendations
                   |
                   v
          PostgreSQL / pgvector
```

## ✨ Key Features

* Resume analysis against job descriptions
* AI-powered resume parsing
* ATS compatibility scoring
* Skill gap analysis
* Keyword-based matching
* LLM-based recommendations
* RAG-based contextual retrieval
* PDF and DOCX resume processing
* Spring Boot REST APIs
* PostgreSQL persistence
* Vector-based similarity search
* Groq LLM integration
* Swagger/OpenAPI API documentation
* Docker support
* Resilience4j fault tolerance
* React/Vite frontend
* CI/CD deployment

## 🛠️ Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate
* Spring Security
* REST APIs
* Maven

### Database

* PostgreSQL
* pgvector
* JPA / Hibernate

### AI / GenAI

* Groq LLM API
* Retrieval-Augmented Generation (RAG)
* Prompt Engineering
* Hybrid ATS Scoring

### Frontend

* React.js
* Vite

### DevOps

* Docker
* Docker Compose
* CI/CD
* Render
* Vercel

## 📋 Core Functionality

### 1. Resume Processing

Users can upload resumes in PDF/DOCX format.

The backend extracts the resume content and prepares it for analysis.

### 2. Job Description Analysis

The system accepts a target job description and identifies relevant skills, technologies and requirements.

### 3. AI Resume Analysis

The extracted resume information is processed using the Groq LLM API to generate contextual insights.

### 4. RAG-Based Analysis

Relevant resume and job-description information is retrieved before sending the context to the LLM.

This helps provide more relevant and consistent analysis.

### 5. ATS Scoring

The platform combines:

* Keyword matching
* Skill matching
* LLM-based analysis

to generate an overall ATS compatibility score.

### 6. Skill Gap Analysis

The system identifies skills and technologies present in the job description but missing or insufficiently represented in the resume.

### 7. Recommendations

The AI generates recommendations to improve the resume for the target position.

## 🔌 API

Example endpoints:

```text
GET /api/hello

POST /api/ask?question=What is AI?
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## 🐳 Run with Docker

Start PostgreSQL:

```bash
docker compose up -d
```

Verify that the PostgreSQL container is running:

```bash
docker ps
```

## ▶️ Run the Application

Clone the repository:

```bash
git clone https://github.com/aakashsamarla-1/nexushire-backend.git
```

Navigate to the project:

```bash
cd nexushire-backend
```

Start the application using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

## 🔐 Environment Variables

Configure the required environment variables before running the application.

Example:

```text
GROQ_API_KEY=your_groq_api_key
DATABASE_URL=your_database_url
DATABASE_USERNAME=your_database_username
DATABASE_PASSWORD=your_database_password
```

Do not commit API keys, passwords or other secrets to GitHub.

## 📊 Project Highlights

* Built a complete Java/Spring Boot backend for an AI-powered application
* Integrated an external LLM API with a dedicated AI service layer
* Implemented resume document processing
* Implemented ATS-oriented resume and job-description analysis
* Used PostgreSQL for persistent application data
* Added Docker-based local database setup
* Added fault tolerance using Resilience4j
* Exposed backend functionality through REST APIs
* Added Swagger/OpenAPI documentation

## 🔮 Future Improvements

* Authentication and role-based access control
* Advanced vector search and embeddings
* Resume version management
* Job recommendation engine
* Automated resume improvement
* Analytics dashboard
* Additional LLM providers
* Automated integration testing

## 👨‍💻 Author

**Aakash Samarla**

Java Backend Developer | Spring Boot | Microservices | REST APIs | AWS

GitHub:
https://github.com/aakashsamarla-1

LinkedIn:
https://www.linkedin.com/in/aakash-samarla
