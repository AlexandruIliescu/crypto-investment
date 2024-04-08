# Crypto Investment Application
## Description
The Crypto Investment Application is a comprehensive Spring Boot-based RESTful API designed for cryptocurrency data ingestion and analysis. It allows for the ingestion of cryptocurrency data from CSV files, and provides statistical analysis of the data, including normalized range calculations, and min, max, oldest, and newest value statistics for specified cryptocurrencies. Built with a hexagonal architecture, the application emphasizes maintainability and testability, supported by a suite of unit and integration tests for robust service and port validation.

## Prerequisites
- Spring Boot 3.2.4
- Java 17
- Maven
- Docker 
- PostgreSQL

 
## Setup and Run
### Build the Project
First, ensure you are in the project's root directory. Then, build the project using Maven with the development profile activated. This profile is configured to use the appropriate application properties for development.

`mvn clean package -D spring.profiles.active=dev`
### Build Docker Image
After successfully building the project, create a Docker image named java-jokes-app. This image will contain the compiled application and all necessary dependencies.

`docker build -t crypto-app .`


### Run Docker Image
Once the Docker image is built, you can run the microservice inside a Docker container. The following command maps port 8080 of the container to port 8080 on your host, allowing you to access the API through localhost:8080.

`docker run -p 8080:8080 crypto-app`

## Additional Docker Commands

### List Running Containers
To view all currently running Docker containers, use:
`docker ps`

### Stop a Container
To stop a running container, you need the container's ID or name. You can find this information using the docker ps command. To stop the container, use:

`docker stop <container_id_or_name>`

---

## Configuration
### Application Properties
The application is configured to run in a development environment by default. The application.properties file contains production settings, including the PostgreSQL database connection details, while the application-dev.properties file provides overrides for development, particularly changing database connection settings for local development. 

If you plan to start Crypto Application using an IDE, please start the application in `dev` profile.

### Database Setup
Ensure PostgreSQL is running and accessible at the specified URI in the application.properties file before starting the application. For Docker environments, the docker-compose.yml file is configured to set up and run a PostgreSQL container alongside the application.

---

## Usage
### Ingest Cryptocurrency Data
Ingest cryptocurrency data by uploading a CSV file through the /api/crypto-data/prices endpoint. The file must follow a specific format with headers: timestamp, symbol, and price.

### Fetch Cryptocurrency Statistics
Access various endpoints under /api/crypto-statistics to retrieve statistical analyses of the ingested cryptocurrency data, including min, max, oldest, and newest values for specific cryptocurrencies, as well as normalized ranges for comparison.

### OpenAPI Documentation
The application is equipped with OpenAPI documentation, accessible through the `http://localhost:8080/swagger-ui/index.html/` endpoint, providing a detailed overview of all available API endpoints, their expected parameters, and response formats.

### Testing
Comprehensive unit and integration tests ensure the reliability of the services and ports. The hexagonal architecture facilitates isolated testing of each component, ensuring the application's robustness and reliability.

---

### Docker Compose Setup
The included docker-compose.yml file simplifies the setup process by orchestrating the application and its PostgreSQL dependency, ensuring a seamless development and deployment experience.

### Running the Application
To run the application, use the Docker commands provided above to build and run the Docker image. The application will be accessible on localhost:8080.