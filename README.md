# vendordep-plugin

This Gradle plugin replaces WPILib's vendordep JSON system with a cleaner, code-based DSL for managing robot dependencies using Gradle. Just real, version-controlled Gradle declarations.

## Getting Started
To start, first go to your ``` settings.gradle ``` and under:
```gradle
pluginManagement {
    //...
    repositories {
        //HERE
    }
}
```
add
``` gradle
maven {
    url = uri("https://m00nb0ygame8.github.io/vendordep-plugin/maven/")
}
```

Secondly, in your ``` build.gradle ``` file, put:
```gradle
plugins {
  //...
  id 'vendordep-plugin' version '{LATEST_VERSION_HERE}'
}
```

## Using the Plugin
To use the plugin, simply declare
```gradle
vendorDep {

}
```
and inside, you can use the following:
### mavenRepo
``` mavenRepo("REPO_NAME")``` to declare a maven repository.
### vendorDep
``` vendorDep("VENDOR_GROUP", "VENDOR_ARTIFACT", "VERSION")``` to declare a vendor dependency. For example, ``` vendorDep("com.ctre.phoenix6", "api-java", "6.31.0") ```
### implementation
``` implementation("VENDOR_DEPENDENCY")``` to declare a vendor dependency. For example, ``` implementation("com.ctre.phoenix6:api-java:6.31.0") ```
### nativeDep
``` nativeDep("VENDOR_DEPENDENCY")``` to declare a native dependency. For example, ``` nativeDep("com.ctre.phoenix6:api-cpp:6.31.0:linuxathena@zip") ```
### nativeDep pt. 2
``` nativeDep("VENDOR_GROUP", "VENDOR_ARTIFACT", "VERSION", "TYPE", "IS_JAR(BOOL)")``` to declare a native dependency. For example, ``` nativeDep("com.ctre.phoenix6", "api-cpp", "25.4.0", wpi.platforms.desktop, false) ```

## Deploying vendordeps

Once you've declared all of your dependencies. In our example, we can use the following for Phoenix6
```gradle
vendorDep {
    mavenRepo("https://maven.ctr-electronics.com/release/")
    vendorDep("com.ctre.phoenix6", "wpiapi-java", "25.4.0")
    nativeDep("com.ctre.phoenix6", "api-cpp", "25.4.0", wpi.platforms.desktop, false)
}
```
We now need to cache the vendordeps to be able to use them.

Now, if you run the gradle task called prefetchVendorDeps, all of the dependencies declared in vendorDeps will be applied, so in our example, after running
``` ./gradlew prefetchVendorDeps ```
We see that we can now use the TalonFX class from Phoenix6
