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

For models that need to support dynamic taxonomy, implement the `HasDynamicTags` interface on your content type:

```java
public class MyContentType extends Content implements HasDynamicTags {
    // ...
}
```

This will enable a taxonomy tab on `MyContentType` record in the CMS where you can add dynamic tags to the record.

### Publish Dynamic tag categories

Dynamic tags are organized into categories. Each category has a name and a boolean flag to indicate whether this 
category supports multiple tags.

To publish dynamic tag categories:
- Click the hamburger menu (☰) on top left corner of the CMS
- Navigate to `Taxonomy` > `Categories`
- Click `New Dynamic Tag Category` button in the bottom left corner.
- Enter a name for the category and select whether this category supports multiple tags.
- Click `Save`.

### Publish Dynamic taxonomy mappings

Dynamic taxonomy mappings are used to map dynamic tag categories to a specific content type that supports dynamic tags 
i.e. models that implement the `HasDynamicTags` interface. 

To publish dynamic taxonomy mappings:
- Click the hamburger menu (☰) on top left corner of the CMS
- Navigate to `Taxonomy` > `Content`.
- Click `New Dynamic Taxonomy Mapping` button in the bottom left corner.
- Select a supported content type from the dropdown menu.
- Add a list of dynamic tag categories that you want to map to the selected content type.
- Enter the optional `Display Name`.
- Click `Save`.

### Publish Dynamic tags

Dynamic tags are actual taxon records that can be used to tag content. Dynamic tags are organized into categories.

To publish dynamic tags:
- Click the hamburger menu (☰) on top left corner of the CMS
- Navigate to `Taxonomy` > `Tags`
- Click `New Dynamic Tag` button in the bottom left corner.
- Add the required display name.
- Select a parent tag if applicable.
- Select a category from the dropdown menu. (Note category must match the parent tag's category if parent tag is selected).
- Click `Save`.

### Tagging content with dynamic tags

To tag content with dynamic tags:
- Navigate to the contents edit form.
- Click the `Taxonomy` tab.
- The taxonomy tab will display a list of dynamic tag categories that are mapped to the content type.
- Add tags as needed.
- Publish or save content as usual.

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
