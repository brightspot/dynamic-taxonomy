# Dynamic Taxonomy


This extension provides the ability to editorially curate tags and tag categories for content types. Tag categories can then be mapped to content types to enable tagging of content with tags from the mapped categories.

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

This interface enables a taxonomy tab on the `MyContentType` record in the CMS where editors can add dynamic tags to the record.

_Optionally_ Dynamic tags can be customized with respective modification and substitution to support hierarchy as follows:

Step 1: Add a modification to the `DynamicTag` model to add a parent field:

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

Step 2: Add a substitution to the `DynamicTag` model to facilitate hierarchy recalculations

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
## Documentation

- [Video Demo] <**Add link**> 
- [User Guide](https://www.brightspot.com/documentation/brightspot-cms-user-guide/dynamic-taxonomy)
- [Javadocs](https://artifactory.psdops.com/public/com/brightspot/dynamic-taxonomy/%5BRELEASE%5D/dynamic-taxonomy-%5BRELEASE%5D-javadoc.jar!/index.html) 

## Versioning

The version numbers for this extension will strictly follow [Semantic Versioning](https://semver.org/). The latest release can be found here <ADD IN LINK>.

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
