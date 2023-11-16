package brightspot.taxonomy;

import java.util.Collections;

import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.page.AbstractSearchPageServlet;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.RoutingFilter;

import static brightspot.taxonomy.DynamicTaxonomyTool.*;

/**
 * Search page for {@link DynamicTagCategory}s and management.
 */
@RoutingFilter.Path(application = APPLICATION, value = DynamicTagCategorySearchPage.PATH)
public class DynamicTagCategorySearchPage extends AbstractSearchPageServlet {

    private static final String DISPLAY_NAME = "Categories";
    private static final String INTERNAL_NAME = "taxonomy/categories";
    private static final String HIERARCHY = "taxonomy/categories";
    private static final String DISPLAY_NAME_PROPERTIES_KEY = "displayName";

    public static final String PATH = "taxonomy-categories";

    @Override
    public Search getSearch() {

        Search search = new Search();
        ObjectType type = ObjectType.getInstance(DynamicTagCategory.class);
        search.setTypes(Collections.singleton(type));
        search.setSelectedType(type);

        return search;
    }

    @Override
    public String getDisplayName() {
        return ToolLocalization.text(
            getClass(),
            DISPLAY_NAME_PROPERTIES_KEY,
            DISPLAY_NAME);
    }

    @Override
    public String getInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public String getHierarchy() {
        return HIERARCHY;
    }

    @Override
    public String getUrl() {
        return RoutingFilter.Static.getApplicationPath(APPLICATION) + "/" + PATH;
    }
}
