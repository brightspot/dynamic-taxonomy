package brightspot.taxonomy;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UnresolvedState;

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
    public Set<DynamicTag> getTagsAndAncestors() {
        Set<DynamicTag> tagsAndAncestors = new LinkedHashSet<>();

        Set<DynamicTag> tags = UnresolvedState.resolveAndGet(getOriginalObject(), HasDynamicTags::getDynamicTags);

        if (tags == null) {
            return Collections.emptySet();
        }

        Set<DynamicTag> visited = new HashSet<>();
        Queue<DynamicTag> toProcess = new LinkedList<>(tags);

        while (!toProcess.isEmpty()) {
            DynamicTag next = toProcess.remove();
            if (!visited.add(next)) {
                continue;
            }

            tagsAndAncestors.add(next);

            Optional.ofNullable(UnresolvedState.resolveAndGet(next, DynamicTag::getTagParent))
                .ifPresent(toProcess::add);
        }

        return tagsAndAncestors;
    }

    @Indexed
    @ToolUi.Hidden
    public Set<DynamicTag> getVisibleTagsAndAncestors() {

        return getTagsAndAncestors()
            .stream()
            .filter(tag -> !tag.isHiddenTag())
            .collect(Collectors.toSet());
    }
}
