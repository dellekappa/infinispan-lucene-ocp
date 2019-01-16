This project is a prototype deployable inside openshift to test the integration with lucene and infinispan.
It uses infinispan as base index directory storage and infinispan is configured to evict cached object in a dataource.

This test project works only with a MySql database and can be run as a spring boot application or deployed as a WAR in an application server.

To start the project you can:

`mvn spring-boot:run`

or you can build it

`mvn package`

and launch the jar produced

`java -jar target/infinispan-lucene-ocp-1.0.0-SNAPSHOT.jar`

The application will try to connect the database configured in src/main/resources/application.yml

When the application is up and runnning you can go to http://localhost:8080 and a very simple html page will appear.
The first time you will use this app you must build the lucene indexes. After that you write in the combo to simulate the lucene search

you can try hitting 

ROSSI
MARIO
VERDI
BIANCHI

and so on
