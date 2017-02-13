

#Customer service

Service project consisting of basic funtionality like:

1. Create, delete, modify and getting customers
2. Global exception handler
3. Secure url reference (https)
4. Basic test and generate snippets
5. Asciidoctor generate api guide  **(https://localhost:8445/api.html)**



##IDE

Intelij Idea

##Dependency Managment 

Maven

##Framework

Spring 

##Data Storage

MongoDB


##Docker option - on Windows

Run:

Creating the mongodb server

`docker run -p 27017:27017 --name mongo-server  mongo`

Creating the customer service and link him to the mongo server

`docker run --link mongo-server --name customers-service -p 8445:8445 galcohen/customers-service`
