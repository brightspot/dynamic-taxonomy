package brightspot.taxonomy;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.Note;
import com.psddev.dari.db.Record;

import static brightspot.taxonomy.Utils.*;

/**
 * Grouping for all {@link DynamicTag} types.
 */
@Content.Searchable
@ToolUi.ExcludeFromGlobalSearch
public class DynamicTagCategory extends Record {

    @Indexed
    @Required
    @InternalName(DYNAMIC_TAXONOMY_CATEGORY_NAME_FIELD)
    private String name;

    @Note("Support multiple tags in this category?")
    private boolean multiValue;

    public String getName() {
        return name;
    }

    // accessors and mutators

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMultiValue() {
        return multiValue;
    }

    public void setMultiValue(boolean multiValue) {
        this.multiValue = multiValue;
    }

    // Overrides

    @Override
    public String getLabel() {
        return getName();
    }
}
