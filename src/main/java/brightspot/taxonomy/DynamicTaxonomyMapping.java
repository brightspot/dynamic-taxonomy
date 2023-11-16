package brightspot.taxonomy;

import java.util.ArrayList;
import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Record;
import com.psddev.dari.util.ObjectUtils;

import static brightspot.taxonomy.Utils.*;

/**
 * Record to hold {@link HasDynamicTags} to {@link DynamicTagCategory} mapping.
 */
@ToolUi.ExcludeFromGlobalSearch
public class DynamicTaxonomyMapping extends Record {

    private String displayName;

    @Indexed(unique = true)
    @Required
    @Where("(groups = '" + HAS_DYNAMIC_TAXONOMY_GROUP + "') AND isConcrete = true AND deprecated != true")
    private ObjectType contentType;

    @Indexed
    @Required
    private List<DynamicTagCategory> taxonomyCategories;

    // accessors and mutators

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ObjectType getContentType() {
        return contentType;
    }

    public void setContentType(ObjectType contentType) {
        this.contentType = contentType;
    }

    public List<DynamicTagCategory> getTaxonomyCategories() {
        if (taxonomyCategories == null) {
            taxonomyCategories = new ArrayList<>();
        }
        return taxonomyCategories;
    }

    public void setTaxonomyCategories(List<DynamicTagCategory> taxonomyCategories) {
        this.taxonomyCategories = taxonomyCategories;
    }

    // Overrides

    @Override
    public String getLabel() {
        return ObjectUtils.firstNonNull(getDisplayName(), getContentType().getLabel());
    }
}
