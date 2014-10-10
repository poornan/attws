package demo.jaxrs.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by ananthaneshan on 10/8/14.
 */
public class Marshal {
	public static <T> T unmarshal(Class<T> xmlType, InputStream inputStream)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlType);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		T doc = (T) unmarshaller.unmarshal(inputStream);
		return doc;
	}
}
