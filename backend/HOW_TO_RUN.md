# HOW TO RUN THE PROJECT

## Quick Reference Guide

---

## 🚀 **3 WAYS TO RUN THE PROJECT**

### **METHOD 1: Run JAR File (Production - FASTEST)**
```bash
cd d:\Vs Code\projects\student-management-system\backend
java -jar target/backend-0.0.1-SNAPSHOT.jar
```
✅ **Best for:** Production deployment  
⏱️ **Time:** Instant (pre-compiled)  
📊 **Memory:** Optimal

---

### **METHOD 2: Run with Maven (Development - RECOMMENDED)**
```bash
cd d:\Vs Code\projects\student-management-system\backend
.\mvnw.cmd spring-boot:run
```
✅ **Best for:** Development & debugging  
⏱️ **Time:** ~5-10 seconds (compiles then runs)  
📊 **Memory:** Slightly higher (compilation overhead)

---

### **METHOD 3: Run from VS Code (IDE Integration)**
1. Open folder: `d:\Vs Code\projects\student-management-system\backend`
2. Press `Ctrl + Shift + P`
3. Type: `Maven: Run Maven Build`
4. Select: `spring-boot:run`

✅ **Best for:** IDE debugging with breakpoints  
📊 **Features:** Debug console, integrated debugging

---

## ✅ **PREREQUISITES CHECKLIST**

Before running, verify:

- [ ] **Java 21** installed
  ```bash
  java -version
  # Output should show: openjdk version "21.0.8"
  ```

- [ ] **JAVA_HOME** set
  ```bash
  set JAVA_HOME=C:\Users\gyana\.jdk\jdk-21.0.8
  ```

- [ ] **Oracle Database** running
  ```bash
  sqlplus student/student123@XEPDB1
  # Should connect successfully
  ```

- [ ] **Port 8081** available
  ```bash
  netstat -ano | findstr :8081
  # Should show NO output
  ```

- [ ] **Maven wrapper** present
  ```bash
  # Files should exist:
  # - mvnw
  # - mvnw.cmd
  ```

---

## 📋 **STEP-BY-STEP GUIDE: Run with JAR (Simplest)**

### Step 1: Navigate to Project
```bash
cd d:\Vs Code\projects\student-management-system\backend
```

### Step 2: Set Java Environment (Optional)
```bash
set JAVA_HOME=C:\Users\gyana\.jdk\jdk-21.0.8
```

### Step 3: Verify JAR File Exists
```bash
dir target\backend-0.0.1-SNAPSHOT.jar
```
Expected: File exists, size ~60 MB

### Step 4: Run the Application
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Step 5: Wait for Startup (3-4 seconds)
Look for this log:
```
Tomcat started on port(s): 8081 (http) with context path ''
```

### Step 6: Application is Ready! ✅
Access at: `http://localhost:8081`

---

## 📋 **STEP-BY-STEP GUIDE: Run with Maven (Recommended)**

### Step 1: Navigate to Project
```bash
cd d:\Vs Code\projects\student-management-system\backend
```

### Step 2: Set Java Environment (Optional)
```bash
set JAVA_HOME=C:\Users\gyana\.jdk\jdk-21.0.8
```

### Step 3: Clean Build (First Time)
```bash
.\mvnw.cmd clean package -DskipTests
```
This ensures everything is compiled fresh.

### Step 4: Run the Application
```bash
.\mvnw.cmd spring-boot:run
```

### Step 5: Wait for Startup (5-10 seconds)
Look for these logs:
```
[INFO] Building backend 0.0.1-SNAPSHOT
[INFO] Tomcat initialized with port 8081
[INFO] Started BackendApplication in X.XXX seconds
```

### Step 6: Application is Ready! ✅
Access at: `http://localhost:8081`

---

## 🌐 **ACCESSING THE APPLICATION**

Once running, access the application via:

| Type | URL |
|------|-----|
| **Base URL** | http://localhost:8081 |
| **API Base** | http://localhost:8081/api |
| **Health Check** | http://localhost:8081/actuator/health |

### Test with curl:
```bash
curl http://localhost:8081
```

### Test with browser:
```
http://localhost:8081
```

---

## 📊 **EXPECTED STARTUP OUTPUT**

When the application starts, you should see:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.0.2)

