package brightspot.taxonomy;

public class Utils {

    public static final String DYNAMIC_TAXONOMY_NAME_FIELD = "name";
    public static final String DYNAMIC_TAXONOMY_NAME_INDEX =
        DynamicTag.class.getName() + "/" + DYNAMIC_TAXONOMY_NAME_FIELD;

    public static final String DYNAMIC_TAXONOMY_CATEGORY_FIELD = "category";
    public static final String DYNAMIC_TAXONOMY_CATEGORY_INDEX =
        DynamicTag.class.getName() + "/" + DYNAMIC_TAXONOMY_CATEGORY_FIELD;

    public static final String DYNAMIC_TAXONOMY_CATEGORY_NAME_FIELD = "name";
    public static final String DYNAMIC_TAXONOMY_CATEGORY_NAME_INDEX =
        DynamicTagCategory.class.getName() + "/" + DYNAMIC_TAXONOMY_CATEGORY_NAME_FIELD;

    public static final String HAS_DYNAMIC_TAXONOMY_GROUP = "brightspot.taxonomy.HasDynamicTags";

    public static final String HAS_DYNAMIC_TAGS_FIELD_PREFIX = "hasDynamicTags.";

    public static final String HAS_DYNAMIC_TAGS_FIELD = "dynamicTags";

    public static final String TAXONOMY_TYPE_TAB = "Taxonomy";

    private Utils() {
        // prevent instantiation
    }

}
