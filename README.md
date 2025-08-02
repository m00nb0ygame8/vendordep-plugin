# vendordep-plugin
This was made to combat WPILib's vendordep jsons.

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
``` nativeDep("VENDOR_DEPENDENCY")``` to declare a native dependency. For example, ``` nativeDep("com.ctre.phoenix6:api-cpp:6.31.0:linuxx86-64") ```
