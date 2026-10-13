# Kitchen

**Kitchen** is a sample Spring Boot web application demonstrating the use of Feature Flags via Config Server for Tanzu
Application Service. (For information on the Config Server product in Tanzu Application Service, please see
the [documentation](https://techdocs.broadcom.com/tnz-app-services).)

## Building the Application

This project supports both Gradle and Maven build systems. It uses Spring Enterprise artifacts from Broadcom's repository, which requires authentication.

### Dependencies

Kitchen imports the Tanzu Spring SDK BOM
(`com.vmware.tanzu.spring:tanzu-spring-sdk-dependencies`, version `1.2.0`) in both builds. The BOM manages the version of the
Application Services starters, so the feature flag starter is declared without a version. The BOM defaults to the
Application Services Starters 4.x line, so this branch selects the `2.x` line (`2.0.0`):

- Gradle sets the `tanzu.spring.application-services.version` property, which the dependency management plugin applies to the BOM.
- Maven cannot override properties of an imported BOM, so `pom.xml` pins the Application Services starter to the same
  property. The starter brings in its matching autoconfigure artifacts.

```xml
<!-- Maven -->
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.vmware.tanzu.spring.applicationservices</groupId>
      <artifactId>application-services-starter-feature-flag</artifactId>
      <version>${tanzu.spring.application-services.version}</version>
    </dependency>
    <dependency>
      <groupId>com.vmware.tanzu.spring</groupId>
      <artifactId>tanzu-spring-sdk-dependencies</artifactId>
      <version>1.2.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<properties>
  <tanzu.spring.application-services.version>2.0.0</tanzu.spring.application-services.version>
</properties>
```

```kotlin
// Gradle
extra["tanzu.spring.application-services.version"] = "2.0.0"

dependencyManagement {
    imports {
        mavenBom("com.vmware.tanzu.spring:tanzu-spring-sdk-dependencies:1.2.0")
    }
}
```

To use a different SDK release, change the BOM version in `pom.xml` and `build.gradle.kts`. Both files must stay in sync.

### Authentication Setup

Your build must resolve artifacts from a repository that mirrors or proxies the Spring Enterprise Repository.
See [Spring Enterprise Repository for Artifact Repository Administrators](https://techdocs.broadcom.com/us/en/vmware-tanzu/spring/tanzu-spring/commercial/spring-tanzu/guide-artifact-repository-administrators.html).

#### 1. Set Environment Variables (Recommended)
Both Gradle and Maven can use these environment variables:

```bash
export SEC_ARTIFACTORY_USERNAME=your_username
export SEC_ARTIFACTORY_PASSWORD=your_access_token
```

#### 2. Build Tool Specific Configuration

**For Gradle:**
Gradle is automatically configured to use the environment variables above. Alternatively, you can add them to your `~/.gradle/gradle.properties` file:
```properties
secArtifactoryUsername=your_username
secArtifactoryPassword=your_access_token
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
