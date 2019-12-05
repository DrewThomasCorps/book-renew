# Book Renew
## Contributors
* Alan Bauer
* Ethan Bowers
* Tristan Oshier
* Drew Thomas

## Setup
### Server
1. Install MAMP or XAMPP  
2. Move the project to the htdocs folder of MAMP or XAMPP
3. Start your apache server and mysql server  
4. Open up phpmyadmin and create a new database `book_renew`  

### Backend
1. Open book_renew_backend as the root directory in a new project in Intellij  
2. Install the maven dependencies  
3. Set the correct values for your installation in application.properties located in src/main/java/resources  
4. As JWTs are used for authentication, the real secret key may need to be updated in src/java/com/bookrenew/api/security/SecurityConstants as it could be compromised if it's in version control  
5. To run the tests, port 8080 must be open, run the Application (ApiApplication in src/main/java/com/bookrenew/api)
6. After this is running the tests should work and will recompile your code  

For more help see the HELP.md file located in the backend directory

### Frontend
1. Install Node.js and NPM.
2. Open up the terminal and `cd` into the client directory.  
3. Run `npm install && npm start` (PC users will see SKIPPING OPTIONAL DEPENDENCY: fsevents. This dependency is for Mac users).  
5. This should open up the project on localhost port 3000  
6. You may have to edit the config file located in client/src/config  

For more help see the README.md in the client directory  


## Post-Initial Setup
1. Start your apache and mysql server
2. If one of the tables in the database has changed since last running the backend you must either make the change manually or drop the table in the database
3. Run ApiApplication in src/main/java/com/bookrenew/api to start the tomcat server/backend. You may need to open the backend separately in Intellij for this to work. (until this is done the unit tests won't run correctly)
4. `cd` into the client directory and run `npm install && npm start`


## Suppressed Warnings
1. The entities in the backend suppress unused because Spring will call the functions dynamically behind the scenes so an IDE won't recognize them as being used.