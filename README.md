# Sursa Employee Management System Upgraded Version 

# Technologies Used

1. Spring Boot
2. Maven
3. Java 8
4. Apache Tomcat
5. MySQL



# Steps I followed

	-------------------------------------------------------------------------------------------------------------
	
	# PHASE ONE #(Environment Configuration)
	
		1. Import a .rar project from https://start.spring.io with the following dependencies. Also select WAR instead of JAR for web projects.
		      - MySQL Driver
		      - Spring Web Starter
		      - Spring Security
		      - JPA
		      - Spring Boot Dev Tools
		      
		2. Generate Deployment Descriptor Stub by rightclicking on Deployment Descriptor at the top. 
		      - This will generate a web.xml file inside ~src/main/webapp/WEB-INF
		
		3. Create a database using a DBMS. 
			  - The database here is called 'employee_mgmt_system_2'.
		
		4. Configure 'application.properties' inside ~src/main/resources
		      - First of all, provide the database information as :
		      	# Database Details
				spring.datasource.url = jdbc:mysql://localhost/employee_mgmt_system_2
				spring.datasource.username=root
				spring.datasource.password = 
				spring.datasource.driver-class-name = com.mysql.cj.jdbc.Driver
		
		5. Go to Run -> Run Configuration -> Maven Build -> New Configuration
		      - Fill in the required details
			  - Goals: spring-boot:run
			  - Add: server.port 8888
		
		6. In the browser, hit localhost:8888
			  - The name will be 'user' by default and the default password will be present in the console :
			  	# Using generated security password: 3f497bfb-c475-4962-af27-0bf12d64d38e
	    
	    7. In case of jdk missing error, select jdk instead of jre in the (Application->Preferences->Installed JRES)
	    
	    8. Now you can finally update project by right clicking on the project.
	          - Maven -> Update Project
	          
	-------------------------------------------------------------------------------------------------------------
	
	# PHASE TWO #(User Login and Registration)
	   
	   
	   1. Create database tables and sample user data.
	   
	   	     - There are three tables in user management.
	   	     	- auth_user
	   	     	- auth_user_role
	   	     	- auth_role  =>  SUPER_USER, ADMIN_USER, SITE_USER
	   	     - Execute the SQL queries from UserManagementTables.sql from the site below Or just copy it from below.
	   	     	- https://tutorials.webencyclop.com/spring-boot/03-create-user-login-registration/
	   	     	
	   	     	DROP TABLE IF EXISTS auth_user_role;
				DROP TABLE IF EXISTS auth_role;
				DROP TABLE IF EXISTS auth_user;
				CREATE TABLE auth_role (
				  auth_role_id int(11) NOT NULL AUTO_INCREMENT,
				  role_name varchar(255) DEFAULT NULL,
				  role_desc varchar(255) DEFAULT NULL,
				  PRIMARY KEY (auth_role_id)
				);
				INSERT INTO auth_role VALUES (1,'SUPER_USER','This user has ultimate rights for everything');
				INSERT INTO auth_role VALUES (2,'ADMIN_USER','This user has admin rights for administrative work');
				INSERT INTO auth_role VALUES (3,'SITE_USER','This user has access to site, after login - normal user');
				
				CREATE TABLE auth_user (
				  auth_user_id int(11) NOT NULL AUTO_INCREMENT,
				  first_name varchar(255) NOT NULL,
				  last_name varchar(255) NOT NULL,
				  email varchar(255) NOT NULL,
				  password varchar(255) NOT NULL,
				  status varchar(255),
				  PRIMARY KEY (auth_user_id)
				);
				
				CREATE TABLE auth_user_role (
				  auth_user_id int(11) NOT NULL,
				  auth_role_id int(11) NOT NULL,
				  PRIMARY KEY (auth_user_id,auth_role_id),
				  KEY FK_user_role (auth_role_id),
				  CONSTRAINT FK_auth_user FOREIGN KEY (auth_user_id) REFERENCES auth_user (auth_user_id),
				  CONSTRAINT FK_auth_user_role FOREIGN KEY (auth_role_id) REFERENCES auth_role (auth_role_id)
				) ;
				
				insert into auth_user (auth_user_id,first_name,last_name,email,password,status) values (1,'admin','admin','admin@gmail.com','$2a$10$DD/FQ0hTIprg3fGarZl1reK1f7tzgM4RuFKjAKyun0Si60w6g3v5i','VERIFIED');
				insert into auth_user_role (auth_user_id, auth_role_id) values ('1','1');
				insert into auth_user_role (auth_user_id, auth_role_id) values ('1','2');
				insert into auth_user_role (auth_user_id, auth_role_id) values ('1','3');
	   	     - Execute the queries into the MySQL which will create required tables and data.
	   
	   
	   2. Define Models for database tables.
	         
	         - Need to create models for corresponding database tables]
	         - Object Relationship Mapping
	         - We will create :
	         	- User for auth_user table
	         	- Role for auth_role table
	         
	         ~AUTH_USER 
	         @Entity
			 @Table(name = "auth_user")
			 public class User {

			 @Id
			 @GeneratedValue(strategy = GenerationType.AUTO)
			 @Column(name = "auth_user_id")
			 private int id;
		
			 @Column(name = "first_name")
			 private String name;
		
			 @Column(name = "last_name")
			 private String lastName;
		
			 @Column(name = "email")
			 private String email;
		
			 @Column(name = "password")
			 private String password;
		
			 @Column(name = "mobile")
			 private String mobile;
		
			 @Column(name = "status")
			 private String status;
		
			 @ManyToMany(cascade = CascadeType.ALL)
			 @JoinTable(name = "auth_user_role", joinColumns = @JoinColumn(name = "auth_user_id"), inverseJoinColumns = @JoinColumn(name = "auth_role_id"))
			 private Set<Role> roles;
			 
			 ~AUTH_ROLE
			 @Entity
			 @Table(name = "auth_role")
			 public class Role {
			 @Id
			 @GeneratedValue(strategy = GenerationType.AUTO)
			 @Column(name = "auth_role_id")
			 private int id;
		
			 @Column(name = "role_name")
			 private String role;
		
			 @Column(name = "role_desc")
			 private String desc;
			 
	   	
	   
	   3. Define Spring Bean for BCryptPasswordEncoder.
	   	     
	   	     - Define the bean for it so that we can autowire this object for this. We will use Java Configurations for defining bean.
	   	     - Implementation for the bean above is provided by the package :
	   	          - org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
	   	     - We need to define the bean which will return this implementation when autowired in another class.
	   	     	  - We will do this in WebMvcConfig class which will implement WebMvcConfigurer.
	   	     	  
	   
	   4. Override Spring Security Default Configurations using Java Config.
	   		 
	   		 - Edit application.properties file, and add two queries.
	   		      - One for : authorization in usersByUsernameQuery
	   		      - Other for : authorization in authoritiesByUserNameQuery
	   		 - Setup Spring Security Configuration using Java Config, to use JDBC based authentication and authorzation.
	   		 - To override the Default Configurations of Spring Security, create the configuration class with @EnableWebSecurity 
	   		   annotation, which will extend WebSecurityConfigurerAdapter class
	   		  

	   5. Define controllers which will show Login and Registration page to User.
	   
	         - We will need to show the login screen and registration screen to our users.
	         - So we will define two controllers, one for showing login screen and another for showing registration screen
	         
	         	@Controller
				public class AuthenticationController {

				@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
				public ModelAndView login() {
					ModelAndView modelAndView = new ModelAndView();
					modelAndView.setViewName("login"); // resources/template/login.html
					return modelAndView;
				}
			
				@RequestMapping(value = "/register", method = RequestMethod.GET)
				public ModelAndView register() {
					ModelAndView modelAndView = new ModelAndView();
					// User user = new User();
					// modelAndView.addObject("user", user); 
					modelAndView.setViewName("register"); // resources/template/register.html
					return modelAndView;
				}
				
				@RequestMapping(value = "/home", method = RequestMethod.GET)
				public ModelAndView home() {
					ModelAndView modelAndView = new ModelAndView();
					modelAndView.setViewName("home"); // resources/template/home.html
					return modelAndView;
				}
			}

	   
	   6. Generate HTML pages for Login and Registration.
	         
	         - Once the controllers are created, we need to write the HTML code which will act as the Spring Views.
	         - As we are using thymeleaf template engine, the files will be picked from these locations :
	         	  - "resources/templates/" + filename + ".html"
	         - So, we just need to create files in resources/templates folder with .html format
	
	
	-------------------------------------------------------------------------------------------------------------
	
	# PHASE THREE #(User Registration and JPA Form Validation)
	
	   1. Show registration page to the user, ysing spring controller and view(HTML).
	   
	   2. Define spring models for database tables.
	   
	   3. Set validations on Spring model - (Server side validations for registration form)
	      Set validation on error message - (Message to display when the validation fails)
	   
	   4. Define Spring Controller for receiving data after form submit.
		  Define Spring Service - (Controller will call service method to register user)
		  Define Spring Repository - (Service method will call the repository method to actually save data to the database)
		     This is how it looks like :
		  	    --> Controller --> Service --> Repository
		  	    
	   5. @Valid User user => Spring will internally validate the HTML form with model validations.
	      BindingResult => It will contain the validation related information
	      ModelMap => We can pass BindingResult Object to HTML view.
	      
	-------------------------------------------------------------------------------------------------------------
	
	
	# PHASE FOUR #(Role Dependent Redirection)
	
	   1. Create 2 pages which will open after login
	         - Process of URL mapping and bind the view(HTML) to it.
	         - Need Url Mapping for /home and /admin
	         - Define the mapping, define the controller method, generate the view and finally load the view from the controller method.
	   			Also, do this.
	   			
	   			http.authorizeRequests()
				// URLs matching for access rights
					.antMatchers("/").permitAll()
				    .antMatchers("/admin/**").hasAnyAuthority("SUPER_USER", "ADMIN_USER")
	   
	   2. Define Configuration class - extends SimpleUrlAuthenticationSuccessHandler
	   
	   3. Declare successHandler in Spring Security configuration.
	   
	
	
	
	         