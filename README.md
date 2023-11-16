# Dynamic Taxonomy


This extension provides the ability for Brightspot to print the words "Hello World" to the log when certain records are saved.

* [Prerequisites](#prerequisites)
* [Installation](#installation)
* [Usage](#usage)
* [Documentation](#documentation)
* [Versioning](#versioning)
* [Contributing](#contributing)
* [Local Development](#local-development)
* [License](#license)

## Prerequisites

This extension requires an instance of [Brightspot](https://www.brightspot.com/) and access to the project source code.

The instance of Brightspot should have the following versions:
- Brightspot: 4.5.15.8 or higher
- Brightspot GO: 1.4.3 or higher
- Java: 11 or higher

## Installation

Gradle:
```groovy
api 'com.brightspot:dynamic-taxonomy:1.0.0'
```

Maven:
```xml
<dependency>
    <groupId>com.brightspot</groupId>
    <artifactId>dynamic-taxonomy</artifactId>
    <version>1.0.0</version>
</dependency>
```

Substitute `1.0.0` for the desired version found on the [releases](/releases) list.

## Usage

> [!WARNING]
> This section describes how a developer would use this extension in their project.
> It should include code samples and/or screenshots and descriptions of any relevant configuration screens.

To opt in to this behavior, implement the `SaysHelloWorld` interface on your content type:

```java
public class MyContentType extends Content implements SaysHelloWorld {
    // ...
}
```

Now, when a `MyContentType` record is saved, the words "Hello World" will be printed to the log.

## Documentation

The latest Javadocs can be found [here](https://artifactory.psdops.com/public/com/brightspot/platform-extension-example/%5BRELEASE%5D/platform-extension-example-%5BRELEASE%5D-javadoc.jar!/index.html).

## Versioning

The version numbers for this extension will strictly follow [Semantic Versioning](https://semver.org/).

## Contributing
Pull requests are welcome. For major changes, please open an issue first to
discuss what you would like to change.

## Local Development

Assuming you already have a local Brightspot instance up and running, you can 
test this extension by running the following command from this project's root 
directory to install a `SNAPSHOT` to your local Maven repository:

```shell
./gradlew publishToMavenLocal
```

Next, ensure your project's `build.gradle` file contains 

```groovy
repositories {
    mavenLocal()
}
```

Then, add the following to your project's `build.gradle` file:

```groovy
dependencies {
    api 'com.brightspot:dynamic-taxonomy:1.0.0-SNAPSHOT'
}
```

Finally, compile your project and run your local Brightspot instance.

## License

See: [LICENSE](LICENSE).
