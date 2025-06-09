package cameoExamplePlugin;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Classifier;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class CameoExamplePluginModel {

    // Constants
    private final Project project;

    public CameoExamplePluginModel(Project project)
    {
        this.project = project;
    }

    public Project getProject()
    {
        return this.project;
    }


    public Optional<File> getExampleFile()
    {
        var result = CameoExampleProjectOptions.EXAMPLE_DIRECTORY_SELECTOR.getValue(project);
        return result.isFile() ? Optional.of(result) : Optional.empty();
    }

    public boolean IsExampleBooleanEnabled()
    {
        return CameoExampleProjectOptions.EXAMPLE_BOOLEAN_PROPERTY.getValue(project);
    }

    public String getDisplayText() {
        return CameoExampleProjectOptions.DISPLAY_TEXT.getValue(project);
    }

    public List<Element> getSelectedProjectOptionBlocks()
    {
        return CameoExampleProjectOptions.BLOCK_SELECTOR_PROPERTY.getValues(project);
    }

    public List<Element> displaySelectedProjectOptionBlocksValueChooser()
    {
        return CameoExampleProjectOptions.BLOCK_SELECTOR_PROPERTY.displayValueChooser(project);
    }

    public Optional<Classifier> chooseCameoBlock()
    {
        return CameoBlockSelector.displayValueChooser();
    }

}
