package brightspot.taxonomy;

import java.util.ArrayList;
import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.Plugin;
import com.psddev.cms.tool.Tool;
import com.psddev.cms.ui.ToolLocalization;

@ToolUi.Hidden
public class DynamicTaxonomyTool extends Tool {

    private static final String INTERNAL_NAME = "taxonomy";
    private static final String HIERARCHY = "taxonomy";
    private static final String TITLE_PROPERTIES_KEY = "title";
    public static final String APPLICATION = "cms";

    @Override
    public List<Plugin> getPlugins() {
        List<Plugin> plugins = new ArrayList<>();
        plugins.add(createArea2(
            ToolLocalization.text(DynamicTaxonomyTool.class, TITLE_PROPERTIES_KEY, "Taxonomy"),
            INTERNAL_NAME, HIERARCHY, null));
        return plugins;
    }

}
