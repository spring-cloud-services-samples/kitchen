# Kitchen

**Kitchen** is a sample Spring Boot web application demonstrating the use of Feature Flags via Config Server for Tanzu
Application Service. (For information on the Config Server product in Tanzu Application Service, please see
the [documentation](https://techdocs.broadcom.com/tnz-app-services).)

## Deployment

### Create Service Instance

```bash
cf create-service p.config-server standard kitchen-config-server \
  -c '{ "git": { "uri": "https://github.com/pivotal-cf/scs-public-config-repo" }}'
```

### Build and Deploy Application

```bash
./gradlew build && cf push -p build/libs/kitchen-0.0.1-SNAPSHOT.jar
```

Visit your application route (e.g. `https://kitchen.<your-app-domain>`), and log in by providing a sample email address.

You may use the tester email addresses listed [in the config repo](https://github.com/pivotal-cf/scs-public-config-repo/blob/main/kitchen.flags.yaml#L19-L21)
to see the dishes that are only available for beta testers.

You can also enable `dev` profile and restart the app to verify that `theme` is changing to green.

```bash
cf set-env kitchen SPRING_PROFILES_ACTIVE dev && cf restart kitchen
```