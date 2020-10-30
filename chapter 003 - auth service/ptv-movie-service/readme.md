# Swagger API
* Add swagger maven dependency in pom file
* Add swagger configurations to scan package
* Access url http://localhost:8080/swagger-ui.html

# Patch Movie
`curl -i -X PATCH http://localhost:8080/movies/5 -H "Content-Type: application/json-patch+json" -d '[
    {"op":"replace","path":"/title","value":"Patched title"}, 
    {"op":"replace","path":"/description","value": "This is new patched description"}
]'`

# Spring Security JDBC Authentication
## 1 - Create database resources
* Create user table (users)
* Create entity for user table (UserEntity)
* Create a repository for user (UserRepository)

## 2 - Create User Details
### 2.1 Principal
* Create PTVUserDetails (principal) extends with UserDetails
* Pass UserEntity in constructor
* Override all methods and returns values accordingly
### 2.2 Detail Service
* Create PTVUserDetailService implement UserDetailsService
* Pass UserRepository to newly created detail service
* Override loadByUsername method, and load user with the help of repository
* Wrap loaded UserEntity object in PTVUserDetails (principal) and return

## 3 - SQL Script
* Write create user query in schema.sql file
* Write insert records in user table in data.sql

## 4 - Disable auto generation
* Open application.properties file and write
* spring.jpa.hibernate.ddl-auto=none

## 5 - Configure Security
* Create DAO authentication provider and pass UserDetailService
* Override configure(AuthenticationManagerBuilder auth)
* Set authentication provider
