package brightspot.taxonomy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.Tab;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.ui.form.FormRequest;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.DariUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.UuidUtils;
import com.psddev.dari.web.WebRequest;
import com.psddev.dari.web.WriterWebResponseBody;

import static brightspot.taxonomy.Utils.*;

/**
 * Tab UI to display {@link DynamicTag} in the categorized {@link DynamicTagCategory} for the corresponding
 * {@link HasDynamicTags} object.
 */
public class DynamicTaxonomyTab implements Tab {

    @Override
    public String getDisplayName() {
        return TAXONOMY_TYPE_TAB;
    }

    @Override
    public boolean shouldDisplay(ToolPageContext page, Object content) {
        // only display if content is an instance of HasDynamicTags
        return content instanceof HasDynamicTags;
    }

    @Override
    public void writeHtml(ToolPageContext page, Object content) {
        if (!WebRequest.isAvailable()) {
            return;
        }

        for (ObjectField pseudoObjectField : getPseudoObjectFields(content, true)) {
            WebRequest.getCurrent().as(FormRequest.class).writeInput(
                (Recordable) content,
                pseudoObjectField,
                new WriterWebResponseBody(page));
        }
    }

    @Override
    public void onUpdate(ToolPageContext page, Object content) {
        if (!WebRequest.isAvailable()) {
            return;
        }

        State objectState = State.getInstance(content);

        if (objectState != null) {
            UUID contentId = objectState.getId();
            Set<DynamicTag> dynamicTags = new HashSet<>();

            for (ObjectField pseudoObjectField : getPseudoObjectFields(content, false)) {
                String pseudoObjectFieldInternalName = pseudoObjectField.getInternalName();
                String pseudoObjectFieldInternalNameWithContentIdPrefix =
                    contentId + "/" + pseudoObjectFieldInternalName;

                dynamicTags.addAll(Optional.ofNullable(WebRequest.getCurrent().getParameters(
                        String.class,
                        pseudoObjectFieldInternalNameWithContentIdPrefix))
                    .map(List::stream)
                    .orElse(Stream.empty())
                    .filter(uuid -> !ObjectUtils.isBlank(uuid))
                    .map(productTagId -> ObjectType.getInstance(DynamicTag.class)
                        .createObject(UuidUtils.fromString(productTagId)))
                    .map(DynamicTag.class::cast)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

                objectState.remove(pseudoObjectFieldInternalName);
            }

            objectState.as(HasDynamicTagsData.class).setDynamicTags(dynamicTags);
        }
    }

    /**
     * Helper to generate a list {@link ObjectField}s based on the {@link DynamicTaxonomyMapping} for the corresponding
     * {@link HasDynamicTags} object.
     *
     * @param content - {@link HasDynamicTags} object.
     * @param shouldUpdateState - flag to indicate is content state should be updated.
     * @return - list of dynamic tag {@link ObjectField}s.
     */
    private List<ObjectField> getPseudoObjectFields(Object content, boolean shouldUpdateState) {

        ObjectType contentObjectType = ObjectType.getInstance(content.getClass());

        // Fetch existing taxonomy mapping for the content object type
        DynamicTaxonomyMapping taxonomyContent = Query.from(DynamicTaxonomyMapping.class)
            .where("contentType = ?", contentObjectType)
            .first();

        // return empty list if objectType is not instantiable to HasDynamicTags or doesn't have a taxonomy mapping published.
        if (!contentObjectType.isInstantiableTo(HasDynamicTags.class) || taxonomyContent == null) {
            return Collections.emptyList();
        }

        // Fetch the taxonomy tags field from the content object type
        ObjectType hasDynamicTagObjectType = ObjectType.getInstance(HasDynamicTags.class);

        // reset the taxonomy tags field values to empty set
        ObjectField taxonomyTagsField = hasDynamicTagObjectType.getField(
            HAS_DYNAMIC_TAGS_FIELD_PREFIX + HAS_DYNAMIC_TAGS_FIELD);
        taxonomyTagsField.setValues(new HashSet<>());

        HasDynamicTagsData hasDynamicTagsData = ((HasDynamicTags) content).asHasDynamicTagsData();
        List<DynamicTagCategory> taxonomyCategories = new ArrayList<>(taxonomyContent.getTaxonomyCategories());
        List<ObjectField> pseudoObjectFields = new ArrayList<>();

        // add a pseudo object field for each taxonomy category
        for (DynamicTagCategory taxonomyCategory : taxonomyCategories) {
            UUID taxonomyCategoryId = taxonomyCategory.getId();

            State hasTaxonomyContentDataState = hasDynamicTagsData.getState();
            String cmsDisplayName = taxonomyCategory.getName();
            String internalName = HAS_DYNAMIC_TAGS_FIELD_PREFIX
                + "." + DariUtils.toCamelCase(cmsDisplayName);

            ObjectField pseudoField = new ObjectField(taxonomyTagsField);

            if (shouldUpdateState) {
                Stream<DynamicTag> dynamicTagStream = hasDynamicTagsData.getDynamicTags()
                    .stream()
                    .filter(dynamicTag -> dynamicTag.getCategory() != null)
                    .filter(dynamicTag -> dynamicTag.getCategory()
                        .getId()
                        .equals(taxonomyCategoryId));

                Object categorySpecificTaxonomyTags = taxonomyCategory.isMultiValue()
                    ? dynamicTagStream.collect(Collectors.toSet())
                    : dynamicTagStream
                        .findFirst()
                        .orElse(null);

                hasTaxonomyContentDataState.put(internalName, categorySpecificTaxonomyTags);
            }

            if (!taxonomyCategory.isMultiValue()) {
                pseudoField.setInternalType(ObjectField.RECORD_TYPE);
            }

            pseudoField.as(ToolUi.class).setTab(TAXONOMY_TYPE_TAB);
            pseudoField.setInternalName(internalName);
            pseudoField.setDisplayName(cmsDisplayName);
            pseudoField.setPredicate(DYNAMIC_TAXONOMY_CATEGORY_FIELD + " = " + taxonomyCategoryId);

            pseudoObjectFields.add(pseudoField);
        }
        return pseudoObjectFields;
    }
}
