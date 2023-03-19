# springrestfulserv

RESTful API using Spring HATEOAS.
Actions GET, POST, PUT, PATCH, DELETE retrieves, saves, updates and deletes (CRUD) data.

Example usage of the API:

Action GET through HTTP path /guests/1 (JSON result will bring hyperlinks that signal valid actions according to resource state):

$ : curl -v localhost:8080/guests/1 | json_pp

GET /guests/1 HTTP/1.1

Host: localhost:8080

User-Agent: curl/7.74.0

HTTP/1.1 200 

Content-Type: application/hal+json

![Screenshot from 2023-03-18 21-43-20](https://user-images.githubusercontent.com/93228693/226147608-4f2c33af-9fbc-41cd-b86e-21bcdf7844da.png)

===

Class diagram:

![guest](https://user-images.githubusercontent.com/93228693/226147834-74d086d2-839f-4b47-bad8-9c6ca700c45b.png)



