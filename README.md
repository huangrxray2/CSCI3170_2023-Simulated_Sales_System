Group number: 26

Student ID and names:

1155173870 Huang Ruixiang
1155141702 ZHANG Haoxiang
1155173761 Ziqin WEI

/* 

Structure of the project file:

src/
├── demo_data
├── sample_data
├── mysql-jdbc.jar
├── SalesSystem.java
├── Databas.java
├── Administrator.java
├── Salesperson.java
└── Manager.java

sample_data/
├── category.txt
├── manufacturer.txt
├── part.txt
├── salesperson.txt
└── transaction.txt


demo_data/
├── category.txt
├── manufacturer.txt
├── part.txt
├── salesperson.txt
└── transaction.txt

Compilation and Run: 

Execute:
Get into the target folder
// Run mysql --host=projgw --port=2633 -u Group26 -p // No explict need 
input password: CSCI3170
javac SalesSystem.java Database.java Administrator.java Salesperson.java Manager.java
then:
java -classpath ./mysql-jdbc.jar:./ SalesSystem



Deployment: 
Upload the srouce file folder to cse Linux machine. // I use Linux12.
Get into this folder

*/