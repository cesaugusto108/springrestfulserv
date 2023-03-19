# springrestfulserv

RESTful API using Spring HATEOAS.
Actions GET, POST, PUT, PATCH, DELETE retrieves, saves, updates and deletes (CRUD) data.

Example usage of the API:

Action GET through HTTP path /guests (JSON result will bring hyperlinks that signal valid actions according to resource state):

$ : curl -v localhost:8080/guests | json_pp
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying ::1:8080...
* Connected to localhost (::1) port 8080 (#0)
> GET /guests HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 
< Content-Type: application/hal+json
< Transfer-Encoding: chunked
< Date: Sun, 19 Mar 2023 00:24:56 GMT
< 
{ [1074 bytes data]
100  1067    0  1067    0     0   2376      0 --:--:-- --:--:-- --:--:--  2376
* Connection #0 to host localhost left intact
{
    "_embedded": {
        "guestList": [
            {
                "id": 1,
                "name": {
                    "firstName": "Marcela",
                    "lastName": "Carvalho"
                },
                "address": {
                    "street": "Av. Augusto Franco",
                    "number": 142,
                    "city": "Aracaju"
                },
                "telephone": {
                    "telephone": "79999999999"
                },
                "email": "marcela@email.com",
                "emailAddress": {
                    "username": "marcela",
                    "domainName": "@email.com"
                },
                "stay": "RESERVED",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/guests/1"
                    },
                    "guests": {
                        "href": "http://localhost:8080/guests"
                    },
                    "check-in": {
                        "href": "http://localhost:8080/guests/1/check-in"
                    },
                    "cancel-reserve": {
                        "href": "http://localhost:8080/guests/1/cancel"
                    }
                }
            },
            {
                "id": 2,
                "name": {
                    "firstName": "João Carlos",
                    "lastName": "Souza"
                },
                "address": {
                    "street": "Rua Boquim",
                    "number": 552,
                    "city": "Aracaju"
                },
                "telephone": {
                    "telephone": "79998989898"
                },
                "email": "joaocarlos@email.com",
                "emailAddress": {
                    "username": "joaocarlos",
                    "domainName": "@email.com"
                },
                "stay": "CHECKED_IN",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/guests/2"
                    },
                    "guests": {
                        "href": "http://localhost:8080/guests"
                    },
                    "check-out": {
                        "href": "http://localhost:8080/guests/2/check-out"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/guests"
        }
    }
}

===

Class diagram:

![guest](https://user-images.githubusercontent.com/93228693/226147268-f0ea7891-2646-4d7b-b537-13b4c27150fd.svg)


