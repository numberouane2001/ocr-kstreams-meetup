# ocr-kstreams-meetup
Kafka Streams applications

This repository contains the source code of the Kafka streams apps developped for the project ocr-k8s-meetup (a small app analysing the Meetup activities in France).

PREREQUISITES: 
It requires Eclipse IDE 2019-03 (or +).
It requires Java 8.
It requires Maven 3.0.5 (or +)

CONTENT: 
It contents the POM file to initialize the project. It downloads the Cassandra drivers and the Kafka Streams library.
It contents the source code of the streaming app, getting the Json messages in Kafka broker, parses it and inserts it in Cassandra. 

Building JAR files:
In order to build JAR file, for each project go into the main directory of the project. 
Call the instruction mvn package.
It should return the message Build Success.
The jar file can be retrieved in the folder "target".


DEPLOYMENT: In order to deploy the scripts, please follow the instructions in the project ocr-k8s-meetup

ROADMAP:
- Change the hostname for Kafka to a parameter
- Change the hostname for Cassandra to a parameter
