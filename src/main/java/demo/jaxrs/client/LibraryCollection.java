package demo.jaxrs.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by prindu on 08/10/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryCollection {

	@XmlElement
	private Library[] libraries;

	public Library[] getLibraries() {
		return libraries;
	}

	public void setLibraries(Library[] libraries) {
		this.libraries = libraries;
	}
}



