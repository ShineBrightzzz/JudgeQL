# Environment Configuration for JudgeQL

This project uses environment variables for configuration, which are loaded from a `.env` file in the project root directory.

## How It Works

1. The application loads environment variables from the `.env` file at startup.
2. These variables are then made available as system properties.
3. Spring Boot's property placeholder resolution mechanism uses these variables in `application.properties`.

## Setting Up

1. Create a `.env` file in the project root directory.
2. Add your environment-specific configuration values using the format:
   ```
   KEY=VALUE
   ```
3. The application will automatically load these values at startup.

## Available Environment Variables

### Database Configuration
- `DB_URL`: JDBC URL for the database connection
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `DB_DRIVER`: JDBC driver class name

### JPA Configuration
- `JPA_DDL_AUTO`: Hibernate DDL auto setting (create, update, validate, etc.)
- `JPA_SHOW_SQL`: Whether to show SQL queries in logs

### JWT Configuration
- `JWT_SECRET`: Secret key for JWT token signing
- `JWT_EXPIRATION`: JWT token expiration time in milliseconds

### RabbitMQ Configuration
- `RABBITMQ_HOST`: RabbitMQ host
- `RABBITMQ_PORT`: RabbitMQ port
- `RABBITMQ_USERNAME`: RabbitMQ username
- `RABBITMQ_PASSWORD`: RabbitMQ password
- Various RabbitMQ retry and configuration options

## Example .env File

```
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/judgeql
DB_USERNAME=postgres
DB_PASSWORD=123456
DB_DRIVER=org.postgresql.Driver

# JPA Configuration
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true

# JWT Configuration
JWT_SECRET=your-secret-key
JWT_EXPIRATION=3600000

# RabbitMQ Configuration
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
```

## Notes

- The `.env` file should never be committed to version control. It is included in `.gitignore`.
- For production deployments, use system environment variables or other secure methods to provide these values.
- The application will still start if the `.env` file is missing, but will use default values or fail if required properties are not available.
