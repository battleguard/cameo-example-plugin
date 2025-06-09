package cameoExamplePlugin;

import com.nomagic.magicdraw.core.options.Group;
import com.nomagic.magicdraw.core.options.ProjectOptions;
import com.nomagic.magicdraw.core.options.ProjectOptionsConfigurator;


import java.io.File;
import java.util.*;

public class CameoExampleProjectOptions implements ProjectOptionsConfigurator {

    public static final String GROUP_NAME = "Example Cameo Plugin";


    public static final ProjectOption<String> DISPLAY_TEXT = new ProjectOption<String>("Display Text", "Hello World",
            "String Example that changes project name on reload", ProjectOptions.PROJECT_GENERAL_PROPERTIES, GROUP_NAME);


    public static final ProjectOption<File> EXAMPLE_DIRECTORY_SELECTOR = new ProjectOption<>("Example Directory Selector Property", new File(""),
            "This is a project option that selects a file.", ProjectOptions.PROJECT_GENERAL_PROPERTIES, GROUP_NAME);


    public static final ProjectOption<Boolean> EXAMPLE_BOOLEAN_PROPERTY = new ProjectOption<>("Example Boolean Property", false,
            "This is a project option of a boolean",
            ProjectOptions.PROJECT_GENERAL_PROPERTIES, GROUP_NAME);

    public static final RestrictedElementsProjectOption BLOCK_SELECTOR_PROPERTY = new RestrictedElementsProjectOption(
            "Block Selector Property",
            "This is an example of how to select items and restrict which items are available for selection.",
            ProjectOptions.PROJECT_GENERAL_PROPERTIES, GROUP_NAME,
            Arrays.asList("Block",  "Block")
    );

    public static final Collection<String> GROUP_NAMES = List.of(GROUP_NAME);

    public CameoExampleProjectOptions() {
        ProjectOptions.remapGroupInUI(GROUP_NAMES, new Group("Example Cameo Plugin", "Example Cameo Plugin", "Example Cameo Plugin"));
    }

    @Override
    public void configure(ProjectOptions projectOptions) {
        createProperties(projectOptions);
    }

    @Override
    public void afterLoad(ProjectOptions projectOptions) {
        createProperties(projectOptions);
    }

    public void createProperties(ProjectOptions projectOptions) {
        // this needs to be called twice (configure, afterLoad) due to very weird caching issues where the project was
        // saved with an old version of the plugin and the property class type is different.
        DISPLAY_TEXT.getOrCreateProperty(projectOptions);
        EXAMPLE_BOOLEAN_PROPERTY.getOrCreateProperty(projectOptions);
        EXAMPLE_DIRECTORY_SELECTOR.getOrCreateProperty(projectOptions);
        BLOCK_SELECTOR_PROPERTY.getOrCreateProperty(projectOptions);
    }
}
