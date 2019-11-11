# Book Renew
## Contributors
* Alan Bauer
* Ethan Bowers
* Tristan Oshier
* Drew Thomas

## Setup
### Server
1. Install MAMP or XAMPP  
2. Start your apache server and mysql server  
3. Open up phpmyadmin and create a new database `book_renew`  

### Backend
1. Open book_renew_backend as the root directory in a new project in Intellij  
2. Install the maven dependencies  
3. Set the correct values for your installation in application.properties located in src/main/java/resources  
4. As JWTs are used for authentication, the real secret key may need to be updated in src/java/com/bookrenew/api/security/SecurityConstants as it could be compromised if it's in version control  
5. To run the tests, port 8080 must be open, run the Application (ApiApplication in src/main/java/com/bookrenew/api)
6. After this is running the test should work and will recompile your code  

For more help see the HELP.md file located in the backend directory

### Frontend
1. Install Node.js and NPM.
2. Open up the terminal and `cd` into the client directory.  
3. Run `npm install && npm start`  
4. This should open up the project on localhost port 3000  
5. You may have to edit the config file located in client/src/config  

For more help see the README.md in the client directory  
