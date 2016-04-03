Business requirement
====================

As a web service consumer, I would like to calculate Net, Gross, VAT amounts for purchases in Austria so that I can use correctly calculated data in my system. 

Specification
=============
1. When the system receives one of the Net, Gross or VAT amounts
And additionally a valid Austrian VAT rate (10 %, 12 %, 13 %, 20 %)

Then the system calculates the other two missing amounts (e.g. Net, Gross or VAT) based on the input
2. When the system receives no or invalid (<=0 or non-numeric) VAT rate

Then the system provides an error with meaningful error message
3. When the web service consumer provides no amount

Or invalid amount (<=0 or non-numeric)
Or more than one input amount
Then the system provides an error with meaningful error message


Technical requirements
======================
· Solution shall provide SOAP and REST interface (no authentication/authorization is necessary)
· Soap implementation needs to follow the contract first approach
· Application shall be based on Spring Framework, but any well-known/trusted 3rd party libraries can be used additionally
· Maven must be used for dependency management and build
· Application needs to run in Tomcat

You can use the link below to test the calculation logic:
http://www.calkoo.com/?lang=3&page=8
