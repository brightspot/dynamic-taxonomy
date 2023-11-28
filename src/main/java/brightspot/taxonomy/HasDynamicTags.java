package brightspot.taxonomy;

import java.util.Set;

import com.psddev.dari.db.Recordable;

/**
 * Interface to mark content with dynamic taxonomy content.
 */
public interface HasDynamicTags extends Recordable {

    default Set<DynamicTag> getDynamicTags() {
        return asHasDynamicTagsData().getDynamicTags();
    }

    default HasDynamicTagsData asHasDynamicTagsData() {
        return as(HasDynamicTagsData.class);
    }

}
