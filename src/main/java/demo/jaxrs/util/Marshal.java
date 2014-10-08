package demo.jaxrs.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by ananthaneshan on 10/8/14.
 */
public class Marshal {
	public static <T> T unmarshal( Class<T> docClass, InputStream inputStream )
			throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance( docClass );
		Unmarshaller u = jc.createUnmarshaller();
		T doc = (T)u.unmarshal( inputStream );
		return doc;
	}
}
