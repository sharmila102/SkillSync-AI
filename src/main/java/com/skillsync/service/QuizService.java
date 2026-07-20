package com.skillsync.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class QuizQuestion {
                private int id;
                private String questionText;
                private List<String> options;
                private int correctOptionIndex;
        }

        private static final int QUIZ_QUESTION_COUNT = 10;
        private final Map<String, List<QuizQuestion>> questionBank = new HashMap<>();

        public QuizService() {
                // 1. Java
                questionBank.put("Java", Arrays.asList(
                                new QuizQuestion(1, "Which of the following is NOT a feature of Java?",
                                                Arrays.asList("Object-Oriented", "Use of pointers",
                                                                "Platform Independent", "Multi-threaded"),
                                                1),
                                new QuizQuestion(2, "What is the size of double variable in Java?",
                                                Arrays.asList("16 bits", "32 bits", "64 bits", "128 bits"), 2),
                                new QuizQuestion(3,
                                                "Which method is called first when a Java program starts execution?",
                                                Arrays.asList("start()", "init()", "main()", "run()"), 2),
                                new QuizQuestion(4, "Which keyword is used to prevent method overriding in Java?",
                                                Arrays.asList("static", "abstract", "final", "private"), 2),
                                new QuizQuestion(5, "Which class is the superclass of all classes in Java?",
                                                Arrays.asList("Object", "Class", "String", "System"), 0)));

                // 2. Spring Boot
                questionBank.put("Spring Boot", Arrays.asList(
                                new QuizQuestion(1,
                                                "Which annotation is used to mark a class as a Spring Boot application configuration?",
                                                Arrays.asList("@SpringBootConfiguration", "@EnableAutoConfiguration",
                                                                "@ComponentScan", "@SpringBootApplication"),
                                                3),
                                new QuizQuestion(2,
                                                "What is the default port of the embedded Tomcat server in Spring Boot?",
                                                Arrays.asList("8080", "8081", "9090", "3000"), 0),
                                new QuizQuestion(3, "Which annotation is used to inject dependencies in Spring Boot?",
                                                Arrays.asList("@Inject", "@Autowired", "@Resource", "@Bean"), 1),
                                new QuizQuestion(4,
                                                "What does the star starter dependency (spring-boot-starter-web) include by default?",
                                                Arrays.asList("Tomcat, Spring MVC", "Jetty, Jackson",
                                                                "Thymeleaf, Security", "JPA, Hibernate"),
                                                0),
                                new QuizQuestion(5,
                                                "In which file do you define application configuration properties in Spring Boot?",
                                                Arrays.asList("config.xml", "application.properties", "web.xml",
                                                                "spring.properties"),
                                                1)));

                // 3. Git
                questionBank.put("Git", Arrays.asList(
                                new QuizQuestion(1,
                                                "How do you check the current status of your Git working directory?",
                                                Arrays.asList("git check", "git diff", "git status", "git log"), 2),
                                new QuizQuestion(2,
                                                "Which command is used to save changes temporarily without committing them?",
                                                Arrays.asList("git save", "git stash", "git cache",
                                                                "git commit --temp"),
                                                1),
                                new QuizQuestion(3,
                                                "How do you download the latest changes from a remote repository without merging?",
                                                Arrays.asList("git pull", "git download", "git sync", "git fetch"), 3),
                                new QuizQuestion(4, "What is the command to create a new branch named 'feature-test'?",
                                                Arrays.asList("git branch create feature-test",
                                                                "git checkout -b feature-test",
                                                                "git newbranch feature-test",
                                                                "git branch -n feature-test"),
                                                1),
                                new QuizQuestion(5,
                                                "Which command merges the commit history of one branch into another by copying commits?",
                                                Arrays.asList("git rebase", "git merge", "git clone", "git push"), 0)));

                // 4. Docker
                questionBank.put("Docker", Arrays.asList(
                                new QuizQuestion(1,
                                                "What is the standard name of the file used to define steps for building a Docker image?",
                                                Arrays.asList("docker.config", "Dockerfile", "DockerCompose",
                                                                "image.txt"),
                                                1),
                                new QuizQuestion(2,
                                                "Which command runs a container in detached mode (in the background)?",
                                                Arrays.asList("docker run -d <image>", "docker run -b <image>",
                                                                "docker run -g <image>",
                                                                "docker start --background <image>"),
                                                0),
                                new QuizQuestion(3, "What command lists all active running Docker containers?",
                                                Arrays.asList("docker show", "docker ps", "docker images",
                                                                "docker list"),
                                                1),
                                new QuizQuestion(4, "What does a Docker Volume solve?",
                                                Arrays.asList("Resource limits",
                                                                "Data persistence when container stops",
                                                                "Network speed", "Image build caching"),
                                                1),
                                new QuizQuestion(5,
                                                "What command is used to build an image from a Dockerfile in the current folder?",
                                                Arrays.asList("docker run .", "docker compile .",
                                                                "docker build -t name .", "docker make ."),
                                                2)));

                // 5. MySQL / SQL
                questionBank.put("MySQL", Arrays.asList(
                                new QuizQuestion(1,
                                                "Which SQL keyword is used to retrieve only unique values from a column?",
                                                Arrays.asList("UNIQUE", "DIFFERENT", "DISTINCT", "LIMIT"), 2),
                                new QuizQuestion(2,
                                                "Which join returns all rows from the left table, and matching rows from the right?",
                                                Arrays.asList("INNER JOIN", "LEFT JOIN", "RIGHT JOIN",
                                                                "FULL OUTER JOIN"),
                                                1),
                                new QuizQuestion(3,
                                                "Which constraint uniquely identifies each record in a database table?",
                                                Arrays.asList("FOREIGN KEY", "UNIQUE KEY", "PRIMARY KEY", "CHECK"), 2),
                                new QuizQuestion(4,
                                                "Which function returns the total number of rows matching query conditions?",
                                                Arrays.asList("SUM()", "TOTAL()", "COUNT()", "NUMBER()"), 2),
                                new QuizQuestion(5,
                                                "Which SQL clause is used to filter records after applying an aggregate function?",
                                                Arrays.asList("WHERE", "HAVING", "GROUP BY", "ORDER BY"), 1)));
                questionBank.put("SQL", questionBank.get("MySQL"));
                questionBank.put("PostgreSQL", questionBank.get("MySQL"));

                // 6. REST API
                questionBank.put("REST API", Arrays.asList(
                                new QuizQuestion(1,
                                                "Which HTTP method is typically used to create a new resource on the server?",
                                                Arrays.asList("GET", "POST", "PUT", "DELETE"), 1),
                                new QuizQuestion(2, "What HTTP status code represents '200 OK' success?",
                                                Arrays.asList("200", "201", "400", "404"), 0),
                                new QuizQuestion(3,
                                                "Which HTTP header is commonly used to specify the format of the response payload?",
                                                Arrays.asList("Accept", "Content-Type", "Authorization", "User-Agent"),
                                                1),
                                new QuizQuestion(4, "What does 'REST' stand for?",
                                                Arrays.asList("Representational State Transfer",
                                                                "Request State Transmission", "Response State Transmit",
                                                                "Representation Standard Transition"),
                                                0),
                                new QuizQuestion(5,
                                                "Which HTTP method is idempotent and updates/replaces an existing resource?",
                                                Arrays.asList("POST", "PATCH", "PUT", "GET"), 2)));

                // 7. Microservices
                questionBank.put("Microservices", Arrays.asList(
                                new QuizQuestion(1,
                                                "Which architectural component dynamically routes client requests to microservices?",
                                                Arrays.asList("Config Server", "API Gateway", "Message Broker",
                                                                "Database Cluster"),
                                                1),
                                new QuizQuestion(2,
                                                "What is the primary benefit of service discovery (e.g. Netflix Eureka)?",
                                                Arrays.asList("Secure endpoints",
                                                                "Dynamically resolve microservice instances network addresses",
                                                                "Cache database queries",
                                                                "Orchestrate Docker containers"),
                                                1),
                                new QuizQuestion(3,
                                                "Which design pattern is used to handle service failures gracefully without crashing other services?",
                                                Arrays.asList("Saga Pattern", "CQRS", "Circuit Breaker",
                                                                "Observer Pattern"),
                                                2),
                                new QuizQuestion(4, "How do microservices primarily communicate asynchronously?",
                                                Arrays.asList("REST API", "gRPC", "Message Queues / Event Brokers",
                                                                "Direct Database shared access"),
                                                2),
                                new QuizQuestion(5, "What does the Saga Pattern solve in microservices?",
                                                Arrays.asList("Load balancing",
                                                                "Distributed data transactions consistency",
                                                                "Centralized logging", "Continuous integration"),
                                                1)));

                // 8. Python
                questionBank.put("Python", Arrays.asList(
                                new QuizQuestion(1,
                                                "Which data type in Python is mutable and declared with square brackets?",
                                                Arrays.asList("Tuple", "List", "Set", "Dictionary"), 1),
                                new QuizQuestion(2, "How do you add an element to the end of a list in Python?",
                                                Arrays.asList("list.add(item)", "list.insert(item)",
                                                                "list.append(item)", "list.push(item)"),
                                                2),
                                new QuizQuestion(3, "Which operator is used for exponentiation in Python?",
                                                Arrays.asList("^", "**", "//", "pow"), 1),
                                new QuizQuestion(4, "How is code block indentation defined in Python?",
                                                Arrays.asList("Curled brackets {}", "Keywords begin/end",
                                                                "Whitespace (spaces/tabs)", "Semicolons"),
                                                2),
                                new QuizQuestion(5, "What is the output of len('Python')?",
                                                Arrays.asList("5", "6", "7", "Error"), 1)));

                // 9. JavaScript
                questionBank.put("JavaScript", Arrays.asList(
                                new QuizQuestion(1,
                                                "Which keyword is used to declare block-scoped variables that can be reassigned?",
                                                Arrays.asList("var", "let", "const", "fixed"), 1),
                                new QuizQuestion(2, "What is the correct way to write an arrow function in JavaScript?",
                                                Arrays.asList("() => {}", "function => {}", "() -> {}", "def() =>"), 0),
                                new QuizQuestion(3, "Which method parses a JSON string into a JavaScript object?",
                                                Arrays.asList("JSON.stringify()", "JSON.parse()", "JSON.toObject()",
                                                                "JSON.load()"),
                                                1),
                                new QuizQuestion(4,
                                                "Which keyword is used to wait for a Promise to resolve inside a function?",
                                                Arrays.asList("async", "wait", "await", "then"), 2),
                                new QuizQuestion(5, "What does the '===' operator do?",
                                                Arrays.asList("Assigns a value", "Compares value only",
                                                                "Compares both value and type",
                                                                "Checks reference address"),
                                                2)));

                // 10. React
                questionBank.put("React", Arrays.asList(
                                new QuizQuestion(1,
                                                "What hook is used to maintain local state in a React functional component?",
                                                Arrays.asList("useEffect", "useState", "useContext", "useReducer"), 1),
                                new QuizQuestion(2,
                                                "How do you pass data from a parent component down to a child component?",
                                                Arrays.asList("State", "Props", "Context", "Redux"), 1),
                                new QuizQuestion(3,
                                                "What hook manages side-effects like fetching data or subscribing to events?",
                                                Arrays.asList("useSideEffect", "useLayoutEffect", "useEffect",
                                                                "useMemo"),
                                                2),
                                new QuizQuestion(4, "What is the virtual DOM in React?",
                                                Arrays.asList("A direct copy of the HTML file",
                                                                "An in-memory representation of real DOM elements",
                                                                "A web browser caching engine", "A CSS processor"),
                                                1),
                                new QuizQuestion(5,
                                                "What must every React element inside a list map return to prevent rendering issues?",
                                                Arrays.asList("A unique key prop", "A div wrap", "A string index",
                                                                "A className"),
                                                0)));

                // 11. HTML5
                questionBank.put("HTML5", Arrays.asList(
                                new QuizQuestion(1,
                                                "Which tag is used to display a self-contained header/banner in HTML5 semantic layout?",
                                                Arrays.asList("<head>", "<header>", "<nav>", "<section>"), 1),
                                new QuizQuestion(2,
                                                "Which HTML5 attribute specifies that an input field must be filled out before submitting?",
                                                Arrays.asList("validate", "mandatory", "required", "need"), 2),
                                new QuizQuestion(3,
                                                "Which semantic element is used to represent self-contained, reusable articles or blog entries?",
                                                Arrays.asList("<section>", "<article>", "<aside>", "<div>"), 1),
                                new QuizQuestion(4, "What is the correct tag for playing audio files in HTML5?",
                                                Arrays.asList("<sound>", "<music>", "<audio>", "<media>"), 2),
                                new QuizQuestion(5,
                                                "Which attribute is used to provide placeholder text in an input element?",
                                                Arrays.asList("value", "hint", "placeholder", "text"), 2)));

                // 12. CSS3
                questionBank.put("CSS3", Arrays.asList(
                                new QuizQuestion(1,
                                                "Which CSS property controls the layout alignment along the main axis in Flexbox?",
                                                Arrays.asList("align-items", "justify-content", "align-content",
                                                                "flex-direction"),
                                                1),
                                new QuizQuestion(2,
                                                "What is the correct selector to style an element with id='submit-btn'?",
                                                Arrays.asList(".submit-btn", "#submit-btn", "submit-btn",
                                                                "*submit-btn"),
                                                1),
                                new QuizQuestion(3,
                                                "Which unit is relative to the font-size of the root element (html)?",
                                                Arrays.asList("em", "rem", "px", "vh"), 1),
                                new QuizQuestion(4,
                                                "Which CSS3 layout model is designed for 2-dimensional (rows and columns) alignments?",
                                                Arrays.asList("Flexbox", "Grid", "Float", "Inline-block"), 1),
                                new QuizQuestion(5,
                                                "What media query syntax is used to target screens smaller than 768px?",
                                                Arrays.asList("@media (max-width: 768px)", "@media (min-width: 768px)",
                                                                "@media screen and 768px", "@media mobile 768px"),
                                                0)));

                // 13. Tailwind CSS
                questionBank.put("Tailwind CSS", Arrays.asList(
                                new QuizQuestion(1, "What is Tailwind CSS?",
                                                Arrays.asList("A CSS preprocessing language",
                                                                "A utility-first CSS framework", "A JavaScript bundler",
                                                                "A responsive UI component pack"),
                                                1),
                                new QuizQuestion(2, "Which utility class is used to center-align text in Tailwind CSS?",
                                                Arrays.asList("align-center", "text-middle", "text-center",
                                                                "content-center"),
                                                2),
                                new QuizQuestion(3, "How do you add hover styles in Tailwind CSS?",
                                                Arrays.asList("hover:bg-blue-500", "bg-blue-500-hover",
                                                                "hover(bg-blue-500)", "hover-bg-blue-500"),
                                                0),
                                new QuizQuestion(4,
                                                "What class adds 16px of horizontal padding (assuming standard 4px steps)?",
                                                Arrays.asList("p-4", "px-4", "py-4", "ps-4"), 1),
                                new QuizQuestion(5, "How do you configure custom colors and extensions in Tailwind?",
                                                Arrays.asList("tailwind.css", "styles.css", "tailwind.config.js",
                                                                "package.json"),
                                                2)));

                // Fallback General Web Dev Questions
                questionBank.put("General Software Engineering", Arrays.asList(
                                new QuizQuestion(1, "What is the primary benefit of test-driven development (TDD)?",
                                                Arrays.asList("Faster compiler steps",
                                                                "Writing tests before writing production code to ensure clean structure",
                                                                "Automating deployment steps",
                                                                "Styling user interfaces"),
                                                1),
                                new QuizQuestion(2,
                                                "Which protocol is stateless and forms the backbone of data exchange on the Web?",
                                                Arrays.asList("FTP", "SMTP", "HTTP", "TCP"), 2),
                                new QuizQuestion(3, "What does CI/CD stand for?",
                                                Arrays.asList("Continuous Integration & Continuous Delivery/Deployment",
                                                                "Code Inspection & Continuous Debugging",
                                                                "Command Interface & Code Directory",
                                                                "Compilation Instance & Connection Device"),
                                                0),
                                new QuizQuestion(4, "Which database concept guarantees ACID properties?",
                                                Arrays.asList("Indexing", "Transactions", "Relational Views",
                                                                "ORM mapping"),
                                                1),
                                new QuizQuestion(5, "What does MVC architecture separate?",
                                                Arrays.asList("Classes, Objects, Methods", "Model, View, Controller",
                                                                "Main, Variable, Class",
                                                                "Microservice, Variable, Configuration"),
                                                1)));

                // 14. Kubernetes
                questionBank.put("Kubernetes", Arrays.asList(
                                new QuizQuestion(1, "What is the primary purpose of Kubernetes?",
                                                Arrays.asList("Writing unit tests",
                                                                "Container orchestration and clustering management",
                                                                "Compiling Java bytecode",
                                                                "Designing relational schemas"),
                                                1),
                                new QuizQuestion(2,
                                                "Which key-value store component acts as the source of truth for cluster state?",
                                                Arrays.asList("Redis", "etcd", "MySQL", "MongoDB"), 1),
                                new QuizQuestion(3, "What is the smallest deployable computing unit in Kubernetes?",
                                                Arrays.asList("Pod", "Container", "Service", "Node"), 0),
                                new QuizQuestion(4,
                                                "Which command line tool is used to interact with Kubernetes clusters?",
                                                Arrays.asList("docker", "kubectl", "git", "npm"), 1),
                                new QuizQuestion(5,
                                                "What Kubernetes resource exposes an internal network application to external traffic?",
                                                Arrays.asList("Secret", "ConfigMap", "Service / Ingress", "Volume"),
                                                2)));

                // 15. Jenkins
                questionBank.put("Jenkins", Arrays.asList(
                                new QuizQuestion(1, "What is Jenkins primarily used for?", Arrays.asList(
                                                "Relational database hosting",
                                                "Continuous Integration and Continuous Delivery (CI/CD) pipelines",
                                                "Client-side style processing", "Image rendering"), 1),
                                new QuizQuestion(2,
                                                "Which text file outlines the automated steps of a Jenkins build pipeline?",
                                                Arrays.asList("pom.xml", "Dockerfile", "Jenkinsfile", "package.json"),
                                                2),
                                new QuizQuestion(3,
                                                "What are the two major syntaxes used for writing Jenkins pipelines?",
                                                Arrays.asList("XML and JSON", "Declarative and Scripted",
                                                                "Java and Python", "YAML and HTML"),
                                                1),
                                new QuizQuestion(4, "What is the role of a Jenkins agent?",
                                                Arrays.asList("To run the main dashboard UI",
                                                                "To execute build steps on remote target machines",
                                                                "To manage user database permissions",
                                                                "To package dependencies"),
                                                1),
                                new QuizQuestion(5,
                                                "Which feature in Jenkins triggers automated builds at specific intervals?",
                                                Arrays.asList("Build Triggers (cron scheduler)", "Plugins manager",
                                                                "Node credentials", "Console output logs"),
                                                0)));

                // 16. Ansible
                questionBank.put("Ansible", Arrays.asList(
                                new QuizQuestion(1, "What is Ansible?", Arrays.asList("A front-end UI framework",
                                                "An agentless configuration management and automation tool",
                                                "A cloud load balancer", "A compiled language compiler"), 1),
                                new QuizQuestion(2, "In which file format are Ansible playbooks written?",
                                                Arrays.asList("JSON", "XML", "YAML", "INI"), 2),
                                new QuizQuestion(3,
                                                "Which file specifies target hosts and group definitions in Ansible?",
                                                Arrays.asList("Inventory file", "Playbook", "Ad-hoc script", "pom.xml"),
                                                0),
                                new QuizQuestion(4, "Which command is used to execute an Ansible playbook?",
                                                Arrays.asList("ansible run", "ansible-playbook", "ansible deploy",
                                                                "ansible-exec"),
                                                1),
                                new QuizQuestion(5, "What is the purpose of Ansible Galaxy?", Arrays.asList(
                                                "A cloud hosting service",
                                                "A central repository for sharing community Ansible roles and collections",
                                                "An analytics dashboard", "A package manager for node"), 1)));

                // 17. Terraform
                questionBank.put("Terraform", Arrays.asList(
                                new QuizQuestion(1, "What type of software tool is Terraform?",
                                                Arrays.asList("An object relational mapper",
                                                                "An Infrastructure as Code (IaC) tool",
                                                                "A testing harness", "A database engine"),
                                                1),
                                new QuizQuestion(2, "What file extension is used for Terraform configuration files?",
                                                Arrays.asList(".tf", ".tfstate", ".hcl.json", ".tfconf"), 0),
                                new QuizQuestion(3,
                                                "Which command initializes a new or existing Terraform configuration directory?",
                                                Arrays.asList("terraform plan", "terraform apply", "terraform init",
                                                                "terraform refresh"),
                                                2),
                                new QuizQuestion(4, "Where does Terraform keep track of managed resource state?",
                                                Arrays.asList("Local git repository", "The terraform.tfstate file",
                                                                "MySQL target table", "Web server cache memory"),
                                                1),
                                new QuizQuestion(5,
                                                "Which command generates execution plans and applies infrastructure modifications?",
                                                Arrays.asList("terraform apply", "terraform output",
                                                                "terraform validation", "terraform generate"),
                                                0)));

                // 18. AWS
                questionBank.put("AWS", Arrays.asList(
                                new QuizQuestion(1, "What does AWS stand for?",
                                                Arrays.asList("Advanced Web Services", "Amazon Web Services",
                                                                "Apex Web Systems", "Aligned Work Solutions"),
                                                1),
                                new QuizQuestion(2,
                                                "Which service provides secure, resizable compute instances in AWS?",
                                                Arrays.asList("S3", "EC2", "RDS", "DynamoDB"), 1),
                                new QuizQuestion(3, "What is the primary highly durable object storage service in AWS?",
                                                Arrays.asList("EFS", "EBS", "S3", "RDS"), 2),
                                new QuizQuestion(4,
                                                "Which AWS service controls authentication and user permission structures?",
                                                Arrays.asList("IAM", "VPC", "Route 53", "CloudWatch"), 0),
                                new QuizQuestion(5, "What is AWS Lambda?", Arrays.asList(
                                                "A container orchestration tool",
                                                "A serverless compute engine executing event-driven code",
                                                "A relational database proxy", "An API analytics dashboard"), 1)));

                // 19. Linux & Bash
                questionBank.put("Linux", Arrays.asList(
                                new QuizQuestion(1, "Which command lists directory contents in Linux terminal?",
                                                Arrays.asList("cd", "ls", "dir", "mkdir"), 1),
                                new QuizQuestion(2, "How do you declare variables in a Bash shell script?",
                                                Arrays.asList("var name = value", "name = value", "name=value",
                                                                "set name value"),
                                                2),
                                new QuizQuestion(3, "Which Linux command alters read/write/execution file permissions?",
                                                Arrays.asList("chown", "chmod", "chperm", "chgrp"), 1),
                                new QuizQuestion(4, "What is the purpose of the 'grep' utility in Linux?",
                                                Arrays.asList("To compile files",
                                                                "To search text patterns within files",
                                                                "To trace network ports", "To copy database entries"),
                                                1),
                                new QuizQuestion(5, "Where are standard system logs located in Linux filesystems?",
                                                Arrays.asList("/etc/log", "/var/log", "/bin/logs", "/opt/log"), 1)));

                // 20. CI/CD
                questionBank.put("CI/CD", Arrays.asList(
                                new QuizQuestion(1, "What does CI stand for in CI/CD pipeline methodology?",
                                                Arrays.asList("Continuous Integration", "Code Inspection",
                                                                "Compilation Instance", "Connection Interface"),
                                                0),
                                new QuizQuestion(2, "What is the principal objective of Continuous Delivery?",
                                                Arrays.asList("Automating unit tests",
                                                                "Making release packages ready to deploy automatically",
                                                                "Executing code syntax linters",
                                                                "Logging user sessions"),
                                                1),
                                new QuizQuestion(3, "Where are GitHub Actions workflows configured in a repository?",
                                                Arrays.asList(".github/workflows/ folder containing YAML files",
                                                                "pom.xml dependencies", "Jenkinsfile scripting",
                                                                "application.properties"),
                                                0),
                                new QuizQuestion(4, "What is the function of a deployment pipeline?", Arrays.asList(
                                                "Parsing text documents",
                                                "Automating code compilation, verification, packaging, and releases",
                                                "Resolving SQL queries", "Compiling HTML pages"), 1),
                                new QuizQuestion(5, "Which of these is a direct benefit of CI/CD pipeline automation?",
                                                Arrays.asList("Longer delivery times",
                                                                "Faster software feedback loops and safer updates",
                                                                "Larger memory prints", "No unit tests needed"),
                                                1)));

                // 21. Kotlin
                questionBank.put("Kotlin", Arrays.asList(
                                new QuizQuestion(1, "What is Kotlin?", Arrays.asList("An interpreted python package",
                                                "A statically-typed JVM compatible programming language",
                                                "A front-end style framework", "A database engine"), 1),
                                new QuizQuestion(2, "Which keyword declares a read-only variable in Kotlin?",
                                                Arrays.asList("var", "let", "val", "const"), 2),
                                new QuizQuestion(3, "How does Kotlin design-level prevent NullPointerExceptions?",
                                                Arrays.asList("With compiler try-catch blocks",
                                                                "With explicit Null safety indicators like the '?' operator",
                                                                "By banning null values entirely",
                                                                "Using default JVM flags"),
                                                1),
                                new QuizQuestion(4, "What is the primary IDE recommended for Android Kotlin coding?",
                                                Arrays.asList("Eclipse", "Android Studio", "Xcode", "Visual Studio"),
                                                1),
                                new QuizQuestion(5, "Which keyword declares functions in Kotlin?",
                                                Arrays.asList("function", "fun", "def", "func"), 1)));

                // 22. Swift
                questionBank.put("Swift", Arrays.asList(
                                new QuizQuestion(1, "Which operating platforms primarily utilize Swift?",
                                                Arrays.asList("Apple platforms (iOS, macOS, watchOS, tvOS)",
                                                                "Windows Desktop and Server",
                                                                "Linux Server distributions", "Android OS devices"),
                                                0),
                                new QuizQuestion(2, "Which keyword declares constants in Swift?",
                                                Arrays.asList("var", "let", "const", "val"), 1),
                                new QuizQuestion(3, "What structures unpack optional variables safely in Swift?",
                                                Arrays.asList("try/catch blocks", "if let / guard let checks",
                                                                "Optional wrappers", "Null pointers"),
                                                1),
                                new QuizQuestion(4, "Which Apple IDE is standard for compiling Swift applications?",
                                                Arrays.asList("Android Studio", "Xcode", "VS Code", "NetBeans"), 1),
                                new QuizQuestion(5,
                                                "Which layout framework represents Apple's modern declarative UI architecture?",
                                                Arrays.asList("UIKit", "SwiftUI", "Xamarin", "Flutter"), 1)));

                // 23. Flutter
                questionBank.put("Flutter", Arrays.asList(
                                new QuizQuestion(1, "Which programming language compiles Flutter applications?",
                                                Arrays.asList("Java", "Dart", "Kotlin", "Swift"), 1),
                                new QuizQuestion(2, "What is Flutter?", Arrays.asList("A serverless runtime cluster",
                                                "A cross-platform native UI framework designed by Google",
                                                "A Python data cleaning tool", "A browser web compiler"), 1),
                                new QuizQuestion(3, "What represents the fundamental UI building block in Flutter?",
                                                Arrays.asList("Element", "Widget", "Component", "View"), 1),
                                new QuizQuestion(4,
                                                "What differentiates StatefulWidget from StatelessWidget in Flutter?",
                                                Arrays.asList("StatelessWidget can trigger redraw loops",
                                                                "StatefulWidget maintains state and dynamically rebuilds",
                                                                "StatelessWidget has state fields",
                                                                "StatefulWidget consumes more memory only"),
                                                1),
                                new QuizQuestion(5,
                                                "Which diagnostic tool checks environment components for Flutter setup?",
                                                Arrays.asList("flutter check", "flutter doctor", "flutter status",
                                                                "flutter inspect"),
                                                1)));

                // 25. React Native
                questionBank.put("React Native", Arrays.asList(
                                new QuizQuestion(1, "What is React Native?", Arrays.asList(
                                                "A web-only compiling library",
                                                "A framework for building native mobile apps using React and JavaScript",
                                                "A relational database schema", "An android virtualization module"), 1),
                                new QuizQuestion(2, "Which core component renders string text in React Native views?",
                                                Arrays.asList("<View>", "<Text>", "<Paragraph>", "<Label>"), 1),
                                new QuizQuestion(3, "How does React Native perform element positioning layout?",
                                                Arrays.asList("CSS Grid", "Flexbox", "Table grids",
                                                                "Absolute coordinate maps"),
                                                1),
                                new QuizQuestion(4,
                                                "What utility method compiles reusable component styles in React Native?",
                                                Arrays.asList("StyleSheet.create", "Style.load", "Component.style",
                                                                "Style.compile"),
                                                0),
                                new QuizQuestion(5, "Which bundler is executed to assemble assets in React Native?",
                                                Arrays.asList("Webpack", "Metro Bundler", "Maven", "Gradle"), 1)));

                // 26. Scikit-Learn
                questionBank.put("Scikit-Learn", Arrays.asList(
                                new QuizQuestion(1, "What is Scikit-Learn?", Arrays.asList("A website style sheet",
                                                "A Python library for machine learning algorithms",
                                                "A deep neural network accelerator", "A SQL server plugin"), 1),
                                new QuizQuestion(2,
                                                "Which package method separates datasets into training and testing components?",
                                                Arrays.asList("train_test_split", "data_divider", "split_set",
                                                                "set_partition"),
                                                0),
                                new QuizQuestion(3,
                                                "Which method fits a model onto provided training samples in Scikit-Learn?",
                                                Arrays.asList("fit()", "train()", "compile()", "learn()"), 0),
                                new QuizQuestion(4,
                                                "Which of these is a machine learning algorithm supported in Scikit-Learn?",
                                                Arrays.asList("Saga transactions", "Decision Tree / Random Forest",
                                                                "Tomcat server", "Websockets"),
                                                1),
                                new QuizQuestion(5, "Which evaluation metric measures regression model error values?",
                                                Arrays.asList("Accuracy Score", "Mean Squared Error (MSE)",
                                                                "Precision Score", "F1 Score"),
                                                1)));

                // 27. TensorFlow
                questionBank.put("TensorFlow", Arrays.asList(
                                new QuizQuestion(1, "What are TensorFlow and PyTorch primarily used for?",
                                                Arrays.asList("Database optimization",
                                                                "Deep learning and neural networks",
                                                                "Front-end web styling", "Automated system deployment"),
                                                1),
                                new QuizQuestion(2,
                                                "What is the core multi-dimensional array structure in both deep learning libraries?",
                                                Arrays.asList("DataFrame", "Tensor", "Matrix list", "Vector series"),
                                                1),
                                new QuizQuestion(3,
                                                "Which neural network layers extract spatial features from images in CNNs?",
                                                Arrays.asList("Dense layer", "Convolutional layer", "LSTM layer",
                                                                "Recurrent layer"),
                                                1),
                                new QuizQuestion(4,
                                                "Which PyTorch method evaluates variable gradients during optimization?",
                                                Arrays.asList("backward()", "gradient()", "optimize()", "fit()"), 0),
                                new QuizQuestion(5,
                                                "What is the role of algorithms like Adam or SGD in training neural networks?",
                                                Arrays.asList("Saving configuration files",
                                                                "Optimizing model weights to minimize loss",
                                                                "Formatting text outputs", "Parsing data structures"),
                                                1)));

                // 28. Data Science (for NumPy/Pandas)
                questionBank.put("Data Science", Arrays.asList(
                                new QuizQuestion(1, "What is NumPy primarily used for in Python development?",
                                                Arrays.asList("Web servers",
                                                                "High-performance multi-dimensional array calculations",
                                                                "Creating websites", "Authenticating users"),
                                                1),
                                new QuizQuestion(2, "What is the central tabular data structure in the Pandas library?",
                                                Arrays.asList("Series", "DataFrame", "DataGrid", "Spreadsheet"), 1),
                                new QuizQuestion(3,
                                                "Which Pandas method removes rows containing missing or null values?",
                                                Arrays.asList("dropna()", "clean()", "remove_null()", "delete_empty()"),
                                                0),
                                new QuizQuestion(4, "Which NumPy method calculates the arithmetic mean of an array?",
                                                Arrays.asList("mean()", "average()", "sum()", "divide()"), 0),
                                new QuizQuestion(5,
                                                "How do you import the Pandas library using standard Python conventions?",
                                                Arrays.asList("import pandas", "import pandas as pd", "import pd",
                                                                "load pandas"),
                                                1)));

                // 29. Data Visualization
                questionBank.put("Data Visualization", Arrays.asList(
                                new QuizQuestion(1, "What is the core goal of data visualization?", Arrays.asList(
                                                "Saving database spaces",
                                                "Communicating quantitative data patterns clearly and efficiently via charts",
                                                "Creating complex algorithms", "Encrypting user files"), 1),
                                new QuizQuestion(2,
                                                "Which Python plotting library is standard for drawing static graphs?",
                                                Arrays.asList("Matplotlib", "BeautifulSoup", "Flask", "Django"), 0),
                                new QuizQuestion(3,
                                                "Which plot type displays correlation distribution between two variables?",
                                                Arrays.asList("Bar chart", "Scatter plot", "Pie chart", "Line graph"),
                                                1),
                                new QuizQuestion(4,
                                                "Which of these is a popular enterprise dashboarding and BI visualization software?",
                                                Arrays.asList("Docker", "Tableau", "GitLab", "Kubernetes"), 1),
                                new QuizQuestion(5,
                                                "What chart type best showcases proportional segments of a single total?",
                                                Arrays.asList("Line plot", "Pie chart", "Histogram", "Area plot"), 1)));

                // 30. Mobile Design
                questionBank.put("Mobile Design", Arrays.asList(
                                new QuizQuestion(1, "What does UX design stand for in application development?",
                                                Arrays.asList("User Experience design", "User XML structures",
                                                                "Universal execution", "Union interface"),
                                                0),
                                new QuizQuestion(2,
                                                "Which design tool is standard for UI wireframing and clickable mockup designs?",
                                                Arrays.asList("Figma", "Photoshop", "Notepad", "VS Code"), 0),
                                new QuizQuestion(3,
                                                "What design guideline system is established by Google for Android app aesthetics?",
                                                Arrays.asList("Human Interface Guidelines", "Material Design",
                                                                "Metro UI", "Bootstrap CSS"),
                                                1),
                                new QuizQuestion(4,
                                                "Which practice details layouts adapting dynamically to screen scale factors?",
                                                Arrays.asList("Media queries", "Responsive / Adaptive Design",
                                                                "Bootstrap columns", "Flexbox wrap"),
                                                1),
                                new QuizQuestion(5, "What is a mobile wireframe diagram?", Arrays.asList(
                                                "Full-fidelity code",
                                                "Low-fidelity visual layout map showing screen structure and elements",
                                                "A database relationship map", "A server port flowchart"), 1)));
        }

        public List<QuizQuestion> getQuestionsForSkill(String skill) {
                // Try exact match or mapping
                String lookupKey = skill;
                if (skill.equalsIgnoreCase("Bash") || skill.equalsIgnoreCase("Linux")) {
                        lookupKey = "Linux";
                } else if (skill.equalsIgnoreCase("PyTorch") || skill.equalsIgnoreCase("TensorFlow")) {
                        lookupKey = "TensorFlow";
                } else if (skill.equalsIgnoreCase("NumPy") || skill.equalsIgnoreCase("Pandas")
                                || skill.equalsIgnoreCase("Data Science")) {
                        lookupKey = "Data Science";
                } else if (skill.equalsIgnoreCase("PostgreSQL") || skill.equalsIgnoreCase("MySQL")
                                || skill.equalsIgnoreCase("SQL")) {
                        lookupKey = "MySQL";
                }

                for (String key : questionBank.keySet()) {
                        if (key.equalsIgnoreCase(lookupKey)) {
                                return selectQuizQuestions(questionBank.get(key));
                        }
                }
                // General default fallback
                return selectQuizQuestions(questionBank.get("General Software Engineering"));
        }

        public int gradeQuiz(String skill, Map<String, String> answers) {
                int score = 0;
                int index = 0;

                while (true) {
                        String answerKey = "q" + index;
                        String answerVal = answers.get(answerKey);
                        if (answerVal == null) {
                                break;
                        }

                        String idKey = answerKey + "_id";
                        String idVal = answers.get(idKey);
                        if (idVal != null) {
                                try {
                                        int questionId = Integer.parseInt(idVal);
                                        QuizQuestion question = findQuestionById(skill, questionId);
                                        if (question != null) {
                                                int chosenIndex = Integer.parseInt(answerVal);
                                                if (chosenIndex == question.getCorrectOptionIndex()) {
                                                        score++;
                                                }
                                        }
                                } catch (NumberFormatException e) {
                                        // Ignore invalid input
                                }
                        }
                        index++;
                }
                return score;
        }

        private QuizQuestion findQuestionById(String skill, int questionId) {
                List<QuizQuestion> questions = getQuestionsForSkill(skill);
                for (QuizQuestion question : questions) {
                        if (question.getId() == questionId) {
                                return question;
                        }
                }
                return null;
        }

        private List<QuizQuestion> selectQuizQuestions(List<QuizQuestion> questions) {
                if (questions == null || questions.isEmpty()) {
                        return Collections.emptyList();
                }

                if (questions.size() <= QUIZ_QUESTION_COUNT) {
                        return new ArrayList<>(questions);
                }

                List<QuizQuestion> shuffled = new ArrayList<>(questions);
                Collections.shuffle(shuffled);
                return shuffled.subList(0, QUIZ_QUESTION_COUNT);
        }
}
