# Train ticket machine

### Prerequisites

`Docker` and `docker-compose`

Steps to run and test the application:

### Code quality

To view code quality metrics, use sonarqube. To do this, access the folder `sonarqube/` where the document docker-compose.yml is to run a container with sonarqube. Within that directory run:

```sh
docker-compose up
```
Sonarquebe will be available at: [localhost:9000](http://localhost:9000/)

To log in sonarqube use:

`Username: admin`

`Password: admin`

Create a new project on sonarqube and generate a new token.


If necessary, after sonarqube is up, go to the application's root directory and run the following command with the generated token:

```
mvn clean verify -P sonar:sonar -Dsonar.projectKey=ticket-machine -Dsonar.host.url=http://localhost:9000 -Dsonar.login={token}
```

And then update sonarqube to view the project evaluation.


### Run the application

To run the application, go to the application's root directory, where you will see the `pom.xml` file and run the commands:

```sh
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=app/ticket-machine
docker container run -p 9090:9090 app/ticket-machine
```

If something goes wrong with docker, run it as a maven application locally.

The application will be available at: [localhost:9090](http://localhost:9090/)


Endpoints availables: 


[Autocomplete](http://localhost:9090/autocomplete/{word})


[localhost:9090/stations](http://localhost:9090/stations)


[Swagger-ui](http://localhost:9090/swagger-ui.html)


[Actuator-health](http://localhost:9090/actuator/health)


[Actuator-info](http://localhost:9090/actuator/info)

### Test

To execute the automated tests it is possible to execute it in your favorite IDE, downloading the code or via the code quality step, in which the tests are already executed and the results are presented in sonarqube.

But if you really want to do your tests, you can use the the swagger interface (or another tool that feels more comfortable, like the postman) to place inputs and see the results of the autocomplete service.
For this, some train stations are available, whose values can be accessed in the file `java/resources/data.sql` or by the endpoint: [list all stations](http://localhost:9090/stations)
