# OrderManager

This is a simple order manager tha possess a Postgres Database and a service that sends e-mails to users.

# How to run this project

First clone this repository to your workstation by using this command:

`https://github.com/rlourenco29a/OrderManager.git`

Then before compiling the code, be sure to have Postgres installed on your workstation and have a database ready

on the 'src/main/resources' in the application.properties and edit the following properties:

`on spring.datasource.url edit {database-Name} to match the database name and {database-schema} to use a schema
spring.datasource.username={database-username} to match your username
spring.datasource.password={database-password} to match your password

I used a gmail account to send the emails of completed orders

spring.mail.username={email-username} to match your email
spring.mail.password={email-password} to match your password or app password
`

After changing these properties, please go to your database administration tool (I was using DBeaver) and please run the data.sql queries because I was having problems running those at the launch of the application.

After that go to the root of the project and open a command terminal and execute the following command 

`mvn clean install -Dmaven.test.skip=true` there is the tag to skip tests becaus in this project there is currently no test to be executed.

After running the mvn command you can use java -jar {path_to_project}/target/ordermanager-0.0.1-SNAPSHOT.jar pt.exercise.ordermanager to start the application

To test the applicarion I left in the src/main/resources a Postman collection with the request for the controllers
