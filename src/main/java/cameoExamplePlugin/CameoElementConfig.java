package cameoExamplePlugin;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Classifier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CameoElementConfig {

    public String Name;
    public String Id;
    public List<String> DirectLinkNames;
    public String ParentName;
    public Map<String, String> Properties;

    @XmlTransient
    public Classifier DebugThisClassifier;

    @XmlTransient
    public Classifier DebugParentClassifier;

    @XmlTransient
    public List<Classifier> DebugDirectLinkClassifiers = new ArrayList<>();
}
