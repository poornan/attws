package demo.jaxrs.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by ananthaneshan on 10/8/14.
 */
public class Marshal {
	public static <T> T unmarshal( Class<T> docClass, InputStream inputStream )
			throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance( packageName );
		Unmarshaller u = jc.createUnmarshaller();
		JAXBElement<T> doc = (JAXBElement<T>)u.unmarshal( inputStream );
		return doc.getValue();
	}
}
