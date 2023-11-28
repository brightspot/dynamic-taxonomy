package brightspot.taxonomy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

import static brightspot.taxonomy.Utils.*;

/**
 * Data {@link Modification} for {@link HasDynamicTags}.
 */
@Recordable.FieldInternalNamePrefix(HAS_DYNAMIC_TAGS_FIELD_PREFIX)
public class HasDynamicTagsData extends Modification<HasDynamicTags> {

    @InternalName(HAS_DYNAMIC_TAGS_FIELD)
    @ToolUi.Hidden
    private Set<DynamicTag> dynamicTags;

    public Set<DynamicTag> getDynamicTags() {
        if (dynamicTags == null) {
            dynamicTags = new HashSet<>();
        }
        return dynamicTags;
    }

    public void setDynamicTags(Set<DynamicTag> dynamicTags) {
        this.dynamicTags = dynamicTags;
    }

    @Indexed
    @ToolUi.Hidden
    @ToolUi.Filterable(displayByDefault = true)
    @DisplayName("Tags")
    public Set<DynamicTag> getTags() {
        return Optional.ofNullable(getOriginalObject())
            .map(HasDynamicTags::getDynamicTags).orElse(Collections.emptySet());
    }
}
