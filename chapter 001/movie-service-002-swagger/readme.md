# Swagger API
* Add swagger maven dependency in pom file
* Add swagger configurations to scan package
* Access url http://localhost:8080/swagger-ui.html

# Patch Movie
`curl -i -X PATCH http://localhost:8080/movies/5 -H "Content-Type: application/json-patch+json" -d '[
    {"op":"replace","path":"/title","value":"Patched title"}, 
    {"op":"replace","path":"/description","value": "This is new patched description"}
]'`