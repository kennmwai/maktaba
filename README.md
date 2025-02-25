# Maktaba - Online Library with AI Insights

Maktaba is an Online Library (Maktaba) with AI (Akili Bandia) Insights (Maarifa) project built with Spring Boot. This project leverages AI-powered insights to enhance the user experience.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed [JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
- You have a valid `AI_API_KEY` for the AI integration.

## Building the Application

To build the application, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/kennmwai/maktaba.git
   cd maktaba
   ```

2. Build the application using the provided Maven Wrapper:
   ```bash
   ./mvnw clean install
   ```

## Running the Application

To run the application, follow these steps:

1. Ensure you have set the `AI_API_KEY` environment variable:
   ```bash
   export AI_API_KEY=your_ai_api_key_here
   ```

2. Run the application using the provided Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

3. The application will start on `http://localhost:8080`.

## Accessing the API Documentation

This project integrates Swagger/OpenAPI for API documentation. To access the API documentation:

1. Start the application by following the steps in the "Running the Application" section.
2. Open your web browser and navigate to `http://localhost:8080/swagger-ui.html`.

## Configuration

The application requires an AI API key for the AI integration. You can set the `AI_API_KEY` environment variable as shown in the "Running the Application" section. Alternatively, you can add the key to the `application.properties` file:

```properties name=src/main/resources/application.properties
ai.api.key=your_ai_api_key_here
```

The default AI API endpoint is set to use Mistral. You can change the endpoint URL to any OpenAI compatible API by setting the `AI_API_URL` environment variable:

```bash
export AI_API_URL=your_openai_compatible_api_url_here
```

Or by adding it to the `application.properties` file:

```properties name=src/main/resources/application.properties
ai.api.url=your_openai_compatible_api_url_here
```

## Design Decisions

- **Spring Boot**: Chosen for its simplicity and ease of integration with various components.
- **Maven**: Selected as the build tool for its robust dependency management and compatibility with Spring Boot. It simplifies the build process and dependency management.
- **Spring Boot Actuator**: Included for monitoring and managing the application. Provides production-ready features such as metrics, health checks, and monitoring.
- **Spring Data JPA**: Used for data access and ORM. Simplifies database interactions and provides a robust repository abstraction.
- **H2 Database**: Chosen for in-memory database support during development. Helps in quick prototyping and testing.
- **Spring Boot Validation**: Used for validating request parameters and entities. Ensures data integrity and consistency.
- **Spring Boot Web**: Included for building the web layer of the application. Provides a comprehensive stack for developing web applications.
- **Springdoc OpenAPI**: Integrated for easy API documentation and testing using the `springdoc-openapi` library. This library helps to automate the generation of API documentation by examining the application at runtime to infer API semantics based on Spring configurations, class structure, and various annotations. It supports OpenAPI 3, Spring Boot v3 (Java 17 & Jakarta EE 9), JSR-303, Swagger-ui, OAuth 2, and GraalVM native images.
- **Lombok**: Used to reduce boilerplate code through annotations. Simplifies the codebase by generating getters, setters, constructors, and other methods.
- **Spring Boot DevTools**: Included to enhance the development experience. Provides features like automatic restarts, live reload, and configurations for improved development workflow.
- **Spring Boot Starter Test**: Used for unit and integration testing. Provides a comprehensive testing framework with JUnit, Mockito, and other testing libraries.
- **AI Integration**: Uses an AI API for providing insights, with flexible configuration options for different AI providers.

## Maktaba-UI

There is a secondary repository, [maktaba-ui](https://github.com/kennmwai/maktaba-ui), which is a simple Next.js application that communicates with [maktaba](https://github.com/kennmwai/maktaba) The UI will display the list of books, allow users to view detailed information about each book, and display AI-generated insights when requested.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

If you have any questions, feel free to reach out:

- GitHub: [kennmwai](https://github.com/kennmwai)
