package brightspot.taxonomy;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.util.DariUtils;

import static brightspot.taxonomy.Utils.*;

/**
 * Dynamic tag record for dynamic taxonomies.
 */
@Content.Searchable
@ToolUi.ExcludeFromGlobalSearch
public class DynamicTag extends Record {

    @Required
    private String displayName;

    @Indexed
    @DisplayName("Internal Name")
    @InternalName(DYNAMIC_TAXONOMY_NAME_FIELD)
    @ToolUi.ReadOnly
    private String name;

    @Indexed
    @Required
    @InternalName(DYNAMIC_TAXONOMY_CATEGORY_FIELD)
    private DynamicTagCategory category;

    // accessors and mutators

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DynamicTagCategory getCategory() {
        return category;
    }

    public void setCategory(DynamicTagCategory category) {
        this.category = category;
    }

    // Overrides

    @Override
    protected void beforeCommit() {
        // normalize and set the internal name
        setName(DariUtils.toNormalized(getDisplayName()));
    }
}