2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] com.student.backend.BackendApplication : Starting BackendApplication using Java 21.0.8
2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] com.student.backend.BackendApplication : No active profile set, falling back to 1 default profile: "default"
2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] o.hibernate.orm.jpa : HHH008540: Processing PersistenceUnitInfo [name: default]
2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] com.zaxxer.hikari.HikariDataSource : HikariPool-1 - Starting...
2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] com.zaxxer.hikari.HikariDataSource : HikariPool-1 - Start completed.
2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] o.apache.catalina.core.StandardEngine : Starting Servlet engine: Apache Tomcat/11.0.15
2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] o.apache.catalina.core.StandardService : Starting service [Tomcat]
2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] o.springframework.boot.web.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8081 (http) with context path ''
2026-02-05T08:XX:XX.XXX+05:30  INFO XXXX --- [main] com.student.backend.BackendApplication : Started BackendApplication in X.XXX seconds

✅ APPLICATION READY!
```

---

## ⚠️ **TROUBLESHOOTING**

### Problem: Port 8081 already in use
```bash
# Find process using port 8081
netstat -ano | findstr :8081

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F

# OR change port in application.properties
# Change: server.port=8081
# To:     server.port=8082
```

### Problem: Cannot connect to Oracle Database
```bash
# Verify Oracle is running
sqlplus student/student123@XEPDB1

# Check application.properties for correct credentials:
# - URL: jdbc:oracle:thin:@localhost:1521/XEPDB1
# - Username: student
# - Password: student123
```

### Problem: Java 21 not found
```bash
# Verify Java installation
java -version

# Set JAVA_HOME
set JAVA_HOME=C:\Users\gyana\.jdk\jdk-21.0.8

# Add to PATH
set PATH=C:\Users\gyana\.jdk\jdk-21.0.8\bin;%PATH%
```

### Problem: Maven not found
```bash
# Use Maven wrapper instead
.\mvnw.cmd spring-boot:run

# OR install Maven globally
# Download from: https://maven.apache.org/download.cgi
```

### Problem: Build fails with compilation errors
```bash
# Clean and rebuild
.\mvnw.cmd clean compile

# Check Java version compatibility
javac -version

# Should match Java 21 configured in pom.xml
```

---

## 🛑 **STOPPING THE APPLICATION**

### Method 1: Graceful Shutdown (Recommended)
```bash
# In the terminal where application is running:
Press: Ctrl + C

# The application will shutdown gracefully
```

### Method 2: Kill Process (Windows)
```bash
# Find the Java process
tasklist | findstr java

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

---

## 📝 **QUICK COMMAND REFERENCE**

```bash
# Navigate to project
cd d:\Vs Code\projects\student-management-system\backend

# Set Java environment
set JAVA_HOME=C:\Users\gyana\.jdk\jdk-21.0.8

# Clean and build
.\mvnw.cmd clean package

# Run tests
.\mvnw.cmd test

# Run with Maven
.\mvnw.cmd spring-boot:run

# Run JAR file
java -jar target/backend-0.0.1-SNAPSHOT.jar

# Check if port is in use
netstat -ano | findstr :8081

# Test application
curl http://localhost:8081

# View help
.\mvnw.cmd --help
```

---

## 🎯 **PROJECT CONFIGURATION**

### Database Settings
- **URL:** `jdbc:oracle:thin:@localhost:1521/XEPDB1`
- **Username:** `student`
- **Password:** `student123`
- **Pool Size:** 10 connections (HikariCP)

### Server Settings
- **Port:** `8081`
- **Server:** Apache Tomcat 11.0.15
- **Context Path:** `/`
- **Shutdown:** Graceful

### Java Settings
- **Version:** 21 LTS
- **Encoding:** UTF-8
- **Release:** 21

---

## 🚀 **NEXT STEPS AFTER RUNNING**

Once the application is running successfully:

1. **Create REST Endpoints**
   ```java
   @RestController
   @RequestMapping("/api")
   public class StudentController {
       // Add endpoints
   }
   ```

2. **Create JPA Entities**
   ```java
   @Entity
   public class Student {
       // Add properties
   }
   ```

3. **Create Repositories**
   ```java
   public interface StudentRepository extends JpaRepository<Student, Long> {
       // Add custom queries
   }
   ```

4. **Add Business Logic**
   ```java
   @Service
   public class StudentService {
       // Add business methods
   }
   ```

5. **Write Tests**
   ```java
   @SpringBootTest
   public class StudentControllerTest {
       // Add tests
   }
   ```

---

## 📚 **USEFUL RESOURCES**

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Oracle JDBC Documentation](https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/index.html)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## ✅ **SUMMARY**

| Method | Command | Best For |
|--------|---------|----------|
| JAR | `java -jar target/backend-0.0.1-SNAPSHOT.jar` | Production |
| Maven | `.\mvnw.cmd spring-boot:run` | Development |
| VS Code | Ctrl+Shift+P → Maven: Run Maven Build | Debugging |

**Choose the method that best fits your workflow!**

---

**Generated:** 2026-02-05  
**Project:** Student Management System Backend  
**Status:** ✅ Ready to Run
