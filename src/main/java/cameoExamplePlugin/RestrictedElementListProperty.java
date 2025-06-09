package cameoExamplePlugin;

import com.nomagic.magicdraw.properties.ElementListProperty;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Classifier;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created to enable restricting a Property Element Chooser to a specific set of stereotypes for use in project options.
 * The default ElementListProperty requires Stereotype instances to be passed in but the project options are created before
 * all stereotypes are loaded in the MagicDrawProfile.
 * This implementation lazy loads the Stereotype instances on demand using a list of stereotype names.
 */
@SuppressWarnings("deprecation")
public class RestrictedElementListProperty extends ElementListProperty {
    protected final Collection<String> stereotypeNames;
    protected List<Stereotype> stereotypesCache;
    protected List<Classifier> stereotypesClassifiersCache;
    protected Boolean isCacheLoaded = false;


    public RestrictedElementListProperty(String s, Collection<String> stereotypeNames, Object value) {
        super(s, value);
        assert !stereotypeNames.isEmpty();
        this.stereotypeNames = stereotypeNames;
    }

    @Override
    public void setSelectableRestrictedElements(Collection collection) {
        throw new UnsupportedOperationException("This is auto setup by constructor do not use this method");
    }

    public void loadStereotypesCache()
    {
        var project = getProjectFromSourcesOrActive();
        stereotypesCache = stereotypeNames.stream()
                .map(name -> StereotypesHelper.getStereotype(project, name)).collect(Collectors.toList());
        stereotypesClassifiersCache = stereotypesCache.stream().map(s -> (Classifier)s).collect(Collectors.toList());
        for(var stereotype : stereotypesCache)
            assert stereotype != null;
        super.setSelectableRestrictedElements(stereotypesCache);
        isCacheLoaded  = true;
    }

    public List<Stereotype> getStereotypes() {
        if(!isCacheLoaded) loadStereotypesCache();
        return stereotypesCache;
    }

    public boolean isValidElement(Element element) {
        if(!isCacheLoaded) loadStereotypesCache();
        return StereotypesHelper.isTypeOf(element, stereotypesClassifiersCache, true);
    }

    public List<Element> getValidElements()
    {
        return Arrays.stream(this.getElements()).filter(this::isValidElement).collect(Collectors.toList());
    }

    @Override
    public RestrictedElementListProperty clone() {
        var clone = (RestrictedElementListProperty)super.clone();
        if (isCacheLoaded) {
            clone.loadStereotypesCache();
        }
        return clone;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Collection getSelectableRestrictedElements() {
        return getStereotypes();
    }
}


