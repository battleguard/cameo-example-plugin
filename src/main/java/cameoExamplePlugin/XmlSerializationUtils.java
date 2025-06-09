package cameoExamplePlugin;

import org.apache.logging.log4j.LogManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class XmlSerializationUtils {
    public static String toString(Object config, Class<?>... classes) {
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classes);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(config, sw);
        } catch (Exception e) {
            LogManager.getLogger(XmlSerializationUtils.class).error("Failed to serialize object: ", e);
        }
        return sw.toString();
    }
}
