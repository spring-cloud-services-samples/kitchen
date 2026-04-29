# Kitchen

**Kitchen** is a sample Spring Boot web application demonstrating the use of Feature Flags via Config Server for Tanzu
Application Service. (For information on the Config Server product in Tanzu Application Service, please see
the [documentation](https://techdocs.broadcom.com/tnz-app-services).)

## Building the Application

This project supports both Gradle and Maven build systems. It uses Spring Enterprise artifacts from Broadcom's repository, which requires authentication.

### Authentication Setup

Before building with either Gradle or Maven, you must configure your Spring Enterprise repository credentials.

#### 1. Set Environment Variables (Recommended)
Both Gradle and Maven can use these environment variables:

```bash
export SEC_ARTIFACTORY_USERNAME=your_username
export SEC_ARTIFACTORY_PASSWORD=your_password
```

#### 2. Build Tool Specific Configuration

**For Gradle:**
Gradle is automatically configured to use the environment variables above. Alternatively, you can add them to your `~/.gradle/gradle.properties` file:
```properties
secArtifactoryUsername=your_username
secArtifactoryPassword=your_password
```

**For Maven:**
Maven requires you to add the `spring-enterprise` server to your `~/.m2/settings.xml` file. The configuration below will read the environment variables you set in step 1:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>spring-enterprise</id>
            <username>${env.SEC_ARTIFACTORY_USERNAME}</username>
            <password>${env.SEC_ARTIFACTORY_PASSWORD}</password>
        </server>
    </servers>
</settings>
```

### Gradle Build (Default)

```bash
./gradlew build
./gradlew bootRun
```

### Maven Build (Alternative)

```bash
# Using Maven wrapper (recommended)
./mvnw clean compile
./mvnw clean package
./mvnw spring-boot:run

# Or if you have Maven installed globally
mvn clean compile
mvn clean package
mvn spring-boot:run
```

## Deployment

### Create Service Instance

```bash
cf create-service p.config-server standard kitchen-config-server \
  -c '{ "git": { "uri": "https://github.com/pivotal-cf/scs-public-config-repo" }}'
```

### Build and Deploy Application

**Using Gradle:**
```bash
./gradlew build && cf push -p build/libs/kitchen-0.0.1-SNAPSHOT.jar
```

**Using Maven:**
```bash
./mvnw clean package && cf push -p target/kitchen-0.0.1-SNAPSHOT.jar
```

Visit your application route (e.g. `https://kitchen.<your-app-domain>`), and log in by providing a sample email address.

You may use the tester email addresses listed [in the config repo](https://github.com/pivotal-cf/scs-public-config-repo/blob/main/kitchen.flags.yaml#L19-L21)
to see the dishes that are only available for beta testers.

You can also enable `dev` profile and restart the app to verify that `theme` is changing to green.

```bash
cf set-env kitchen SPRING_PROFILES_ACTIVE dev && cf restart kitchen
```