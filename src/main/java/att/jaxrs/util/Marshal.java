package att.jaxrs.util;

import org.apache.http.HttpResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ananthaneshan on 10/8/14.
 */
public class Marshal {
	public static <T> T unmarshal(Class<T> xmlType, InputStream inputStream)
			throws JAXBException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder out = new StringBuilder();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		String namespace = out.toString();

		System.out.println(namespace);
		namespace = namespace.replaceAll(Constants.DATA_SERVICE_XMLNS, "");
		System.out.println(namespace);
		InputStream stream = Util.getInputStreamFromString(namespace);
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlType);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		T doc = (T) unmarshaller.unmarshal(stream);
		return doc;
	}

	public static <T> T unmarshal(Class<T> xmlType, HttpResponse httpResponse)
			throws JAXBException {
		String namespace = "";
		try {
			namespace = Util.getStringFromInputStream(httpResponse.getEntity().getContent());
			System.out.println(namespace);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println(namespace);
		namespace = namespace.replaceAll(Constants.DATA_SERVICE_XMLNS, "");
		System.out.println(namespace);
		InputStream stream = Util.getInputStreamFromString(namespace);
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlType);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		T doc = (T) unmarshaller.unmarshal(stream);
		return doc;
	}

	public static <T> T unmarshal(Class<T> xmlType, String string)
			throws JAXBException {
		String namespace = "";

		namespace = new String(string);
		System.out.println(namespace);

		namespace = namespace.replaceAll(Constants.DATA_SERVICE_XMLNS, "");
		System.out.println(namespace);

		InputStream stream = Util.getInputStreamFromString(namespace);
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlType);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		T doc = (T) unmarshaller.unmarshal(stream);
		return doc;
	}
}
