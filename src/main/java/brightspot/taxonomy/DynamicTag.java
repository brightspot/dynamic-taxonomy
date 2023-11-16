package brightspot.taxonomy;

import brightspot.tag.Tag;
import brightspot.taxon.TaxonParentExtension;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Taxon;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.StringException;
import com.psddev.dari.util.DariUtils;
import com.psddev.dari.util.ObjectUtils;

import static brightspot.taxonomy.Utils.*;

/**
 * Concrete implementation of {@link Tag} for dynamic taxonomies.
 */
@Content.Searchable
@ToolUi.ExcludeFromGlobalSearch
public class DynamicTag extends Record implements Tag, TaxonParentExtension {

    @Required
    private String displayName;

    @Indexed
    @DisplayName("Internal Name")
    @InternalName(DYNAMIC_TAXONOMY_NAME_FIELD)
    @ToolUi.ReadOnly
    private String name;

    @Indexed
    @InternalName(DYNAMIC_TAXONOMY_PARENT_FIELD)
    @Where("_id != ?")
    private DynamicTag parent;

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

    public DynamicTag getParent() {
        return parent;
    }

    public void setParent(DynamicTag parent) {
        this.parent = parent;
    }

    public DynamicTagCategory getCategory() {
        return category;
    }

    public void setCategory(DynamicTagCategory category) {
        this.category = category;
    }

    // Overrides

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
    public DynamicTag getTagParent() {
        return getParent();
    }

    @Override
    public String getLinkableText() {
        return DariUtils.toNormalized(getName());
    }

    @Override
    public TaxonParentExtension getTaxonParent() {
        return getParent();
    }

    @Override
    public Query<? extends Taxon> getTaxonChildrenQuery() {
        return Query.from(DynamicTag.class)
            .where(DYNAMIC_TAXONOMY_PARENT_INDEX + " = ?", this);
    }

    @Override
    protected void beforeCommit() {
        // normalize and set the internal name
        setName(DariUtils.toNormalized(getDisplayName()));
    }

    @Override
    protected void onValidate() {
        // validate that the category matches the parent category
        if (!ObjectUtils.isBlank(getParent()) && (!ObjectUtils.equals(getParent().getCategory(), getCategory()))) {
            getState().addError(
                getState().getField(DYNAMIC_TAXONOMY_CATEGORY_FIELD),
                new StringException("Tag category must match parent category."));
        }
    }
}
