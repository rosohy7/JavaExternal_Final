Launch instuctions:

1. Install Apahce Tomcat 8 v8.0.52.

2. Add a user with login 'admin' and password 'admin' to Tomcat. It should have access to 'manager-gui' and 'manager-script' roles.

3. Install MySQL server.

4. Add a user with login 'bankapp' and password 'bankapp' to MySQL. It should have permissions to modify database.

5. Run 'reset_database.sql' script in MySQL. It is located in 'src/main/scripts/MySQL'.

6. Start Apache Tomcat 8 on port 8080.

7. Run 'mvn tomcat7:deploy' in root project directory. On subsequent attempts to deploy the application run 'mvn tomcat7:redeploy'.

8. Connect to '127.0.0.1:8080/bankapp' address using a web browser.

9. To log in as administrator, use login 'root' and password 'root'.



