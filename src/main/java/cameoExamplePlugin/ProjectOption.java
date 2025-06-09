package cameoExamplePlugin;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.options.ProjectOptions;
import com.nomagic.magicdraw.pathvariables.RecursivePathVariableException;
import com.nomagic.magicdraw.properties.*;

import java.io.File;

public class ProjectOption<T> {
    public final String key;
    private final T defaultValue;
    private final String description;
    private final String optionGroup;
    private final Boolean editable;
    private final String nonEditableReason;
    private final Boolean multiLine;
    private final String subGroup;

    public ProjectOption(String key, T defaultValue, String description, String optionGroup, String subGroup) {
        this(key, defaultValue, description, optionGroup, subGroup, true, "", false);
    }

    public ProjectOption(String key, T defaultValue, String description, String optionGroup, String subGroup,
                         Boolean editable, String nonEditableReason, Boolean multiLine) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.description = description;
        this.optionGroup = optionGroup;
        this.editable = editable;
        this.nonEditableReason = nonEditableReason;
        this.multiLine = multiLine;
        this.subGroup = subGroup;
    }

    @SuppressWarnings("unchecked")
    public T getValue(Project project) {
        Property property = getOrCreateProperty(project.getOptions());
        if (property instanceof FileProperty) {
            try {
                return (T) ((FileProperty) property).getFile((String) property.getValue());
            } catch (RecursivePathVariableException e) {
                return null;
            }
        }
        return (T) property.getValue();
    }

    public void GuiChooseValue()
    {

    }

    public void setValue(Project project, T value) {
        Property property = getOrCreateProperty(project.getOptions());
        if (property != null) {
            property = property.clone();
            property.setValue(value);
            project.getOptions().addProperty(optionGroup, property);
        }
    }

    public Property getOrCreateProperty(ProjectOptions projectOptions) {
        Property property = projectOptions.getProperty(optionGroup, key);
        if (property != null) {
            return property;
        }

        if (defaultValue instanceof File) {
            property = new FileProperty(key, defaultValue.toString(), FileProperty.DIRECTORIES_ONLY);
        } else if (defaultValue instanceof Boolean) {
            property = new BooleanProperty(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof String) {
            property = new StringProperty(key, (String) defaultValue, this.multiLine);
        } else if (defaultValue instanceof Integer) {
            // todo: switch port to integer once we update this code to handle updating an invalid property type
            // i.e the property has changed types due to a version change string to int.
            property = new NumberProperty(key, (Integer) defaultValue, 0.0, 65535.0);
        } else {
            Application.getInstance().getGUILog().showError("Failed to create " + optionGroup + " " + key + " due to default value: " +
                    defaultValue + " of type: " + defaultValue.getClass() + " is an unsupported type!");
            return null;
        }

        property.setDescription(description);
        property.setGroup(subGroup);
        property.setEditable(editable);
        if (!editable) {
            property.setNonEditableReason(nonEditableReason);
        }

        property.setResourceProvider((s, property1) -> {
            if ((key + "_DESCRIPTION").equals(s)) {
                return description;
            }
            return s;
        });
        projectOptions.addProperty(optionGroup, property);
        return property;
    }
}