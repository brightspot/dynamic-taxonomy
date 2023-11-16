package brightspot.taxonomy;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.dari.db.Recordable;

/**
 * Interface to mark content with dynamic taxonomy content.
 */
public interface HasDynamicTags extends Recordable {

    default Set<DynamicTag> getDynamicTags() {
        return asHasDynamicTagsData().getDynamicTags();
    }

    default Set<DynamicTag> getVisibleTags() {

        Set<DynamicTag> tags = getDynamicTags();
        if (tags == null) {
            return Collections.emptySet();
        }

        return getDynamicTags().stream().filter(tag -> !tag.isHiddenTag()).collect(Collectors.toSet());
    }

    default HasDynamicTagsData asHasDynamicTagsData() {
        return as(HasDynamicTagsData.class);
    }

}
