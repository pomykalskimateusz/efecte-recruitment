### Swagger API available at `http://localhost:8080/swagger-ui/index.html`

## Application config requirements

### 1. Setup ENVs
- database_username
- database_password
- database_url

### 2. Create schema `db_changelog` in database

## Local environment
1. Run the `docker-compose -f docker/docker-compose.yml up --build`

## Production environment

1. create `db_changelog` schema in the database
2. `/mvnw clean install`
3. Run the `efecte-post-it.jar` program passing correct env variables to the database
```
   ~/.jdks/openjdk-21.0.1/bin/java -jar efecte-post-it.jar \
   --database_url=jdbc:postgresql://localhost:5432/efecte \
   --database_username=postgres \
   --database_password=postgres
```
