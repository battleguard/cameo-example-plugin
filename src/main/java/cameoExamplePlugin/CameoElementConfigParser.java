package cameoExamplePlugin;


import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.*;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;

import java.util.*;
import java.util.stream.Collectors;


public class CameoElementConfigParser {

    public static CameoDiagramDataConfig CreateCameoDiagramData(Classifier rootElement) {
        CameoElementConfig rootConfig = CameoElementConfigParser.CreateConfig(rootElement);
        Map<String, CameoElementConfig> configMap = new LinkedHashMap<>();
        CameoElementConfigParser.ParseConfigTree(rootConfig, configMap);
        CameoDiagramDataConfig elementsConfig = new CameoDiagramDataConfig();
        elementsConfig.Elements = new ArrayList<>(configMap.values());
        return elementsConfig;
    }

    public static void ParseConfigTree(CameoElementConfig object, Map<String, CameoElementConfig> configMap) {
        if (configMap.containsKey(object.Name))
            return;
        configMap.put(object.Name, object);

        if (object.ParentName != null && !configMap.containsKey(object.ParentName)) {
            ParseConfigTree(CreateConfig(object.DebugParentClassifier), configMap);
        }

        if (object.DirectLinkNames == null)
            return;

        for (int i = 0; i < object.DirectLinkNames.size(); i++) {
            if (!configMap.containsKey(object.DirectLinkNames.get(i))) {
                ParseConfigTree(CreateConfig(object.DebugDirectLinkClassifiers.get(i)), configMap);
            }
        }
    }

    public static CameoElementConfig CreateConfig(Classifier e) {
        CameoElementConfig config = new CameoElementConfig();
        config.Name = e.getName();
        config.Id = e.getID();
        config.DebugThisClassifier = e;

        // add parent
        Optional<Classifier> parent = e.getGeneral().stream().findFirst();
        if (parent.isPresent()) {
            config.DebugParentClassifier = parent.get();
            config.ParentName = parent.get().getName();
        }

        // Get redefined properties such as side = red, end_time = 7200
        List<Property> valueProperties = e.getAttribute().stream().filter(a -> a.getType() instanceof DataType).collect(Collectors.toList());
        config.Properties = new HashMap<>();
        for (Property attribute : valueProperties) {
            String name = attribute.getName();
            final ValueSpecification defaultValue = attribute.getDefaultValue();
            if (defaultValue != null) {
                String value = getParsedValue(defaultValue);
                config.Properties.put(name, value);
            }
        }
        if (config.Properties.isEmpty()) { // null out Properties if empty, so it does not create an empty map during serialization
            config.Properties = null;
        }

        // todo find way to just get part properties
        Class block = (Class)e;
        var associations2 = block.getPart().stream().map(Property::getAssociation).collect(Collectors.toList());
        List<Class> directLinks = associations2.stream()
                .filter(x -> x != null && x.getRelatedElement() != null && x.getRelatedElement().size() == 2)
                .map(x -> x.getRelatedElement().stream().findFirst())
                .filter(Optional::isPresent).map(Optional::get)
                .filter(x -> x != e) // only allow inbound links not outgoing links
                .filter(Class.class::isInstance).map(Class.class::cast)
                .collect(Collectors.toList());

        List<String> directLinkNames = directLinks.stream().map(NamedElement::getName).collect(Collectors.toList());

        if (!directLinkNames.isEmpty()) {
            config.DirectLinkNames = directLinkNames;
            config.DebugDirectLinkClassifiers = directLinks.stream().map(Classifier.class::cast).collect(Collectors.toList());
        }
        return config;
    }

    /**
     * Returns the parsed value of the property element. Returns "N/A" if no value can be parsed.
     * @param defaultValue the cameo property to be parsed.
     * @return A string representation of the cameo property.
     */
    private static String getParsedValue(ValueSpecification defaultValue) {
        if (defaultValue instanceof LiteralString) {
            return ((LiteralString) defaultValue).getValue();
        } else if (defaultValue instanceof LiteralReal) {
            return String.valueOf(((LiteralReal) defaultValue).getValue());
        } else if (defaultValue instanceof LiteralInteger) {
            return  String.valueOf(((LiteralInteger) defaultValue).getValue());
        } else if (defaultValue instanceof LiteralBoolean) {
            return String.valueOf(((LiteralBoolean) defaultValue).isValue());
        } else if (defaultValue instanceof ElementValue) {
            var element = ((ElementValue) defaultValue).getElement();
            if (element instanceof Class) {
                // default value is the name of a Block
                return ((Class) element).getName();
            } else if (element instanceof ValueSpecification) {
                // default value is a Literal
                return getParsedValue((ValueSpecification) element);
            } else if (element instanceof Property) {
                defaultValue = ((Property)element).getDefaultValue();
                if(defaultValue != null) {
                    // default value is the default value of another property
                    return getParsedValue(defaultValue);
                }
            }
        }
        return "N/A";
    }

    public static String toString(CameoDiagramDataConfig config) {
        return XmlSerializationUtils.toString(config, CameoElementConfig.class, CameoDiagramDataConfig.class);
    }
}
