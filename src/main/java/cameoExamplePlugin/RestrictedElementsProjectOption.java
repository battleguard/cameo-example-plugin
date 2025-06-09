package cameoExamplePlugin;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.options.ProjectOptions;
import com.nomagic.magicdraw.properties.*;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.magicdraw.ui.dialogs.SelectElementInfo;
import com.nomagic.magicdraw.ui.dialogs.selection.ElementSelectionDlgFactory;
import com.nomagic.magicdraw.ui.dialogs.selection.TypeFilterImpl;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RestrictedElementsProjectOption {
    public final String key;
    private final String description;
    private final String optionGroup;
    private final String subGroup;
    private final Collection<String> stereotypeNames;

    public RestrictedElementsProjectOption(String key, String description, String optionGroup, String subGroup,
                                           Collection<String> stereotypeNames) {
        this.key = key;
        this.description = description;
        this.optionGroup = optionGroup;
        this.subGroup = subGroup;
        this.stereotypeNames = stereotypeNames;
    }

    public List<Element> getValues(Project project) {
        return getOrCreateProperty(project.getOptions()).getValidElements();
    }


    public List<Element> displayValueChooser(Project project) {
        var property = getOrCreateProperty(project.getOptions());
        var typeFilter = new TypeFilterImpl(property.getStereotypes());

        var selectInfo = new SelectElementInfo(false, false, true);
        var dialog = ElementSelectionDlgFactory.create(MDDialogParentProvider.getProvider().getDialogOwner(null), "SELECT_MODEL_ELEMENT_SYMBOL", null);
        ElementSelectionDlgFactory.initMultiple(dialog, selectInfo, typeFilter, typeFilter, null, property.getValidElements());
        dialog.setVisible(true);
        if (dialog.getResult() == 1) {
            var selectedElements = dialog.getSelectedElements();
            property.setValue(selectedElements.toArray(new Element[0]));
            return getValues(project);
        }
        return new ArrayList<>();
    }

    public RestrictedElementListProperty getOrCreateProperty(ProjectOptions projectOptions) {
        Property propertyCache = projectOptions.getProperty(optionGroup, key);
        if (propertyCache instanceof RestrictedElementListProperty) {
            // note: this pathway never happens due to what I believe is serialization of only the cameo lib property
            // types into the project file on save.
            return (RestrictedElementListProperty) propertyCache;
        }
        var value = propertyCache != null ? propertyCache.getValue() : null;
        var property = new RestrictedElementListProperty(key, stereotypeNames, value);
        var selectableTypes = Arrays.asList(com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class.class, Stereotype.class);
        property.setSelectableTypes(selectableTypes);
        property.setDescription(description);
        property.setGroup(subGroup);

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