# Dynamic Taxonomy


This extension provides the ability to editorially curate tags and tag categories for content types. 
Tag categories can then be mapped to content types to enable tagging of content with tags from the mapped categories.

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
- Java: 8 or higher

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

_Optionally_ Dynamic tags can be customized with respective modification and substitution to support hierarchy as follows:

Add a modification to the `DynamicTag` model to add a parent field:

```java
package brightspot.tag;

import brightspot.taxonomy.DynamicTag;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.StringException;
import com.psddev.dari.util.ObjectUtils;

@Recordable.FieldInternalNamePrefix(DynamicTagModification.FIELD_PREFIX)
public class DynamicTagModification extends Modification<DynamicTag> {

    public static final String FIELD_PREFIX = "dt.";

    @Indexed
    @Where("_id != ?") // exclude self to prevent cyclic references
    private DynamicTag parent;

    public DynamicTag getParent() {
        return parent;
    }

    public void setParent(DynamicTag parent) {
        this.parent = parent;
    }

    @Override
    protected void onValidate() {
        // validate that the category matches the parent category
        if (!ObjectUtils.isBlank(getParent()) && (!ObjectUtils.equals(getParent().getCategory(), getOriginalObject().getCategory()))) {
            getState().addError(
                getState().getField("dt.parent"),
                new StringException("Tag category must match parent category."));
        }
    }
}
```

Then add a substitution to the `DynamicTag` model to facilitate hierarchy recalculations

```java
package brightspot.tag;

import brightspot.taxon.TaxonParentExtension;
import brightspot.taxonomy.DynamicTag;
import com.psddev.cms.db.Taxon;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.DariUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;

public class DynamicTagSubstitution extends DynamicTag implements Substitution, Tag, TaxonParentExtension {

    @Override
    public String getTagDisplayNameRichText() {
        return ObjectUtils.firstNonBlank(getDisplayName(), getName());
    }

    @Override
    public String getTagDisplayNamePlainText() {
        return ObjectUtils.firstNonBlank(getDisplayName(), getName());
    }

    @Override
    public boolean isHiddenTag() {
        // all tags are visible by default
        return false;
    }

    @Override
    public Tag getTagParent() {
        return (Tag) as(DynamicTagModification.class).getParent();
    }

    @Override
    public String getLinkableText() {
        return DariUtils.toNormalized(getName());
    }

    @Override
    public TaxonParentExtension getTaxonParent() {
        return (TaxonParentExtension) as(DynamicTagModification.class).getParent();
    }

    @Override
    public Query<? extends Taxon> getTaxonChildrenQuery() {
        return Query.from(Taxon.class)
            .where("brightspot.tag.DynamicTagModification/dt.parent = ?", this);
    }
}
```

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
- Select a category from the dropdown menu.
- Click `Save`.

### Tagging content with dynamic tags

To tag content with dynamic tags:
- Navigate to the contents edit form.
- Click the `Taxonomy` tab.
- The taxonomy tab will display a list of dynamic tag categories that are mapped to the content type.
- Add tags as needed.
- Publish or save content as usual.

## Documentation

The latest Javadocs can be found [here](https://artifactory.psdops.com/public/com/brightspot/dynamic-taxonomy/%5BRELEASE%5D/dynamic-taxonomy-%5BRELEASE%5D-javadoc.jar!/index.html).

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
