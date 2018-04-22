# Transfer Application
 
This is money transfer service, processes money transfer between accounts saving actual data in in-memory DB.
The communication is done via HTTP using REST-based architecture.
Application has own database schema in H2 database for accounts and transfers.
 
##Technologies
Application is written in Java language (Java 8) and following libraries and frameworks are used to improve the development process:
- Jersey(https://jersey.github.io/): Jersey frameworks
- Hibernate (http://hibernate.org/): Hibernate framework
- JUnit(https://junit.org/): Test framework
- Log4j (https://logging.apache.org/log4j/2.x/) logging framework
- Restassured(http://rest-assured.io/  framework for unit tests
- Hamcrest matcher
- Embedded Tomcat server
- Embedded H2 Database
 
##Setup a local development environment
It is a Maven project, you can simply open it via Eclipse or IDEA, configure start of application. By starting this application you automatically
start embedded tomcat server on port 8075. 

###To build component run:
    mvn clean package
###To start application:
    java -jar TransferApp.jar [portNumber]
- by default portNumber = 8075
- folder with application jar file has to contain webapp from target/classes, 
in web.xml possible to set application source URI, by default is "webapi"      
    

## Component flow
To start transfer processing firstly application should to have accounts.
After account creation it's possible to perform transfer.

## resource operations
###ACCOUNT
####/webapi/account:
`POST`: create account.

 - Fields accountName and email are unique and mandatory, if there is not field balance account will have balance = 0;
 
######example:

     {
         "accountName": "firstAccount",
         "balance": 15.7,
         "email": "firstAccount@ya.com"
     }
    
`GET`: get list of existing accounts. Can be concretize by account name, email, startDate and endDate, example with accountName:   

      localhost:8075/webapi/account?accountName=firstAccount

####/webapi/account/{uuid}:

`UPDATE`: update account by uuid

######example:

     {
         "accountName": "firstAccount",
         "balance": 15.7,
         "email": "firstAccount@ya.com"
     }

`GET`: Get account By UUID

`DELETE`: delete account by uuid

###TRANSFER

####/webapi/transfer:

`POST`: create transfer.

 - Fields accountFrom, accountTo, balance are mandatory.
 
######example:

    {
        "accountFrom": "52607091-c5d3-499c-9a8b-e0d2a21a0453",
        "accountTo": "e5886e0b-d074-45ea-b2a8-8c9e0327557f",
        "balance": 1.45
    }

`GET`: get list of transfers

####/webapi/transfer/{uuid}:

`GET`: get transfer by uuid

####/webapi/transfer/account/{uuid}:

`GET`: get list of all transfers for this account uuid. Result will contain ingoing and outgoing transfers

######exampleOfResult:

     {
         "ingoingTransfers": [
             {
                 "accountFrom": "9b9dec26-e363-49b1-80d3-3dde2e6c4809",
                 "accountTo": "52607091-c5d3-499c-9a8b-e0d2a21a0453",
                 "balance": 1.45,
                 "created": "2018-04-22T13:20:20.274",
                 "id": "360df447-fa24-4269-8693-72ace1efd49e"
             },
             {
                 "accountFrom": "9b9dec26-e363-49b1-80d3-3dde2e6c4809",
                 "accountTo": "52607091-c5d3-499c-9a8b-e0d2a21a0453",
                 "balance": 1.45,
                 "created": "2018-04-22T13:20:23.186",
                 "id": "d6925c7d-d482-4aba-af77-2b7b8b29dab9"
             }
         ],
         "outgoingTransfers": [
             {
                 "accountFrom": "52607091-c5d3-499c-9a8b-e0d2a21a0453",
                 "accountTo": "9b9dec26-e363-49b1-80d3-3dde2e6c4809",
                 "balance": 1.45,
                 "created": "2018-04-22T13:20:42.403",
                 "id": "7713bea5-fe6b-4883-883f-bb40de1f034c"
             },
             {
                 "accountFrom": "52607091-c5d3-499c-9a8b-e0d2a21a0453",
                 "accountTo": "9b9dec26-e363-49b1-80d3-3dde2e6c4809",
                 "balance": 10.45,
                 "created": "2018-04-22T13:20:46.31",
                 "id": "11956d10-bcad-48ea-b15c-213faba7da71"
             },
             {
                 "accountFrom": "52607091-c5d3-499c-9a8b-e0d2a21a0453",
                 "accountTo": "9b9dec26-e363-49b1-80d3-3dde2e6c4809",
                 "balance": 0.01,
                 "created": "2018-04-22T13:20:51.083",
                 "id": "98cbcde1-9f39-4ef7-9d05-f99e683d2857"
             }
         ]
     }
     
 
 
}