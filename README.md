Business requirement
--------------------
As a web service consumer, I would like to calculate Net, Gross, VAT amounts for purchases in Austria so that I can use correctly calculated data in my system. 

Specification
-------------
- When the system receives one of the Net, Gross or VAT amounts And additionally a valid Austrian VAT rate (10 %, 12 %, 13 %, 20 %)
- Then the system calculates the other two missing amounts (e.g. Net, Gross or VAT) based on the input
- When the system receives no or invalid (<=0 or non-numeric) VAT rate
- Then the system provides an error with meaningful error message
- When the web service consumer provides no amount
- Or invalid amount (<=0 or non-numeric)
- Or more than one input amount
- Then the system provides an error with meaningful error message

Technical requirements
----------------------
- Solution shall provide SOAP and REST interface (no authentication/authorization is necessary)
- Soap implementation needs to follow the contract first approach
- Application shall be based on Spring Framework, but any well-known/trusted 3rd party libraries can be used additionally
- Maven must be used for dependency management and build
- Application needs to run in Tomcat

You can use the link below to test the calculation logic:
http://www.calkoo.com/?lang=3&page=8

Implementation notes
--------------------
- Used pure Spring (Spring Boot) as core technology for implemeting defined requirement (not third party libs used). From what I understood during the first-round interview with Mr. Kiss, Global Blue uses Spring Boot for new projects, therefore I tried to implement everything (and at the same time introduce myself to Spring Boot more closely) using Spring only (otherwise, probably other technologies, eg. Apache CXF would be used).

- Provided SOAP/REST APIs are basically using the same core logic defined in related service that calculates VAT
- implemented JUnit tests (separate classes for SOAP/REST), which should cover test cases/scenarious from requirement
- in addition, additiobal consumer services created for both SOAP/REST (also covered with JUnit test)

Notes
-----
- default port (8080) for Tomcat is overriden in **application.properties** file (since for me on Linux it was already used)
- for running some of JUnit tests, I had to also use annontation for setting-up the port (e.g. **@WebIntegrationTest(randomPort = true)** or **@WebIntegrationTest("server.port:8097"))**. Hope you'll have no issues running the tests.

Additional Notes
----------------
- implementation done on Ubuntu, using IntelliJ, Java 8, Tomcat 8, Maven 3
- the project should be able to be build from command line using Maven and/or from IDE
