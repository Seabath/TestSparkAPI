# TestSparkAPI
This project is an API that record tests launched by any system.

Here is a trello link to follow what's to come: [Trello](https://trello.com/b/bJoQWAA3/testsparkapi)


Technologies
-
The API is written in JAVA 8.\
The library used here to map routes is [Spark java](http://sparkjava.com/)\
The database management system used here is Postgresql, but any can be added if needed.\
To communicate with the database, the library used is hibernate with its dialect PostgreSQL95Dialect.
For more details about hibernate configuration see [here](src/main/resources/hibernate.cfg.xml).

To build the API and run unit tests, you have to use maven.

Usage
-
To use this API, you can build the [dockerfile](Dockerfile) and run the image on your favorite server.

According to [this file](src/main/java/dao/SimpleDAO.java), you can see that database url, user and password must be 
defined as environment variables. So when running your image, don't forget to put those credentials in your container.
You can see [here](docker-compose.yaml) an example of how to use it with docker-compose.




Unit Tests
-
To mock tests, mockito it overly used.
The goal here is to have 1 test class for 1 source class.

Pojo and entity are not tested because there is no logic inside.
DAOs are not tested either, it's an issue I have to address later.\
But as every methods there are calls of single hibernate function, there shouldn't be a problem short term speaking.
And according to `getSessionFactory`method, if a problem must occur, it will be at API launch, i-e. easy to track and
fix.

To record test coverage, I use jacoco; `mvn jacoco:report`.


